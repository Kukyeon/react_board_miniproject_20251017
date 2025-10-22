import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Detail.css";

import PostEdit from "../component/PostEdit";
import PostView from "../component/PostView";
import CommentForm from "../component/CommentForm";
import CommentList from "../component/CommentList";

function Detail({ user }) {
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editing, setEditing] = useState(false);

  const { id } = useParams();

  const loadPost = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/api/board/${id}`);
      setPost(res.data);
    } catch (err) {
      console.error(err);
      setError("해당 게시글은 존재하지 않습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPost();
    loadComments();
  }, [id]);

  const [comments, setComments] = useState([]);

  const loadComments = async () => {
    try {
      const res = await api.get(`/api/comments/${id}`);

      setComments(res.data);
    } catch (err) {
      console.error(err);
      alert("댓글 리스트 불러오기 실패!");
    }
  };

  if (loading) return <p>게시글 로딩 중....</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!post)
    return <p sytle={{ color: "blue" }}>해당 게시글이 존재하지 않습니다.</p>;

  return (
    <div className="detail-container">
      {editing ? (
        <PostEdit post={post} setEditing={setEditing} setPost={setPost} />
      ) : (
        <>
          <PostView post={post} setEditing={setEditing} user={user} />

          <div className="comment-section">
            <CommentForm boardId={id} loadComments={loadComments} />

            <CommentList
              comments={comments}
              user={user}
              loadComments={loadComments}
            />
          </div>
        </>
      )}
    </div>
  );
}

export default Detail;
