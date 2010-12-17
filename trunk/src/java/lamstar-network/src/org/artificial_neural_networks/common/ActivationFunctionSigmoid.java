package org.artificial_neural_networks.common;

/**
 * This class represents the abstraction of a sigmoid activation function
 * @author Riccardo Cattaneo
 * @version 0.1
*/
public class ActivationFunctionSigmoid extends ActivationFunction {
	/**
	 * This is the default constructor. 
	 * @return a new sigmoid function object  
	 */
	public ActivationFunctionSigmoid() {}
	
	/**
	 * This function returns the value of the sigmoid function in valueIn. The
	 * function implemented is the following: (1/(1+Math.pow(Math.E, -valueIn)))
	 * @param valueIn is the point at which is computed the value of the 
	 * function
	 * @return the computed value
	 */
	@Override
	public double valueOf(double valueIn) {
		return (1/(1+Math.pow(Math.E, -valueIn)));
	}
	
	/**
	 * This function returns a copy of this object.
	 * @return the copy of this object
	 */
	@Override
	public ActivationFunctionSigmoid clone() {
		return new ActivationFunctionSigmoid();
	}
}
