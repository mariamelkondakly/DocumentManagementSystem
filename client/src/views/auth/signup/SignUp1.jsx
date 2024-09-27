import React, { useState } from 'react';
import { Card, Row, Col, CardHeader } from 'react-bootstrap';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import Breadcrumb from '../../../layouts/AdminLayout/Breadcrumb';
import { registerAPICall } from '../../../services/AuthService'; // Adjust the path as needed

const SignUp1 = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    first_name: '',
    last_name: '',
    email: '',
    password: '',
    nid: '',
    age: ''
  });

  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const { first_name, last_name, email, password, nid, age } = formData;

    if (!first_name || !last_name || !email || !password || nid.length !== 14 || !/^\d+$/.test(nid)) {
      setErrorMessage('Please fill all required fields correctly.');
      return;
    }

    try {
      await registerAPICall({ first_name, last_name, email, password, nid, age });
      setErrorMessage('');
      navigate('/login'); // Redirect to login page
    } catch (error) {
      setErrorMessage('Signup failed. Please try again.');
    }
  };

  return (
    <React.Fragment>
      <Breadcrumb />
      <div className="auth-wrapper">
        <div className="auth-content">
          <div className="auth-bg">
            <span className="r" />
            <span className="r s" />
            <span className="r s" />
            <span className="r" />
          </div>
          <Card className="borderless">
            <Row className="align-items-center">
              <Col>
                <CardHeader>
                  <h1 className="cursive-font text-center" style={{ marginLeft: '0px', fontSize: '70px' }}>
                    Documented
                  </h1>
                  <p className="text-center">Your timeless digital archive</p>
                </CardHeader>
                <Card.Body className="text-center">
                  <div className="mb-4">
                    <i className="feather icon-user-plus auth-icon " />
                  </div>
                  <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                      <label htmlFor="first_name" className="form-label text-start d-block">
                        First Name
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        id="first_name"
                        name="first_name"
                        value={formData.first_name}
                        onChange={handleChange}
                        required
                      />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="last_name" className="form-label text-start d-block">
                        Last Name
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        id="last_name"
                        name="last_name"
                        value={formData.last_name}
                        onChange={handleChange}
                        required
                      />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="email" className="form-label text-start d-block">
                        Email Address
                      </label>
                      <input
                        type="email"
                        className="form-control"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                      />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="nid" className="form-label text-start d-block">
                        NID (14 digits)
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        id="nid"
                        name="nid"
                        value={formData.nid}
                        onChange={handleChange}
                        required
                      />
                      {formData.nid.length !== 14 && formData.nid && (
                        <p className="text-danger text-start d-block">NID should be exactly 14 digits.</p>
                      )}
                    </div>
                    <div className="mb-3">
                      <label htmlFor="age" className="form-label text-start d-block">
                        Age (Optional)
                      </label>
                      <input type="number" className="form-control" id="age" name="age" value={formData.age} onChange={handleChange} />
                    </div>
                    <div className="mb-4">
                      <label htmlFor="password" className="form-label text-start d-block">
                        Password
                      </label>
                      <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                      />
                    </div>
                    {errorMessage && <p className="text-danger text-start d-block">{errorMessage}</p>}
                    <button className="btn btn-primary mb-4" type="submit">
                      Sign up
                    </button>
                  </form>
                  <p className="mb-2">
                    Already have an account?{' '}
                    <NavLink to={'/login'} className="f-w-400">
                      Login
                    </NavLink>
                  </p>
                </Card.Body>
              </Col>
            </Row>
          </Card>
        </div>
      </div>
    </React.Fragment>
  );
};

export default SignUp1;
