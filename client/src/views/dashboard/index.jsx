import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Table, Spinner, Alert } from 'react-bootstrap';
import WorkspaceRow from './WorkspaceRow'; // Adjust the import path if necessary
import apiClient from '../../services/Interceptor'; // Adjust the import path if necessary

const DashDefault = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch workspaces data
    const fetchWorkspaces = async () => {
      try {
        const response = await apiClient.get('');
        setWorkspaces(response.data.content);
        console.log(response);
      } catch (err) {
        setError('Failed to fetch workspaces');
      } finally {
        setLoading(false);
      }
    };

    fetchWorkspaces();
  }, []);

  const handleDelete = async (id) => {
    try {
      await apiClient.delete(`/${id}`);
      setWorkspaces(workspaces.filter(workspace => workspace.id !== id));
    } catch (err) {
      setError('Failed to delete workspace');
    }
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <React.Fragment>
      <Row>
        <Col md={6} xl={8}>
          <Card className="Recent-Workspaces widget-focus-lg">
            <Card.Header>
              <Card.Title as="h5">Recent Workspaces</Card.Title>
            </Card.Header>
            <Card.Body className="px-0 py-2">
              <Table responsive hover className="recent-workspaces">
                <thead>
                  <tr>
                    <th>Workspace Name</th>
                    <th>Date Created</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {workspaces.map(workspace => (
                    <WorkspaceRow 
                      key={workspace.id}
                      workspace={workspace}
                      onDelete={handleDelete}
                    />
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
        <Col md={6} xl={4}>
          <Card>
            <Card.Body className="border-bottom">
              <div className="row d-flex align-items-center">
                <div className="col-auto">
                  <i className="feather icon-folder f-30 text-c-green" />
                </div>
                <div className="col">
                  <h3 className="f-w-300">{workspaces.length}</h3>
                  <span className="d-block text-uppercase">Total Workspaces</span>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </React.Fragment>
  );
};

export default DashDefault;
