/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * This class represents the exception that has to be raised when an operation 
 * on two matrices or vectors is not viable due to dimension mismatch
 * @author riccardo
 */
public class VectorDimensionMismatchException extends InputErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -461807121369077198L;

	/**
	 * 
	 */
	public VectorDimensionMismatchException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VectorDimensionMismatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VectorDimensionMismatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VectorDimensionMismatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
