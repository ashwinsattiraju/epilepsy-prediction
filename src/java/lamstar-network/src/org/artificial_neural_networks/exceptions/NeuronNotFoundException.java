/**
 * 
 */
package org.artificial_neural_networks.exceptions;

/**
 * This class represents the exception that is thrown when a neuron is not found
 * inside a Collection when it should have
 * @author riccardo
 */
public class NeuronNotFoundException extends Exception {	
	/**
	 * Serializitaion related field.
	 */
	private static final long serialVersionUID = -2597185543819884406L;


	/**
	 * This is the default donstructor.
	 */
	public NeuronNotFoundException() {
		super();
	}

	/**
	 * This creates an exception with a mesasge in it.
	 * @param s this is the message of the string
	 */
	public NeuronNotFoundException(String message) {
		super(message);
	}

	/**
	 * This creates an exception based on a throwable object.
	 * @param t the Throwable object
	 */
	public NeuronNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * This creates an exception based on a throwable object and a message.
	 * @param s the attached message
	 * @param t the Throwable object from which the details and the casuses of 
	 * the excpetion are taken 
	 */
	public NeuronNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
