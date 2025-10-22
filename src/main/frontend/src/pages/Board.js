import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Board.css";

function Board({ user }) {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const navigate = useNavigate();

  const loadPosts = async (page = 0) => {
    try {
      setLoading(true);
      const res = await api.get(`/api/board?page=${page}&size=10`);
      setPosts(res.data.posts);
      setCurrentPage(res.data.currentPage);
      setTotalPages(res.data.totalPages);
      setTotalItems(res.data.totalItems);
    } catch (err) {
      console.error(err);
      setError("게시글 불러오기 실패");
      setPosts([]);
    } finally {
      setLoading(false);
    }
  };

  const handleWrite = () => {
    if (!user) {
      alert("로그인 후 이용 바랍니다.");
      return;
    }
    navigate("/board/write");
  };

  useEffect(() => {
    loadPosts(currentPage);
  }, [currentPage]);

  const getPageNumbers = () => {
    const startPage = Math.floor(currentPage / 10) * 10;
    const endPage = startPage + 10 > totalPages ? totalPages : startPage + 10;
    const page = [];
    for (let i = startPage; i < endPage; i++) {
      page.push(i);
    }
    return page;
  };

  const date = (dateString) => {
    return dateString.substring(0, 10);
  };

  return (
    <div className="container">
      <h2>자유게시판</h2>
      <div className="write-button-container">
        <button className="write-button" onClick={handleWrite}>
          글쓰기
        </button>
      </div>
      {loading && <p>게시판 목록 로딩중..</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table className="board-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>조회수</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {posts.length > 0 ? (
            posts.map((p, index) => (
              <tr key={p.id}>
                <td>{totalItems - index + 10 * currentPage}</td>
                <td
                  className="click-title"
                  onClick={() => navigate(`/board/${p.id}`)}
                  style={{
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    gap: "8px",
                  }}
                >
                  {p.title}
                  {p.commentCount > 0 && (
                    <span style={{ fontSize: "0.8em", color: "#888" }}>
                      [{p.commentCount}]
                    </span>
                  )}
                </td>
                <td>{p.viewCount}</td>
                <td>{p.author.username}</td>
                <td>{date(p.createDate)}</td>
              </tr>
            ))
          ) : (
            <tr colspan="4">게시물이 없습니다.</tr>
          )}
        </tbody>
      </table>
      <div className="pagination">
        <button
          onClick={() => currentPage > 0 && setCurrentPage(currentPage - 1)}
          disabled={currentPage === 0}
        >
          {" "}
          ◀{" "}
        </button>

        {getPageNumbers().map((num) => (
          <button
            className={num === currentPage ? "active" : ""}
            key={num}
            onClick={() => setCurrentPage(num)}
          >
            {num + 1}
          </button>
        ))}

        <button
          onClick={() =>
            currentPage < totalPages - 1 && setCurrentPage(currentPage + 1)
          }
          disabled={currentPage === totalPages - 1}
        >
          {" "}
          ▶{" "}
        </button>
      </div>
    </div>
  );
}

export default Board;
