package Running;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import Building.TransitionMatrixFromFile;
import Building.ConfusionMatrixHere;
import Building.TrieTree;
import Running.HMModel.HMModel;
import Running.PYSeparator.PYSeparator;
import Running.PYSeparator.Separator;

class NewComparator implements Comparator<HMModel<String, String>>{
	@Override
	public int compare(HMModel<String, String> arg0, HMModel<String, String> arg1) {
		double v0 = arg0.observeProbabilityInModel(), v1 = arg1.observeProbabilityInModel();
		if(v0 > v1)
			return -1;
		if(v0 < v1)
			return 1;
		return 0;
	}
}

public class Process {
	private static boolean isFromJar = true;
	private static TrieTree TREE = TrieTree.getInstance(false);
	private static TransitionMatrixFromFile T_MATRIX = TransitionMatrixFromFile.getInstance(false);
	private static ConfusionMatrixHere C_MATRIX = ConfusionMatrixHere.getInstance();
    static Separator SEPARATOR = new PYSeparator(TREE);
	private static int DEFAULT_2_CHAR_WORD_NUM = 7;
	
	public static boolean isFromJar(){
		return Process.isFromJar;
	}
	static String list2String(List<String> l){
		String ret = "";
		for(String s : l){
			ret += s;
		}
		return ret;
	}
	
	public static Iterator<String> getCandidates(List<String> pys){
		int pyNum = pys.size();
		String first = pys.get(0);
		SortedList sl = TREE.get(first);
		Iterator<Word> iter = sl.iterator();
		ArrayList<String> chars1 = new ArrayList<String>();
		ArrayList<Double> ps = new ArrayList<Double>();
		while(iter.hasNext()){
			Word w = iter.next();
			int freq = w.getFrequency();
			if(freq < 4)
				break;
			chars1.add("" + w.getWord());
			ps.add((0.0 + freq)/(0.0 + T_MATRIX.getAllCharNum()));
		}
		
		//如果是单拼音输入
		if(pys.size() == 1){
			return chars1.iterator();
		}
		
		//多拼音输入
		PriorityQueue<HMModel<String, String>> pq = new PriorityQueue<HMModel<String, String>>(3, new NewComparator());
		HMModel<String, String> nowModel = new HMModel<String, String>(Process.T_MATRIX, Process.C_MATRIX, ps, first, chars1.iterator());
		for(int i = 1; i < pyNum; ++i){
			String py = pys.get(i);
			sl = TREE.get(py);
			iter = sl.iterator();
			ArrayList<String> chars = new ArrayList<String>();
			while(iter.hasNext()){
				Word w = iter.next();
				if(w.getFrequency() < 4)
					break;
				chars.add("" + w.getWord());
			}
			nowModel.addLayer(py, chars.iterator());
			if(i + 1 < pyNum){
				pq.add(new HMModel<String, String>(nowModel));
			}
		}
		ArrayList<String> retl = new ArrayList<String>();
		if(2 == pyNum){
			PriorityQueue<LinkedList<String>> retpq = nowModel.rankedStatusPairsListIn2Layers();
			int sz = retpq.size(), ceil = (sz < Process.DEFAULT_2_CHAR_WORD_NUM) ? sz : Process.DEFAULT_2_CHAR_WORD_NUM;
			for(int num = 0; num < ceil; num++){
				retl.add(Process.list2String(retpq.poll()));
			}
			retl.addAll(chars1);
			return retl.iterator();
		}
		retl.add(Process.list2String(nowModel.bestStatusSequence()));
		int pqsize = pq.size();
		for(int num = 0; num < pqsize; num++){
			HMModel<String, String> hmm = pq.poll();
			
			if(num == 0 && hmm.numOfLayer() == 2){
				PriorityQueue<LinkedList<String>> retpq = hmm.rankedStatusPairsListIn2Layers();
				int sz = retpq.size(), ceil = (sz < Process.DEFAULT_2_CHAR_WORD_NUM/2) ? sz : Process.DEFAULT_2_CHAR_WORD_NUM/2;
				
				for(int num1 = 0; num1 < ceil; num1++){
					retl.add(Process.list2String(retpq.poll()));
				}
			}
			else{
				retl.add(Process.list2String(hmm.bestStatusSequence()));
			}
		}
		
		retl.addAll(chars1);
		return retl.iterator();
	}
	
	public static void main(String[] args){
		System.out.println(Process.T_MATRIX.transitionProbability("喜", "还"));
		System.out.println(Process.T_MATRIX.transitionProbability("喜", "欢"));
		
	}
}
