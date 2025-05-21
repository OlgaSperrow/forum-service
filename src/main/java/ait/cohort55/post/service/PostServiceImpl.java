package ait.cohort55.post.service;

import ait.cohort55.post.dao.PostRepository;
import ait.cohort55.post.dto.NewCommentDto;
import ait.cohort55.post.dto.NewPostDTO;
import ait.cohort55.post.dto.PostDto;
import ait.cohort55.post.dto.exception.PostNotFoundException;
import ait.cohort55.post.model.Comment;
import ait.cohort55.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDto addNewPost(String author, NewPostDTO newPostDTO) {
        Post post = modelMapper.map(newPostDTO, Post.class);
        post.setAuthor(author);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.addLike();
        postRepository.save(post);

    }

    @Override
    public PostDto updatePost(String id, NewPostDTO newPostDTO) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.setTitle(newPostDTO.getTitle());
        post.setContent(newPostDTO.getContent());
        Set<String> tags = newPostDTO.getTags();
        if(!tags.isEmpty()) {
            post.getTags().clear();
            post.getTags().addAll(tags);
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto deletePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto addComment(String id, String author, NewCommentDto newCommentDTO) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(author, newCommentDTO.getMessage());
        comment.setMessage(newCommentDTO.getMessage());
        comment.setUser(author);
        post.addComment(comment);
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Iterable<PostDto> findPostsByAuthor(String author) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getAuthor().equals(author))
                .collect(Collectors.toList());

        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<PostDto> findPostsByTags(List<String> tags) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getDateCreated().isAfter(dateFrom) && post.getDateCreated().isBefore(dateTo))
                .collect(Collectors.toList())
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());;
    }
}
