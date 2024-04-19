package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;


@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public List<Post> getWithPaging(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "1") Integer limit) {
        return posts.stream()
                .skip(page - 1)
                .limit(limit)
                .toList();
    }

    @GetMapping("/posts/{postId}")
    public Optional<Post> showPost(@PathVariable String postId) {
        return posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst();
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable String postId, @RequestBody Post newPost) {
        var searchedPost = posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst();
        if (searchedPost.isPresent()) {
            var post = searchedPost.get();
            post.setId(newPost.getId());
            post.setTitle(newPost.getTitle());
            post.setBody(newPost.getBody());
        }

        return newPost;
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable String postId) {
        posts.removeIf(p -> p.getId().equals(postId));
    }
    // END
}
