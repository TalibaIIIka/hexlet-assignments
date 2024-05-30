package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.model.Author;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    // BEGIN
    @Autowired
    AuthorRepository repository;

    @Autowired
    AuthorMapper mapper;

    public List<AuthorDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public Author findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
    }

    public AuthorDTO add(AuthorCreateDTO dto) {
        var author = mapper.map(dto);
        repository.save(author);

        return mapper.map(author);
    }

    public AuthorDTO update(Long id, AuthorUpdateDTO dto) {
        var author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
        mapper.update(dto, author);
        repository.save(author);
        return mapper.map(author);
    }


    // END
}
