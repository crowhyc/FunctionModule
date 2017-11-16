package com.javanewb.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.util.DerValue;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * <p>
 * Title: SHA256WithRSAUtil
 * </p>
 * <p>
 * Description: com.javanewb.common.util
 * </p>
 *
 * @author Dean.Hwang
 * date 2017/6/23 下午1:14
 */
public class SHA256WithRSAUtil {

    /**
     * 验签
     *
     * @param publicKeyInput 公钥
     * @param signParam      签名参数
     * @param sign           签名
     * @return
     */
    public static boolean verify(InputStream publicKeyInput, String signParam, String sign) {

        try {
            byte bytes[] = new byte[publicKeyInput.available()];
            publicKeyInput.read(bytes);

            DerValue derValue = new DerValue(Base64.decodeBase64(bytes));
            PublicKey publicKey = RSAPublicKeyImpl.parse(derValue);

            // 确定SHA256withRSA签名方式
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(signParam.getBytes("UTF-8"));

            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(publicKeyInput);
        }

        return false;
    }

    /**
     * @param privateKeyInput 私钥
     * @param content         待签名的内容
     * @return
     */
    public static String sign(InputStream privateKeyInput, String content) {
        try {
            byte[] bytes = new byte[privateKeyInput.available()];
            privateKeyInput.read(bytes);

            // BASE64解码私钥
            DerValue derValue = new DerValue(Base64.decodeBase64(bytes));
            PrivateKey rsaPrivateKey = RSAPrivateKeyImpl.parseKey(derValue);

            // 确定SHA256withRSA签名方式
            Signature signature = Signature.getInstance("SHA256withRSA");
            // 注入私钥
            signature.initSign(rsaPrivateKey);
            // 给定需要被签名的串
            signature.update(content.getBytes("UTF-8"));

            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(privateKeyInput);
        }

        return "";
    }
}
