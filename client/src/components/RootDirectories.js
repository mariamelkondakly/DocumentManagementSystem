import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom"; // Import the hook for accessing route parameters
import interceptor from "../services/Interceptor";

const RootDirectories = () => {
  const { workspaceId } = useParams(); // Extract the workspaceId from the route
  const token = localStorage.getItem("token"); // Fetch token from localStorage
  const [directories, setDirectories] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);

  const fetchDirectories = async () => {
    try {
      const response = await interceptor.get(`/root/${workspaceId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          page: currentPage - 1, // Backend expects zero-indexed pages
          size: 5, // Fetch 5 directories per page
        },
      });

      console.log(response.data.content);
      setDirectories(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Error fetching directories:", error);
    }
  };

  useEffect(() => {
    fetchDirectories();
  }, [workspaceId, currentPage]); // Refetch when workspaceId or page changes

  return (
    <div>
      <h2>Root Directories for Workspace</h2>
      <ul>
        {directories.map((directory) => (
          <li key={directory.id}>{directory.name}</li>
        ))}
      </ul>
      {/* Pagination Controls */}
      <div>
        <button
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          disabled={currentPage === 1}
        >
          Previous
        </button>
        <span> Page {currentPage} of {totalPages} </span>
        <button
          onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
          disabled={currentPage === totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default RootDirectories;
