/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * @author riccardo
 *
 */
public class VectorDimensionIsZeroException extends InputErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2927072989717422800L;

	/**
	 * 
	 */
	public VectorDimensionIsZeroException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VectorDimensionIsZeroException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VectorDimensionIsZeroException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VectorDimensionIsZeroException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
