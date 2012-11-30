package Building;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.lang.Character;  


public class ReadCorpusV2 {
     private File f = null; 
     //public static long num_chinese = 0;
     public ReadCorpusV2(String filePath){
         f = new File(filePath);
     }

     public ArrayList<File> getAllFile(){
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
     
    public static void readFileByChar(String fileName) {
         File file = new File(fileName);
         Reader reader = null;
         try {
            // System.out.println("以字符为单位读取文件内容，一次读一个字节：");
             // 一次读一个字符
             reader = new InputStreamReader(new FileInputStream(file));
             int tempchar;
             while ((tempchar = reader.read()) != -1) {
                 if(isChinese((char)tempchar)&&(!Character.isDigit((char)tempchar))&&(Character.isLetter((char)tempchar))&&(!Character.isSpaceChar((char)tempchar)))              
                     {
	                	 //num_chinese++;          统计汉字个数
	                	 //System.out.print((char) tempchar);
	                	 //System.out.print(num_chinese);      输出汉字个数
                	 	 TransitionMatrixFromFile.getInstance(true).ReadCharIndex((char) tempchar);
                     }
                 
             }
             reader.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
         finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e1) {
                 }
             }
         }
     }
     
     public static void readFileByDoubleChar(String fileName, int type) {
    	 File file = new File(fileName);
         Reader reader = null;
         try {
            // System.out.println("以字符为单位读取文件内容，一次读一个字节：");
             // 一次读一个字符
             reader = new InputStreamReader(new FileInputStream(file));
             int tempchar;
             int firstchar = 0;
             while ((tempchar = reader.read()) != -1) {
                 if(isChinese((char)tempchar)&&(!Character.isDigit((char)tempchar))&&(Character.isLetter((char)tempchar))&&(!Character.isSpaceChar((char)tempchar)))              
                     {
                	 if(firstchar  != 0)
                	 {
                		 //System.out.print((char) firstchar);
                		 //System.out.print((char) tempchar);
                	 	 if (type == 0)
                	 		 TransitionMatrixFromFile.getInstance(true).ReadDoubleChar((char) firstchar, (char) tempchar);
                	 	 else
                 	 		TransitionMatrixFromFile.getInstance(true).ReadDoubleCharWord((char) firstchar, (char) tempchar);
              
                		 //public void ReadDoubleChar(Character firstchar, Character tempchar);
                	 }
                	 
                	 firstchar = tempchar;
                	// System.out.print((char) tempchar);
                     }
                 else
                	 firstchar = 0;
             }
             reader.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
         finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e1) {
                 }
             }
         }  
     }
     
     public static void readCorpusV2(){
    	 String folderPath = Process.corpusPath; // 读取语料库文件夹   
         ArrayList<File> listfolder =  new ReadCorpusV2(folderPath).getAllFile();

    	 // 读取拼音表
    	 String fileName = Process.pyWPath;
    	 TransitionMatrixFromFile.getInstance(true).ReadPinYinMap(fileName);
    	 
         int foldernum = listfolder.size();
         ArrayList<File> listfile = null;
         for(int i = 0;i<foldernum;i++)
         {
        	 String filePath = listfolder.get(i).getAbsolutePath();
        	 listfile = new ReadCorpusV2(filePath).getAllFile();
        	 System.out.print(listfile);
        	 int filenum = listfile.size();
        	 for(int j = 0;j<filenum;j++)
        	 {
        		ReadCorpusV2.readFileByChar(listfile.get(j).getAbsolutePath());
        	 }   	 
         }
         
         TransitionMatrixFromFile.getInstance(true).InitTMatrix();
         
         for(int i = 0;i<foldernum;i++)
         {
        	 String filePath = listfolder.get(i).getAbsolutePath();
        	 listfile = new ReadCorpusV2(filePath).getAllFile();
        	 System.out.print(listfile);
        	 int filenum = listfile.size();
        	 for(int j = 0;j<filenum;j++)
        	 {
        		 ReadCorpusV2.readFileByDoubleChar(listfile.get(j).getAbsolutePath(), 0);
        	 }   	 
         }
         
         // 在扫描一遍词语库
    	 String filePath = "wordku.txt";
    	 ReadCorpusV2.readFileByDoubleChar(filePath, 1); 
        	 
         TransitionMatrixFromFile.getInstance(true).sumUpRow();
     }
}
