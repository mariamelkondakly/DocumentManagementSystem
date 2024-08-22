import React, { useState } from 'react';
import { registerAPICall } from '../services/AuthService';
import { validateEmail } from '../utils';

const PasswordErrorMessage = () => {
    return (
      <p className="FieldError">Password should have at least 8 characters</p>
    );
  };

  const EmailErrorMessage = () => {
    return (
      <p className="FieldError">Make sure you're typing the right email</p>
    );
  };
  const NIDErrorMessage = () => {
    return (
      <p className="FieldError">Password should have exactly 14 characters</p>
    );
  };
const RegisterComponent = () => {
    const [nid, setNID] = useState({
        value:"",
        isTouched:false
    });
    const [email, setEmail] = useState({value:"",
        isTouched:false});
    const [info, setInfo] = useState("");
    const [password, setPassword] = useState({value:"",
        isTouched:false}); 

    function handleRegisterationForm(e) {
        e.preventDefault();
        const register = { email, nid,password, info };
        registerAPICall(register).then((response) => {
            console.log(response.data);
            clearData();

        })
    }

    

    const clearData=()=>{
        setEmail({value:"",
        isTouched:false});
        setInfo("");
        setNID({value:"",
        isTouched:false});
        setPassword({value:"",
        isTouched:false});
    }

    const checkData=()=>{
        return(
            validateEmail(email.value),
            password.value.length>=8,
            nid.value.length===14
        );
    }

    return (
        <div className='container'>
            <br /> <br />
            <div className='row'>
                <div className='col-md-6 offset-md-3'>
                    <div className='card'>

                        <div className='card-header'>
                            <h2 className='text-center'>User Registration</h2>
                        </div>

                        <div className='card-body'>
                            <form onSubmit={handleRegisterationForm}>
                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label' htmlFor='email'>Email</label>
                                    <div className='col-md-9'>
                                        <input 
                                            type='text' 
                                            name='email' 
                                            className='form-control' 
                                            placeholder='Enter email' 
                                            value={email.value} 
                                            onChange={(e) => { 
                                                setEmail({ ...email, value: e.target.value }); 
                                            }} 
                                            onBlur={() => { 
                                                setEmail({ ...email, isTouched: true }); 
                                            }} 
                                        />         
                                        {email.isTouched && !validateEmail(email.value) ? ( 
                                            <EmailErrorMessage /> ) : null}                               
                                    </div>
                                </div>

                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label' htmlFor='password'>Password</label>
                                    <div className='col-md-9'>
                                        <input
                                            type='password'
                                            name='password'
                                            className='form-control'
                                            placeholder='Enter password'
                                            value={password.value}
                                            onChange={(e) => { 
                                                setPassword({ ...password, value: e.target.value }); 
                                            }} 
                                            onBlur={() => { 
                                                setPassword({ ...password, isTouched: true }); 
                                                }} 
                                        />
                                        {password.isTouched && password.value.length < 8 ? ( 
                                            <PasswordErrorMessage /> ) : null} 
                                    </div>
                                </div>

                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label' htmlFor='nid'>National Id</label>
                                    <div className='col-md-9'>
                                        <input 
                                            type='text' 
                                            name='nid' 
                                            className='form-control' 
                                            placeholder='Enter your national id' 
                                            value={nid.value}
                                            onChange={(e) => { 
                                                setNID({ ...nid, value: e.target.value }); 
                                                }} 
                                            onBlur={() => { 
                                                setNID({ ...nid, isTouched: true }); 
                                                }} 
                                        />
                                        {nid.isTouched && nid.value.length !== 14 ? ( 
                                            <NIDErrorMessage /> ) : null}
                                    </div>
                                </div>

                                <div className='row mb-3'>
                                    <label className='col-md-3 control-label' htmlFor='info'>Personal Info</label>
                                    <div className='col-md-9'>
                                        <textarea 
                                            name="info"
                                            className='form-control' 
                                            placeholder='Talk about yourself'
                                            value={info} 
                                            onChange={(e) => setInfo(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className='form-group mb-3'>
                                    <button 
                                        className='btn btn-primary' 
                                        onClick={(e) => handleRegisterationForm(e)}
                                        disabled={!checkData()}
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

export default RegisterComponent;
