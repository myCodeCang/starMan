package com.thinkgem.jeesite.modules.lianlianPay.until;


import com.thinkgem.jeesite.modules.lianlianPay.entity.ApiBaseBean;

import java.text.SimpleDateFormat;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 公共处理组件
 * 
 * @author shmily
 * @date May 25, 2011 2:35:38 PM
 */
public class FuncUtils{

    /***************************************************************************
     * 方法名:getSysDate 功能描述:取系统日期 参数说明: 返回参数:返回系统日期串 编写:yajs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String getSysDate()
    {
        SimpleDateFormat formdate = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();
        String ss;
        ss = formdate.format(curDate);
        return ss;
    }

    /***************************************************************************
     * 方法名:getSysTime 功能描述:取系统时间 参数说明: 返回参数:返回系统时间串 编写:yaojs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String getSysTime()
    {
        SimpleDateFormat formdate = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date();
        String ss;
        ss = formdate.format(curDate);
        return ss;
    }

    /***************************************************************************
     * 方法名:WriteFile 功能描述:写文件 参数说明: 返回参数:返回是否写成功 编写:yaojs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static boolean WriteFile(String fileName, String StrBuf)
            throws IOException
    {
        File file = new File(fileName);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(StrBuf, 0, StrBuf.length());
        bw.newLine();
        bw.close();
        fw.close();
        return true;
    }

    /***************************************************************************
     * 方法名:FormatStringAddBlank 功能描述:给字符串后补空格 参数说明: 返回参数:返回后补的字符串 编写:yaojs
     * 日期:2006.12.13 修改记录:
     * 
     * @throws UnsupportedEncodingException
     **************************************************************************/
    public static String FormatStringAddBlank(String sReturnBuf, int length)
            throws Exception
    {
        StringBuffer tempBuffer = new StringBuffer();
        if (null == sReturnBuf || sReturnBuf.equals("")
                || sReturnBuf.equals("null"))
        {
            for (int i = 0; i < length; i++)
            {
                tempBuffer.append(" ");
            }
            return tempBuffer.toString();
        }
        String s2 = new String(sReturnBuf.getBytes("GB2312"), "ISO8859_1");
        int iLength = s2.length();
        if (length > iLength)
        {
            tempBuffer.append(sReturnBuf);
            for (int j = 0; j < length - iLength; j++)
            {
                tempBuffer.append(" ");
            }
            sReturnBuf = tempBuffer.toString();
        } else if (length < iLength)
        {
            sReturnBuf = absoluteSubstring(sReturnBuf, 0, length);
        }
        return sReturnBuf;
    }

    /***************************************************************************
     * 方法名:getStringLength 功能描述:获取字符的长度,包括汉字的长度 参数说明: 返回参数:返回长度 编写:yaojs
     * 日期:2006.12.13 修改记录:
     * 
     * @throws UnsupportedEncodingException
     **************************************************************************/
    public static int getStringLength(String s1) throws Exception
    {
        if (null == s1 || s1.equals(""))
            return 0;
        String s2 = new String(s1.getBytes("GB2312"), "ISO8859_1");
        return s2.length();
    }

    /***************************************************************************
     * 方法名:FormatStringAddZero 功能描述:给字符串前补0 参数说明: 返回参数:返回前补的字符串 编写:yaojs
     * 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String FormatStringAddZero(String sReturnBuf, int length)
    {
        StringBuffer tempBuffer = new StringBuffer();
        if (null == sReturnBuf || sReturnBuf.equals("")
                || sReturnBuf.equals("null"))
        {
            for (int i = 0; i < length; i++)
            {
                tempBuffer.append("0");
            }
            return tempBuffer.toString();
        }
        int iLength = sReturnBuf.length();
        if (length > iLength)
        {
            for (int j = 0; j < length - iLength; j++)
            {
                sReturnBuf = "0" + sReturnBuf;
            }
        } else if (length < iLength)
        {
            sReturnBuf = absoluteSubstring(sReturnBuf, 0, length);
        }
        return sReturnBuf;
    }

    /***************************************************************************
     * 方法名:MultString 功能描述:将字符串金额变为*100后的金额 参数说明:sMoney：需要变更的字符串， iEr：需要乘的数
     * flag：+代表乘,-代表除 返回参数:返回前补的字符串 编写:yaojs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String MultString(String sMoney, int iEr, String flag)
    {
        if (null == sMoney || sMoney.equals(""))
            return "";
        if (null == flag || flag.equals(""))
            return "";
        sMoney = stringMoveZero(sMoney);
        double iTemp = new Double(sMoney).doubleValue();
        if (flag.equals("+"))
        {
            iTemp = iTemp * iEr;
            int aa = new Double(iTemp).intValue();
            return String.valueOf(aa);
        } else if (flag.equals("-"))
        {
            iTemp = iTemp / iEr;
        }
        return String.valueOf(iTemp);
    }

    /***************************************************************************
     * 方法名:MultStringExt 功能描述:将字符串金额变为*100后的金额 参数说明:sMoney：需要变更的字符串， iEr：需要乘的数
     * flag：+代表乘,-代表除 返回参数:返回前补的字符串 编写:yaojs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String MultStringExt(String sMoney, int iEr, String flag)
    {
        if (null == sMoney || sMoney.equals(""))
            return "";
        if (null == flag || flag.equals(""))
            return "";
        sMoney = stringMoveZero(sMoney);
        double iTemp = new Double(sMoney).doubleValue();
        if (flag.equals("+"))
        {
            iTemp = iTemp * iEr;
            double aa = new Double(iTemp).doubleValue();
            return String.valueOf(aa);
        } else if (flag.equals("-"))
        {
            iTemp = iTemp / iEr;
        }
        return String.valueOf(iTemp);
    }

    /***************************************************************************
     * 方法名:stringMoveZero 功能描述:将字符串金额中负号前的0去掉 参数说明:sMoney：需要变更的字符串， 返回参数:返回的字符串
     * 编写:yaojs 日期:2006.12.13 修改记录:
     **************************************************************************/
    public static String stringMoveZero(String sMoney)
    {
        if (null == sMoney || sMoney.equals(""))
            return "";
        int ilen = sMoney.indexOf("-");
        if (ilen > 0)
            return sMoney.substring(ilen, sMoney.length());
        return sMoney;
    }

    /**
     * 对一个字符串的绝对长度进行拆解(如果遇到汉字字符会把它当作两个字符处理)
     * 
     * @param s
     *            传入的字串
     * @param start
     *            起始绝对位置
     * @param end
     *            终止绝对位置
     * @return 返回的字串 编写:yaojs 日期:2006.12.13
     */
    public static String absoluteSubstring(String s, int start, int end)
    {
        if (s == null || s.equals(""))
        {
            return "";
        }
        try
        {
            String s2 = new String(s.getBytes("GB2312"), "ISO8859_1");
            s2 = s2.substring(start, end);
            return new String(s2.getBytes("ISO8859_1"), "GB2312");
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * 对返回的多条记录的字符串进行处理，
     * 
     * @param s
     *            传入的字串
     * @param
     * @return 返回字符数组 编写:yaojs 日期:2006.12.13
     * @throws Exception
     */
    public static String[] getAbsoluteSubstringArray(String s, int ilength)
            throws Exception
    {
        if (s == null || s.equals(""))
        {
            return new String[0];
        }
        try
        {
            String s2 = new String(s.getBytes("GB2312"), "ISO8859_1");
            int ilen = s2.length() / ilength;
            if (ilen == 0)
                return new String[0];
            int start = 0;
            int end = ilength;
            String[] returnarray = new String[ilen];
            for (int i = 0; i < ilen; i++)
            {
                String s1 = s2.substring(start, end);
                start = end;
                end = end + ilength;
                returnarray[i] = new String(s1.getBytes("ISO8859_1"), "GB2312");
            }
            return returnarray;
        } catch (Exception e)
        {
            return new String[0];
        }
    }

    /**
     * 对返回的多条记录的字符串进行处理，
     * 
     * @param s
     *            传入的字串
     * @param
     *
     * @param
     * @return 返回字符数组 编写:yaojs 日期:2006.12.13
     * @throws Exception
     */
    public static String[] getAbsoluteSubstringArrayExt(String s, int ilength,
            int extlen) throws Exception
    {
        if (s == null || s.equals(""))
        {
            return new String[0];
        }
        try
        {
            String s2 = new String(s.getBytes("GB2312"), "ISO8859_1");
            int zlen = getStringLength(s);
            int start = 0;
            int end = 0;
            int iRow = new Integer(s2.substring(start, 2)).intValue();
            String[] returnarray = new String[iRow];
            start = start + 2;
            for (int i = 0; i < iRow; i++)
            {
                if (start >= zlen)
                    break;
                int ilen = new Integer(s2.substring(start, start + ilength))
                        .intValue();
                end = start + extlen + ilen;
                String s1 = s2.substring(start, end);
                start = end;
                returnarray[i] = new String(s1.getBytes("ISO8859_1"), "GB2312");
            }
            return returnarray;
        } catch (Exception e)
        {
            return new String[0];
        }
    }

    /**
     * 去除字符串中的所有的空格
     * 
     * @param str
     * @return 去除后的字符串
     */
    public static String trimAllBlank(String str)
    {
        if (isNull(str))
        {
            return "";
        }
        return str.replace(" ", "");
    }

    /**
     * 对一个字符串按间隔分解
     * 
     * @param fieldsru
     *            传入的字串
     * @param tag
     *            间隔符
     * @return 返回的一个数组 编写:yaojs 日期:2006.12.13
     */
    public static String[] spiltStr(String fieldsru, String tag)
    {
        char dot = tag.charAt(0);
        String field;
        field = fieldsru + dot;
        int num = 0;
        int field_len = field.length();
        for (int i = 0; i < field_len; i++)
        {
            if (field.charAt(i) == dot)
            {
                num++;
            }
        }
        String[] returnarray = new String[num];
        int begin = 0;
        int end;
        for (int j = 0; j < num; j++)
        {
            end = field.indexOf(dot, begin);
            returnarray[j] = field.substring(begin, end);
            begin = end + 1;
        }
        return returnarray;
    }

    /**
     * 对一个字符串判断是否为手机号码
     * 
     * @param checkMobile
     *            传入的字串
     * @return ture or false 编写:yaojs 日期:2006.12.13
     */
    public static boolean checkMobile(String fieldsru)
    {
        if (FuncUtils.isNull(fieldsru))
            return false;
        return Pattern.matches("1[3458][0-9]{9}", fieldsru.trim());
    }

    /**
     * 解密时转换成byte算法时补位
     * 
     * @param strhex
     *            传入的字串符
     * @return byte 编写:yaojs 日期:2006.12.13
     */
    public static byte[] hex2byte(String strhex)
    {
        if (strhex == null)
        {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1)
        {
            return null;
        }

        byte[] b = new byte[l / 2];

        for (int i = 0; i != l / 2; i++)
        {

            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String strchange(String str)
    {
        if (null == str || str.equals("NULL") || str.equals("null"))
        {
            return "";
        } else
            return str;
    }

    public static boolean isNull(String str)
    {
        if (null == str || str.equalsIgnoreCase("NULL") || str.equals(""))
        {
            return true;
        } else
            return false;
    }

    public static String stringToThouMony(String sMoney)
    {
        if (null == sMoney || sMoney.equals(""))
            return "";
        double d = new Double(sMoney).doubleValue();
        DecimalFormat df = new DecimalFormat("##,###,###,###,##0.00");
        return df.format(d);
    }

    public static String FormatMoney(String sMoney)
    {
        if (null == sMoney || sMoney.equals(""))
            return "";
        double d = new Double(sMoney).doubleValue();
        DecimalFormat df = new DecimalFormat("##########0.00");
        return df.format(d);
    }

    /**
     * 对double类型进行造型,提供保留的小数位数,不进行四舍五入
     * 
     * @param val
     * @param precision
     * @return
     */
    public static double roundDouble(double val, int precision)
    {
        double factor = Math.pow(10, precision);
        return Math.floor(val * factor) / factor;
    }

    /**
     * 对平台手续费进行格式化 1.首先保留四位小数进行四舍五入 2.取结果,保留三位小数
     * 
     * @param fee
     * @param precision
     * @return
     */
    public static String formatFee(double fee, int precision)
    {
        DecimalFormat format = new DecimalFormat("#0.0000");
        return String.valueOf(roundDouble(Double
                .parseDouble(format.format(fee)), precision));
    }

    /**
     * 用户注册生成随机密码
     * 
     * @param index
     * @return
     */
    public static String getRandomNumber(int index)
    {
        // 生成随类
        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < index; i++)
        {
            String rand = null;
            if (random.nextInt(6) == i)
            {
                rand = String.valueOf((char) (65 + random.nextInt(26)));
            } else if (random.nextInt(6) == i + 1)
            {
                rand = String.valueOf((char) (97 + random.nextInt(26)));
            } else
            {
                rand = String.valueOf(random.nextInt(10));
            }
            sRand += rand;
        }
        return sRand;
    }



    /**
     * 里转元方法
     * 
     * @param money
     * @return
     */
    public static String formatLTYMoneny(String money)
    {
        if (money == null || money.trim().equals("") || money.equals("null"))
        {
            return "0";
        }
        try
        {
            double dmoney = Double.parseDouble(money);
            dmoney /= 1000;
            DecimalFormat format = new DecimalFormat("#0.00");
            return format.format(dmoney);
        } catch (Exception e)
        {
            return "0";
        }
    }

    /**
     * 
     * 功能描述：元转里方法
     * 
     * @param yuan
     * @return
     */
    public static String formatYTLMoneny(String yuan)
    {
        try
        {
            double dYuan = Double.parseDouble(yuan);
            double dLi = dYuan * 1000;
            DecimalFormat format = new DecimalFormat("#");
            return format.format(dLi);
        } catch (Exception e)
        {
            return "0";
        }

    }

    /**
     * 
     * 功能描述：元转分方法
     * 
     * @param yuan
     * @return
     */
    public static String formatYTFMoneny(String yuan)
    {
        try
        {
            double dYuan = Double.parseDouble(yuan);
            double dLi = dYuan * 100;
            DecimalFormat format = new DecimalFormat("#");
            return format.format(dLi);
        } catch (Exception e)
        {
            return "0";
        }

    }

    /**
     * 显示卡号后四位
     * 
     * @param cardNo
     * @return 卡号后四位
     */
    public static String hiddenCard(String cardNo)
    {
        if (isNull(cardNo))
        {
            return "";
        } else if (cardNo.length() <= 4)
        {
            return cardNo;
        } else
        {
            return cardNo.substring(cardNo.length() - 4, cardNo.length());
        }
    }

    /**
     * 隐藏身份证号的方法
     * 
     * @param idNo
     * @return 一前一后不隐藏 如：5*****************5
     */
    public static String hiddenCertifiID(String idNo)
    {
        if (FuncUtils.isNull(idNo) || idNo.length() < 2)
        {
            return idNo;
        }
        String head = idNo.substring(0, 1);
        String end = idNo.substring(idNo.length() - 1);
        String sbf = head;
        for (int i = 0; i < idNo.length() - 2; i++)
        {
            sbf += "*";
        }
        sbf += end;
        return sbf;
    }

    /**
     * 隐藏银行卡号的方法
     * 
     * @param idNo
     * @return 前六后四不隐藏 如：123456******7890
     */
    public static String hiddenBankCard(String idNo)
    {
        if (FuncUtils.isNull(idNo) || idNo.length() < 6)
        {
            return idNo;
        }
        String head = idNo.substring(0, 6);
        String end = idNo.substring(idNo.length() - 4);
        String sbf = head;
        for (int i = 0; i < idNo.length() - 10; i++)
        {
            sbf += "*";
        }
        sbf += end;
        return sbf;
    }

    /**
     * 功能描述：序列号处理-->微软格式 序列号头字母大于等于8的，添加00
     * 
     * @param numer
     * @return
     */
    public static String buildCertSerialnumber(String numer)
    {
        if (isNull(numer))
        {
            return numer;
        }
        String first = numer.substring(0, 1);
        if (Integer.valueOf(first, 16) >= 8)
        {
            return "00" + numer;
        }
        return numer;
    }

    /**
     * 功能描述：日期改变方法
     * 
     * @param date
     * @param day
     *            为改变日期的天数
     * @return
     */
    public static String changeDay(String date, int day)
    {
        if (date != null && date.trim().length() > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date newDate = sdf.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(newDate);
                cal.add(Calendar.DATE, day);
                Date nextDate = cal.getTime();
                String next_dateStr = new SimpleDateFormat("yyyy-MM-dd")
                        .format(nextDate);
                return next_dateStr;
            } catch (Exception e)
            {
                return date;
            }

        }
        return date;
    }

    /**
     * 功能描述：月份改变方法
     * 
     * @param date
     * @param month
     *            为改变日期的月份个数
     * @return
     */
    public static String changeMonth(String date, int month)
    {
        if (date != null && date.trim().length() > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date newDate = sdf.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(newDate);
                cal.add(Calendar.MONTH, month);
                Date nextDate = cal.getTime();
                String next_dateStr = new SimpleDateFormat("yyyy-MM-dd")
                        .format(nextDate);
                return next_dateStr;
            } catch (Exception e)
            {
                return date;
            }

        }
        return date;
    }

    /**
     * 功能描述：时间改变方法
     * 
     * @param date
     * @param day
     * 改变时间
     * @return
     */
    public static String changeMin(String date, int min)
    {
        if (date != null && date.trim().length() > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try
            {
                Date newDate = sdf.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(newDate);
                cal.add(Calendar.MINUTE, min);
                Date nextDate = cal.getTime();
                String next_dateStr = new SimpleDateFormat("yyyyMMddHHmmss")
                        .format(nextDate);
                return next_dateStr;
            } catch (Exception e)
            {
                return date;
            }

        }
        return date;
    }

    /**
     * 
     * 功能描述：将Bean对象中String类型为""的字段设置为NULL
     * 
     * @param baseBean
     * @return
     */
    public static ApiBaseBean clearBean(ApiBaseBean baseBean)
    {
        Field[] fields = baseBean.getClass().getFields();
        for (Field filed : fields)
        {
            if (filed != null)
            {
                // 得到属性的类名
                String value = "";
                // 得到属性值
                String className = filed.getType().getSimpleName();
                if (className.equalsIgnoreCase("String"))
                {
                    try
                    {
                        value = (String) filed.get(baseBean);
                        if (FuncUtils.isNull(value))
                        {
                            filed.set(baseBean, null);
                        }
                    } catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return baseBean;
    }

    /**
     * 
     * 功能描述：将Bean对象中String类型为""的字段设置为NULL
     * 
     * @param baseBean
     * @return
     */
    // public static traderOfflineBean clearBean(traderOfflineBean baseBean)
    // {
    // Field[] fields = baseBean.getClass().getFields();
    // for (Field filed : fields)
    // {
    // if (filed != null)
    // {
    // // 得到属性的类名
    // String value = "";
    // // 得到属性值
    // String className = filed.getType().getSimpleName();
    // if (className.equalsIgnoreCase("String")||className.equalsIgnoreCase("List"))
    // {
    // try
    // {
    // value = (String) filed.get(baseBean);
    // if (FuncUtils.isNull(value))
    // {
    // filed.set(baseBean, null);
    // }
    // } catch (IllegalArgumentException e)
    // {
    // e.printStackTrace();
    // } catch (IllegalAccessException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    // return baseBean;
    // }

    /**
     * 字符串前补0
     * 
     * @param sReturnBuf
     * @param length
     * @return
     */
    public static String formatStringAddZero(String sReturnBuf, int length)
    {
        StringBuffer tempBuffer = new StringBuffer();
        if (null == sReturnBuf || sReturnBuf.equals("")
                || sReturnBuf.equals("null"))
        {
            for (int i = 0; i < length; i++)
            {
                tempBuffer.append("0");
            }
            return tempBuffer.toString();
        }
        if (sReturnBuf.length() > length)
        {
            return sReturnBuf;
        }
        int iLength = sReturnBuf.length();
        if (length > iLength)
        {
            for (int j = 0; j < length - iLength; j++)
            {
                sReturnBuf = "0" + sReturnBuf;
            }
        } else if (length < iLength)
        {
            sReturnBuf = FuncUtils.absoluteSubstring(sReturnBuf, 0, length);
        }
        return sReturnBuf;
    }

    /**
     * 加密方法
     * 
     * @param str
     * @return
     */
    public static String encrytion(String str)
    {
        if (FuncUtils.isNull(str) || str.length() < 2)
        {
            return str;
        }
        int length = str.length();
        // 姓名加密
        if (length < 5)
        {
            return "*" + str.substring(1);
        }
        // 手机加密
        if (length == 11)
        {
            return str.substring(0, 3) + "****" + str.substring(7);
        }
        // 其他 末四位加密
        return str.substring(0, length - 4) + "**";
    }

    /**
     * 
     * 功能描述: 非注册用户
     * 
     * @param id
     * @return
     */
    public static boolean isMachineId(String id)
    {
        if (isNull(id))
        {
            return false;
        }
        if (id.length() != 32)
        {
            return false;
        }
        return true;
    }

    /**
     * 
     * 功能描述：手势码4-9位1-9数字
     * 
     * @param signs
     * @return
     */
    public static boolean isSigns(String signs)
    {
        if (FuncUtils.isNull(signs))
            return false;
        return Pattern.matches("[1-9]{4,9}", signs.trim());
    }

    /**
     * 流转字符串方法
     * 
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (null != reader)
                {
                    reader.close();
                }
            } catch (IOException e)
            {

            }
        }
        return sb.toString();
    }

    public static boolean isBankCard(String str)
    {
        if (str.matches("^\\d{15,19}$"))
        {// 从第二位开始，出现相同的数字
            if (isSameCharacter(str.substring(1)))
            {
                return false;
            }
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 校验信用卡有限期
     * 
     * @param exp_date
     * @return
     */
    public static boolean isCard_ExpDate(String exp_date)
    {
        if (isNull(exp_date))
        {
            return false;
        }
        if (!exp_date.matches("^\\d{4}$"))
        {
            return false;
        }
        String month = exp_date.substring(exp_date.length() - 2, exp_date
                .length());
        if (Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1)
        {
            return false;
        }
        if (Integer.parseInt(exp_date) < Integer.parseInt(DateUtil
                .getCurrentYearMonthStr()))
        {
            return false;
        }
        return true;
    }

    /**
     * 功能描述：金额校验，必须大于0.01
     * 
     * @param money
     * @return
     */
    public static boolean isMoney(String money)
    {
        if (FuncUtils.isNull(money))
        {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]{0,}[.]{0,1}[0-9]{0,2}$");
        if (!pattern.matcher(money).matches())
        {
            return false;
        }
        if (Double.parseDouble(money) < 0.01)
        {
            return false;
        }
        return true;
    }

    /**
     * 功能描述：比较金额是否相等
     * 
     * @param money1
     * @param money2
     * @return
     */
    public static boolean isMoneyEqual(String money1, String money2)
    {
        if (!isMoney(money1) || !isMoney(money2))
        {
            return false;
        }
        long long1 = Double.doubleToLongBits(Double.parseDouble(money1));
        long long2 = Double.doubleToLongBits(Double.parseDouble(money2));
        if (long1 == long2)
        {
            return true;
        }
        return false;
    }

    private static boolean isSameCharacter(String s)
    {
        // 如果不区分大小写，可以添加下面这句
        // s = s.toUpperCase();或者 s = s.toLowerCase();
        String character = s.substring(0, 1);
        String replace = "";
        String test = s.replace(character, replace);
        if ("".equals(test))
            return true;
        return false;
    }

    /**
     * 判断是否是纯数字字符串
     * @param str
     * @return
     */
    public static boolean isDigitStr(String str)
    {
        if (isNull(str) || !str.matches("^\\d*$"))
        {
            return false;
        }
        return true;
    }

    /**
     * 系统日期转unix时间戳
     * @param d
     * @return
     */
    public static String getUnixTime(String d)
    {
        Timestamp appointTime = Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date()));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try
        {
            date = df.parse(String.valueOf(appointTime));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        long s = date.getTime();
        String unixDate = String.valueOf(s).substring(0, 10);
        d = unixDate;
        return d;
    }

    /**
     * 比较金额 money1<=money2的115%
     * @param money1
     * @param money2
     * @return
     */
    public static boolean isMoneyEqual2(String money1, String money2)
    {
        if (!isMoney(money1) || !isMoney(money2))
        {
            return false;
        }
        long long1 = Double.doubleToLongBits(Double.parseDouble(money1));
        long long2 = Double.doubleToLongBits(Double.parseDouble(money2) * 1.15);
        if (long1 <= long2)
        {
            return true;
        }
        return false;
    }

    /**
     * 校验money1是否小于等于(money3-money2)
     * @param money1
     * @param money2
     * @param money3
     * @return
     */
    public static boolean isMoneyEqual3(String refundmoney,
            String isrefundmoney, String totalmoney)
    {
        String isrefundmoney_new = "0";
        String totalmoney_new = "0";
        if (!FuncUtils.isNull(isrefundmoney))
        {
            isrefundmoney_new = isrefundmoney;
        }
        if (!FuncUtils.isNull(totalmoney))
        {
            totalmoney_new = totalmoney;
        }

        long long1 = Double.doubleToLongBits(Double.parseDouble(refundmoney));
        long long2 = Double.doubleToLongBits(Double.parseDouble(totalmoney_new)
                - Double.parseDouble(isrefundmoney_new));
        if (long1 <= long2)
        {
            return true;
        }
        return false;
    }

    /**
     * 单位为里
     * @param amt
     * @param refund_amt
     * @return
     */
    public static String addAmt(String amt, String refund_amt)
    {

        String amt_new = "0";
        String refund_new = "0";
        if (!FuncUtils.isNull(amt))
        {
            amt_new = amt;
        }
        if (!FuncUtils.isNull(refund_amt))
        {
            refund_new = refund_amt;
        }

        try
        {
            double amtN = Double.parseDouble(amt_new);
            double refundN = Double.parseDouble(refund_new);
            double res = amtN + refundN;
            DecimalFormat format = new DecimalFormat("#0");
            return format.format(res);
        } catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }

    }

    public static void main(String[] args)
    {
        System.out.println(isMoneyEqual3("4991", "10", "5000"));
    }
}
