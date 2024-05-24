package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @Autowired
    ProductMapper productMapper;

    @GetMapping("")
    public List<ProductDTO> showAll() {
        return productRepository.findAll().stream()
                .map(entity -> productMapper.map(entity))
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDTO showProduct(@PathVariable long id) {
        return productMapper.map(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found")));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO dto) {
        var product = productMapper.map(dto);
        productRepository.save(product);

        return productMapper.map(product);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable long id, @RequestBody ProductUpdateDTO dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        productMapper.update(dto, product);
        productRepository.save(product);
        return productMapper.map(product);
    }
    // END
}
