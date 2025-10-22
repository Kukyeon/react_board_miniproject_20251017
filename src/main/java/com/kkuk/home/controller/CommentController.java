package com.kkuk.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.CommentDto;
import com.kkuk.home.entity.Board;
import com.kkuk.home.entity.Comment;

import com.kkuk.home.entity.User;
import com.kkuk.home.repository.BoardRepository;
import com.kkuk.home.repository.CommentRepository;

import com.kkuk.home.repository.UserRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@PostMapping("/{boardId}")
	public ResponseEntity<?> writeComment(
			@PathVariable("boardId") Long boardId, @Valid @RequestBody CommentDto commentDto,
			BindingResult bindingResult, Authentication auth){
		
		if(bindingResult.hasErrors()) { 
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());					
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		Optional<Board> _board = boardRepository.findById(boardId);
		if(_board.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("boardError", "해당 게시글이 존재하지 않습니다.");
			
			return ResponseEntity.status(404).body(error);
		}
		
		 User user = userRepository.findByUsername(auth.getName()).orElseThrow();
		 
		 Comment comment = new Comment();
		 comment.setBoard(_board.get());
		 comment.setAuthor(user);
		 comment.setContent(commentDto.getContent());
		 
		 commentRepository.save(comment);
		 
		 return ResponseEntity.ok(comment);
	}
	
	@GetMapping("/{boardId}")
	public ResponseEntity<?> getComments(@PathVariable("boardId") Long boardId){
		
		Optional<Board> _board = boardRepository.findById(boardId);
		if(_board.isEmpty()) {
			return ResponseEntity.badRequest().body("해당 게시글이 존재하지 않습니다.");
		}
		
		List<Comment> comments = commentRepository.findByBoard(_board.get());
		
		return ResponseEntity.ok(comments);
		
	}
	
	@PutMapping("/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId,
			@RequestBody CommentDto commentDto, Authentication auth){
		
		Comment comment = commentRepository.findById(commentId).orElseThrow();
		if(!comment.getAuthor().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("수정 권한이 없습니다.");
		}
		comment.setContent(commentDto.getContent());
		commentRepository.save(comment);
		
		return ResponseEntity.ok(comment);
	}
	
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId, Authentication auth){
		Optional<Comment> _comment = commentRepository.findById(commentId);
		if(_comment.isEmpty()) {
			return ResponseEntity.status(404).body("삭제 실패 댓글 존재여부 확인");
		}
		if(!_comment.get().getAuthor().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("삭제 실패 권한 여부 확인");
		}
		
		commentRepository.delete(_comment.get());
		
		return ResponseEntity.ok("삭제 완료");
	}
}