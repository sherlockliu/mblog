package cn.magicstudio.mblog.base.framework.uuid;

import java.io.PrintStream;

import com.yougou.logistics.base.common.uuid.UUID;
public class Test
{
  public static void main(String[] args)
  {
    for (int i = 0; i < 200; i++)
    {
      String x = new UUID().toString();
      System.out.println(x);
    }
  }
}
