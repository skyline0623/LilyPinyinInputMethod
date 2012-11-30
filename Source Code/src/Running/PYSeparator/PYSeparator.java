package Running.PYSeparator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import Building.TrieTree;

public class PYSeparator implements Separator {
	TrieTree dict;
	public PYSeparator(TrieTree d){
		this.dict = d;
	}
	public ArrayList<String> separate(String str){
		ArrayList<String> ret = new ArrayList<String>();
		if(dict.costs(str) != 2){
			ret.add(str);
			return ret;
		}	
		int size = str.length();
		int[][] vMat = new int[size][size], posMat = new int[size][size];//posMat记录分割线前面一个字母的位置
		int row, col;
		for(int i = 0; i < size; i++){
			int ceil = size - i;
			col = i;
			row = 0;
			for(int j = 0; j < ceil; j++){
				if(row == col){
					vMat[row][col] = dict.costs("" + str.charAt(row));
					posMat[row][col] = row;
				}
				else{
					int min = Integer.MAX_VALUE, pos = row;

					for(int t = row; t < col; t++){
						int value = vMat[row][t] + vMat[t + 1][col] + dict.costs(str.substring(row, col + 1));
						if(value <= min){
							min = value;
							pos = t;
						}
					}
					vMat[row][col] = min;
					posMat[row][col] = pos;
				}
				col++;
				row++;
			}
		}
		
		
		LinkedList<Entry<Integer, Integer>> splits = new LinkedList<Entry<Integer, Integer>>();
		splits.add(new SimpleEntry<Integer, Integer>(0, str.length() - 1));
		while(!splits.isEmpty()){
			Entry<Integer, Integer> span = splits.pollFirst();
			int st = span.getKey(), end = span.getValue();
			String nowSt = str.substring(st, end + 1);
			if(dict.costs(nowSt) != 2){
				ret.add(nowSt);
				continue;
			}
			int sp = posMat[st][end];
			if(sp + 1 <= end)
				splits.addFirst(new SimpleEntry<Integer, Integer>(sp + 1, end));
			if(st <= sp)
				splits.addFirst(new SimpleEntry<Integer, Integer>(st, sp));
		}
		return ret;
	}
	public static void main(String[] args){
		PYSeparator sep = new PYSeparator(TrieTree.getInstance(false));
		List<String> res = sep.separate("wm");
		for(String s : res){
			System.out.println(s);
		}
	}
}
