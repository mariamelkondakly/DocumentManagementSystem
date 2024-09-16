import React, { useState } from "react";
import { registerAPICall } from "../services/AuthService";
import { validateEmail } from "../utils";
import registerDecoration from "../Assets/registerDecoration.jpeg";

const PasswordErrorMessage = () => {
  return (
    <p className="text-red-900 pt-2">
      Password should have at least 8 characters
    </p>
  );
};

const EmailErrorMessage = () => {
  return (
    <p className="text-red-900 pt-2 text-sm">
      Make sure you're typing the right email
    </p>
  );
};
const NameErrorMessage = () => {
  return <p className="text-red-900 pt-2 text-sm">required field</p>;
};
const NIDErrorMessage = () => {
  return (
    <p className="text-red-900 pt-2 text-sm">
      Password should have exactly 14 characters
    </p>
  );
};

const RegisterComponent = () => {
  const [nid, setNID] = useState({
    value: "",
    isTouched: false,
  });
  const [email, setEmail] = useState({ value: "", isTouched: false });
  const [firstName, setFirstName] = useState({ value: "", isTouched: false });

  const [age, setAge] = useState("");

  const [lastName, setLastName] = useState({ value: "", isTouched: false });

  const [password, setPassword] = useState({ value: "", isTouched: false });

  async function handleRegistrationForm(e) {
    e.preventDefault();
    // Convert values to the correct types
    const ageNumber = Number(age);
    const nidNumber = Number(nid.value);
    const firstnameString = firstName.value;
    const lastnameString = lastName.value;
    const emailString = email.value;
    const passwordString = password.value;
    if (isNaN(ageNumber) || isNaN(nidNumber)) {
      console.error("Age or NID is not a valid number");
      return;
    }

    const register = {
      first_name: firstnameString,
      last_name: lastnameString,
      age: ageNumber, // Correctly converted to number
      email: emailString,
      nid: nidNumber, // Correctly converted to number
      password: passwordString,
    };

    try {
      const response = await registerAPICall(register);
      clearData();
    } catch (error) {
      console.error("Registration error:", error);
    }
  }

  const clearData = () => {
    setEmail({ value: "", isTouched: false });
    setFirstName({ value: "", isTouched: false });
    setLastName({ value: "", isTouched: false });
    setAge("");
    setNID({ value: "", isTouched: false });
    setPassword({ value: "", isTouched: false });
  };

  const checkData = () => {
    return (
      validateEmail(email.value) &&
      password.value.length >= 8 &&
      nid.value.length === 14
    );
  };

  return (
    <div className="flex w-full h-screen ">
      <div className="w-full items-center justify-center flex lg:w-3/4">
        <div className="mt-1 mb-0.5 p-10 shadow-lg bg-orange-50">
          <h2 className=" text-green-lightPastel text-6xl text-right p-12">
            Welcome abroad
          </h2>
            <form onSubmit={handleRegistrationForm}>
              <div className="mb-4">
                <label className="text-2xl" htmlFor="firstName">
                  First name
                </label>
                <input
                  type="text"
                  name="firstName"
                  className="ml-2 shadow-lg"
                  placeholder="Enter your name"
                  value={firstName.value}
                  onChange={(e) => {
                    setFirstName({ ...firstName, value: e.target.value });
                  }}
                  onBlur={() => {
                    setFirstName({ ...firstName, isTouched: true });
                  }}
                />
                {firstName.isTouched && firstName.value === "" ? (
                  <NameErrorMessage />
                ) : null}
              </div>

              <div className="mb-3">
                <label className="text-2xl" htmlFor="lastName">
                  Last name
                </label>
                <input
                  type="text"
                  name="lastName"
                  className="ml-2 shadow-lg"
                  placeholder="Last name"
                  value={lastName.value}
                  onChange={(e) => {
                    setLastName({ ...lastName, value: e.target.value });
                  }}
                  onBlur={() => {
                    setLastName({ ...lastName, isTouched: true });
                  }}
                />
                {lastName.isTouched && lastName.value === "" ? (
                  <NameErrorMessage />
                ) : null}
              </div>
              <div className="mb-3">
                <label className="text-2xl" htmlFor="age">
                  Age
                </label>
                <input
                  type="number"
                  name="age"
                  className="ml-2 shadow-lg"
                  placeholder="Enter your age"
                  value={age}
                  onChange={(e) => {
                    setAge(e.target.value);
                  }}
                />
              </div>
              <div className="mb-3">
                <label className="text-2xl" htmlFor="nid">
                  National Id
                </label>
                <input
                  type="text"
                  name="nid"
                  className="ml-2 shadow-lg"
                  placeholder="Enter your national id"
                  value={nid.value}
                  onChange={(e) => {
                    setNID({ ...nid, value: e.target.value });
                  }}
                  onBlur={() => {
                    setNID({ ...nid, isTouched: true });
                  }}
                />
                {nid.isTouched && nid.value.length !== 14 ? (
                  <NIDErrorMessage />
                ) : null}
              </div>
              <div className="mb-3">
                <label className="text-2xl" htmlFor="email">
                  Email
                </label>
                <input
                  type="text"
                  name="email"
                  className="ml-2 shadow-lg"
                  placeholder="Enter email"
                  value={email.value}
                  onChange={(e) => {
                    setEmail({ ...email, value: e.target.value });
                  }}
                  onBlur={() => {
                    setEmail({ ...email, isTouched: true });
                  }}
                />
                {email.isTouched && !validateEmail(email.value) ? (
                  <EmailErrorMessage />
                ) : null}
              </div>

              <div className="mb-3">
                <label className="text-2xl" htmlFor="password">
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  className="ml-2 shadow-lg"
                  placeholder="Enter password"
                  value={password.value}
                  onChange={(e) => {
                    setPassword({ ...password, value: e.target.value });
                  }}
                  onBlur={() => {
                    setPassword({ ...password, isTouched: true });
                  }}
                />
                {password.isTouched && password.value.length < 8 ? (
                  <PasswordErrorMessage />
                ) : null}
              </div>
                <button
                  className="btn-primary"
                  onClick={(e) => handleRegistrationForm(e)}
                  disabled={!checkData()}
                >
                  Submit
                </button>
            </form>
          </div>
      </div>
      <div className="hidden lg:flex h-full items-center justify-center bg-purple-50 lg:w-fit">
        <img src={registerDecoration} alt="decorative"></img>
      </div>
    </div>
  );
};

export default RegisterComponent;
