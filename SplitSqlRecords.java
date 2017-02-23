package EmotionAnalysis.myAnalysis;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;  
import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;  
import scala.Tuple2;

public class SplitSqlRecords {

	/**
	 * spark读取mysql数据库（豆瓣）并把所有数据以电影id聚合
	 * ，得到各自的文件（评分，时间，评论），为后面的统计做准备
	 * @param args
	 */
		static SparkConf conf ;
		static JavaSparkContext sc ;	
		public SplitSqlRecords(){
			this.conf = new SparkConf().setMaster("local").setAppName("splitSqlRecords") ;
			this.sc  = new JavaSparkContext(conf);
			connAndSplit();
		}
	
	public static void connAndSplit(){
		//连接数据库，并切分数据，存储为文件
		try{
			SQLContext sqlContext = new SQLContext(sc);		
			//设置连接的参数，连接mysql数据库
			String url = "jdbc:mysql://192.168.235.20:3306/downloader";
			String table = "douban_comment";
			Properties connectionProperties = new Properties();
			connectionProperties.setProperty("dbtable", table);
			connectionProperties.setProperty("user", "root");
			connectionProperties.setProperty("password", "iiip");
			//读取所有数据，Row表示每一行记录
			Dataset<Row> jdbcDF = sqlContext.read().jdbc(url, table, connectionProperties);
			JavaRDD<Row> rowsRDD = jdbcDF.javaRDD();
			//转换为【电影id：（评分，时间，评论）】的键值对
			JavaPairRDD<String, String> timeRecordPRDD = rowsRDD.mapToPair(new createPair());
			//相同电影id聚合
			JavaPairRDD<String, Iterable<String>> timeIterablePRDD = timeRecordPRDD.groupByKey();
			List<Tuple2<String, Iterable<String>>> pairCollection = timeIterablePRDD.collect();
			//写入文件
			for(Tuple2<String, Iterable<String>> tup: pairCollection){
				fileWrite(tup);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
//	public static void splitFile(){
//		SQLContext sqlContext = new SQLContext(sc);
//		
//	}
	
	public static void fileWrite (Tuple2<String, Iterable<String>>  tup){
		//写入文件
		try{
			File file = new File("/home/yingying/sqlIdFile/" + tup._1() + ".txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWritter = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fileWritter);
			for(String s: tup._2()){
				//System.out.println(s);
				bw.write(s + "\n");
			}
			bw.close();			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static class createPair implements PairFunction<Row, String, String>{
		public Tuple2<String, String> call(Row r){
			return new Tuple2<String, String>(String.valueOf(r.get(1)), r.get(4) + "," + r.get(6) + "," + r.get(7));
		}
	};
	
	public static void main(String[] args) {
		SplitSqlRecords sp = new SplitSqlRecords();

	}


	
}
