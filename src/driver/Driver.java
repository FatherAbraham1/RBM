package driver;

import rbm.RBM;
import visualizer.Visualizer;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RBM rbm = new RBM(5, 8);
		rbm.connectFully();
		System.out.println(rbm);
		
//		Visualizer visualizer = new Visualizer(rbm);
//		visualizer.showStructure();

	}

}
