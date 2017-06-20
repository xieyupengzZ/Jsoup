
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class JsoupWeiboUtil  {
	
	private static final Logger logger = LoggerFactory.getLogger(JsoupWeiboUtil.class);
	
	public static List<JSONObject> jsoupAllPage(){
		List<JSONObject> userInfoList = new ArrayList<JSONObject>();
		//------------------------first page 3 url
		//List<String> firstPageUrlList = getUrlFirstPage();
		//------------------------3 url to 3dcoument
		//List<Document> firstPageDocList = getDocList(firstPageUrlList);
		/*int j = 0;
		for(Document doc : firstPageDocList){
			j++;
			bodyOut(doc.toString(),"/upload/"+j+".txt");
		}*/
		//------------------------all html of 3 document
		//List<String> firstPageHtmlList = getHtmlEle(firstPageDocList);
		//------------------------search pageNum
		//int pageNum = getPageNum(firstPageHtmlList);
		//System.out.println( pageNum+"page");
		
		//------------------------all url
		List<String> allUrlList = getAllUrl(5);
		System.out.println("url number:"+allUrlList.size());
		//------------------------all document
		List<Document> allPageDocList = getDocList(allUrlList);
		System.out.println("url connect finished");
		//------------------------select weibo
		List<Element> allOkElements = getCheckedWeibo(allPageDocList);
		System.out.println("weibo number:"+allOkElements.size());
		
		//------------------------username and userhref
		List<JSONObject> allUserLink = JsoupWeiboUtil.getUserLink(allOkElements);
		System.out.println("user number:"+allUserLink.size());
		//------------------------userinfo
		for(JSONObject ul : allUserLink){
			String url =(String) ul.get("userHref");
			List<Document> docList2 = new ArrayList<Document>();
			Document docu = JsoupWeiboUtil.JsoupUrl(url);
			docList2.add(docu);
			List<String> htmlEle = new ArrayList<String>();
			htmlEle = JsoupWeiboUtil.getHtmlEle(docList2);
			JSONObject userinfo = new JSONObject();
			userinfo = JsoupWeiboUtil.getUserInfo(ul,htmlEle);
			userInfoList.add(userinfo);
		}
		System.out.println("get userinfo finished");
		return userInfoList;
	}
	/**
	 *   把从urllist中获取document
	 */
	public static List<Document> getDocList(List<String> urlList){
		List<Document> docList = new ArrayList<Document>();
		for(String url : urlList){
			Document doc = JsoupUrl(url);
			docList.add(doc);
		}
		return docList;
	}
	
	/**
	 * 从doclist中的script中查询html
	 */
  public static List<String> getHtmlEle(List<Document> bodys){
	   
	  List<String> htmlS = new ArrayList<String>();
	  //int i = 0;
	  	for(Document doc : bodys){
	  		//i++;
	  		Elements objEle = doc.select("script");
			//System.out.print("script size:"+objEle.size());
			String fengeSign = ",\"html\":\"";
			String[] list = null;
			
			for(Element ele : objEle){
				//①select html from script
				if(ele.toString().contains(fengeSign)){
					//②split html 
					list = ele.toString().split(fengeSign);
					String s1 = list[1].replace("\\t", "").replace("\\n", "").replace("\\r", "");
					String s2 = s1.replace("\\", "").replace("\"}]&lt;//script&gt;", "");
					htmlS.add(s2);
				}
			}
	  	}
		//System.out.println("html size:"+htmlS.size());
		//System.out.println("document size:"+i);
		return htmlS;
	}
	/**
	 * 从首页中获取页数
	 */
  public static int getPageNum(List<String> htmlList){
	  int pageNum = 1;
	  		for(String html : htmlList){
	  			if(html.contains("class=\"WB_cardwrap S_bg2\"")){
	  				if(html.contains("class = \"W_pages\"")){
	  					Document doc = Jsoup.parseBodyFragment(html);
	  					pageNum = doc.select("div.WB_cardwrap div.W_pages span.list li").size();
	  				}
	  			}
	  		}
	  return pageNum;
  }
  
  /**
	 * select weibo
	 * @param bodys
	 * @return
	 */
	public static List<Element> getCheckedWeibo(List<Document> bodys){
		
		List<String> htmlString = new ArrayList<String>();
		htmlString = getHtmlEle(bodys);
		System.out.println("select html finished");
		int m = 0,n = 0,p = 0,q=0;
		List<Element> allSelectEle = new ArrayList<Element>();
		for(String htmls : htmlString){
			//①select class=WB_cardwrap WB_feed_type S_bg2 from html
			if(htmls.contains("class=\"WB_cardwrap WB_feed_type S_bg2\"")){
				Document doc =  Jsoup.parseBodyFragment(htmls);
				//②select weibo
				Elements selectEles = doc.body().select("div.WB_cardwrap").select("div[diss-data=filter_actionlog=hot]");
				q+=selectEles.size();
				//③select user is ok from weibo 
				List<Element> deletelist = new ArrayList<Element>();
							for(Element selectEle : selectEles){
								Elements EleV = selectEle.select("a[href=http://verified.weibo.com/verify]");//if have V
								Elements EleImg = selectEle.select("div.WB_detail div.WB_media_wrap img");//if have image
								String EleZan = selectEle.select("div.WB_feed_handle li").get(3).select("em").val();//if zan
								if(EleV.size() > 0){
									m++;
									deletelist.add(selectEle);//is not ok element
								}else if(EleImg.size() <= 0){
									deletelist.add(selectEle);
								}else if(EleZan!=null&&!("").equals(EleZan)&&Integer.parseInt(EleZan) < 0){
									deletelist.add(selectEle);
								}
								if(EleImg.size() <= 0 ){
									n ++;
								}
								if(EleZan!=null&&!("").equals(EleZan)&&Integer.parseInt(EleZan) <= 0){
									p ++;
								}
							}
							
				for(Element delEle : deletelist){
					selectEles.remove(delEle);
				}
				//④ok weibo put into allSelectEle
				for(Element saveEle : selectEles){
					allSelectEle.add(saveEle);
				}
			}
		}
		System.out.println("hot weibo number:"+q);
		System.out.println("not have V:"+m);
		System.out.println("not have image:"+n);
		System.out.println("not have zan:"+p);

		return allSelectEle;
	}

	/**
	 * 获取合格用户的主页链接
	 */
	public static List<JSONObject> getUserLink(List<Element> listEle){
		List<JSONObject> userInfoList = new ArrayList<JSONObject>();
		List<String> userSignList = new ArrayList<String>();
		String userSign = null,userHref = null;
		for(Element ele : listEle){
			JSONObject userinfo = new JSONObject();
			userSign = ele.select("div.WB_info a").get(0).text();
			userHref = ele.select("div.WB_info a").get(0).attr("href");
			
			if(!userSignList.contains(userSign)){
				userSignList.add(userSign);
				userinfo.put("userSign", userSign);
				userinfo.put("userHref", userHref);
				userInfoList.add(userinfo);
			}
		}
		for(JSONObject u : userInfoList){
			/*System.out.print(u.get("userSign"));
			System.out.print("===");
			System.out.println(u.get("userHref"));*/
		}
		System.out.println("");
		return userInfoList;
	}
	
	/**
	 * 获取用户信息
	 */
	public static JSONObject getUserInfo(JSONObject userLink,List<String> htmlString){
		int n = 0,m=0;
		JSONObject jsonInfo = new JSONObject();
		for(String htmls : htmlString){
			//weibo fensi care
			if(htmls.contains("class=\"PCD_counter\"")){
				n++;
				Document docNum = Jsoup.parse(htmls);
				Elements infoNum = docNum.select("div.PCD_counter td");
				jsonInfo.put("care",infoNum.get(0).select("strong").text());
				jsonInfo.put("fensi",infoNum.get(1).select("strong").text());
				jsonInfo.put("weibo",infoNum.get(2).select("strong").text());
				/*for(Element num : infoNum){
					String name = num.select("span").text();
					String number = num.select("strong").text();
					jsonInfo.put(name,num.select("strong").text());
					}*/
			}
			//sex
			if(htmls.contains("class=\"pf_username\"")){
					m++;
					Document docSex = Jsoup.parse(htmls);
					String  infoSex = docSex.select("div.pf_username i").attr("class");
					if("W_icon icon_pf_female".equals(infoSex)){
						jsonInfo.put("sex", "w");
					}else if("W_icon icon_pf_male".equals(infoSex)){
						jsonInfo.put("sex", "m");
					}
			}
		}
		jsonInfo.put("href", userLink.get("userHref"));
		jsonInfo.put("username", userLink.get("userSign"));
		return jsonInfo;
	}
	
	/**
	 * Jsoup content url
	 */
	public static Document JsoupUrl(String url){
		Document doc = null;String cookie = CookieInfo.getCookie();
		try {
			//String bodys2 = StringEscapeUtils.unescapeHtml(doc.toString());//remove &quot; &gt;%lt;
			Entities.EscapeMode.base.getMap().clear();
			doc = Jsoup.connect(url).ignoreContentType(true).header("Cookie", cookie).header("Host", "weibo.com").header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0").header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3").header("Accept-Encoding","gzip, deflate").header("Connection", "keep-alive").timeout(10000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String bodys2 = doc.toString().replace("\\\"", "\"");// class \" to "
		Document docs = Jsoup.parseBodyFragment(bodys2);
		//Document bodyFile = JsoupWeiboUtil.bodyOut(docs.toString(),"/upload/bodycontent.txt");
		return docs;
	}
	/**
	 *获取所有的url链接
	 */
	public static List<String> getAllUrl(int pageNum){
		List<String> urlParamsList = new ArrayList<String>();
		//String url1 = "100808aa81502265ec2c621612bb0f503117f5";
		//String url2 = "10080804c8a8072bf74ee060e2ed8536b4903d";
		//String url3 = "1008080f7b92fb313170b42c4a22334987f515";
		//String url4 = "100808c24d6f13a44a53dcaff9093c7ce4f879";
		String url5 = CookieInfo.getUrl();
		List<String> UrlTypeList = new ArrayList<String>();
		UrlTypeList.add(url5);
		//UrlTypeList.add(url2);
		//UrlTypeList.add(url3);
		//UrlTypeList.add(url4);
		//url
		for(String urlType : UrlTypeList){
			long  MaxSinceId = 400000000;
			for(int page = 1 ; page<=pageNum ; page++){
					int FirCurPag = (page-1)*3;
					String url = "http://ssss.com/p/"+urlType+"?current_page="+FirCurPag+"&since_id=2"+MaxSinceId+"&page="+page+"#Pl_Third_App__9";
					MaxSinceId = MaxSinceId - 45;
					urlParamsList.add(url);
					/*while(FirCurPag<(page*3)){
					MaxSinceId = MaxSinceId - 15;
					String url = "http://weibo.com/p/100808aa81502265ec2c621612bb0f503117f5?current_page="+FirCurPag+"&since_id=2"+MaxSinceId+"&page="+page+"#Pl_Third_App__9";
					urlParamsList.add(url);
					FirCurPag ++;
				}*/
			}
		}
		return urlParamsList;
	}
	/**
	 * 首页3段url
	 */
	public static List<String> getUrlFirstPage(){
		List<String> urlList = new ArrayList<String>();
		String url1 = "";
		String url2 = "";
		String url3 = "";
		urlList.add(url1);
		urlList.add(url2);
		urlList.add(url3);
		return urlList;
	}
	/**
	 * 创建新文件，包括父路径
	 */
	public static File createFileWithParentPath(String parentPath,String fileName){
		File file = new File(parentPath + "/"+fileName);
		if(!file.getParentFile().exists()){//判断父路径（parentPath/file.getParentFile）是否存在
			new File(parentPath).mkdirs();//不存在的父文件全部创建
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	/**
	 * 用户信息输出到Excel
	 */
	public static void putExcel(List<JSONObject> userInfolist) throws WriteException,IOException{
		
		String parentPath = "/Excel"; //此处可以通过参数调用传入
		File file = new File(parentPath+"/userinfo.xls");
		if(!file.getParentFile().exists()){ //判断父路径（parentPath/file.getParentFile）是否存在
			System.out.println(file.getParentFile());
			new File(parentPath).mkdirs();//不存在的父文件全部创建
		}
		file.createNewFile();
		OutputStream os = new FileOutputStream(file);
			WritableWorkbook workbook = Workbook.createWorkbook(os);  
			 WritableSheet sheet = workbook.createSheet("userInfo",0); 
			 int lineNum = 1;
		        Label name = new Label(0,0,"user");
		        sheet.addCell(name);
		        Label sex = new Label(1,0,"sex");
		        sheet.addCell(sex);
		        Label fensi = new Label(2,0,"fensi");
		        sheet.addCell(fensi);
		        Label weibo = new Label(3,0,"weibo");
		        sheet.addCell(weibo);
		        Label care = new Label(4,0,"care");
		        sheet.addCell(care);
		        Label href = new Label(5,0,"href");
		        sheet.addCell(href);
			 for(JSONObject user: userInfolist){
				 	
				 	String userName = user.getString("username");
				 	String userSex = user.getString("sex");
				 	String userFensi = user.getString("fensi");
				 	String userWeibo = user.getString("weibo");
				 	String userCare = user.getString("care");
				 	String userHref = user.getString("href");
				 	
			        Label nameLabel = new Label(0,lineNum,userName);
			        sheet.addCell(nameLabel);
			        Label sexLabel = new Label(1,lineNum,userSex);
			        sheet.addCell(sexLabel);
			        Label fensiLabel = new Label(2,lineNum,userFensi);
			        sheet.addCell(fensiLabel);
			        Label weiboLabel = new Label(3,lineNum,userWeibo);
			        sheet.addCell(weiboLabel);
			        Label careLabel = new Label(4,lineNum,userCare);
			        sheet.addCell(careLabel);
			        Label hrefLabel = new Label(5,lineNum,userHref);
			        sheet.addCell(hrefLabel);
			       
			        lineNum ++;
			 }
			 System.out.println("put into finished");
		        workbook.write();
		        workbook.close();
		        os.close();
	}
	/**
	 *字符串输入到txt
	 */
	public static Document bodyOut(String bodycontent,String filename){
		
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Writer out = null;
		Document doc = null;
		try {
			out = new FileWriter(file,true);
			out.write(bodycontent);
			doc = Jsoup.parse(file, "UTF-8","");
			out.close();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
