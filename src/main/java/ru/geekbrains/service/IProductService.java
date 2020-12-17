package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.persist.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductService {

    Page<Product> findWithFilter(Optional<String> nameFilter,
                                 Optional<BigDecimal> minCost,
                                 Optional<BigDecimal> maxCost,
                                 Optional<Integer> page,
                                 Optional<Integer> size);

    List<Product> findAll(Specification<Product> spec);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    void save(Product product);

    void deleteById(Long id);

    void deleteAll();

    void saveAll(List<Product> products);
}
