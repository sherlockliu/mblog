package cn.magicstudio.mblog.base.framework.uuid;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


import org.omg.CORBA.portable.IDLEntity;

import cn.magicstudio.mblog.base.framework.uuid.lang.Hex;

public class UUID implements Comparable<UUID>, Externalizable, Cloneable,
		IDLEntity {
	static final long serialVersionUID = 7435962790062944603L;
	public long time;
	public long clockSeqAndNode;

	public UUID() {
		this(UUIDGen.newTime(), UUIDGen.getClockSeqAndNode());
	}

	public UUID(long time, long clockSeqAndNode) {
		this.time = time;
		this.clockSeqAndNode = clockSeqAndNode;
	}

	public UUID(UUID u) {
		this(u.time, u.clockSeqAndNode);
	}

	public UUID(CharSequence s) {
		this(Hex.parseLong(s.subSequence(0, 18)), Hex.parseLong(s.subSequence(
				19, 36)));
	}

	public int compareTo(UUID t) {
		if (this == t) {
			return 0;
		}
		if (this.time > t.time) {
			return 1;
		}
		if (this.time < t.time) {
			return -1;
		}
		if (this.clockSeqAndNode > t.clockSeqAndNode) {
			return 1;
		}
		if (this.clockSeqAndNode < t.clockSeqAndNode) {
			return -1;
		}
		return 0;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(this.time);
		out.writeLong(this.clockSeqAndNode);
	}

	public void readExternal(ObjectInput in) throws IOException {
		this.time = in.readLong();
		this.clockSeqAndNode = in.readLong();
	}

	public final String toString() {
		return toAppendable(null).toString();
	}

	public StringBuffer toStringBuffer(StringBuffer in) {
		StringBuffer out = in;
		if (out == null) {
			out = new StringBuffer(36);
		} else {
			out.ensureCapacity(out.length() + 36);
		}
		return (StringBuffer) toAppendable(out);
	}

	public Appendable toAppendable(Appendable a) {
		Appendable out = a;
		if (out == null) {
			out = new StringBuilder(36);
		}

		Hex.append(out, this.clockSeqAndNode, 12);
		Hex.append(out, (short) (int) this.time);
		Hex.append(out, (short) (int) (this.time >> 16));
		Hex.append(out, (short) (int) (this.clockSeqAndNode >> 48));
		Hex.append(out, (int) (this.time >> 32));

		return out;
	}

	public int hashCode() {
		return (int) (this.time >> 32 ^ this.time ^ this.clockSeqAndNode >> 32 ^ this.clockSeqAndNode);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
		}

		return null;
	}

	public final long getTime() {
		return this.time;
	}

	public final long getClockSeqAndNode() {
		return this.clockSeqAndNode;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof UUID)) {
			return false;
		}
		return compareTo((UUID) obj) == 0;
	}

	public static UUID nilUUID() {
		return new UUID(0L, 0L);
	}
}
