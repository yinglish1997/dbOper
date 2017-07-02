package catchDbDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class getMysqlComment {

	/**
	 * @param 给定电影名
	 *从Mysql上获取电影信息：评分，时间，评论
	 */
	String driver = "";
	String url = "";
	String user = "";
	String password = "";
	public getMysqlComment(String mStr) throws IOException{
		this.driver = "com.mysql.jdbc.Driver";
		this.url =  "jdbc:mysql://192.168.235.20:3306/seeing?characterEncoding=utf8";
		this.user = "root";
		this.password = "iiip";
		File file = new File("/home/yingying/桌面/movie/movieComment/" + mStr + ".txt");
		FileWriter fileWriter = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fileWriter);
		if(!file.exists())
			file.createNewFile();
		try{
			String mid = mStr;
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if(! conn.isClosed()){
				System.out.println("success to connnect db");
				Statement statement = conn.createStatement();
				if(! isNumeric(mid)){
					String findId = "select mid from douban_movie where name = '" + mid + "'";
					//System.out.println(findId);
					ResultSet result = statement.executeQuery(findId);
					while(result.next()){
						mid = result.getString("mid");
					}
					System.out.println(mStr + "'s id is " + mid);
				}
				String sql = "select rating, time, comment from douban_comment where mid = '" + mid + "'";
				ResultSet result = statement.executeQuery(sql);
				while(result.next()){
					//System.out.println(result.getString("comment"));
					bw.write(result.getDouble("rating") + ",");
					bw.write(result.getString("time")  + ",");
					bw.write(result.getString("comment"));
					bw.write("\n");
				}
				System.out.println("the end of writer ");
				bw.close();
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public  boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	public static void main(String[] args) throws IOException {
		getMysqlComment test = new getMysqlComment("生化危机：终章");

	}

}
