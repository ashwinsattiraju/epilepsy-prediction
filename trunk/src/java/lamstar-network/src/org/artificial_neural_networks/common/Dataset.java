package org.artificial_neural_networks.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import org.artificial_neural_networks.exceptions.*;

/**
 * @author  riccardo  This is the "highest" abstraction employed to represent data. This is a  container for many different types of feature sets.
 */
public class Dataset implements Serializable{
	/**
	 * @serial This is useful for serializing the network and will be used
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = -889120594489875426L;
	/**
	 * The list of feature sets added to this dataset
	 * @uml.property  name="featureSets"
	 */
	private ArrayList<FeatureSet> featureSets;
	/**
	 * The ID of the dataset
	 * @uml.property  name="datasetID"
	 */
	private Long datasetID;
	/**
	 * After the first FeatureSet has been loaded, this must be set so that at
	 * any given time where two or more datasets are present in this class, it
	 * must be that for each pair of them, the number of elements that contain
	 * is always the same
	 */
	private int expectedLengthOfFeatureSets;
	/**
	 * This ArrayList contains the class of any given word loaded via featureSets
	 * @uml.property  name="wordsClass"
	 */
	private ArrayList<Classification> wordsClass;

	/**
	 * This constructor takes a collection of FeatureSets in input and builds a
	 * dataset out of it
	 * @param pFeatureSets the collection of FeatureSet used to init the net
	 */
	public Dataset(Long pDatasetID, ArrayList<FeatureSet> pFeatureSets, ArrayList<Classification> pWordsClass) {
		featureSets = new ArrayList<FeatureSet>(pFeatureSets);
		datasetID = pDatasetID;
		expectedLengthOfFeatureSets = -1;
		wordsClass = pWordsClass;
	}

	/**
	 * This method adds a set of features to the dataset
	 * @param pFeatureSet the set of features
	 * @throws InputErrorException if the first argument contains more feature
	 * elements than any of the previous ones
	 */
	public void addFeatureSet(FeatureSet pFeatureSet) throws InputErrorException {
		if (expectedLengthOfFeatureSets == -1) {
			expectedLengthOfFeatureSets = pFeatureSet.getFeatureElements().size();
		}
		if (pFeatureSet.getFeatureElements().size() != expectedLengthOfFeatureSets) {
			throw new InputErrorException();
		}
		featureSets.add(pFeatureSet);
	}

	/**
	 * @return an ArrayList containing the IDs of the elements inside "this"
	 */
	public ArrayList<Long> getFeatureSetsIDs() {
		ArrayList<Long> featureSetsID = new ArrayList<Long>(featureSets.size());
		for (FeatureSet featureSet : featureSets) {
			featureSetsID.add(featureSet.getFeatureID());
		}
		return featureSetsID;
	}

	/**
	 * This method removes a feature set from the dataset
	 * @param featureSetID the ID of the feature to remove
	 * @throws FeatureIDNotFoundException thrown if the ID is not contained in
	 * the class
	 */
	public void removeFeatureSet(Long featureSetID) throws FeatureIDNotFoundException {
		// obtain list of IDs
		ArrayList<Long> featureSetsID = getFeatureSetsIDs();
		// if the ID is not present, throw exception
		if(!featureSetsID.contains(featureSetID)) {
			throw new FeatureIDNotFoundException();
		}
		// else, remove it
		else {
			int index = -1;
			int counter = 0;
			for (FeatureSet iterFeature : featureSets) {
				if (iterFeature.getFeatureID() == featureSetID) {
					index = counter;
				}
				counter++;
			}
			// TODO check this: if the foreach is unordered (somehow) than this
			// will mess things up - probably not because of Iterator
			featureSets.remove(index);
		}
	}

	/**
	 * This method removes a feature set from the dataset
	 * @param toRemove the FeatureSet of the dataset to remove
	 * @throws FeatureIDNotFoundException thrown if the ID is not contained in
	 * the class
	 */
	public void removeFeatureSet(FeatureSet toRemove) throws FeatureIDNotFoundException {
		removeFeatureSet(toRemove.getFeatureID());
	}

	/**
	 * @return the number of classes in this dataset
	 */
	public int getNumOfClasses() {
		// TreeSet so adding more than once the same class doesn't result in \
		// multiple copies of it in the collection
		TreeSet<Classification> numClasses = new TreeSet<Classification>();
		// And addAll Classes to the TreeSet, so as to maintain the unicity
		numClasses.addAll(wordsClass);
		// return numOf classes
		return numClasses.size();
	}

	/**
	 * @return the classes in this dataset
	 */
	public TreeSet<Classification> getClasses() {
		// TreeSet so adding more than once the same class doesn't result in \
		// multiple copies of it in the collection
		TreeSet<Classification> numClasses = new TreeSet<Classification>();
		// And addAll Classes to the TreeSet, so as to maintain the unicity
		numClasses.addAll(wordsClass);
		// return numOf classes
		return numClasses;
	}

	/**
	 * @return the number of features in this dataset
	 */
	public int getNumOfFeatures() {
		return featureSets.size();
	}

	/**
	 * This method returns a feature set element, given its ID
	 * @param featureSetID the ID of the feature set sought
	 * @return the feature set sought
	 * @throws FeatureSetIDNotFoundException thrown if the ID is not in
	 * getFeatureSetsIDs()
	 * @throws Exception if the implementation is not correct, this is thrown
	 * as a result of having checked that the item is inside the available items
	 * but is not returned because of an unknown reason.
	 */
	public FeatureSet getFeatureSetElementByID(Long featureSetID) throws FeatureSetIDNotFoundException, Exception {
		// obtain list of IDs
		ArrayList<Long> featureSetsID = getFeatureSetsIDs();
		// if the ID is not found
		if (!featureSetsID.contains(featureSetID)) {
			throw new FeatureSetIDNotFoundException();
		}
		// else return the element
		else {
			for (FeatureSet iterFeature : featureSets) {
				if (iterFeature.getFeatureID() == featureSetID) {
					return iterFeature;
				}
			}
		}
		// else throw an unhandled exception
		throw new Exception("Unhandled exception in org.artificial_neural_networks.common.Dataset@getFeatureSetElementByID\n");
	}

	/**
	 * @return  the datasetID
	 * @uml.property  name="datasetID"
	 */
	public Long getDatasetID() {
		return datasetID;
	}

	/**
	 * @param datasetID  the datasetID to set
	 * @uml.property  name="datasetID"
	 */
	public void setDatasetID(Long datasetID) {
		this.datasetID = datasetID;
	}

	/**
	 * @return  a shallow copy of the ArrayList containing the feature sets
	 * @uml.property  name="featureSets"
	 */
	public ArrayList<FeatureSet> getFeatureSets() {
		return featureSets;
	}

	/**
	 * @return  the wordsClass
	 * @uml.property  name="wordsClass"
	 */
	public ArrayList<Classification> getWordsClass() {
		return wordsClass;
	}

	/**
	 * @param wordsClass  the wordsClass to set
	 * @uml.property  name="wordsClass"
	 */
	public void setWordsClass(ArrayList<Classification> wordsClass) {
		this.wordsClass = wordsClass;
	}

	public int getWordsNumber() {
		return featureSets.get(0).getNumberOfElements();
	}
}