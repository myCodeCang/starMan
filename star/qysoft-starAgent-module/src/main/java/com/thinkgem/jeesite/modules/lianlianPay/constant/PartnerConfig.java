package com.thinkgem.jeesite.modules.lianlianPay.constant;

/**
* 商户配置信息
* @author guoyx e-mail:guoyx@lianlian.com
* @date:2013-6-25 下午01:45:40
* @version :1.0
*
*/
public interface PartnerConfig {
    // 银通公钥
    String YT_PUB_KEY     = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbz+7E5C26vcMZGAKTM3KOWS/uqlCK05NtxpKUz81Jrw9dYfM+cS0SaldfLHUmfVsj62x3SYEcpPoa2jmSs3aepnHq2s2YJWRTw2arLtFeApsmAhAa3SosK5BQi/CvNhenKXkPxBuhW8ECdIebJDdt8JTEvUw9llKU0Kip+GyDGwIDAQAB";
    // 商户私钥
    String TRADER_PRI_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANvP7sTkLbq9wxkYApMzco5ZL+6qUIrTk23GkpTPzUmvD11h8z5xLRJqV18sdSZ9WyPrbHdJgRyk+hraOZKzdp6mcerazZglZFPDZqsu0V4CmyYCEBrdKiwrkFCL8K82F6cpeQ/EG6FbwQJ0h5skN23wlMS9TD2WUpTQqKn4bIMbAgMBAAECgYACTeCz4ax1C64y4iSMxHpL8bVl7BUiD2u5N96rfq4pOpOPqAozul39lm3KCp5DxAajjsE5UuTulvAkPetilU0GAcAQSvO3DvwbovCVbW7big2vm2v5pqGTo9UC0yY6JcqLZ56Gnjg/CV+JktR9ThO2Th0lkG40igGOeL5pPVO4uQJBAPinj3n26EtS7JLSnKmU9WG0bq9iq3N8/oQX4dSCNWOsBoHYsoRLT0+uPiX0xFW1xr8OXDI3NX+vk3RwMdxa2c0CQQDiTkDqVn8mBf4WyNWASRaumtM6/Ai2Wrmcy0kFSvcFNqPyjUV+1u6yDfFCQM8klCR2qqyFNLeFRgqQbuFKSkiHAkEA6gZCOicREDuWnsUMX2rr265G8liqRwKgRNB5Ylm+R/XTFghOU8bReYvjwDNzSbvQt2abjUN63Zbw8AjcVKlJDQJAL/MgsgZNvoK6iZ1YD2xtD+XiJ7dLKUgj1+MCv8pYeJRRPI1OUke8H6HwoncU7M0uxK35C+hVkjl694tm1NTUdQJBAN+CJCjYp+Qb65GmbCaInY+Bvx+OzPNvKnNAQae/CFkQlmh4/fzLCvgpaQTIJX3mEmKQmriY5TuXYB4TGQMQ1xY=";
    // MD5 KEY
    String MD5_KEY        = "201408071000001546_test_20140815";
    // 接收异步通知地址
    String NOTIFY_URL     = "http://ip:port/wepdemo/notify.htm";
    // 支付结束后返回地址
    String URL_RETURN     = "http://ip:port/wepdemo/urlReturn.jsp";
    // 商户编号
    String OID_PARTNER    = "201710270001075186";
    // 签名方式 RSA或MD5
    String SIGN_TYPE      = "RSA";
    // 接口版本号，固定1.0
    String VERSION        = "1.0";

    // 业务类型，连连支付根据商户业务为商户开设的业务类型； （101001：虚拟商品销售、109001：实物商品销售、108001：外部账户充值）

    String BUSI_PARTNER   = "101001";


}
