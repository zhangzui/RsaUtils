package com.zz.rsa.bean;

import com.alibaba.common.lang.SystemUtil;
import com.alibaba.common.lang.SystemUtil.OsInfo;

public class Env
{
  public static String OS = "window";

  public static String os_name = SystemUtil.getOsInfo().getName();

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