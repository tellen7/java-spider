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
 * ���ݹؼ��֣���weixin.sougou.com��վ�ϲ�ؼ���΢�Ź��ںŵ�����
 * 1.����·�����ùؼ�����ϳ�·��
 * 2.ͨ��Jsoup��ܻ�ȡ���������Doc
 * 3.���Դ�룬�ҵ�Ŀ��id��������Ŀ��Ԫ��
 * 4.����Ŀ��Ԫ�أ���Ŀ��Ԫ���е�Ŀ�����ӷ���hashmap���ݴ棬һ����ȡ20ƪ
 * 5.�õ���������map����ȡÿһƪ���µ�href���ٽ��������µ�html
 * 6.�����±��⣬���ݣ��ָ��ߴ����ļ�
 * 
 */
public class Main {

	public static void main(String[] args) {
		//�ؼ��ֻ�ȡ
		String keyworld = null;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("������ؼ���:");
		keyworld = scanner.next();
		scanner.close();
		
		//��ѯ�����ӦDocument
		Document doc = null;
		
		//������Ӧ��Document���ҵ��������ӣ������뵽map��
		Map<String,String> headHref = new HashMap<>();
		for(int i=0;i<5;i++){
			doc = search(keyworld,i);
			getArticle(headHref,doc);
		}
		
		//����map����ȡ������������
		getContent(headHref);
	}
	
	//��������
	public static Document search(String keyworld,int p){
		Document doc = null;
		String url = "http://weixin.sogou.com/weixin?type=2&s_from=input&query=";
		url += keyworld;
		url += "&page=" +p;
		//��ȡ���������doc
		doc = JsoupUtils.getDoc(url);
		return doc;
	}

	//����������Ӧ��doc������map�������
	public static void getArticle(Map<String,String> map,Document doc){
		Element article = null;
		//�������±��������,�浽map��
		String id = "sogou_vr_11002601_title_0";
		String head = null;
		String href = null;
		
		for(int i=0;i<=9;i++){
			id = id.substring(0, 24) + i;
			article = doc.getElementById(id);//��ȡ�����±���a��ǩԪ��
			
			head = article.text();
			href = article.attr("href");
			map.put(head, href);
			
		}
	}
	//��ȡ��������
	public static void getContent(Map<String,String> map){
		//�ļ�����
		File file = new File("test.txt");
		PrintWriter pw = null;
		try {
			//�����ļ��ַ���
			 pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println("test.txt�ļ���ʧ��");
			e.printStackTrace();
		}
		
		//���²���
		String href = null;
		String head = null;
		int count = 0;
		for(Entry<String, String> entry: map.entrySet()){
			head = entry.getKey();
			href = entry.getValue();
			//����̨��ʾ���
			System.out.println("��ʼ����"+(++count)+": "+head);
			System.out.println("Ŀ����ַ: "+href);
			//��ȡ�������µ�doc
			Document doc = JsoupUtils.getDoc(href);
			//��ȡ��������
			Element e = doc.getElementById("page-content");
			//д���ļ�
			pw.write("����: "+head);
			pw.write("\r\n==================================================================\r\n");
			pw.write(e.html());
			pw.write("\r\n\n\r\n");
			System.out.println("����"+head+"�������\n");
			
			//��ֹ���������ܾ�
			try {
				Thread.sleep(700);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		pw.close();
		System.out.println("�������");
	}
}
