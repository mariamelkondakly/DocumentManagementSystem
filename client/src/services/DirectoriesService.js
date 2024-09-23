import apiClient from "./Interceptor";

const DirectoriesService={

    viewRootDirectories: async(workspaceId)=>{
        try{
            const token= localStorage.getItem('token');
            const response=await apiClient.get(`/root/${workspaceId}`, {headers:{
                Authorization: `Bearer ${token}`
            }});
        return { success: true, data: response.data };
        }
         catch (error) {
            console.error("API Error:", error); // Log the actual error

          return { success: false, message: 'Failed to load root directories' };
        }
    },

    createRootDirectory: async (workspaceId, name) => {
      try {
        const token = localStorage.getItem('token');
        
        const response = await apiClient.post(`/root/${workspaceId}/${name}`, 
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        return { success: true, data: response.data };
      } catch (error) {
        console.error("API Error:", error.response?.data || error.message); // Log actual error response if available
    
        let message = 'Failed to create root directory';
        
        // Handle specific error cases
        if (error.response) {
          // Server response with an error status code
          if (error.response.status === 401) {
            message = 'Unauthorized: Please check your login status.';
          } else if (error.response.status === 403) {
            message = 'Forbidden: You do not have permission to create a root directory.';
          } else if (error.response.status === 404) {
            message = 'Workspace not found.';
          }
        } else if (error.request) {
          // Request was made but no response received
          message = 'No response from server. Please check your network connection.';
        }
    
        return { success: false, message };
      }
    },
    

    deleteDirectory: async (id) => {
      try {
        const token = localStorage.getItem('token');
        await apiClient.delete(`/deleteDirectory/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        return { success: true };
      } catch (err) {
        console.error(`Failed to delete directroy with id ${id}:`, err);
        return { success: false, message: 'Failed to delete directory' };
      }
    },

    moveDirectory: async (id, parentId, isRoot, originalRoot) => {
      try {
        const token = localStorage.getItem('token');
        const response = await apiClient.put(`/move/${id}`, {
          parentId: parentId,  // The new parent ID
          isRoot: isRoot,      // True if moving to a root directory
          originalRoot: originalRoot // True if the original was a root directory
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        });
        return response.data; // Return the response data if needed
      } catch (error) {
        console.error('Error moving directory:', error);
        throw error; // Rethrow the error for handling in the calling function
      }
    }    
}
export default DirectoriesService;