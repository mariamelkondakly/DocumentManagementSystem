import React, { useEffect, useState } from "react";
import axios from "axios";
import interceptor from "../services/Interceptor";
import { Link } from "react-router-dom";


const WorkspacesComponent = () => {
  const [workspaces, setWorkspaces] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); // Start at page 1
  const [totalPages, setTotalPages] = useState(0); // Total pages in the API

  const token = localStorage.getItem('token'); // Assuming you store the token in local storage


  const fetchWorkspaces = async () => {
    try {
      const response = await interceptor.get("", {
        headers: {
        Authorization: `Bearer ${token}`
    },
        params: {
          page: currentPage - 1,
          size: 5,
        },
      });
      console.log(response.data.content);
      setWorkspaces(response.data.content);
      setTotalPages(response.data.totalPages);
      setCurrentPage(currentPage);
    } catch (error) {
      console.error("Error fetching workspaces:", error);
    }
  };

  useEffect(() => {
    fetchWorkspaces(currentPage);
  }, [currentPage]);

  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  const handleWorkspaceClick = (id) => {
    console.log("Selected Workspace ID:", id); // Log the workspace ID
  };


  return (
    <div className="justify-center align-middle flex">
      <div className="w-3/5">
        <h1 className="mt-12 mb-12 text-green-lightPastel text-6xl">Workspaces</h1>
        <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Created At</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {workspaces.map((workspace) => (
            <tr key={workspace.id}>
              <td><Link to={`/root/${workspace.id}`} onClick={() => handleWorkspaceClick(workspace.id)}>{workspace.name}</Link></td>
              <td>{workspace.createdAt}</td>
              <td>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
        <div>
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <span>
            Page {currentPage} of {totalPages}
          </span>
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};
export default WorkspacesComponent;
