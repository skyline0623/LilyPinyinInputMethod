package Building;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

class Res{
	static WordLib Word2PY = new WordLib();
}

public class ReadMapFromFile {
	/**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
    
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            char[] pinyin = new char[10];
            int index = 0;
            String curstr = null;
            while ((tempchar = reader.read()) != -1) {
            	if (((char) tempchar) == '\r' || ((char) tempchar) == '\n' || ((char) tempchar) == ' ')
            	{
            		if (index != 0)
            		{
	            		curstr = (String.valueOf(pinyin)).substring(0, index);
	            		index = 0;
            		}
            	}
            	else if (((char) tempchar) >= 'a' && ((char) tempchar) <= 'z')
            	{
            		pinyin[index] = (char) tempchar;
            		++index;
            	}
            	else if (((char) tempchar) == '\'') {
            		// 开始读取各个汉字
            		while((tempchar = reader.read()) != '\'')
            		{
            			Res.Word2PY.add((char)tempchar, curstr);
            			Process.TREE.add(curstr, (char)tempchar);
            		}
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
	    String fileName = "pinyin_word.txt";
	    ReadMapFromFile.readFileByChars(fileName);
	    ArrayList<String> l = Res.Word2PY.getPinyin('蔡');
	    for(String s : l){
	    	System.out.println(s + " " + s.length());
	    }
    }
}




