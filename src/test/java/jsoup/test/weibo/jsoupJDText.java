package jsoup.test.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class jsoupJDText {

	public static void main(String[] args) {
		try {
			List<String> lists = readExcelToList();
			List<JSONObject> listjo = new ArrayList<JSONObject>();
			if(lists.size()>0){
				for(String s : lists){
					try {
						String url = "http://sssssssss/showOrderMessage?orderId="
								+ s;
						JSONObject jo = JsoupUrl(url);
						listjo.add(jo);
						System.out.println("第" + lists.indexOf(s) + "条完成");
					} catch (Exception e) {
						System.out.println("第" + lists.indexOf(s) + "条异常");
						continue;
					}
				}
			}
			if(listjo.size()>0){
				writeListToExcel(listjo);
			}
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONObject JsoupUrl(String url){
		JSONObject jo = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Document doc = null;
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).
					header("Cookie", " __jda=122270672.956368518.1466150467.1466150467.1466150909.1; VC_INTEGRATION_JSESSIONID=a4dfcafe-c7b1-485c-94c9-dfe0cbaf3530; __jdc=122270672; __jdv=122270672|direct|-|none|-; __jdu=956368518; _jrda=1; 3AB9D23F7A4B3C9B=ACD42B84A65E59AA2178BE43A99FD9B6368AB581E3ECCEBF5A18B257BCAA4C4D7F6F60544DAEAC1A7D5AAC0DC4ADAF20; thor=A5B799B6ADF35DB49AEFF34B87C5C37F3DA5D74884C6DAACE0B0AD1F4A0F015DE02012B0F57D2339C5A0259DC04E2932A0AFA98341DDFA1077362EB586CCF3867F22FE4071D578F6FD9D6F1A8DE593CB94E9D1A7720C3371AE01C7078535617D4840E595DC9681E0C6D106132007E2778DE374AE6E1CC24E6A04B883F9F3A932379DDB2E8CEE5B7137C8ACF18152075F; _tp=s8Asx%2FWH1KZ8fN1VKt54kQ%3D%3D; logining=1; unick=iyunmai; _pst=iyunmai; TrackID=19NwIHFAgZ2uAY4S9qxeaNMlHyndGj869-zTTAhQgOGzIp6L7Huan9ZtxIOb9A75I7aegqfmkseMQ59BMYAJEDo2kLfAG-ojJOZo5Ni-6Vew; pinId=zr_3fTDUBLU; ceshi3.com=-7mdAc_mevVDv6D01APRk-3-LBZHVBYAqHpdzeSlTKY; pin=iyunmai").
					header("Host", "vcp.jd.com").
					header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0").
					header("Accept","application/json, text/javascript, */*; q=0.01").
					header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3").
					header("Accept-Encoding","gzip, deflate").
					header("Connection", "keep-alive").timeout(10000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject js = JSON.parseObject(doc.text());
		JSONArray ja = js.getJSONArray("prodInfoList");
		JSONObject ja1 = ja.getJSONObject(0);
		if(ja.size()==1&&ja1.getInteger("buyNum")==1){
			jo.put("ifMore", "否");
		}else{
			jo.put("ifMore", "是");
		}
		
		Float prize = ja1.getFloat("price");
		String ifFinished = js.getString("orderStatusName");
		String finishTime = sdf.format(new Date(Long.valueOf(js.getString("orderCompleteTime"))));
		String orderid = js.getString("orderNum");
		jo.put("price", prize);jo.put("ifFinished", ifFinished);jo.put("finishTime", finishTime);jo.put("orderNum", orderid);
		return jo;
	}
	
	public static List<String> readExcel(){
		List<String> list = new ArrayList<String>();
			
		return list;
	}
	/**
	 * 从excel中读取信息
	 * @return
	 * @throws Exception
	 */
	public static List<String> readExcelToList() throws Exception{
		 File file = new File("/evpnoFile/orderid.xlsx");
		 List<String> excelContent = new ArrayList<String>();
		 if(file != null){
				FileInputStream files = new FileInputStream(file);
				Workbook rwb = new XSSFWorkbook(files);
				Sheet sheet = rwb.getSheetAt(0);
				for (int j = 0; j <= sheet.getLastRowNum(); j++) {     
		               Row row = sheet.getRow(j);     
		               if(row!=null){
		            	   String s = row.getCell(0).getStringCellValue();
		            	   excelContent.add(s);
		               }else{
		            	   excelContent.add("");
		               }
		           }
		}
		 return excelContent;
	 }
	/**
	 * 从把jsonarray写入Excel
	 * @param content
	 * @throws Exception
	 */
	 public static void writeListToExcel(List<JSONObject> content) throws Exception{
		 Workbook rwb = new XSSFWorkbook();
		 Sheet sheet = rwb.createSheet("sheet1");
		 for(int i = 0 ; i < content.size() ; i ++){
			 Row row= sheet.createRow(i);
			 Cell cell = row.createCell(0);
			 cell.setCellValue(content.get(i).getString("orderNum"));
			 Cell cell1 = row.createCell(1);
			 cell1.setCellValue(content.get(i).getString("ifMore"));
			 Cell cell2 = row.createCell(2);
			 cell2.setCellValue(content.get(i).getFloat("price"));
			 Cell cell3 = row.createCell(3);
			 cell3.setCellValue(content.get(i).getString("ifFinished"));
			 Cell cell4 = row.createCell(4);
			 cell4.setCellValue(content.get(i).getString("finishTime"));
		 }
		 FileOutputStream os = new FileOutputStream("/evpnoFile/orderidWrite.xlsx");
		 rwb.write(os);
		 os.close();
	 }
}
