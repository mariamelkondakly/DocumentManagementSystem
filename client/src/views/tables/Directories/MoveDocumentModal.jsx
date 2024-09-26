import React, { useState } from 'react';
import { Modal, Button, ListGroup, Collapse } from 'react-bootstrap';
import DirectoriesService from 'services/DirectoriesService';

const MoveModal = ({ show, handleClose, workspaces, moveToDirectory }) => {
  const [openDirectories, setOpenDirectories] = useState({}); // Track open directories by unique key
  const [directories, setDirectories] = useState({}); // Store fetched directories by their parentId or workspaceId
  const [depth,setDepth]=useState(1);

  // Helper to manage toggle state for root workspaces
  const toggleRootDirectory = async (workspaceId) => {
    try {
      const isAlreadyOpen = openDirectories[`workspace-${workspaceId}`];

      if (isAlreadyOpen) {
        setOpenDirectories((prevState) => ({
          ...prevState,
          [`workspace-${workspaceId}`]: false, // Close the workspace
        }));
        return;
      }

      if (!directories[workspaceId]) {
        const response = await DirectoriesService.viewRootDirectories(workspaceId);
        setDirectories((prev) => ({
          ...prev,
          [workspaceId]: response.data[1]?.content || [], // Store fetched directories
        }));
      }

      setOpenDirectories((prevState) => ({
        ...prevState,
        [`workspace-${workspaceId}`]: true, // Open the clicked workspace
      }));

    } catch (error) {
      console.error('Error fetching directories:', error);
    }
  };

  // Toggle subdirectories for a parent directory
  const toggleSubDirectory = async (dirId) => {
    const uniqueKey = `dir-${dirId}`;

    try {
      const isAlreadyOpen = openDirectories[uniqueKey];

      if (isAlreadyOpen) {
        setOpenDirectories((prevState) => ({
          ...prevState,
          [uniqueKey]: false, // Close the subdirectory
        }));
        return;
      }

      if (!directories[dirId]) {
        setDepth(depth+1);
        const response = await DirectoriesService.viewSubDirectories(dirId);
        console.log(response);
        setDirectories((prev) => ({
          ...prev,
          [dirId]: response.data[1]?.content || [], // Store fetched subdirectories
        }));
      } 

      setOpenDirectories((prevState) => ({
        ...prevState,
        [uniqueKey]: true, // Open the clicked subdirectory
      }));

    } catch (error) {
      console.error('Error fetching subdirectories:', error);
    }
  };

  // Render subdirectories recursively
  const renderSubDirectories = (children) => {
    if (!Array.isArray(children)) return null;

    return children.map((dir) => (
      <React.Fragment key={dir.id}>
        <ListGroup.Item className="d-flex justify-content-between align-items-center" style={{ paddingLeft: `${depth * 1.5}rem` }}>
          <span onClick={() => toggleSubDirectory(dir.id)} style={{ cursor: 'pointer' }}>
            {dir.name}
          </span>
          <Button variant="primary" size="sm" onClick={() => moveToDirectory(dir.id)}>
            Select
          </Button>
        </ListGroup.Item>
        <Collapse in={openDirectories[`dir-${dir.id}`]}>
          <ListGroup className="pl-4">
            {directories[dir.id] && renderSubDirectories(directories[dir.id])}
          </ListGroup>
        </Collapse>
      </React.Fragment>
    ));
  };

  const renderRootDirectories = (workspaceId) => {
    if (!directories[workspaceId]) return null;
    return directories[workspaceId].map((dir) => (
    
      <React.Fragment key={dir.id}>
        <ListGroup.Item className="d-flex justify-content-between align-items-center" style={{ paddingLeft: `2.5rem` }}>
          <span onClick={() => toggleSubDirectory(dir.id)} style={{ cursor: 'pointer' }}>
            {dir.name}
          </span>
          <Button variant="primary" size="sm" onClick={() => moveToDirectory(dir.id)}>
            Select
          </Button>
        </ListGroup.Item>
        <Collapse in={openDirectories[`dir-${dir.id}`]}>
          <ListGroup className="pl-4">
            {directories[dir.id] && renderSubDirectories(directories[dir.id])}
          </ListGroup>
        </Collapse>
      </React.Fragment>
    ));
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Select a Directory</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ListGroup>
          {workspaces.map((workspace) => (
            <React.Fragment key={workspace.id}>
              <ListGroup.Item className="d-flex justify-content-between align-items-center">
                <span onClick={() => toggleRootDirectory(workspace.id)} style={{ cursor: 'pointer' }}>
                  {workspace.name}
                </span>
              </ListGroup.Item>
              <Collapse in={openDirectories[`workspace-${workspace.id}`]}>
                <ListGroup className="pl-4">
                  {renderRootDirectories(workspace.id)}
                </ListGroup>
              </Collapse>
            </React.Fragment>
          ))}
        </ListGroup>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default MoveModal;
