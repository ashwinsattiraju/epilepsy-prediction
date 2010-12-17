package org.artificial_neural_networks.common;

import org.artificial_neural_networks.exceptions.*;

/**
 * This class represents the abstraction of the general weight vector of a neuron.
 * @author  riccardo
 * @version  0.1
 */
public class NeuralVector {
	/**
	 * @uml.property  name="rowVector"
	 */
	private double[] rowVector;

	/**
	 * Default constructor
	 * @param weights is the array of weights of the neuron
	 */
	public NeuralVector(double[] weights) {
		rowVector = weights;
	}

	/**
	 * @return  the current weights vector.
	 * @uml.property  name="rowVector"
	 */
	public double[] getRowVector() {
		return rowVector;
	}

	/**
	 * Set the current weight vector of the neuron
	 * @param rowVector  the new weight vector
	 * @uml.property  name="rowVector"
	 */
	public void setRowVector(double[] rowVector) {
		this.rowVector = rowVector;
	}

	/**
	 * This function returns a copy of the instance
	 * @return a copy of the instance on which is called
	 */
	public NeuralVector clone() {
		return new NeuralVector(rowVector);
	}

	/**
	 * This function returns a copy of this
	 * @return a copy of the instance on which is called
	 */
	public boolean equals(NeuralVector o) {
		boolean equal = true;
		double[] v = o.getRowVector();
		for(int i=0; i<v.length; i++) {
			if (rowVector[i]!=v[i])
				equal = false;
		}
		return equal;
	}

	/**
	 * This method returns the dot product between neuron's weigths and term's.
	 * @param term the NeuralVector containing the data to do the dot product
	 * @return the dot product between term and the weights of this neuron
	 * @throws VectorDimensionMismatchException if the dimensions of the two
	 * vectors mismatch
	 * @throws VectorDimensionIsZeroException if one of the arguments is
	 * 0-length
	 */
	public double dotProductWith(NeuralVector term) throws VectorDimensionMismatchException, VectorDimensionIsZeroException {
		// check if dimensions match
		if (term.getRowVector().length != this.rowVector.length)
			throw new VectorDimensionMismatchException();
		// check if neither is zero-length
		if (this.rowVector.length==0) {
			throw new VectorDimensionIsZeroException();
		}
		// else
		double sum = 0;
		double[] v = term.getRowVector();
		for (int i = 0; i<v.length; i++) {
			sum += (v[i]*rowVector[i]);
		}
		return sum;
	}

	/**
	 * This function checks if rowVector is normalized
	 * @return
	 */
	public boolean isNormalized() {
		double sum=0;
		for (int i=0; i< rowVector.length; i++) {
			sum += Math.pow(rowVector[i],2);
		}
		// be tolerant up to 10^-9
		if (Math.abs(1-sum)<Math.pow(10, -3))
			return true;
		return false;
	}

	/**
	 * Normalization procedure
	 */
	public void normalize() {
		// normalization factor
		double sum=0;
		for (int i=0; i< rowVector.length; i++) {
			sum += Math.pow(rowVector[i],2);
		}
		// normalize
		for (int i=0; i< rowVector.length; i++) {
			rowVector[i] /= Math.sqrt(sum);
		}
	}

	/**
	 * This method returns the "distance" between the given NeuralVector
	 * parameter and this
	 * @param term the NeuralVector of which it is requested the distance
	 * @return the distance between term and this
	 * @throws VectorDimensionMismatchException
	 * @throws VectorDimensionIsZeroException
	 */
	public double getDistanceWith(NeuralVector term) throws VectorDimensionMismatchException, VectorDimensionIsZeroException {
		return (1-this.dotProductWith(term));
	}
}
