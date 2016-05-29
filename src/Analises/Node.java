package Analises;

public class Node {
		private String tag;
		private boolean coda;
		private Node child;

		public Node(String t, boolean i) {
			tag = t;
			coda = i;
		}
		public String getTag(){return tag;}
		public boolean getCoda(){return coda;}
		public Node getChild(){return child;}
		public void setChild(Node newChild){child=newChild;}
}
