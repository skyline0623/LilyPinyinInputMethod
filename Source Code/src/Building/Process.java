package Building;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class Process {
	static TrieTree TREE = new TrieTree();
	static String pyWPath;
	static String corpusPath;
	public static void building(){
		Properties prop = new Properties();
		try {
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
			pyWPath = prop.getProperty("py_word_map_path");
			corpusPath = prop.getProperty("corpus_path");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReadMapFromFile.readFileByChars(pyWPath);
		ReadCorpus.readCorpus(corpusPath);
		TrieTree.storeTree(TREE);
	}
	public static void main(String[] args) throws IOException{
		Process.building();
		
		/*TREE = TrieTree.restroreTree();  
		while(true){
			System.out.println("Pinyin Input Method:");
			byte[] b = new byte[100];
			int n = System.in.read(b);
			String temp = new String(b, 0, n);
			temp = temp.substring(0, temp.length() - 2);
			if(temp == "!q")
				break;
			Iterator<Word> iter = TREE.get(temp);
			int i = 1;
			if(iter == null){
				System.out.println("没有这样的汉字");
				continue;
			}
			while(iter.hasNext()){
				if(i == 20)
					break;
				System.out.println((i++) + ": " + iter.next());
			}
		}*/
	}
}
