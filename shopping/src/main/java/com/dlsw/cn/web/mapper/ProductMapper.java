package com.dlsw.cn.web.mapper;

import com.dlsw.cn.web.dto.ProductDTO;
import com.dlsw.cn.po.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productToProductDTOList(List<Product> productList);

    List<ProductDTO> productToProductDTOList(Iterable<Product> productList);
}