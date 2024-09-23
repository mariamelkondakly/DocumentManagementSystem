import PropTypes from 'prop-types';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Card, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaFolder } from 'react-icons/fa';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

const NavSearch = (props) => {
  const { windowWidth } = props;
  const [searchString, setSearchString] = useState(windowWidth < 600 ? '100px' : '');
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState({ directories: [], workspaces: [] });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (searchTerm) {
      fetchSearchResults();
    } else {
      setResults({ directories: [], workspaces: [] });
    }
  }, [searchTerm]);

  const fetchSearchResults = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${API_BASE_URL}/workspaces/search/${searchTerm}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      console.log("search results: "+JSON.stringify(response));


      setResults({
        directories: response.data.directories || [],
        workspaces: response.data.workspaces || []
      });
    } catch (error) {
      console.error('Error fetching search results:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  // Check if there are any results in either workspaces or directories
  const hasResults = 
    (results.workspaces.content && results.workspaces.content.length > 0) ||
    (results.directories.content && results.directories.content.length > 0);

  return (
    <React.Fragment>
      <div id="main-search" className="mb-4 ">
        <div className="input-group">
          <input
            type="text"
            id="m-search"
            className="form-control"
            placeholder="Search . . ."
            style={{ width: searchString }}
            value={searchTerm}
            onChange={handleInputChange}
          />
          <span
            role="button"
            tabIndex="0"
            className=" search-btn btn-theme"
          >
            <i className="feather icon-search" />
          </span>
        </div>
        {loading && <p>Loading...</p>}
        <div
          style={{
            opacity: hasResults ? 1 : 0,
            maxHeight: hasResults ? '1000px' : '0px',
            transition: 'opacity 0.5s ease, max-height 0.5s ease',
            overflow: 'hidden'
          }}
        >
        {!loading && hasResults && (
          <Card>
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
                            <FaFolder className=" text-c-green me-2" style={{ fontSize: '24px'}} />
                            {workspace.name}
                          </td>
                          <td>{workspace.createdAt}</td>
                          <td>
                            <Link to={`/rootDirectories/${workspace.id}`} className="btn btn-sm btn-theme">
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
                            <FaFolder className="me-2" style={{ fontSize: '24px',color: "#f88379"  }} />
                            {dir.name}
                          </td>
                          <td>{dir.createdAt}</td>
                          <td>
                            <Link to={`/directory/details/${dir.id}`} className="btn btn-sm btn-theme">
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
  windowWidth: PropTypes.number
};

export default NavSearch;
