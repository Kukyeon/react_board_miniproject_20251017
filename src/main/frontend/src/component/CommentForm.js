import { useState } from "react";
import api from "../api/axiosConfig";

function CommentForm({ boardId, loadComments }) {
  const [newComment, setNewComment] = useState("");
  const [commentErrors, setCommentErrors] = useState({});

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    setCommentErrors({});
    if (!newComment.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }
    try {
      alert("댓글을 입력하시겠습니까?");
      await api.post(`/api/comments/${boardId}`, { content: newComment });
      setNewComment("");
      loadComments();
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setCommentErrors(err.response.data);
      } else {
        console.error(err);
        alert("댓글 등록 실패!");
      }
    }
  };

  return (
    <>
      <h3>댓글 쓰기</h3>
      <form onSubmit={handleCommentSubmit} className="comment-form">
        <textarea
          placeholder="댓글을 입력하세요."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
        />
        {commentErrors.content && (
          <p style={{ color: "red" }}>{commentErrors.content}</p>
        )}
        <button type="submit" className="comment-button">
          등록
        </button>
      </form>
    </>
  );
}

export default CommentForm;
