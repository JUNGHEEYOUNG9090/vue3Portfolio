package com.vue3Test.demo.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vue3Test.demo.DTO.PostsDTO;
import com.vue3Test.demo.repository.PostsRepository;

@Service
public class PostsService {
	@Autowired
	private PostsRepository postsRepository;
	
	public Page<PostsDTO> getAllPosts(Pageable pageable) {
		Page<Object[]> results = postsRepository.findAllPosts(pageable);
        return results.map(obj -> new PostsDTO((long)obj[0], (String) obj[1], (String) obj[2], (String) obj[3]));
    }
}
