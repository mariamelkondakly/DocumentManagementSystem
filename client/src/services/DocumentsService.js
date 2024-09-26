import apiClient from "./Interceptor";

const DocumentsService={
    deleteDocument: async (id) => {
        try {
          const token = localStorage.getItem('token');
          const response=await apiClient.delete(`/directories/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          console.log(response);
          return { success: true };
        } catch (err) {
          console.error(`Failed to delete directroy with id ${id}:`, err);
          return { success: false, message: 'Failed to delete directory' };
        }
      },
      updateDocumentName: async (id,newName) => {
        try {
          const token = localStorage.getItem('token');
          await apiClient.put(`directories/rename/${id}/${newName}`, 
            {headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          return { success: true };
        } catch (err) {
          console.error(`Failed to rename document with id ${id}:`, err);
          return { success: false, message: 'Failed to rename document' };
        }
      },
      moveDocument: async (id, parentId) => {
        try {
          const token = localStorage.getItem('token');
          const response = await apiClient.put(`/directories/move`, {

            newParentId: parentId,  // The new parent ID
            documentId:id
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
      },
      previewDocument: async (documentId) => {
        const token = localStorage.getItem('token');
    
        try {
            const response = await apiClient.get(`/directories/preview/${documentId}`, {
                responseType: 'blob', // Ensure responseType is inside the config object
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            });
            
            // Return the content type along with the blob data
            return { success: true, data: response.data}; // Include content type in the response
        } catch (error) {
            return { success: false, message: error.message };
        }
    },
      addNewTags: async (documentId, tags) => {
        const token = localStorage.getItem('token');
        console.log('Tags being sent:', tags);  // Add this to debug the tags being sent
        try {
          // Ensure 'tags' is an array before sending it
          if (!Array.isArray(tags)) {
            throw new Error('Tags must be an array');
          }
      
          const response = await apiClient.put(
            `/directories/tags/${documentId}`,
            {
              tags,  // Ensure this is sent as an array
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          
          return { success: true, data: response.data };
        } catch (error) {
          console.error('Error in addNewTags:', error);  // Log the error for debugging
          return { success: false, message: error.message };
        }
      },
      
    
      downloadDocument: async (documentId,documentName) => {
        const token = localStorage.getItem('token');

        try {
          const response = await apiClient.get(`/directories/download/${documentId}`, {
            responseType: 'blob', // To handle binary data
          },{
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', `${documentName}`); // Specify a default filename
          document.body.appendChild(link);
          link.click();
          link.remove();
          return { success: true };
        } catch (error) {
          return { success: false, message: error.message };
        }
      },   
}
export default DocumentsService;