import axios from "axios";

const apiClient= axios.create(
    {
        baseURL:"http://localhost:8081/workspaces",
    }
);

apiClient.interceptors.request.use(
    (config)=>{
        const token=localStorage.getItem('token');

        if(token){
            config.headers['Authorization']=`Bearer ${token}`;
        }
        console.log(config);

        return config;
    },
    (error)=>{
        return Promise.reject(error);
    }
    
);
export default apiClient;