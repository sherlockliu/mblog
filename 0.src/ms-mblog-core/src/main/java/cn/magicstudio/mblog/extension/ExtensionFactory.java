package cn.magicstudio.mblog.extension;

public abstract interface ExtensionFactory
{
  public abstract <T> T getExtension(Class<T> paramClass, ExtensionData paramExtensionData);
}
