import React, { useState } from 'react';
import { Table } from 'react-bootstrap';
import DirectoriesRow from './DirectoriesRow'; // Adjust the import path if necessary
import DocumentsRow from './DocumentsRow';

const DirectoriesTable = ({ directories = [], documents = [], onDirectoriesDeleted, onDirectoryMoved, isOriginalRoot, refresh }) => {
  const [sortField, setSortField] = useState('name'); // Default sort by 'name'
  const [sortOrder, setSortOrder] = useState('asc'); // Default sort in ascending order

  // Ensure directories and documents are arrays
  directories = Array.isArray(directories) ? directories : [];
  documents = Array.isArray(documents) ? documents : [];

  // Sorting logic
  const handleSort = (field) => {
    const order = (field === sortField && sortOrder === 'asc') ? 'desc' : 'asc';
    setSortField(field);
    setSortOrder(order);
  };

  const sortData = (data) => {
    // Ensure data is an array before sorting
    if (!Array.isArray(data)) {
      return [];
    }

    return [...data].sort((a, b) => {
      let valueA = a[sortField];
      let valueB = b[sortField];

      // Handle date fields properly
      if (sortField === 'createdAt' || sortField === 'lastAccessedAt') {
        valueA = new Date(valueA);
        valueB = new Date(valueB);
      }

      // Handle string comparison for 'name'
      if (typeof valueA === 'string') {
        valueA = valueA.toLowerCase();
        valueB = valueB.toLowerCase();
      }

      if (valueA < valueB) {
        return sortOrder === 'asc' ? -1 : 1;
      }
      if (valueA > valueB) {
        return sortOrder === 'asc' ? 1 : -1;
      }
      return 0;
    });
  };

  const sortedDirectories = sortData(directories);
  const sortedDocuments = sortData(documents);

  return (
    <Table responsive hover className="recent-workspaces">
      <thead>
        <tr>
          <th onClick={() => handleSort('name')} style={{ cursor: 'pointer' }}>
            Directory Name {sortField === 'name' && (sortOrder === 'asc' ? '▲' : '▼')}
          </th>
          <th onClick={() => handleSort('createdAt')} style={{ cursor: 'pointer' }}>
            Date Created {sortField === 'createdAt' && (sortOrder === 'asc' ? '▲' : '▼')}
          </th>
          <th onClick={() => handleSort('lastAccessedAt')} style={{ cursor: 'pointer' }}>
            Last Accessed At {sortField === 'lastAccessedAt' && (sortOrder === 'asc' ? '▲' : '▼')}
          </th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {sortedDirectories.length > 0 || sortedDocuments.length > 0 ? (
          <>
            {sortedDirectories.map((directory) => (
              <DirectoriesRow
                key={directory.id}
                directory={directory}
                onDirectoryDeleted={onDirectoriesDeleted}
                onDirectoryMoved={onDirectoryMoved}
                isOriginalRoot={isOriginalRoot}
              />
            ))}
            {sortedDocuments.map((document) => (
              <DocumentsRow
                key={document.id}
                document={document}
                onDocumentDeleted={onDirectoriesDeleted}
                onDocumentMoved={onDirectoryMoved}
                refresh={refresh}
              />
            ))}
          </>
        ) : (
          <tr>
            <td colSpan="4" className="text-center">No directories or documents found.</td>
          </tr>
        )}
      </tbody>
    </Table>
  );
};

export default DirectoriesTable;
