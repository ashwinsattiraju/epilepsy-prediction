/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * @author riccardo
 *
 */
public class VectorNonNormalizedException extends InputErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676581664462623666L;

	/**
	 * 
	 */
	public VectorNonNormalizedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VectorNonNormalizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VectorNonNormalizedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VectorNonNormalizedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
