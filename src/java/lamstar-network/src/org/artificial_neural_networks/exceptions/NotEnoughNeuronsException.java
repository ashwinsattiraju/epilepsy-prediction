/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * This class represents the exception that is thrown whenever an empty list of
 * neurons is passed to a Layer constructor 
 * @author riccardo
 * @version 0.1
 */
public class NotEnoughNeuronsException extends Exception {
	/**
	 * Serializitaion related field.
	 */
	private static final long serialVersionUID = -6166398224585134645L;
	/**
	 * This is the default donstructor.
	 */
	public NotEnoughNeuronsException() {
		super();
	}

	/**
	 * This creates an exception with a mesasge in it.
	 * @param s this is the message of the string
	 */
	public NotEnoughNeuronsException(String s) {
		super(s);
	}

	/**
	 * This creates an exception based on a throwable object.
	 * @param t the Throwable object
	 */
	public NotEnoughNeuronsException(Throwable t) {
		super(t);
	}

	/**
	 * This creates an exception based on a throwable object and a message.
	 * @param s the attached message
	 * @param t the Throwable object from which the details and the casuses of 
	 * the excpetion are taken 
	 */
	public NotEnoughNeuronsException(String s, Throwable t) {
		super(s, t);
	}

}
