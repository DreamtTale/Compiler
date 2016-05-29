package Analises;

import java.io.IOException;
import java.util.*;
import Analises.WordAnalises.Twotuples;
import Analises.WordAnalises;

/**
 * �﷨������
 * 
 * @author Dreamtale
 *
 */
public class GrammerAnalises {
	// ���뵥�ʼ���
	private List<Twotuples> words;
	private int i;
	WordAnalises wa;
	Tree graTree;
	Node node, codaNode;

	public GrammerAnalises(String url) throws IOException {
		i = 0;
		wa = new WordAnalises(url);
		words = wa.getTwotuples();
		start();
	}

	public Tree getTree() {
		return graTree;
	}

	private void start() {
		program();
		System.out.println("������!");
	}

	/**
	 * �����򡵡��������ײ����������塵
	 */
	private void program() {
		node = new Node("program", false);
		graTree = new Tree(node);
		programHeader(node);
		programBody(node);
	}

	/**
	 * �������ײ����� program������������
	 */
	private void programHeader(Node parent) {
		node = new Node("programHeader", false);
		graTree.addChild(parent, node);
		if (words.get(i).syn == WordAnalises._SYN_PROGRAM) {
			codaNode = new Node("program", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��program!");
			return;
		}
		programName(node);
		semicolon(node);
	}

	/**
	 * �������塵����������������������䡵
	 */
	private void programBody(Node parent) {
		node = new Node("programBody", false);
		graTree.addChild(parent, node);
		varDeclaration(node);
		stmts(node);
	}

	/**
	 * �������������� var�����������б�|���ա�
	 */
	private void varDeclaration(Node parent) {
		node = new Node("varDeclaration", false);
		graTree.addChild(parent, node);
		if (words.get(i).syn == WordAnalises._SYN_VAR) {
			codaNode = new Node("var", true);
			graTree.addChild(node, codaNode);
			i++;
			varDecList(node);
		}
	}

	/**
	 * �����������б� �� ���������塵�����������б�|���������塵
	 */
	private void varDecList(Node parent) {
		node = new Node("varDecList", false);
		graTree.addChild(parent, node);
		varDef(node);
		if (words.get(i).syn != WordAnalises._SYN_BEGIN) {
			varDecList(node);
		} else {

		}
	}

	/**
	 * ���������塵�����������б�: <����> ��
	 */
	private void varDef(Node parent) {
		node = new Node("varDef", false);
		graTree.addChild(parent, node);
		varList(node);
		if (words.get(i).syn == WordAnalises._SYN_COLON) {
			codaNode = new Node(":", true);
			graTree.addChild(node, codaNode);
			i++;
			type(node);
			semicolon(node);
		} else {
			System.out.println("�����������");
		}
	}

	/**
	 * <�������б�> �� ����������|����������,���������б�
	 */
	private void varList(Node parent) {
		node = new Node("varList", false);
		graTree.addChild(parent, node);
		varName(node);
		if (words.get(i).syn == WordAnalises._SYN_COMMA) {
			codaNode = new Node(",", true);
			graTree.addChild(node, codaNode);
			i++;
			varList(node);
		}
	}

	/**
	 * <����> �� integer
	 */
	private void type(Node parent) {
		if (words.get(i).syn == WordAnalises._SYN_INTEGER) {
			codaNode = new Node("integer", true);
			graTree.addChild(node, codaNode);
			i++;
			return;
		} else {
			System.out.println("��������");
			return;
		}
	}

	/**
	 * ��������䡵�� begin�����页end
	 */
	private void stmts(Node parent) {
		node = new Node("stmts", false);
		graTree.addChild(parent, node);
		if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			codaNode = new Node("begin", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��BEGIN!");
			return;
		}
		block(node);
		if (words.get(i).syn == WordAnalises._SYN_END) {
			codaNode = new Node("end", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��END!");
			return;
		}
	}

	/**
	 * �����页������䡵������䡵; �����页
	 */
	private void block(Node parent) {
		node = new Node("block", false);
		graTree.addChild(parent, node);
		stmt(node);
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			codaNode = new Node(";", true);
			graTree.addChild(node, codaNode);
			i++;
			block(node);
		}
	}

	/**
	 * ����䡵������ֵ��䡵|��������䡵|��ѭ����䡵|��������䡵|���ա�
	 */
	private void stmt(Node parent) {
		node = new Node("stmt", false);
		graTree.addChild(parent, node);
		if (words.get(i + 1).syn == WordAnalises._SYN_ASSIG) {
			assinStmt(node);
		} else if (words.get(i).syn == WordAnalises._SYN_IF) {
			codaNode = new Node("if", true);
			graTree.addChild(node, codaNode);
			i++;
			ifStmt(node);
		} else if (words.get(i).syn == WordAnalises._SYN_WHILE) {
			i++;
			loopStmt(node);
		} else if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			stmts(node);
		}
	}

	/**
	 * ����ֵ��䡵�����󲿡�:= ���Ҳ���
	 */
	private void assinStmt(Node parent) {
		node = new Node("assinStmt", false);
		graTree.addChild(parent, node);
		left(node);
		i++;
		right(node);
	}

	/**
	 * ���󲿡�������������
	 */
	private void left(Node parent) {
		node = new Node("left", false);
		graTree.addChild(parent, node);
		if (id(node)) {
			return;
		} else {
			System.out.println("����ı�����!");
		}
	}

	/**
	 * ���Ҳ��������������ʽ��
	 */
	private void right(Node parent) {
		node = new Node("right", false);
		graTree.addChild(parent, node);
		arithmeticExpr(node);
	}

	/**
	 * ��������䡵�� if����ϵ���ʽ��then����䡵else����䡵
	 */
	private void ifStmt(Node parent) {
		node = new Node("ifStmt", false);
		graTree.addChild(parent, node);
		relationExpr(node);
		if (words.get(i).syn == WordAnalises._SYN_THEN) {
			codaNode = new Node("then", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��THEN!");
		}
		stmt(node);
		if (words.get(i).syn == WordAnalises._SYN_ELSE) {
			codaNode = new Node("else", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��ELSE!");
		}
		stmt(node);
	}

	/**
	 * ��ѭ����䡵�� while����ϵ���ʽ��do����䡵
	 */
	private void loopStmt(Node parent) {
		node = new Node("loopStmt", false);
		graTree.addChild(parent, node);
		relationExpr(node);
		if (words.get(i).syn == WordAnalises._SYN_DO) {
			codaNode = new Node("do", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("ȱ��DO!");
		}
		stmt(node);
	}

	/**
	 * <��ϵ���ʽ> �����������ʽ������ϵ����������������ʽ��
	 */
	private void relationExpr(Node parent) {
		node = new Node("relationExpr", false);
		graTree.addChild(parent, node);
		arithmeticExpr(node);
		if (!relationOperator(node)) {
			System.out.println("����Ĺ�ϵ�����!");
		}
		arithmeticExpr(node);
	}

	/**
	 * <�������ʽ> �� ���| ���������ʽ����������������
	 */
	private void arithmeticExpr(Node parent) {
		node = new Node("arithmeticExpr", false);
		graTree.addChild(parent, node);
		item(node);
		if (addOperator(node)) {
			arithmeticExpr(node);
		}
	}

	/**
	 * <��> �� �����ӡ�| �������������������ӡ�
	 */
	private void item(Node parent) {
		node = new Node("item", false);
		graTree.addChild(parent, node);
		factor(node);
		if (mulOperator(node)) {
			item(node);
		}
	}

	/**
	 * �����ӡ���������������(���������ʽ��) ��������
	 */
	private void factor(Node parent) {
		node = new Node("factor", false);
		graTree.addChild(parent, node);
		if (id(node)) {

		} else if (words.get(i).syn == WordAnalises._SYN_LPAREN) {
			codaNode = new Node("(", true);
			graTree.addChild(node, codaNode);
			i++;
			arithmeticExpr(node);
			if (words.get(i).syn == WordAnalises._SYN_RPAREN) {
				codaNode = new Node(")", true);
				graTree.addChild(node, codaNode);
				i++;
			} else {
				System.out.println("ȱ��������");
			}
		} else if (num(node)) {
		} else {
			System.out.println("���������!");
		}
	}

	/**
	 * ����������������ʶ����
	 */
	private void programName(Node parent) {
		node = new Node("factor", false);
		graTree.addChild(parent, node);
		if (id(node)) {
			return;
		} else {
			System.out.println("����ĳ�����");
		}
	}

	/**
	 * ����������������ʶ����
	 */
	private void varName(Node parent) {
		node = new Node("varName", false);
		graTree.addChild(parent, node);
		if (!id(node)) {
			System.out.println("����������");
		}
	}

	/**
	 * ��ʶ��
	 * 
	 * @return �Ǳ�ʶ��������true�����򷵻�false
	 */
	private boolean id(Node parent) {
		if (words.get(i).syn > 100 && words.get(i).syn < 201) {
			codaNode = new Node("id", true);
			graTree.addChild(parent, codaNode);
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ƥ��ֺ�
	 */
	private void semicolon(Node parent) {
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
			codaNode = new Node(";", true);
			graTree.addChild(parent, codaNode);
			return;
		} else {
			System.out.println("ȱ��';'!");
		}
	}

	/**
	 * ����
	 * 
	 * @return �����ַ���true�����򷵻�false
	 */
	private boolean num(Node parent) {
		if (words.get(i).syn > 200 && words.get(i).syn < 301) {
			codaNode = new Node("num", true);
			graTree.addChild(parent, codaNode);
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����ϵ��������� < | <= | �� | >= | > | <>
	 */
	private boolean relationOperator(Node parent) {
		if (words.get(i).syn >= 35 && words.get(i).syn <= 40) {
			codaNode = new Node("reOp", true);
			graTree.addChild(parent, codaNode);
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ������������� + | -
	 */
	private boolean addOperator(Node parent) {
		if (words.get(i).syn == 22 || words.get(i).syn == 23) {
			codaNode = new Node("+-", true);
			graTree.addChild(parent, codaNode);
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ������������� * | /
	 */
	private boolean mulOperator(Node parent) {
		if (words.get(i).syn == 24 || words.get(i).syn == 25) {
			codaNode = new Node("*/", true);
			graTree.addChild(parent, codaNode);
			i++;
			return true;
		} else {
			return false;
		}
	}
}
