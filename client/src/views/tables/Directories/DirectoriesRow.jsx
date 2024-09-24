import React, { useState } from 'react';
import { FaFolder } from 'react-icons/fa';
import {useNavigate} from 'react-router-dom';
import DirectoriesService from '../../../services/DirectoriesService';
import WorkspaceService from '../../../services/WorkspaceService';
import MoveModal from './MoveModal'; // Add the move modal component

const DirectoriesRow = ({ directory, onDirectoryDeleted, onDirectoryMoved}) => {
  const [showMoveModal, setShowMoveModal] = useState(false);
  const [selectedDirectory, setSelectedDirectory] = useState(null);
  const [workspaces, setWorkspaces] = useState([]);
  const navigate = useNavigate();




  const handleDelete = async (id) => {
    const isConfirmed = window.confirm('Are you sure you want to delete this Directory? This action cannot be undone.');
    if (isConfirmed) {
      const result = await DirectoriesService.deleteDirectory(id);
      if (result.success) {
        onDirectoryDeleted(id); // Optimistically remove from UI
      } else {
        alert('Failed to delete directory');
      }
    }
  };
  const handleViewClick = async (id) => {
    navigate(`/subDirectories/${id}`); // Navigate to the root directories page
  };

  const fetchWorkspaces = async () => {
    const result = await WorkspaceService.fetchWorkspaces();
    if (result.success) {
      setWorkspaces(result.data);
    } else {
      setError(result.message);
    }
  };

  const handleMove = (directory) => {
    setSelectedDirectory(directory);
    fetchWorkspaces();
    setShowMoveModal(true); // Show modal when move button is clicked
  };

  const handleCloseMoveModal = () => {
    setShowMoveModal(false);
    setSelectedDirectory(null); // Reset selected directory after modal is closed
  };

  const handleDirectoryMoved = async (newParentDirectory) => {
    // Call API to move the directory
    const result = await DirectoriesService.moveDirectory(selectedDirectory.id, newParentDirectory.id,true,true);

    if (result==="Directory updated successfully") {
      onDirectoryMoved();
      handleCloseMoveModal();
      alert(`Directory moved to ${newParentDirectory.name}`);

    } else {
      alert('Failed to move directory');
    }
  };

  return (
    <>
      <tr>
        <td>
          <FaFolder className="me-2" style={{ fontSize: '24px', color: "#f88379" }} />
          {directory.name}
        </td>
        <td>
          <h6 className="text-muted">{directory.createdAt}</h6>
        </td>
        <td>
          <h6 className="text-muted">{directory.lastAccessedAt}</h6>
        </td>
        <td>
          <button onClick={()=>handleViewClick(directory.id)} className="btn btn-theme">
            View
          </button>
          <button 
            className="btn btn-theme3"
            onClick={() => handleMove(directory)} // Handle move button click
          >
            Move
          </button>
          <button 
            className="btn btn-theme2"
            onClick={() => handleDelete(directory.id)}
          >
            Delete
          </button>
        </td>
      </tr>

      {/* Move Modal */}
      {showMoveModal && (
        <MoveModal 
          show={showMoveModal} 
          handleClose={handleCloseMoveModal} 
          workspaces={workspaces}
          moveToDirectory={handleDirectoryMoved} 
        />
      )}
    </>
  );
};

export default DirectoriesRow;
