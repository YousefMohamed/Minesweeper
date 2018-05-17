public class Tokenizer {

	private TrieNode root;
	private String source;
	private int currentChar = 0;

	public Tokenizer(String source, String... delimiter) {
		this.source = source;
		this.root = new TrieNode(delimiter);
	}

	public String next() {
		final int lastChar = currentChar;
		int startedmatching = currentChar;

		boolean matching = false;
		TrieNode current = root;

		while(matching || currentChar < source.length()) {
			if(matching) {
				TrieNode nextNode = currentChar < source.length() ? current.getNode(source.charAt(currentChar)) : null;
				if(nextNode == null && current.getNode('\0') != null) {
					matching = false;
					return source.substring(lastChar, startedmatching);
				} else if(nextNode != null) {
					current = nextNode;
				} else {
					matching = false;
				}
			} else {
				TrieNode nextNode = root.getNode(source.charAt(currentChar));
				if(nextNode != null){
					matching = true;
					startedmatching = currentChar;
					current = nextNode;
				}
			}
			currentChar++;
		}
		return source.substring(lastChar);
	}

	public boolean hasNext() {
		return currentChar < source.length();
	}
}
