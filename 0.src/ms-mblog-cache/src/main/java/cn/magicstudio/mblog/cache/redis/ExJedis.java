package cn.magicstudio.mblog.cache.redis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.Protocol.Keyword;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;

class ExJedis {
	private final double score = 1.0D;

	private Client client;

	private static Map<String, Method> methodCache = new HashMap();

	private static Map<String, Field> fieldCache = new HashMap();

	private static final String SENDCOMMAND_METHOD = "sendCommand";

	private static final String ZRANGEBYLEX_CMD = "ZRANGEBYLEX";

	public ExJedis(Jedis jedis) {
		this.client = jedis.getClient();
	}

	private final byte[] default_score = SafeEncoder.encode(String
			.valueOf(1.0D));

	public Long mzadd(String key, Map<String, Double> scoreMembers) {
		ArrayList<byte[]> args = new ArrayList(scoreMembers.size() * 2 + 1);
		args.add(SafeEncoder.encode(key));
		for (Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
			args.add(SafeEncoder.encode(String.valueOf(entry.getValue())));
			args.add(SafeEncoder.encode((String) entry.getKey()));
		}
		byte[][] argsArray = new byte[args.size()][];
		args.toArray(argsArray);
		sendCommand(Protocol.Command.ZADD, argsArray);
		return this.client.getIntegerReply();
	}

	private void sendCommand(Protocol.Command command, byte[][] argsArray) {
		Method method = (Method) methodCache.get("sendCommand");
		if (method == null) {
			method = ReflectionUtils
					.findMethod(Client.class, "sendCommand", new Class[] {
							Protocol.Command.class, argsArray.getClass() });
			method.setAccessible(true);
			methodCache.put("sendCommand", method);
		}
		try {
			method.invoke(this.client, new Object[] { command, argsArray });
		} catch (Exception e) {
			throw new RuntimeException("执行命令出错", e);
		}
	}

	private void _sendExtCommand(byte[] command, List<byte[]> args)
			throws IOException {
		this.client.connect();
		Field field = (Field) fieldCache.get("outputStream");
		if (field == null) {
			field = ReflectionUtils.findField(Client.class, "outputStream");
			field.setAccessible(true);
			fieldCache.put("outputStream", field);
		}
		RedisOutputStream os = (RedisOutputStream) ReflectionUtils.getField(
				field, this.client);
		os.write((byte) 42);
		os.writeIntCrLf(args.size() + 1);
		os.write((byte) 36);
		os.writeIntCrLf(command.length);
		os.write(command);
		os.writeCrLf();
		for (byte[] arg : args) {
			os.write((byte) 36);
			os.writeIntCrLf(arg.length);
			os.write(arg);
			os.writeCrLf();
		}
		Field pipeline = (Field) fieldCache.get("pipelinedCommands");
		if (pipeline == null) {
			pipeline = ReflectionUtils.findField(Client.class,
					"pipelinedCommands");
			pipeline.setAccessible(true);
			fieldCache.put("pipelinedCommands", pipeline);
		}
		int pipCount = Integer.valueOf(
				(String) ReflectionUtils.getField(pipeline, this.client)).intValue();
		ReflectionUtils.setField(pipeline, this.client,
				Integer.valueOf(pipCount + 1));
	}

	public Long mzadd(String key, Set<String> scoreMembers) {
		ArrayList<byte[]> args = new ArrayList(scoreMembers.size() * 2 + 1);
		args.add(SafeEncoder.encode(key));
		for (String member : scoreMembers) {
			args.add(this.default_score);
			args.add(SafeEncoder.encode(member));
		}
		byte[][] argsArray = new byte[args.size()][];
		args.toArray(argsArray);
		sendCommand(Protocol.Command.ZADD, argsArray);
		return this.client.getIntegerReply();
	}

	public Collection<String> zrangebylex(String key, String min, String max,
			int start, int take) {
		ArrayList<byte[]> args = new ArrayList();
		args.add(SafeEncoder.encode(key));
		args.add(SafeEncoder.encode(min));
		args.add(SafeEncoder.encode(max));
		args.add(Protocol.Keyword.LIMIT.raw);
		args.add(SafeEncoder.encode(String.valueOf(start)));
		args.add(SafeEncoder.encode(String.valueOf(take)));
		try {
			_sendExtCommand(SafeEncoder.encode("ZRANGEBYLEX"), args);
		} catch (IOException e) {
			throw new RuntimeException("sendcmd error", e);
		}
		return this.client.getMultiBulkReply();
	}
}
