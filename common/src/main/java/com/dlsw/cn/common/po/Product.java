package com.dlsw.cn.common.po;

import com.dlsw.cn.common.converter.ProductTypeConverter;
import com.dlsw.cn.common.enumerate.ProductType;
import com.dlsw.cn.common.enumerate.RoleType;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author zhanwang
 * @create 2017-08-08 13:34
 **/
@Entity
@Table(name = "t_product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_image")
    private String productImage;
    @Column(name = "send_man")
    private String sendMan;
    @Column(name = "send_man_head")
    private String sendManHead;
    @Column(name = "send_phone")
    private String sendPhone;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "retail_Price")
    private BigDecimal retailPrice;
    @Column(name = "piece")
    private Integer piece;
    @Column(name = "price1")
    private BigDecimal price1;
    @Column(name = "price2")
    private BigDecimal price2;
    @Column(name = "price3")
    private BigDecimal price3;
    @Column(name = "price4")
    private BigDecimal price4;
    @Column(name = "price5")
    private BigDecimal price5;
    @Column(name = "release_time")
    private String releaseTime;
    @Column(name = "is_off_shelf")
    private Boolean isOffShelf;
    @Column(name = "role_type")
    private RoleType roleType;
    @Convert(converter = ProductTypeConverter.class)
    @Column(name = "product_type")
    private ProductType productType;

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Boolean getOffShelf() {
        return isOffShelf;
    }

    public void setOffShelf(Boolean offShelf) {
        isOffShelf = offShelf;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public BigDecimal getPrice4() {
        return price4;
    }

    public void setPrice4(BigDecimal price4) {
        this.price4 = price4;
    }

    public BigDecimal getPrice5() {
        return price5;
    }

    public void setPrice5(BigDecimal price5) {
        this.price5 = price5;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getSendManHead() {
        return sendManHead;
    }

    public void setSendManHead(String sendManHead) {
        this.sendManHead = sendManHead;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        return productCode.equals(product.productCode);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + productCode.hashCode();
        return result;
    }
}
