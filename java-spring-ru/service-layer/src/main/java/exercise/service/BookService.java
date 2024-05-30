package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    // BEGIN
    @Autowired
    BookRepository repository;
    @Autowired
    BookMapper mapper;

    public List<BookDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public BookDTO findById(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
        return mapper.map(book);
    }

    public BookDTO add(BookCreateDTO dto) {
        var book = mapper.map(dto);
        repository.save(book);
        return mapper.map(book);
    }

    public BookDTO updateById(Long id, BookUpdateDTO dto) {
        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
        mapper.update(dto, book);
        repository.save(book);
        return mapper.map(book);
    }

    public void destroyById(Long id) {
        repository.deleteById(id);
    }
    // END
}
