package cn.magicstudio.mblog.analysis;

import cn.magicstudio.mblog.analysis.viterbi.FinalSeg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Segmenter
{
  private static WordDictionary wordDict = null;
  private static FinalSeg finalSeg = FinalSeg.getInstance();
  
  public static enum SegMode {
    INDEX, 
    SEARCH;
  }
  
  private Map<Integer, List<Integer>> createDAG(String sentence)
  {
    Map<Integer, List<Integer>> dag = new HashMap();
    DictSegment trie = wordDict.getTrie();
    char[] chars = sentence.toCharArray();
    int N = chars.length;
    int i = 0;int j = 0;
    while (i < N) {
      Hit hit = trie.match(chars, i, j - i + 1);
      if ((hit.isPrefix()) || (hit.isMatch())) {
        if (hit.isMatch())
          if (!dag.containsKey(Integer.valueOf(i))) {
            List<Integer> value = new ArrayList();
            dag.put(Integer.valueOf(i), value);
            value.add(Integer.valueOf(j));
          }
          else {
            ((List)dag.get(Integer.valueOf(i))).add(Integer.valueOf(j));
          }
        j++;
        if (j >= N) {
          i++;
          j = i;
        }
      }
      else {
        i++;
        j = i;
      }
    }
    for (i = 0; i < N; i++) {
      if (!dag.containsKey(Integer.valueOf(i))) {
        List<Integer> value = new ArrayList();
        value.add(Integer.valueOf(i));
        dag.put(Integer.valueOf(i), value);
      }
    }
    return dag;
  }
  
  private Map<Integer, Pair<Integer>> calc(String sentence, Map<Integer, List<Integer>> dag)
  {
    int N = sentence.length();
    HashMap<Integer, Pair<Integer>> route = new HashMap();
    route.put(Integer.valueOf(N), new Pair(Integer.valueOf(0), 0.0D));
    for (int i = N - 1; i > -1; i--) {
      Pair<Integer> candidate = null;
      for (Integer x : dag.get(Integer.valueOf(i))) {
        double freq = wordDict.getFreq(sentence.substring(i, x.intValue() + 1)).doubleValue() + ((Pair)route.get(Integer.valueOf(x.intValue() + 1))).freq.doubleValue();
        if (candidate == null) {
          candidate = new Pair(x, freq);
        }
        else if (candidate.freq.doubleValue() < freq) {
          candidate.freq = Double.valueOf(freq);
          candidate.key = x;
        }
      }
      route.put(Integer.valueOf(i), candidate);
    }
    return route;
  }
  
  public List<SegToken> process(String paragraph, SegMode mode)
  {
    List<SegToken> tokens = new ArrayList();
    StringBuilder sb = new StringBuilder();
    int offset = 0;
    char ch; for (int i = 0; i < paragraph.length(); i++) {
      ch = CharacterUtil.regularize(paragraph.charAt(i));
      if (CharacterUtil.ccFind(ch)) {
        sb.append(ch);
      } else {
        if (sb.length() > 0)
        {
          if (mode == SegMode.SEARCH) {
            for (String word : sentenceProcess(sb.toString())) {
              tokens.add(new SegToken(word, offset, offset += word.length()));
            }
            
          } else {
            for (String token : sentenceProcess(sb.toString())) {
              if (token.length() > 2)
              {
                for (int j = 0; 
                    j < token.length() - 1; j++) {
                  String gram2 = token.substring(j, j + 2);
                  if (wordDict.containsWord(gram2))
                    tokens.add(new SegToken(gram2, offset + j, offset + j + 2));
                }
              }
              if (token.length() > 3)
              {
                for (int j = 0; 
                    j < token.length() - 2; j++) {
                  String gram3 = token.substring(j, j + 3);
                  if (wordDict.containsWord(gram3))
                    tokens.add(new SegToken(gram3, offset + j, offset + j + 3));
                }
              }
              tokens.add(new SegToken(token, offset, offset += token.length()));
            }
          }
          sb = new StringBuilder();
          offset = i;
        }
        if (wordDict.containsWord(paragraph.substring(i, i + 1))) {
          tokens.add(new SegToken(paragraph.substring(i, i + 1), offset++, offset));
        } else
          tokens.add(new SegToken(paragraph.substring(i, i + 1), offset++, offset));
      }
    }
    if (sb.length() > 0) {
      if (mode == SegMode.SEARCH) {
        for (String token : sentenceProcess(sb.toString())) {
          tokens.add(new SegToken(token, offset, offset += token.length()));
        }
        
      } else {
        for (String token : sentenceProcess(sb.toString())) {
          if (token.length() > 2)
          {
            for (int j = 0; 
                j < token.length() - 1; j++) {
              String gram2 = token.substring(j, j + 2);
              if (wordDict.containsWord(gram2))
                tokens.add(new SegToken(gram2, offset + j, offset + j + 2));
            }
          }
          if (token.length() > 3)
          {
            for (int j = 0; 
                j < token.length() - 2; j++) {
              String gram3 = token.substring(j, j + 3);
              if (wordDict.containsWord(gram3))
                tokens.add(new SegToken(gram3, offset + j, offset + j + 3));
            }
          }
          tokens.add(new SegToken(token, offset, offset += token.length()));
        }
      }
    }
    return tokens;
  }
  



  public List<String> sentenceProcess(String sentence)
  {
    List<String> tokens = new ArrayList();
    int N = sentence.length();
    Map<Integer, List<Integer>> dag = createDAG(sentence);
    Map<Integer, Pair<Integer>> route = calc(sentence, dag);
    
    int x = 0;
    int y = 0;
    
    StringBuilder sb = new StringBuilder();
    while (x < N) {
      y = ((Integer)((Pair)route.get(Integer.valueOf(x))).key).intValue() + 1;
      String lWord = sentence.substring(x, y);
      if (y - x == 1) {
        sb.append(lWord);
      } else {
        if (sb.length() > 0) {
          String buf = sb.toString();
          sb = new StringBuilder();
          if (buf.length() == 1) {
            tokens.add(buf);

          }
          else if (wordDict.containsWord(buf)) {
            tokens.add(buf);
          }
          else {
            finalSeg.cut(buf, tokens);
          }
        }
        
        tokens.add(lWord);
      }
      x = y;
    }
    String buf = sb.toString();
    if (buf.length() > 0) {
      if (buf.length() == 1) {
        tokens.add(buf);

      }
      else if (wordDict.containsWord(buf)) {
        tokens.add(buf);
      }
      else {
        finalSeg.cut(buf, tokens);
      }
    }
    

    return tokens;
  }
}
