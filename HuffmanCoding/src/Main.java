import java.util.PriorityQueue;
import java.nio.ByteBuffer;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


public class Main {

	public static class Node implements Comparable<Node> {
		public char character;
		public int frequency;

		public Node leftChild;
		public Node rightChild;

		public Node(char character, int frequency) {
			this.character = character;
			this.frequency = frequency;
		}

		@Override
		public int compareTo(Node other) {
			return this.frequency - other.frequency;
		}

		@Override
		public String toString() {
			if (isIntermediate) {
				return "Intermediate Node, Frequency: " + frequency;
			} else {
				return "Character: " + character + ", Frequency: " + frequency;
			}
		}

		@Override
		public boolean equals(Object other) {
			Node other2 = (Node) other;
			if (other2.frequency == this.frequency && other2.character == this.character) {
				return true;
			}
			return false;
		}

		public boolean isIntermediate() {
			return !leftChild == null && rightChild == null;
		}

		public Node[] listNodes() {
			return new Node[] { leftChild, rightChild };
		}
	}

	public static Node buildTree(PriorityQueue<Node> queue) {
		while (queue.size() > 1) {
			Node first = queue.poll();
			Node second = queue.poll();
			Node newNode = new Node('\0', first.frequency + second.frequency, true);
			newNode.leftChild = first;
			newNode.rightChild = second;
			queue.offer(newNode);
		}
		return queue.poll();
	}

	public static ByteBuffer getByteBuffer(Main.Node root, String text) {
		ArrayList<Byte> bytes = new ArrayList<>();
		HashMap<Character, String> representation = buildRepresentation(Main.Node root);
	}

	public static HashMap<Character, String> buildRepresentation(Main.Node root,HashMap<Character, String> map) {

	}

	public static PriorityQueue<Node> getQueue(String text) {
		HashMap<Character, Node> nodes = new HashMap<>();
		for (int i = 0; i < text.length(); i++) {
			char currentCharacter = text.charAt(i);
			Node currentNode = nodes.getOrDefault(currentCharacter, new Node(currentCharacter, 0));
			currentNode.frequency++;
			nodes.putIfAbsent(currentCharacter, currentNode);
		}
		return new PriorityQueue<>(nodes.values());
	}

	public static StringBuilder buildTreeString(int depth, StringBuilder indent, StringBuilder dirTree, Main.Node startingNode, int limit, boolean isTail) {
		dirTree.append(indent.append((isTail ?  "└── " : "├── " )).append(startingNode).append("\n"));
		indent.setLength(indent.length() - (startingNode.toString().length() + 5));

		if (!startingNode.isIntermediate()) return dirTree;

		if (depth < limit) {
			Main.Node[] nodes = startingNode.listNodes();
			indent.append((isTail ?  "    "  : "│   "));
			for (int i = 0; i < nodes.length; i++) {
				buildTreeString(depth + 1, indent, dirTree, nodes[i], limit);
			}
			indent.setLength(indent.length() - 4);
		}
		return dirTree;
	}

	public static void main(String[] args) throws Exception {
		PriorityQueue<Node> queue = getQueue(Files.readAllLines(Paths.get(args[0])).stream().collect(Collectors.joining("\n")));
		System.out.println(buildTreeString(1000, new StringBuilder(), new StringBuilder(), buildTree(queue), 10000, true));
	}
}
