package org.artificial_neural_networks.networks.lamstar;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.artificial_neural_networks.common.*;
import org.artificial_neural_networks.exceptions.*;

/**
 * @see  <a href="http://www.ece.uic.edu/~rgandhi/resume/LAMSTAR.pdf">What LAMSTAR ANN is</a>
 * @author  Riccardo Cattaneo
 * @version  0.1
 */
public class Lamstar implements Serializable{
	/**
	 * @serial This is useful for serializing the network and will be used
	 * to store the state of the object.
	 */
	private static final long serialVersionUID = -9128748485881649510L;
	/**
	 * The list of input layers of this network
	 */
	private ArrayList<LamstarInputLayer> inputLayers;
	/**
	 * The output layer
	 * @uml.property  name="outputLayer"
	 * @uml.associationEnd
	 */
	private LamstarOutputLayer outputLayer;
	/**
	 * The dataset we are currently training on
	 * @uml.property  name="trainingSet"
	 * @uml.associationEnd
	 */
	private Dataset trainingSet;
	/**
	 * The dataset we are currently testing on
	 * @uml.property  name="testingSet"
	 * @uml.associationEnd
	 */
	private Dataset testingSet;
	/**
	 * The ID of the next layer
	 */
	private Long currentLayerID;

	/**
	 * Default constructor.
	 * @param pTrainingSet the dataset this network has to be trained on
	 * @throws Exception
	 * @throws FeatureSetIDNotFoundException
	 */
	public Lamstar(Dataset pTrainingSet, Dataset pTestingSet, double maxAllowedDistance, double reward, double punish) throws FeatureSetIDNotFoundException, Exception {
		// init IDs of layer
		currentLayerID = 0L;
		trainingSet = pTrainingSet;
		testingSet  = pTestingSet;
		// determine the number of layers - 1 per feature
		int numFeatures = pTrainingSet.getNumOfFeatures();
		// for each feature set in dataset, create a layer and add all the data
		inputLayers = new ArrayList<LamstarInputLayer>(numFeatures);
		for (FeatureSet featureSet : pTrainingSet.getFeatureSets()) {
			// add all elements of this feature set to the layer, then add layer
			// to network. For each feature element in the feature set, add it
			// to the current layer (a SOM Neuron is created)
			LamstarInputLayer inputLayer = new LamstarInputLayer(maxAllowedDistance, currentLayerID, featureSet.getFeatureName());
			for (FeatureElement featureElement : featureSet.getFeatureElements()) {
				NeuralVector iterExample = new NeuralVector(featureElement.getDataVector());
				inputLayer.addExample(iterExample);
			}
			// now, add the layer to the network
			inputLayers.add(inputLayer);
			// set the next ID
			currentLayerID++;
		}
		// get classes and IDs
		TreeSet<Classification> classes = pTrainingSet.getClasses();
		HashMap<Long, Long[]> layersAndNeuronsIDs = new HashMap<Long, Long[]>();
		for (LamstarInputLayer iterInputLayer : inputLayers) {
			layersAndNeuronsIDs.put(iterInputLayer.getLayerID(), iterInputLayer.getAllNeuronsIDs());
		}
		// create output layer
		outputLayer = new LamstarOutputLayer(classes, layersAndNeuronsIDs, reward, punish);
	}

	/**
	 * Apply an input and get the corresponding Classification
	 * @param word a LamstarWord containing the data to classify
	 * @return the class of the input
	 * @throws InputErrorException
	 * @throws NotEnoughNeuronsException
	 */
	public Classification classify(LamstarWord word) throws InputErrorException, NotEnoughNeuronsException {
		if (!wordIsCoherent(word)) {
			throw new InputErrorException();
		}
		// get the winners per each layer
		HashMap<Long, Long> inputIDs = new HashMap<Long, Long>();
		for (int i=0; i<inputLayers.size(); i++) {
			LamstarInputLayer iterLayer = inputLayers.get(i);
			Long iterWinnerID = iterLayer.applyInputGetBestNeuronID(word.getSubword(i));
			inputIDs.put(iterLayer.getLayerID(), iterWinnerID);
		}
		return outputLayer.getClassificationForInput(inputIDs);
	}

	/**
	 * @param word the LamstarWord to check
	 * @return true iif iif for each layer, the corresponding subword has the
	 * expected length
	 */
	public boolean wordIsCoherent(LamstarWord word) {
		boolean goOn = true;
		if (word.numOfSubwords() == inputLayers.size()) {
			for(int i=0; i<word.numOfSubwords() && goOn; i++) {
				if (word.getSubword(i).getRowVector().length != inputLayers.get(i).getExpectedNeuralVectorLength())
					goOn = false;
			}
		}
		return goOn;
	}

	/**
	 * Training procedure. Each time is invokes, the network reinforces the
	 * links that have to be activated (and possibly punishes the others)
	 * @param consoleOutput where the outputs of the computation are sent out
	 * @param punishToo if true, all link weights to the current winners in
	 * those layers that must not be activated are punished
	 * @throws NeuronNotFoundException
	 * @throws InputErrorException
	 * @throws NotEnoughNeuronsException
	 */
	private void train(PrintStream consoleOutput, boolean punishToo) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		// 0) obtain classes
		ArrayList<Classification> classValues = trainingSet.getWordsClass();
		// 1) for each word of the dataset
		int numWords = trainingSet.getWordsNumber();
		for (int i=0; i<numWords; i++) {
			// 1.1) determine how much computation is left
			if (consoleOutput!=null) {
				double idiv = i;
				double numWordsidv = numWords;
				consoleOutput.println("-- Training iteration @ " + Double.toString(100*(idiv/numWordsidv)) + "%");
			}
			// 2) determine which output neuron k should be fired
			//    remember that elements in feature sets and classes are row
			//    aligned
			Classification iterClass = classValues.get(i);
			Long outputNeuronID = outputLayer.getNeuronIDClassifying(iterClass);
			// 3) determine the ID of the winners of the various layers
			// 3.1) create the word to classify
			ArrayList<NeuralVector> pSubWords = new ArrayList<NeuralVector>();
			ArrayList<FeatureSet> pFeatureSets = trainingSet.getFeatureSets();
			// 3.2) load it with all the sub words taken from the feature sets
			for (FeatureSet featureSet : pFeatureSets) {
				double[] data = featureSet.getFeatureElements().get(i).getDataVector();
				pSubWords.add(new NeuralVector(data));
			}
			LamstarWord pWord = new LamstarWord(pSubWords);
			// 3.3) get the ID of the winning neurons in each layer
			for (int j=0; j<inputLayers.size(); j++) {
				LamstarInputLayer iterLayer = inputLayers.get(j);
				pWord.getSubword(j).normalize();
				Long iterWinnerID = iterLayer.applyInputGetBestNeuronID(pWord.getSubword(j));
				// 4) and reinforce its link weights to op neuron k
				outputLayer.rewardInputNeuron(outputNeuronID, iterLayer.getLayerID(), iterWinnerID);
				// 5) optionally, punish the other neurons
				if (punishToo) {
					for (Long iterNeuronOutputID : outputLayer.getNeuronsIDs()) {
						if (iterNeuronOutputID != outputNeuronID) {
							outputLayer.rewardInputNeuron(iterNeuronOutputID, iterLayer.getLayerID(), iterWinnerID);
						}
					}
				}
			}
		}
	}

	/**
	 * Specialized version of train. When the first between numberOfIterations and performanceGoal is reached,
	 * the function returns with the current performance on testing set. Prints the progresses to consoleOutput
	 * @param consoleOutput the PrintStream to which sending the progress stream
	 * @param numberOfIterations maximum number of allowed repetitions of training phase
	 * @param performanceGoal maximum accuracy reachable
	 * @param punishToo whether or not to add punishment factor when training
	 * @throws NeuronNotFoundException
	 * @throws InputErrorException
	 * @throws NotEnoughNeuronsException
	 */
	public double[] train(PrintStream consoleOutput, int numberOfIterations, double performanceGoal, boolean punishToo) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		consoleOutput.println("Training has begun. Progress:");
		int numOfIter = 1;
		ArrayList<Double> performanceVector = new ArrayList<Double>();
		double[] test;
		double perIter = 0;
		double performanceWithoutTraining = performance();
		while(numberOfIterations > 0 && perIter < performanceGoal) {
			train (consoleOutput, punishToo);
			perIter = performance();
			performanceVector.add(perIter);
			numberOfIterations--;
			numOfIter++;
			// TODO remove, for debug purposes
			this.printConfiguration();
		}
		test = new double[performanceVector.size()+1];
		test[0] = performanceWithoutTraining;
		for (int i=0; i<performanceVector.size(); i++) {
			test[i+1] = performanceVector.get(i);
		}
		return test;
	}

	/**
	 * Specialized version of train. When the first between numberOfIterations and performanceGoal is reached,
	 * the function returns with the current performance on testing set
	 * @param numberOfIterations maximum number of allowed repetitions of training phase
	 * @param performanceGoal maximum accuracy reachable
	 * @param punishToo whether or not to add punishment factor when training
	 * @throws NeuronNotFoundException
	 * @throws InputErrorException
	 * @throws NotEnoughNeuronsException
	 */
	public double[] train(int numberOfIterations, double performanceGoal, boolean punishToo) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		ArrayList<Double> performanceVector = new ArrayList<Double>();
		double[] test;
		double perIter = 0;
		double performanceWithoutTraining = performance();
		while(numberOfIterations > 0 && perIter < performanceGoal) {
			train(null, punishToo);
			perIter = performance();
			performanceVector.add(perIter);
			numberOfIterations--;
		}
		test = new double[performanceVector.size()+1];
		test[0] = performanceWithoutTraining;
		for (int i=0; i<performanceVector.size(); i++) {
			test[i+1] = performanceVector.get(i);
		}
		return test;
	}

	/**
	 * Default train procedure. Just one iteration of the process.
	 * @throws NeuronNotFoundException
	 * @throws InputErrorException
	 * @throws NotEnoughNeuronsException
	 */
	public void train() throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		train(null, false);
	}

	public double performance(PrintStream consoleOutput, Dataset pTestingSet) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		// 0) obtain classes and init performance variable
		if (testingSet == null && pTestingSet == null) {
			throw new InputErrorException("I was never told what dataset to use when testing.");
		}
		if (pTestingSet != null) {
			testingSet = pTestingSet;
		}
		ArrayList<Classification> classValues = testingSet.getWordsClass();
		int correctWords = 0;
		// 1) for each word of the testing dataset
		int numWords = testingSet.getWordsNumber();
		for (int i=0; i<numWords; i++) {
			// 1.1) determine how much computation is left
			if (consoleOutput!=null) {
				double idiv = i;
				double numWordsidv = numWords;
				consoleOutput.println("-- Performance iteration @ " + Double.toString(100*(idiv/numWordsidv)) + "%");
			}
			// 2) determine the class of the current word
			Classification iterClass = classValues.get(i);
			// 3) determine the ID of the winners of the various layers
			// 3.1) create the word to classify
			ArrayList<NeuralVector> pSubWords = new ArrayList<NeuralVector>();
			ArrayList<FeatureSet> pFeatureSets = testingSet.getFeatureSets();
			// 3.2) load it with all the sub words taken from the feature sets
			for (FeatureSet featureSet : pFeatureSets) {
				double[] data = featureSet.getFeatureElements().get(i).getDataVector();
				pSubWords.add(new NeuralVector(data));
			}
			LamstarWord pWord = new LamstarWord(pSubWords);
			// 3.3) get the ID of the winning neurons in each layer
			HashMap<Long, Long> inputWinnersID = new HashMap<Long, Long>();
			for (int j=0; j<inputLayers.size(); j++) {
				LamstarInputLayer iterLayer = inputLayers.get(j);
				Long iterWinnerID = iterLayer.applyInputGetBestNeuronID(pWord.getSubword(j));
				inputWinnersID.put(iterLayer.getLayerID(), iterWinnerID);
			}
			// 4) classify and test that class is correctly predicted
			Classification classificationValue = outputLayer.getClassificationForInput(inputWinnersID);
			if (classificationValue.equals(iterClass)) {
				correctWords++;
			}
		}
		return new Double(correctWords)/new Double(numWords)*100;
	}

	public double performance(PrintStream consoleOutput) throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		return performance(consoleOutput, null);
	}

	public double performance() throws NeuronNotFoundException, InputErrorException, NotEnoughNeuronsException {
		return performance(null, null);
	}

	public void setTrainingSet(Dataset pTrainingSet, double maxAllowedDistance, double reward, double punish) throws NotEnoughClassesException, NotEnoughNeuronsException {
		// init IDs of layer
		currentLayerID = 0L;
		trainingSet = pTrainingSet;
		// determine the number of layers - 1 per feature
		int numFeatures = trainingSet.getNumOfFeatures();
		// for each feature set in dataset, create a layer and add all the data
		inputLayers = new ArrayList<LamstarInputLayer>(numFeatures);
		for (FeatureSet featureSet : pTrainingSet.getFeatureSets()) {
			// add all elements of this feature set to the layer, then add layer
			// to network. For each feature element in the feature set, add it
			// to the current layer
			LamstarInputLayer inputLayer = new LamstarInputLayer(maxAllowedDistance, currentLayerID, featureSet.getFeatureName());
			ArrayList<FeatureElement> featureElements = featureSet.getFeatureElements();
			for (FeatureElement featureElement : featureElements) {
				NeuralVector iterExample = new NeuralVector(featureElement.getDataVector());
				inputLayer.addExample(iterExample);
			}
			// now, add the layer to the network
			inputLayers.add(inputLayer);
			// set the next ID
			currentLayerID++;
		}
		// get classes and IDs
		TreeSet<Classification> classes = pTrainingSet.getClasses();
		HashMap<Long, Long[]> layersAndNeuronsIDs = new HashMap<Long, Long[]>();
		for (LamstarInputLayer iterInputLayer : inputLayers) {
			layersAndNeuronsIDs.put(iterInputLayer.getLayerID(), iterInputLayer.getAllNeuronsIDs());
		}
		// create output layer
		outputLayer = new LamstarOutputLayer(classes, layersAndNeuronsIDs, reward, punish);
	}

	/**
	 * @param pTestingSet
	 * @uml.property  name="testingSet"
	 */
	public void setTestingSet(Dataset pTestingSet) {
		testingSet = pTestingSet;
	}

	public Long getIndexOfMostImportantInputLayers() {
		return outputLayer.getIndexOfMostImportantInputLayer();
	}

	public void printConfiguration() {
		for (LamstarInputLayer layer : inputLayers) {
			System.out.println("Layer ID " + Long.toString(layer.getLayerID()));
			System.out.println("Neurons contained: " + Integer.toString(layer.getNumberOfNeurons()));
			System.out.println("Tolerance" + Double.toString(layer.getTolerance()));
			for (Long neuroID : layer.getAllNeuronsIDs()) {
				System.out.print(Long.toString(neuroID) + " ");
			}
			System.out.println();
			System.out.println("Most important layer ID " + Long.toString(getIndexOfMostImportantInputLayers()));
			System.out.println();
		}
		outputLayer.printWeightsConfiguration();
	}
}
