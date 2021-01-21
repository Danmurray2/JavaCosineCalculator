package is.gmit.dip;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles interface with the user and other menu option selections
 * 
 * @author G00387933
 *
 */
public class Manager {

	private String userQuery;
	private String userSubject;
	private Scanner s;
	private int userChoice;
	private Parser subject = new Parser();
	private Parser query = new Parser();
	private Database data = new Database();
	private boolean keepRunning;

	public Manager() {
		this.s = new Scanner(System.in);

	}

	public void startMenu() {
		keepRunning = true;
		do { // do/while loop used to return to main menu until exit option breaks
			displayMenu(); // main menu display
			start(); // menu option selection process
		} while (keepRunning);
	}

	/**
	 * Displays the menu options to the user.
	 */
	public void displayMenu() {
		System.out.print("~~~~~~~Cosine Similarity Checker~~~~~~\n");
		System.out.println("---------------------------------------");
		System.out.println("1. Enter New File's.");
		System.out.println("2. Quit.");
		System.out.println("--------------------------");
	}

	/*
	 * Start method begins the program, and assigns user input.
	 */
	public void start() {
		try {
			int option = Integer.parseInt(s.nextLine());
			if (option == 1) {
				// User prompt to enter file locations
				System.out.println("-> Enter a subject  file: ");
				this.userSubject = s.nextLine();
				System.out.println("-> Enter a query  file: ");
				this.userQuery = s.nextLine();

				// Prompts user for shingle or kmers.
				System.out.println("Type 'w' for word shingles, or 'k' for kmers");
				String user = s.nextLine();

				if (user.equalsIgnoreCase("w")) {
					System.out.println("Word shingles selected..enter word group size ");
					this.userChoice = Integer.valueOf(s.nextLine());
					System.out.println("Selected shingle size of: " + userChoice);

				} else if (user.equalsIgnoreCase("k")) {
					this.userChoice = 0;
					System.out.println("Selected kmers.. default value is 5 char kmers");

				} else {
					System.out.println("Invalid selection..type 's' to start over, or any other key to quit");
					String choice = s.nextLine();
					if (choice.equalsIgnoreCase("s")) {
						startMenu();
					} else {
						System.out.println("Exiting program..");
						keepRunning = false;
						System.exit(0);

					}
				}
				System.out.println("Processing....\n");
				process();
				calculateSimilarity();
			} else if (option == 2) {
				System.out.println("You have selected quit.. exiting");
				keepRunning = false;
				System.exit(0);
			} else {
				System.out.println("Invalid input (1-2 only). \n");
				startMenu();
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage() + " try again.. \n");
			startMenu();
		}

	}

	public void process() {
		// create new Parser objects
		this.subject = new Parser(this.userSubject, this.userChoice);
		this.query = new Parser(this.userQuery, this.userChoice);

		// pass into threads
		Thread t1 = new Thread(subject, "UserSubject");
		Thread t2 = new Thread(query, "UserQuery");

		// start thread execution
		t1.start();
		t2.start();
		// Wait until both threads are finished
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void calculateSimilarity() {
		// assign HashMaps to Database class variables
		Map<Integer, Integer> hashMapSubject = subject.getHashMap();
		data.setHashMapSubject(hashMapSubject);

		Map<Integer, Integer> hashMapQuery = query.getHashMap();
		data.setHashMapQuery(hashMapQuery);

		// Calculate cosine similarity
		try {
			DecimalFormat numberFormat = new DecimalFormat("#.00"); // to limit to 2 decimal places
			double result = data.cosineSimilarity() * 100;
			System.out.print("----------------------------------------------");
			System.out.println("\nSimilarity between two documents = " + numberFormat.format(result) + "%");
			System.out.print("----------------------------------------------\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
