package ait.cohort55.post.service;

import ait.cohort55.post.dto.NewCommentDto;
import ait.cohort55.post.dto.NewPostDTO;
import ait.cohort55.post.dto.PostDto;

import java.time.LocalDate;
import java.util.List;

public interface PostService {
    PostDto addNewPost(String author, NewPostDTO newPostDTO);
    PostDto findPostById(String id);
    void  addLike (String id);
    PostDto updatePost(String id, NewPostDTO newPostDTO);
    PostDto deletePost(String id);
    PostDto addComment(String id, String author, NewCommentDto newCommentDTO);
    Iterable<PostDto> findPostsByAuthor(String author);
    Iterable<PostDto> findPostsByTags(List<String> tags);
    Iterable<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo);
}
