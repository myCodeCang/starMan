package com.thinkgem.jeesite.modules.lianlianPay.until;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA签名公共类
 * @author shmily
 */
public class TraderRSAUtil{

    private static TraderRSAUtil instance;

    /**
     * 加密算法RSA
     */
    public static final String   KEY_ALGORITHM = "RSA";

    private TraderRSAUtil()
    {

    }

    public static TraderRSAUtil getInstance()
    {
        if (null == instance)
            return new TraderRSAUtil();
        return instance;
    }

    /**
     * 
     * 公钥、私钥文件生成
     * @param keyPath：保存文件的路径
     * @param keyFlag：文件名前缀
     */
    private void generateKeyPair(String key_path, String name_prefix)
    {
        java.security.KeyPairGenerator keygen = null;
        try
        {
            keygen = java.security.KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e1)
        {
            System.out.println("签名失败");
        }
        SecureRandom secrand = new SecureRandom();
        secrand.setSeed("3500".getBytes()); // 初始化随机产生器
        keygen.initialize(1024, secrand);
        KeyPair keys = keygen.genKeyPair();
        PublicKey pubkey = keys.getPublic();
        PrivateKey prikey = keys.getPrivate();

        String pubKeyStr = new String(org.apache.commons.codec.binary.Base64
                .encodeBase64(pubkey.getEncoded()));
        String priKeyStr = new String(org.apache.commons.codec.binary.Base64
                .encodeBase64(org.apache.commons.codec.binary.Base64
                        .encodeBase64(prikey.getEncoded())));
        File file = new File(key_path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            // 保存私钥
            FileOutputStream fos = new FileOutputStream(new File(key_path
                    + name_prefix + "_RSAKey_private.txt"));
            fos.write(priKeyStr.getBytes());
            fos.close();
            // 保存公钥
            fos = new FileOutputStream(new File(key_path + name_prefix
                    + "_RSAKey_public.txt"));
            fos.write(pubKeyStr.getBytes());
            fos.close();
        } catch (IOException e)
        {
        	 System.out.println("签名异常");
        }
    }

    /**
     * 读取密钥文件内容
     * @param key_file:文件路径
     * @return
     */
    private static String getKeyContent(String key_file)
    {
        File file = new File(key_file);
        BufferedReader br = null;
        InputStream ins = null;
        StringBuffer sReturnBuf = new StringBuffer();
        try
        {
            ins = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            String readStr = null;
            readStr = br.readLine();
            while (readStr != null)
            {
                sReturnBuf.append(readStr);
                readStr = br.readLine();
            }
        } catch (IOException e)
        {
            return null;
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                    br = null;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (ins != null)
            {
                try
                {
                    ins.close();
                    ins = null;
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return sReturnBuf.toString();
    }

    /**
     * 签名处理
     * @param prikeyvalue：私钥文件
     * @param sign_str：签名源内容
     * @return
     */
    public static String sign(String prikeyvalue, String sign_str)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64
                    .getBytesBASE64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature
                    .getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(sign_str.getBytes("UTF-8"));
            byte[] signed = signet.sign(); // 对信息的数字签名
            // return Base64.getBASE64(signed);
            return new String(org.apache.commons.codec.binary.Base64
                    .encodeBase64(signed));
        } catch (Exception e)
        {
            System.out.println("签名失败," + e.getMessage());
        }
        return null;
    }

    /**
     * 签名验证
     * @param pubkeyvalue：公钥
     * @param oid_str：源串
     * @param signed_str：签名结果串
     * @return
     */
    public static boolean checksign(String pubkeyvalue, String oid_str,
            String signed_str)
    {
        try
        {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64
                    .getBytesBASE64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签名
            java.security.Signature signetcheck = java.security.Signature
                    .getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oid_str.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (Exception e)
        {
            System.out.println("签名验证异常," + e.getMessage());
        }
        return false;
    }

    /** 
     * 得到公钥 
     * @param key 密钥字符串（经过base64编码） 
     * @throws Exception 
     */
    public static PublicKey getPublicKey(String key) throws Exception
    {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /** 
     * 得到私钥 
     * @param key 密钥字符串（经过base64编码） 
     * @throws Exception 
     */
    public static PrivateKey getPrivateKey(String key) throws Exception
    {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static void main(String[] args)
    {
        String text = "123";
//        String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=";
//        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmRl6Zn4MmtoBoelHRT6j6ounts/x1+GiJTB9/eBTl01cBK50hmOUtGBcOVrJCa0C1NkR8BYgOT/WLfFT8cICw6XSJtf2uzZco71jbwXfFe8MiEx/LXiQNQHuclpkUa1hXFUUo6Qat8X8L++pVZfjav40dPKf7oFWCYLWBCDOdyQIDAQAB";
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJRjYf1rMmUmJ0HB3fbealJIXDobkJqM6wGPQzOPu34aWyXp5mHnuHIURFuEQd5vefr7M/Q6gqutf3SDqCyveNP15oSpLlY3z6hyaaMhybB3DGMtm1wndnyPvqxEXV7WJtGI9pRW6tWhAHJHIrrclWXACB0KGbBcBfJeqEug5mUQIDAQAB";
        String pri = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMlGNh/WsyZSYnQcHd9t5qUkhcOhuQmozrAY9DM4+7fhpbJenmYee4chREW4RB3m95+vsz9DqCq61/dIOoLK940/XmhKkuVjfPqHJpoyHJsHcMYy2bXCd2fI++rERdXtYm0Yj2lFbq1aEAckciutyVZcAIHQoZsFwF8l6oS6DmZRAgMBAAECgYAApq1+JN+nfBS9c2nVUzGvzxJvs5I5qcYhY7NGhySpT52NmijBA9A6e60Q3Ku7vQeICLV3uuxMVxZjwmQOEEIEvXqauyYUYTPgqGGcwYXQFVI7raHa0fNMfVWLMHgtTScoKVXRoU3re6HaXB2z5nUR//NE2OLdGCv0ApaJWEJMwQJBAPWoD/Cm/2LpZdfh7oXkCH+JQ9LoSWGpBDEKkTTzIqU9USNHOKjth9vWagsR55aAn2ImG+EPS+wa9xFTVDk/+WUCQQDRv8B/lYZD43KPi8AJuQxUzibDhpzqUrAcu5Xr3KMvcM4Us7QVzXqP7sFc7FJjZSTWgn3mQqJg1X0pqpdkQSB9AkBFs2jKbGe8BeM6rMVDwh7TKPxQhE4F4rHoxEnND0t+PPafnt6pt7O7oYu3Fl5yao5Oh+eTJQbyt/fwN4eHMuqtAkBx/ob+UCNyjhDbFxa9sgaTqJ7EsUpix6HTW9f1IirGQ8ac1bXQC6bKxvXsLLvyLSxCMRV/qUNa4Wxu0roI0KR5AkAZqsY48Uf/XsacJqRgIvwODstC03fgbml890R0LIdhnwAvE4sGnC9LKySRKmEMo8PuDhI0dTzaV0AbvXnsfDfp";
        String sign = sign(pri, text);
        System.out.println("sign:" + sign);
        boolean istrue = checksign(pub, text, sign);
        System.out.println("result:" + istrue);
    }
}
