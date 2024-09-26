import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DirectoriesService from '../../../services/DirectoriesService';
import DirectoriesTable from '../../tables/Directories/DirectoriesTable';
import UploadDocument from '../../forms/UploadDocument';
import { Form, Modal, Button, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faArrowUpFromBracket } from '@fortawesome/free-solid-svg-icons';

const Directories = () => {
  const { parentId } = useParams(); // Get the directory ID from the URL
  const [directoryName, setDirectoryName] = useState('');
  const [directories, setDirectories] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newDirectoryName, setNewDirectoryName] = useState('');
  const [originalRoot, setOriginalRoot] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [isCreateHovered, setIsCreateHovered] = useState(false);
  const [isUploadHovered, setIsUploadHovered] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [tempDirectoryName, setTempDirectoryName] = useState('');

  // Fetching directories and documents based on the parent ID
  const fetchSubDirectories = async () => {
    try {
      const result = await DirectoriesService.viewSubDirectories(parentId);
      const docResult = await DirectoriesService.viewDocuments(parentId);
      if (result.success) {
        setDirectories(result.data[1].content);
        setDocuments(docResult.data.content);
        setDirectoryName(result.data[0]);
      } else {
        setError(result.message);
      }
    } catch (err) {
      setError('Error fetching directories. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSubDirectories();
  }, [parentId]);

  const handleDirectoryDeleted = (id) => {
    setDirectories((prevDirectories) =>
      prevDirectories.filter((directory) => directory.id !== id)
    );
    setDocuments((prevDocuments) =>
      prevDocuments.filter((document) => document.id !== id)
    );
  };

  const handleAddDirectory = () => {
    setShowModal(true);
  };

  const handleEditDirectory = (name) => {
    setTempDirectoryName(name);
    setIsEditing(true);
  };

  const handleSaveDirectoryName = async () => {
    const result = await DirectoriesService.updateDirectoryName(parentId, tempDirectoryName);
    if (result.success) {
      setDirectoryName(tempDirectoryName);
      setIsEditing(false);
    } else {
      setError(result.message);
    }
  };

  const handleCreateDirectory = async () => {
    try {
      const result = await DirectoriesService.createSubDirectory(parentId, newDirectoryName);
      if (result.success) {
        setNewDirectoryName('');
        setShowModal(false);
        await fetchSubDirectories(); // Refresh directories after adding
      } else {
        setError(result.message);
      }
    } catch (err) {
      setError('Error creating directory. Please try again.');
    }
  };

  const handleUploadDocument = () => {
    setShowUploadModal(true);
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <>
      <Col>
        <Row className="mb-4">
          <Col md={2}>
            <Button
              className="add-workspace-button btn btn-theme"
              onMouseEnter={() => setIsCreateHovered(true)}
              onMouseLeave={() => setIsCreateHovered(false)}
              onClick={handleAddDirectory}
            >
              {isCreateHovered ? 'Add Directory' : <FontAwesomeIcon icon={faPlus} size="lg" style={{ color: '#ffffff' }} />}
            </Button>
          </Col>
          <Col md={2}>
            <Button
              className="add-workspace-button btn btn-theme3"
              onMouseEnter={() => setIsUploadHovered(true)}
              onMouseLeave={() => setIsUploadHovered(false)}
              onClick={handleUploadDocument}
            >
              {isUploadHovered ? 'Upload Document' : <FontAwesomeIcon icon={faArrowUpFromBracket} size="lg" className="btn-icon" style={{ color: '#ffffff' }} />}
            </Button>
          </Col>
        </Row>
        <Row>
          <Card className="widget-focus-lg">
            <Card.Header>
              {isEditing ? (
                <Form>
                  <Form.Control
                    type="text"
                    value={tempDirectoryName}
                    onChange={(e) => setTempDirectoryName(e.target.value)}
                    className="mb-2"
                  />
                  <Button className="btn-theme" onClick={handleSaveDirectoryName}>Save</Button>
                  <Button className="btn-theme2" onClick={() => setIsEditing(false)}>Cancel</Button>
                </Form>
              ) : (
                <Card.Title as="h5" onClick={() => handleEditDirectory(directoryName)} style={{ cursor: 'pointer' }}>
                  {directoryName}
                  <p className="mt-3 text-black-50">Click the directory name to edit</p>
                </Card.Title>
              )}
            </Card.Header>
            <Card.Body className="px-0 py-2">
              <DirectoriesTable
                directories={directories}
                documents={documents}
                onDirectoriesDeleted={handleDirectoryDeleted}
                isOriginalRoot={originalRoot}
                refresh={fetchSubDirectories}
              />
            </Card.Body>
          </Card>
        </Row>
      </Col>

      {/* Modal for creating new directory */}
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
          <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleCreateDirectory}>Create Directory</Button>
        </Modal.Footer>
      </Modal>

      {/* Upload Document Modal */}
      <UploadDocument parentId={parentId} show={showUploadModal} onHide={() => setShowUploadModal(false)} />
    </>
  );
};

export default Directories;
