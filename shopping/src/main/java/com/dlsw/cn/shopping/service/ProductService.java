package com.dlsw.cn.shopping.service;

import com.dlsw.cn.common.dto.ProductDTO;
import com.dlsw.cn.common.po.Product;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface ProductService {

    Iterable<ProductDTO> getProductOrdinaryList(String phone);

    Iterable<ProductDTO> getProductPackageList();

    ProductDTO getProductDtoById(String phone,long id);

    Product getProductById(long id);
}
