import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WorkspaceService from '../../../services/WorkspaceService';
import MoveDocumentModal from './MoveModal'; // Add the move modal component
import DocumentsService from '../../../services/DocumentsService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFile } from '@fortawesome/free-solid-svg-icons';
import { Dropdown, Modal, Form, Button } from 'react-bootstrap'; // Import Dropdown from react-bootstrap

const DocumentsRow = ({ document, onDocumentDeleted, onDocumentMoved, refresh }) => {
  const [showMoveModal, setShowMoveModal] = useState(false);
  const [selectedDocument, setSelectedDocument] = useState(null);
  const [workspaces, setWorkspaces] = useState([]);
  const [tags, setTags] = useState([]);
  const [showContentModal, setShowContentModal] = useState(false);
  const [showRenameModal, setShowRenameModal] = useState(false); // For document upload modal visibility
  const [tempDocumentName, setTempDocumentName] = useState(''); // Temporary storage for directory name while editing
  const [modalContent, setModalContent] = useState('');
  const [isImage, setIsImage] = useState(false);
  const [showPropertiesModal, setShowPropertiesModal] = useState(false);
  const [showAddTagsModal, setShowAddTagsModal] = useState(false);
  const [newTag, setNewTag] = useState('');
  const [error, setError] = useState(null);

  const handleDelete = async (id) => {
    const isConfirmed = window.confirm('Are you sure you want to delete this Document? This action cannot be undone.');
    if (isConfirmed) {
      const result = await DocumentsService.deleteDocument(id);
      if (result.success) {
        console.log('deleted');
        onDocumentDeleted(id); // Optimistically remove from UI
      } else {
        alert('Failed to delete document');
      }
    }
  };

  const handleViewClick = async (id) => {
    const result = await DocumentsService.previewDocument(id);
    if (result.success) {
      const { data: blob } = result; // Only destructure blob
      const url = window.URL.createObjectURL(blob);
      // Check if the content type is an image
      if (document.type.startsWith('image/')) {
        setModalContent(url);
        setIsImage(true);
      } else if (document.type === 'application/pdf') {
        // Handle PDF display differently
        setModalContent(url); // Use the URL to display in an iframe
        setIsImage(false);
      } else {
        alert('Unsupported file type');
        return;
      }

      setShowContentModal(true); // Show modal
    } else {
      alert(result.message);
    }
  };
  const handleCloseModal = () => {
    setShowContentModal(false);
    setModalContent(''); // Reset content when closing modal
    setIsImage(false); // Reset image state
  };

  const fetchWorkspaces = async () => {
    const result = await WorkspaceService.fetchWorkspaces();
    if (result.success) {
      setWorkspaces(result.data);
    } else {
      alert(result.message);
    }
  };

  const handleMove = (document) => {
    setSelectedDocument(document);
    fetchWorkspaces();
    setShowMoveModal(true); // Show modal when move button is clicked
  };

  const handleCloseMoveModal = () => {
    refresh();
    setShowMoveModal(false);
    setSelectedDocument(null); // Reset selected document after modal is closed
  };

  const handleDocumentMoved = async (parentId) => {
    const result = await DocumentsService.moveDocument(selectedDocument.id, parentId.id);
    if (result === 'File successfully moved') {
      handleCloseMoveModal();
    } else {
      alert('Failed to move document');
    }
  };

  const handleDownload = async () => {
    const result = await DocumentsService.downloadDocument(document.id, document.name);
    if (!result.success) {
      alert(result.message);
    }
  };

  const handleRename = async (id) => {
    const result = await DocumentsService.updateDocumentName(id, tempDocumentName);
    if (result.success) {
      handleCloseRenameModal();
    } else {
      setError(result.message);
    }
  };
  const handleNewTags = async () => {
    const result = await DocumentsService.addNewTags(document.id, tags);
    if (result.success) {
      handleCloseAddTagsModal();
    } else {
      setError(result.message);
    }
  };

  const addTagToList = () => {
    if (newTag.trim() && !tags.includes(newTag)) {
      // Check if the tag is not empty and not already in the list
      setTags([...tags, newTag]); // Create a new array with the existing tags and the new tag
      setNewTag(''); // Clear the input after adding the tag
    }
  };

  const handleShowRenameModal = () => {
    setTempDocumentName(document.name); // Set the current document name before showing the modal
    setShowRenameModal(true);
  };
  const handleCloseRenameModal = () => {
    refresh();
    setShowRenameModal(false);
  };

  const handleShowAddTagsModal = () => {
    setTags(document.tags); // Set the current document name before showing the modal
    setShowAddTagsModal(true);
  };
  const handleCloseAddTagsModal = async () => {
    refresh();
    setShowAddTagsModal(false);
  };

  const handleShowProperties = () => {
    setShowPropertiesModal(true);
  };
  const handleCloseProperties = () => {
    setShowPropertiesModal(false);
  };

  return (
    <>
      <tr>
        <td style={{ cursor: 'pointer' }} onClick={() => handleViewClick(document.id)}>
          <FontAwesomeIcon className=" m-2 mb-0 ml-0" icon={faFile} size="xl" style={{ color: '#B197FC' }} />
          {document.name}
        </td>
        <td>
          <h6 className="text-muted">{document.createdAt}</h6>
        </td>
        <td>
          <h6 className="text-muted">{document.lastAccessedAt}</h6>
        </td>
        <td>
          <Dropdown>
            <Dropdown.Toggle className="btn-theme" id="dropdown-basic">
              Options
            </Dropdown.Toggle>

            <Dropdown.Menu>
              <Dropdown.Item onClick={() => handleShowProperties(document.id)}>Show Properties</Dropdown.Item>
              <Dropdown.Item onClick={() => handleMove(document)}>Move</Dropdown.Item>
              <Dropdown.Item onClick={handleShowRenameModal}>Rename</Dropdown.Item>
              <Dropdown.Item onClick={() => handleDownload(document.id)}>Download</Dropdown.Item>
              <Dropdown.Item onClick={() => handleShowAddTagsModal()}>Add tags</Dropdown.Item>
              <Dropdown.Item className="text-danger" onClick={() => handleDelete(document.id)}>
                Delete
              </Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </td>
      </tr>

      {/* Move Modal */}
      {showMoveModal && (
        <MoveDocumentModal
          show={showMoveModal}
          handleClose={handleCloseMoveModal}
          workspaces={workspaces}
          moveToDirectory={handleDocumentMoved}
        />
      )}

      {/* Modal for Document Preview */}
      <Modal show={showContentModal} onHide={handleCloseModal}>
        <Modal.Header closeButton className="d-flex align-items-center">
          <Modal.Title className=" flex-grow-1">Document Preview</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {isImage ? (
            <img src={modalContent} alt="Document Preview" style={{ width: '100%', height: 'auto' }} />
          ) : (
            <iframe
              src={modalContent}
              title="Document Preview"
              style={{ width: '100%', height: '500px', border: 'none' }} // Set height in pixels and remove border
            />
          )}
        </Modal.Body>
        <Modal.Footer>
          <button className="btn btn-secondary" onClick={handleCloseModal}>
            Close
          </button>
        </Modal.Footer>
      </Modal>

      <Modal show={showPropertiesModal} onHide={handleCloseProperties}>
        <Modal.Header closeButton>
          <Modal.Title>Properties</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Name: <span className="text-info">{document.name}</span>{' '}
          </p>
          <p>
            Created at: <span className="text-info">{document.createdAt}</span>{' '}
          </p>
          <p>
            Last modified at: <span className="text-info">{document.lastAccessedAt}</span>{' '}
          </p>
          <p>
            Size: <span className="text-info">{document.size}KB</span>{' '}
          </p>
          <p>
            Type: <span className="text-info">{document.type}</span>{' '}
          </p>
          <p>
            Tags:
            {document.tags && document.tags.length > 0 ? (
              document.tags.map((tag, index) => (
                <span key={index} className="text-info">
                  {tag}
                  {index < document.tags.length - 1 ? ', ' : ''}
                </span>
              ))
            ) : (
              <span className="text-info"> No tags</span>
            )}
          </p>
        </Modal.Body>
        <Modal.Footer>
          <button className="btn btn-secondary" onClick={handleCloseProperties}>
            Close
          </button>
        </Modal.Footer>
      </Modal>

      <Modal show={showRenameModal} onHide={handleCloseRenameModal}>
        <Modal.Header closeButton>
          <Modal.Title className="mb-3">Rename document</Modal.Title>
          <p className="text-black-50">Make sure you don't change the extension</p>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="documentName">
              <Form.Label>New name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter new document name"
                value={tempDocumentName}
                onChange={(e) => setTempDocumentName(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseRenameModal}>
            Cancel
          </Button>
          <Button variant="primary" onClick={() => handleRename(document.id)}>
            Rename Document
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showAddTagsModal} onHide={handleCloseAddTagsModal}>
        <Modal.Header closeButton>
          <Modal.Title className="mb-3">Add Tags</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {/* Display existing tags */}
          <div className="mb-2">
            {tags.length > 0 ? (
              <div>
                {tags.map((tag, index) => (
                  <span key={index} className="badge bg-dark me-1">
                    {tag}
                  </span> // Using Bootstrap badge for styling
                ))}
              </div>
            ) : (
              <span>No tags added yet.</span>
            )}
          </div>
          <Form>
            <Form.Group controlId="documentName" className="d-flex align-items-center">
              <Form.Control
                type="text"
                placeholder="Enter a new tag"
                value={newTag}
                onChange={(e) => setNewTag(e.target.value)}
                className="me-2" // Add margin to the right
              />
              <Button className="btn btn-theme" onClick={addTagToList}>
                Add
              </Button>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button className="btn-theme2" onClick={handleCloseAddTagsModal}>
            Cancel
          </Button>
          <Button className="btn-theme" onClick={handleNewTags}>
            Done
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default DocumentsRow;
