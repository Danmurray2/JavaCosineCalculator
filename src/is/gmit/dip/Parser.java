
package is.gmit.dip;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements Runnable and allows multithreaded parsing and Map
 * storage
 * 
 * @author G00387933
 *
 */
public class Parser implements Runnable {
	private Map<Integer, Integer> hashMap;
	private int user;
	private String text;
	private String line;
	private String[] words;
	private String[] shingleArray;
	
	/*
	 * Constructor with users chosen input as parameters
	 */
	public Parser(String text, int user) {
		this.text = text;
		this.user = user;
		this.hashMap = new HashMap<>();
	}
	public Parser() {	
		this.hashMap = new HashMap<>();
	}
	
	@Override
	public void run() {
	processFile();	
	}
	/**
	 * Responsible for file processing. Reads file and generates either word
	 * shingles or kmers, followed by further processing into HashMaps
	 * 
	 * @return new HashMap containing hashcodes and their corresponding frequencies.
	 */
	private Map<Integer, Integer> processFile() {
	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.text)));
			
			while ((line = br.readLine()) != null) {
				//remove whitespaces 
				line.toLowerCase().replaceAll(",", "");

				// generates shingles
				if (this.user >= 1) {
					words = line.split(" ");
					wordShingles();

					// regex used to split, creating kmers
				} else {
					kmers();
				}

				generateMap();
				
				//Show threads interleaving
				//System.out.println(Thread.currentThread().getName());

			}
		} catch (IOException e) {
			System.out.println("File not found!");
			e.getMessage();
		}

		return hashMap;
	}

	/**
	 * This returns a new array with word shingles specified by user. Math.ceil
	 * ensures the new array size rounds up to avoid OOB. Arrays.copyOfRange copies
	 * specified range into new array. String.join used to create spaces between
	 * words. Math.min ensures no OOB on the very last iteration.
	 * 
	 * @return new array returned.
	 */
	private String[] wordShingles() {
		this.shingleArray = new String[(int) Math.ceil((double) this.words.length / this.user)];

		int newArrayPos = 0;
		for (int i = 0; i < words.length; i += user) {
			shingleArray[newArrayPos] = String.join(" ",
					Arrays.copyOfRange(words, i, Math.min(words.length, i + user)));
			newArrayPos++;
		}

		return shingleArray;
	}
	/*
	 * Generates kmers of character size 5 
	 */
	private String[] kmers() {
		return this.words = line.split("(?<=\\G.{5})");
	}
	
	/**
	 * Converts shingles into hashcodes, calculates their frequency, and stores in
	 * HashMap
	 */
	private void generateMap() {
			// kmer map generate. int user defaults to zero for kmer choice
		if (this.user == 0) {
			for (String word : words) {
				int hash = word.hashCode();
				int frequency = 1;
				if (hashMap.containsKey(hash)) {
					frequency += hashMap.get(hash);
				}
				hashMap.put(hash, frequency);}
		
			// word shingle map generate
		} else {

			for (String word : shingleArray) {				
				//System.out.println(word); //to show new shingles
				int hash = word.hashCode();
				int frequency = 1;
				if (hashMap.containsKey(hash)) {
					frequency += hashMap.get(hash);
				}
				hashMap.put(hash, frequency);
				
				
			}
			
		}
	}
	//getter
	protected Map<Integer, Integer> getHashMap() {
		return hashMap;
	}

	
}
