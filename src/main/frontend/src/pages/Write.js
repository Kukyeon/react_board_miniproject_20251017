import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Write.css";
function Write({ user }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    if (!user) {
      alert("로그인 후 글 작성 가능합니다.");
      return;
    }

    try {
      await api.post("/api/board", { title, content });
      alert("글 작성 완료!");
      navigate("/board");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
      } else {
        console.error(err);
        alert("글쓰기 실패!");
      }
    }
  };

  return (
    <div className="write-container">
      <h2>글쓰기</h2>
      <form onSubmit={handleSubmit} className="write-form">
        <input
          type="text"
          placeholder="제목"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        {errors.title && <p style={{ color: "red" }}>{errors.title}</p>}
        <textarea
          placeholder="내용"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        {errors.content && <p style={{ color: "red" }}>{errors.content}</p>}
        <div className="button-group">
          <button type="submit">등록</button>
          <button type="button" onClick={() => navigate("/board")}>
            취소
          </button>
        </div>
      </form>
    </div>
  );
}

export default Write;
