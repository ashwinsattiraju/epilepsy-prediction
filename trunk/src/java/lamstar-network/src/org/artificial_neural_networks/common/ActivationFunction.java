package org.artificial_neural_networks.common;

/**
 * @author Riccardo Cattaneo
 * @version 0.1
 * This class represents the abstraction of an activation function. It is 
 * used to instruct a neuron on the kind of activation function that it 
 * should use.
*/
public abstract class ActivationFunction {
	/**
	 * This function returns the value of the function in valueIn.
	 * @param valueIn is the point at which is computed the value of the 
	 * function
	 * @return the computed value
	 */
	public abstract double valueOf(double valueIn);
	
	/**
	 * This function returns a copy of this object.
	 * @return the copy of this object
	 */
	public abstract ActivationFunction clone();
	
	/**
	 * This function returns true if o1 is of the same type of this class	 
	 * @param o1 the object to compare
	 * @return true iif they're of the same type
	 */
	@Override
	public boolean equals(Object o1) {
		return o1.getClass() == this.getClass();
	}
}
