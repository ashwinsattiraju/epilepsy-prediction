/**
 *
 */
package org.artificial_neural_networks.networks.lamstar;

import java.util.ArrayList;

import org.artificial_neural_networks.common.*;
import org.artificial_neural_networks.exceptions.InputErrorException;
import org.artificial_neural_networks.exceptions.NeuronNotFoundException;
import org.artificial_neural_networks.exceptions.NotEnoughNeuronsException;

/**
 * @author  riccardo
 */
public class LamstarFactoryForMatlab {

	/**
	 * @uml.property  name="lamstar"
	 * @uml.associationEnd  
	 */
	private Lamstar lamstar;
	/**
	 * @uml.property  name="trainingSet"
	 * @uml.associationEnd  
	 */
	private Dataset trainingSet;
	/**
	 * @uml.property  name="testingSet"
	 * @uml.associationEnd  
	 */
	private Dataset testingSet;
	private ArrayList<FeatureSet> featureSetTrainingList;
	private ArrayList<FeatureSet> featureSetTestingList;
	private ArrayList<Classification> featureSetTrainingListClasses;
	private ArrayList<Classification> featureSetTestingListClasses;

	public LamstarFactoryForMatlab() {
		featureSetTrainingList = new ArrayList<FeatureSet>();
		featureSetTestingList = new ArrayList<FeatureSet>();
		featureSetTrainingListClasses = new ArrayList<Classification>();
		featureSetTestingListClasses = new ArrayList<Classification>();
	}

	public void initNewLamstarForMatlab(double maxAllowedDistance, double reward, double punish) {
		try {
			trainingSet = new Dataset(0L, featureSetTrainingList, featureSetTrainingListClasses);
			testingSet  = new Dataset(1L, featureSetTestingList, featureSetTestingListClasses);
			lamstar = new Lamstar(trainingSet, testingSet, maxAllowedDistance, reward, punish);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void addElementToFeatureSetIDForTraining(double featureSetID, double[] data, int classValue) throws InputErrorException {
		System.out.println("Data seen for training:");
		for (int i = 0; i<data.length; i++) {
			System.out.print(data[i]); System.out.print(' ');
		}
		for (FeatureSet featureSet : featureSetTrainingList) {
			if (featureSet.getFeatureID() == featureSetID) {
				// create elem, add to it, return
				FeatureElement fe = new FeatureElement(normalize(data));
				System.out.println("Data seen for training after normalization:");
				for (int i = 0; i<fe.getDataVector().length; i++) {
					System.out.print(fe.getDataVector()[i]); System.out.print(' ');
				}
				featureSet.addFeatureElement(fe);
				featureSetTrainingListClasses.add(new Classification("", classValue));
				return;
			}
		}
		// if this point is reached, create a new layer with the given ID and
		// add item to it
		featureSetTrainingList.add(new FeatureSet("", Math.round(featureSetID)));
		// recall me!
		addElementToFeatureSetIDForTraining(featureSetID, data, classValue);
	}

	public void addElementToFeatureSetIDForTesting(double featureSetID, double[] data, int classValue) throws InputErrorException {
		System.out.println("Data seen for testing:");
		for (int i = 0; i<data.length; i++) {
			System.out.print(data[i]); System.out.print(' ');
		}
		for (FeatureSet featureSet : featureSetTestingList) {
			if (featureSet.getFeatureID() == featureSetID) {
				// create elem, add to it, return
				FeatureElement fe = new FeatureElement(normalize(data));
				System.out.println("Data seen for testing after noramlization:");
				for (int i = 0; i<fe.getDataVector().length; i++) {
					System.out.print(fe.getDataVector()[i]); System.out.print(' ');
				}
				featureSet.addFeatureElement(fe);
				featureSetTestingListClasses.add(new Classification("", classValue));
				return;
			}
		}
		// if this point is reached, create a new layer with the given ID and
		// add item to it
		featureSetTestingList.add(new FeatureSet("", Math.round(featureSetID)));
		// recall me!
		addElementToFeatureSetIDForTraining(featureSetID, data, classValue);
	}

	public double[]trainWithOutput(int numberOfIterations, double performanceGoal, boolean punishToo) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		double[] performanceEvolution = lamstar.train(System.out, numberOfIterations, performanceGoal, punishToo);
		return performanceEvolution;
	}

	private double[] normalize(double[] data) {
		double normFact = 0;
		for (int i=1; i< data.length; i++) {
			normFact += Math.pow(data[i], 2);
		}
		normFact = Math.sqrt(normFact);
		for (int i=1; i< data.length; i++) {
			data[i] = data[i]/normFact;
		}
		return data;
	}

	public Long getIndexOfMostImportantInputLayers() {
		return lamstar.getIndexOfMostImportantInputLayers();
	}

	public void printLamstarConfiguration () {
		lamstar.printConfiguration();
	}

}
