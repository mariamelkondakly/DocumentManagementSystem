import apiClient from "./Interceptor";
import parseDate from "./DateUtils";
const WorkspaceService = {
    // Fetch all workspaces
    fetchWorkspaces: async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await apiClient.get('', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        let fetchedWorkspaces = response.data.content;
        const noOfElements=response.data.totalElements;
        fetchedWorkspaces.sort((a, b) => parseDate(b.createdAt) - parseDate(a.createdAt));
  
        return { success: true, data: fetchedWorkspaces,noOfElements };
      } catch (err) {
        console.error('Failed to fetch workspaces:', err);
        return { success: false, message: 'Failed to fetch workspaces' };
      }
    },
  
    // Delete a workspace by ID
    deleteWorkspace: async (id) => {
      try {
        const token = localStorage.getItem('token');
        await apiClient.delete(`/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        return { success: true };
      } catch (err) {
        console.error(`Failed to delete workspace with id ${id}:`, err);
        return { success: false, message: 'Failed to delete workspace' };
      }
    },

    createWorkspace: async (newWorkspaceName) => {
        try {
          const token = localStorage.getItem('token');
          // Make POST request to create new workspace
          const response = await apiClient.post(`/createWorkspace/${newWorkspaceName}`, {}, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          return { success: true, data: response.data };
        } catch (error) {
          return { success: false, message: 'Failed to create new workspace' };
        }
      },
  };
  
  export default WorkspaceService;
  