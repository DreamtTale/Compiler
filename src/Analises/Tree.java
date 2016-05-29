package Analises;

import java.util.*;

public class Tree {
	Node gen;
	List<Node> root;
	
	Tree(Node tRoot) {
		root=new ArrayList<Node>();
		gen=tRoot;
		root.add(gen);
	}
	public void addChild(Node parent,Node child){
		if(child.getCoda()==false){
			root.add(new Node(child.getTag(),false));
		}
		Node p=parent;
		while(p.getChild()!=null){
			p=p.getChild();
		}
		p.setChild(child);
	}
}
