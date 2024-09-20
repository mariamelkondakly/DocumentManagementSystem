import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Spinner, Alert, Button, Modal, Form } from 'react-bootstrap';
import WorkspacesTable from '../tables/WorkspacesTable'; // Adjust the import path if necessary
import apiClient from '../../services/Interceptor'; // Adjust the import path if necessary

const WorkspaceComponent = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isHovered, setIsHovered] = useState(false); // For hover effect on the Add button
  const [showModal, setShowModal] = useState(false); // For modal visibility
  const [newWorkspaceName, setNewWorkspaceName] = useState(''); // Store the new workspace name

  useEffect(() => {
    const parseDate = (dateStr) => {
      const parts = dateStr.split(' ');
      if (parts.length !== 2) {
        console.error(`Invalid format: ${dateStr}`);
        return new Date(NaN); // Return an invalid date
      }

      const [day, month, year] = parts[0].split('-');
      const [hour, minute, second] = parts[1].split(':');

      if (!day || !month || !year || !hour || !minute || !second) {
        console.error(`Incomplete date parts: ${dateStr}`);
        return new Date(NaN); // Return an invalid date
      }

      // Convert to ISO 8601 format: "YYYY-MM-DDTHH:MM:SSZ"
      const isoDateStr = `${year}-${month}-${day}T${hour}:${minute}:${second}Z`;
      const dateObj = new Date(isoDateStr);

      if (isNaN(dateObj.getTime())) {
        console.error(`Invalid date conversion: ${dateStr} -> ${isoDateStr}`);
      }

      return dateObj;
    };

    // Fetch workspaces data
    const fetchWorkspaces = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await apiClient.get('', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        let fetchedWorkspaces = response.data.content;

        fetchedWorkspaces.sort((a, b) => parseDate(b.createdAt) - parseDate(a.createdAt));

        setWorkspaces(fetchedWorkspaces);
      } catch (err) {
        setError('Failed to fetch workspaces');
      } finally {
        setLoading(false);
      }
    };

    fetchWorkspaces();
  }, []);

  const handleAddWorkspace = () => {
    // Open the modal to create a new workspace
    setShowModal(true);
  };

  const handleCreateWorkspace = async () => {
    try {
      const token = localStorage.getItem('token');
      // Send POST request to create new workspace
      await apiClient.post(`/createWorkspace/${newWorkspaceName}`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      // Refresh the workspaces after adding
      const response = await apiClient.get('', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setWorkspaces(response.data.content);

      // Close the modal after creating the workspace
      setShowModal(false);
      setNewWorkspaceName('');
    } catch (err) {
      setError('Failed to create new workspace');
    }
  };

  const handleDelete = async (id) => {
    try {
      await apiClient.delete(`/${id}`);
      setWorkspaces(workspaces.filter((workspace) => workspace.id !== id));
    } catch (err) {
      setError('Failed to delete workspace');
    }
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <React.Fragment>
      <Col>
        <Row className="mb-4">
          <Col>
            <Button
              className="add-workspace-button"
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
              <WorkspacesTable workspaces={workspaces} onDelete={handleDelete} />
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
