import React, { useState } from 'react';
import { loginAPICall } from '../services/AuthService';

const LoginComponent = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    function checkData(){
        
    }

    function handleRegisterationForm(e) {
        e.preventDefault();
        const login = {email, password};
        console.log(login);

        loginAPICall(login).then((response) => {
            console.log(response.data);
        }).catch(error => {
            loginAPICall(login).then((response) => {
                console.log(response.data);
            }).catch(error => {
                console.error("Error response:", error.response);
                console.error("Error message:", error.message);
                console.error("Error config:", error.config);
            });
            
        });
    }

    return (
        <div className='container'>
            <br /> <br />
            <div className='row'>
                <div className='col-md-6 offset-md-3'>
                    <div className='card'>

                        <div className='card-header'>
                            <h2 className='text-center'>User Login</h2>
                        </div>

                        <div className='card-body'>
                            <form>
                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label' htmlFor='email'>Email</label>
                                    <div className='col-md-9'>
                                        <input 
                                            type='text' 
                                            name='email' 
                                            className='form-control' 
                                            placeholder='Enter email' 
                                            value={email} 
                                            onChange={(e) => setEmail(e.target.value)} 
                                        />
                                    </div>
                                </div>

                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label'>Password</label>
                                    <div className='col-md-9'>
                                        <input
                                            type='password'
                                            name='password'
                                            className='form-control'
                                            placeholder='Enter password'
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className='form-group mb-3'>
                                    <button 
                                        className='btn btn-primary' 
                                        onClick={(e) => handleRegisterationForm(e)}
                                    >
                                        Submit
                                    </button>
                                </div>
                            </form>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    );
}

export default LoginComponent;
