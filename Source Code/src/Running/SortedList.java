package Running;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * 始终保持SortedList有序，按Word.frequency降序排列
 */
public class SortedList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<Word> list;
	public SortedList(){
		list = new LinkedList<Word>();
	}
	
	/**
	 * 如果list里面有这个单词，则将对应Word结点的frequency增加1，调整它的位置（如果需要）；如果list里面没有这个单词，添加这个单词到队尾
	 * @param c
	 */
	public void add(char c){
			Word w;
			int i;
			for (i = 0; i < list.size(); i++) {
				w = list.get(i);
				if (w.getWord() == c) {
					w.increase();
					list.remove(i);
					if (i == 0) {
						list.add(0, w);
						break;
					} else {
						for (int j = i - 1; j >= 0; j--) {
							list.get(j);
							if (list.get(j).getFrequency() >= w.getFrequency()) {
								list.add(j + 1, w);
								break;
							}
							if (j == 0) {
								list.add(0, w);
							}
						}
						break;
					}
				}
			}

			if(i == list.size())
				list.add(new Word(c));
		
	}
	
	public void add(Word w){
		char c = w.getWord();
		int frequency = w.getFrequency();
		for(int i = 0; i < list.size(); i++){
			Word ww = list.get(i);
			if(ww.getWord() == c)
				return;
			if(ww.getFrequency() < frequency){
				list.add(i, w);
				return;
			}
		}
		list.add(w);
	}
	public void clear(){
		list = new LinkedList<Word>();
	}
	public Iterator<Word> iterator(){
		return list.iterator();
	}
	public SortedList(SortedList nl){
		this.list = new LinkedList<Word>(nl.list);
	}
	public int size(){
		return list.size();
	}
	public static void main(String[] args){
		SortedList l = new SortedList();
		l.add('好');
		l.add('好');
		l.add('好');
		l.add('学');
		l.add('习');
		l.add('天');
		l.add('天');
		l.add('天');
		l.add('天');
		l.add('天');
		l.add('天');
		l.add('向');
		l.add('向');
		l.add('向');
		l.add('向');
		l.add('上');
		Iterator<Word> iter = l.iterator();
		while(iter.hasNext()){
			Word w = iter.next();
			System.out.println(w.getWord() + " " + w.getFrequency());
		}
	}
}
