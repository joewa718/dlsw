package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.dto.ProductDTO;
import com.dlsw.cn.enumerate.ProductType;
import com.dlsw.cn.web.mapper.ProductMapper;
import com.dlsw.cn.po.Product;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.ProductRepository;
import com.dlsw.cn.repositories.UserRepository;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.web.service.ProductService;
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
