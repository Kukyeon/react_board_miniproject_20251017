import { Link } from "react-router-dom";
import "./Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <span>© 2025 미니 프로젝트</span>
        <div className="footer-menu">
          <Link to="/">홈</Link>
          <Link to="/board">게시판</Link>
          <Link to="/Location">오시는길</Link>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
