package org.artificial_neural_networks.networks.lamstar;

import java.io.Serializable;
import java.util.ArrayList;

import org.artificial_neural_networks.common.*;
import org.artificial_neural_networks.exceptions.*;

/**
 * @author  Riccardo Cattaneo
 * @version  0.1  This class represents the abstraction of a LAMSTAR input layer. It is a  container of SOM neurons.
 */
public class LamstarInputLayer extends Layer implements Serializable{
	/**
	 * @serial This is useful for serializing the network and will be used
	 * (eg:by Matlab) to store the state of the object.
	 */
	private static final long serialVersionUID = 3895934280726206407L;
	/**
	 * The neurons contained in this layer.
	 */
	private ArrayList<SOMNeuron> neurons;
	/**
	 * The tolerance in the neuronal distance.
	 * @uml.property  name="tolerance"
	 */
	private double tolerance;
	/**
	 * This is the unique ID of the layer
	 * @uml.property  name="layerID"
	 */
	private long layerID;
	/**
	 * The name of the layer
	 * @uml.property  name="layerName"
	 */
	private String layerName;
	/**
	 * The expected length of the subword entering this layer
	 * @uml.property  name="expectedNeuralVectorLength"
	 */
	private int expectedNeuralVectorLength;

	/**
	 * This is the default constructor.
	 * @param  pTolerance is the maximum allowed distance between two neurons.
	 * If a new example is presented to the network, a new neuron is created iif
	 * there is no neuron whose distance from this example is less than
	 * pTolerance.
	 * @param pID the unique ID of the layer. Uniquenessmust be guaranteed
	 * in the network among all the layers.
	 */
	public LamstarInputLayer(double pTolerance, long pID, String pLayerName) {
		super();
		this.neurons = new ArrayList<SOMNeuron>();
		tolerance = pTolerance;
		layerID = pID;
		layerName = pLayerName;
		currentNeuronID = 0;
		expectedNeuralVectorLength = -1;
	}

	/**
	 * This constructor takes an ArrayList<Neuron> and creates a layer out of
	 * it.
	 * @param pNeurons an ArrayList<Neuron> of Neuron.
	 * @param  pTolerance is the maximum allowed distance between two neurons.
	 * If a new example is presented to the network, a new neuron is created iif
	 * there is no neuron whose distance from this example is less than
	 * pTolerance.
	 * @param pID the unique ID of the layer. Uniquenessmust be guaranteed
	 * in the network among all the layers.
	 * @throws NotEnoughNeuronsException
	 */
	public LamstarInputLayer(ArrayList<Neuron> pNeurons, double pTolerance, long pID, String pLayerName) throws NotEnoughNeuronsException {
		super(pNeurons);
		tolerance = pTolerance;
		layerID = pID;
		layerName = pLayerName;
		currentNeuronID = 0;
		expectedNeuralVectorLength = -1;
	}

	/**
	 * @return  the tolerance
	 * @uml.property  name="tolerance"
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance  the tolerance to set
	 * @uml.property  name="tolerance"
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return  the layerID
	 * @uml.property  name="layerID"
	 */
	public long getLayerID() {
		return layerID;
	}

	/**
	 * This method returns the ID of the neuron inside this layer which is best
	 * describing the input.
	 * @param inputVector the vector of doubles that represents the input vector
	 * @return the ID of the neuron inside this layer whose response to the
	 * input is maximum
	 * @throws InputErrorException this exception is thrown if there is any kind
	 * of error with the input (dimension-related)
	 * @throws NotEnoughNeuronsException this exception is thrown whenever the
	 * layer doesn't contain any neurons
	 */
	public long applyInputGetBestNeuronID(double[] inputVector) throws InputErrorException, NotEnoughNeuronsException{
		// Throw if there are not enough neurons in this layer
		if (this.neurons.isEmpty()) {
			throw new NotEnoughNeuronsException("In package org.artificial_neural_networks.networks.lamstar/LamstarInputLayer@applyInputGetNeuronID: no neurons found in me!\n");
		}
		// find neuron whose response to input is maximum
		long bestMatchingID = this.neurons.get(0).getNeuronID();
		double bestMatchingOutput = this.neurons.get(0).applyInput(inputVector);
		double iterOutput = 0;
		// Iterate over all neurons and get their o/p; if gte to the current
		// maximum, store ID else go to next neuron; if end, stop
		for (SOMNeuron n : this.neurons) {
			iterOutput = n.applyInput(inputVector);
			if (n.applyInput(inputVector) > bestMatchingOutput) {
				bestMatchingOutput = iterOutput;
				bestMatchingID = n.getNeuronID();
			}
		}
		return bestMatchingID;
	}

	/**
	 * This method returns the ID of the neuron inside this layer which is best
	 * describing the input.
	 * @param inputVector the NeuralVector that represents the input vector
	 * @return the ID of the neuron inside this layer whose response to the
	 * input is maximum
	 * @throws InputErrorException this exception is thrown if there is any kind
	 * of error with the input (dimension-related)
	 * @throws NotEnoughNeuronsException this exception is thrown whenever the
	 * layer doesn't contain any neurons
	 */
	public long applyInputGetBestNeuronID(NeuralVector inputVector) throws InputErrorException, NotEnoughNeuronsException{
		return applyInputGetBestNeuronID(inputVector.getRowVector());
	}

	/**
	 * This method adds a new example to the network
	 * @param inputVector the example that has to be added
	 */
	public void addExample(NeuralVector inputVector) {
		// if there's no neuron whose response is under the tolerance level, add
		// a new neuron fitted on this example
		try {
			if (expectedNeuralVectorLength == -1) {
				// assign expectedNeuralVectorLength first time an example is
				// addded to the layer
				expectedNeuralVectorLength = inputVector.getRowVector().length;
			}
			if (inputVector.getRowVector().length != expectedNeuralVectorLength) {
				throw new InputErrorException();
			}
			inputVector.normalize();
			long bestID = applyInputGetBestNeuronID(inputVector);
			double bestOutput = getNeuronByID(bestID).applyInput(inputVector);
			if (Math.abs(1-bestOutput) > tolerance) {
				this.neurons.add(new SOMNeuron(inputVector, currentNeuronID));
				currentNeuronID++;
			}
		} catch (InputErrorException e) {
			System.err.println("The example was not of the expected length/dimension.");
			e.printStackTrace();
		} catch (NeuronNotFoundException e) {
			System.err.println("The ID was not found in this layer.");
			e.printStackTrace();
		} catch (NotEnoughNeuronsException e) {
			// This is because the layer is empty
			this.neurons.add(new SOMNeuron(inputVector, currentNeuronID));
			currentNeuronID++;
		}
	}

	/**
	 * @return  the layerName
	 * @uml.property  name="layerName"
	 */
	public String getLayerName() {
		return layerName;
	}

	/**
	 * @param layerName  the layerName to set
	 * @uml.property  name="layerName"
	 */
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	/**
	 * This method returns a shallow copy (reference) to the neuron that has the
	 * given ID
	 * @param pNeuronID the ID of the neuron sought in this layer
	 * @return a shallow copy to this neuron, if there exists a neuron with that
	 * ID
	 * @throws NeuronNotFoundException if the ID is not found
	 */
	public SOMNeuron getNeuronByID(Long pNeuronID) throws NeuronNotFoundException {
		for (SOMNeuron iterNeuron : this.neurons) {
			if (iterNeuron.getNeuronID() == pNeuronID)
				return iterNeuron;
		}
		throw new NeuronNotFoundException();
	}

	/**
	 * @return the IDs of all the neurons inside this layers
	 */
	public Long[] getAllNeuronsIDs() {
		Long[] type = new Long[1];
		ArrayList<Long> outputValue = new ArrayList<Long>();
		for (SOMNeuron iterNeuron : this.neurons) {
			outputValue.add(iterNeuron.getNeuronID());
		}
		return outputValue.toArray(type);
	}

	/**
	 * @return  the expectedNeuralVectorLength
	 * @uml.property  name="expectedNeuralVectorLength"
	 */
	public int getExpectedNeuralVectorLength() {
		return expectedNeuralVectorLength;
	}

	public int getNumberOfNeurons() {
		return this.neurons.size();
	}
}
