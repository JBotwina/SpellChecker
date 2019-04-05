
//class that is used to hold a word and its similarity in comparison to another word
public class WordWithMetric {
	
	private String word;
	private double similarityMetric;
	
	public WordWithMetric(String word, double metric) {
		this.word = word;
		this.similarityMetric = metric;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getSimilarityMetric() {
		return similarityMetric;
	}

	public void setSimilarityMetric(double similarityMetric) {
		this.similarityMetric = similarityMetric;
	}
	
	
}
