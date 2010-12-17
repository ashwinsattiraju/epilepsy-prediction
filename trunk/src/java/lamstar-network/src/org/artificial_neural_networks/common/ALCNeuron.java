/**
 *
 */
package org.artificial_neural_networks.common;

import java.util.HashMap;

import org.artificial_neural_networks.exceptions.*;

/**
 * @author  riccardo
 */
public class ALCNeuron extends Neuron {
	/**
	 * @serial This is useful for serializing the network and will be used
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = -529366595189893754L;
	/**
	 * This variable maps
	 * 1) an input layer IDLayer
	 * 2) to a neuron IDNeuron inside the input layer IDLayer
	 * 3) to a link weight value
	 * The resulting triplet is <Long, Long, Double>
	 */
	private HashMap<Long, HashMap<Long, Double>> inputLayerMap;
	/**
	 * This variable maps
	 * 1) a correlation layer IDLayer
	 * 2) to a neuron IDNeuron inside the correlation layer IDLayer
	 * 3) to a link weight value
	 * The resulting triplet is <Long, Long, Double>
	 */
	private HashMap<Long, HashMap<Long, Double>> correlationLayerMap;
	/**
	 * This is the representation of the class that is recognized by the neuron.
	 * @uml.property  name="classValue"
	 * @uml.associationEnd  
	 */
	private Classification classValue;

	/**
	 * The default constructor
	 * @param pNeuronID the UID of this neuron inside the op layer
	 * @param pClassValue the class that this neuron must recognize. Unique in
	 * the output layer as well
	 * @param pInputLayerMap this value MUST BE A SHALLOW COPY of the map that
	 * is used in the output layer to store the relationship between this neuron
	 * and the link weights of the input layers
	 * @param pCorrelationLayerMap this value MUST BE A SHALLOW COPY of the map
	 * that is used in the output layer to store the relationship between this
	 * neuron and the link weights of the correlation layers
	 */
	public ALCNeuron(Long pNeuronID, Classification pClassValue, HashMap<Long, HashMap<Long, Double>> pInputLayerMap, HashMap<Long, HashMap<Long, Double>> pCorrelationLayerMap) {
		// pass the reference to the parameters but for the Classification value
		// so that when the caller modifies the values the changes are
		// reflected inside the class
		neuronID = pNeuronID;
		inputLayerMap = pInputLayerMap;
		correlationLayerMap = pCorrelationLayerMap;
		classValue = pClassValue.clone();
	}

	/**
	 * The default constructor for input layers only cases
	 * @param pNeuronID the UID of this neuron inside the op layer
	 * @param pClassValue the class that this neuron must recognize. Unique in
	 * the output layer as well
	 * @param pInputLayerMap this value MUST BE A SHALLOW COPY of the map that
	 * is used in the output layer to store the relationship between this neuron
	 * and the link weights of the input layers
	 */
	public ALCNeuron(Long pNeuronID, Classification pClassValue, HashMap<Long, HashMap<Long, Double>> pInputLayerMap) {
		// pass the reference to the parameters but for the Classification value
		// so that when the caller modifies the values the changes are
		// reflected inside the class
		neuronID = pNeuronID;
		inputLayerMap = pInputLayerMap;
		correlationLayerMap = null;
		classValue = pClassValue.clone();
	}

	/**
	 * This method computes the output of the neuron, given the winners of the
	 * input and correlation layers
	 * @param winnersInput A map where the key is an ID of an output layer and
	 * the value is the ID of a neuron inside it
	 * @param winnersCorrelation  A map where the key is an ID of a correlation
	 * layer and the value is the ID of a neuron inside it
	 * @return the sum of the link weights to this neuron
	 * @throws InputErrorException if the inputs are not consistent
	 */
	public double applyInput(HashMap<Long, Long> winnersInput, HashMap<Long, Long> winnersCorrelation) throws InputErrorException {
		// (partial) input sanitization
		if (!inputLayerMap.keySet().containsAll(winnersInput.keySet()) ||
			!correlationLayerMap.keySet().containsAll(winnersCorrelation.keySet())) {
			throw new InputErrorException();
		}
		// (partial) healthy inputs: (try to) compute the output
		// sum all the link weights of the winners
		double sum = 0;
		for (Long layerID : winnersInput.keySet()) {
			Long winningNeuron = winnersInput.get(layerID);
			sum += inputLayerMap.get(layerID).get(winningNeuron);
		}
		for (Long layerID : winnersCorrelation.keySet()) {
			Long winningNeuron = winnersCorrelation.get(layerID);
			sum += correlationLayerMap.get(layerID).get(winningNeuron);
		}
		return sum;
	}

	/**
	 * This method computes the output of the neuron, given the winners of the
	 * input layers
	 * @param winnersInput A map where the key is an ID of an output layer and
	 * the value is the ID of a neuron inside it
	 * @return the sum of the link weights to this neuron
	 * @throws InputErrorException if the inputs are not consistent
	 */
	public double applyInput(HashMap<Long, Long> winnersInput) throws InputErrorException {
		// (partial) input sanitization
		if (!inputLayerMap.keySet().containsAll(winnersInput.keySet())) {
			throw new InputErrorException();
		}
		// (partial) healthy inputs: (try to) compute the output
		// sum all the link weights of the winners
		double sum = 0;
		for (Long layerID : winnersInput.keySet()) {
			Long winningNeuron = winnersInput.get(layerID);
			sum += inputLayerMap.get(layerID).get(winningNeuron);
		}
		return sum;
	}

	/**
	 * @param neuron a Neuron to compare "this" against
	 * @return true iif ID of neuron and "this" are equal
	 * @see org.artificial_neural_networks.common.Neuron#equals(org.artificial_neural_networks.common.Neuron)
	 */
	public boolean equals(ALCNeuron neuron) {
		// if ID == ID true
		if (this.neuronID == neuron.getNeuronID())
			return true;
		return false;
	}

	/**
	 * Returns a copy of this neuron, the ID is copied too.
	 * @see org.artificial_neural_networks.common.Neuron#clone()
	 */
	@Override
	public ALCNeuron clone() {
		return new ALCNeuron(neuronID, classValue, correlationLayerMap, correlationLayerMap);
	}

}
