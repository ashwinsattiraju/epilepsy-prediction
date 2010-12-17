/**
 * 
 */
package org.artificial_neural_networks.common;

/**
 * @author  riccardo  This class represents the most basic form of data on which a network should   train on. It is an element representing one feature of the data.
 */
public class FeatureElement {
	/**
	 * The feature itself
	 * @uml.property  name="dataVector"
	 */
	private double[] dataVector;
	
	/**
	 * Default constructor.
	 * @param pDataVector the actual data.
	 * @param pClassName a human-readable alias used to distinguish classes
	 * @param pClassID a machine-readable alias used to distinguish classes
	 */
	public FeatureElement(double[] pDataVector) {
		dataVector = pDataVector.clone();
	}

	/**
	 * @return  a hard copy of the dataVector
	 * @uml.property  name="dataVector"
	 */
	public double[] getDataVector() {
		return dataVector;
	}

	/**
	 * @param dataVector  the dataVector to set
	 * @uml.property  name="dataVector"
	 */
	public void setDataVector(double[] dataVector) {
		this.dataVector = dataVector;
	}
	
	/**
	 * Comparison between objects
	 * @param compareTo the FeatureElement to compare "this" against
	 * @return true iif the classification values are the same and the data is 
	 * the same 
	 */
	public boolean equals(FeatureElement compareTo) {
		if (dataVector.equals(compareTo.getDataVector())) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return a hard copy of "this"
	 */
	public FeatureElement clone() {
		return new FeatureElement(dataVector.clone());
	}
	
	public int getFeatureElementDataSize() {
		return dataVector.length;
	}
}
