// DashDefault.js
import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import WorkspacesTable from '../tables/WorkspacesTable'; // Adjust the import path if necessary
import apiClient from '../../services/Interceptor'; // Adjust the import path if necessary

const DashDefault = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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
        const token=localStorage.getItem('token');
        const response = await apiClient.get('', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        let fetchedWorkspaces = response.data.content;

        fetchedWorkspaces.sort((a, b) => parseDate(b.createdAt) - parseDate(a.createdAt));

        setWorkspaces(fetchedWorkspaces.slice(0, 5));
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
      setWorkspaces(workspaces.filter((workspace) => workspace.id !== id));
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
              <span className="d-block m-t-5">
                These are the five most recently created workspaces
              </span>
            </Card.Header>
            <Card.Body className="px-0 py-2">
              {/* Use the reusable WorkspacesTable component */}
              <WorkspacesTable workspaces={workspaces} onDelete={handleDelete} />
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
