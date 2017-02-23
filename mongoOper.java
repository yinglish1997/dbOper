package EmotionAnalysis.myAnalysis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class mongoOper {

	/**
	 * 把一部电影的高中低三类评分关注点存入mongodb
	 * 需要调用FouceAnalysis类，构造该类时放入电影文档地址
	 * @param args
	 */
	String[] fouces;//设置关注点序列，注意，一定要保证与FouceAnalysis类一样的顺序
	FouceAnalysis numList; //要先把原文件.txt通过FouceAnalysis的分析
	String name;
	
	public mongoOper(String path) throws IOException{
		//this.name = getName(path);
		this.name = "XingQiuDaZhan";
		this.fouces = new String[]{ "制作", "出品公司", "选景", "导演","编剧","主题", "风格", "题材内容" ,
				"剧情", "开头", "发展", "结局", "笑点", "泪点", "视听", "动作", "画面","镜头", "音乐", "角色", "男主角", "女主角", "反派", "配角"};
		this.numList = new FouceAnalysis(path);
		List<double[]> threeList = this.numList.resultList;
		//this.numList.printResult(hFinalCalculate, mFinalCalculate, lFinalCalculate);
		FullMongo(this.name, threeList);
	}
	
	private String getName(String path) {
		//通过截取路径得到电影名字
		String[] array = path.split("/");	
		return array[array.length - 1].replace(".txt", "");
	}

	public  void FullMongo(String name, List<double[]> numArray){
		//输入高中低三条关注点列表，插入数据库
		try{
			Mongo connection = new Mongo("192.168.235.20", 27017);
			DB db = connection.getDB("focusAnalysis");
			boolean ok = db.authenticate("root", "iiip".toCharArray());
			if(ok){
				System.out.println("db connection success ");
				//db.createCollection(arg0, arg1)
				DBCollection collection = db.getCollection(name);
				System.out.println(name  + "   collection has been created");
				String[] types = new String[]{"high", "middle", "low"};
				for(int i = 0; i < 3; i ++){
					double[] oneArray = numArray.get(i);
					BasicDBObject document = new BasicDBObject();
					document.put("_id", types[i]);
					for(int j = 0; j < oneArray.length; j ++){
						if(oneArray[j] != 0.0)
							document.put(fouces[j], oneArray[j]);
					}
					//如果是要替换原文件，用save
					//collection.save(document);
					collection.insert(document);
					System.out.println(types[i] + "   doucment insert success");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		mongoOper testMon = new mongoOper("/home/yingying/sqlIdFile/3434070.txt");
	}

}
