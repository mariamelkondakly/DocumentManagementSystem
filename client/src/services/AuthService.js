
import axios from 'axios';
import { data } from 'jquery';

const AUTH_REST_API_BASE_URL = 'http://localhost:8080';

export const registerAPICall = async (registerObj) => {
    
  return axios.post(`${AUTH_REST_API_BASE_URL}/register`, registerObj)
      .then(response => {
          console.log("Registration successful:", response.data);
          return response.data;
          
      })
      .catch(error => {
          if (error.response) {
              // The request was made and the server responded with a status code
              // that falls out of the range of 2xx
              console.error("Registration error response:", error.response.data);
              console.error("Registration error status:", error.response.status);
              console.error("Registration error headers:", error.response.headers);
          } else if (error.request) {
              // The request was made but no response was received
              console.error("Registration error request:", error.request);
          } else {
              // Something happened in setting up the request that triggered an Error
              console.error("Registration error message:", error.message);
          }
          throw error; // Re-throw the error after logging it
      });
};

export const loginAPICall = async (loginObj, setErrorMessage) => {
  return axios.post(`${AUTH_REST_API_BASE_URL}/login`, loginObj)
    .then(response => {
      const token = response.data[0];
      
      // Store token in local storage
      localStorage.setItem('token', token);
      localStorage.setItem('firstName',response.data[1].first_name);
      localStorage.setItem('lastName',response.data[1].last_name);
      localStorage.setItem('email',response.data[1].email);
      localStorage.setItem('age',response.data[1].age);
      localStorage.setItem('nid',response.data[1].nid);


      return response.data[0];

    })
    .catch(error => {
      if (error.response && error.response.status === 401) {
        setErrorMessage("Invalid login credentials");
      } else {
        setErrorMessage("An unexpected error occurred");
      }
    });
};
