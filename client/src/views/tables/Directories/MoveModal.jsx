import React, { useState } from 'react';
import { Modal, Button, ListGroup, Collapse } from 'react-bootstrap';
import DirectoriesService from 'services/DirectoriesService';

const MoveModal = ({ show, handleClose, workspaces, moveToDirectory }) => {

  const [openDirectories, setOpenDirectories] = useState({});
  const [directories, setDirectories]=useState({});

  const toggleDirectory = async (workspaceId, dirId) => {
    const response= await DirectoriesService.viewRootDirectories(workspaceId);
    setDirectories(response.data[1].content);
    console.log(directories);
    setOpenDirectories((prevState) => ({
      ...prevState,
      [`${workspaceId}-${dirId}`]: !prevState[`${workspaceId}-${dirId}`]
    }));
  };

  const renderDirectories = (workspaceId) => {
    if (!Array.isArray(directories)) return null; // Make sure directories is an array
    return directories.map((dir) => (
        <React.Fragment key={dir.id}>
        <ListGroup.Item className="d-flex justify-content-between align-items-center">
          <span onClick={() => toggleDirectory(workspaceId, dir.id)} style={{ cursor: 'pointer' }}>
            {dir.name}
          </span>
          <Button variant="primary" size="sm" onClick={() => moveToDirectory(dir)}>
            Select
          </Button>
        </ListGroup.Item>
        <Collapse in={openDirectories[`${workspaceId}-${dir.id}`]}>
          <ListGroup className="pl-4">
            {/* Recursively render child directories if they exist */}
            {dir.children && renderDirectories(dir.children, workspaceId)}
          </ListGroup>
        </Collapse>
      </React.Fragment>
    ));
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Select Workspace and Directory</Modal.Title>
      </Modal.Header>
      <Modal.Body>
      <ListGroup>
  {workspaces.map((workspace) => (
    <React.Fragment key={workspace.id}>
      <ListGroup.Item className="d-flex justify-content-between align-items-center">
        <span onClick={() => toggleDirectory(workspace.id, 'root')} style={{ color:'#000000',cursor: 'pointer' }} className='primary'>
          {workspace.name}
        </span>
        <Button variant="primary" size="sm" onClick={() => moveToDirectory(workspace)}>
          Select
        </Button>
      </ListGroup.Item>
      <Collapse in={openDirectories[`${workspace.id}-root`]}>
        <ListGroup className="pl-4">
          {/* Safeguard workspace.directories */}
          {renderDirectories(directories, workspace.id)}
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
