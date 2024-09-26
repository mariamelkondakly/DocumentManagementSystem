import React from 'react';
import { FaFolder } from 'react-icons/fa';
import {useNavigate } from 'react-router-dom';
import WorkspaceService from '../../../services/WorkspaceService'; // Import service

const WorkspaceRow = ({ workspace, onWorkspaceDeleted }) => {
  const navigate = useNavigate();

  const handleDelete = async (id) => {
    const isConfirmed = window.confirm('Are you sure you want to delete this workspace? This action cannot be undone.');

    if (isConfirmed) {
      const result = await WorkspaceService.deleteWorkspace(id);
      if (result.success) {
        onWorkspaceDeleted(id); // Optimistically remove from UI
      } else {
        alert('Failed to delete workspace');
      }
    }
  };

  const handleViewClick = async (id) => {
    navigate(`/rootDirectories/${id}`); // Navigate to the root directories page
  };

  return (
    <tr>
      <td onClick={()=>handleViewClick(workspace.id)} style={{ cursor: 'pointer' }}>
        <FaFolder className="text-c-green me-2" style={{ fontSize: '24px' }} />
        {workspace.name}
      </td>
      <td>
        <h6 className="text-muted">{workspace.createdAt}</h6>
      </td>
      <td>
        {/* <button 
          className="btn btn-theme"
           // Call the handleViewClick function
        >
          View
        </button> */}
        <button 
          className="btn  btn-theme2"
          onClick={() => handleDelete(workspace.id)}
        >
          Delete
        </button>
      </td>
    </tr>
  );
};

export default WorkspaceRow;
