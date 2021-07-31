package com.yoonyeong.book.springboot.service.posts;


import com.yoonyeong.book.springboot.domain.posts.Posts;
import com.yoonyeong.book.springboot.domain.posts.PostsRepository;
import com.yoonyeong.book.springboot.web.dto.PostsListResponseDto;
import com.yoonyeong.book.springboot.web.dto.PostsResponseDto;
import com.yoonyeong.book.springboot.web.dto.PostsSaveRequestDto;
import com.yoonyeong.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;



    // Insert
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }


    // update
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(requestDto.getTitle(),requestDto.getContent());
        return id;
    }

    // find by id
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }


    //findAll
    @Transactional(readOnly=true)
    public List<PostsListResponseDto> findAllDesc(){



        return postsRepository.findAllDesc()
                .stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }



    // delete
    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts);
    }

}
