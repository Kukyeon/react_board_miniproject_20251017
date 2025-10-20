package com.kkuk.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	
}
