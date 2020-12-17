package ru.geekbrains.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.entity.Product;
import ru.geekbrains.persist.repo.ProductSpecification;
import ru.geekbrains.service.IProductService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @GetMapping
    public String indexProductPage(Model model, @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minCost") Optional<BigDecimal> minCost,
                                   @RequestParam(name = "maxCost") Optional<BigDecimal> maxCost,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "sortDirection") Optional<String> sortDirection,
                                   @RequestParam(name = "fieldName") Optional<String> fieldName) {
        logger.info("Product page update");
        logger.info("Direction = " + (sortDirection.orElse(null)));
        model.addAttribute("products", productService.findWithFilter(nameFilter, minCost, maxCost, page, size, sortDirection, fieldName));
        return "product";
    }

    @GetMapping("/fillDB")
    public String fillDB() {
        productService.deleteAll();
        List<Product> products = new ArrayList<>();
        int cost = 0;
        for (var i = 1; i <= 20; i++) {
            products.add(new Product(null, "product_" + i, "desc_" + i, new BigDecimal(cost += 250)));
        }
        productService.saveAll(products);
        return "redirect:/product";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit product with id {}", id);
        model.addAttribute("product", productService.findById(id).orElseThrow(NotFoundException::new));
        return "product_form";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute(new Product());
        return "product_form";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "product_form";
        }
        productService.save(product);
        return "redirect:/product";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        productService.deleteById(id);
        return "redirect:/product";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
