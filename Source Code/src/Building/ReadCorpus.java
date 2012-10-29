package Building;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Character;  



public class ReadCorpus {
     public static ArrayList<File> getAllFile(File f){
          File[] fileInF = f.listFiles(); // 得到f文件夹下面的所有文件。
          ArrayList<File> list = new ArrayList<File>();
          for(File file : fileInF){
             list.add(file);
          }
          return list;
     }
     
     public static boolean isChinese(char c) {  
		  
         Character.UnicodeBlock ub = Character.UnicodeBlock.of(c); 
   
         if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
   
                 || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
   
                 || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
   
                 || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
   
                 || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
   
                 || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
   
             return true;  
   
         }  
         return false;
 	}
     
     public static void readFileByChars(String fileName) {
         File file = new File(fileName);
         Reader reader = null;
         try {
             // 一次读一个字符
             reader = new InputStreamReader(new FileInputStream(file));
             int tempchar;
             while ((tempchar = reader.read()) != -1) {
                 if(isChinese((char)tempchar)&&(!Character.isDigit((char)tempchar))&&(Character.isLetter((char)tempchar))&&(!Character.isSpaceChar((char)tempchar))){
                	 
                	 //TODO
                	 ArrayList<String> py = Res.Word2PY.getPinyin((char)tempchar);
                	 if(py != null){
                		 Iterator<String> iter = py.iterator();
                		 while(iter.hasNext()){
                			 String s = iter.next();
                			 Process.TREE.add(s, (char)tempchar);
                		 }
                	 }
                 }
             }
             reader.close();
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e1) {
                 }
             }
         }
     }
     
    
     public static void readCorpus(String folderPath){
         ArrayList<File> listfolder =  ReadCorpus.getAllFile(new File(folderPath));
        
         int foldernum = listfolder.size();
         ArrayList<File> listfile = null;
         for(int i = 0;i<foldernum;i++)
         {
        	 String filePath = listfolder.get(i).getAbsolutePath();
        	 listfile = ReadCorpus.getAllFile(new File(filePath));
        	 int filenum = listfile.size();
        	 for(int j = 0;j<filenum;j++)
        	 {
        		 ReadCorpus.readFileByChars(listfile.get(j).getAbsolutePath());
        	 }   	 
         }
     }
     public static void main(String[] args){
    	 ReadCorpus.readCorpus("SogouC.reduced/Reduced");
     }
}
