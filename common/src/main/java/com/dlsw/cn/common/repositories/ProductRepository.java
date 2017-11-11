package com.dlsw.cn.common.repositories;

import com.dlsw.cn.common.enumerate.ProductType;
import com.dlsw.cn.common.po.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where isOffShelf = false and productType =?1")
    List<Product> getProductListByProductType(ProductType productType);

    @Query("select p from Product p where isOffShelf = false and productCode =?1")
    Product getProductByproductCode(String productCode);

}
