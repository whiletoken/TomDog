package com.spring.dto;

import java.util.Objects;

/**
 * student
 *
 * @author liujunjie
 */
public class Student {

    private Long id;

    private String name;

    private int age;

    private String address;

    private ProductInfo productInfo;

    public Student(Long id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

//    @Autowired
//    public Student(ProductInfo productInfoA) {
//        this.productInfo = productInfoA;
//    }


    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(address, student.address) && Objects.equals(productInfo, student.productInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, address, productInfo);
    }
}

