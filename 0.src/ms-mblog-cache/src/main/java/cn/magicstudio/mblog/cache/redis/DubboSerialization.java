package cn.magicstudio.mblog.cache.redis;

import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.support.dubbo.GenericObjectInput;
import com.alibaba.dubbo.common.serialize.support.dubbo.GenericObjectOutput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class DubboSerialization<T> implements RedisSerializer<T> {
	public byte[] serialize(T t) throws SerializationException {
		ByteArrayOutputStream bytes = null;
		try {
			bytes = new ByteArrayOutputStream();
			ObjectOutput objectOutput = new GenericObjectOutput(bytes);
			if (t == null) {
				return null;
			}
			if ((t instanceof String)) {
				objectOutput.writeUTF(String.valueOf(t));
			} else {
				objectOutput.writeObject(t);
			}

			objectOutput.flushBuffer();
			return bytes.toByteArray();
		} catch (IOException e) {
			throw new SerializationException("序列化出错", e);
		}
	}

	public T deserialize(byte[] bytes) throws SerializationException {
		if ((bytes == null) || (bytes.length == 0)) {
			return null;
		}
		ObjectInput objectInput = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			objectInput = new GenericObjectInput(in);
			return (T) objectInput.readObject();
		} catch (IOException e) {
			throw new SerializationException("反序列化出错", e);
		} catch (ClassNotFoundException e) {
			throw new SerializationException("反序列化出错,类不存在", e);
		} catch (Exception ex) {
			throw new SerializationException("反序列化出错");
		}
	}
}
