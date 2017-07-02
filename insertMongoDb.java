package catchDbDate;

import java.io.IOException;
import java.text.DecimalFormat;

import EmotionAnalysis.myAnalysis.FouceAnalysis;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class insertMongoDb {

	/**
	 * 与mongoOper的唯一不同是，增加了关注点
	 */
	String[] fouces;//设置关注点序列，注意，一定要保证与FouceAnalysis类一样的顺序
	MongoFocusOper fouceAn; //要先把原文件.txt通过FouceAnalysis的分析
	String name;
	double[] numList;
	
	public insertMongoDb(String path) throws IOException{
		this.name = getName(path);
		//this.name = "XingQiuDaZhan";
		System.out.println("name : " + name);
		this.fouces = new String[]{ "制片", "投资出品", "导演","编剧","市场商业", "制作", "预告" ,
				"主题", "思想", "故事题材", "电影类型", "文化元素", "情节内容", "开头", "结局", "发展","台词", "剧情", "视听效果", "画面", "音乐", "动作", "特效"
				,"造型设计", "演员角色", "正派", "男主", "女主", "反派", "配角", "配音"};
		this.fouceAn = new MongoFocusOper(path);
		this.numList = this.fouceAn.totalComList;
		for(int i = 0; i < this.numList.length; i ++)
			System.out.println(this.fouces[i] + "   " + this.numList[i]);
		
		Mongo connection = new Mongo("192.168.235.20", 27017);
		DB db = connection.getDB("Focus");
		boolean ok = db.authenticate("root", "iiip".toCharArray());
		if(ok){
			System.out.println("db connection success !");
			DBCollection collection = db.getCollection("totalFocus");
			BasicDBObject document = new BasicDBObject();
			document.put("_id", this.name);
			for(int i = 0; i < this.numList.length; i ++){
				DecimalFormat df = new DecimalFormat("#0.0000");
				document.put(this.fouces[i], df.format(this.numList[i]));
				
			}
			collection.insert(document);
			System.out.println("document has been inserted ");
		}else{
			System.out.println("db connection fail");
		}
	}
	private String getName(String path) {
		//通过截取路径得到电影名字
		String[] array = path.split("/");	
		return array[array.length - 1].replace(".txt", "");
	}
	public static void main(String[] args) throws IOException {
		insertMongoDb insert = new insertMongoDb("/home/yingying/桌面/movie/movieComment/速度与激情8.txt");
	}

}
