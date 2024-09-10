import axios from "axios";

const AUTH_REST_API_BASE_URL = "http://localhost:8080";

export const registerAPICall = (registerObj) => {
    console.log(registerObj);
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


export const loginAPICall = (loginObj)=>{
    console.log(loginObj);

    return axios.post(`${AUTH_REST_API_BASE_URL}/login`, loginObj)
        .then(response => {
            const token = response.data.token;
            localStorage.setItem('token', token);

            console.log("Login successful:", response.data);
            return response.data;
        })
        .catch(error=>{
            console.error("Login error:", error.response ? error.response.data : error.message);
            throw error;})
}
