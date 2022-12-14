package com.spring.lamda;

import com.google.common.collect.Lists;
import com.spring.dto.Student;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * lambda
 */

public class LambdaTest {

    private static List<Student> students = new ArrayList<>();

    static {
        Student s1 = new Student(1L, "肖战", 15, "浙江");
        Student s2 = new Student(2L, "王一博", 15, "湖北");
        Student s3 = new Student(3L, "杨紫", 17, "北京");
        Student s4 = new Student(4L, "李现", 17, "浙江");
        Student s5 = new Student(5L, "李现1", 11, "浙江");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);
    }

    @Test
    public void testFilter() {

        // 筛选住在浙江省的学生
        List<Student> streamStudents = students.stream().filter(s -> "浙江".equals(s.getAddress())).collect(Collectors.toList());

        streamStudents.forEach(System.out::println);
    }

    @Test
    public void testMap() {

        //在地址前面加上部分信息，只获取地址输出
        List<String> addresses = students.stream().map(s -> "住址:" + s.getAddress()).collect(Collectors.toList());
        addresses.forEach(System.out::println);

        Map<Long, String> map = students.stream().collect(Collectors.toMap(Student::getId, Student::getName));

        map.keySet().forEach(System.out::println);
        map.values().forEach(System.out::println);


        Map<Long, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        System.out.println(studentMap);
    }

    @Test
    public void testDistinct() {

        //简单字符串的去重
        List<String> list = Lists.newArrayList("111", "222", "333", "111", "222");
        list.stream().distinct().forEach(System.out::println);

        // 对象去重，需要重写hashcode和equals
        students.stream().distinct().forEach(System.out::println);
    }

    @Test
    public void testSort() {

        List<String> list = Lists.newArrayList("333", "222", "111");
        list.stream().sorted().forEach(System.out::println);

        students.stream()
                .sorted((stu1, stu2) -> Long.compare(stu2.getId(), stu1.getId()))
                .sorted((stu1, stu2) -> Integer.compare(stu2.getAge(), stu1.getAge()))
                .forEach(System.out::println);
    }

    @Test
    public void testLimit() {
        List<String> list = Lists.newArrayList("333", "222", "111");
        list.stream().limit(2).forEach(System.out::println);
    }

    @Test
    public void testSkip() {
        List<String> list = Lists.newArrayList("333", "222", "111");
        list.stream().skip(2).forEach(System.out::println);
    }

    @Test
    public void testReduce() {
        List<String> list = Lists.newArrayList("欢", "迎", "你");
        String appendStr = list.stream().reduce("北京", (a, b) -> a + b);
        System.out.println(appendStr);
    }

    @Test
    public void testMin() {
        Student minS = students.stream().min(Comparator.comparingInt(Student::getAge)).get();
        System.out.println(minS.toString());
    }

    @Test
    public void testMatch() {

        boolean anyMatch = students.stream().anyMatch(s -> "湖北".equals(s.getAddress()));
        if (anyMatch) {
            System.out.println("有湖北人");
        }
        boolean allMatch = students.stream().allMatch(s -> s.getAge() >= 15);
        if (allMatch) {
            System.out.println("所有学生都满15周岁");
        }
        boolean noneMatch = students.stream().noneMatch(s -> "杨洋".equals(s.getName()));
        if (noneMatch) {
            System.out.println("没有叫杨洋的同学");
        }
    }

}
