package ru.geekbrains.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.persist.entity.Product;
import ru.geekbrains.persist.repo.ProductRepository;
import ru.geekbrains.persist.repo.ProductSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String indexProductPage(Model model, @RequestParam(name = "nameFilter", required = false) String nameFilter,
                                   @RequestParam(name = "minCost", required = false) String minCost,
                                   @RequestParam(name = "maxCost", required = false) String maxCost) {
        logger.info("Product page update");
        Specification<Product> spec = Specification.where(null);

        if (nameFilter != null && !nameFilter.isBlank()) {
            spec = spec.and(ProductSpecification.nameLike(nameFilter));
        }
        if((minCost == null || minCost.isBlank()) || (maxCost == null || maxCost.isBlank())){
            if((minCost == null || minCost.isBlank()) && (maxCost == null || maxCost.isBlank())){
                model.addAttribute("products", productRepository.findAll());
                return "product";
            }
            if(minCost != null && !minCost.isBlank()){
                spec = spec.and(ProductSpecification.minPrice(Integer.parseInt(minCost)));
            }

            if(maxCost != null && !maxCost.isBlank()){
                spec = spec.and(ProductSpecification.maxPrice(Integer.parseInt(maxCost)));
            }
        }else {
            spec = spec.and(ProductSpecification.betweenPrice(Integer.parseInt(minCost), Integer.parseInt(maxCost)));
        }

        model.addAttribute("products", productRepository.findAll(spec));
        return "product";
    }

    @GetMapping("/fillDB")
    public String fillDB(){
        productRepository.deleteAll();
        List<Product> products = new ArrayList<>();
        int cost = 0;
        for(var i = 1; i <= 20; i++){
            products.add(new Product(null,"product_"+i, "desc_"+i,new BigDecimal(cost+=250)));
        }
        productRepository.saveAll(products);
        return "redirect:/product";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit product with id {}", id);
        model.addAttribute("product", productRepository.findById(id));
        return "product_form";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute(new Product());
        return "product_form";
    }

    @PostMapping("/update")
    public String updateProduct(Product product) {
        productRepository.save(product);
        return "redirect:/product";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        productRepository.deleteById(id);
        return "redirect:/product";
    }
}
