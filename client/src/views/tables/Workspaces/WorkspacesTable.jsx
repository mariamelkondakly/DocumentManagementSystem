import React from 'react';
import { Table } from 'react-bootstrap';
import WorkspaceRow from './WorkspaceRow'; // Adjust the import path if necessary

const WorkspacesTable = ({ workspaces = [], onWorkspaceDeleted }) => {
  if (!Array.isArray(workspaces)) {
    return <p>No workspaces available.</p>;
  }

  return (
    <Table responsive hover className="recent-workspaces">
      <thead>
        <tr>
          <th>Workspace Name</th>
          <th>Date Created</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {workspaces.length > 0 ? (
          workspaces.map((workspace) => (
            <WorkspaceRow 
              key={workspace.id} 
              workspace={workspace} 
              onWorkspaceDeleted={onWorkspaceDeleted}            />
          ))
        ) : (
          <tr>
            <td colSpan="3" className="text-center">No workspaces found.</td>
          </tr>
        )}
      </tbody>
    </Table>
  );
};

export default WorkspacesTable;
