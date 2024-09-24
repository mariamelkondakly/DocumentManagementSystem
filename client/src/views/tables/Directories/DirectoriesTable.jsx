// DirectoriesTable.jsx
import React from 'react';
import { Table } from 'react-bootstrap';
import DirectoriesRow from './DirectoriesRow'; // Adjust the import path if necessary

const DirectoriesTable = ({ directories = [], onDirectoriesDeleted, onDirectoryMoved }) => {
  if (!Array.isArray(directories)) {
    return <p>No directories available.</p>;
  }

  return (
    <Table responsive hover className="recent-workspaces">
      <thead>
        <tr>
          <th>Directory Name</th>
          <th>Date Created</th>
          <th>Last Accessed At</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {directories.length > 0 ? (
          directories.map((directory) => (
            <DirectoriesRow 
              key={directory.id} 
              directory={directory} 
              onDirectoryDeleted={onDirectoriesDeleted} 
              onDirectoryMoved={onDirectoryMoved}
              
            />
          ))
        ) : (
          <tr>
            <td colSpan="4" className="text-center">No directories found.</td>
          </tr>
        )}
      </tbody>
    </Table>
  );
};

export default DirectoriesTable; 
