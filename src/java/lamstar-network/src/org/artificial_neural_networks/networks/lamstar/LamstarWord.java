/**
 * 
 */
package org.artificial_neural_networks.networks.lamstar;

import java.util.ArrayList;

import org.artificial_neural_networks.common.NeuralVector;

/**
 * @author riccardo
 *
 */
public class LamstarWord {
	private ArrayList<NeuralVector> subWords;
	
	public LamstarWord(ArrayList<NeuralVector> pSubWords) {
		if (!(pSubWords == null)) {
			if (pSubWords.size() >= 1) {
				subWords = pSubWords;
				return;
			}
		}
		throw new NullPointerException();
	}
	
	public int numOfSubwords() {
		return subWords.size();
	}
	
	public NeuralVector getSubword(int indexOfSubWord) {
		return subWords.get(indexOfSubWord);
	}
}
