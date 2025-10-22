package com.kkuk.home.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String content;
	
	private Long viewCount = 0L;
	
	@CreationTimestamp
    private LocalDateTime createDate;
	
	@ManyToOne
    private User author;
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties({"board", "author"})
	private List<Comment> comments = new ArrayList<>();

}
