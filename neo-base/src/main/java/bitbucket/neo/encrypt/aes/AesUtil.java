package bitbucket.neo.encrypt.aes;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AesUtil
 *
 * @author liujunjie
 * @version 1.0.1: AesUtil, v0.1 2019年04月16日 20:49  Exp $
 */
public class AesUtil {

    private final static String key = "CB4BCDA4C9426E0817578E5CADDDD445";

    /**
     * 生成128位随机密钥
     */
    public static byte[] randomKey() {
        return randomKey(128);
    }

    /**
     * 生成随机密钥
     *
     * @param keySize 密钥位数
     */
    private static byte[] randomKey(int keySize) {
        if (keySize != 128) {
            throw new RuntimeException("AES密钥长度不合法. keySize:" + keySize);
        }
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        /*if (key.length != 16) {
            throw new RuntimeException("AES加密失败, 密钥不是128位");
        }*/
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    /**
     * 解密数据
     *
     * @param value 待解密数据
     * @param key   密钥
     */
    public static byte[] decrypt(byte[] value, byte[] key) throws Exception {
        /*if (key.length != 16) {
            throw new RuntimeException("AES解密失败, 密钥不是128位");
        }*/
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(value);
    }


    /**
     * 默认的AES加密字符串方法，可以内部使用
     * 对明文的UTF-8编码字节进行加密，然后使用Base64转码
     * 密钥使用UTF-8编码
     *
     * @param data 明文
     * @param key  加密密钥
     */
    public static String encrypt(String data, String key) throws Exception {
        if (Strings.isNullOrEmpty(data) || Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("AES加密失败, 参数有误. data: " + data + ", key: " + key);
        }
        byte[] keyBytes = key.getBytes(Charsets.UTF_8);
        byte[] dataBytes = data.getBytes(Charsets.UTF_8);
        byte[] encryptBytes = encrypt(dataBytes, keyBytes);
        return parseByte2HexStr(encryptBytes);
    }

    /**
     * 默认的AES解密字符串方法，可以内部使用
     * 对密文使用Base64转码后，再进行解密，然后再转为UTF-8编码
     * 密钥使用UTF-8编码
     *
     * @param ciphertext 密文
     * @param key        加密密钥
     */
    public static String decrypt(String ciphertext, String key) throws Exception {
        if (Strings.isNullOrEmpty(ciphertext) || Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("AES解密失败, 参数有误. ciphertext: " + ciphertext + ", key: " + key);
        }
        byte[] keyBytes = key.getBytes(Charsets.UTF_8);
        byte[] dataBytes = parseHexStr2Byte(ciphertext);
        byte[] keyBytesDecrypt = decrypt(dataBytes, keyBytes);
        return new String(keyBytesDecrypt, Charsets.UTF_8);
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return new byte[0];
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        System.out.println(encrypt("", key));
        System.out.println(decrypt("0DA145A588BCAB7D50F67FCC810139C5", key));
    }

}
