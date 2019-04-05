
import java.io.*;
import java.util.*;

public class WordRecommender {
	private String filename;
	private ArrayList<String> words;
	
	//constructor 
	public WordRecommender(String file) {
		//take in the dictionary file
		filename = file;
		//initialize words variable to an empty arraylist.
		words = new ArrayList<String>();
		//read in file to the words variable every time the WordRecommender class in instantiated. 
		readFile();
		
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
	
	//loop through a list of words and place the words in the words variable.
	private void readFile() {
		
		File inputFile = new File(filename);
		
		try {
			Scanner s = new Scanner(inputFile);
			
			while(s.hasNextLine()) {
				String line = s.nextLine();
				words.add(line);
			}
			s.close();
			} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			}
	}
	
	//returns a list of words with n common letters
	public ArrayList<String> getWordsWithCommonLetters(String word, ArrayList<String> listOfWords, int n){
		//make target word distinct characters
		ArrayList<Character> textWord = makeDistinct(word);
		ArrayList<String> wordsWithCommonLetters = new ArrayList<String>();
		
		for(String dictWord : listOfWords) {
			int counter = 0;
			//make dictionary word distinct characters
			ArrayList<Character> distinctDictWord = makeDistinct(dictWord);
			//if character is in the target word, then increment counter
			for(char letter : distinctDictWord) {
				if(textWord.contains(letter)) {
					counter++;
				}
			}
			//check if the dictionary word's counter reaches the threshold we want.
			if(counter >= n) {
				wordsWithCommonLetters.add(dictWord);
			}
		}
		return wordsWithCommonLetters;
	}
	
	//get similarity from front to back and back to front
	public double getSimilarityMetric(String word1, String word2) {
		//the loop will always be limited by the shortest word, declare which word is longer and shorter.
		String longerWord= "";
		String shorterWord= "";
		if(word1.length()>= word2.length()) {
			longerWord = word1;
			shorterWord = word2;
		}else {
			longerWord = word2;
			shorterWord = word1;
		}
		char[] shortAsArray = shorterWord.toCharArray();
		char[] longAsArray = longerWord.toCharArray();
		//loop through from left to right
		double leftSimilarity=0.0;
		for(int i = 0; i< shorterWord.length(); i++) {
			if(shortAsArray[i] == longAsArray[i]) {
				leftSimilarity++;
			}
		}
		//get the differences in length so that we can loop through the longest and shortest word in accurately until the
		//end of the shorter word is reached.
		int difference = longAsArray.length - shortAsArray.length;
		
		//loop through from right to left
		double rightSimilarity= 0.0;
		for(int m = shorterWord.length()-1; m>=0; m--) {
			if(shortAsArray[m] == longAsArray[m+difference]) {
				rightSimilarity++;
			}
		}
		double simMetric = (leftSimilarity +rightSimilarity)/2;
		
		
		return simMetric;
	}
	
	//narrow the dictionary list to the topN words of highest similarity.
	public ArrayList<String> getWordSuggestions(String word, int n, double commonPercent, int topN){
		
		//narrow dictionary to words with common letters
		ArrayList<String> commonWords = getWordsWithCommonLetters(word, words, 2);
		//narrow words with common letters to those of a specific range
		ArrayList<String> commonWordsInRange = new ArrayList<String>();
		for(String commonWord: commonWords) {
			if( (commonWord.length() >= word.length()-n) && (commonWord.length() <= word.length()+n) ) {
				commonWordsInRange.add(commonWord);
			}
		}
		//find the percent of distinct letters in common between word and words in common range
		ArrayList<Character> distinctLettersOfWord = makeDistinct(word);
		double totalDistinctLetters = distinctLettersOfWord.size();
		
		ArrayList<String> wordsOfCommonPercent = new ArrayList<String>();
		
		//narrow the list to those that meet the threshold of letters in common
		for(String wordInRange: commonWordsInRange) {
			double counter = 0.0;
			ArrayList<Character> distinctLettersOfWordInRange = makeDistinct(wordInRange);
			for(char distinctLetter : distinctLettersOfWordInRange) {
				if(distinctLettersOfWord.contains(distinctLetter)) {
					counter++;
				}
			}
			//compare common percents
			double commonPercentOfDistinctLetters = counter/totalDistinctLetters;
			if(commonPercentOfDistinctLetters >= commonPercent) {
				wordsOfCommonPercent.add(wordInRange);
			}
		}
		//create an arraylist of object that contain the word and the similarity metric.
		ArrayList<WordWithMetric> wordsWithMetric = new ArrayList<WordWithMetric>();
		for(String wordOfCommonPercent : wordsOfCommonPercent) {
			WordWithMetric commonPercentwithMetric = new WordWithMetric(wordOfCommonPercent, getSimilarityMetric(word, wordOfCommonPercent));
			wordsWithMetric.add(commonPercentwithMetric);
		}
		//sort the words with metrics from greatest to least similarity metric
		sortList(wordsWithMetric);
		
		// make new arrayList that only has the top-n words with metric
		ArrayList<String> topNWords = new ArrayList<String>();
		//check for the edge case where there are less generated suggestions than the user wants to be generated.
		//if there are zero suggestions, that is handled by the Interface class.
		if(wordsWithMetric.size()< topN) {
			for(WordWithMetric finalist: wordsWithMetric) {
				topNWords.add(finalist.getWord());
			}
			return topNWords;
		}
		
		for(int m = 0; m<topN; m++) {
			topNWords.add(wordsWithMetric.get(m).getWord());
		}
		
		
		return topNWords;
	}
	
	// implement selection sort algorithm, focusing on max, not minimum.
	public void sortList(ArrayList<WordWithMetric> wordList) {
		for(int i =0; i< wordList.size()-1; i++) {
			int max_idx = i;
			for(int j = i+1; j < wordList.size(); j++) {
				if(wordList.get(j).getSimilarityMetric() > wordList.get(max_idx).getSimilarityMetric()) {
					max_idx = j;
				}
			WordWithMetric temp = wordList.get(max_idx);
			wordList.set(max_idx, wordList.get(i));
			wordList.set(i, temp);
			}
		}	
	}
	//return enumerated string of choices
	public String prettyPrint(ArrayList<String> list) {
		String choices = "";
		for(int i =0; i<list.size(); i++) {
			choices += Integer.toString(i+1) +". " + list.get(i) + "\n";
		}
		return choices;
	}
	
	
	
	/**
	 * 
	 * @param word
	 * @return ArrayList<Character> of distinct letters of the word
	 */
	public ArrayList<Character> makeDistinct(String word){
		char[] letters =  word.toCharArray();
		ArrayList<Character> distinctLetters = new ArrayList<Character>();
		for(char letter : letters) {
			if(!(distinctLetters.contains(letter))) {
				distinctLetters.add(letter);
			}
		}
		
		return distinctLetters;
		
	}
}




















