import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Signup.css";
import api from "../api/axiosConfig";

function Login({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await api.post(
        "/api/auth/login",
        new URLSearchParams({ username, password })
      );

      const res = await api.get("api/auth/me");
      onLogin(res.data.username);

      alert("로그인 성공");
      navigate("/", { replace: true });
    } catch (err) {
      console.error(err);
      alert("로그인 실패!");
    }
  };

  return (
    <div className="form-container">
      <h2>회원로그인</h2>
      <form onSubmit={handleLogin}>
        <div>
          <input
            type="text"
            placeholder="아이디"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />{" "}
        </div>
        <br />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />{" "}
        <br />
        <br />
        <button type="submit">로그인</button>
      </form>
    </div>
  );
}

export default Login;
