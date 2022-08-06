package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.entity.Product;
import com.springsecurity.rbac.springsecurityrbac.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/findAll")
    @PreAuthorize(value = "hasAnyRole('ROLE_VIEWER','ROLE_ADMIN','ROLE_EDITOR','ROLE_CREATOR')")
    public List<Product> findAllProduct() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_VIEWER','ROLE_ADMIN','ROLE_EDITOR','ROLE_CREATOR')")
    public Product findProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_CREATOR')")
    public ResponseEntity<Product> createNewProduct(@RequestBody Product product) {
        product = productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_EDITOR')")
    public Product editProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public String deleteProductByID(@PathVariable long id) {
        productService.removeById(id);
        return "success";
    }
}
