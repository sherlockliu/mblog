package cn.magicstudio.mblog.analysis.viterbi;

import cn.magicstudio.mblog.analysis.CharacterUtil;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class FinalSeg {
	private static FinalSeg singleInstance;
	private static final String PROB_EMIT = "/prob_emit.txt";
	private static char[] states = { 'B', 'M', 'E', 'S' };
	private static Map<Character, Map<Character, Double>> emit;
	private static Map<Character, Double> start;
	private static Map<Character, Map<Character, Double>> trans;
	private static Map<Character, char[]> prevStatus;
	private static Double MIN_FLOAT = Double.valueOf(-3.14E100D);

	public static synchronized FinalSeg getInstance() {
		if (singleInstance == null) {
			singleInstance = new FinalSeg();
			singleInstance.loadModel();
		}
		return singleInstance;
	}

	/* Error */
	private void loadModel() {
		//TODO
	}

	public void cut(String sentence, List<String> tokens) {
		StringBuilder chinese = new StringBuilder();
		StringBuilder other = new StringBuilder();
		for (int i = 0; i < sentence.length(); i++) {
			char ch = sentence.charAt(i);
			if (CharacterUtil.isChineseLetter(ch)) {
				if (other.length() > 0) {
					processOtherUnknownWords(other.toString(), tokens);
					other = new StringBuilder();
				}
				chinese.append(ch);
			} else {
				if (chinese.length() > 0) {
					viterbi(chinese.toString(), tokens);
					chinese = new StringBuilder();
				}
				other.append(ch);
			}
		}

		if (chinese.length() > 0) {
			viterbi(chinese.toString(), tokens);
		} else {
			processOtherUnknownWords(other.toString(), tokens);
		}
	}

	public void viterbi(String sentence, List<String> tokens) {
		// Vector<Map<Character, Double>> v = new Vector();
		// Map<Character, Node> path = new HashMap();
		//
		// v.add(new HashMap());
		// char[] arrayOfChar1; int j = (arrayOfChar1 = states).length; Double
		// emP; for (int i = 0; i < j; i++) { char state = arrayOfChar1[i];
		// emP =
		// (Double)((Map)emit.get(Character.valueOf(state))).get(Character.valueOf(sentence.charAt(0)));
		// if (emP == null)
		// emP = MIN_FLOAT;
		// ((Map)v.get(0)).put(Character.valueOf(state),
		// Double.valueOf(((Double)start.get(Character.valueOf(state))).doubleValue()
		// + emP.doubleValue()));
		// path.put(Character.valueOf(state), new Node(Character.valueOf(state),
		// null));
		// }
		//
		// for (int i = 1; i < sentence.length(); i++) {
		// Object vv = new HashMap();
		// v.add(vv);
		// Object newPath = new HashMap();
		// char[] arrayOfChar2; Double localDouble1 = (arrayOfChar2 =
		// states).length; for (emP = 0; emP < localDouble1; emP++) { char y =
		// arrayOfChar2[emP];
		// Double emp =
		// (Double)((Map)emit.get(Character.valueOf(y))).get(Character.valueOf(sentence.charAt(i)));
		// if (emp == null)
		// emp = MIN_FLOAT;
		// Pair<Character> candidate = null;
		// char[] arrayOfChar3; int m = (arrayOfChar3 =
		// (char[])prevStatus.get(Character.valueOf(y))).length; for (int k = 0;
		// k < m; k++) { char y0 = arrayOfChar3[k];
		// Double tranp =
		// (Double)((Map)trans.get(Character.valueOf(y0))).get(Character.valueOf(y));
		// if (tranp == null)
		// tranp = MIN_FLOAT;
		// tranp = Double.valueOf(tranp.doubleValue() + (emp.doubleValue() +
		// ((Double)((Map)v.get(i -
		// 1)).get(Character.valueOf(y0))).doubleValue()));
		// if (candidate == null) {
		// candidate = new Pair(Character.valueOf(y0), tranp.doubleValue());
		// } else if (candidate.freq.doubleValue() <= tranp.doubleValue()) {
		// candidate.freq = tranp;
		// candidate.key = Character.valueOf(y0);
		// }
		// }
		// ((Map)vv).put(Character.valueOf(y), candidate.freq);
		// ((Map)newPath).put(Character.valueOf(y), new
		// Node(Character.valueOf(y), (Node)path.get(candidate.key)));
		// }
		// path = (Map<Character, Node>)newPath;
		// }
		// double probE = ((Double)((Map)v.get(sentence.length() -
		// 1)).get(Character.valueOf('E'))).doubleValue();
		// double probS = ((Double)((Map)v.get(sentence.length() -
		// 1)).get(Character.valueOf('S'))).doubleValue();
		// Vector<Character> posList = new Vector(sentence.length());
		// Node win;
		// Node win; if (probE < probS) {
		// win = (Node)path.get(Character.valueOf('S'));
		// } else {
		// win = (Node)path.get(Character.valueOf('E'));
		// }
		// while (win != null) {
		// posList.add(win.value);
		// win = win.parent;
		// }
		// Collections.reverse(posList);
		//
		// int begin = 0;int next = 0;
		// for (int i = 0; i < sentence.length(); i++) {
		// char pos = ((Character)posList.get(i)).charValue();
		// if (pos == 'B') {
		// begin = i;
		// } else if (pos == 'E') {
		// tokens.add(sentence.substring(begin, i + 1));
		// next = i + 1;
		// }
		// else if (pos == 'S') {
		// tokens.add(sentence.substring(i, i + 1));
		// next = i + 1;
		// }
		// }
		// if (next < sentence.length()) {
		// tokens.add(sentence.substring(next));
		// }
		// TODO
	}

	private void processOtherUnknownWords(String other, List<String> tokens) {
		Matcher mat = CharacterUtil.reSkip.matcher(other);
		int offset = 0;
		while (mat.find()) {
			if (mat.start() > offset) {
				tokens.add(other.substring(offset, mat.start()));
			}
			tokens.add(mat.group());
			offset = mat.end();
		}
		if (offset < other.length()) {
			tokens.add(other.substring(offset));
		}
	}
}