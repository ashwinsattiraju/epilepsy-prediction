package org.compression_algorithms.lzw;

import java.util.*;

/**
 * @author riccardo
 *
 */
public class LZWmanager {

	public LZWmanager () {}

    /** Compress a string to a list of output symbols. */
    public List<Integer> compress(String uncompressed) {
        // Build the dictionary of ASCII characters.
        int dictSize = 256;
        Map<String,Integer> dictionary = new HashMap<String,Integer>(1000);
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char)i, i);

        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));
        return result;
    }

    /** Decompress a list of output ks to a string. */
    public String decompress(List<Integer> compressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)compressed.remove(0);
        String result = w;
        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result += entry;

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }
        return result;
    }

    /**
     *
     * @param input
     * @return
     */
    public double getCompressionRate(double[] input) {
    	String toCompress = "";
    	for(int i=0; i<input.length; i++) {
    		toCompress += Long.toBinaryString(Double.doubleToRawLongBits(input[i]));
    	}
    	double originalLength = toCompress.length();
    	double newLength = this.compress(toCompress).size();
    	return newLength/originalLength;
    }
}
