/**
 *
 */
package org.artificial_neural_networks.common;

import org.artificial_neural_networks.exceptions.*;

/**
 * This neuron is the core of a SOM layer / SOM network. It does store in itself a normalized weight vector and applies the inner product between it and the input in order to compute the output.
 * @author  riccardo
 */
public class SOMNeuron extends Neuron {

	/**
	 * @serial This is useful for serializing the network and will be used
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = -2943314016711582910L;
	/**
	 * The input weights of this neuron
	 * @uml.property  name="weightsVector"
	 * @uml.associationEnd  
	 */
	protected NeuralVector weightsVector;
	/**
	 * Default constructor.
	 * @param pWeightsVector the weight vector this neuron has to recognize. It
	 * can be un normalized, but in this case the vector is saved after
	 * normalization.
	 * @param pID the unique ID assigned to this neuron. This ID must be unique
	 * inside the layer in which the neuron is found.
	 */
	public SOMNeuron(NeuralVector pWeightsVector, long pID) {
		weightsVector = pWeightsVector;
		neuronID = pID;
		weightsVector = normalizeVector(pWeightsVector);
	}

	/**
	 * Default constructor.
	 * @param pWeightsVector the weight vector this neuron has to recognize. It
	 * can be un normalized, but in this case the vector is saved after
	 * normalization.
	 * @param pID the unique ID assigned to this neuron. This ID must be unique
	 * inside the layer in which the neuron is found.
	 */
	public SOMNeuron(double[] pWeightsVector, long pID) {
		weightsVector = new NeuralVector(pWeightsVector);
		neuronID = pID;
		weightsVector = normalizeVector(pWeightsVector);
	}

	/**
	 * This method applies a NeuralVector as input and returns the value
	 * obtained computing the inner product between neuron's weights and input
	 * itself.
	 * @see org.artificial_neural_networks.common.Neuron#applyInput(org.artificial_neural_networks.common.NeuralVector)
	 * @param input MUST BE a normalized vector
	 * @throws InputErrorException
	 */
	public double applyInput(NeuralVector input) throws InputErrorException {
		// check input is normalized
		if (!input.isNormalized()) {
			double[] d =input.getRowVector();
			for (int i=0; i<d.length; i++) {
				System.err.println(d[i]);
			}
			throw new VectorNonNormalizedException();
		}
		try {
			return weightsVector.dotProductWith(input);
		} catch (VectorDimensionMismatchException e) {
			throw new InputErrorException(e);
		} catch (VectorDimensionIsZeroException e) {
			throw new InputErrorException(e);
		}
	}


	/**
	 * @see org.artificial_neural_networks.common.Neuron#applyInput(double[])
	 */
	public double applyInput(double[] input) throws InputErrorException {
		NeuralVector nvInput = new NeuralVector(input);
		nvInput.normalize();
		return applyInput(nvInput);
	}

	/**
	 * @return  the weightsVector
	 * @uml.property  name="weightsVector"
	 */
	public NeuralVector getWeightsVector() {
		return weightsVector;
	}

	/**
	 * @param weights
	 * @uml.property  name="weightsVector"
	 */
	public void setWeightsVector(NeuralVector weights) {
		weightsVector = weights.clone();
	}

	/* (non-Javadoc)
	 * @see org.artificial_neural_networks.common.Neuron#updateWeights(double[])
	 */
	public void setWeightsVector(double[] weights) {
		setWeightsVector(new NeuralVector(weights));
	}

	/**
	 * @param neuron a Neuron to compare this against
	 * @return true iif ID of neuron and this are equal or if weights are equal
	 * @see org.artificial_neural_networks.common.Neuron#equals(org.artificial_neural_networks.common.Neuron)
	 */
	public boolean equals(SOMNeuron neuron) {
		// if ID == ID true
		if (this.neuronID == neuron.getNeuronID())
			return true;
		// else, equality relies on the equality of the weights vector
		return weightsVector.equals(neuron.getWeightsVector());
	}

	/**
	 * Returns a copy of this neuron, the ID is copied too.
	 * @see org.artificial_neural_networks.common.Neuron#clone()
	 */
	@Override
	public SOMNeuron clone() {
		return new SOMNeuron(weightsVector.clone(), neuronID);
	}


	public double distanceWith(NeuralVector vector) throws VectorDimensionMismatchException, VectorDimensionIsZeroException {
		return 1-weightsVector.dotProductWith(vector);
	}

	/**
	 * This method returns the normalized vector obtained from unnormalized
	 * @param unnormalized vector of doubles non normalized
	 * @return a NeuralVector representing the normalized version of unnormlized
	 */
	private NeuralVector normalizeVector(double[] unnormalized) {
		double normFactor = 0;
		double[] normalized = new double[unnormalized.length];
		for(int i=0; i<unnormalized.length;i++) {
			normFactor += Math.pow(unnormalized[i], 2);
		}
		normFactor = Math.sqrt(normFactor);
		if (normFactor != 0) {
			for(int i=0; i<unnormalized.length;i++) {
				normalized[i] = unnormalized[i] / normFactor;
			}
		} else {
			for(int i=0; i<unnormalized.length;i++) {
				normalized[i] = 0;
			}
		}
		return new NeuralVector(normalized);
	}

	/**
	 * This method returns the normalized vector obtained from unnormalized
	 * @param unnormalized NeuralVector non normalized
	 * @return a NeuralVector representing the normalized version of unnormlized
	 */
	private NeuralVector normalizeVector(NeuralVector unnormalized) {
		return normalizeVector(unnormalized.getRowVector());
	}


}
