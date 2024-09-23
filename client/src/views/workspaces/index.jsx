import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Spinner, Alert, Button, Modal, Form } from 'react-bootstrap';
import WorkspacesTable from '../tables/Workspaces/WorkspacesTable'; // Adjust the import path if necessary
import WorkspaceService from 'services/WorkspaceService'; // Import the service

const WorkspaceComponent = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isHovered, setIsHovered] = useState(false); // For hover effect on the Add button
  const [showModal, setShowModal] = useState(false); // For modal visibility
  const [newWorkspaceName, setNewWorkspaceName] = useState(''); // Store the new workspace name

  useEffect(() => {
    const fetchWorkspaces = async () => {
      const result = await WorkspaceService.fetchWorkspaces();
      if (result.success) {
        setWorkspaces(result.data);
      } else {
        setError(result.message);
      }
      setLoading(false);
    };

    fetchWorkspaces();
  }, []);

  const handleAddWorkspace = () => {
    // Open the modal to create a new workspace
    setShowModal(true);
  };

  const handleCreateWorkspace = async () => {
    const result = await WorkspaceService.createWorkspace(newWorkspaceName);
    if (result.success) {
      // Refresh the workspaces after adding
      const refreshedResult = await WorkspaceService.fetchWorkspaces();
      setWorkspaces(refreshedResult.data);

      // Close the modal after creating the workspace
      setShowModal(false);
      setNewWorkspaceName('');
    } else {
      setError(result.message);
    }
  };

  const handleWorkspaceDeleted = (id) => {
    // Optimistically update the state to remove the deleted workspace
    setWorkspaces((prevWorkspaces) => prevWorkspaces.filter((workspace) => workspace.id !== id));
  };

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
              onClick={handleAddWorkspace}
            >
              {isHovered ? 'Add Workspace' : '+'}
            </Button>
          </Col>
        </Row>
        <Row>
          <Card className="widget-focus-lg">
            <Card.Header>
              <Card.Title as="h5">All Workspaces</Card.Title>
              <span className="d-block m-t-5">All of your workspaces, in one place</span>
            </Card.Header>
            <Card.Body className="px-0 py-2">
              <WorkspacesTable workspaces={workspaces} onWorkspaceDeleted={handleWorkspaceDeleted} />
            </Card.Body>
          </Card>
        </Row>
      </Col>

      {/* Modal for creating new workspace */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Create New Workspace</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="workspaceName">
              <Form.Label>Workspace Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter workspace name"
                value={newWorkspaceName}
                onChange={(e) => setNewWorkspaceName(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleCreateWorkspace}>
            Create Workspace
          </Button>
        </Modal.Footer>
      </Modal>
    </React.Fragment>
  );
};

export default WorkspaceComponent;
