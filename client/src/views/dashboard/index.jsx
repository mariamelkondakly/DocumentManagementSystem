import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import WorkspacesTable from '../tables/Workspaces/WorkspacesTable'; // Adjust the import path if necessary
import WorkspaceService from '../../services/WorkspaceService'; // Import the workspace service

const DashDefault = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [totalWorkspaces,setTotalWorkspaces]=useState(0);

  useEffect(() => {
    const fetchWorkspaces = async () => {
      const result = await WorkspaceService.fetchWorkspaces();
      if (result.success) {
        setTotalWorkspaces(result.noOfElements);
        setWorkspaces(result.data.slice(0, 5));
      } else {
        setError(result.message);
      }
      setLoading(false);
    };

    fetchWorkspaces();
  }, []);

  const handleWorkspaceDeleted = (id) => {
    // Optimistically update the state to remove the deleted workspace
    setWorkspaces((prevWorkspaces) => prevWorkspaces.filter((workspace) => workspace.id !== id));
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
              <span className="d-block m-t-5">These are the five most recently created workspaces</span>
            </Card.Header>
            <Card.Body className="px-0 py-2">
              <WorkspacesTable workspaces={workspaces}  onWorkspaceDeleted={handleWorkspaceDeleted} />
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
                  <h3 className="f-w-300">{totalWorkspaces}</h3>
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
