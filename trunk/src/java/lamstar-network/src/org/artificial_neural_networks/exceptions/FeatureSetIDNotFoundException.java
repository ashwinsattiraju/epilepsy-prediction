/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * @author riccardo
 *
 */
public class FeatureSetIDNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3363452242025867087L;

	/**
	 * 
	 */
	public FeatureSetIDNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public FeatureSetIDNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FeatureSetIDNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FeatureSetIDNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
