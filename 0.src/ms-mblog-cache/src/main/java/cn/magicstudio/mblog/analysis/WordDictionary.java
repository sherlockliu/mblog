package cn.magicstudio.mblog.analysis;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordDictionary {
	private static WordDictionary singleton;
	private static final String MAIN_DICT = "/dict.txt";
	private static String USER_DICT_SUFFIX = ".dict";

	public final Map<String, Double> freqs = new HashMap();
	public final Set<String> loadedPath = new HashSet();
	private Double minFreq = Double.valueOf(Double.MAX_VALUE);
	private Double total = Double.valueOf(0.0D);
	private DictSegment _dict;

	private WordDictionary() {
		loadDict();
	}

	public static WordDictionary getInstance() {
		if (singleton == null) {
			synchronized (WordDictionary.class) {
				if (singleton == null) {
					singleton = new WordDictionary();
					return singleton;
				}
			}
		}
		return singleton;
	}

	public void init(File configFile) {
		String path = configFile.getAbsolutePath();
		System.out.println("initialize user dictionary:" + path);
		synchronized (WordDictionary.class) {
			if (this.loadedPath.contains(path))
				return;
			File[] arrayOfFile;
			int j = (arrayOfFile = configFile.listFiles()).length;
			for (int i = 0; i < j; i++) {
				File userDict = arrayOfFile[i];
				if (userDict.getPath().endsWith(USER_DICT_SUFFIX)) {
					singleton.loadUserDict(userDict);
					this.loadedPath.add(path);
				}
			}
		}
	}

	/* Error */
	public void loadDict() {
	
	}

	private String addWord(String word) {
		if ((word != null) && (!"".equals(word.trim()))) {
			String key = word.trim().toLowerCase();
			this._dict.fillSegment(key.toCharArray());
			return key;
		}

		return null;
	}

	public void loadUserDict(File userDict) {
		loadUserDict(userDict, Charset.forName("UTF-8"));
	}

	/* Error */
	public void loadUserDict(File userDict, Charset charset) {

	}

	public DictSegment getTrie() {
		return this._dict;
	}

	public boolean containsWord(String word) {
		return this.freqs.containsKey(word);
	}

	public Double getFreq(String key) {
		if (containsWord(key)) {
			return (Double) this.freqs.get(key);
		}
		return this.minFreq;
	}
}
