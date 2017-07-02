package catchDbDate;

import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;  
import java.io.FileWriter;
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLConnection;  
import java.net.URLEncoder;  
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
public class ReadNewFocusIntoresource{  
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
    public static void main(String[] args) throws IOException {  
//    	System.out.println(isNumeric("yinglish"));
//    	System.out.println(isNumeric("11234567"));
//    	System.out.println(isNumeric("11er67"));
    	File file = new File("/home/yingying/下载/挑战杯/新关注点词典/新关注点.txt");
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	ArrayList<ArrayList<String>> allDict = new ArrayList<ArrayList<String>>();
    	try{
    		String tempStr = "";
    		while((tempStr = reader.readLine()) != null){
    			String[] splitedStr = tempStr.split(":");
    			ArrayList<String> oneFocus = new ArrayList<String>();
    			if(splitedStr.length == 1){
    				oneFocus.add(splitedStr[0]);
    			}else{
    				oneFocus.add(splitedStr[0]);
    				String[] words = splitedStr[1].split(" ");
    				for(String word: words)
    					oneFocus.add(word);
    			}
    			allDict.add(oneFocus);
    		}
    		reader.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
   
    
    	
    	for(ArrayList<String> aDict: allDict){  
    		if(aDict.size() != 0){   			  	
    			String path ="/home/yingying/下载/挑战杯/新关注点词典/" + aDict.get(0) ;
    			System.out.println( aDict.get(0));
    			File dictTxt = new File(path);  			
    			if(!dictTxt.exists()){
    					dictTxt.createNewFile();
    				}
    			BufferedWriter writer = new BufferedWriter(new FileWriter(dictTxt, true));
    			if(aDict.size() > 1){
    			try{	
    				for(int i = 1; i < aDict.size(); i ++){
    					writer.write(aDict.get(i));
    					writer.write("\n");
    				}
    				} catch(IOException e){
    					e.printStackTrace();
    				} 			
    				writer.close();
    		}
    			}	
    	}
    }	    
}
