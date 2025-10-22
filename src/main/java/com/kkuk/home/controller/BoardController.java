package com.kkuk.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.BoardDto;

import com.kkuk.home.entity.Board;

import com.kkuk.home.entity.User;
import com.kkuk.home.repository.BoardRepository;
import com.kkuk.home.repository.CommentRepository;

import com.kkuk.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	BoardController(CommentRepository commentRepository){
		this.commentRepository = commentRepository;
	}
	
	@GetMapping
	public ResponseEntity<?> pageList(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
	if(page < 0) page = 0;
	if(size <= 0) size = 10;
	
	Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
	Page<Board> boardPage = boardRepository.findAll(pageable);

// 게시글 + 댓글 수 매핑
	List<Map<String, Object>> postsWithCommentCount = boardPage.getContent().stream().map(board -> {
	Map<String, Object> map = new HashMap<>();
	map.put("id", board.getId());
	map.put("title", board.getTitle());
	map.put("author", board.getAuthor());
	map.put("createDate", board.getCreateDate());
	map.put("viewCount", board.getViewCount());
	map.put("commentCount", commentRepository.countByBoard(board)); // 댓글 수
	return map;
	}).toList();
	
	Map<String, Object> pageResponse = new HashMap<>();
	pageResponse.put("posts", postsWithCommentCount);
	pageResponse.put("currentPage", boardPage.getNumber());
	pageResponse.put("totalPages", boardPage.getTotalPages());
	pageResponse.put("totalItems", boardPage.getTotalElements());
	
	return ResponseEntity.ok(pageResponse);
	}
	
	@PostMapping
	public ResponseEntity<?> write(@Valid @RequestBody BoardDto boardDto, 
									BindingResult bindingResult, 
									Authentication auth) {
		
		if (auth == null) {  
			return ResponseEntity.status(401).body("로그인 후 글쓰기 가능합니다.");
		}
		
		if(bindingResult.hasErrors()) { //참이면 유효성 체크 실패->error 발생
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());					
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		User user = userRepository.findByUsername(auth.getName())
				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
		
		
		Board board = new Board();
		board.setTitle(boardDto.getTitle());
		board.setContent(boardDto.getContent());
		board.setAuthor(user);
		
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isPresent()) { 
			 Board board = _board.get();
			 board.setViewCount(board.getViewCount() + 1);
			 boardRepository.save(board);
			 return ResponseEntity.ok(board);
		} else { 
			return ResponseEntity.status(404).body("해당 게시글은 존재하지 않습니다.");
		}
		
	}
	
	//특정 id 글 삭제(삭제권한->로그인한 후 본인 글만 삭제 가능)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {

		Optional<Board> _board = boardRepository.findById(id);	
		
		if (_board.isEmpty()) { //참이면 삭제할 글이 존재하지 않음			
			return ResponseEntity.status(404).body("삭제실패 게시글 여부 확인 요망");
		}
		
		if (auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("삭제실패 권한 여부 확인 요망");
		}
		
		boardRepository.delete(_board.get());
		return ResponseEntity.ok("글 삭제 성공");
		
	} 
	
	//게시글 수정(권한 설정->로그인 후 본인 작성글만 수정 가능)
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(
			@PathVariable("id") Long id, 
			@RequestBody Board updateBoard, 
			Authentication auth) {
		
		Optional<Board> _board = boardRepository.findById(id);
		
		if (_board.isEmpty()) { //참이면 수정할 글이 존재하지 않음
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
		}
		
		if (auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("해당 글에 대한 수정 권한이 없습니다.");
		}
		
		Board oldPost = _board.get(); //기존 게시글
		
		oldPost.setTitle(updateBoard.getTitle()); //제목 수정
		oldPost.setContent(updateBoard.getContent()); //내용 수정
		
		boardRepository.save(oldPost); //수정한 내용 저장
		
		return ResponseEntity.ok(oldPost); //수정된 내용이 저장된 글 객체 반환
	}
	
	
}
