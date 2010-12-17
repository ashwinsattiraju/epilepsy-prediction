/**
 *
 */
package org.artificial_neural_networks.networks.lamstar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.artificial_neural_networks.common.*;
import org.artificial_neural_networks.exceptions.*;

/**
 * @author  riccardo
 */
public class LamstarOutputLayer extends Layer {
	/**
	 * @serial This is useful for serializing the network and will be used
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = 4100597968124682421L;
	/**
	 * The recognized classes. This map stores <neuronID, classValue> pairs
	 */
	private HashMap<ALCNeuron, Classification> classOfNeuron;
	/**
	 * The maps associating an output neuron to the link weights related to the input
	 * layers
	 */
	private HashMap<ALCNeuron, HashMap<Long, HashMap<Long, Double>>> inputLinkWeights;
	/**
	 * The maps associating an output neuron to the link weights related to the
	 * correlation layers
	 */
	private HashMap<ALCNeuron, HashMap<Long, HashMap<Long, Double>>> correlationLinkWeights;
	/**
	 * This obscures the superclass variable and allows us to allow only
	 * ALCNeuron type to be the type of output neuron
	 */
	private ArrayList<ALCNeuron> neurons;
	/**
	 * The amount by which reward a link weight whenever it is to do so
	 * @uml.property  name="reward"
	 */
	private double reward;
	/**
	 * The amount by which punish a link weight whenever it is to do so
	 * @uml.property  name="punish"
	 */
	private double punish;

	/**
	 * The default constructor of a LAMSTAR Output Layer
	 * @param recognizedClasses A set containing all the classes that must be
	 * recognized
	 * @param inputLayerMap the keys are the IDs of the input layers and the
	 * values are the IDs of the neurons contained in the corresponding input
	 * layer
	 * @param correlationLayerMap the keys are the IDs of the correlation layers
	 * and the values are the IDs of the neurons contained in the corresponding
	 * correlation layer
	 * @param pReward the amount by which reward a neuron
	 * @param pPunish the amount by which punish a neuron
	 * @throws NotEnoughClassesException the first parameter has less than 2
	 * classes
	 * @throws NotEnoughNeuronsException the input or correlation map contains a
	 * number of neurons that is less than 1
	 */
	public LamstarOutputLayer(TreeSet<Classification> recognizedClasses, HashMap<Long, Long[]> inputLayerMap, HashMap<Long, Long[]> correlationLayerMap, double pReward, double pPunish) throws NotEnoughClassesException, NotEnoughNeuronsException {
		// input sanitization
		if (recognizedClasses.size() < 2) {
			throw new NotEnoughClassesException();
		}
		for (Long[] value : inputLayerMap.values()) {
			if (value.length < 1) {
				throw new NotEnoughNeuronsException();
			}
		}
		for (Long[] value : correlationLayerMap.values()) {
			if (value.length < 1) {
				throw new NotEnoughNeuronsException();
			}
		}
		// inputs are healthy, begin initialization
		currentNeuronID 		= 0;
		neurons 				= new ArrayList<ALCNeuron>();
		inputLinkWeights		= new HashMap<ALCNeuron, HashMap<Long,HashMap<Long,Double>>>();
		correlationLinkWeights	= new HashMap<ALCNeuron, HashMap<Long,HashMap<Long,Double>>>();
		classOfNeuron 			= new HashMap<ALCNeuron, Classification>();
		reward 					= pReward;
		punish					= pPunish;
		// for each class, create a neuron with the corresponding link weights
		for(Classification iterClass : recognizedClasses) {
			// =====================CREATE NEURON'S PARAMS======================
			// INPUT RELATED CODE
			// This map stores the data related to a single layer, at the end of
			// the iteration must be passed to a new ALCNeuron
			HashMap<Long, HashMap<Long, Double>> iterMapInput = new HashMap<Long, HashMap<Long, Double>>();
			// for each input layer of the network
			for (Long inputLayerID : inputLayerMap.keySet()) {
				// create the corresponding link weight matrix "in" the
				// corresponding ALCNeuron
				HashMap<Long, Double> iterInternalMap = new HashMap<Long, Double>();
				// for each neuron ID in the current layer
				for(Long iterNeuronID : inputLayerMap.get(inputLayerID)) {
					// assign in the map a new "small random value"
					double zero = 0;
					iterInternalMap.put(iterNeuronID, zero);
				}
				// after creating this link weight matrix, add it to iterMap
				iterMapInput.put(inputLayerID, iterInternalMap);
			}
			// =====================CREATE NEURON'S PARAMS======================
			// CORRELATION RELATED CODE
			HashMap<Long, HashMap<Long, Double>> iterCorrelationMap = new HashMap<Long, HashMap<Long, Double>>();
			// for each input layer of the network
			for (Long correlationLayerID : correlationLayerMap.keySet()) {
				// create the corresponding link weight matrix "in" the
				// corresponding ALCNeuron
				HashMap<Long, Double> iterInternalCorrelationMap = new HashMap<Long, Double>();
				// for each neuron ID in the current layer
				for(Long iterNeuronID : correlationLayerMap.get(correlationLayerID)) {
					// assign in the map a new "small random value"
					double zero = 0;
					iterInternalCorrelationMap.put(iterNeuronID, zero);
				}
				// after creating this link weight matrix, add it to iterMap
				iterCorrelationMap.put(correlationLayerID, iterInternalCorrelationMap);
			}
			// =========================CREATE NEURON===========================
			// now create the actual neuron
			// passing the reference and relying on the fact that the method
			// will save a shallow copy of iterClass and iterMapInput, we have
			// the possibility to update the values of iterNeuron directly
			// accessing iterMapInput's fields
			ALCNeuron iterNeuron = new ALCNeuron(currentNeuronID, iterClass, iterMapInput, iterCorrelationMap);
			currentNeuronID++;
			// add the neuron and the refs to the linkWeights to the internal
			// structures
			classOfNeuron.put(iterNeuron, iterClass);
			neurons.add(iterNeuron);
			inputLinkWeights.put(iterNeuron, iterMapInput);
			correlationLinkWeights.put(iterNeuron, iterCorrelationMap);
			// =================================================================
		}
	}

	/**
	 * The default constructor of a LAMSTAR Output Layer (input layers only)
	 * @param recognizedClasses A set containing all the classes that must be
	 * recognized
	 * @param inputLayerMap the keys are the IDs of the input layers and the
	 * values are the IDs of the neurons contained in the corresponding input
	 * layer
	 *
	 * @param pReward the amount by which reward a neuron
	 * @param pPunish the amount by which punish a neuron
	 * @throws NotEnoughClassesException the first parameter has less than 2
	 * classes
	 * @throws NotEnoughNeuronsException the input or correlation map contains a
	 * number of neurons that is less than 1
	 */
	@SuppressWarnings("unchecked")
	public LamstarOutputLayer(TreeSet<Classification> recognizedClasses, HashMap<Long, Long[]> inputLayerMap, double pReward, double pPunish) throws NotEnoughClassesException, NotEnoughNeuronsException {
		// input sanitization
		if (recognizedClasses.size() < 2) {
			throw new NotEnoughClassesException();
		}
		for (Long[] value : inputLayerMap.values()) {
			if (value.length < 1) {
				throw new NotEnoughNeuronsException();
			}
		}
		// inputs are healthy, begin initialization
		currentNeuronID 	= 0;
		neurons 			= new ArrayList<ALCNeuron>();
		classOfNeuron	 	= new HashMap<ALCNeuron, Classification>();
		inputLinkWeights	= new HashMap<ALCNeuron, HashMap<Long,HashMap<Long,Double>>>();
		reward 				= pReward;
		punish				= pPunish;

		// for each class, create a neuron with the corresponding link weights
		for(Classification iterClass : recognizedClasses) {
			// =====================CREATE NEURON'S PARAMS======================
			// This map stores the data related to a single layer, at the end of
			// the iteration must be passed to a new ALCNeuron
			// HashMap<Long, HashMap> <--> layerID, neuronID, linkWeight
			HashMap<Long, HashMap<Long, Double>> iterMapInput = new HashMap<Long, HashMap<Long, Double>>();
			// for each input layer of the network
			for (Long inputLayerID : inputLayerMap.keySet()) {
				// create the corresponding link weight matrix "in" the
				// corresponding ALCNeuron
				// HashMap<Long, Double> <--> neuronID, linkWeight
				HashMap<Long, Double> iterInternalMap = new HashMap<Long, Double>();
				// for each neuron ID in the current layer
				for(Long iterNeuronID : inputLayerMap.get(inputLayerID)) {
					// assign in the map 0 value
					double zero = 0;
					iterInternalMap.put(iterNeuronID, zero);
				}
				// after creating this link weight matrix, add it to iterMap
				iterMapInput.put(inputLayerID, (HashMap<Long, Double>) iterInternalMap.clone());
			}
			// =========================CREATE NEURON===========================
			// now create the actual neuron
			// passing the reference and relying on the fact that the method
			// will save a shallow copy of iterClass and iterMapInput, we have
			// the possibility to update the values of iterNeuron directly
			// accessing iterMapInput's fields
			ALCNeuron iterNeuron = new ALCNeuron(currentNeuronID, iterClass, iterMapInput);
			currentNeuronID++;
			// add the neuron and the refs to the linkWeights to the internal
			// structures
			classOfNeuron.put(iterNeuron, iterClass);
			neurons.add(iterNeuron);
			inputLinkWeights.put(iterNeuron, iterMapInput);
			// =================================================================
		}
	}

	/**
	 * This method adds a small amount to the link weight relative to
	 * outputNeuronId and neuron neuronID in input layer layerID
	 * @param outputNeuronId the ID of the neuron that has to be updated
	 * @param layerID the layer in which neuronID is found
	 * @param neuronID the ID of the neuron whose link weight with output neuron
	 * (whose ID is outputNeuronId) is to be updated
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	public void rewardInputNeuron(Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		updateInputNeuron(reward, outputNeuronId, layerID, neuronID);
	}

	/**
	 * This method removes a small amount to the link weight relative to
	 * outputNeuronId and neuron neuronID in input layer layerID
	 * @param outputNeuronId the ID of the neuron that has to be updated
	 * @param layerID the layer in which neuronID is found
	 * @param neuronID the ID of the neuron whose link weight with output neuron
	 * (whose ID is outputNeuronId) is to be updated
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	public void punishInputNeuron(Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		updateInputNeuron(punish, outputNeuronId, layerID, neuronID);
	}

	/**
	 * This method should be invoked whenever a neuron of the correlation  layer has to
	 * be updated
	 * @param value the value to add to the current link weight
	 * @param outputNeuronId the output neuron whose link weights we are interested into
	 * @param layerID the ID of the layer where the neuronID referes to
	 * @param neuronID the ID of the neuron in correlation layer LayerID whose value we
	 * want to update
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	private void updateInputNeuron(double addValue, Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		for (ALCNeuron iterNeuron : neurons) {
			// find the neuron to update ...
			if (iterNeuron.getNeuronID() == outputNeuronId) {
				// ...found.
				// now let's get a reference to the neuron whose id is neuronID
				// that is found in layer whose ID is layerID
				if (inputLinkWeights.get(iterNeuron).containsKey(layerID))
				{
					if (inputLinkWeights.get(iterNeuron).get(layerID).containsKey(neuronID))
					{
						double oldValue = inputLinkWeights.get(iterNeuron).get(layerID).get(neuronID);
						double newValue = oldValue + addValue;
						inputLinkWeights.get(iterNeuron).get(layerID).put(neuronID, newValue);
						return;
					}
				}
				// not found the neuron to update!
				throw new NeuronNotFoundException();
			}
		}
		// not found the neuron to update!
		throw new NeuronNotFoundException();
	}

	/**
	 * This method adds a small amount to the link weight relative to
	 * outputNeuronId and neuron neuronID in correlation layer layerID
	 * @param outputNeuronId the ID of the neuron that has to be updated
	 * @param layerID the correlation layer in which neuronID is found
	 * @param neuronID the ID of the neuron whose link weight with output neuron
	 * (whose ID is outputNeuronId) is to be updated
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	public void rewardCorrelationNeuron(Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		updateCorrelationNeuron(reward, outputNeuronId, layerID, neuronID);
	}

	/**
	 * This method removes a small amount to the link weight relative to
	 * outputNeuronId and neuron neuronID in correlation layer layerID
	 * @param outputNeuronId the ID of the neuron that has to be updated
	 * @param layerID the correlation layer in which neuronID is found
	 * @param neuronID the ID of the neuron whose link weight with output neuron
	 * (whose ID is outputNeuronId) is to be updated
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	public void punishCorrelationNeuron(Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		updateCorrelationNeuron(punish, outputNeuronId, layerID, neuronID);
	}

	/**
	 * This method should be invoked whenever a neuron of a correlation layer
	 * has to be updated
	 * @param value the value to add to the current link weight
	 * @param outputNeuronId the output neuron whose link weights we are interested into
	 * @param layerID the ID of the correlation layer where the neuronID referes to
	 * @param neuronID the ID of the neuron in correlation layer LayerID whose value we
	 * want to update
	 * @throws NeuronNotFoundException if the ID of the neuron or the layer
	 * is non existent
	 */
	private void updateCorrelationNeuron(double addValue, Long outputNeuronId, Long layerID, Long neuronID) throws NeuronNotFoundException {
		for (ALCNeuron iterNeuron : neurons) {
			// find the neuron to update ...
			if (iterNeuron.getNeuronID() == outputNeuronId) {
				// ...found.
				// now let's get a reference to the neuron whose id is neuronID
				// that is found in layer whose ID is layerID
				if (correlationLinkWeights.get(iterNeuron).containsKey(layerID))
				{
					if (correlationLinkWeights.get(iterNeuron).get(layerID).containsKey(neuronID))
					{
						double oldValue = correlationLinkWeights.get(iterNeuron).get(layerID).get(neuronID);
						double newValue = oldValue + addValue;
						correlationLinkWeights.get(iterNeuron).get(layerID).put(neuronID, newValue);
						return;
					}
				}
				// not found the neuron to update!
				throw new NeuronNotFoundException();
			}
		}
		// not found the neuron to update!
		throw new NeuronNotFoundException();
	}

	public Classification getClassificationForInput(HashMap<Long, Long> inputIDs) throws InputErrorException {
		double bestOutput	 = neurons.get(0).applyInput(inputIDs);
		ALCNeuron bestNeuron = neurons.get(0);
		double iterOutput = 0;
		for(ALCNeuron iterNeuron : neurons) {
			iterOutput = iterNeuron.applyInput(inputIDs);
			if (iterOutput > bestOutput) {
				bestOutput = iterOutput;
				bestNeuron = iterNeuron;
			}
		}
		return classOfNeuron.get(bestNeuron);
	}

	public Classification getClassificationForInput(HashMap<Long, Long> inputIDs, HashMap<Long, Long> correlationLayersIDs) throws InputErrorException {
		double bestOutput	 = neurons.get(0).applyInput(inputIDs, correlationLayersIDs);
		ALCNeuron bestNeuron = neurons.get(0);
		double iterOutput = 0;
		for(ALCNeuron iterNeuron : neurons) {
			iterOutput = iterNeuron.applyInput(inputIDs, correlationLayersIDs);
			if (iterOutput > bestOutput) {
				bestOutput = iterOutput;
				bestNeuron = iterNeuron;
			}
		}
		return classOfNeuron.get(bestNeuron);
	}

	/**
	 * @return  the reward
	 * @uml.property  name="reward"
	 */
	public double getReward() {
		return reward;
	}

	/**
	 * @param reward  the reward to set
	 * @uml.property  name="reward"
	 */
	public void setReward(double reward) {
		this.reward = reward;
	}

	/**
	 * @return  the punish
	 * @uml.property  name="punish"
	 */
	public double getPunish() {
		return punish;
	}

	/**
	 * @param punish  the punish to set
	 * @uml.property  name="punish"
	 */
	public void setPunish(double punish) {
		this.punish = punish;
	}

	public Long getNeuronIDClassifying(Classification value) throws NeuronNotFoundException {
		for (ALCNeuron neuron : neurons) {
			if (classOfNeuron.get(neuron).equals(value)) {
				return neuron.getNeuronID();
			}
		}
		throw new NeuronNotFoundException();
	}

	public ArrayList<Long> getNeuronsIDs() {
		ArrayList<Long> IDs = new ArrayList<Long>();
		for (ALCNeuron iterNeuron : neurons) {
			IDs.add(iterNeuron.getNeuronID());
		}
		return IDs;
	}

	public Long getIndexOfMostImportantInputLayer() {
		// "important" link: one that has highest sum of input link weight
		double highestSum = 0;
		Long mostImportantID =0L;
		ALCNeuron initNeuron = null;
		// iterate over all layers ID
		for (ALCNeuron iter : inputLinkWeights.keySet()) {
			initNeuron = iter;
		}
		for (Long layerID: inputLinkWeights.get(initNeuron).keySet()) {
			double iterLinksSum = sumLinksOfInputLayerID(layerID);
			if (highestSum < iterLinksSum) {
				highestSum = iterLinksSum;
				mostImportantID = layerID;
			}
		}
		return mostImportantID;
	}

	private double sumLinksOfInputLayerID(Long inputID) {
		double sum = 0;
		// for each output neuron
		for (ALCNeuron iterOutputNeuronID : inputLinkWeights.keySet()) {
			// foreach neuron in the given layer
			for (Long iterOutputNeuronIDLayerIDneuron : inputLinkWeights.get(iterOutputNeuronID).get(inputID).keySet()) {
				sum += inputLinkWeights.get(iterOutputNeuronID).get(inputID).get(iterOutputNeuronIDLayerIDneuron);
			}
		}
		return sum;
	}

	public int getNumberOfNeurons() {
		return neurons.size();
	}

	public void printWeightsConfiguration() {
		System.out.println("Printing Input Link Weights internal configuration");
		for (ALCNeuron iterNeuron : neurons) {
			System.out.println("For output neuron (ID) " + Long.toString(iterNeuron.getNeuronID()));
			for(Long iterLayerID : inputLinkWeights.get(iterNeuron).keySet()) {
				System.out.println("For layer (ID) " + Long.toString(iterLayerID));
				for (Long iterNeuronID : inputLinkWeights.get(iterNeuron).get(iterLayerID).keySet()) {
					System.out.print(Double.toString(inputLinkWeights.get(iterNeuron).get(iterLayerID).get(iterNeuronID)) + " ");
				}
				System.out.println();
			}
		}
		System.out.println("");
	}
}
