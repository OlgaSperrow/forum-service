package ait.cohort55.post.dto;

import lombok.Getter;

import java.util.Set;
@Getter
public class NewPostDTO {
    private String title;
    private String content;
    private Set<String> tags;
}
