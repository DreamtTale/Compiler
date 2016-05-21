package Analises;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析器
 * 
 * @author Dreamtale 输出二元组来存代码单词 （token,syn）
 */
class WordAnalises {
	/** 关键字种别码 */
	public static final int _SYN_PROGRAM = 1, _SYN_VAR = 2, _SYN_INTEGER = 3, _SYN_BEGIN = 4, _SYN_END = 5, _SYN_IF = 6,
			_SYN_THEN = 7, _SYN_ELSE = 8, _SYN_WHILE = 9, _SYN_DO = 10, _SYN_REAL = 11, _SYN_ARRAY = 12,
			_SYN_PROCDURE = 13, _SYN_FUNCTION = 14, _SYN_READ = 15, _SYN_WRITE = 16, _SYN_DIV = 17, _SYN_MOD = 18;
	/** 标识符 */
	public static int _SYN_ID = 101;
	/** 数字 */
	public static int _SYN_NUM = 201;
	/** 符号种别码 */
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
	public static final int MAXLENGTH = 255; // 一行允许的字符个数

	// 词法存储二元组
	class Twotuples {
		int syn;
		String str;
	}

	// 二元组存储集合
	private List<Twotuples> twotuplesList;
	// 关键字数组
	private String[] keyWord;
	// 代码读取缓冲区
	private StringBuffer stringbuffer;
	// 代码读取位置
	private int i;
	private Twotuples tt;

	/**
	 * 构造方法
	 * 
	 * @throws IOException:io异常
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
			System.out.println("读取文件失败！");
		}
		scan();
	}

	/**
	 * 读取文本
	 * 
	 * @param url：代码读取位置
	 * @throws IOException
	 *             :io异常
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
	 * 判断是否是字符
	 * 
	 * @param ch:读取字符
	 * @return 判断是否是字母或者下划线，是返回true，否则返回false
	 */
	private boolean isChar(char ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_')
			return true;
		else
			return false;
	}

	/**
	 * 判断是否是小写
	 * 
	 * @param ch
	 * @return 如果是小写返回true，否则返回false
	 */
	private boolean isLetter(char ch) { // 小写判断
		if (ch >= 'a' && ch <= 'z' || ch == '_')
			return true;
		else
			return false;
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param ch
	 * @return 如果是数字返回true，否则返回flase
	 */
	private boolean isDigit(char ch) { // 数字判断
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	/**
	 * 扫描文本
	 */
	void scan() {
		int len;
		for (i = 0, len = stringbuffer.length(); i < len; i++) {
			char recentChar = stringbuffer.charAt(i);
			if (recentChar == ' ' || recentChar == '\t' || recentChar == '\n' || recentChar == '\r') {
				continue;
			}
			if (recentChar == '{') {// 注释判断
				int k = i + 1;
				while (stringbuffer.charAt(k) != '}')
					k++;
				i = k + 1;
				continue;
			}
			if (isChar(recentChar)) { // 判断字母下划线
				if (isLetter(recentChar)) { // 小写，关键字匹配
					if (!keywordTuples()) {
						identifierTuples();
					}
				} else {
					identifierTuples();
				}
			} else {
				if (isDigit(recentChar)) {// 判断数字
					numTuples();
				} else { // 判断字符
					signTuples(recentChar);
				}
			}
		}
	}

	/**
	 * 分析是否是关键字
	 * 
	 * @return 如果是返回true，如果不是返回false
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
	 * 分析是否是标识符
	 * 
	 * @return 如果是返回true，如果不是返回false
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
	 * 分析是否是符号
	 * 
	 * @return 如果是返回true，如果不是返回false
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
	 * 分析是否是数字
	 * 
	 * @return 如果是返回true，如果不是返回false
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
	 * 返回词法二元组集合
	 * 
	 * @return 返回词法二元组集合
	 */
	List<Twotuples> getTwotuples() {
		return twotuplesList;
	}
}