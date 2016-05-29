package Analises;

import java.io.IOException;

public class Start {
	public static void main(String[] agrs) throws IOException {
		String url = new String("123.txt");
		GrammerAnalises ga = new GrammerAnalises(url);
		Tree tree = ga.getTree();
		for (int i = 0; i < tree.root.size(); i++) {
			Node p=tree.root.get(i);
			while(p.getChild()!=null){
				p=p.getChild();
				if(p.getCoda()==true){
					System.out.println(p.getTag());
				}
			}
			
		}
	}
}
