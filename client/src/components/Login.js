import React, { useState } from "react";
import { loginAPICall } from "../services/AuthService";
import { useNavigate } from 'react-router-dom';


const LoginComponent = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();


  function checkData() {}

  async function handleLoginForm(e) {
    e.preventDefault();
    const login = { email, password };

    loginAPICall(login,setErrorMessage)
      .then((response) => {
        navigate('/workspaces'); 
      })
      .catch((error) => {
        console.error("Error response:", error.response);
        console.error("Error message:", error.message);
        console.error("Error config:", error.config);
      });

  }

  return (
    <div className="flex w-full h-screen">
      <br /> <br />
      <div className="w-full items-center justify-center flex lg:w-0.5">
        <div className="card-header">
          <h2 className="text-center">User Login</h2>
        </div>

        <div className="card-body">
          <form>
            <div className="row mb-3">
              <label className="col-md-3 control-label" htmlFor="email">
                Email
              </label>
              <div className="col-md-9">
                <input
                  type="text"
                  name="email"
                  className="form-control"
                  placeholder="Enter email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
            </div>

            <div className="row mb-3">
              <label className="col-md-3 control-label">Password</label>
              <div className="col-md-9">
                <input
                  type="password"
                  name="password"
                  className="form-control"
                  placeholder="Enter password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
            </div>

            <div className="form-group mb-3">
              <button
                className="btn btn-primary"
                onClick={(e) => handleLoginForm(e)}
              >
                Submit
              </button>
              {errorMessage && <p>{errorMessage}</p>}

            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
