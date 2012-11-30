package Building;


import java.util.HashMap;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;

import Running.HMModel.TransitionMatrix;

public class TransitionMatrixFromFile extends TransitionMatrix<String> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TransitionMatrixFromFile INSTANCE = null;
	
	public static TransitionMatrixFromFile getInstance(boolean isBuilding){
		if(TransitionMatrixFromFile.INSTANCE == null){
			if(isBuilding)
				TransitionMatrixFromFile.INSTANCE = new TransitionMatrixFromFile();
			else if(!Running.Process.isFromJar()){
				INSTANCE = TransitionMatrixFromFile.restoreMatrix();
			}
			else{
				INSTANCE = TransitionMatrixFromFile.restoreMatrixFromJar();
			}
		}
		return TransitionMatrixFromFile.INSTANCE;
	}
	
	private TransitionMatrixFromFile() {
		// TODO Auto-generated constructor stub
		charmap = new HashMap<Character, Integer>();
		charmapPinYin = new HashMap<Character, Integer>();
		maxindex = 0;
	}
	private int maxindex;
	// 由于需要每个单字的ID号，所以需要一个哈希表来存放字典中每个字的ID
	private HashMap<Character, Integer> charmap;
	// 由于需要每个单字的ID号，所以需要一个哈希表来存放字典中每个字的ID
	private HashMap<Character, Integer> charmapPinYin;
	private int[][] TMatrix;
	private int[] sumOfRow;

	
	// 先扫描拼音表，建立一张拼音表对应的哈希表 (0)
	public void ReadPinYinMap(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
    
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
            	if (((char) tempchar) == '\'') {
            		// 开始读取各个汉字
            		while((tempchar = reader.read()) != '\'')
            		{
            			charmapPinYin.put((char) tempchar, 0);
            		}
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	// 第一次扫描语料库 (1)
	public void ReadCharIndex(Character c)
	{
		// 对于当前汉字库中没有，但是在拼音汉字对照表中有的字才加入汉字库
		if (!charmap.containsKey(c) && charmapPinYin.containsKey(c))
		{
			charmap.put(c, maxindex++);
		}
	}
	
	// 在第一次读入数据完毕后必须根据数据总数来初始化矩阵 (2)
	public void InitTMatrix()
	{
		// 注意一定要在第一次扫描语料库完成后进行这一步操作
		TMatrix = new int[maxindex][maxindex];
		this.sumOfRow = new int[this.maxindex];
	}
	
	void sumUpRow(){
		for(int row = 0; row < this.maxindex; row++){
			for(int col = 0; col < this.maxindex; col++){
				this.sumOfRow[row] += this.TMatrix[row][col];
			}
		}
		/*
		for(int row = 0; row < this.maxindex; row++){
			for(int col = 0; col < this.maxindex; col++){
				this.TMatrix[row][col] /= this.sumOfRow[row];
			}
		}*/
	}
	
	public int getAllCharNum(){
		return this.maxindex;
	}
	
	// 第二次扫描语料库 (3)
	public void ReadDoubleChar(Character c0, Character c1)
	{
		if (charmap.containsKey(c0) && charmap.containsKey(c1))
			TMatrix[charmap.get(c0)][charmap.get(c1)] += 1;
	}

	// 扫描词语表 (4)
	public void ReadDoubleCharWord(Character c0, Character c1)
	{
		if (charmap.containsKey(c0) && charmap.containsKey(c1))
			TMatrix[charmap.get(c0)][charmap.get(c1)] += 20;
	}
	
	// 假设矩阵为A，返回 A[i][j]/sum(A[i])(矩阵第i行第j列的值除以第i行的值的和）, i为c0对应的索引，j为c1对应索引
	@Override
	public double transitionProbability(String i, String j) {
		Integer row = charmap.get(i.charAt(0)), col = charmap.get(j.charAt(0));
		if(row == null || col == null){
			int sum = 0;
			for(int t = 0; t < this.maxindex; t++){
				sum += this.sumOfRow[t];
			}
			return 1.0/(0.0 + sum);
		}
		return (0.0 + TMatrix[row][col])/(0.0 + this.sumOfRow[row]);
	}
	
	private static String MAT_PATH = null; 
	void storeMatrix(){
		try{
			Properties prop = new Properties();
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
			MAT_PATH = prop.getProperty("mat_path");
			FileOutputStream fos = new FileOutputStream(new File(MAT_PATH));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(TransitionMatrixFromFile.INSTANCE);
			oos.flush();
			oos.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private static TransitionMatrixFromFile restoreMatrix(){
		try{
			Properties prop = new Properties();
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/conf.properties")))));
			MAT_PATH = prop.getProperty("mat_path");
			FileInputStream fis = new FileInputStream(new File(MAT_PATH));
			ObjectInputStream ois = new ObjectInputStream(fis);
			TransitionMatrixFromFile mat = (TransitionMatrixFromFile)ois.readObject();
			ois.close();
			fis.close();
			return mat;
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static TransitionMatrixFromFile restoreMatrixFromJar(){
		try{
			 MAT_PATH = "trans_mat.obj";
			 InputStream is= new TransitionMatrixFromFile().getClass().getResourceAsStream(MAT_PATH); 
			 ObjectInputStream ois = new ObjectInputStream(is);  
			 TransitionMatrixFromFile mat = (TransitionMatrixFromFile)ois.readObject();
			 ois.close();
			 is.close();
			 
			 //debug
			 System.out.println(mat.maxindex);
			 
			 return mat;
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args){
		TransitionMatrixFromFile tm = restoreMatrixFromJar();
	}
}

