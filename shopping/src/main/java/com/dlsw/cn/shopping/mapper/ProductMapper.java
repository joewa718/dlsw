package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.dto.ProductDTO;
import com.dlsw.cn.common.po.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productToProductDTOList(List<Product> productList);

    List<ProductDTO> productToProductDTOList(Iterable<Product> productList);
}