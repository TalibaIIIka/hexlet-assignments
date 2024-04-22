package exercise.controller.users;

import exercise.Data;
import exercise.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private final List<Post> posts = Data.getPosts();

    @GetMapping("/users/{userId}/posts")
    public List<Post> usersPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @PathVariable int userId) {

        return posts.stream()
                .filter(post -> post.getUserId() == userId)
                .toList();
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<Post> addPostToUser(@PathVariable int userId, @RequestBody Post newPost) {
        newPost.setUserId(userId);
        posts.add(newPost);

        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }
}
// END
