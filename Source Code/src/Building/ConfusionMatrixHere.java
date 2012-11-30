package Building;

import Running.HMModel.ConfusionMatrix;

public class ConfusionMatrixHere extends ConfusionMatrix<String, String>{
	private static ConfusionMatrixHere INSTANCE = null;
	public static ConfusionMatrixHere getInstance(){
		if(ConfusionMatrixHere.INSTANCE == null){
			INSTANCE = new ConfusionMatrixHere();
		}
		return INSTANCE;
	}
	private ConfusionMatrixHere(){}
	@Override
	public double transformProbability(String i, String j) {
		// TODO Auto-generated method stub
		return 1.0;
	}
	
}
