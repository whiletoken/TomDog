package com.tomdog.base;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;
import sun.misc.Unsafe;

import java.beans.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * StringTest
 *
 * @author william
 **/

public class StringTest {

    @Test
    public void testPool() {

        String a = "123";
        String b = new String("1");
        System.out.println(b.hashCode());

        b.intern();
        System.out.println(b.hashCode());

        String c = new String("1");
        System.out.println(a.hashCode());
        String d = "1" + "2" + "3";
        System.out.println(d == a);

    }

    private static class User {
        private String name;
        private int age;

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
    }

    @Test
    public void test1() throws IntrospectionException {

        //获取 User Bean 信息
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class);

        //属性描述
        PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
        System.out.println("属性描述：");
        Stream.of(propertyDescriptors).forEach(System.out::println);

        //方法描述
        System.out.println("方法描述：");
        MethodDescriptor[] methodDescriptors = userBeanInfo.getMethodDescriptors();
        Stream.of(methodDescriptors).forEach(System.out::println);

        //事件描述
        System.out.println("事件描述：");
        EventSetDescriptor[] eventSetDescriptors = userBeanInfo.getEventSetDescriptors();
        Stream.of(eventSetDescriptors).forEach(System.out::println);
    }

    public static class VO {
        private int a = 0;
        public Long b = 6L;
        public String c = "1111111111111111111111111";
        public Object d = new Object();
        public int e = 100;
        public static int f = 0;
        public static String g = "1111111111111111111111111";
        public Object h = null;
        public boolean i;

        public VO() {
            c = "1111111111111111111111111";
        }
    }

    public static Unsafe getUnsafeInstance() throws Exception {

        // 通过反射获取rt.jar下的Unsafe类
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        // return (Unsafe) theUnsafeInstance.get(null);是等价的
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }

    /**
     * 除基本类型外都是引用类型
     */
    @Test
    public void testObject() throws Exception {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(VO.class).toPrintable());
        System.out.println("=================");
        Unsafe unsafe = getUnsafeInstance();
        VO vo = new VO();
        vo.a = 2;
        vo.b = 3L;
        vo.e = 10;
        vo.d = new HashMap<>();
        long aoffset = unsafe.objectFieldOffset(VO.class.getDeclaredField("b"));
        System.out.println("aoffset=" + aoffset);
        // 获取a的值
        int va = unsafe.getInt(vo, aoffset);
        System.out.println("va=" + va);
    }

}
