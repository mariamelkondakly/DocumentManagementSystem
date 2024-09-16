import { BrowserRouter, Route, Routes } from 'react-router-dom';
import FooterComponent from './components/Footer';
import HeaderComponent from './components/Header';
import RegisterComponent from './components/Register';
import LoginComponent from './components/Login';
import WorkspacesComponent from './components/Workspaces';
import RootDirectories from './components/RootDirectories';

function App() {
  return (
    <div>
    <BrowserRouter>
      <HeaderComponent/>
      <div className="container">
      <Routes>
      <Route path="/" element={<RegisterComponent/>}></Route>
      <Route path="/register" element={<RegisterComponent/>}></Route>
      <Route path="/login" element={<LoginComponent/>}></Route>
      <Route path="/workspaces" element={<WorkspacesComponent/>}></Route>
      <Route path="/root/:workspaceId" element={<RootDirectories />}></Route>
      </Routes>
      </div>
      <FooterComponent/>
      </BrowserRouter>
    </div>
  );
}

export default App;
