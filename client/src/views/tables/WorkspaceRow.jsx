import React from 'react';
import { FaFolder } from 'react-icons/fa';
import { Link } from 'react-router-dom';

const WorkspaceRow = ({ workspace, onDelete }) => {
  return (
    <tr>
      <td>
        <FaFolder className="text-primary me-2" style={{ fontSize: '24px' }} />
        {workspace.name}
      </td>
      <td>
        <h6 className="text-muted">{workspace.createdAt}</h6>
      </td>
      <td>
        <Link to={`/workspace/details/${workspace.id}`} className="btn btn-info btn-sm theme-bg">
          View
        </Link>
        <button 
          className="btn btn-info btn-sm theme-bg2"
          onClick={() => onDelete(workspace.id)}
        >
          Delete
        </button>
      </td>
    </tr>
  );
};

export default WorkspaceRow;
