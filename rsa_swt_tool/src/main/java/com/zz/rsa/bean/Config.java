package com.zz.rsa.bean;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Config
{
  public static String charset = System.getProperty("file.encoding");
  public static final String MAC = "mac";
  public static final String WINDOW = "window";
  public static final String RSA_1024 = "1024";
  public static final String RSA_2048 = "2048";
  public static final String KEY_FILE_SUFFIX = ".txt";
  public static final String RSA_PRIVATE_KEY_FILE_NAME = "merchant-privateKey";
  public static final String RSA_PUBLIC_KEY_FILE_NAME = "merchant-publicKey";
  public static final String RSA_PRIVATE_KEY_TMP_FILE_NAME = "merchant_privateKey_tmp.txt";
  public static final String SIGN_STEP_FILE_NAME = "sign_step";
  public static final String CHECK_SIGN_STEP_FILE_NAME = "check_sign_step";
  public static final String KEY_SAVE_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "RSA_KEY/";

  public static String CURRENT_SIGN_TYPE = "RSA";

  public static String SIGN_TYPE_RSA = "RSA(SHA1withRSA)";

  public static String SIGN_TYPE_RSA2 = "RSA2(SHA256withRSA)";

  public static String DEVLEPMENT_LANGUAGE_JAVA = "JAVA";

  public static String DEVLEPMENT_LANGUAGE_OTHER = "OTHER";

  public static String RSA_SHA1 = "SHA1withRSA";

  public static String RSA_SHA256 = "SHA256withRSA";

  public static void main(String[] args) throws UnsupportedEncodingException {
    String gbk = "notify_time=2016-12-05+14%3A32%3A39&memo=%BC%C6%BB%AE%D3%D0%B1%E4%A3%AC%C3%BB%CA%B1%BC%E4%CF%FB%B7%D1&order_operator_type=REFUND&sign_type=RSA&charset=gbk&notify_type=koubei_trade_ext_notify&order_principal_id=2088811691258281&order_no=20161205001040030100500000018612&version=1.0&out_biz_no=4fd2647d4e111b3d185b71538a5f4f36&sign=ixwKH5CkS06dgGVnoLNRVnDSgaKtYGvUGWQWobMFCk5WsYw49npJKB1O2xm6w9Zjz5c7%2b84rU6s9oW%2b6fkA3nW9g2MFvAPbv1yU27gfjzOxkqnMUIa3iPAbTGMv50Mdh92Zdgvfmk%2f9mh966HK1XW4Jd2FWqDXQN5n8FwTaRy7M%3d&amount=0.01&vouchers=%5B%7B%22voucher_id%22%3A%2220161205000730025032003Y16I2%22%2C%22item_id%22%3A%222016120520076004000010959041%22%7D%5D&gmt_create=2016-12-05+14%3A32%3A39&trans_no=2016120521001008500229615963&app_id=2016101302147254&notify_id=43bb638c74e9c1d4f7ee620ee301004jmm";
    gbk = URLDecoder.decode(gbk, "GBK");
    System.out.println(gbk);
    String utf8 = new String(gbk.getBytes("GBK"), "UTF-8");
    System.out.println(utf8);
  }
}