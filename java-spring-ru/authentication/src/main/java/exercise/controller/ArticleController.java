package exercise.controller;

import exercise.dto.ArticleCreateDTO;
import exercise.dto.ArticleDTO;
import exercise.dto.ArticleUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ArticleMapper;
import exercise.repository.ArticleRepository;
import exercise.repository.UserRepository;
import exercise.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserUtils userUtils;


    // BEGIN
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDTO create(@RequestBody ArticleCreateDTO dto) {
        var article = articleMapper.map(dto);
        var user = userUtils.getCurrentUser();
        article.setAuthor(user);
        articleRepository.save(article);
        return articleMapper.map(article);
    }
    // END

    @GetMapping("")
    List<ArticleDTO> index() {
        var tasks = articleRepository.findAll();

        return tasks.stream()
                .map(t -> articleMapper.map(t))
                .toList();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ArticleDTO show(@PathVariable Long id) {
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return articleMapper.map(article);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ArticleDTO update(@RequestBody @Valid ArticleUpdateDTO articleData, @PathVariable Long id) {
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        articleMapper.update(articleData, article);
        articleRepository.save(article);
        return articleMapper.map(article);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        articleRepository.deleteById(id);
    }
}
