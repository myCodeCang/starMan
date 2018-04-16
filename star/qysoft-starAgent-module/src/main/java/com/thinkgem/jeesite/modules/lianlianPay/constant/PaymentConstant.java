package com.thinkgem.jeesite.modules.lianlianPay.constant;

public class PaymentConstant {

	/** 连连银通公钥（不需替换，这是连连公钥，用于报文加密和连连返回响应报文时验签，不是商户生成的公钥. */
	public static final String PUBLIC_KEY_ONLINE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";

	/** 商户私钥 商户通过openssl工具生成公私钥，公钥通过商户站上传，私钥用于加签，替换下面的值 . */
	public static final String BUSINESS_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANvP7sTkLbq9wxkYApMzco5ZL" +
			"+6qUIrTk23GkpTPzUmvD11h8z5xLRJqV18sdSZ9WyPrbHdJgRyk" +
			"+hraOZKzdp6mcerazZglZFPDZqsu0V4CmyYCEBrdKiwrkFCL8K82F6cpeQ/EG6FbwQJ0h5skN23wlMS9TD2WUpTQqKn4bIMbAgMBAAECgYACTeCz4" +
			"ax1C64y4iSMxHpL8bVl7BUiD2u5N96rfq4pOpOPqAozul39lm3KCp5DxAajjsE5UuTulvAkPetilU0GAcAQSvO3DvwbovCVbW7big2vm2v5pqGTo9" +
			"UC0yY6JcqLZ56Gnjg/CV" +
			"+JktR9ThO2Th0lkG40igGOeL5pPVO4uQJBAPinj3n26EtS7JLSnKmU9WG0bq9iq3N8/oQX4dSCNWOsBoHYsoRLT0+uPiX0xFW1xr8OXDI3NX" +
			"+vk3RwMdxa2c0CQQDiTkDqVn8mBf4WyNWASRaumtM6/Ai2Wrmcy0kFSvcFNqPyjUV" +
			"+1u6yDfFCQM8klCR2qqyFNLeFRgqQbuFKSkiHAkEA6gZCOicREDuWnsUMX2rr265G8liqRwKgRNB5Ylm" +
			"+R/XTFghOU8bReYvjwDNzSbvQt2abjUN63Zbw8AjcVKlJDQJAL/MgsgZNvoK6iZ1YD2xtD" +
			"+XiJ7dLKUgj1+MCv8pYeJRRPI1OUke8H6HwoncU7M0uxK35C+hVkjl694tm1NTUdQJBAN+CJCjYp+Qb65GmbCaInY+Bvx" +
			"+OzPNvKnNAQae/CFkQlmh4/fzLCvgpaQTIJX3mEmKQmriY5TuXYB4TGQMQ1xY=";

	/** 商户号（商户需要替换自己的商户号）. */
	public static final String OID_PARTNER = "201710270001075186";

	/** 实时付款api版本. */
	public static final String API_VERSION = "1.0";

	/** 实时付款签名方式. */
	public static final String SIGN_TYPE = "RSA";

}
