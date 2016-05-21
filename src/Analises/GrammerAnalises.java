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

	public GrammerAnalises(String url) throws IOException {
		i = 0;
		wa = new WordAnalises(url);
		words = wa.getTwotuples();
		start();
	}

	private void start() {
		program();
		System.out.println("������!");
	}

	/**
	 * �����򡵡��������ײ����������塵
	 */
	private void program() {
		programHeader();
		programBody();
	}

	/**
	 * �������ײ����� program������������
	 */
	private void programHeader() {
		if (words.get(i).syn == WordAnalises._SYN_PROGRAM) {
			i++;
			programName();
			semicolon();
		} else {
			System.out.println("ȱ��program!");
			return;
		}
	}

	/**
	 * �������塵����������������������䡵
	 */
	private void programBody() {
		varDeclaration();
		stmts();
	}

	/**
	 * �������������� var�����������б�|���ա�
	 */
	private void varDeclaration() {
		if (words.get(i).syn == WordAnalises._SYN_VAR) {
			i++;
			varDecList();
		}
	}

	/**
	 * �����������б� �� ���������塵�����������б�|���������塵
	 */
	private void varDecList() {
		varDef();
		if (words.get(i).syn != WordAnalises._SYN_BEGIN) {
			varDecList();
		} else {
		}
	}

	/**
	 * ���������塵�����������б�: <����> ��
	 */
	private void varDef() {
		varList();
		if (words.get(i).syn == WordAnalises._SYN_COLON) {
			i++;
			type();
			semicolon();
		} else {
			System.out.println("�����������");
		}
	}

	/**
	 * <�������б�> �� ����������|����������,���������б�
	 */
	private void varList() {
		varName();
		if (words.get(i).syn == WordAnalises._SYN_COMMA) {
			i++;
			varList();
		}
	}

	/**
	 * <����> �� integer
	 */
	private void type() {
		if (words.get(i).syn == WordAnalises._SYN_INTEGER) {
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
	private void stmts() {
		if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			i++;
		} else {
			System.out.println("ȱ��BEGIN!");
			return;
		}
		block();
		if (words.get(i).syn == WordAnalises._SYN_END) {
			i++;
		} else {
			System.out.println("ȱ��END!");
			return;
		}
	}

	/**
	 * �����页������䡵������䡵; �����页
	 */
	private void block() {
		stmt();
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
			block();
		}
	}

	/**
	 * ����䡵������ֵ��䡵|��������䡵|��ѭ����䡵|��������䡵|���ա�
	 */
	private void stmt() {
		if (words.get(i + 1).syn == WordAnalises._SYN_ASSIG) {
			assinStmt();
		} else if (words.get(i).syn == WordAnalises._SYN_IF) {
			i++;
			ifStmt();
		} else if (words.get(i).syn == WordAnalises._SYN_WHILE) {
			i++;
			loopStmt();
		} else if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			stmts();
		}
	}

	/**
	 * ����ֵ��䡵�����󲿡�:= ���Ҳ���
	 */
	private void assinStmt() {
		left();
		i++;
		right();
	}

	/**
	 * ���󲿡�������������
	 */
	private void left() {
		if (id()) {
			return;
		} else {
			System.out.println("����ı�����!");
		}
	}

	/**
	 * ���Ҳ��������������ʽ��
	 */
	private void right() {
		arithmeticExpr();
	}

	/**
	 * ��������䡵�� if����ϵ���ʽ��then����䡵else����䡵
	 */
	private void ifStmt() {
		relationExpr();
		if (words.get(i).syn == WordAnalises._SYN_THEN) {
			i++;
		} else {
			System.out.println("ȱ��THEN!");
		}
		stmt();
		if (words.get(i).syn == WordAnalises._SYN_ELSE) {
			i++;
		} else {
			System.out.println("ȱ��ELSE!");
		}
		stmt();
	}

	/**
	 * ��ѭ����䡵�� while����ϵ���ʽ��do����䡵
	 */
	private void loopStmt() {
		relationExpr();
		if (words.get(i).syn == WordAnalises._SYN_DO) {
			i++;
		} else {
			System.out.println("ȱ��DO!");
		}
		stmt();
	}

	/**
	 * <��ϵ���ʽ> �����������ʽ������ϵ����������������ʽ��
	 */
	private void relationExpr() {
		arithmeticExpr();
		if (!relationOperator()) {
			System.out.println("����Ĺ�ϵ�����!");
		}
		arithmeticExpr();
	}

	/**
	 * <�������ʽ> �� ���| ���������ʽ����������������
	 */
	private void arithmeticExpr() {
		item();
		if (addOperator()) {
			arithmeticExpr();
		}
	}

	/**
	 * <��> �� �����ӡ�| �������������������ӡ�
	 */
	private void item() {
		factor();
		if (mulOperator()) {
			item();
		}
	}

	/**
	 * �����ӡ���������������(���������ʽ��) ��������
	 */
	private void factor() {
		if (id()) {

		} else if (words.get(i).syn == WordAnalises._SYN_LPAREN) {
			i++;
			arithmeticExpr();
			if (words.get(i).syn == WordAnalises._SYN_RPAREN) {
				i++;
			} else {
				System.out.println("ȱ��������");
			}
		} else if (num()) {
		} else {
			System.out.println("���������!");
		}
	}

	/**
	 * ����������������ʶ����
	 */
	private void programName() {
		if (id()) {
			return;
		} else {
			System.out.println("����ĳ�����");
		}
	}

	/**
	 * ����������������ʶ����
	 */
	private void varName() {
		if (!id()) {
			System.out.println("����������");
		}
	}

	/**
	 * ��ʶ��
	 * 
	 * @return �Ǳ�ʶ��������true�����򷵻�false
	 */
	private boolean id() {
		if (words.get(i).syn > 100 && words.get(i).syn < 201) {
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ƥ��ֺ�
	 */
	private void semicolon() {
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
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
	private boolean num() {
		if (words.get(i).syn > 200 && words.get(i).syn < 301) {
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����ϵ��������� < | <= | �� | >= | > | <>
	 */
	private boolean relationOperator() {
		if (words.get(i).syn >= 35 && words.get(i).syn <= 40) {
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ������������� + | -
	 */
	private boolean addOperator() {
		if (words.get(i).syn == 22 || words.get(i).syn == 23) {
			i++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ������������� * | /
	 */
	private boolean mulOperator() {
		if (words.get(i).syn == 24 || words.get(i).syn == 25) {
			i++;
			return true;
		} else {
			return false;
		}
	}
}
