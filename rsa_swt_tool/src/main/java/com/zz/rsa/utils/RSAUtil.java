package com.zz.rsa.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

/**
 * 非对称加密是公钥加密，私钥来解密.使用的是Bouncy Castle 是一种用于 Java 平台的开放源码的轻量级密码术包.
 * 1、此Demo使用的是bcprov-ext-jdk15on-152.jar
 * 2、把jar文件复制到 $JAVA_HOME$\jre\lib\ext目录下面
 * 3、修改配置文件\jre\lib\security\java.security
 * security.provider.1=sun.security.provider.Sun
 * security.provider.2=sun.security.rsa.SunRsaSign
 * security.provider.3=com.sun.net.ssl.internal.ssl.Provider
 * security.provider.4=com.sun.crypto.provider.SunJCE
 * security.provider.5=sun.security.jgss.SunProvider
 * security.provider.6=com.sun.security.sasl.Provider
 * security.provider.7=org.jcp.xml.dsig.internal.dom.XMLDSigRI
 * security.provider.8=sun.security.smartcardio.SunPCSC 上述8个是JDK已经实现了的加密方式. 将
 * security.provider.9=org.bouncycastle.jce.provider.BouncyCastleProvider 添加到后面.
 * 非对称加密称为公钥加密，算法更加复杂，速度慢，加密和解密钥匙不相同，任何人都可以知道公钥，只有一个人持有私钥可以解密。
 *
 */
public class RSAUtil {

    /**
     * 根据publicKey 对data进行加密.
     *
     * @param publicKey
     * @param data
     * @throws Exception
     * @author Peter.Qiu
     */
    public static byte[] encryptMode(PublicKey publicKey, byte[] data)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA",
                    new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);// ENCRYPT_MODE 加密
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
                    : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i
                            * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i
                            * blockSize, raw, i * outputSize);
                }
                i++;
            }
            return raw;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据privateKey 对data进行解密.
     *
     * @param privateKey
     * @param data
     * @throws Exception
     * @author Peter.Qiu
     */
    public static byte[] decryptMode(PrivateKey privateKey, byte[] data)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);// DECRYPT_MODE 解密
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
        int j = 0;

        while (data.length - j * blockSize > 0) {
            bout.write(cipher.doFinal(data, j * blockSize, blockSize));
            j++;
        }
        return bout.toByteArray();
    }

    /**
     * 获取密钥.
     *
     * @param rsaKeyStore
     * @return
     * @throws Exception
     * @author Peter.Qiu
     */
    public static KeyPair getKeyPair(String rsaKeyStore) throws Exception {
        FileInputStream fis = new FileInputStream(rsaKeyStore);
        ObjectInputStream oos = new ObjectInputStream(fis);
        KeyPair kp = (KeyPair) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }

    /**
     * 将密钥写入文件.
     *
     * @param kp
     * @param path
     * @throws Exception
     * @author Peter.Qiu
     */
    public static void saveKeyPair(KeyPair kp, String path) throws Exception {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // 生成密钥
        oos.writeObject(kp);
        oos.close();
        fos.close();
    }

    /**
     * 用于生成公匙或私匙.
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @author Peter.Qiu
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {

        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA",
                new BouncyCastleProvider());
        // 注意密钥大小最好为1024,否则解密会有乱码情况.
        kg.initialize(1024, sr);
        KeyPair genKeyPair = kg.genKeyPair();
        return genKeyPair;

    }

    /**
     * 将公密或私密写入文件.
     *
     * @param obj
     * @param path
     * @throws Exception
     * @author Peter.Qiu
     */
    public static void saveFile(Object obj, String path) throws Exception {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // 生成密钥
        oos.writeObject(obj);
        oos.close();
        fos.close();
    }

    /**
     * 获取公密.
     *
     * @param publicKeyPath
     * @return
     * @throws Exception
     * @author Peter.Qiu
     */
    public static PublicKey getPublicKey(String publicKeyPath) throws Exception {
        FileInputStream fis = new FileInputStream(publicKeyPath);
        ObjectInputStream oos = new ObjectInputStream(fis);
        PublicKey kp = (PublicKey) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }

    /**
     * 获取私密.
     *
     * @param privateKeyPath
     * @return
     * @throws Exception
     * @author Peter.Qiu
     */
    public static PrivateKey getPrivateKey(String privateKeyPath)
            throws Exception {
        FileInputStream fis = new FileInputStream(privateKeyPath);
        ObjectInputStream oos = new ObjectInputStream(fis);
        PrivateKey kp = (PrivateKey) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }

    public static void main(String[] args) throws Exception {
        File dir = new File("./key/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 生成公钥及私钥
        KeyPair generateKeyPair = generateKeyPair = generateKeyPair();
        // 获取公匙及私匙
        //KeyPair generateKeyPair = getKeyPair("./key/key");

        // 存储KeyPair到本地用于后期解密 注意修改前台RSAKeyPair
        saveKeyPair(generateKeyPair,"./key/key");
        PublicKey publicKey = generateKeyPair.getPublic();
        PrivateKey privateKey = generateKeyPair.getPrivate();
        saveFile(publicKey,"./key/publicKey.key");
        saveFile(privateKey,"./key/privateKey.key");

        System.out.println("publicKey to String:"+encryptBASE64(publicKey.getEncoded()));
        System.out.println("privateKey to String:"+encryptBASE64(privateKey.getEncoded()));

        // 测试加密解密
        String test = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCM6Qga1DJWVzgkeiNsTokCgEyXSvxWB45i3nCyIMaV9Ig5MMjQtUr+lT+UkZZX+dw8y09kEprewSYBgx8UeZ/OdKy+6vSZAyYyVviQhSgzPe4EV3D+dbT4w6iKBoq3XJz9Mdlx9UmtYqQ7dzS8o7OE1ZGcKOI4VwisMPYkyeEIM2KCBGpurrpB4OpBuNgPCQJRiceVTbawamYqFVZp2sNzUrz/L9xmz9nUvyT9owTPGnmrG/CYhHwgQ2PbISobGakuWcgFYgFhCUndMD3kbDrhVmbHI1/wAH/OPU1ElfUbObfVsVxPdOMQQXtijwl+qH6x0DLWVFGD9mb4jbakoJblAgMBAAECggEAEDWiZAuy+rGv2KpgNs5V/n6p0OGkY3UdEQyLpEIW9hsaLJILF88zIFQk6z9QLzCkAC5XJKjrPeK0L4pY4e8XzhUn5hJ7LrrpAvN0RpO6yyI9piw06GC7/73qPm6AOJWjzZCVdgGEhziIsfqFi4m9VfOHe5JMa7S/X6eGOTcr9thdT279/6ewNX8eDKtbfAKLDLRBSS69++LiDZJF4/+H+J/qv83IdylheVY+2K7TGgxklS+GuBlxB6TVHPiLItV3WMR/0jxGwFapHi9l1Dqsu8mRo82P/DRCKa/s/QE6TYbaS7PutW9OiCDCpAsqHy0zIWnQ1htQIF+eQ/TxCmNxhQKBgQD0PXtkfaAl7kjGmYzJnpvMCC2tWiafJ4d0K9IkDP37UeBaRg+MX74VT1Df8i4MchTu4N1vI0krLuPjOQHXXFsyraaB6U7td2p0K+nCOPmqPm9ORy19jO14NxYbZOR13j/9sanRvczOYLjrvK4KwcvnV//5B7gtYUc4w9nliAGJ1wKBgQCTseZSWzdmaSVQHRJHKx6ztuTYv2EeTgKPo5Y9QO+G5Pk/yocokl2A7ELmlFkH9C4qvZvJw7v5VN78AT80ehAfbf7OwrshVqzx+9Osv/lolevEaQhl4CD5MdK4rzrOxfOnpj7x2luCa03s98FXfTc+zgPmRq0LMBsbeeY1cVxlowKBgA3SkPIt/etHogzDUelzNYQFjng6Gud0+ECF3l218oXPq2/QeT7LiFlyZLRX421G45JlXYLVr2VnQPRyHH2Qt4SVuU+u4Y2k7Xxno9IfIVoWMHxwoM1zNVlRXMnuGtPk4SA9dsvs/vNLHAwTYJIxR5XvMAhq/GxtDffTdbh8ywF/AoGAQZSAnJj8JHETrFr8PrWa0Fmpi8SOrxIceQM+Nryant8U/YpEMXOsKQ6/Nfsw/SMjTL8caZ6L3M4epO84zWhVuF7zAYPd65O8/0W+W7pyWEoZggDRRcsbWQsScgb8qcujwRFEacd3UpWq6Emqg9qJqU141csUEx+tKaoa2igXytkCgYBnKf35JPc5056p1N/zPGzFZkAxS5FaSxrrc7BT73SfU/9YswWqBSHMavFGCYMhXcZdZ4868cJG4/Ilk/R0xHGNFqRWxjBR70Y2hpBqgTJ1FJW12kb5OjBnT7rujdSKy00x5JwjSoiUVFfkCKqrp0ZBUpKujb4cs/BYBsJVIjD6wQ==";

        System.out.println("加密前字符：" + test);
        byte[] en_test = encryptMode(publicKey, test.getBytes("UTF-8"));
        System.out.println("after:" + new String(en_test));

        byte[] de_test = decryptMode(privateKey, en_test);
        System.out.println("解密后字符:" + new String(de_test, "UTF-8"));



    }
    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }


}
