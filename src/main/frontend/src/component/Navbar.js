import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar({ onLogout, user }) {
  return (
    <nav className="navbar">
      <div className="logo">미니 프로젝트</div>
      <div className="menu">
        <Link to="/">홈</Link>
        <Link to="/board">게시판</Link>
        <Link to="/Location">오시는길</Link>
        {!user && <Link to="/login">로그인</Link>}
        {!user && <Link to="/signup">회원가입</Link>}
        {user && (
          <button onClick={onLogout} className="logout-btn">
            로그아웃
          </button>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
