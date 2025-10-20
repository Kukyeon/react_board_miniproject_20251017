package com.kkuk.home.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.Comment;
import com.kkuk.home.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByPost(Post post);
	
	Long countByPostId(Long postId);
	
}
