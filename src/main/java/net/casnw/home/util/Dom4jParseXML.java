/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.util;

/**
 *
 * @author minyufang
 * @date 20120823
 * @description Dom4j解析XML的常用方法
 */

import java.util.*;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Node;
import org.dom4j.DocumentHelper;
import org.dom4j.Attribute;
import org.dom4j.io.XMLWriter;

public class Dom4jParseXML {
    //通过xml文件名得到DOM
    public Document getDocument(String xmlFileName) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document d = reader.read(new File(xmlFileName));
        return d;
    }
    //重载，通过xml文件内容得到DOM
    public Document getDocument(String xmlContent, boolean b) throws
            DocumentException {
        Document d = DocumentHelper.parseText(xmlContent);
        return d;
    }

    //输出字符串
    public String transformDOM(Document d) {
        String xmlContent = "";
        xmlContent = d.asXML();
        return xmlContent;
    }

    //得到节点
    public Element getNode(Document d, String elePath, String eleValue) {
        Element ele = null;
        List l = d.selectNodes(elePath);
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            Element tmp = (Element) iter.next();
            if (tmp.getText().equals(eleValue)) {
                ele = tmp;
            }
        }
        return ele;
    }
    //重载，得到节点
    public Element getNode(Document d, String eleName) {
        Element ele = (Element) d.selectSingleNode(eleName);
        return ele;
    }

    //增加节点
    public void addNode(Element parentEle, String eleName, String eleValue) {
        Element newEle = parentEle.addElement(eleName);
        newEle.setText(eleValue);
    }

    //增加属性节点
    public void addAttribute(Element ele, String attributeName,
                             String attributeValue) {
        ele.addAttribute(attributeName, attributeValue);
    }

    //删除节点
    public void removeNode(Element parentEle, String eleName, String eleValue) {
        Iterator iter = parentEle.elementIterator();
        Element delEle = null;
        while (iter.hasNext()) {
            Element tmp = (Element) iter.next();
            if (tmp.getName().equals(eleName) && tmp.getText().equals(eleValue)) {
                delEle = tmp;
            }
        }
        if (delEle != null) {
            parentEle.remove(delEle);
        }
    }

    //删除属性
    public void removeAttr(Element ele, String attributeName) {
        Attribute att = ele.attribute(attributeName);
        ele.remove(att);
    }

    //修改节点值
    public void setNodeText(Element ele, String newValue) {
        ele.setText(newValue);
    }

    //修改属性值
    public void setAttribute(Element ele, String attributeName,
                             String attributeValue) {
        Attribute att = ele.attribute(attributeName);
        att.setText(attributeValue);
    }
  
    
    //写XML文件
    public static boolean doc2XmlFile(Document document, String filename) {
        boolean flag = true;
        try {XMLWriter writer = new XMLWriter( 
                new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
                writer.write(document);
                writer.close();
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        System.out.println(flag);
        return flag;
    } 
    
  /*  public void writeTo(OutputStream out, String encoding) 
            throws UnsupportedEncodingException, IOException {
       OutputFormat format = OutputFormat.createPrettyPrint();

       format.setEncoding("gb2312");

       XMLWriter writer = new XMLWriter(System.out,format);

       writer.write(doc);

       writer.flush();

       return;
    }
*/
    
    //遍历xml节点
    public boolean isOnly(String catNameEn,String path,String xml) throws DocumentException {
        boolean flag = true;
        Document doc;
        doc = getDocument(path+"/"+xml);
        Element root = doc.getRootElement();
        for (Iterator i = root.elementIterator(); i.hasNext();) 
        {
            Element el = (Element) i.next();
            if(catNameEn.equals(el.elementTextTrim("engName")))
            {
                flag = false;
                break;
            }
        }
        return flag;
    }
    
    
    //创建XML文件
    public static void creatXML(String args[],String fileName){
        
        Document document=DocumentHelper.createDocument();//建立document对象，用来操作xml文件
        Element booksElement=document.addElement("books");//建立根节点
        booksElement.addComment("This is a test for dom4j ");//加入一行注释
        Element bookElement=booksElement.addElement("book");//添加一个book节点
        bookElement.addAttribute("show","yes");//添加属性内容
        Element titleElement=bookElement.addElement("title");//添加文本节点
        titleElement.setText("ajax in action");//添加文本内容
        try{
            XMLWriter writer=new XMLWriter(new FileWriter(new File(fileName)));

            writer.write(document);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

