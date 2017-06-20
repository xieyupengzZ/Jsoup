package jsoup.test.weibo;



import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jsoup.iyunmai.weibo.JsoupWeiboUtil;
import jxl.write.WriteException;

public class JsoupWeiboTest {

	public static void main(String[] args) {
		
		//testGetUrlParams(20);
		//testGetUserInfoForUrl();
		
		/*List<JSONObject> userInfoList = new ArrayList<JSONObject>();
		JSONObject userInfo   = new JSONObject();
		userInfo.put("userSign", "user");
		userInfo.put("userHref", "href");
		userInfo.put("fensi", "fensi");
		userInfo.put("weibo", "weibo");
		userInfo.put("care", "care");
		userInfo.put("sex", "sex");
		userInfoList.add(userInfo);
		putExcelTest(userInfoList);*/
		
		try {
			getWeibo();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("test finished");
	}
	//微博抓取测试

	public static void testGetUserInfoForUrl(){
		String url4 = "";

		Document doc =  JsoupUrl(url4);
		
		/*docList.add(doc);
		List<Document> docList = new ArrayList<Document>();
		List<Element> elements = JsoupWeiboUtil.getCheckedWeibo(docList);
		System.out.println(elements.size());
		List<JSONObject> userLink = JsoupWeiboUtil.getUserLink(elements);
		*/
		
	}
	public static  void putExcelTest(List<JSONObject> userInfoList){
		try {
			JsoupWeiboUtil.putExcel(userInfoList);
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Document JsoupUrl(String url){
		Document doc = null;
		try {
			//String bodys2 = StringEscapeUtils.unescapeHtml(doc.toString());
			Entities.EscapeMode.base.getMap().clear();
			doc = Jsoup.connect(url).ignoreContentType(true).header("Cookie", "YF-Page-G0=f9cf428f7c30a82f1d66fab847fbb873; SUB=_2A257hXDrDeTxGeRN71QT9yzPyDuIHXVY8-UjrDV8PUNbuNBeLXT2kW8HJGuoWKB2dtPVNhcCUtAGk8uU3w..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9Whcljz4vNXj-vF3PbEONRj15JpX5K2t; _s_tentry=passport.weibo.com; Apache=9721011205634.857.1451294340633; SINAGLOBAL=9721011205634.857.1451294340633; ULV=1451294340635:1:1:1:9721011205634.857.1451294340633:; YF-Ugrow-G0=5b31332af1361e117ff29bb32e4d8439; WBtopGlobal_register_version=d0bee671faf5f116; SUS=SID-2346272337-1451294907-GZ-59oy7-ca9474a13a4171834df1ba39695786e1; SUE=es%3Dc3b63129e34e0032e36cf90b61a4a22c%26ev%3Dv1%26es2%3D5a04b85ae2d56f164f4cfc9b06c9eb11%26rs0%3DQMyn3aFF1rr8GLWtaofoq%252BifmBfwDwocs0s3PKzx7zwKKBcjhjoces1RqSksUBTvswFP5Pe3e255xzWzKn8O9zOlDKySrTIaJlemMi4Zd%252F1WnmIZ8PWQI43%252FSnjq%252FjXIyZ3f%252BeIRjk7an33jlxa7aBvwh9NQvacVwFFMveGoOZ0%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1451294907%26et%3D1451381307%26d%3Dc909%26i%3D86e1%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D2346272337%26name%3D1107761900%2540qq.com%26nick%3D-%25E5%25BE%25AE%25E4%25B9%258B%25E5%258D%259A-%26fmp%3D%26lcp%3D; SUHB=0S7Ta18JJ49L0O; ALF=1451899578; SSOLoginState=1451294874; un=1107761900@qq.com; YF-V5-G0=ab4df45851fc4ded40c6ece473536bdd").header("Host", "weibo.com").header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0").header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3").header("Accept-Encoding","gzip, deflate").header("Connection", "keep-alive").timeout(10000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String bodys2 = doc.toString().replace("\\\"", "");
		Document docs = Jsoup.parseBodyFragment(bodys2);
		//Document bodyFile = JsoupWeiboUtil.bodyOut(docs.toString(),"/upload/bodycontent.txt");
		return docs;
	}
	private static void getWeibo() throws IOException{
		String url = "";
		Entities.EscapeMode.base.getMap().clear();
		Document doc = Jsoup.connect(url).ignoreContentType(true).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate, sdch").header("Cookie", "SINAGLOBAL=568225940223.7832.1447467541753; SUHB=0ft4ONXryQaoK2; SUB=_2AkMhNck6dcNhrAFSnP4Xy2nhb4pWywn0vtH3MUfcF1NCbTccg1NnqCZqthF-XYyj7S26sLt8sQS9VpqbIHZYeWaO0inzrNrY; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9Whcljz4vNXj-vF3PbEONRj15JpV2K2pehMNSo24ehW5MP2Vqcv_; _s_tentry=www.intertid.com; Apache=5951712084934.115.1452500246952; ULV=1452500246970:7:1:1:5951712084934.115.1452500246952:1449739628986; YF-V5-G0=8c4aa275e8793f05bfb8641c780e617b; UOR=www.php100.com,widget.weibo.com,v.baidu.com; YF-Page-G0=c47452adc667e76a7435512bb2f774f3")
				.header("Accept-Language", "zh-CN,zh;q=0.8").header("X-Requested-With", "XMLHttpRequest")
				.header("Connection", "keep-alive").header("Cache-Control","max-age=0")
				.timeout(20000).get();
				String content = doc.text();//得到的字符串少了很多信息
				JSONObject j = JSON.parseObject(content);
				System.out.println(j.getString("data"));
	}
	
	
	
	
	
	//新浪抓取测试
	private static void getTvSinaCn() throws IOException {
		String url = "";
		Set<TitleLink> set = new LinkedHashSet<TitleLink>();
		int page = 1;
		while (set.size() < 60) {
		Document document = getDoc(url.replace("${page}",""+page));//拿到的是json数据（中文是乱码）
			if (document != null) {
				String content = document.text();//通过text转换成string（中文是乱码）
				if (content != null) {
					content = content.replace("callbackFunction(", "").replace(")", "");
					//System.out.println(content); 
					JSONObject j =  JSON.parseObject(content);// string转化成json（中文正常）
					JSONArray jsonArray = 	j.getJSONObject("result").getJSONObject("data").getJSONArray("list");//list对象里包含数据list
					//System.out.println(jsonArray.size());
					
					if (jsonArray != null) {
						int size = jsonArray.size();
						for (int i = 0; i < size; ++i) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String title = jsonObject.getString("title");
							String link = jsonObject.getString("URL");
							set.add(new TitleLink(title, link));
						}
					}
				}
			}
			++page;
		}
		System.out.println(set.size());
		for(TitleLink tl : set){
			System.out.print(tl.title);
			System.out.println(tl.link);
		}
		System.out.println();
	}
	
	private static void getFilmSinaCn() throws IOException {
		String url = "";
		Set<TitleLink> set = new LinkedHashSet<TitleLink>();
		int page = 1;
		while (set.size() < 60) {
		Document document = getDoc(url.replace("${page}",""+page));
			if (document != null) {
				String content = document.text();
				if (content != null) {
					content = content.replace("callbackFunction(", "").replace(")", "");
					//System.out.println(content); //此处输出还是中文乱码
					JSONObject j =  JSON.parseObject(content);// 转化成json之后中文就正常了
					JSONArray jsonArray = 	j.getJSONObject("result").getJSONObject("data").getJSONArray("list");
					//System.out.println(jsonArray.size());
					
					if (jsonArray != null) {
						int size = jsonArray.size();
						for (int i = 0; i < size; ++i) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String title = jsonObject.getString("title");
							String link = jsonObject.getString("URL");
							set.add(new TitleLink(title, link));
						}
					}
				}
			}
			++page;
		}
		System.out.println(set.size());
		for(TitleLink tl : set){
			System.out.print(tl.title);
			System.out.println(tl.link);
		}
		System.out.println();
	}
	
	private static void getStarthSinaCn() throws IOException {
		String url = "";
		Set<TitleLink> set = new LinkedHashSet<TitleLink>();
		int page = 1;
		while (set.size() < 60) {
		Document document = getDoc(url.replace("${page}",""+page));
			if (document != null) {
				String content = document.text();
				if (content != null) {
					content = content.replace("callbackFunction(", "").replace(")", "");
					//System.out.println(content); //此处输出还是中文乱码
					JSONObject j =  JSON.parseObject(content);// 转化成json之后中文就正常了
					JSONArray jsonArray = 	j.getJSONObject("result").getJSONObject("data").getJSONArray("list");
					//System.out.println(jsonArray.size());
					
					if (jsonArray != null) {
						int size = jsonArray.size();
						for (int i = 0; i < size; ++i) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String title = jsonObject.getString("title");
							String link = jsonObject.getString("URL");
							set.add(new TitleLink(title, link));
						}
					}
				}
			}
			++page;
		}
		System.out.println(set.size());
		for(TitleLink tl : set){
			System.out.print(tl.title);
			System.out.println(tl.link);
		}
		System.out.println();
	}
	
	private static void getToutiaoSinaCn() throws IOException {
		String url = "";
		Set<TitleLink> set = new LinkedHashSet<TitleLink>();
		int page = 1;
		while (set.size() < 200) {
			System.out.println(page);
		Document document = getDoc(url.replace("${page}",""+page));
			if (document != null) {
				String content = document.toString();
				//System.out.println(content);
				if (content != null) {
					Elements es = document.body().select("div#ColumnContainer").select("div.pin");
					//System.out.println(es.toString());
					if (es != null) {
						int size = es.size();
						for (int i = 0; i < size; ++i) {
							Element tr = es.get(i).select("tr").get(0);
							String title = tr.select("a").attr("href");
							System.out.print(title);
							String link = tr.select("a").text();
							System.out.println(link);
							set.add(new TitleLink(title, link));
						}
					}
				}
			}
			++page;
		}
		System.out.println(set.size());
	}
	
	private static void getFilmTvStarSinaCn() throws IOException{
		String url = "";
		Set<TitleLink> set = new LinkedHashSet<TitleLink>();
		String[] sorts = {"34764","34766","34978"};
		for(String s : sorts){
			int page = 1;
			Set<TitleLink> setSort = new LinkedHashSet<TitleLink>();
			while (setSort.size() < 200) {
				System.out.println(page);
				Document document = getDoc(url.replace("${sort}", ""+s).replace("${page}",""+page));
					if (document != null) {
						String content = document.text();
						if (content != null) {
							content = content.replace("callbackFunction(", "").replace(")", "");
							
							JSONObject j =  JSON.parseObject(content);// 转化成json之后中文正常
							JSONArray jsonArray = 	j.getJSONObject("result").getJSONObject("data").getJSONArray("list");
							
							if (jsonArray != null) {
								int size = jsonArray.size();
								for (int i = 0; i < size; ++i) {
									JSONObject jsonObject = jsonArray.getJSONObject(i);
									String title = jsonObject.getString("title");
									System.out.print(title);
									String link = jsonObject.getString("URL");
									System.out.println(link);
									setSort.add(new TitleLink(title, link));
								}
							}
						}
					}
					++page;
				}
			set.addAll(setSort);
		}
		System.out.println(set.size());
	}
	
	private static class TitleLink {
		String title;
		String link;

		public TitleLink(String title, String link) {
			this.title = title;
			this.link = link;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((link == null) ? 0 : link.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TitleLink other = (TitleLink) obj;
			if (link == null) {
				if (other.link != null)
					return false;
			} else if (!link.equals(other.link))
				return false;
			if (title == null) {
				if (other.title != null)
					return false;
			} else if (!title.equals(other.title))
				return false;
			return true;
		}
	}

	public static Document getDoc(String url) throws IOException {
		if (url.indexOf("m.toutiao.com") != -1 || url.indexOf("openapi.inews.qq.com") != -1) {
			return Jsoup.connect(url).ignoreContentType(true).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8").header("Cache-Control", "max-age=0")
					.header("Connection", "keep-alive")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36")
					.timeout(20000).get();
		}
		return Jsoup.connect(url)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate, sdch").header("Accept-Language", "zh-CN,zh;q=0.8")
				.header("Cache-Control", "max-age=0").header("Connection", "keep-alive")
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36")
				.timeout(20000).get();
	}
	
}
