# SpellChecker
This program compares the spelling of each word of a input file with a list of words from the a English dictionary. If the word is not present in the dictionary, the word is assumed to be spelled incorrectly and then the program offers the user:
1) a choice of words he/she can use instead of the misspelled word based on a similarity metric
2) the ability to accept the misspelling
3) the ability to type a new word to be used instead of the misspelled word. 

Any choice that the users elects, will be printed to the output file. If the word is located
in the dictionary, then the word is automatically printed to the output file. 

The design that I chose was to ask the user for the input file from the start and then instantiate an Interface object that scans the input file. The most important program is the Interface.runProgram() method that collects all the helper methods in a series of executions until the file is fully checked. Whenever, I needed to repeat a task, such as printing to the output file or making a word an array of distinct characters, I created a helper method that would execute that task. The runProgram() method acts as a skeleton that calls different functions based on the words it scans. 

If I were to make one suggestion on how to improve this program, I would say that the user should be prompted in the beginning of the programto choose parameters for the getWordSuggestions() method. This would personalize the program to the user's tastes. I would implement this by making a scanner class within the getWordSuggestions() method that asks the user how many correctly spelled suggestions they would want and of what length, and similarity metric.

This project was a great exercise in file i/o and I also learned how to implement the selection sort algorithm from scratch. 
