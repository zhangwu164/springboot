package com.youedata.util.xml;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

public class XStreamTest {
  static XStream xStream = new XStream(new Xpp3Driver());

    public static void main(String[] args) {
        Book b = new Book();
        b.setName("冰与火之歌");
        b.setAuthor("乔治马丁");
        b.setId("1");
        b.setLanguage("English");
        b.setPrice("86");
        b.setYear("2014");

        //如果不取别名
        //  xStream.alias("person",Person.class); //可以根据自己的需要设置需要的xml文件格式
        xStream.useAttributeFor(Book.class,"personid");
        String s = xStream.toXML(b);//得到xml文件
        System.out.println(s);
        //  解析xml字符串
        Book ps = (Book) xStream.fromXML(s);//解析s
        System.out.println(ps);
    }
}
