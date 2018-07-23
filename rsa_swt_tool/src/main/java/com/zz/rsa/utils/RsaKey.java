package com.zz.rsa.utils;

import com.zz.rsa.bean.Config;
import com.zz.rsa.bean.Env;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class RsaKey
{
  public static String openssl;
  public static String txtedit;
  public static String[] openMacTxteditCmd;
  public static String openbrowser = "rundll32 url.dll,FileProtocolHandler ";

  static {
    openssl = "openssl\\bin\\openssl.exe";
    txtedit = "cmd /c start \"\" ";
    String os = System.getProperty("os.name");;
    if (os.toLowerCase().contains("mac")) {
      openssl = "openssl";
      openMacTxteditCmd = new String[] { "open", "-a", "TextEdit", "" };
      os = "mac";
      openbrowser = "open ";
    }
  }

  public static void main(String[] args)
  {
  }

  public static String mkRsaPublicKeyFile(String privateKeyFile, String keyLength)
    throws Exception
  {
    if (!new File(privateKeyFile).exists()) {
      return null;
    }
    String dir = Config.KEY_SAVE_PATH;

    if (Env.isMac()) {
      String[] cmdStr = { openssl, "rsa", "-in", privateKeyFile, 
        "-pubout", "-out", dir + "publicKey" + keyLength + ".txt" };
      GenerateRsaUtil.runCMD(cmdStr);
    } else {
      Cmd cmd = new Cmd();
      cmd.exec(openssl + " rsa -in \"" + privateKeyFile + 
        "\" -pubout -out \"" + dir + "publicKey" + keyLength + ".txt" + "\"");
      cmd.close();
    }
    return new File(dir + "publicKey" + keyLength + ".txt").getAbsolutePath();
  }

  public static String mkTempRsaPublicKeyFile(String privateKeyFile, String keyLength)
    throws Exception
  {
    if (!new File(privateKeyFile).exists()) {
      return null;
    }
    String dir = Config.KEY_SAVE_PATH;
    String timestr = String.valueOf(new Date().getTime());

    if (Env.isMac()) {
      String[] cmdStr = { openssl, "rsa", "-in", privateKeyFile, 
        "-pubout", "-out", dir + "publicKey" + keyLength + timestr + ".txt" };
      GenerateRsaUtil.runCMD(cmdStr);
    } else {
      Cmd cmd = new Cmd();
      cmd.exec(openssl + " rsa -in \"" + privateKeyFile + 
        "\" -pubout -out \"" + dir + "publicKey" + keyLength + timestr + ".txt" + "\"");
      cmd.close();
    }
    return new File(dir + "publicKey" + keyLength + timestr + ".txt").getAbsolutePath();
  }

  public static String getKeyFromFile(String file) throws Exception {
    String key = "";
    try {
      key = SupportUtil.readFileAsString(file);
    } catch (FileNotFoundException ex) {
      return key;
    }
    key = getOriginalKey(key);
    return key;
  }

  public static String getOriginalKey(String key)
  {
    key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
      .replace("-----END RSA PRIVATE KEY-----", "")
      .replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "")
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "");
    key = SupportUtil.filterSpaceTab(key);
    key = SupportUtil.filterLineSeparator(key);
    return key;
  }

  public static String convert2KeyFile(String key, String keyLength)
  {
    String key_file_content = "";
    String fileName = Config.KEY_SAVE_PATH;

    if (!SupportUtil.isExists(fileName)) {
      SupportUtil.mkDir(fileName);
    }
    int rsaType = check_rsa_type(key);
    switch (rsaType) {
    case 1:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN RSA PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END RSA PRIVATE KEY-----\n";
      break;
    case 2:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END PRIVATE KEY-----\n";
      break;
    case 3:
      fileName = fileName + "publicKey";
      key_file_content = "-----BEGIN PUBLIC KEY-----\n" + split2Base64(key) + "\n-----END PUBLIC KEY-----\n";
      break;
    case 4:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN RSA PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END RSA PRIVATE KEY-----\n";
      break;
    case 5:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END PRIVATE KEY-----\n";
      break;
    case 6:
      fileName = fileName + "publicKey";
      key_file_content = "-----BEGIN PUBLIC KEY-----\n" + split2Base64(key) + "\n-----END PUBLIC KEY-----\n";
    }

    if (!StringUtils.isEmpty(fileName)) {
      fileName = fileName + keyLength + ".txt";
      String file = new File(fileName).getAbsolutePath();
      try {
        SupportUtil.writeFileString(file, key_file_content);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return file;
    }
    return "";
  }

  public static String convert2TempKeyFile(String key, String keyLength)
  {
    String key_file_content = "";
    String fileName = Config.KEY_SAVE_PATH;

    if (!SupportUtil.isExists(fileName)) {
      SupportUtil.mkDir(fileName);
    }
    int rsaType = check_rsa_type(key);
    switch (rsaType) {
    case 1:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN RSA PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END RSA PRIVATE KEY-----\n";
      break;
    case 2:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END PRIVATE KEY-----\n";
      break;
    case 3:
      fileName = fileName + "publicKey";
      key_file_content = "-----BEGIN PUBLIC KEY-----\n" + split2Base64(key) + "\n-----END PUBLIC KEY-----\n";
      break;
    case 4:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN RSA PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END RSA PRIVATE KEY-----\n";
      break;
    case 5:
      fileName = fileName + "privateKey";
      key_file_content = "-----BEGIN PRIVATE KEY-----\n" + split2Base64(key) + "\n-----END PRIVATE KEY-----\n";
      break;
    case 6:
      fileName = fileName + "publicKey";
      key_file_content = "-----BEGIN PUBLIC KEY-----\n" + split2Base64(key) + "\n-----END PUBLIC KEY-----\n";
    }

    if (!StringUtils.isEmpty(fileName)) {
      fileName = fileName + keyLength + new Date().getTime() + ".txt";
      String file = new File(fileName).getAbsolutePath();
      try {
        SupportUtil.writeFileString(file, key_file_content);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return file;
    }
    return "";
  }

  public static int check_rsa_type(String key)
  {
    if ((key.length() >= 810) && (key.length() <= 820))
      return 1;
    if ((key.length() >= 840) && (key.length() <= 860))
      return 2;
    if ((key.length() >= 210) && (key.length() <= 220))
      return 3;
    if ((key.length() >= 1580) && (key.length() <= 1600))
      return 4;
    if ((key.length() >= 1620) && (key.length() <= 1630))
      return 5;
    if ((key.length() >= 390) && (key.length() <= 400)) {
      return 6;
    }
    return 0;
  }

  public static String convert2pcks8(String priKey, String keyLength) throws Exception
  {
    String fileName = Config.KEY_SAVE_PATH + "privateKey_tmp.txt";
    priKey = split2Base64(priKey);
    priKey = "-----BEGIN RSA PRIVATE KEY-----\n" + priKey;
    priKey = priKey + "\n-----END RSA PRIVATE KEY-----\n";
    if (!SupportUtil.isExists(Config.KEY_SAVE_PATH)) {
      SupportUtil.mkDir(Config.KEY_SAVE_PATH);
    }
    SupportUtil.writeFileString(fileName, priKey);
    String file = mkPkcs8FromPrivate(fileName, keyLength);
    return getKeyFromFile(file);
  }

  public static String convertPcks82Original(String priKeyPkcs8, String keyLength) throws Exception {
    String fileName = Config.KEY_SAVE_PATH + "publicKey_tmp.txt";
    priKeyPkcs8 = split2Base64(priKeyPkcs8);
    priKeyPkcs8 = "-----BEGIN PRIVATE KEY-----\n" + priKeyPkcs8;
    priKeyPkcs8 = priKeyPkcs8 + "\n-----END PRIVATE KEY-----\n";
    if (!SupportUtil.isExists(Config.KEY_SAVE_PATH)) {
      SupportUtil.mkDir(Config.KEY_SAVE_PATH);
    }
    SupportUtil.writeFileString(fileName, priKeyPkcs8);
    String file = mkPrivateFromPkcs8(fileName, keyLength);
    return getKeyFromFile(file);
  }

  public static String mkPrivateFromPkcs8(String privateKeyFile, String keyLength) throws Exception
  {
    if (!new File(privateKeyFile).exists()) {
      return null;
    }
    String dir = Config.KEY_SAVE_PATH;
    if (Env.isMac()) {
      String[] cmdStr = { openssl, "pkcs8", "-inform", "PEM", "-in", privateKeyFile, 
        "-outform", "PEM", "-nocrypt", "-out", dir + "privateKey" + keyLength + ".txt" };
      GenerateRsaUtil.runCMD(cmdStr);
    } else {
      Cmd cmd = new Cmd();
      String s = null;
      s = openssl + " pkcs8 -inform PEM -in \"" + privateKeyFile + 
        "\" -outform PEM -nocrypt -out \"" + dir + 
        "privateKey" + keyLength + ".txt" + "\"";
      cmd.exec(s);
      cmd.close();
    }

    return new File(dir + "privateKey" + keyLength + ".txt").getAbsolutePath();
  }

  public static String mkPkcs8FromPrivate(String privateKeyFile, String keyLength) throws Exception
  {
    if (!new File(privateKeyFile).exists()) {
      return null;
    }
    String dir = Config.KEY_SAVE_PATH;
    if (Env.isMac()) {
      String[] cmdStr = { openssl, "pkcs8", "-topk8", "-inform", "PEM", "-in", privateKeyFile, 
        "-outform", "PEM", "-nocrypt", "-out", dir + "privateKey" + keyLength + ".txt" };
      GenerateRsaUtil.runCMD(cmdStr);
    } else {
      Cmd cmd = new Cmd();
      cmd.exec(openssl + " pkcs8 -topk8 -inform PEM -in \"" + privateKeyFile + 
        "\" -outform PEM -nocrypt -out \"" + dir + 
        "privateKey" + keyLength + ".txt" + "\"");
      cmd.close();
    }
    return new File(dir + "privateKey" + keyLength + ".txt").getAbsolutePath();
  }

  public static void openBrowserForUrl(String url)
  {
    if (Env.isMac())
      openMacURL(url);
    else
      openWindowsURL(url);
  }

  public static void openMacURL(String url)
  {
    try {
      Runtime.getRuntime().exec(openbrowser + url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void openWindowsURL(String url) {
    try {
      Runtime.getRuntime().exec(openbrowser + url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void openTxtFile(String txtFileName)
    throws Exception
  {
    String signStepFile = Config.KEY_SAVE_PATH + txtFileName + ".txt";
    if (!new File(signStepFile).exists()) {
      return;
    }

    if (Env.isMac()) {
      openMacTxteditCmd[(openMacTxteditCmd.length - 1)] = signStepFile;
      GenerateRsaUtil.runCMD(openMacTxteditCmd);
    }
    else {
      Runtime.getRuntime().exec(txtedit + "\"" + signStepFile + "\"");
    }
  }

  public static void appendTxtFile(String txtFileName, String content)
    throws IOException
  {
    if (!SupportUtil.isExists(Config.KEY_SAVE_PATH)) {
      SupportUtil.mkDir(Config.KEY_SAVE_PATH);
    }
    String signStepFile = Config.KEY_SAVE_PATH + txtFileName + ".txt";
    SupportUtil.appendFileString(signStepFile, content, "GBK");
  }

  public static void deleteTxtFile(String txtFileName) throws IOException
  {
    String signStepFile = Config.KEY_SAVE_PATH + txtFileName + ".txt";
    SupportUtil.delFile(signStepFile);
  }

  public static String readFile2String(String filePath)
    throws Exception
  {
    return SupportUtil.readFileAsString(filePath);
  }

  public static String split2Base64(String str) {
    str = str.split("\\n")[0];
    String base64 = "";
    while (str.length() >= 64) {
      base64 = base64 + str.substring(0, 64) + "\n";
      str = str.substring(64);
    }
    base64 = base64 + str;
    return base64.trim();
  }

  public static void clearHeadBottom(String keyLength) throws Exception {
    String privateKeyFilePath = Config.KEY_SAVE_PATH + "privateKey" + ".txt";
    String publicKeyFilePath = Config.KEY_SAVE_PATH + "publicKey" + ".txt";
    String privateKey = getKeyFromFile(privateKeyFilePath);
    String publicKey = getKeyFromFile(publicKeyFilePath);

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

    String tmpFilePath = Config.KEY_SAVE_PATH + "privateKey_tmp.txt";
    File tmpFile = new File(tmpFilePath);
    if (tmpFile.exists())
      tmpFile.delete();
  }

  public static String getKeyLength(String privateKey)
  {
    int keyFormat = check_rsa_type(privateKey);
    if ((keyFormat == 5) || (keyFormat == 4)) {
      return "2048";
    }
    return "1024";
  }
}