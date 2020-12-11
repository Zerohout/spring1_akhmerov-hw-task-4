package ru.geekbrains.persist.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.entity.Product;

public class ProductSpecification {

    public static Specification<Product> nameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> betweenPrice(int minPrice, int maxPrice){
        return (root, query, builder) -> builder.between(root.get("price"), minPrice, maxPrice);
    }
}
