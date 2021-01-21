package is.gmit.dip;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Database class containing both HashMap databases. Calculates cosine
 * similarity between subject and query text files.
 * 
 * @author G00387933
 *
 */
public class Database {
	private Map<Integer, Integer> hashMapSubject;
	private Map<Integer, Integer> hashMapQuery;

	public Database() {
		this.hashMapSubject = new HashMap<Integer, Integer>();
		this.hashMapQuery = new HashMap<Integer, Integer>();
	}

	/**
	 * Calculates cosine similarity. New HashSet created to create unique Set
	 * containing keys shared by both maps. This eliminates redundant zero values
	 * and allows for dot product calculation. Outputs the cosine distance in
	 * percentage.
	 * 
	 * @throws IOException
	 */
	public double cosineSimilarity() throws IOException {

		HashSet<Integer> intersection = new HashSet<>(hashMapSubject.keySet());
		intersection.retainAll(hashMapQuery.keySet());

		double dotProduct = 0;
		double magnitudeSubject = 0;
		double magnitudeQuery = 0;

		// Calculate dot product
		for (Integer item : intersection) {
			dotProduct += hashMapSubject.get(item) * hashMapQuery.get(item);

		}

		// Calculate magnitude a
		for (Integer k : hashMapSubject.keySet()) {
			magnitudeSubject += Math.pow(hashMapSubject.get(k), 2);

		}

		// Calculate magnitude b
		for (Integer k : hashMapQuery.keySet()) {
			magnitudeQuery += Math.pow(hashMapQuery.get(k), 2);

		}

		double result = dotProduct / Math.sqrt(magnitudeSubject * magnitudeQuery);

//		System.out.println("Dot product " + dotProduct);
//		System.out.println("MagnitudeA " + magnitudeSubject);
//		System.out.println("Magnitude B " + magnitudeQuery + "\n");
		
		return result;
	}

	// Setters to allow assignment of values to HashMaps.
	protected void setHashMapSubject(Map<Integer, Integer> hashMapSubject) {
		this.hashMapSubject = hashMapSubject;
	}

	protected void setHashMapQuery(Map<Integer, Integer> hashMapQuery) {
		this.hashMapQuery = hashMapQuery;
	}

}
