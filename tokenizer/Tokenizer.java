public class Tokenizer {

	private TrieNode root;
	private String source;
	private int currentChar = 0;

	public Tokenizer(String source, String... delimiter) {
		this.source = source;
		this.root = new TrieNode(delimiter);
	}

	public String next() {
		for(int i = currentChar; i < source.length(); i++) {
			TrieNode current = root;
			int j = i;
			while(j < source.length() && current.getNode(source.charAt(j)) != null) {
				current = current.getNode(source.charAt(j));
				j++;
			}
			if(current.getNode('\0') != null) {
				String val = source.substring(currentChar, i);
				currentChar = j;
				return val;
			}
		}

		return source.substring(currentChar);
	}

	public boolean hasNext() {
		return currentChar < source.length();
	}
}
