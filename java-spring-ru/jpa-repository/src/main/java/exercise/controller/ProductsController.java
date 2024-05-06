package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path = "")
    public List<Product> paging(@RequestParam(defaultValue = "-1") Integer min,
                                @RequestParam(defaultValue = "-1") Integer max) {
        var sort = Sort.by(Sort.Order.asc("price"));
        if (min > 0) {
            if (max > 0) {
                return productRepository.findByPriceBetween(min, max, sort);
            } else {
                return productRepository.findByPriceGreaterThanEqual(min, sort);
            }
        } else if (max > 0) {
            return productRepository.findByPriceLessThanEqual(max, sort);
        }

        return productRepository.findAll(sort);
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
