package com.spring.dto;

import java.util.Objects;

/**
 * 产品信息
 *
 * @author home
 */
public class ProductInfo {

    private String name;

    private Integer productId;

    private Student student;

//    @Autowired
//    public ProductInfo(Student studentA) {
//        this.student = studentA;
//    }


    public ProductInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInfo that = (ProductInfo) o;
        return Objects.equals(name, that.name) && Objects.equals(productId, that.productId) && Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, productId, student);
    }
}
