package cn.magicstudio.mblog.base.framework.uuid;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class UUIDHolder implements Streamable {
	public UUID value = null;

	public UUIDHolder() {
	}

	public UUIDHolder(UUID initialValue) {
		this.value = initialValue;
	}

	public void _read(InputStream i) {
		this.value = UUIDHelper.read(i);
	}

	public void _write(OutputStream o) {
		UUIDHelper.write(o, this.value);
	}

	public TypeCode _type() {
		return UUIDHelper.type();
	}
}
