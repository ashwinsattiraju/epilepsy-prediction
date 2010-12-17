/**
 *
 */
package org.artificial_neural_networks.networks.demo;

import java.util.ArrayList;

import org.artificial_neural_networks.common.*;
import org.artificial_neural_networks.exceptions.*;
import org.artificial_neural_networks.networks.lamstar.*;

/**
 * @author riccardo
 *
 */
public class LamstarDemo {
	/**
	 * @param args
	 * @throws Exception
	 * @throws FeatureSetIDNotFoundException
	 */
	public static void main(String[] args) throws FeatureSetIDNotFoundException, Exception {
		FeatureSet fs0 = new FeatureSet("first feature" , 0L);
		FeatureSet fs1 = new FeatureSet("second feature", 1L);
		FeatureSet fs2 = new FeatureSet("third feature" , 2L);
		ArrayList<Classification> classValues = new ArrayList<Classification>();
			// load first word
			FeatureElement fe00 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe00);
			FeatureElement fe10 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe10);
			FeatureElement fe20 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe20);
			Classification c0	= new  Classification("First word", 0L);
				classValues.add(c0);
			// load second word
			FeatureElement fe01 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe01);
			FeatureElement fe11 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe11);
			FeatureElement fe21 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe21);
			Classification c1	= new  Classification("Second word", 0L);
				classValues.add(c1);
			// load third word
			FeatureElement fe02 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe02);
			FeatureElement fe12 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe12);
			FeatureElement fe22 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe22);
			Classification c2	= new  Classification("Third word", 0L);
				classValues.add(c2);
			// load fourth word
			FeatureElement fe03 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe03);
			FeatureElement fe13 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe13);
			FeatureElement fe23 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe23);
			Classification c3	= new  Classification("Fourth word", 1L);
				classValues.add(c3);
			// load fifth word
			FeatureElement fe04 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe04);
			FeatureElement fe14 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe14);
			FeatureElement fe24 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe24);
			Classification c4	= new  Classification("Fifth word", 1L);
				classValues.add(c4);
			// load sixth word
			FeatureElement fe05 = new FeatureElement(getRandomDoubleVector(10));
				fs0.addFeatureElement(fe05);
			FeatureElement fe15 = new FeatureElement(getRandomDoubleVector(20));
				fs1.addFeatureElement(fe15);
			FeatureElement fe25 = new FeatureElement(getRandomDoubleVector(20));
				fs2.addFeatureElement(fe25);
			Classification c5	= new  Classification("Sixth word", 1L);
				classValues.add(c5);
		// Now create the ArrayList<FeatureSet> to be passed to Dataset
		ArrayList<FeatureSet> featureSets = new ArrayList<FeatureSet>();
			featureSets.add(fs0);
			featureSets.add(fs1);
			featureSets.add(fs2);
		Dataset pDataset = new Dataset(0L, featureSets, classValues);
		// Now create the Lamstar network itself
		Lamstar lamstar = new Lamstar(pDataset, pDataset, 0.005, 0.05, -0.05);

		// Measure performance
		// System.out.println("Performance: " + Double.toString(lamstar.performance(System.out)));
		// Train it
		@SuppressWarnings("unused")
		double[] results = lamstar.train(System.out, 200, 95, false);
		System.out.println(lamstar.performance());
	}

	private static double[] getRandomDoubleVector(int length) {
		double[] returnValue = new double[length];
		for (int i=0; i<length; i++) {
			returnValue[i] = Math.random();
		}
		return returnValue;
	}
}
