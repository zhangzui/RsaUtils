package com.zz.rsa.bean;

public class Env
{
  public static String OS = "window";

  public static String os_name = System.getProperty("os.name");

  static { if (os_name.toLowerCase().contains("mac"))
      OS = "mac";
  }

  public static boolean isMac()
  {
    return "mac".equals(OS);
  }

  public static boolean isWindow()
  {
    return "window".equals(OS);
  }
}