package Building;

import java.util.ArrayList;
import java.util.HashMap;

public class WordLib {
        
	public WordLib() {
                map = new HashMap<Character, ArrayList<String>>();
	}

	/**
	 * @author Yanan Zhang
	 */
	private HashMap<Character, ArrayList<String>> map;

        /**
         * 将汉字与其对应的拼音加入到这个字典中，多音字要加入多次
         * @param word 要加入的单个汉字
         * @param pinyin 这个汉字对应的拼音
        **/ 
	public void add(char word, String pinyin) {
		ArrayList<String> list; 
		if (map.containsKey(word)) {
			list = map.get(word);
			list.add(pinyin);
			map.put(word, list);
		} else {
                        list = new ArrayList<String>();
			list.add(pinyin);
			map.put(word, list);
		}
	}
        /**
         * @param word 输入单个的汉字
         * return 该汉字对应的拼音列表（如果找不到该汉字的话返回null；多音字返回多个拼音，单音的返回一个拼音）
        **/
	public ArrayList<String> getPinyin(char word) {
                ArrayList<String> list = null;
		if (map.containsKey(word)) {
			list = map.get(word);
		}
		return list;
	}
	public static void main(String[] args){
		WordLib l = new WordLib();
		l.add('满', "man");
		l.add('蛮', "man");
		l.add('好', "hao");
		ArrayList<String> res = l.getPinyin('满');
		for(String s : res){
			System.out.println(s);
		}
		res = l.getPinyin('好');
		for(String s : res){
			System.out.println(s);
		}
	}
}
