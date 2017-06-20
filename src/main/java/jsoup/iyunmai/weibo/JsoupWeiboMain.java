
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jxl.write.WriteException;

import com.alibaba.fastjson.JSONObject;

public class JsoupWeiboMain {
	
		public static  void main(String[] args){
			//命令执行jar的时候，参数直接写在命令jar名 后面
			/*if(args.length == 1){
				String argString = args[0];
				if("".equals(argString)){
					
				}
			}*/
			
			List<JSONObject> userInfoList = new ArrayList<JSONObject>();
			BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));  
			String cookie = null,url = null;
			try {
				System.out.println("输入cookie :");
				cookie = strin.readLine();
				System.out.println("输入查询的url :");
				url = strin.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			CookieInfo.setCookie(cookie);
			CookieInfo.setUrl(url);
			userInfoList = JsoupWeiboUtil.jsoupAllPage();
			try {
				JsoupWeiboUtil.putExcel(userInfoList);
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
}
