package Analises;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * �ʷ�������
 * 
 * @author Dreamtale �����Ԫ��������뵥�� ��token,syn��
 */
class WordAnalises {
	/** �ؼ����ֱ��� */
	public static final int _SYN_PROGRAM = 1, _SYN_VAR = 2, _SYN_INTEGER = 3, _SYN_BEGIN = 4, _SYN_END = 5, _SYN_IF = 6,
			_SYN_THEN = 7, _SYN_ELSE = 8, _SYN_WHILE = 9, _SYN_DO = 10, _SYN_REAL = 11, _SYN_ARRAY = 12,
			_SYN_PROCDURE = 13, _SYN_FUNCTION = 14, _SYN_READ = 15, _SYN_WRITE = 16, _SYN_DIV = 17, _SYN_MOD = 18;
	/** ��ʶ�� */
	public static int _SYN_ID = 101;
	/** ���� */
	public static int _SYN_NUM = 201;
	/** �����ֱ��� */
	public static final int _SYN_ASSIG = 21; // :=
	public static final int _SYN_PLUS = 22; // +
	public static final int _SYN_MINUS = 23; // -
	public static final int _SYN_TIMES = 24; // *
	public static final int _SYN_DIVIDE = 25; // /
	public static final int _SYN_LPAREN = 26; // (
	public static final int _SYN_RPAREN = 27; // )
	public static final int _SYN_LEFTBRACKET1 = 28; // [
	public static final int _SYN_RIGHTBRACKET1 = 29;// ]
	public static final int _SYN_LEFTBRACKET2 = 30; // {
	public static final int _SYN_RIGHTBRACKET2 = 31;// }
	public static final int _SYN_COMMA = 32; // ,
	public static final int _SYN_COLON = 33; // :
	public static final int _SYN_SEMICOLON = 34; // ;
	public static final int _SYN_LG = 35; // >
	public static final int _SYN_LT = 36; // <
	public static final int _SYN_ME = 37; // >=
	public static final int _SYN_LE = 38; // <=
	public static final int _SYN_EQ = 39; // =
	public static final int _SYN_NE = 40; // !=
	public static final int _SYN_ERROR = -1; // error
	public static final int MAXLENGTH = 255; // һ��������ַ�����

	// �ʷ��洢��Ԫ��
	class Twotuples {
		int syn;
		String str;
	}

	// ��Ԫ��洢����
	private List<Twotuples> twotuplesList;
	// �ؼ�������
	private String[] keyWord;
	// �����ȡ������
	private StringBuffer stringbuffer;
	// �����ȡλ��
	private int i;
	private Twotuples tt;

	/**
	 * ���췽��
	 * 
	 * @throws IOException:io�쳣
	 */
	public WordAnalises(String url) throws IOException {
		twotuplesList = new ArrayList<Twotuples>();
		keyWord = new String[] { "program", "var", "integer", "begin", "end", "if", "then", "else", "while", "do",
				"real", "array", "procedure", "function", "read", "write", "div", "mod" };
		stringbuffer = new StringBuffer();
		i = 0;
		try {
			readFile(url);
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ�ܣ�");
		}
		scan();
	}

	/**
	 * ��ȡ�ı�
	 * 
	 * @param url�������ȡλ��
	 * @throws IOException
	 *             :io�쳣
	 */
	private void readFile(String url) throws IOException {
		int ch = 0;
		FileReader fr = new FileReader(url);
		while ((ch = fr.read()) != -1) {
			stringbuffer.append((char) ch);
		}
		stringbuffer.append((char)'#');
		fr.close();
	}

	/**
	 * �ж��Ƿ����ַ�
	 * 
	 * @param ch:��ȡ�ַ�
	 * @return �ж��Ƿ�����ĸ�����»��ߣ��Ƿ���true�����򷵻�false
	 */
	private boolean isChar(char ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_')
			return true;
		else
			return false;
	}

	/**
	 * �ж��Ƿ���Сд
	 * 
	 * @param ch
	 * @return �����Сд����true�����򷵻�false
	 */
	private boolean isLetter(char ch) { // Сд�ж�
		if (ch >= 'a' && ch <= 'z' || ch == '_')
			return true;
		else
			return false;
	}

	/**
	 * �ж��Ƿ�������
	 * 
	 * @param ch
	 * @return ��������ַ���true�����򷵻�flase
	 */
	private boolean isDigit(char ch) { // �����ж�
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	/**
	 * ɨ���ı�
	 */
	void scan() {
		int len;
		for (i = 0, len = stringbuffer.length(); i < len; i++) {
			char recentChar = stringbuffer.charAt(i);
			if (recentChar == ' ' || recentChar == '\t' || recentChar == '\n' || recentChar == '\r') {
				continue;
			}
			if (recentChar == '{') {// ע���ж�
				int k = i + 1;
				while (stringbuffer.charAt(k) != '}')
					k++;
				i = k + 1;
				continue;
			}
			if (isChar(recentChar)) { // �ж���ĸ�»���
				if (isLetter(recentChar)) { // Сд���ؼ���ƥ��
					if (!keywordTuples()) {
						identifierTuples();
					}
				} else {
					identifierTuples();
				}
			} else {
				if (isDigit(recentChar)) {// �ж�����
					numTuples();
				} else { // �ж��ַ�
					signTuples(recentChar);
				}
			}
		}
	}

	/**
	 * �����Ƿ��ǹؼ���
	 * 
	 * @return ����Ƿ���true��������Ƿ���false
	 */
	private boolean keywordTuples() {
		for (int j = 0, keyword = keyWord.length; j < keyword; j++) {
			if (i + keyWord[j].length() < stringbuffer.length()) {
				if (keyWord[j].equals(stringbuffer.substring(i, i + keyWord[j].length()))) {
					tt = new Twotuples();
					tt.syn = j + 1;
					tt.str = keyWord[j];
					twotuplesList.add(tt);
					i = i + keyWord[j].length() - 1;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * �����Ƿ��Ǳ�ʶ��
	 * 
	 * @return ����Ƿ���true��������Ƿ���false
	 */
	private void identifierTuples() {
		String temp = new String();
		while (isChar(stringbuffer.charAt(i))) {
			temp += stringbuffer.charAt(i++);
		}
		for (int j = 0, k = twotuplesList.size(); j < k; j++) {
			tt = twotuplesList.get(j);
			if (tt.syn < 101 | tt.syn > 200)
				continue;
			if (tt.str.equals(temp)) {
				int tempsyn = tt.syn;
				tt = new Twotuples();
				tt.syn = tempsyn;
				tt.str = temp;
				twotuplesList.add(tt);
				i--;
				return;
			}
		}
		tt = new Twotuples();
		tt.syn = _SYN_ID++;
		tt.str = temp;
		twotuplesList.add(tt);
		i--;
	}

	/**
	 * �����Ƿ��Ƿ���
	 * 
	 * @return ����Ƿ���true��������Ƿ���false
	 */
	private void signTuples(char ch) {
		switch (ch) {
		case ':':
			tt = new Twotuples();
			if (stringbuffer.charAt(i + 1) == '=') {
				tt.syn = _SYN_ASSIG;
				tt.str = ":=";
				twotuplesList.add(tt);
				i++;
			} else {
				tt.syn = _SYN_COLON;
				tt.str = ":";
				twotuplesList.add(tt);
			}
			break;
		case '+':
			tt = new Twotuples();
			tt.syn = _SYN_PLUS;
			tt.str = "+";
			twotuplesList.add(tt);
			break;
		case '-':
			tt = new Twotuples();
			tt.syn = _SYN_MINUS;
			tt.str = "-";
			twotuplesList.add(tt);
			break;
		case '*':
			tt = new Twotuples();
			tt.syn = _SYN_TIMES;
			tt.str = "*";
			twotuplesList.add(tt);
			break;
		case '/':
			tt = new Twotuples();
			tt.syn = _SYN_DIVIDE;
			tt.str = "/";
			twotuplesList.add(tt);
			break;
		case '(':
			tt = new Twotuples();
			tt.syn = _SYN_LPAREN;
			tt.str = "(";
			twotuplesList.add(tt);
			break;
		case ')':
			tt = new Twotuples();
			tt.syn = _SYN_RPAREN;
			tt.str = ")";
			twotuplesList.add(tt);
			break;
		case '[':
			tt = new Twotuples();
			tt.syn = _SYN_LEFTBRACKET1;
			tt.str = "[";
			twotuplesList.add(tt);
			break;
		case ']':
			tt = new Twotuples();
			tt.syn = _SYN_RIGHTBRACKET1;
			tt.str = "]";
			twotuplesList.add(tt);
			break;
		case ',':
			tt = new Twotuples();
			tt.syn = _SYN_COMMA;
			tt.str = ",";
			twotuplesList.add(tt);
			break;
		case ';':
			tt = new Twotuples();
			tt.syn = _SYN_SEMICOLON;
			tt.str = ";";
			twotuplesList.add(tt);
			break;
		case '>':
			tt = new Twotuples();
			if (stringbuffer.charAt(i + 1) == '=') {
				tt.syn = _SYN_ME;
				tt.str = ">=";
				twotuplesList.add(tt);
				i++;
			} else {
				tt.syn = _SYN_LG;
				tt.str = ">";
				twotuplesList.add(tt);
			}
			break;
		case '<':
			tt = new Twotuples();
			if (stringbuffer.charAt(i + 1) == '=') {
				tt.syn = _SYN_LE;
				tt.str = "<=";
				twotuplesList.add(tt);
				i++;
			} else {
				tt.syn = _SYN_LG;
				tt.str = "<";
				twotuplesList.add(tt);
			}
			break;
		case '=':
			tt = new Twotuples();
			tt.syn = _SYN_EQ;
			tt.str = "=";
			twotuplesList.add(tt);
			break;
		case '#':
			tt=new Twotuples();
			tt.syn=WordAnalises.MAXLENGTH;
			tt.str="#";
			twotuplesList.add(tt);
		default:
			break;
		}
	}

	/**
	 * �����Ƿ�������
	 * 
	 * @return ����Ƿ���true��������Ƿ���false
	 */
	private void numTuples() {
		String temp = new String();
		while (isDigit(stringbuffer.charAt(i))) {
			temp += stringbuffer.charAt(i++);
		}
		tt = new Twotuples();
		tt.syn = _SYN_NUM++;
		tt.str = temp;
		twotuplesList.add(tt);
		i--;
	}

	/**
	 * ���شʷ���Ԫ�鼯��
	 * 
	 * @return ���شʷ���Ԫ�鼯��
	 */
	List<Twotuples> getTwotuples() {
		return twotuplesList;
	}
}