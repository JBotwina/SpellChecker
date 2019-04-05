import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		System.out.println("Enter the file you wish to spell check.");
		Scanner s = new Scanner(System.in);
		String file = s.nextLine();
		Interface spellCheck = new Interface(file);
		spellCheck.runProgram();
	}
}
