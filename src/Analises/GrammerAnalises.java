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
		System.out.println("检查完毕!");
	}

	/**
	 * 〈程序〉→〈程序首部〉〈程序体〉
	 */
	private void program() {
		node = new Node("program", false);
		graTree = new Tree(node);
		programHeader(node);
		programBody(node);
	}

	/**
	 * 〈程序首部〉→ program〈程序名〉；
	 */
	private void programHeader(Node parent) {
		node = new Node("programHeader", false);
		graTree.addChild(parent, node);
		if (words.get(i).syn == WordAnalises._SYN_PROGRAM) {
			codaNode = new Node("program", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("缺少program!");
			return;
		}
		programName(node);
		semicolon(node);
	}

	/**
	 * 〈程序体〉→〈变量声明〉〈复合语句〉
	 */
	private void programBody(Node parent) {
		node = new Node("programBody", false);
		graTree.addChild(parent, node);
		varDeclaration(node);
		stmts(node);
	}

	/**
	 * 〈变量声明〉→ var〈变量定义列表〉|〈空〉
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
	 * 〈变量定义列表〉 → 〈变量定义〉〈变量定义列表〉|〈变量定义〉
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
	 * 〈变量定义〉→〈变量名列表〉: <类型> ；
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
			System.out.println("变量定义错误");
		}
	}

	/**
	 * <变量名列表> → 〈变量名〉|〈变量名〉,〈变量名列表〉
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
	 * <类型> → integer
	 */
	private void type(Node parent) {
		if (words.get(i).syn == WordAnalises._SYN_INTEGER) {
			codaNode = new Node("integer", true);
			graTree.addChild(node, codaNode);
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
	private void stmts(Node parent) {
		node = new Node("stmts", false);
		graTree.addChild(parent, node);
		if (words.get(i).syn == WordAnalises._SYN_BEGIN) {
			codaNode = new Node("begin", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("缺少BEGIN!");
			return;
		}
		block(node);
		if (words.get(i).syn == WordAnalises._SYN_END) {
			codaNode = new Node("end", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("缺少END!");
			return;
		}
	}

	/**
	 * 〈语句块〉→〈语句〉｜〈语句〉; 〈语句块〉
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
	 * 〈语句〉→〈赋值语句〉|〈条件语句〉|〈循环语句〉|〈复合语句〉|〈空〉
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
	 * 〈赋值语句〉→〈左部〉:= 〈右部〉
	 */
	private void assinStmt(Node parent) {
		node = new Node("assinStmt", false);
		graTree.addChild(parent, node);
		left(node);
		i++;
		right(node);
	}

	/**
	 * 〈左部〉→〈变量名〉
	 */
	private void left(Node parent) {
		node = new Node("left", false);
		graTree.addChild(parent, node);
		if (id(node)) {
			return;
		} else {
			System.out.println("错误的变量名!");
		}
	}

	/**
	 * 〈右部〉→〈算术表达式〉
	 */
	private void right(Node parent) {
		node = new Node("right", false);
		graTree.addChild(parent, node);
		arithmeticExpr(node);
	}

	/**
	 * 〈条件语句〉→ if〈关系表达式〉then〈语句〉else〈语句〉
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
			System.out.println("缺少THEN!");
		}
		stmt(node);
		if (words.get(i).syn == WordAnalises._SYN_ELSE) {
			codaNode = new Node("else", true);
			graTree.addChild(node, codaNode);
			i++;
		} else {
			System.out.println("缺少ELSE!");
		}
		stmt(node);
	}

	/**
	 * 〈循环语句〉→ while〈关系表达式〉do〈语句〉
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
			System.out.println("缺少DO!");
		}
		stmt(node);
	}

	/**
	 * <关系表达式> →〈算术表达式〉〈关系运算符〉〈算术表达式〉
	 */
	private void relationExpr(Node parent) {
		node = new Node("relationExpr", false);
		graTree.addChild(parent, node);
		arithmeticExpr(node);
		if (!relationOperator(node)) {
			System.out.println("错误的关系运算符!");
		}
		arithmeticExpr(node);
	}

	/**
	 * <算术表达式> → 〈项〉| 〈算术表达式〉〈加运算符〉〈项〉
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
	 * <项> → 〈因子〉| 〈项〉〈乘运算符〉〈因子〉
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
	 * 〈因子〉→〈变量名〉｜(〈算术表达式〉) ｜〈整数
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
				System.out.println("缺少右括号");
			}
		} else if (num(node)) {
		} else {
			System.out.println("错误的因子!");
		}
	}

	/**
	 * 〈程序名〉→〈标识符〉
	 */
	private void programName(Node parent) {
		node = new Node("factor", false);
		graTree.addChild(parent, node);
		if (id(node)) {
			return;
		} else {
			System.out.println("错误的程序名");
		}
	}

	/**
	 * 〈变量名〉→〈标识符〉
	 */
	private void varName(Node parent) {
		node = new Node("varName", false);
		graTree.addChild(parent, node);
		if (!id(node)) {
			System.out.println("变量名错误");
		}
	}

	/**
	 * 标识符
	 * 
	 * @return 是标识符，返回true，否则返回false
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
	 * 匹配分号
	 */
	private void semicolon(Node parent) {
		if (words.get(i).syn == WordAnalises._SYN_SEMICOLON) {
			i++;
			codaNode = new Node(";", true);
			graTree.addChild(parent, codaNode);
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
	 * 〈关系运算符〉→ < | <= | ＝ | >= | > | <>
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
	 * 〈加运算符〉→ + | -
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
	 * 〈乘运算符〉→ * | /
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
