import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class WordRecommender2 {
	public String[] dictionary = new String[46372];
	public ArrayList<String> spellCheckThisFile = new ArrayList<String>();
	public boolean wordRecAvailable = true;

	/**
	 * Required per the assignment instructions
	 * Constructor with one instance variable – a String filename 
	 * that has a dictionary of words
	 * @param fileName
	 */
	public WordRecommender2(String fileName) {
		// TODO Auto-generated constructor stub
		File f = new File(fileName);
		try {
			Scanner input = new Scanner(f);
			int word = 0;
			while (input.hasNextLine()) {
				dictionary[word] = input.nextLine();
				word++;
				
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Required per the assignment instructions
	 * - return the list of words in the dictionary that have at least (>=) n letters in common
	 * - consider only the distinct letters in the word
	 * @param word - the word that requires options for replacement
	 * @param listOfWords - possible replacement words
	 * @param n - number of letter in common
	 * @return
	 */
	public ArrayList<String> getWordsWithCommonLetters(String word,
			ArrayList<String> listOfWords, int n) {
		ArrayList<String> refinedListOfWords = new ArrayList<String>();
		String distinctWord;
		String newWord = getDistinctWord(word);
		for (String wordInList : listOfWords) {
			int count = 0;
			distinctWord = getDistinctWord(wordInList);
			for (int i=0; i < newWord.length(); i++) {
				for (int j=0; j<distinctWord.length(); j++) {
					if (newWord.charAt(i) == distinctWord.charAt(j)) {
						count++;
					}
				}
			}
			if (count >= n) {
				refinedListOfWords.add(wordInList);
			}
		}
		return refinedListOfWords;
	}
	
	/**
	 * Required per the assignment instructions
	 * - given two words, this function computes two measures of similarity and returns the average
	 * - to get the similarity score, take the average of leftSimilarity and rightSimilarity and 
	 * return that value
	 * @param word1
	 * @param word2
	 * @return the average of left and right similarity metrics
	 */

	public double getSimilarityMetric(String word1, String word2) {
		double average = (leftSimilarity(word1, word2) + rightSimilarity(word1, word2)) / 2;
		return average;
	}
	
	/**
	 * Required per the assignment instructions
	 * - given an incorrect word, return a list of legal word suggestions as per an algorithm.
	 * - assume this function will only be called with a word that is not already present in 
	 * the dictionary
	 * - come up with a list of candidate words that satisfy both of these two criteria
	 * - a) candidate word length is word length +/- n characters
	 * - b) have at least commonPercent% of the letters in common
	 * - For the letters in common, consider only the distinct letters in each word
	 * - for all the words that satisfy these two criteria, order them based on the similarity metric
	 * and return the topN number of them.
	 * @param word
	 * @param n - the accepted length of the word
	 * @param commonPercent - should be a double between 0.0, corresponding to 0%, and 
	 * 1.0, corresponding to 100%
	 * @param topN - the top words that can be presented to the user
	 * @return - the final list of words suggested to the user
	 */
	public ArrayList<String> getWordSuggestions(String word, int
			n, double commonPercent, int topN) {
		ArrayList<String> suggestedWords = new ArrayList<String>();
		ArrayList<String> commonWords = new ArrayList<String>();
		suggestedWords = wordLengthMatches(word, n);
		if (suggestedWords.size() == 0) {
			wordRecAvailable = false;
			return suggestedWords;
		} else {
			wordRecAvailable = true;
		}
			
		int numOfLetters = (int) (word.length() * commonPercent);
		commonWords = getWordsWithCommonLetters(word, suggestedWords, numOfLetters);
		if (commonWords.size() == 0) {
			wordRecAvailable = false;
			return commonWords;
		} else {
			wordRecAvailable = true;
		}

		double[] averageSimilarity = new double[commonWords.size()];
		int i=0;
		for (String sWord : commonWords) {
			averageSimilarity[i] = getSimilarityMetric(word, sWord);
			i++;
		}
		ArrayList<String> topWords = new ArrayList<String>();
	
		double[] topSimilarities = new double[commonWords.size()]; // Working array, to be overwritten
		for (int k=0; k < commonWords.size(); k++) { // make a copy of averageSimilarity
			topSimilarities[k]=averageSimilarity[k];
		}
		
		for (int j=0; j < topN; j++) { // fill the top few elements
			int curTop = 0;
		    double topValue = topSimilarities[0]; // start from start
			for (int k=0; k < commonWords.size(); k++) {
				if (topSimilarities[k] >= topValue) {
					curTop = k; // top element so far
					topValue=topSimilarities[k];
				} 
			}
			if (curTop > 0) {
				topWords.add(commonWords.get(curTop));
				topSimilarities[curTop] = 0.0; // make that 0
			}
		}
		return topWords;
	}
	
	/**
	 * Required per the assignment instructions purely for display purposes
	 * - This method takes an ArrayList and returns a String
	 * which when printed will have the list elements with a number in front of them
	 * @param list - the words that must be printed as options for the user
	 * @return - the string that must be printed
	 */
	public String prettyPrint(ArrayList<String> list) {
		StringBuilder printReady = new StringBuilder();
		if (list.size()>0) {
			for(int i =0; i<list.size(); i++) {
				printReady.append(i+1);
				printReady.append(".");
				printReady.append(list.get(i));
				printReady.append("\n");
			}
		} else {
			printReady.append("No suggestions available from the dictionary");
		}
		return printReady.toString();
	}

	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Open the file and copy the words into an arraylist that can be used
	 * across the class
	 * @param fileName - name of the input file as specified by the user
	 * @return the arraylist that consists of the words from the file
	 */
	public ArrayList<String> getSpellCheckThisFile(String fileName) {
		File f = new File(fileName);
		String tempWord;
		try {
			Scanner input = new Scanner(f);
			while (input.hasNext()) {
				tempWord = input.next();
				spellCheckThisFile.add(tempWord.toLowerCase());
			}
			input.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spellCheckThisFile;
	}


	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * - the number of letters that match up between word1 and word2 
	 * as we go from left to right and compare character by character
	 * @param word1
	 * @param word2
	 * @return the left similarity count
	 */
	public double leftSimilarity (String word1, String word2) {
		double count = 0;
		int lengthToCount = 0;
		if (word1.length() <= word2.length()) {
			lengthToCount = word1.length();
		} else 
		{
			lengthToCount = word2.length();
		}
		for (int i=0; i<lengthToCount; i++ )  {
			if (word1.charAt(i) == word2.charAt(i)) {
				count = count + 1;
			}
		}
		return count;
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * the number of letters that match up, but this time going from right to left
	 * @param word1
	 * @param word2
	 * @return the right similarity count
	 */
	public double rightSimilarity (String word1, String word2) {
		double count = 0;
		int lengthToCount = word1.length();
		if (word1.length() < word2.length()) {
			word2 = word2.substring (word2.length()- word1.length(), word2.length());
			lengthToCount = word1.length();
		} else if (word1.length() > word2.length()) {
			word1 = word1.substring (word1.length()- word2.length(), word1.length());
			lengthToCount = word2.length();
		}
		for (int i=lengthToCount-1; i>=0; i-- )  {
			if (word1.charAt(i) == word2.charAt(i)) {
				count = count + 1;
			}
		}
		return count;
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Generates a distinct word by eliminating duplicate letters in a word
	 * @param word - original
	 * @return distinct word
	 */
	public String getDistinctWord(String word) {
		StringBuffer sb = new StringBuffer(word);
		for (int i=0; i<sb.length(); i++) {
			for (int j=i+1; j<sb.length(); j++) {
				if (sb.charAt(i) == sb.charAt(j)) {
					sb.deleteCharAt(j);
					j--;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Generates a list of words from the dictionary that match the input word
	 * based on the variance from the word length. For example, if the original word
	 * is 5 letters long, and the variance accepted is +/- 2, then the selected words
	 * can be 3-7 letters in length
	 * @param word
	 * @param n
	 * @return
	 */
	public ArrayList<String> wordLengthMatches(String word, int n) {
		ArrayList<String> matchingWords = new ArrayList<String>();
		int lowSizeLimit = word.length() - n;
		int upSizeLimit = word.length() + n;
		for (int i=0; i < dictionary.length; i++) {
			if (dictionary[i].length() >= lowSizeLimit && dictionary[i].length() <= upSizeLimit ) {
				matchingWords.add(dictionary[i]);
			}
		}
		return matchingWords;
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Compares the input word with each word in the dictionary, and returns true of 
	 * it finds a match.
	 * @param word
	 * @return
	 */
	public boolean compareWithDictionary(String word) {
		for(int i=0; i < dictionary.length; i++) {
			if (word.contentEquals(dictionary[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Simple method to inform the user that the input word from the file
	 * is misspelled
	 * @param word
	 */
	public void tellUser(String word) {
		System.out.println("The word ‘" + word + "’ is misspelled.");
		System.out.println("The following suggestions are available");
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * When a word is misspelled, then the user is offered options to either
	 * replace it with the suggestions we offer, or leave it in the file as is, or 
	 * allow the user to input an alternative
	 * @param wordFromFile
	 * @param finalSet
	 * @return
	 */
	public String giveOptionsToUser(String wordFromFile, ArrayList<String> finalSet) {
		String wordToWrite = wordFromFile;
		boolean keepGoing = true;
		if (finalSet.size() == 0) {
			System.out.println("There are 0 suggestions in our dictionary for the word " + wordFromFile);
			System.out.println("Press ‘a’ for accept as is, ‘t’ for type in manually.");
		} else {
			System.out.println("Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.");
		}
		Scanner option = new Scanner(System.in);
		String userSelect = null;
		userSelect = option.next();
		while(keepGoing) {
			switch (userSelect) {
			case "a":
				keepGoing = false;
				break;
			case "r":
				System.out.println("Your word will now be replaced with one of the suggestions");
				System.out.println("Enter the number corresponding to the word that you want to use for replacement.");
				userSelect = option.next();
				if (userSelect.contains("1") && finalSet.size() > 0 ) {
					wordToWrite = finalSet.get(0);
					keepGoing = false;
				} else if (userSelect.contains("2") && finalSet.size() > 1) {
					wordToWrite = finalSet.get(1);
					keepGoing = false;
				} else if (userSelect.contains("3") && finalSet.size() > 2) {
					wordToWrite = finalSet.get(2);
					keepGoing = false;
				} else {
					System.out.println("Please enter the right choice. Let's try from the top");
					System.out.println("Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.");
					keepGoing = true;
				}
				break;
			case "t":
					System.out.println("Please type the word that will be used as the replacement in the output file.");
					wordToWrite = option.next();
					keepGoing = false;
					break;
			default:
				System.out.println("Please enter the right choice. Let's try again");
				if (finalSet.size() == 0) {
					System.out.println("Press ‘a’ for accept as is, ‘t’ for type in manually.");
				} else {
					System.out.println("Press ‘r’ for replace, ‘a’ for accept as is, ‘t’ for type in manually.");
				}
				userSelect = option.next();
				keepGoing = true;
				break;
			}
		}
		
		return wordToWrite;
	}
	
	/**
	 * ------Mamatha's own method, not the requirement of assignment-------
	 * Based on the input filename, add a suffix '_chk' to the filename before
	 * the file extension. If the filename is test.txt, then the output filename is
	 * test_chk.txt
	 * @param inputFileName
	 * @return
	 */
	public String createOutputFileName(String inputFileName) {
		StringBuilder sb = new StringBuilder();
		int i = inputFileName.lastIndexOf('.');
		if (i>0) {
			sb.append(inputFileName.substring(0,i));
			sb.append("_chk");
			sb.append(inputFileName.substring(i));
		} else {
			sb.append(inputFileName);
			sb.append("_chk");
		}
		return sb.toString();
	}

	/**
	 * The main method of the class
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> finalSetOfWords = new ArrayList<String>();
		WordRecommender2 WR = new WordRecommender2("engDictionary.txt");
		System.out.println("Provide the name of the file that must be spell checked");
		Scanner fileName = new Scanner(System.in);
		String spellCheckThisFileName = fileName.nextLine();
		String outputFileName = WR.createOutputFileName(spellCheckThisFileName);
		//fileName.close();
		System.out.println("OK. Let's spell check " +  spellCheckThisFileName);
		WR.getSpellCheckThisFile(spellCheckThisFileName);
		FileWriter fw;
		try {
			fw = new FileWriter(outputFileName, true);
			PrintWriter pw = new PrintWriter(fw);
			for (String eachWord : WR.spellCheckThisFile) {
				String writeThisWord = eachWord;
				if (!WR.compareWithDictionary(eachWord)) {
					WR.tellUser(eachWord);
					finalSetOfWords = WR.getWordSuggestions(eachWord, 1, 0.85, 3);
					if (WR.wordRecAvailable) {
						System.out.println(WR.prettyPrint(finalSetOfWords));
					} 
					
					writeThisWord = WR.giveOptionsToUser(eachWord, finalSetOfWords);
				}
					pw.print(writeThisWord + " ");
					
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileName.close();
		System.out.println("The spellchecked content is now available in the file " + outputFileName);
	}

}
