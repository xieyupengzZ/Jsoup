/*

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientLoginWeibo {

	public static Cookie[] getWCookies(String username,String password) throws HttpException, IOException{
        HttpClient client =null; 
        PostMethod post = null;
        GetMethod get = null;
        try{
            client = new HttpClient();
            client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            post = new PostMethod("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.16)");    
            String data = getServerTime();    
           String nonce = makeNonce(6);
            NameValuePair[] nvps = new NameValuePair[] {
                                new NameValuePair("entry", "weibo"),
                                new NameValuePair("gateway", "1"),
                                new NameValuePair("from", ""),
                                new NameValuePair("savestate", "7"),
                                new NameValuePair("useticket", "1"),
                                new NameValuePair("ssosimplelogin", "1"),
                                new NameValuePair("vsnf", "1"),
                                new NameValuePair("vsnval", ""),
                                new NameValuePair("su", encodeAccount(username)),
                                new NameValuePair("service", "miniblog"),
                                new NameValuePair("servertime", data),
                                new NameValuePair("nonce", nonce),
                                new NameValuePair("pwencode", "wsse"),
                                new NameValuePair("sp", new SinaSSOEncoder().encode(password, data, nonce)),
                                new NameValuePair("encoding", "UTF-8"),
                                new NameValuePair("returntype", "META"),
                                new NameValuePair(
                                        "url",
                                        "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack") };
        
                post.setRequestBody(nvps);
                client.executeMethod(post);
                String url = post.getResponseBodyAsString().substring(post.getResponseBodyAsString().indexOf("http://weibo.com/ajaxlogin.php?"),post.getResponseBodyAsString().indexOf("code=0")+6);
                get = new GetMethod(url);
                client.executeMethod(get);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            get.abort();
            post.abort();
        }
        
        return client.getState().getCookies();
    }
}
*/