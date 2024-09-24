import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DirectoriesService from '../../../services/DirectoriesService'; // Adjust the import path as needed
import DirectoriesTable from '../../tables/Directories/DirectoriesTable';
import { Form, Modal, Button, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';

const Directories = () => {
  const {parentId } = useParams(); // Get the workspace ID from the URL
  const [directoryName, setDirectoryName] = useState('');
  const [directories, setDirectories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newDirectoryName, setNewDirectoryName] = useState(''); // Store the new workspace name
  const [showModal, setShowModal] = useState(false); // For modal visibility
  const [isHovered, setIsHovered] = useState(false); // For hover effect on the Add button

  useEffect(() => {
    const fetchSubDirectories = async () => {
        const result = await DirectoriesService.viewSubDirectories(parentId);
        if (result.success) {
            setDirectories(result.data[1].content);
            setDirectoryName(result.data[0]);
        } else {
            setError(result.message);
        }
        setLoading(false);
    };

    fetchSubDirectories();
}, [parentId]); // Ensure parentId is in the dependency array


  const handleDirectoryDeleted = (id) => {
    // Optimistically update the state to remove the deleted workspace
    setDirectories((prevDirectories) => prevDirectories.filter((directory) => directory.id !== id));
  };
  const handleMove = async () => {
    const result = await DirectoriesService.viewSubDirectories(parentId); // Call the service method
    if (result.success) {
      setDirectories(result.data[1].content);
      setDirectoryName(result.data[0]);
    } else {
      setError(result.message);
    }
  };
  const handleAddDirectory = () => {
    // Open the modal to create a new workspace
    setShowModal(true);
  };

  const handleCreateDirectory = async () => {
    const result = await DirectoriesService.createSubDirectory(parentId,newDirectoryName);
    if (result.success) {
      // Refresh the workspaces after adding
      const refreshedResult = await DirectoriesService.viewSubDirectories(parentId);
      setDirectories(refreshedResult.data[1].content);

      // Close the modal after creating the workspace
      setShowModal(false);
      setNewDirectoryName('');
    } else {
      setError(result.message);
    }
  };
  if (!parentId) {
        // Return a loader or placeholder until workspaceId is available
        return <p>Loading directory...</p>;
    }

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <React.Fragment>
      <Col>
        <Row className="mb-4">
          <Col>
            <Button
              className="add-workspace-button btn btn-theme"
              onMouseEnter={() => setIsHovered(true)}
              onMouseLeave={() => setIsHovered(false)}
              onClick={handleAddDirectory}
            >
              {isHovered ? 'Add Directory' : '+'}
            </Button>
          </Col>
        </Row>
        <Row>
          <Card className="widget-focus-lg">
            <Card.Header>
              <Card.Title as="h5">{directoryName}</Card.Title>
            </Card.Header>
            <Card.Body className="px-0 py-2">
              <DirectoriesTable directories={directories} onDirectoriesDeleted={handleDirectoryDeleted} onDirectoryMoved={handleMove} />
            </Card.Body>
          </Card>
        </Row>
      </Col>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Create New Directory</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="directoryName">
              <Form.Label>Directory Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Directory name"
                value={newDirectoryName}
                onChange={(e) => setNewDirectoryName(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={ handleCreateDirectory}>
            Create Workspace
          </Button>
        </Modal.Footer>
      </Modal>
    </React.Fragment>
  );
};

export default Directories;
