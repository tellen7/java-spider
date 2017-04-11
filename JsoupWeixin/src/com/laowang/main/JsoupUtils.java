package com.laowang.main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtils {
	public static Document getDoc(String url){
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0")
					.timeout(3000)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ªÒ»°html ß∞‹£°«ÎºÏ≤Èurl");
		}
		
		return doc;
	}
}
