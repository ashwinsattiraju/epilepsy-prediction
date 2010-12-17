package org.artificial_neural_networks.common;

/**
 * This class represents the abstraction of a constant (to 1) function
 * @author Riccardo Cattaneo
 * @version 0.1
*/
public class ActivationFunctionConstant extends ActivationFunction {
	/**
	 * This is the default constructor. 
	 * @return a new constant (to 1) function object  
	 */
	public ActivationFunctionConstant() {}
	
	/**
	 * This class represents the abstraction of a constant (to 1) function
	 * @return 1
	 */
	@Override
	public double valueOf(double valueIn) {
		return 1;
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