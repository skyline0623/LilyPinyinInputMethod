package Running;

import java.io.Serializable;

/*
 * 每个汉字的数据结构
 */

public class Word implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char ch;
	private int frequency;
	public Word(char c){
		this.ch = c;
		this.frequency = 1;
	}
	public void increase(){
		this.frequency++;
	}
	public void increase(int amount){
		this.frequency += amount;
	}
	public char getWord(){
		return this.ch;
	}
	public int getFrequency(){
		return this.frequency;
	}
	public boolean equals(Object obj){
		return this.ch == ((Word)obj).ch;
	}
	public String toString(){
		return this.ch + "[" + this.frequency + "]";
	}
}
