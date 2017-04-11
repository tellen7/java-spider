package com.laowang.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/**
 * 根据关键字，到weixin.sougou.com网站上查关键字微信公众号的文章
 * 1.剖析路径，用关键字组合成路径
 * 2.通过Jsoup框架获取搜索结果的Doc
 * 3.检查源码，找到目标id，解析出目标元素
 * 4.分析目标元素，把目标元素中的目标连接放入hashmap中暂存，一共获取20篇
 * 5.用迭代器遍历map，获取每一篇文章的href，再解析出文章的html
 * 6.将文章标题，内容，分割线存入文件
 * 
 */
public class Main {

	public static void main(String[] args) {
		//关键字获取
		String keyworld = null;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("请输入关键字:");
		keyworld = scanner.next();
		scanner.close();
		
		//查询后的响应Document
		Document doc = null;
		
		//解析响应的Document，找到文章链接，并存入到map中
		Map<String,String> headHref = new HashMap<>();
		for(int i=0;i<5;i++){
			doc = search(keyworld,i);
			getArticle(headHref,doc);
		}
		
		//处理map，获取具体文章内容
		getContent(headHref);
	}
	
	//搜索功能
	public static Document search(String keyworld,int p){
		Document doc = null;
		String url = "http://weixin.sogou.com/weixin?type=2&s_from=input&query=";
		url += keyworld;
		url += "&page=" +p;
		//获取搜索结果的doc
		doc = JsoupUtils.getDoc(url);
		return doc;
	}

	//解析搜索响应的doc，生成map存放文章
	public static void getArticle(Map<String,String> map,Document doc){
		Element article = null;
		//解析文章标题和链接,存到map中
		String id = "sogou_vr_11002601_title_0";
		String head = null;
		String href = null;
		
		for(int i=0;i<=9;i++){
			id = id.substring(0, 24) + i;
			article = doc.getElementById(id);//获取到文章标题a标签元素
			
			head = article.text();
			href = article.attr("href");
			map.put(head, href);
			
		}
	}
	//获取文章内容
	public static void getContent(Map<String,String> map){
		//文件部分
		File file = new File("test.txt");
		PrintWriter pw = null;
		try {
			//创建文件字符流
			 pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println("test.txt文件打开失败");
			e.printStackTrace();
		}
		
		//文章操作
		String href = null;
		String head = null;
		int count = 0;
		for(Entry<String, String> entry: map.entrySet()){
			head = entry.getKey();
			href = entry.getValue();
			//控制台提示输出
			System.out.println("开始解析"+(++count)+": "+head);
			System.out.println("目标网址: "+href);
			//获取具体文章的doc
			Document doc = JsoupUtils.getDoc(href);
			//获取文章内容
			Element e = doc.getElementById("page-content");
			//写入文件
			pw.write("样本: "+head);
			pw.write("\r\n==================================================================\r\n");
			pw.write(e.html());
			pw.write("\r\n\n\r\n");
			System.out.println("文章"+head+"解析完成\n");
			
			//防止被服务器拒绝
			try {
				Thread.sleep(700);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		pw.close();
		System.out.println("工作完成");
	}
}
