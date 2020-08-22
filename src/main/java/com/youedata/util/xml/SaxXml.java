package com.youedata.util.xml;

import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaxXml {

    public static void main(String[] args) {
        List<Book> bList = new ArrayList<Book>();
        Book b = new Book();
        b.setName("冰与火之歌");
        b.setAuthor("乔治马丁");
        b.setId("1");
        b.setLanguage("English");
        b.setPrice("86");
        b.setYear("2014");
        bList.add(b);
        Long start = System.currentTimeMillis();
        createXml(bList);
        System.out.println("运行时间："+ (System.currentTimeMillis() - start));
    }

    // 生成xml
    public static void createXml(List<Book> bList){
// 1、创建一个SAXTransformerFactory类的对象
        SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        try {
// 2、通过SAXTransformerFactory创建一个TransformerHandler的对象
            TransformerHandler handler = tff.newTransformerHandler();
// 3、通过handler创建一个Transformer对象
            Transformer tr = handler.getTransformer();
// 4、通过Transformer对象对生成的xml文件进行设置
// 设置编码方式
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
// 设置是否换行
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
// 5、创建一个Result对象
            File f = new File("src/newbooks.xml");
// 判断文件是否存在
            if(!f.exists()){
                f.createNewFile();
            }
            Result result = new StreamResult(new FileOutputStream(f));
// 6、使RESULT与handler关联
            handler.setResult(result);

// 打开document
            handler.startDocument();
            AttributesImpl attr = new AttributesImpl();
            handler.startElement("", "", "bookstore", attr);
            attr.clear();

            for (Book book : bList) {
                attr.clear();
                attr.addAttribute("", "", "id", "", book.getId());
                handler.startElement("", "", "book", attr);

// 创建name
                attr.clear();
                handler.startElement("", "", "name", attr);
                handler.characters(book.getName().toCharArray(), 0, book.getName().length());
                handler.endElement("", "", "name");

// 创建year
                attr.clear();
                handler.startElement("", "", "year", attr);
                handler.characters(book.getYear().toCharArray(), 0, book.getYear().length());
                handler.endElement("", "", "year");

// 创建author
                if(book.getAuthor() != null && !"".equals(book.getAuthor().trim())){
                    attr.clear();
                    handler.startElement("", "", "author", attr);
                    handler.characters(book.getAuthor().toCharArray(), 0, book.getAuthor().length());
                    handler.endElement("", "", "author");
                }

// 创建price
                if(book.getPrice() != null && !"".equals(book.getPrice().trim())){
                    attr.clear();
                    handler.startElement("", "", "price", attr);
                    handler.characters(book.getPrice().toCharArray(), 0, book.getPrice().length());
                    handler.endElement("", "", "price");
                }

// 创建language
                if(book.getLanguage() != null && !"".equals(book.getLanguage().trim())){
                    attr.clear();
                    handler.startElement("", "", "language", attr);
                    handler.characters(book.getLanguage().toCharArray(), 0, book.getLanguage().length());
                    handler.endElement("", "", "language");
                }

                handler.endElement("", "", "book");
            }

            handler.endElement("", "", "bookstore");
// 关闭document
            handler.endDocument();
            System.out.println("生成newbooks.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成newbooks.xml失败");
        }
    }
}
