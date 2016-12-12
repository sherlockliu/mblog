package cn.magicstudio.mblog.analysis;

public class SegToken {
	public String word;
	public int startOffset;
	public int endOffset;

	public SegToken(String word, int startOffset, int endOffset) {
		this.word = word;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public String toString() {
		return "[" + this.word + ", " + this.startOffset + ", "
				+ this.endOffset + "]";
	}
}