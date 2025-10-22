package com.kkuk.home.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.Board;
import com.kkuk.home.entity.Comment;



public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByBoard(Board board);
	
	Long countByBoardId(Long boardId);
	
	int countByBoard(Board board);
}
