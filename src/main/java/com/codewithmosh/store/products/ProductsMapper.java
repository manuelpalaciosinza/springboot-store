package com.codewithmosh.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(CreateProductRequest request);
    void update(CreateProductRequest productRequest, @MappingTarget Product product);
}
