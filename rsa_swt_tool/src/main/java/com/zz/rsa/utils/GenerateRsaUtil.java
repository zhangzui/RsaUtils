package com.zz.rsa.utils;

import com.zz.rsa.bean.Config;
import com.zz.rsa.bean.Env;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateRsaUtil
{
  public static String mkRsaPrivateKeyFile(String privateKeyLength)
    throws Exception
  {
    String keyLength = StringUtils.isBlank(privateKeyLength) ? "1024" : 
      privateKeyLength;
    if (!SupportUtil.isExists(Config.KEY_SAVE_PATH)) {
      SupportUtil.mkDir(Config.KEY_SAVE_PATH);
    }

    if (Env.isMac())
      execCmd(new String[] { 
        "genrsa -out", Config.KEY_SAVE_PATH + "privateKey" + keyLength + ".txt", keyLength });
    else {
      execCmd(new String[] { 
        "genrsa -out", "\"" + Config.KEY_SAVE_PATH + "privateKey" + keyLength + ".txt" + "\"", keyLength });
    }

    return new File(Config.KEY_SAVE_PATH + "privateKey" + keyLength + ".txt").getAbsolutePath();
  }

  public static String mkRsaPrivateKey(String privateKeyLength)
    throws Exception
  {
    String keyLength = StringUtils.isBlank(privateKeyLength) ? "1024" : 
      privateKeyLength;
    return execCmd(new String[] { keyLength });
  }

  public static String mkRsaPublicKeyFile(String privateKeyFile, Integer keyLength)
    throws Exception
  {
    if (!isExistFile(privateKeyFile)) {
      return null;
    }

    if (Env.isMac())
      execCmd(new String[] { 
        "rsa -in", privateKeyFile, "-pubout -out", Config.KEY_SAVE_PATH + "publicKey" + keyLength + ".txt" });
    else {
      execCmd(new String[] { 
        "rsa -in", "\"" + privateKeyFile + "\"", "-pubout -out", "\"" + Config.KEY_SAVE_PATH + "publicKey" + keyLength + ".txt" + "\"" });
    }

    return new File(Config.KEY_SAVE_PATH + "publicKey" + keyLength + ".txt").getAbsolutePath();
  }

  public static String getKeyFromFile(String keyFilePath)
    throws Exception
  {
    if (StringUtils.isBlank(keyFilePath)) {
      return null;
    }
    File file = new File(keyFilePath);
    if (!file.exists()) {
      return null;
    }
    String key = SupportUtil.readFileAsString(keyFilePath);
    key = getOriginalKey(key);
    return key;
  }

  public static String replaceBlank(String str)
  {
    String dest = "";
    if (StringUtils.isNotBlank(str)) {
      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
  }

  public static String removeUrlParams(String url, String[] params)
  {
    for (String param : params) {
      url = removeUrlParam(url, param);
    }
    return url;
  }

  public static String removeUrlParam(String url, String param)
  {
    String[] params = url.split("&");
    String signStr = "";
    for (int i = 0; i < params.length; i++) {
      String para = params[i];
      if (!para.contains(param + "="))
      {
        signStr = signStr + para + "&";
      }
    }
    if (StringUtils.isNotBlank(signStr)) {
      int endIndex = signStr.length();
      String sign = signStr.substring(0, endIndex - 1);
      return sign;
    }
    return null;
  }

  public static String removeUrlHead(String fileName, String url, Integer step)
    throws IOException
  {
    Integer queIndex = Integer.valueOf(url.indexOf("?"));
    Integer startHttpIndex = Integer.valueOf(url.indexOf("http"));
    String retUrlParam;
    if ((startHttpIndex.intValue() == 0) && (queIndex.intValue() > 0)) {
      retUrlParam = url.substring(queIndex.intValue() + 1, url.length());
      RsaKey.appendTxtFile(fileName, step + ".删除url地址：\r\n");
      RsaKey.appendTxtFile(fileName, "【" + retUrlParam + "】\r\n\r\n");
    } else {
      return retUrlParam = url;
    }
    return retUrlParam;
  }

  public static String removeUrlEmptyParams(String url, String fileName) throws IOException
  {
    String[] params = url.split("&");
    StringBuffer retUrl = new StringBuffer();
    for (int i = 0; i < params.length; i++) {
      String para = params[i];
      if (StringUtils.isNotBlank(para)) {
        para = para.trim();

        String[] paramValues = para.split("=");
        if (paramValues.length == 1)
        {
          RsaKey.appendTxtFile(fileName, "\t\t删除空字段【" + paramValues[0] + "】\r\n\r\n");
        }
        else retUrl.append(para.substring(0, para.indexOf("="))).append(para.substring(para.indexOf("="))).append("&");
      }
    }

    String retUrlStr = retUrl.toString();
    String lastChar = retUrlStr.substring(retUrlStr.length() - 1, retUrlStr.length());

    if ((StringUtils.isNotBlank(retUrl.toString())) && ("&".equals(lastChar))) {
      url = retUrl.toString().substring(0, retUrl.toString().length() - 1);
    }
    return url;
  }

  public static String getUrlParams(String url, String param)
  {
    String[] params = url.split("&");
    String signStr = "";
    for (int i = 0; i < params.length; i++) {
      String para = params[i];
      String[] paramValue = para.split("=");
      if (paramValue.length > 1) {
        String paramValueTrim = paramValue[0].trim() + "=" + paramValue[1].trim();
        if (paramValueTrim.contains(param + "=")) {
          signStr = params[i];
          break;
        }
      }
    }
    if (StringUtils.isNotBlank(signStr)) {
      int beginIndex = signStr.indexOf("=");
      int endIndex = signStr.length();
      String sign = signStr.substring(beginIndex + 1, endIndex);
      return sign.trim();
    }
    return null;
  }

  public static String runCMD(String cmd)
    throws Exception
  {
    Process p = null;
    BufferedReader br = null;
    try {
      p = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String readLine;
      while ((readLine = br.readLine()) != null)
      {
        sb.append(readLine);
      }
      return sb.toString();
    } finally {
      if (br != null) {
        br.close();
      }
      if (p != null) {
        p.destroy();
        p = null;
      }
    }
  }

  public static String runCMD(String[] cmd)
    throws Exception
  {
    Process p = null;
    BufferedReader br = null;
    try {
      p = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String readLine;
      while ((readLine = br.readLine()) != null)
      {
        sb.append(readLine);
      }
      return sb.toString();
    } finally {
      if (br != null) {
        br.close();
      }
      if (p != null) {
        p.destroy();
        p = null;
      }
    }
  }

  public static void info(String msg)
  {
    info(msg, null);
  }

  public static void info(String msg, Object[] args)
  {
    try
    {
      if (StringUtils.isNotBlank(msg)) {
        String info = MessageFormat.format(msg, args);
        SupportUtil.writeFileString("run.log", info);
      }
    }
    catch (Exception localException)
    {
    }
  }

  private static String getOriginalKey(String key)
  {
    key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
      .replace("-----END RSA PRIVATE KEY-----", "").replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "").replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "");
    return replaceBlank(key);
  }

  private static String execCmd(String[] cmdStr)
    throws Exception
  {
    String opensslPath = "openssl\\bin\\openssl.exe";
    if (Env.isMac()) {
      opensslPath = "/usr/bin/openssl";
    }
    else if (!isExistFile(opensslPath)) {
      opensslPath = Config.CURRENT_SIGN_TYPE + File.separator + opensslPath;
    }

    if (!isExistFile(opensslPath)) {
      return null;
    }
    StringBuilder sb = new StringBuilder(opensslPath + " ");
    if ((cmdStr == null) || (cmdStr.length == 0)) {
      throw new NullPointerException("执行生成密钥命令失败");
    }
    String[] arrayOfString = cmdStr; int j = cmdStr.length; for (int i = 0; i < j; i++) { String c = arrayOfString[i];
      sb.append(c).append(" ");
    }
    return runCMD(sb.toString());
  }

  public static boolean isExistFile(String filePath)
  {
    if (StringUtils.isBlank(filePath)) {
      return false;
    }
    return new File(filePath).exists();
  }

  public static void clearHeadBottom(Integer keyLength) throws Exception {
    String privateKeyFilePath = Config.KEY_SAVE_PATH + "privateKey" + keyLength + ".txt";
    String publicKeyFilePath = Config.KEY_SAVE_PATH + "publicKey" + keyLength + ".txt";
    String privateKey = RsaKey.getKeyFromFile(privateKeyFilePath);
    String publicKey = RsaKey.getKeyFromFile(publicKeyFilePath);

    if (StringUtils.isEmpty(publicKey)) {
      File tmpFile = new File(publicKeyFilePath);
      if (tmpFile.exists())
        tmpFile.delete();
    }
    else {
      SupportUtil.writeFileString(publicKeyFilePath, publicKey);
    }
    if (StringUtils.isEmpty(privateKey)) {
      File tmpFile = new File(privateKeyFilePath);
      if (tmpFile.exists())
        tmpFile.delete();
    }
    else {
      SupportUtil.writeFileString(privateKeyFilePath, privateKey);
    }
  }
}