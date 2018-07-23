package com.zz.rsa.swt;

import com.alibaba.fastjson.JSON;

import com.swt.util.listener.KeyListenerUtil;

import com.test.common.crypto.SignUtils;
import com.test.common.crypto.SignUtilsImpl;

import com.zz.rsa.bean.Config;
import com.zz.rsa.utils.GenerateRsaUtil;
import com.zz.rsa.utils.RSA;
import com.zz.rsa.utils.RsaKey;
import com.zz.rsa.utils.SupportUtil;

import org.apache.commons.lang.StringUtils;

import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

import java.io.IOException;

import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class RSASignComposite extends Composite {
  private static String charset = Charset.defaultCharset().displayName();
  private static String rsaAlgorithm = Config.RSA_SHA256;
  private Text text_input;
  private Text text_output;
  private Text text_privatekey;
  private Text text_sign;
  private String waitCheckSignStr;
  private String inputParamDemo = "{\"charset\":\"utf-8\",\"data\":\"U8CeJAT3sMSq44YlNGp/9C0pM42j3Jdql9G5GgZTMh8=\",\"appId\":\"00001\",\"signType\":\"RSA2\",\"version\":\"1.0.0\",\"apiType\":\"test\",\"encryptType\":\"AES\",\"timestamp\":\"2018-07-23 10:07:176\",\"merchantNo\":\"00001\"}";
  private String inputPrivateKeyDemo = "Please enter the merchant application private key, this private key is the only document that the merchant calls the platform interface to conduct transactions, please be cautious!";
  private Button button_6;

  public RSASignComposite(Composite parent, int style) {
    super(parent, style);

    //    Link link_url = new Link(this, 0);
    //    link_url.setText("<A>使用说明</A>");
    //    link_url.setToolTipText("点击查看签名说明");
    //    Display display = parent.getDisplay();
    //    link_url.setFont(new Font(display, "宋体", 10, 0));
    //
    //    link_url.setBounds(700, 3, 80, 20);
    //
    //    link_url.addSelectionListener(new SelectionAdapter() {
    //      public void widgetSelected(SelectionEvent event) {
    //        RsaKey.openBrowserForUrl("https://doc.open.alipay.com/docs/doc.htm?docType=1&articleId=106118");
    //      }
    //    });
    Label label = new Label(this, 0);
    label.setBounds(5, 50, 90, 20);
    label.setText("request params");

    this.text_input = new Text(this, 2626);

    this.text_input.setBounds(100, 25, 670, 100);
    this.text_input.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(
            this.text_input));
    this.text_input.setForeground(getDisplay().getSystemColor(15));
    this.text_input.setText(this.inputParamDemo);
    this.text_input.addListener(15,
            new Listener() {
              public void handleEvent(Event e) {
                if (RSASignComposite.this.inputParamDemo.equals(
                        RSASignComposite.this.text_input.getText())) {
                  RSASignComposite.this.text_input.setText("");
                  RSASignComposite.this.text_input.setForeground(RSASignComposite.this.getDisplay()
                          .getSystemColor(2));
                }
              }
            });
    this.text_input.addListener(16,
            new Listener() {
              public void handleEvent(Event e) {
                if (StringUtils.isEmpty(
                        RSASignComposite.this.text_input.getText())) {
                  RSASignComposite.this.text_input.setText(RSASignComposite.this.inputParamDemo);
                  RSASignComposite.this.text_input.setForeground(RSASignComposite.this.getDisplay()
                          .getSystemColor(15));
                }
              }
            });

    Label label_3 = new Label(this, 0);
    label_3.setBounds(5, 150, 90, 45);
    label_3.setText("sign-privateKey:");

    this.text_privatekey = new Text(this, 2626);

    this.text_privatekey.setBounds(100, 135, 670, 80);
    this.text_privatekey.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(
            this.text_privatekey));
    this.text_privatekey.setForeground(getDisplay().getSystemColor(15));
    this.text_privatekey.setText(this.inputPrivateKeyDemo);
    this.text_privatekey.addListener(15,
            new Listener() {
              public void handleEvent(Event e) {
                if (RSASignComposite.this.inputPrivateKeyDemo.equals(
                        RSASignComposite.this.text_privatekey.getText())) {
                  RSASignComposite.this.text_privatekey.setText("");
                  RSASignComposite.this.text_privatekey.setForeground(RSASignComposite.this.getDisplay()
                          .getSystemColor(2));
                }
              }
            });
    this.text_privatekey.addListener(16,
            new Listener() {
              public void handleEvent(Event e) {
                if (StringUtils.isEmpty(
                        RSASignComposite.this.text_privatekey.getText())) {
                  RSASignComposite.this.text_privatekey.setText(RSASignComposite.this.inputPrivateKeyDemo);
                  RSASignComposite.this.text_privatekey.setForeground(RSASignComposite.this.getDisplay()
                          .getSystemColor(15));
                }
              }
            });

    Label lblcharset = new Label(this, 0);
    lblcharset.setBounds(5, 228, 80, 20);
    lblcharset.setText("charset:");

    Group chasetGroup = new Group(this, 32);
    chasetGroup.setBounds(100, 215, 670, 40);

    final Button btnUtf8 = new Button(chasetGroup, 16);
    btnUtf8.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        if (btnUtf8.getSelection()) {
          RSASignComposite.charset = "UTF-8";
        }
      }
    });
    btnUtf8.setText("UTF-8");
    btnUtf8.setBounds(5, 15, 84, 20);

    final Button btnGbk = new Button(chasetGroup, 16);
    btnGbk.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        if (btnGbk.getSelection()) {
          RSASignComposite.charset = "GBK";
        }
      }
    });
    btnGbk.setText("GBK");
    btnGbk.setBounds(120, 15, 84, 20);

    if (charset.equalsIgnoreCase("UTF-8")) {
      btnUtf8.setSelection(true);
    } else {
      btnGbk.setSelection(true);
    }

    Label lblalgorithm = new Label(this, 0);
    lblalgorithm.setBounds(5, 265, 80, 20);
    lblalgorithm.setText("sign type:");

    Group algorithmGroup = new Group(this, 32);
    algorithmGroup.setBounds(100, 253, 670, 40);

    final Button rsa2 = new Button(algorithmGroup, 16);
    rsa2.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        if (rsa2.getSelection()) {
          RSASignComposite.rsaAlgorithm = Config.RSA_SHA256;
        }
      }
    });
    rsa2.setText(Config.SIGN_TYPE_RSA2);
    rsa2.setBounds(5, 15, 200, 20);
    rsa2.setSelection(true);

    Button button_5 = new Button(this, 0);
    button_5.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        SignUtils signUtils = new SignUtilsImpl();
        String pri_key = RSASignComposite.this.text_privatekey.getText()
                .trim();

        if (StringUtils.isNotBlank(pri_key)) {
          if ((pri_key.contains("\r\n")) ||
                  (pri_key.contains("\n"))) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(),
                    "warming",
                    "There is no newline for the merchant application private key. Please delete the newline and try again!");
          }
        } else {
          MessageDialog.openWarning(RSASignComposite.this.getShell(),
                  "warming",
                  "Please enter the merchant application private key！");

          return;
        }

        if ((RsaKey.check_rsa_type(pri_key) == 1) ||
                (RsaKey.check_rsa_type(pri_key) == 4)) {
          try {
            String keyLength = RsaKey.getKeyLength(pri_key);
            pri_key = RsaKey.convert2pcks8(pri_key, keyLength);
          } catch (Exception e1) {
            e1.printStackTrace();
            MessageDialog.openWarning(RSASignComposite.this.getShell(),
                    "warming",
                    "The private key is wrong, please enter the correct private key!");

            return;
          }
        } else if ((RsaKey.check_rsa_type(pri_key) != 2) &&
                (RsaKey.check_rsa_type(pri_key) != 5)) {
          MessageDialog.openWarning(RSASignComposite.this.getShell(),
                  "warming",
                  "The format is wrong, please enter the correct private key!");

          return;
        }

        String waitSignContent = "";

        try {
          String inputParam = RSASignComposite.this.text_input.getText();
          Map<String, String> params = null;
          try {
            params = JSON.parseObject(inputParam,Map.class);
          } catch (Exception e1) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "warming",
                    "the request type is not a Legal JSON");
            writeLog("the request type is not a Legal JSON");
            return;
          }
          inputParam = getSignContent(params);
          System.out.println("params----:" +
                  JSON.toJSONString(params));
          System.out.println("inputParam----:" + inputParam);

          if (RSASignComposite.this.inputParamDemo.equals(
                  inputParam)) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(),
                    "error",
                    "please input request parmas！and type is json");

            return;
          }

          //          String signType = GenerateRsaUtil.getUrlParams(inputParam, "sign_type");
          String signType = params.get("signType");

          if (StringUtils.isBlank(signType)) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(),
                    "error",
                    "please check request parmas ,signType is null！");

            return;
          }

          //          waitSignContent = RSASignComposite.this.getInputString(inputParam);
          waitCheckSignStr = inputParam;
          waitSignContent = inputParam;
        } catch (Exception e1) {
          e1.printStackTrace();
          MessageDialog.openWarning(RSASignComposite.this.getShell(),
                  "warming", "request params type error！");

          return;
        }

        String sign = "";

        try {
          sign = RSA.sign(waitSignContent, pri_key,
                  RSASignComposite.rsaAlgorithm,
                  RSASignComposite.charset);
        } catch (Exception ex) {
          MessageDialog.openWarning(RSASignComposite.this.getShell(),
                  "warming",
                  "sign error please input right private key");
        }

        RSASignComposite.this.text_sign.setText(sign);
        RSASignComposite.this.text_output.setText(RSASignComposite.this.waitCheckSignStr);

        RSASignComposite.this.button_6.setEnabled(true);

        try {
          RsaKey.appendTxtFile("sign steps",
                  "3.generate sign：\r\n");
          RsaKey.appendTxtFile("sign steps", "【" + sign +
                  "】\r\n");
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });
    button_5.setToolTipText("RSA privateKey should be null");
    button_5.setText("sign");
    button_5.setBounds(100, 300, 100, 35);

    this.button_6 = new Button(this, 0);
    this.button_6.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        try {
          RsaKey.openTxtFile("sign steps");
        } catch (Exception ex) {
          MessageDialog.openWarning(RSASignComposite.this.getShell(),
                  "error", "file open failed！");
        }
      }
    });
    this.button_6.setToolTipText("RSA privateKey should be null");
    this.button_6.setText("View signature generation steps");
    this.button_6.setBounds(220, 300, 160, 35);
    this.button_6.setEnabled(false);

    Label label_line = new Label(this, 258);
    label_line.setBounds(10, 340, 770, 5);
    label_line.setText("output__sep");

    Label label_1 = new Label(this, 0);
    label_1.setBounds(5, 350, 90, 100);
    label_1.setText("To be signed：");

    this.text_output = new Text(this, 2626);

    this.text_output.setBounds(100, 350, 670, 100);
    this.text_output.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(
            this.text_output));

    Label label_5 = new Label(this, 0);
    label_5.setBounds(5, 465, 90, 100);
    label_5.setText("sign：");

    this.text_sign = new Text(this, 2626);

    this.text_sign.setBounds(100, 465, 670, 100);
    this.text_sign.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(
            this.text_sign));

    Label label_info = new Label(this, 0);
    label_info.setBounds(30, 575, 720, 50);
    label_info.setText(
            "Assign the generated \"signature\" to the sign parameter, put the sign to the request parameter OpenAPIEntity, and then post the call interface");
  }

  public static void main(String[] args) {
    RSAWindow.main(args);
  }

  protected void checkSubclass() {
  }


  public static String getSignContent(Map<String, String> sortedParams) {
    StringBuffer content = new StringBuffer();
    List<String> keys = new ArrayList<String>(sortedParams.keySet());
    Collections.sort(keys);

    int index = 0;

    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = sortedParams.get(key);

      if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
        content.append(((index == 0) ? "" : "&") + key + "=" + value);
        index++;
      }
    }

    return content.toString();
  }

  public String getInputString(String inputParam) throws IOException {
    RsaKey.deleteTxtFile("sign steps");

    RsaKey.appendTxtFile("sign steps", "一、签名准备\r\n");

    RsaKey.appendTxtFile("sign steps", "1.原始内容：\r\n");
    RsaKey.appendTxtFile("sign steps", "【" + inputParam + "】\r\n\r\n");

    RsaKey.appendTxtFile("sign steps", "2.签名私钥：\r\n");
    RsaKey.appendTxtFile("sign steps",
            "【" + this.text_privatekey.getText() + "】\r\n\r\n");

    RsaKey.appendTxtFile("sign steps", "二、生成待签名字符串：\r\n");

    RsaKey.appendTxtFile("sign steps",
            "1.剔除参数名(和参数值)前后的空格：\r\n(正常业务下，参数值前后不应含有空格)\r\n");

    if (StringUtils.isEmpty(inputParam)) {
      return null;
    }

    inputParam = inputParam.trim();
    RsaKey.appendTxtFile("sign steps", "【" + inputParam + "】\r\n\r\n");

    Integer step = Integer.valueOf(1);
    step = Integer.valueOf(step.intValue() + 1);

    String removeGatewayUrl = GenerateRsaUtil.removeUrlHead("sign steps",
            inputParam, step);

    if (inputParam.equals(removeGatewayUrl)) {
      step = Integer.valueOf(2);
    } else {
      step = Integer.valueOf(3);
    }

    inputParam = removeGatewayUrl;
    inputParam = GenerateRsaUtil.removeUrlEmptyParams(inputParam,
            "sign steps");
    RsaKey.appendTxtFile("sign steps", step + ".剔除空值参数：\r\n");
    RsaKey.appendTxtFile("sign steps", "【" + inputParam + "】\r\n\r\n");
    step = Integer.valueOf(step.intValue() + 1);

    if ((StringUtils.isNotEmpty(inputParam)) &&
            (inputParam.contains("sign="))) {
      inputParam = GenerateRsaUtil.removeUrlParams(inputParam,
              new String[] { "sign" });
    }

    inputParam = SupportUtil.sortParams(inputParam);
    RsaKey.appendTxtFile("sign steps", step + ".排序：\r\n");
    RsaKey.appendTxtFile("sign steps", "【" + inputParam + "】\r\n\r\n");
    this.waitCheckSignStr = inputParam;
    step = Integer.valueOf(step.intValue() + 1);

    return inputParam;
  }
  private void writeLog(String message){
    try {
      RsaKey.appendTxtFile("error_log",  message + "\r\n");
    } catch (IOException e2) {
      e2.printStackTrace();
    }
  }
}
