package exercise.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import exercise.dto.ProductParamsDTO;
import exercise.model.Product;

// BEGIN
@Component
public class ProductSpecification {
    public Specification<Product> build(ProductParamsDTO params) {
        return withCategory(params.getCategoryId())
                .and(withPriceGt(params.getPriceGt()))
                .and(withPriceLt(params.getPriceLt()))
                .and(withRatingGt(params.getRatingGt()))
                .and(withTitleLike(params.getTitleCont()));
    }

    private Specification<Product> withCategory(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? cb.conjunction() : cb.equal(root.get("category").get("id"), categoryId);
    }

    private Specification<Product> withPriceGt(Integer price) {
        return (root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThan(root.get("price"), price);
    }

    private Specification<Product> withPriceLt(Integer price) {
        return (root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThan(root.get("price"), price);
    }

    private Specification<Product> withRatingGt(Double rating) {
        return (root, query, criteriaBuilder) -> rating == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThan(root.get("rating"), rating);
    }

    private Specification<Product> withTitleLike(String contain) {
        return (root, query, criteriaBuilder) -> contain == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.in(root.get("title")).value(contain);
    }
}
// END
