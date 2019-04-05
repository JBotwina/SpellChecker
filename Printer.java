

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Printer {
	FileWriter fw;
	PrintWriter pw;
	
	public Printer(File outputFile, String word) {
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
}

