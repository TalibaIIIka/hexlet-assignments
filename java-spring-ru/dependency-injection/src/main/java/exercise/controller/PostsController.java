package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;

// BEGIN
@RestController
@RequestMapping(path = "/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("")
    public List<Post> showAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Post showPost(@PathVariable long id) {
        var searchedPost = postRepository.findById(id);
        if (searchedPost.isEmpty()) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        return searchedPost.get();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping(path = "/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody Post updPost) {
        var post = postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        post.setBody(updPost.getBody());
        post.setTitle(updPost.getTitle());

        return postRepository.save(post);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePost(@PathVariable long id) {
        var searchedPost = postRepository.findById(id);
        if (searchedPost.isPresent()) {
            var post = searchedPost.get();
            commentRepository.deleteByPostId(post.getId());
            postRepository.deleteById(id);
        }
    }
}
// END
