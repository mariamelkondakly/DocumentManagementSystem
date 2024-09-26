import PropTypes from 'prop-types';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Card, Table, Button, Dropdown } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom'; // Updated import
import { FaFolder, FaFile } from 'react-icons/fa';
import apiClient from 'services/Interceptor';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

const NavSearch = (props) => {
  const { windowWidth } = props;
  const [searchString, setSearchString] = useState(windowWidth < 600 ? '100px' : '');
  const [searchTerm, setSearchTerm] = useState('');
  const [searchFilter, setSearchFilter] = useState(''); // "tags", "type", or ""
  const [results, setResults] = useState({ directories: [], workspaces: [], documents: [] });
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate(); // Replaced useHistory with useNavigate

  const getPlaceholderText = () => {
    if (searchFilter === 'tags') {
      return 'Search documents by tags...';
    }
    if (searchFilter === 'type') {
      return 'Search documents by type...';
    }
    return 'Search workspaces, directories, and documents...';
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      if (searchTerm && searchFilter) {
        fetchSearchResults();
      } else {
        setResults({ directories: [], workspaces: [], documents: [] });
      }
    }, 500); 

    return () => clearTimeout(delayDebounceFn);
  }, [searchTerm, searchFilter]);

  const fetchSearchResults = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      let searchURL = `/search`;

      if (searchFilter === 'tags') {
        searchURL += `/tags/${searchTerm}`;
      } else if (searchFilter === 'type') {
        searchURL += `/type/${searchTerm}`;
      } else {
        searchURL += `/${searchTerm}`;
      }

      const response = await apiClient.get(searchURL, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setResults({
        directories: response.data.directories || [],
        workspaces: response.data.workspaces || [],
        documents: response.data.documents || []
      });
    } catch (error) {
      if (error.response) {
        console.error('Error fetching search results:', error.response.status, error.response.data);
      } else {
        console.error('Error fetching search results:', error.message);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleFilterChange = (filter) => {
    setSearchFilter(filter);
  };

  const handleCancelFilter = () => {
    setSearchFilter('');
    setSearchTerm('');
    setResults({ directories: [], workspaces: [], documents: [] });
  };

  const handleViewClick = () => {
    setResults({ directories: [], workspaces: [], documents: [] }); // Clear the results
  };

  const hasResults =
    (results.workspaces.content && results.workspaces.content.length > 0) ||
    (results.directories.content && results.directories.content.length > 0) ||
    (results.documents.content && results.documents.content.length > 0);

  return (
    <React.Fragment>
      <div id="main-search" className="mb-4">
        <div className="d-flex input-group">
          <input
            type="text"
            id="m-search"
            className="form-control"
            placeholder={getPlaceholderText()}
            style={{ width: searchString }}
            value={searchTerm}
            onChange={handleInputChange}
          />
          <span
            role="button"
            tabIndex="0"
            className="search-btn btn-theme"
            onClick={fetchSearchResults}
          >
            <i className="feather icon-search" />
          </span>

          <Dropdown className="ms-2">
            <Dropdown.Toggle className="btn btn-theme3" id="dropdown-basic">
              {searchFilter ? `Search by ${searchFilter.charAt(0).toUpperCase() + searchFilter.slice(1)}` : 'Filter'}
            </Dropdown.Toggle>

            <Dropdown.Menu>
              <Dropdown.Item onClick={() => handleFilterChange('tags')}>Search by Tags</Dropdown.Item>
              <Dropdown.Item onClick={() => handleFilterChange('type')}>Search by Type</Dropdown.Item>
              {searchFilter && (
                <Dropdown.Item onClick={handleCancelFilter} className="text-danger">
                  Cancel
                </Dropdown.Item>
              )}
            </Dropdown.Menu>
          </Dropdown>
        </div>

        {loading && <p>Loading...</p>}
        <div
          style={{
            opacity: hasResults ? 1 : 0,
            maxHeight: hasResults ? '1000px' : '0px',
            transform: hasResults ? 'translateY(0)' : 'translateY(-20px)',
            transition: 'opacity 0.5s ease, max-height 0.5s ease, transform 0.5s ease',
            overflow: 'hidden'
          }}
        >
          {!loading && hasResults && (
            <Card className="mt-3">
              <Card.Header>
                <Card.Title as="h5">Search Results</Card.Title>
              </Card.Header>
              <Card.Body>
                <Table responsive>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Name</th>
                      <th>Date Created</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {results.workspaces.content && results.workspaces.content.length > 0 && (
                      <>
                        <tr>
                          <td colSpan="4">
                            <h5>Workspaces</h5>
                          </td>
                        </tr>
                        {results.workspaces.content.map((workspace, index) => (
                          <tr key={workspace.id}>
                            <td>{index + 1}</td>
                            <td>
                              <FaFolder className="text-c-green me-2" style={{ fontSize: '24px' }} />
                              {workspace.name}
                            </td>
                            <td>{workspace.createdAt}</td>
                            <td>
                              <Link to={`/rootDirectories/${workspace.id}`} className="btn btn-sm btn-theme" onClick={handleViewClick}>
                                View
                              </Link>
                            </td>
                          </tr>
                        ))}
                      </>
                    )}

                    {results.directories.content && results.directories.content.length > 0 && (
                      <>
                        <tr>
                          <td colSpan="4">
                            <h5>Directories</h5>
                          </td>
                        </tr>
                        {results.directories.content.map((dir, index) => (
                          <tr key={dir.id}>
                            <td>{index + 1}</td>
                            <td>
                              <FaFolder className="me-2" style={{ fontSize: '24px', color: '#f88379' }} />
                              {dir.name}
                            </td>
                            <td>{dir.createdAt}</td>
                            <td>
                              <Link to={`/subDirectories/${dir.id}`} className="btn btn-sm btn-theme" onClick={handleViewClick}>
                                View
                              </Link>
                            </td>
                          </tr>
                        ))}
                      </>
                    )}
                    {results.documents.content && results.documents.content.length > 0 && (
                      <>
                        <tr>
                          <td colSpan="4">
                            <h5>Documents</h5>
                          </td>
                        </tr>
                        {results.documents.content.map((doc, index) => (
                          <tr key={doc.id}>
                            <td>{index + 1}</td>
                            <td>
                              <FaFile className="me-2" style={{ fontSize: '24px', color: 'purple' }} />
                              {doc.name}
                            </td>
                            <td>{doc.createdAt}</td>
                            <td>
                              <Link to={`/subDirectories/${doc.parentId}`} className="btn btn-sm btn-theme" onClick={handleViewClick}>
                                View
                              </Link>
                            </td>
                          </tr>
                        ))}
                      </>
                    )}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          )}
        </div>
      </div>
    </React.Fragment>
  );
};

NavSearch.propTypes = {
  windowWidth: PropTypes.number.isRequired
};

export default NavSearch;
