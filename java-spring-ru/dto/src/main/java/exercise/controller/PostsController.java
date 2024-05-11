package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("")
    public List<PostDTO> index() {
        return postRepository.findAll().stream()
                .map(this::getPostsDTO)
                .toList();
    }

    @GetMapping(path = "{id}")
    public PostDTO showPost(@PathVariable long id) {
        var searchedPost = postRepository.findById(id);
         if (searchedPost.isEmpty()) {
             throw new ResourceNotFoundException("Post with id " + id + " not found");
         }
         return getPostsDTO(searchedPost.get());
    }

    private PostDTO getPostsDTO(Post post) {
        var postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setBody(post.getBody());
        postDTO.setComments(commentRepository.findByPostId(post.getId()).stream()
                .map(comment -> {
                    var commentDTO = new CommentDTO();
                    commentDTO.setId(comment.getId());
                    commentDTO.setBody(comment.getBody());
                    return commentDTO;
                })
                .toList());
        return postDTO;
    }

}
// END
