package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.persist.entity.Product;
import ru.geekbrains.persist.repo.IProductRepository;
import ru.geekbrains.persist.repo.ProductSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findWithFilter(Optional<String> nameFilter,
                                        Optional<BigDecimal> minCost,
                                        Optional<BigDecimal> maxCost,
                                        Optional<Integer> page,
                                        Optional<Integer> size) {
        Specification<Product> spec = Specification.where(null);

        if (nameFilter.isPresent()) {
            spec = spec.and(ProductSpecification.nameLike(nameFilter.get()));
        }
        if (minCost.isPresent()) {
            spec = spec.and(ProductSpecification.minPrice(minCost.get()));
        }
        if (maxCost.isPresent()) {
            spec = spec.and(ProductSpecification.maxPrice(maxCost.get()));
        }
        return productRepository.findAll(spec, PageRequest.of(page.orElse(1) - 1, size.orElse(5)));
    }

    @Override
    public List<Product> findAll(Specification<Product> spec) {
        return productRepository.findAll(spec);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Transactional
    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }
}
