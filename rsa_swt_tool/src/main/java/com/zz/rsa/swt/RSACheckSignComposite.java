package com.zz.rsa.swt;

import com.zz.rsa.bean.Config;
import com.zz.rsa.utils.GenerateRsaUtil;
import com.zz.rsa.utils.RSA;
import com.zz.rsa.utils.RsaKey;
import com.zz.rsa.utils.SupportUtil;
import com.swt.util.listener.KeyListenerUtil;
import com.test.common.crypto.SignUtils;
import com.test.common.crypto.SignUtilsImpl;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class RSACheckSignComposite extends Composite
{
  private static String charset = Charset.defaultCharset().displayName();
  private static String rsaAlgorithm = Config.RSA_SHA256;
  private Text text_input;
  private Text public_text;
  private String waitCheckSignStr;
  private String inputParamDemo = "https请求同步返回报文无需验签。\r\n异步通知报文格式：total_amount=2.00&buyer_id=2088102116773037&body=大乐透2.1&trade_no=2016071921001003030200089909&refund_fee=0.00&notify_time=2016-07-19 14:10:49&subject=大乐透2.1&sign_type=RSA&charset=utf-8&notify_type=trade_status_sync&out_trade_no=0719141034-6418&gmt_close=2016-07-19 14:10:46&gmt_payment=2016-07-19 14:10:47&trade_status=TRADE_SUCCESS&version=1.0&sign=kPbQIjX&gmt_create=2016-07-19 14:10:44&app_id=2015102700040153&seller_id=2088102119685838&notify_id=4a91b7a78a503640467525113fb7d8bg8e";
  private String inputPublicKeyDemo = "请输入支付宝提供的公钥，如不知道，可以点击查看公钥进行查看！";

  public static void main(String[] args)
  {
    RSAWindow.main(args);
  }

  public RSACheckSignComposite(Composite parent, int style)
  {
    super(parent, style);

    Label label_title = new Label(this, 0);
    label_title.setBounds(150, 5, 425, 20);

    label_title.setText("此功能仅支持支付宝异步通知做验签，请勿对其他报文做验签！");
    label_title.setBackground(getDisplay().getSystemColor(7));

    Display display = parent.getDisplay();
//    Link link_url = new Link(this, 0);
//    link_url.setText("<A>使用说明</A>");
//    link_url.setToolTipText("点击查看验签说明");
//    link_url.setFont(new Font(display, "宋体", 10, 0));
//
//    link_url.setBounds(700, 3, 80, 20);
//
//    link_url.addSelectionListener(new SelectionAdapter() {
//      public void widgetSelected(SelectionEvent event) {
//        RsaKey.openBrowserForUrl("https://doc.open.alipay.com/docs/doc.htm?docType=1&articleId=106120");
//      }
//    });
    Label label = new Label(this, 0);
    label.setBounds(5, 65, 90, 20);
    label.setText("响应报文：");

    this.text_input = new Text(this, 2626);
    this.text_input.setBounds(100, 30, 670, 100);
    this.text_input.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(this.text_input));
    this.text_input.setForeground(getDisplay().getSystemColor(15));
    this.text_input.setText(this.inputParamDemo);
    this.text_input.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSACheckSignComposite.this.inputParamDemo.equals(RSACheckSignComposite.this.text_input.getText())) {
          RSACheckSignComposite.this.text_input.setText("");
          RSACheckSignComposite.this.text_input.setForeground(RSACheckSignComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_input.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSACheckSignComposite.this.text_input.getText())) {
          RSACheckSignComposite.this.text_input.setText(RSACheckSignComposite.this.inputParamDemo);
          RSACheckSignComposite.this.text_input.setForeground(RSACheckSignComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Link link = new Link(this, 0);
    link.setText("<A>查看公钥</A>");
    link.setToolTipText("点击登录支付宝开放平台查看支付宝公钥");
    link.setFont(new Font(display, "宋体", 10, 0));
    link.setBounds(5, 193, 90, 20);

    link.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        RsaKey.openBrowserForUrl("https://openhome.alipay.com/platform/keyManage.htm");
      }
    });
    Label label_1 = new Label(this, 0);
    label_1.setBounds(5, 170, 90, 20);
    label_1.setText("支付宝公钥：");

    this.public_text = new Text(this, 2626);
    this.public_text.setBounds(100, 140, 670, 80);
    this.public_text.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(this.public_text));
    this.public_text.setForeground(getDisplay().getSystemColor(15));
    this.public_text.setText(this.inputPublicKeyDemo);
    this.public_text.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSACheckSignComposite.this.inputPublicKeyDemo.equals(RSACheckSignComposite.this.public_text.getText())) {
          RSACheckSignComposite.this.public_text.setText("");
          RSACheckSignComposite.this.public_text.setForeground(RSACheckSignComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.public_text.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSACheckSignComposite.this.public_text.getText())) {
          RSACheckSignComposite.this.public_text.setText(RSACheckSignComposite.this.inputPublicKeyDemo);
          RSACheckSignComposite.this.public_text.setForeground(RSACheckSignComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label lblcharset = new Label(this, 0);
    lblcharset.setBounds(5, 235, 90, 20);
    lblcharset.setText("字符集：");

    Group chasetGroup = new Group(this, 32);
    chasetGroup.setBounds(100, 220, 670, 40);

    final Button button_radio_UTF8 = new Button(chasetGroup, 16);
    button_radio_UTF8.setSelection(true);
    button_radio_UTF8.setBounds(5, 15, 84, 20);
    button_radio_UTF8.setText("UTF-8");
    button_radio_UTF8.pack();

    final Button button_radio_gbk = new Button(chasetGroup, 16);
    button_radio_gbk.setBounds(120, 15, 84, 20);
    button_radio_gbk.setText("GBK");
    button_radio_gbk.pack();

    Label lblalgorithm = new Label(this, 0);
    lblalgorithm.setBounds(5, 273, 90, 20);
    lblalgorithm.setText("验签方式：");

    Group algorithmGroup = new Group(this, 32);
    algorithmGroup.setBounds(100, 260, 670, 40);

    final Button rsa = new Button(algorithmGroup, 16);
    rsa.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (rsa.getSelection())
          RSACheckSignComposite.rsaAlgorithm = Config.RSA_SHA1;
      }
    });
    rsa.setText(Config.SIGN_TYPE_RSA);
    rsa.setBounds(120, 15, 84, 20);

    final Button rsa2 = new Button(algorithmGroup, 16);
    rsa2.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (rsa2.getSelection())
          RSACheckSignComposite.rsaAlgorithm = Config.RSA_SHA256;
      }
    });
    rsa2.setText(Config.SIGN_TYPE_RSA2);
    rsa2.setBounds(5, 15, 84, 20);

    if (rsaAlgorithm.equalsIgnoreCase(Config.RSA_SHA256))
      rsa2.setSelection(true);
    else {
      rsa.setSelection(true);
    }

    Button check_sign_btn = new Button(this, 0);
    check_sign_btn.setText("开始验签");
    check_sign_btn.setBounds(100, 307, 100, 35);

    Label label_line = new Label(this, 258);
    label_line.setBounds(10, 350, 770, 5);
    label_line.setText("output__sep");

    Label setp_one = new Label(this, 0);
    setp_one.setBounds(5, 360, 90, 20);
    setp_one.setText("取出sign");

    final Text sign_value_text = new Text(this, 2626);
    sign_value_text.setBounds(100, 360, 670, 70);
    sign_value_text.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(sign_value_text));

    Label for_sign_string = new Label(this, 0);
    for_sign_string.setBounds(5, 435, 90, 20);
    for_sign_string.setText("组成待验签串");

    final Text for_sign_text = new Text(this, 2626);
    for_sign_text.setBounds(100, 435, 670, 100);
    for_sign_text.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(for_sign_text));

    Label setp_two = new Label(this, 0);
    setp_two.setBounds(5, 545, 90, 20);
    setp_two.setText("验签结果");

    final Text check_sign_result = new Text(this, 2114);
    check_sign_result.setBounds(100, 545, 670, 30);
    check_sign_result.addKeyListener(KeyListenerUtil.mkCtrlAKeyListener(check_sign_result));

    final Button check_detail = new Button(this, 0);
    check_detail.setText("查看验签详细步骤");
    check_detail.setBounds(220, 307, 160, 35);
    check_detail.setEnabled(false);
    check_detail.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        try {
          RsaKey.openTxtFile("验签步骤");
        } catch (Exception ex) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "错误", 
            "步骤文件打开失败！");
        }
      }
    });
    check_sign_btn.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        SignUtils signUtils = new SignUtilsImpl();
        String inputParams = RSACheckSignComposite.this.text_input.getText().trim();
        if (RSACheckSignComposite.this.inputParamDemo.equals(inputParams)) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "请输入报文内容");
          return;
        }
        String publicKey = RSACheckSignComposite.this.public_text.getText();

        if (RSACheckSignComposite.this.inputPublicKeyDemo.equals(publicKey)) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "请输入支付宝公钥！");
          return;
        }

        if (StringUtils.isNotBlank(publicKey)) {
          if ((publicKey.contains("\r\n")) || (publicKey.contains("\n")))
            MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
              "支付宝公钥不能存在换行，请删除换行后再试！");
        }
        else
        {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "请输入支付宝公钥！");
          return;
        }
        String inputString = "";
        String signStr = "";
        try {
          String signType = GenerateRsaUtil.getUrlParams(inputParams, "sign_type");

          if (rsa2.getSelection()) {
            if ((signType != null) && (!"".equals(signType)) && (!Config.SIGN_TYPE_RSA2.equals(signType))) {
              MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "错误", 
                "签名方式错误，请检查输入参数中sign_type是否与所选签名方式相同！");
            }
          }
          else if ((rsa.getSelection()) && 
            (signType != null) && (!"".equals(signType)) && (!Config.SIGN_TYPE_RSA.equals(signType))) {
            MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "错误", 
              "签名方式错误，请检查输入参数中sign_type是否与所选签名方式相同！");
            return;
          }

          signStr = GenerateRsaUtil.getUrlParams(inputParams, "sign");
          inputString = RSACheckSignComposite.this.getInputString(inputParams, signStr);
        }
        catch (Exception e1)
        {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", "响应报文格式错误！");
          e1.printStackTrace();
          return;
        }

        if (StringUtils.isBlank(signStr)) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "缺少签名参数sign！");
          return;
        }

        if (StringUtils.isBlank(inputString)) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "请输入完整报文！");
          return;
        }
        for_sign_text.setText(RSACheckSignComposite.this.waitCheckSignStr);
        sign_value_text.setText(signStr);

        if ((StringUtils.isBlank(inputString)) || (StringUtils.isBlank(signStr))) {
          MessageDialog.openWarning(RSACheckSignComposite.this.getShell(), "警告", 
            "报文错误或签名丢失！");
          return;
        }

        boolean result = RSA.verify(inputString, signStr, GenerateRsaUtil.replaceBlank(publicKey), RSACheckSignComposite.rsaAlgorithm, RSACheckSignComposite.charset);
        if (result) {
          check_sign_result.setText("验签成功");
          check_detail.setEnabled(true);
        } else {
          check_sign_result.setText("验签失败");
          check_detail.setEnabled(true);
        }
        try
        {
          RsaKey.appendTxtFile("验签步骤", "三、验签结果：\r\n");
          RsaKey.appendTxtFile("验签步骤", "【" + (result ? "验签成功" : "验签失败") + "】\r\n");
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });
    button_radio_UTF8.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (button_radio_UTF8.getSelection())
          RSACheckSignComposite.charset = "UTF-8";
      }
    });
    button_radio_gbk.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (button_radio_gbk.getSelection())
          RSACheckSignComposite.charset = "GBK";
      }
    });
    if (charset.equalsIgnoreCase("UTF-8"))
      button_radio_UTF8.setSelection(true);
    else
      button_radio_gbk.setSelection(true);
  }

  public void writeCheckSignStepHead(String inputParam, String signStr)
    throws IOException
  {
    RsaKey.deleteTxtFile("验签步骤");

    RsaKey.appendTxtFile("验签步骤", "一、验签准备\r\n");
    RsaKey.appendTxtFile("验签步骤", "1.原始报文：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + inputParam + "】\r\n\r\n");

    RsaKey.appendTxtFile("验签步骤", "2.验签公钥：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + this.public_text.getText() + "】\r\n\r\n");

    RsaKey.appendTxtFile("验签步骤", "3.从报文取出签名值sign：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + signStr + "】\r\n\r\n");
  }

  public String getInputString(String inputParam, String signStr)
    throws IOException
  {
    writeCheckSignStepHead(inputParam, signStr);

    RsaKey.appendTxtFile("验签步骤", "二、组成待验签串：\r\n");

    Integer step = Integer.valueOf(1);

    String removeGatewayUrl = GenerateRsaUtil.removeUrlHead("验签步骤", inputParam, step);
    if (inputParam.equals(removeGatewayUrl))
      step = Integer.valueOf(1);
    else {
      step = Integer.valueOf(2);
    }
    inputParam = removeGatewayUrl;

    inputParam = GenerateRsaUtil.removeUrlEmptyParams(inputParam, "验签步骤");
    RsaKey.appendTxtFile("验签步骤", step + ".剔除空值参数：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + inputParam + "】\r\n\r\n");
    step = Integer.valueOf(step.intValue() + 1);

    if ((!StringUtils.isBlank(inputParam)) && (inputParam.contains("sign="))) {
      inputParam = GenerateRsaUtil.removeUrlParams(inputParam, new String[] { "sign" });
    }
    if ((!StringUtils.isBlank(inputParam)) && (inputParam.contains("sign_type="))) {
      inputParam = GenerateRsaUtil.removeUrlParams(inputParam, new String[] { "sign_type" });
    }
    RsaKey.appendTxtFile("验签步骤", step + ".剔除sign、sign_type参数：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + inputParam + "】\r\n\r\n");
    step = Integer.valueOf(step.intValue() + 1);

    inputParam = SupportUtil.sortParams(inputParam);
    RsaKey.appendTxtFile("验签步骤", step + "、排序：\r\n");
    RsaKey.appendTxtFile("验签步骤", "【" + inputParam + "】\r\n\r\n");
    step = Integer.valueOf(step.intValue() + 1);

    this.waitCheckSignStr = inputParam;

    return inputParam;
  }

  public String writeCheckSignStep(String signStr, String inputStr) throws IOException {
    String inputParam = this.text_input.getText().trim();

    writeCheckSignStepHead(inputParam, signStr);

    RsaKey.appendTxtFile("验签步骤", "\t2、组成待验签串：" + charset + "）：\r\n\r\n");
    RsaKey.appendTxtFile("验签步骤", "\t\t【" + inputStr + "】\r\n\r\n");
    this.waitCheckSignStr = inputParam;

    inputParam = SupportUtil.getInputString(inputStr, charset);

    return inputParam;
  }
}