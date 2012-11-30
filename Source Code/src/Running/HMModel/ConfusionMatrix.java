package Running.HMModel;

import java.util.ArrayList;


public abstract class ConfusionMatrix<ST, OT> {
	protected ArrayList<ST> statuses;
	protected ArrayList<OT> obs;
	
	public abstract double transformProbability(ST i, OT j);
}



