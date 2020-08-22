package com.youedata.util.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;

public class JDomXml {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        createXml();
        System.out.println("运行时间："+ (System.currentTimeMillis() - start));
    }

    /**
     * 生成xml方法
     */
    public static void createXml(){
        try {
// 1、生成一个根节点
            Element rss = new Element("rss");
// 2、为节点添加属性
            rss.setAttribute("version", "2.0");
// 3、生成一个document对象
            Document document = new Document(rss);

            Element channel = new Element("channel");
            rss.addContent(channel);
            Element title = new Element("title");
            title.setText("国内最新新闻");
            channel.addContent(title);

            Format format = Format.getCompactFormat();
// 设置换行Tab或空格
            format.setIndent(" ");
            format.setEncoding("UTF-8");

// 4、创建XMLOutputter的对象
            XMLOutputter outputer = new XMLOutputter(format);
// 5、利用outputer将document转换成xml文档
            File file = new File("rssNew.xml");
            outputer.output(document, new FileOutputStream(file));

            System.out.println("生成rssNew.xml成功");
        } catch (Exception e) {
            System.out.println("生成rssNew.xml失败");
        }
    }

}
