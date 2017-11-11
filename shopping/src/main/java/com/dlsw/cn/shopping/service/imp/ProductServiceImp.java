package com.dlsw.cn.shopping.service.imp;

import com.dlsw.cn.common.dto.ProductDTO;
import com.dlsw.cn.common.enumerate.ProductType;
import com.dlsw.cn.shopping.mapper.ProductMapper;
import com.dlsw.cn.common.po.Product;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.ProductRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.shopping.service.OrderService;
import com.dlsw.cn.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderService ordinaryOrderServiceImp;
    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<ProductDTO> getProductOrdinaryList(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Product> productList =productRepository.getProductListByProductType(ProductType.普通产品);
        productList.forEach(product -> {
            product.setRetailPrice(ordinaryOrderServiceImp.getProductPrice(user.getRoleType(),product));
        });
        return productMapper.productToProductDTOList(productList);
    }

    @Override
    public Iterable<ProductDTO> getProductPackageList() {
        return productMapper.productToProductDTOList(productRepository.getProductListByProductType(ProductType.套餐产品));
    }

    @Override
    public ProductDTO getProductDtoById(String phone,long id) {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(id);
        product.setRetailPrice(ordinaryOrderServiceImp.getProductPrice(user.getRoleType(),product));
        return productMapper.productToProductDTO(product);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findOne(id);
    }
}
