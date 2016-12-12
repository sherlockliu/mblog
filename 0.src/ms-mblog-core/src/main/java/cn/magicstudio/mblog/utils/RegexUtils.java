package cn.magicstudio.mblog.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class RegexUtils {
	private static Cache<String, Pattern> patterns = CacheBuilder.newBuilder()
			.build();

	public static String findFirst(String originalStr, String regex) {
		if ((StringUtils.isBlank(originalStr)) || (StringUtils.isBlank(regex))) {
			return "";
		}
		Pattern p = null;
//		try {
//			p = (Pattern) patterns.get(regex, new Callable() {
//				PatternCompiler pc = new Perl5Compiler();
//
//				public Pattern call() throws Exception {
//					return this.pc.compile(RegexUtils.this, 32769);
//				}
//			}); TODO
//		} catch (ExecutionException localExecutionException) {
//		}

		PatternMatcher matcher = new Perl5Matcher();
		if (matcher.contains(originalStr, p)) {
			return StringUtils.trimToEmpty(matcher.getMatch().group(0));
		}
		return "";
	}
}