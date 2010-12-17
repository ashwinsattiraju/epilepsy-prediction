package org.artificial_neural_networks.common;

import java.io.Serializable;
import java.util.ArrayList;

import org.artificial_neural_networks.exceptions.*;

/**
 * @author  Riccardo Cattaneo
 * @version  0.1  This class represents the abstraction of a layer. It is a container of  neurons.
 */
public abstract class Layer implements Serializable{
	/**
	 * @serial This is useful for serializing the network and will be used 
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = 3895934280726206407L;
	/**
	 * The neurons of the layer
	 * @uml.property  name="neurons"
	 */
	protected ArrayList<Neuron> neurons;
	/** 
	 * The ID of the last added neuron. Incremental.
	 */
	protected long currentNeuronID;
	
	/**
	 * This is the default constructor.  
	 */
	public Layer() {
		neurons = new ArrayList<Neuron>();
		currentNeuronID = 0;
	}
	
	/**
	 * This constructor takes an ArrayList<Neuron> of Neuron and creates a layer
	 * out of it.  
	 * @param pNeurons an ArrayList<Neuron> of Neuron. 
	 * @throws NotEnoughNeuronsException if no neuron was found in pNeurons
	 */
	public Layer(ArrayList<Neuron> pNeurons) throws NotEnoughNeuronsException {
		if (pNeurons.isEmpty()) {
			throw new NotEnoughNeuronsException("There isn't any neuron here.\n");
		}
		else {
			for (Neuron neuron : pNeurons) {
				Neuron n = neuron.clone();
				n.setNeuronID(currentNeuronID);
				neurons.add(n.clone());
			}
		}	
	}
	
	/**
	 * This function returns the current number of neurons inside this layer
	 * @return the number of neurons in the layer
	 */
	public int getNumberOfNeurons() {
		return neurons.size();
	}
	
	/**
	 * This function returns a copy of the neurons inside the layer. If the  initial list is empty, an exception is thrown
	 * @return  a copy of the neurons inside the layer
	 * @throws NotEnoughNeuronsException  if no neuron was found
	 * @uml.property  name="neurons"
	 */
	public ArrayList<Neuron> getNeurons() throws NotEnoughNeuronsException {
		if (neurons.isEmpty()) {
			throw new NotEnoughNeuronsException("There isn't any neuron here.\n");
		}
		ArrayList<Neuron> outNeurons = new ArrayList<Neuron>(neurons.size());
		for (Neuron neuron : neurons) {
			outNeurons.add(neuron.clone());
		}
		return outNeurons;
	}
}
