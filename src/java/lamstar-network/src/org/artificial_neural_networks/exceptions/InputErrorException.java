/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * @author riccardo
 *
 */
public class InputErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5965189822716825855L;

	/**
	 * 
	 */
	public InputErrorException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InputErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InputErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InputErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
