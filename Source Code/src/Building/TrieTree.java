package Building;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import Running.SortedList;
import Running.Word;

class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean isPinyin = false;
	SortedList candidates = new SortedList();
	ArrayList<Node> children = new ArrayList<Node>();
	ArrayList<Character> chLetters = new ArrayList<Character>();
}
public class TrieTree implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node root;
	static String TREE_PATH;
	private static TrieTree INSTANCE = null;
	public static TrieTree getInstance(boolean isBuilding){
		if(TrieTree.INSTANCE == null){
			if(isBuilding){
				TrieTree.INSTANCE = new TrieTree(true);
			}
			else if(Running.Process.isFromJar()){
				TrieTree.INSTANCE = TrieTree.restoreTreeFromJar();
			}
			else{
				TrieTree.INSTANCE = TrieTree.restroreTree();
			}
		}
		return INSTANCE;
	}
	private TrieTree(){}
	private TrieTree(boolean is){
		root = new Node();
		Properties prop = new Properties();
		try {
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TREE_PATH = prop.getProperty("tree_path");
	}
	public void add(String pinyin, char word){
		Node curr = root;
		boolean hasIt = true;
		for(int i = 0; i < pinyin.length(); ++i){
			char letter = pinyin.charAt(i);
			int index;
			if(hasIt && curr.chLetters != null && ((index = curr.chLetters.indexOf(letter)) != -1)){
				curr = curr.children.get(index);
				if(i == pinyin.length() - 1){
					if(!curr.isPinyin){
						curr.candidates.clear();
						curr.isPinyin = true;
					}
					curr.candidates.add(word);
				}
				else{
					if(!curr.isPinyin){
						curr.candidates.add(word);
					}
				}
			}
			else{
				curr.chLetters.add(letter);
				Node newN = new Node();
				if(i == pinyin.length() - 1){
					newN.isPinyin = true;
				}
				newN.candidates.add(word);
				curr.children.add(newN);
				hasIt = false;
				curr = newN;
			}
		}
	}
	public SortedList get(String py)  {
		Node curr = this.root;
		for(int i = 0; i < py.length(); i++){
			char letter = py.charAt(i);
			int index;
			if(curr.children != null && (index = curr.chLetters.indexOf(letter)) != -1){
				curr = curr.children.get(index);
				if(i == py.length() - 1){
					return curr.candidates;
				}
			}
			else{
				return null;
			}
		}  
		return null;
	}
	public int costs(String py){
		Node curr = this.root;
		for(int i = 0; i < py.length(); i++){
			char letter = py.charAt(i);
			int index;
			if(curr.children != null && (index = curr.chLetters.indexOf(letter)) != -1){
				curr = curr.children.get(index);
				if(i == py.length() - 1){
					if(curr.isPinyin)
						return 0;
					else{
						return 1;
					}
				}
			}
			else{
				return 2;
			}
		}  
		return 2;
	}
	public Iterator<Word> getLegal(String py){
		Node curr = this.root;
		for(int i = 0; i < py.length(); i++){
			char letter = py.charAt(i);
			int index;
			if(curr.children != null && (index = curr.chLetters.indexOf(letter)) != -1){
				curr = curr.children.get(index);
				if(i == py.length() - 1){
					if(curr.isPinyin)
						return curr.candidates.iterator();
					else
						return null;
				}
			}
			else{
				return null;
			}
		}  
		return null;
	}
	
	public static void storeTree(TrieTree tree){
		try{
			Properties prop = new Properties();
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
			TREE_PATH = prop.getProperty("tree_path");
			FileOutputStream fos = new FileOutputStream(new File(TREE_PATH));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tree);
			oos.flush();
			oos.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static TrieTree restroreTree(){
		try{
			Properties prop = new Properties();
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
			TREE_PATH = prop.getProperty("tree_path");
			FileInputStream fis = new FileInputStream(new File(TREE_PATH));
			ObjectInputStream ois = new ObjectInputStream(fis);
			TrieTree tree = (TrieTree)ois.readObject();
			ois.close();
			fis.close();
			return tree;
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static TrieTree restoreTreeFromJar(){
		try{
			 TREE_PATH = "trained_tree.obj";
			 InputStream is= new TrieTree().getClass().getResourceAsStream(TREE_PATH); 
			 ObjectInputStream ois = new ObjectInputStream(is);  
			 TrieTree tree = (TrieTree)ois.readObject();
			 ois.close();
			 is.close();
			 System.out.println(tree.get("hao"));
			 return tree;
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		restoreTreeFromJar();
	}
}
