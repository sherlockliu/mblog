package cn.magicstudio.mblog.cache.redis;

public abstract interface ExRedisCallback
{
  public abstract Object doWithMyJedis(ExJedis paramExJedis);
}