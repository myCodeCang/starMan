package com.thinkgem.jeesite.modules.lianlianPay.until;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtil{

    /**
     * 签名验证
     * 	
     * @param reqObj
     * @return
     */
    public static  boolean checkSign(JSONObject reqObj, String rsa_public,
                                     String md5_key)
    {
        if (reqObj == null)
        {
            return false;
        }
        String sign_type = reqObj.getString("sign_type");
        if (SignTypeEnum.MD5.getCode().equals(sign_type))
        {
            return checkSignMD5(reqObj, md5_key);
        } else
        {
            return checkSignRSA(reqObj, rsa_public);
        }
    }

    /**
     * MD5签名验证
     * 
     * @param signSrc
     * @param sign
     * @return
     */
    private static boolean checkSignMD5(JSONObject reqObj, String md5_key)
    {
       // System.out.println("进入MD5签名验证");
        if (reqObj == null)
        {
            return false;
        }
        String sign = reqObj.getString("sign");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        sign_src += "&key=" + md5_key;
//        System.out.println("返回报文待签名原串" + sign_src);
//        System.out.println("返回报文签名串" + sign);
        try
        {
            if (sign.equalsIgnoreCase(Md5Algorithm.getInstance().md5Digest(
                    sign_src.getBytes("utf-8"))))
            {
//                System.out.println("MD5签名验证通过");
                return true;
            } else
            {
//                System.out.println("MD5签名验证未通过");
                return false;
            }
        } catch (UnsupportedEncodingException e)
        {
//            System.out.println("MD5签名验证异常" + e.getMessage());
            return false;
        }
    }

    /**
     * RSA签名验证
     * 
     * @param reqObj
     * @return
     */
    private static boolean checkSignRSA(JSONObject reqObj, String rsa_public)
    {

        System.out.println("进入RSA签名验证");
        if (reqObj == null)
        {
            return false;
        }
        String sign = reqObj.getString("sign");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("返回报文待签名原串" + sign_src);
        System.out.println("返回报文签名串" + sign);
        try
        {
            if (TraderRSAUtil.checksign(rsa_public, sign_src, sign))
            {
                System.out.println("RSA签名验证通过");
                return true;
            } else
            {
                System.out.println("RSA签名验证未通过");
                return false;
            }
        } catch (Exception e)
        {
            System.out.println("RSA签名验证异常" + e.getMessage());
            return false;
        }
    }

    /**
     * 加签
     * @param reqObj
     * @param rsa_private
     * @param md5_key
     * @return
     */
    public String addSign(JSONObject reqObj, String rsa_private, String md5_key)
    {
        if (reqObj == null)
        {
            return "";
        }
        String sign_type = reqObj.getString("sign_type");
        if (SignTypeEnum.MD5.getCode().equals(sign_type))
        {
            return addSignMD5(reqObj, md5_key);
        } else
        {
            return addSignRSA(reqObj, rsa_private);
        }
    }

    /**
     * RSA加签名
     * 
     * @param reqObj
     * @param rsa_private
     * @return
     */
    private String addSignRSA(JSONObject reqObj, String rsa_private)
    {
        System.out.println("进入RSA加签名");
        if (reqObj == null)
        {
            return "";
        }
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_trader") + "]待签名原串:"
                + sign_src);
        try
        {
            return TraderRSAUtil.sign(rsa_private, sign_src);
        } catch (Exception e)
        {
            System.out.println("RSA加签名异常" + e.getMessage());
            return "";
        }
    }

    /**
     * MD5加签名
     * 
     * @param reqObj
     * @param md5_key
     * @return
     */
    private String addSignMD5(JSONObject reqObj, String md5_key)
    {

        if (reqObj == null)
        {
            return "";
        }
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        sign_src += "&key=" + md5_key;
        System.out.println("商户[" + reqObj.getString("oid_trader") + "]待签名原串:"
                + sign_src);
        try
        {
            return Md5Algorithm.getInstance().md5Digest(
                    sign_src.getBytes("utf-8"));
        } catch (Exception e)
        {
            System.out.println("MD5加签名异常" + e.getMessage());
            return "";
        }
    }

    /**
     * 
     * 
     * @param paramMap
     * @return
     */
    public static String genSignData(JSONObject jsonObject)
    {
        StringBuffer content = new StringBuffer();

        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);

            if ("sign".equals(key))
            {
                continue;
            }
            String value = (String) jsonObject.getString(key);

            if (FuncUtils.isNull(value))
            {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

}
