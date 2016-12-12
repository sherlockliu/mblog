package cn.magicstudio.mblog.cache.suggest;

import java.util.List;
import java.util.Map;

public abstract interface IndexReader<T>
{
  public abstract String name();
  
  public abstract List<T> search(String paramString, int paramInt1, int paramInt2);
  
  public abstract List<T> search(String paramString, int paramInt1, int paramInt2, Map<String, Object> paramMap);
  
  public abstract List<T> get(String paramString);
  
  public abstract List<T> get(String paramString, Map<String, Object> paramMap);
}
