package Analises;

import java.io.IOException;
import java.util.*;
import Analises.WordAnalises.Twotuples;
import Analises.WordAnalises;

/**
 * 语法分析器
 * 
 * @author Dreamtale
 *
 */
public class GrammerAnalises {
	// 代码单词集合
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
		System.out.println("检查完毕!");
	}

	/**
	 * 〈程序〉→〈程序首部〉〈程序体〉
	 */
	private void program() {
		programHeader();
		programBody();
	}

	/**
	 * 〈程序首部〉→ program〈程序名〉；
	 */
	private void programHeader() {
		if (words.get(i).syn == WordAnalises._SYN_PROGRAM) {
			i++;
			programName();
			semicolon();
		} else {
			System.out.println("缺少program!");
			return;
		}
	}

	/**
	 * 〈程序体〉→〈变量声明〉〈复合语句〉
	 */
	private void programBody() {
		varDeclaration();
		stmts();
	}

	/**
	 * 〈变量声明〉→ var〈变量定义列表〉|〈空〉
	 */
	private void varDeclaration() {
		if (words.get(i).syn == WordAnalises._SYN_VAR) {
			i++;
			varDecList();
		}
	}

	/**
	 * 〈变量定义列表〉 → 〈变量定义〉〈变量定义列表〉|〈变量定义〉
	 */
	private void varDecList() {
		varDef();
		if (words.get(i).syn != WordAnalises._SYN_BEGIN) {
			varDecList();
		} else {
		}
	}

	/**
	 * 〈变量定义〉→〈变量名列表〉: <类型> ；
	 */
	private void varDef() {
		varList();
		if (words.get(i).syn == WordAnalises._SYN_COLON) {
			i++;
			type();
			semicolon();
		} else {
			System.out.println("变量定义错误");
		}
	}

	/**
	 * <变量名列表> → 〈变量名〉|〈变量名〉,〈变量名列表〉
	 */
	private void varList() {
		varName();
		if (words.get(i).syn == WordAnalises._SYN_COMMA) {
			i++;
			varList();
		}
	}

	/**
	 * <类型> → integer
	 */
	private void type() {
		if (words.get(i).syn == WordAnalises._SYN_INTEGER) {
			i++;
			return;
		} else {
			System.out.println("错误类型");
			return;
		}
	}

	/**
	 * 〈复合语句〉→ begin〈语句块〉end
	 */
	private void stmts() {
		if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			i++;
		} else {
			System.out.println("缺少BEGIN!");
			return;
		}
		block();
		if (words.get(i).syn == WordAnalises._SYN_END) {
			i++;
		} else {
			System.out.println("缺少END!");
			return;
		}
	}

	/**
	 * 〈语句块〉→〈语句〉｜〈语句〉; 〈语句块〉
	 */
	private void block() {
		stmt();
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
			block();
		}
	}

	/**
	 * 〈语句〉→〈赋值语句〉|〈条件语句〉|〈循环语句〉|〈复合语句〉|〈空〉
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
	 * 〈赋值语句〉→〈左部〉:= 〈右部〉
	 */
	private void assinStmt() {
		left();
		i++;
		right();
	}

	/**
	 * 〈左部〉→〈变量名〉
	 */
	private void left() {
		if (id()) {
			return;
		} else {
			System.out.println("错误的变量名!");
		}
	}

	/**
	 * 〈右部〉→〈算术表达式〉
	 */
	private void right() {
		arithmeticExpr();
	}

	/**
	 * 〈条件语句〉→ if〈关系表达式〉then〈语句〉else〈语句〉
	 */
	private void ifStmt() {
		relationExpr();
		if (words.get(i).syn == WordAnalises._SYN_THEN) {
			i++;
		} else {
			System.out.println("缺少THEN!");
		}
		stmt();
		if (words.get(i).syn == WordAnalises._SYN_ELSE) {
			i++;
		} else {
			System.out.println("缺少ELSE!");
		}
		stmt();
	}

	/**
	 * 〈循环语句〉→ while〈关系表达式〉do〈语句〉
	 */
	private void loopStmt() {
		relationExpr();
		if (words.get(i).syn == WordAnalises._SYN_DO) {
			i++;
		} else {
			System.out.println("缺少DO!");
		}
		stmt();
	}

	/**
	 * <关系表达式> →〈算术表达式〉〈关系运算符〉〈算术表达式〉
	 */
	private void relationExpr() {
		arithmeticExpr();
		if (!relationOperator()) {
			System.out.println("错误的关系运算符!");
		}
		arithmeticExpr();
	}

	/**
	 * <算术表达式> → 〈项〉| 〈算术表达式〉〈加运算符〉〈项〉
	 */
	private void arithmeticExpr() {
		item();
		if (addOperator()) {
			arithmeticExpr();
		}
	}

	/**
	 * <项> → 〈因子〉| 〈项〉〈乘运算符〉〈因子〉
	 */
	private void item() {
		factor();
		if (mulOperator()) {
			item();
		}
	}

	/**
	 * 〈因子〉→〈变量名〉｜(〈算术表达式〉) ｜〈整数
	 */
	private void factor() {
		if (id()) {

		} else if (words.get(i).syn == WordAnalises._SYN_LPAREN) {
			i++;
			arithmeticExpr();
			if (words.get(i).syn == WordAnalises._SYN_RPAREN) {
				i++;
			} else {
				System.out.println("缺少右括号");
			}
		} else if (num()) {
		} else {
			System.out.println("错误的因子!");
		}
	}

	/**
	 * 〈程序名〉→〈标识符〉
	 */
	private void programName() {
		if (id()) {
			return;
		} else {
			System.out.println("错误的程序名");
		}
	}

	/**
	 * 〈变量名〉→〈标识符〉
	 */
	private void varName() {
		if (!id()) {
			System.out.println("变量名错误");
		}
	}

	/**
	 * 标识符
	 * 
	 * @return 是标识符，返回true，否则返回false
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
	 * 匹配分号
	 */
	private void semicolon() {
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
			return;
		} else {
			System.out.println("缺少';'!");
		}
	}

	/**
	 * 数字
	 * 
	 * @return 是数字返回true，否则返回false
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
	 * 〈关系运算符〉→ < | <= | ＝ | >= | > | <>
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
	 * 〈加运算符〉→ + | -
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
	 * 〈乘运算符〉→ * | /
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
