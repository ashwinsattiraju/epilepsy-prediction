package org.artificial_neural_networks.common;

import java.util.ArrayList;

import org.artificial_neural_networks.exceptions.InputErrorException;

/**
 * @author  riccardo  This class represents the abstraction of a set of data related to the same   feature.
 */
public class FeatureSet {
	/**
	 * The name of the feature this feature set refers to
	 * @uml.property  name="featureName"
	 */
	private String featureName;
	/**
	 * The ArrayList of elements making up this dataset
	 * @uml.property  name="featureElements"
	 */
	private ArrayList<FeatureElement> featureElements;
	/**
	 * The ID of the feature set
	 * @uml.property  name="featureID"
	 */
	private long featureID;
	/**
	 * The expected length of the feature element entering this layer
	 */
	private int expectedLength;
	
	/**
	 * Default constructor.
	 * @param pFeatureName The name of the feature this dataset refers to
	 * @param pFeatureID the ID of the feature 
	 */
	public FeatureSet(String pFeatureName, long pFeatureID) {
		featureName = pFeatureName;
		featureElements = new ArrayList<FeatureElement>();
		featureID = pFeatureID;
		expectedLength = -1;
	}
	
	/**
	 * This constructor takes as input the name of the feature and a list of 
	 * elements pertaining this feature to add to the dataset
	 * @param pFeatureName the name of the feature
	 * @param pFeatureID the ID of the feature
	 * @param pListFeatureSetElements the list of elements to add to the dataset
	 */
	public FeatureSet(String pFeatureName, long pFeatureID, ArrayList<FeatureElement> pListFeatureSetElements) {
		featureName = pFeatureName;
		featureElements = new ArrayList<FeatureElement>();
		for (FeatureElement featureElement : pListFeatureSetElements) {
			featureElements.add(featureElement.clone());
		}
		featureID = pFeatureID;
	}
	
	/**
	 * Add element but check consistency of added elements as to preserve
     * the consistency of the data
	 * @param featureElement the element to add
	 * @throws InputErrorException if element is not coherent, ie its dimension
	 * differs from the one of the previous elements. The first element added
	 * gives the dimension 
	 */
	public void addFeatureElement(FeatureElement featureElement) throws InputErrorException {
		// add element but check consistency of added elements as to preserve
		// the consistency of the data
		if (expectedLength == -1) {
			expectedLength = featureElement.getFeatureElementDataSize();
		}
		if (expectedLength != featureElement.getFeatureElementDataSize()) {
			throw new InputErrorException();
		}
		featureElements.add(featureElement);
	}
	
	/**
	 * Remove a FeatureElement from the feature set
	 * @param featureElement the element to remove
	 * @return true if the item was removed, false otherwise
	 */
	public boolean removeFeatureElement(FeatureElement featureElement) {
		return featureElements.remove(featureElement);
	}
	
	/**
	 * @return  This method returns a hard copy of the array where the elements  are stored in, not a hard copy of the actual elements stored in it.
	 * @uml.property  name="featureElements"
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FeatureElement> getFeatureElements() {
		return (ArrayList<FeatureElement>)featureElements.clone();
	}

	/**
	 * @return  the featureName
	 * @uml.property  name="featureName"
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @param featureName  the featureName to set
	 * @uml.property  name="featureName"
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * @return  the featureID
	 * @uml.property  name="featureID"
	 */
	public long getFeatureID() {
		return featureID;
	}

	/**
	 * @param featureID  the featureID to set
	 * @uml.property  name="featureID"
	 */
	public void setFeatureID(long featureID) {
		this.featureID = featureID;
	}
	
	/**
	 * @param compareTo the FeatureSet to compare
	 * @return true iif the IDs are equal, false otherwise
	 */
	public boolean equals(FeatureSet compareTo) {
		return compareTo.featureID == featureID;
	}
	
	public int getNumberOfElements() {
		return featureElements.size();
	}
}
