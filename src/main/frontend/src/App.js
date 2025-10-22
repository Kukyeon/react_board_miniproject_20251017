import { Route, Routes } from "react-router-dom";
import "./App.css";
import Navbar from "./component/Navbar";
import Home from "./pages/Home";
import Board from "./pages/Board";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Detail from "./pages/Detail";
import Write from "./pages/Write";
import { useEffect, useState } from "react";
import api from "./api/axiosConfig";
import Location from "./pages/Location";
import Footer from "./component/Footer";

function App() {
  const [user, setUser] = useState(null);

  const checkUser = async () => {
    try {
      const res = await api.get("api/auth/me");
      setUser(res.data.username);
    } catch {
      setUser(null);
    }
  };

  useEffect(() => {
    checkUser();
  }, []);

  const handleLogout = async () => {
    await api.post("api/auth/logout");
    setUser(null);
  };

  return (
    <div className="App">
      <Navbar onLogout={handleLogout} user={user} />
      <Routes>
        <Route path="/" element={<Home />}></Route>
        <Route path="/signup" element={<Signup />}></Route>
        <Route path="/login" element={<Login onLogin={setUser} />}></Route>
        <Route path="/Board" element={<Board user={user} />}></Route>
        <Route path="/Location" element={<Location />}></Route>
        <Route path="/board/write" element={<Write user={user} />}></Route>
        <Route path="/board/:id" element={<Detail user={user} />}></Route>
      </Routes>
      <Footer></Footer>
    </div>
  );
}

export default App;
