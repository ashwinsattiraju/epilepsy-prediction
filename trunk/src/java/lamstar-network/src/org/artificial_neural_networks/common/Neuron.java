package org.artificial_neural_networks.common;

import java.io.Serializable;

/**
 * @author  Riccardo Cattaneo
 * @version  0.1  This class realizes the abstraction of a neuron
 */
public abstract class Neuron implements Serializable{
	/**
	 * @serial This is useful for serializing the network and will be used 
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = 7336299515871884642L;
	/**
	 * The ID of this neuron
	 * @uml.property  name="neuronID"
	 */
	protected long neuronID;

	/**
	 * This method returns a copy of this neuron
	 * @return a copy of the current neuron
	 */
	public abstract Neuron clone();
	
	/**
	 * @return  the neuronID
	 * @uml.property  name="neuronID"
	 */
	public long getNeuronID() {
		return neuronID;
	}

	/**
	 * @param neuronID  the neuronID to set
	 * @uml.property  name="neuronID"
	 */
	public void setNeuronID(long neuronID) {
		this.neuronID = neuronID;
	}
}
