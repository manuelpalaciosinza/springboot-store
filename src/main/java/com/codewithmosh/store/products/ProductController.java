package com.codewithmosh.store.products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductsMapper productsMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ProductsMapper productsMapper, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productsMapper = productsMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam(required = false,name = "categoryId") Byte categoryId
    ) {
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findAllByCategory_Id(categoryId);
        }
        else {
            products = productRepository.findAllWithCategory();
        }
        return ResponseEntity.ok(products.stream().map(productsMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProduct(
            @PathVariable(name = "id") Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productsMapper.toDto(productOptional.get()));
    }

    @PostMapping
    ResponseEntity<ProductDto> createProduct(
            @RequestBody CreateProductRequest productDto,
            UriComponentsBuilder uriComponentsBuilder){

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productsMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        URI uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productsMapper.toDto(product));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody CreateProductRequest productDto){

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.badRequest().build();
        }
        productsMapper.update(productDto,product);
        product.setCategory(category);
        productRepository.save(product);
        return ResponseEntity.ok(productsMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(
            @PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().build();
        }
        productRepository.delete(product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
