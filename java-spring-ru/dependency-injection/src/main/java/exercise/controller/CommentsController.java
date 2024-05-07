package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;

// BEGIN
@RestController
@RequestMapping(path = "/comments")
public class CommentsController {
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("")
    public List<Comment> showComments() {
        return commentRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Comment showCommentById(@PathVariable long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @PutMapping(path = "/{id}")
    public Comment udpdateComment(@PathVariable long id, @RequestBody Comment updComment) {
        var searchedComment = commentRepository.findById(id);
        if (searchedComment.isPresent()) {
            var comment = searchedComment.get();
            comment.setBody(updComment.getBody());
            comment.setPostId(updComment.getPostId());
            return commentRepository.save(comment);
        }
        return updComment;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteComment(@PathVariable long id) {
        commentRepository.deleteById(id);
    }
}
// END
