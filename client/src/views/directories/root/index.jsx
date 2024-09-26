import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DirectoriesService from '../../../services/DirectoriesService'; // Adjust the import path as needed
import DirectoriesTable from '../../tables/Directories/DirectoriesTable';
import { Form, Modal, Button, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
  

const Directories = () => {
  const { workspaceId } = useParams(); // Get the workspace ID from the URL
  const [workspaceName, setWorkspaceName] = useState('');
  const [directories, setDirectories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newDirectoryName, setNewDirectoryName] = useState(''); // Store the new directory name
  const [showModal, setShowModal] = useState(false); // For modal visibility
  const [isHovered, setIsHovered] = useState(false); // For hover effect on the Add button
  const [originalRoot, setOriginalRoot] = useState(true);

  useEffect(() => {
    const fetchRootDirectories = async () => {
      const result = await DirectoriesService.viewRootDirectories(workspaceId); // Call the service method
      if (result.success) {
        setDirectories(result.data[1].content);
        setWorkspaceName(result.data[0]);
      } else {
        setError(result.message);
      }
      setLoading(false);
    };

    fetchRootDirectories();
  }, [workspaceId]); // Depend on workspaceId

  const handleDirectoryDeleted = (id) => {
    setDirectories((prevDirectories) => prevDirectories.filter((directory) => directory.id !== id));
  };

  const handleMove = async () => {
    const result = await DirectoriesService.viewRootDirectories(workspaceId); // Call the service method
    if (result.success) {
      setDirectories(result.data[1].content);
      setWorkspaceName(result.data[0]);
    } else {
      setError(result.message);
    }
  };

  const handleAddDirectory = () => {
    setShowModal(true);
  };

  const handleCreateDirectory = async () => {
    const result = await DirectoriesService.createRootDirectory(workspaceId, newDirectoryName);
    if (result.success) {
      const refreshedResult = await DirectoriesService.viewRootDirectories(workspaceId);
      setDirectories(refreshedResult.data[1].content);
      setShowModal(false);
      setNewDirectoryName('');
    } else {
      setError(result.message);
    }
  };


  if (!workspaceId) {
    return <p>Loading workspace...</p>;
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
              {isHovered ? 'Add Directory' : <FontAwesomeIcon icon={faPlus} size="lg" style={{ color: "#ffffff" }}/>}
            </Button>
          </Col>
        </Row>
        <Row>
          <Card className="widget-focus-lg">
            <Card.Header>
            <Card.Title as="h5">{workspaceName}</Card.Title>

            </Card.Header>
            <Card.Body className="px-0 py-2">
              <DirectoriesTable directories={directories} documents={null} onDirectoriesDeleted={handleDirectoryDeleted} onDirectoryMoved={handleMove} isOriginalRoot={originalRoot} />
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
          <Button variant="primary" onClick={handleCreateDirectory}>
            Create Directory
          </Button>
        </Modal.Footer>
      </Modal>
    </React.Fragment>
  );
};

export default Directories;
