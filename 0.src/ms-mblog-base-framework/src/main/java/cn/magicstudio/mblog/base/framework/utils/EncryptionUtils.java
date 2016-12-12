package cn.magicstudio.mblog.base.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptionUtils {
	private static final Log log = LogFactory.getLog(EncryptionUtils.class);

	public static final String INITIALIZATION_VECTOR = "cnBHdE9F";

	public static final String TRANSFORMATION = "Blowfish/CBC/PKCS5Padding";

	public static final String BLOWFISH_ENCRYPT = "WoNHIgh@$2=0=1=5";

	public static final String BLOWFISH = "Blowfish";

	public static String md5(String data) {
		if (StringUtils.isEmpty(data)) {
			return "";
		}
		return DigestUtils.md5Hex(data);
	}

	public static String sha(String data) {
		if (StringUtils.isEmpty(data)) {
			return "";
		}
		return DigestUtils.shaHex(data);
	}

	public static String sha4Path(String filePath) throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			return "";
		}
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			return DigestUtils.shaHex(in);
		} catch (FileNotFoundException e) {
			log.error("sha加密失败,文件没找到", e);
		} catch (IOException e) {
			log.error("sha加密失败", e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return "";
	}

	public static String base64Encode(String data) {
		if (StringUtils.isEmpty(data)) {
			return "";
		}
		try {
			return new String(Base64.encodeBase64(data.getBytes(), true),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("base64编码失败", e);
		}
		return "";
	}

	public static String base64Decode(String data) {
		if (StringUtils.isEmpty(data)) {
			return "";
		}
		try {
			return new String(Base64.decodeBase64(data.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("base64解码失败", e);
		}
		return "";
	}

	public static String blowfishEncode(String encryptKey, String text)
			throws Exception {
		SecretKeySpec sksSpec = new SecretKeySpec(encryptKey.getBytes(),
				"Blowfish");

		AlgorithmParameterSpec iv = new IvParameterSpec("cnBHdE9F".getBytes());

		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");

		cipher.init(1, sksSpec, iv);

		byte[] encrypted = cipher.doFinal(text.getBytes());
		return new String(Hex.encodeHex(encrypted));
	}

	public static String blowfishDecode(String encryptKey, String text)
			throws Exception {
		byte[] encrypted = null;
		try {
			encrypted = Hex.decodeHex(text.toCharArray());
		} catch (Exception e) {
			log.error("Blowfish解密失败", e);
		}

		SecretKeySpec skeSpect = new SecretKeySpec(encryptKey.getBytes(),
				"Blowfish");
		AlgorithmParameterSpec iv = new IvParameterSpec("cnBHdE9F".getBytes());
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
		cipher.init(2, skeSpect, iv);
		byte[] decrypted = cipher.doFinal(encrypted);
		return new String(decrypted);
	}

	public static String blowfishEncodeByEnvVar(String text) throws Exception {
		String encryptKey = System.getProperty("blowfishEncryptKey");
		encryptKey = StringUtils.isBlank(encryptKey) ? "WoNHIgh@$2=0=1=5"
				: encryptKey;
		return blowfishEncode(encryptKey, text);
	}

	public static String blowfishDecodeByEnvVar(String text) throws Exception {
		String encryptKey = System.getProperty("blowfishEncryptKey");
		encryptKey = StringUtils.isBlank(encryptKey) ? "WoNHIgh@$2=0=1=5"
				: encryptKey;
		return blowfishDecode(encryptKey, text);
	}
}
