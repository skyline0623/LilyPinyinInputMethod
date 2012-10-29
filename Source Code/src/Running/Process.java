package Running;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;

import Building.TrieTree;

public class Process {
	private static String alph = "abcdefghijklmnopqrstuvwxyz";
	//public static TrieTree TREE = new TrieTree().restoreTreeFromJar();
	public static TrieTree TREE = TrieTree.restroreTree();
	private static HashSet<String> knownEdit1(String word){
		char st = word.charAt(0);
		word = word.substring(1);
		HashSet<String> res = new HashSet<String>();
		ArrayList<Entry<String, String>> splits = new ArrayList<Entry<String, String>>(word.length() + 1);
		for(int i = 0; i < word.length() + 1; i++)//split
			splits.add(new SimpleEntry<String, String>(word.substring(0, i), word.substring(i)));
		for(Entry<String, String> e : splits){
			String a = e.getKey(), b = e.getValue(), temp = null;
			if(b.length() > 0){//deletes & replaces
				temp = a + b.substring(1);
				if(TREE.getLegal(st + temp) != null)
					res.add(st + temp);
				for(int i = 0; i < 26; i++){
					temp = a + alph.charAt(i) + b.substring(1);
					if(TREE.getLegal(st + temp) != null)
						res.add(st + temp);
				}
				if(b.length() > 1){//transpose
					temp = a + b.charAt(1) + b.charAt(0) + b.substring(2);
					if(TREE.getLegal(st + temp) != null)
						res.add(st + temp);
				}
			}
			for(int i = 0; i < 26; i++){//insert
				temp = a + alph.charAt(i) + b;
				if(TREE.getLegal(st + temp) != null)
					res.add(st + temp);
			}
		}
		return res;
	}
	private static HashSet<String> knownEdits2(String word){
		HashSet<String> res = new HashSet<String>();
		HashSet<String> edit1Res = knownEdit1(word);
		for(String s : edit1Res)
			for(String ss : knownEdit1(s))
				res.add(ss);
		return res;
	}
	public static Iterator<Word> getCandidates(String py){
		Iterator<Word> iter = TREE.get(py);
		if(iter != null)
			return iter;
		
		//debug
		System.out.println("correcting");
		
		HashSet<String> candidates = knownEdit1(py);
		if(candidates.size() == 0){
			candidates = knownEdits2(py);
		}
		if(candidates.size() == 0){
			return null;
		}
		SortedList ret = new SortedList();
		for(String s : candidates){
			Iterator<Word> iter1 = TREE.get(s);
			while(iter1.hasNext()){
				ret.add(iter1.next());
			}
		}
		return ret.iterator();
	}
	public static void main(String[] args){
		Iterator<Word> it = Process.getCandidates("haw");
		while(it.hasNext()){
			Word w = it.next();
			System.out.println(w);
		}
	}
}
