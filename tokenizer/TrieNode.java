import java.util.ArrayList;
import java.util.Arrays;

public class TrieNode {

	public final char character;
	private final ArrayList<TrieNode> children;
	private static final TrieNode endNode = new TrieNode('\0');

	public TrieNode(String... text) {
		this('\0');
		Arrays.stream(text).forEach(this::insert);
	}

	private TrieNode(char character) {
		this.character = character;
		this.children = new ArrayList<>();
	}

	private void insert(String string) {
		TrieNode currentNode = this;
		for(int i = 0; i < string.length(); i++) {
			TrieNode nextNode = currentNode.getNode(string.charAt(i));
			if(nextNode == null) {
				nextNode = new TrieNode(string.charAt(i));
				currentNode.children.add(nextNode);
			}
			currentNode = nextNode;
		}
		currentNode.children.add(endNode);
	}

	public TrieNode getNode(char character) {
		for(TrieNode child: children){
			if(child.character == character){
				return child;
			}
		}
		return null;
	}
}
