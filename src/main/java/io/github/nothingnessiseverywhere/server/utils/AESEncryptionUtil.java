package io.github.nothingnessiseverywhere.server.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESEncryptionUtil {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY = "NothingnessIsEverywhere!"; // 建议使用更安全的密钥管理方式

    // 生成 SecretKeySpec 对象
    private static SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    // 加密方法
    public static String encrypt(String username) {
        try {
            // 获取 Cipher 实例
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化 Cipher 为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec());
            // 执行加密操作
            byte[] encryptedBytes = cipher.doFinal(username.getBytes(StandardCharsets.UTF_8));
            // 对加密后的字节数组进行 Base64 编码
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 解密方法
    public static String decrypt(String encryptedUsername) {
        try {
            // 对 Base64 编码的加密数据进行解码
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedUsername);
            // 获取 Cipher 实例
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化 Cipher 为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec());
            // 执行解密操作
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            // 将解密后的字节数组转换为字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}