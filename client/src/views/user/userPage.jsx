import React, { useEffect, useState } from 'react';
import { Card, Container, Row, Col, CardHeader } from 'react-bootstrap';

const ProfileCard = () => {
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    nid: '',
    email: '',
    age: ''
  });

  useEffect(() => {
    // Fetch user details from local storage
    const storedUserData = {
      firstName: localStorage.getItem('firstName'),
      lastName: localStorage.getItem('lastName'),
      nid: localStorage.getItem('nid'),
      email: localStorage.getItem('email'),
      age: localStorage.getItem('age')
    };
    setUserData(storedUserData);
  }, []);

  return (
    <Container className="d-flex justify-content-center align-items-center">
      <Row>
        <Col md={12}>
          <Card style={{ width: '24rem', padding: '30px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <CardHeader>
              <Card.Title className="text-center">User Profile</Card.Title>
            </CardHeader>
            <Card.Body>
              <Card.Text className=' text-info'>
                <strong className='text-black-50'>First Name: </strong>
                {userData.firstName}
              </Card.Text>
              <Card.Text className=' text-info'>
                <strong className='text-black-50'>Last Name: </strong>
                {userData.lastName}
              </Card.Text>
              <Card.Text className=' text-info'>
                <strong className='text-black-50'>National ID: </strong>
                {userData.nid}
              </Card.Text>
              <Card.Text className=' text-info'>
                <strong className='text-black-50'>Email: </strong>
                {userData.email}
              </Card.Text>
              <Card.Text className=' text-info'>
                <strong className='text-black-50'>Age: </strong>
                {userData.age}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ProfileCard;
