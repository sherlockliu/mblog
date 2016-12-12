package cn.magicstudio.mblog.model;

import java.util.Iterator;

public abstract interface DataSource<T> extends AutoCloseable {
	public abstract Iterator<T> getIterator();
}
