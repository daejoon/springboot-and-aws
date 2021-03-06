package com.ddoong2.service.posts;

import com.ddoong2.domain.posts.Posts;
import com.ddoong2.domain.posts.PostsRepository;
import com.ddoong2.web.dto.PostsResponseDto;
import com.ddoong2.web.dto.PostsSaveRequestDto;
import com.ddoong2.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {

        return postsRepository.findById(id)
                .map(PostsResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id" + id));

    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllDesc() {

        return postsRepository.findAllDesc().stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id" + id));

        postsRepository.delete(posts);
    }

}
