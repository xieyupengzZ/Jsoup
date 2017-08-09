package jsoup.iyunmai.weibo;

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

public class JsoupHiydUtil {

private static String urlBase = "http://www.hiyd.com/dongzuo/?page=%";
	
	public static List<Document> JsoupUrl() {
		List<Document> docs = new ArrayList<Document>(80);
		try {
			for (int i = 1; i <= 80; i++) {
				String url = urlBase.replace("%",String.valueOf(i)); //需要用一个字符串接收，替换不是修改原字符串，是生成一个新的字符串。
				System.out.println(url);
				Entities.EscapeMode.base.getMap().clear();
				Document doc = Jsoup
						.connect(url)
						.ignoreContentType(true)
						.header("Host", "www.hiyd.com")
						.header("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36")
						.header("Accept",
								"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
						.header("Accept-Language", "zh-CN,zh;q=0.8")
						.header("Accept-Encoding", "gzip, deflate")
						.header("Connection", "keep-alive").timeout(10000)
						.get();
				docs.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs;
	}

	/**
	 * ‘.’后面的是class的名字，‘.’前面的是标签 class = o-exercise-main的div下面的 class =
	 * o-pagemod-bd 的div 里面的 所有li标签
	 */
	public static List<String[]> getExerciseAllPage(List<Document> docs) {
		List<String[]> exerciseList = new ArrayList<String[]>();
		for (Document doc : docs) {
			Elements lis = doc.body().select(
					"div.o-exercise-main div.o-pagemod-bd li");
			for (Element li : lis) {
				String[] exercise = new String[4];
				String title = li.select("div.cont span.title").html();
				exercise[0] = title;
				Elements spans = li.select("div.cont div.tag span");
				// html标签集合下标也是从0开始
				exercise[1] = spans.get(0).html();
				exercise[2] = spans.get(1).html();
				exercise[3] = spans.get(2).html();
				exerciseList.add(exercise);
			}
		}
		return exerciseList;
	}

	public static void putExcel(List<String[]> exerciseList)
			throws WriteException, IOException {

		String parentPath = "/Excel"; // 此处可以通过参数调用传入
		File file = new File(parentPath + "/exercise.xls");
		if (!file.getParentFile().exists()) { // 判断父路径（parentPath/file.getParentFile）是否存在
			System.out.println(file.getParentFile());
			new File(parentPath).mkdirs();// 不存在的父文件全部创建
		}
		file.createNewFile(); //创建文件
		OutputStream os = new FileOutputStream(file);
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("exercise", 0);
		int lineNum = 1;
		Label name = new Label(0, 0, "title");
		sheet.addCell(name);
		Label sex = new Label(1, 0, "tag1");
		sheet.addCell(sex);
		Label fensi = new Label(2, 0, "tag2");
		sheet.addCell(fensi);
		Label weibo = new Label(3, 0, "tag3");
		sheet.addCell(weibo);
		for (String[] exercise : exerciseList) {

			String title = exercise[0];
			String tag1 = exercise[1];
			String tag2 = exercise[2];
			String tag3 = exercise[3];

			Label titleLabel = new Label(0, lineNum, title);
			sheet.addCell(titleLabel);
			Label tag1Label = new Label(1, lineNum, tag1);
			sheet.addCell(tag1Label);
			Label tag2Label = new Label(2, lineNum, tag2);
			sheet.addCell(tag2Label);
			Label tag3Label = new Label(3, lineNum, tag3);
			sheet.addCell(tag3Label);
			
			lineNum++;
		}
		System.out.println("put into excel finished");
		workbook.write();
		workbook.close();
		os.close();
	}

	public static void main(String[] args) {

		try {
			List<Document> docs = JsoupUrl();
			List<String[]> exerciseList = getExerciseAllPage(docs);
			putExcel(exerciseList);
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
