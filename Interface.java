
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Interface {
	
	Scanner s = new Scanner(System.in);
	private String file;
	private String checkedFile;
	private WordRecommender iRec = new WordRecommender("engDictionary.txt");
	private File outputFile;
	private PrintWriter pw;
	private FileWriter fw;
	private File inputFile;
	private ArrayList<String> suggestions;
	
	// constructor
	public Interface(String file) {
		this.outputFile = new File(createFileName(file));
		this.inputFile = new File(file);
	}
	
	//run the entire program
	public void runProgram() {
		try {
			//scan input file
			Scanner s1 = new Scanner(inputFile);
			//loop through the input file
			while(s1.hasNextLine()) {
				String line = s1.nextLine();
				// split each line to array
				String[] lineAsArray = line.split(" ");
				//leave acronyms as they are, but turn everything else to lower case.
				for(int i = 0; i<lineAsArray.length; i++) {
					if(checkIfAllCapitalized(lineAsArray[i])) {
						continue;
					}
					else{
					lineAsArray[i] = lineAsArray[i].toLowerCase();
					}
				}
				// if word is found in the dictionary, a, i or an acronym, then print it directly to the output file.
				for(String word: lineAsArray) {
					if( (iRec.getWords().contains(word)) || (word.contentEquals("a")) || (word.contentEquals("i")) || (checkIfAllCapitalized(word)) ) {
						printToFile(outputFile, word);
					}
					//if word is misspelled, implement correction options
					else {
						//get the suggested corrections
						suggestions = iRec.getWordSuggestions(word, 2, .85, 5);
						System.out.println("The word " + word + " is spelled incorrectly!\nThe following suggestions are available: ");
						System.out.println(iRec.prettyPrint(suggestions));
						//if there are no suggestions, force the user to either accept the misspelled word or type another.
						if(suggestions.size() == 0) {
							System.out.println("There are 0 suggestions in our dictionary for this word.\n Press'a' for accept as is or 't' for type in manually");
							String responseWithNoSuggestions = s.nextLine();
							while(!( (responseWithNoSuggestions.contentEquals("a")) || (responseWithNoSuggestions.contentEquals("t"))) ) {
								System.out.println("Please type 'a' or 't'! ");
								responseWithNoSuggestions = s.nextLine();
							}
							if(responseWithNoSuggestions.contentEquals("a")) {
								acceptInput(outputFile, word);
							}
							else {
								typeInput(outputFile, word);
							}
							continue;
						}
						
						System.out.println("Press 'r' for replace, 'a' for accept as is, 't' for type in manually.");
						String response = s.nextLine();
						//catch any inappropriate responses
						while( !( (response.contentEquals("a")) || (response.contentEquals("r")) || (response.contentEquals("t"))) ) {
							System.out.println("Please type an appropriate response");
							response = s.nextLine();
						}
						String userResponse = response;
						//replace word
						if(userResponse.contentEquals("r")) {
							replaceInput(outputFile, word);
						}
						//accept spelling
						else if(userResponse.contentEquals("a")) {
							acceptInput(outputFile, word);
						}
						//type new word
						else {
							typeInput(outputFile, word);
							}
						}
					}
				}
			} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			}
	}
	
	//check if a string is all upper case
	public boolean checkIfAllCapitalized(String word) {
		char[] wordAsLetters =word.toCharArray();
		boolean allCaps = true;
		for(char letter: wordAsLetters) {
			if(Character.isLowerCase(letter)) {
				allCaps = false;
			}
		}
		return allCaps;
	}

	//rename file to signify that it has been checked
	public String createFileName(String file) {
		String firstPart = file.substring(0, file.indexOf("."));
		String lastPart = file.substring(file.indexOf("."), file.length());
		String middlePart = "_chk";
		checkedFile = firstPart + middlePart+ lastPart;
		return checkedFile;
	}
	//when user inputs a r
	public void replaceInput(File outputFile, String word) {
		System.out.println("Your word will now be replaced with one of the suggestions.\nEnter the number corresponding to the word that you want to use for replacement");
		int replacementChoice = s.nextInt();
		//catch responses that are out of range
		while( !( (replacementChoice>=1) && (replacementChoice <= suggestions.size()) ) ) {
			System.out.println("Pick a choice from within the range.");
			replacementChoice = s.nextInt();
		}
		//print to output file
		printToFile(outputFile, suggestions.get(replacementChoice -1));
	}
	
	//when user inputs a
	public void acceptInput(File outputFile, String word) {
		printToFile(outputFile, word);
	}
	//when user inputs t
	public void typeInput(File outputFile, String word) {
		System.out.println("Please type the word that will be used as the replacement in the outfile.");
		String typedWord = s.nextLine();
		printToFile(outputFile, typedWord);
	}
	
	//general function that prints to the output file
	public void printToFile(File outputFile, String word) {
		try {
			fw = new FileWriter(outputFile, true);
			pw = new PrintWriter(fw);
			pw.println(word);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	
	
}
