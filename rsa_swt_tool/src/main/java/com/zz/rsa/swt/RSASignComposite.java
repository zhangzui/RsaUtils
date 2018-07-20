package com.zz.rsa.swt;

import com.alibaba.common.lang.StringUtil;
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

public class RSASignComposite extends Composite
{
  private static String charset = Charset.defaultCharset().displayName();
  private static String rsaAlgorithm = Config.RSA_SHA256;
  private Text text_input;
  private Text text_output;
  private Text text_privatekey;
  private Text text_sign;
  private String waitCheckSignStr;
  private String inputParamDemo = "{xxxx}";
//  private String inputPrivateKeyDemo = "请输入商户应用私钥，此私钥是商户调用平台接口进行交易的唯一凭证，请谨慎保管！";
  private String inputPrivateKeyDemo = "Please enter the merchant application private key, this private key is the only document that the merchant calls the platform interface to conduct transactions, please be cautious!";
  private Button button_6;

  public static void main(String[] args)
  {
    RSAWindow.main(args);
  }

  public RSASignComposite(Composite parent, int style)
  {
    super(parent, style);

    Link link_url = new Link(this, 0);
    link_url.setText("<A>使用说明</A>");
    link_url.setToolTipText("点击查看签名说明");
    Display display = parent.getDisplay();
    link_url.setFont(new Font(display, "宋体", 10, 0));

    link_url.setBounds(700, 3, 80, 20);

    link_url.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        RsaKey.openBrowserForUrl("https://doc.open.alipay.com/docs/doc.htm?docType=1&articleId=106118");
      }
    });
    Label label = new Label(this, 0);
    label.setBounds(5, 50, 70, 20);
    label.setText("请求参数：");

    this.text_input = new Text(this, 2626);

    this.text_input.setBounds(100, 25, 670, 100);
    this.text_input.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_input));
    this.text_input.setForeground(getDisplay().getSystemColor(15));
    this.text_input.setText(this.inputParamDemo);
    this.text_input.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSASignComposite.this.inputParamDemo.equals(RSASignComposite.this.text_input.getText())) {
          RSASignComposite.this.text_input.setText("");
          RSASignComposite.this.text_input.setForeground(RSASignComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_input.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSASignComposite.this.text_input.getText())) {
          RSASignComposite.this.text_input.setText(RSASignComposite.this.inputParamDemo);
          RSASignComposite.this.text_input.setForeground(RSASignComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label label_3 = new Label(this, 0);
    label_3.setBounds(5, 150, 90, 45);
    label_3.setText("商户\r\n应用私钥：");

    this.text_privatekey = new Text(this, 2626);

    this.text_privatekey.setBounds(100, 135, 670, 80);
    this.text_privatekey.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_privatekey));
    this.text_privatekey.setForeground(getDisplay().getSystemColor(15));
    this.text_privatekey.setText(this.inputPrivateKeyDemo);
    this.text_privatekey.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSASignComposite.this.inputPrivateKeyDemo.equals(RSASignComposite.this.text_privatekey.getText())) {
          RSASignComposite.this.text_privatekey.setText("");
          RSASignComposite.this.text_privatekey.setForeground(RSASignComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_privatekey.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSASignComposite.this.text_privatekey.getText())) {
          RSASignComposite.this.text_privatekey.setText(RSASignComposite.this.inputPrivateKeyDemo);
          RSASignComposite.this.text_privatekey.setForeground(RSASignComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label lblcharset = new Label(this, 0);
    lblcharset.setBounds(5, 228, 80, 20);
    lblcharset.setText("字符集：");

    Group chasetGroup = new Group(this, 32);
    chasetGroup.setBounds(100, 215, 670, 40);

    final Button btnUtf8 = new Button(chasetGroup, 16);
    btnUtf8.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (btnUtf8.getSelection())
          RSASignComposite.charset = "UTF-8";
      }
    });
    btnUtf8.setText("UTF-8");
    btnUtf8.setBounds(5, 15, 84, 20);

    final Button btnGbk = new Button(chasetGroup, 16);
    btnGbk.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (btnGbk.getSelection())
          RSASignComposite.charset = "GBK";
      }
    });
    btnGbk.setText("GBK");
    btnGbk.setBounds(120, 15, 84, 20);

    if (charset.equalsIgnoreCase("UTF-8"))
      btnUtf8.setSelection(true);
    else {
      btnGbk.setSelection(true);
    }

    Label lblalgorithm = new Label(this, 0);
    lblalgorithm.setBounds(5, 265, 80, 20);
    lblalgorithm.setText("签名方式：");

    Group algorithmGroup = new Group(this, 32);
    algorithmGroup.setBounds(100, 253, 670, 40);

    final Button rsa = new Button(algorithmGroup, 16);
    rsa.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (rsa.getSelection())
          RSASignComposite.rsaAlgorithm = Config.RSA_SHA1;
      }
    });
    rsa.setText(Config.SIGN_TYPE_RSA);
    rsa.setBounds(120, 15, 84, 20);

    final Button rsa2 = new Button(algorithmGroup, 16);
    rsa2.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (rsa2.getSelection())
          RSASignComposite.rsaAlgorithm = Config.RSA_SHA256;
      }
    });
    rsa2.setText(Config.SIGN_TYPE_RSA2);
    rsa2.setBounds(5, 15, 84, 20);

    if (rsaAlgorithm.equalsIgnoreCase(Config.RSA_SHA256))
      rsa2.setSelection(true);
    else {
      rsa.setSelection(true);
    }

    Button button_5 = new Button(this, 0);
    button_5.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        SignUtils signUtils = new SignUtilsImpl();
        String pri_key = RSASignComposite.this.text_privatekey.getText().trim();
        if (StringUtils.isNotBlank(pri_key)) {
          if ((pri_key.contains("\r\n")) || (pri_key.contains("\n")))
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", 
              "商户应用私钥不能存在换行，请删除换行后再试！");
        }
        else
        {
          MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", 
            "请输入商户应用私钥！");
          return;
        }

        if ((RsaKey.check_rsa_type(pri_key) == 1) || (RsaKey.check_rsa_type(pri_key) == 4)) {
          try {
            String keyLength = RsaKey.getKeyLength(pri_key);
            pri_key = RsaKey.convert2pcks8(pri_key, keyLength);
          } catch (Exception e1) {
            e1.printStackTrace();
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", 
              "私钥错误，请输入正确的私钥！");
            return;
          }
        } else if ((RsaKey.check_rsa_type(pri_key) != 2) && (RsaKey.check_rsa_type(pri_key) != 5))
        {
          MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", 
            "格式错误，请输入正确的私钥！");
          return;
        }
        String waitSignContent = "";
        try
        {
          String inputParam = RSASignComposite.this.text_input.getText();

          if (RSASignComposite.this.inputParamDemo.equals(inputParam)) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "错误", 
              "请输入请求参数！");
            return;
          }
          String signType = GenerateRsaUtil.getUrlParams(inputParam, "sign_type");
          if (StringUtils.isBlank(signType)) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "错误", 
              "请检查输入参数,sign_type不能为空！");
            return;
          }
          if (rsa2.getSelection()) {
            if (!Config.SIGN_TYPE_RSA2.equals(signType)) {
              MessageDialog.openWarning(RSASignComposite.this.getShell(), "错误", 
                "签名方式错误，请检查输入参数中sign_type是否与所选签名方式相同！");
            }
          }
          else if ((rsa.getSelection()) && 
            (!Config.SIGN_TYPE_RSA.equals(signType))) {
            MessageDialog.openWarning(RSASignComposite.this.getShell(), "错误", 
              "签名方式错误，请检查输入参数中sign_type是否与所选签名方式相同！");
            return;
          }

          waitSignContent = RSASignComposite.this.getInputString(inputParam);
        } catch (Exception e1) {
          e1.printStackTrace();

          MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", "请求参数格式错误！");
          return;
        }
        String sign = "";
        try
        {
          sign = RSA.sign(waitSignContent, pri_key, RSASignComposite.rsaAlgorithm, RSASignComposite.charset);
        } catch (Exception ex) {
          MessageDialog.openWarning(RSASignComposite.this.getShell(), "警告", 
            "签名错误，请输入正确的私钥！");
        }
        RSASignComposite.this.text_sign.setText(sign);
        RSASignComposite.this.text_output.setText(RSASignComposite.this.waitCheckSignStr);

        RSASignComposite.this.button_6.setEnabled(true);
        try
        {
          RsaKey.appendTxtFile("签名步骤", "三、生成签名：\r\n");
          RsaKey.appendTxtFile("签名步骤", "【" + sign + "】\r\n");
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });
    button_5.setToolTipText("RSA私钥不能为空");
    button_5.setText("开始签名");
    button_5.setBounds(100, 300, 100, 35);

    this.button_6 = new Button(this, 0);
    this.button_6.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        try {
          RsaKey.openTxtFile("签名步骤");
        } catch (Exception ex) {
          MessageDialog.openWarning(RSASignComposite.this.getShell(), "错误", 
            "步骤文件打开失败！");
        }
      }
    });
    this.button_6.setToolTipText("RSA私钥不能为空");
    this.button_6.setText("查看签名生成步骤");
    this.button_6.setBounds(220, 300, 160, 35);
    this.button_6.setEnabled(false);

    Label label_line = new Label(this, 258);
    label_line.setBounds(10, 340, 770, 5);
    label_line.setText("output__sep");

    Label label_1 = new Label(this, 0);
    label_1.setBounds(5, 350, 90, 100);
    label_1.setText("待签名内容：");

    this.text_output = new Text(this, 2626);

    this.text_output.setBounds(100, 350, 670, 100);
    this.text_output.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_output));

    Label label_5 = new Label(this, 0);
    label_5.setBounds(5, 465, 90, 100);
    label_5.setText("签名(sign)：");

    this.text_sign = new Text(this, 2626);

    this.text_sign.setBounds(100, 465, 670, 100);
    this.text_sign.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_sign));

    Label label_info = new Label(this, 0);
    label_info.setBounds(30, 575, 720, 50);
    label_info.setText("将生成的“签名”赋值给sign参数，将sign放到请求参数中，再将每个参数值做urlencode后发起请求即可。");
  }

  protected void checkSubclass()
  {
  }

  public String getInputString(String inputParam)
    throws IOException
  {
    RsaKey.deleteTxtFile("签名步骤");

    RsaKey.appendTxtFile("签名步骤", "一、签名准备\r\n");

    RsaKey.appendTxtFile("签名步骤", "1.原始内容：\r\n");
    RsaKey.appendTxtFile("签名步骤", "【" + inputParam + "】\r\n\r\n");

    RsaKey.appendTxtFile("签名步骤", "2.签名私钥：\r\n");
    RsaKey.appendTxtFile("签名步骤", "【" + this.text_privatekey.getText() + "】\r\n\r\n");

    RsaKey.appendTxtFile("签名步骤", "二、生成待签名字符串：\r\n");

    RsaKey.appendTxtFile("签名步骤", "1.剔除参数名(和参数值)前后的空格：\r\n(正常业务下，参数值前后不应含有空格)\r\n");
    if (StringUtils.isEmpty(inputParam)) {
      return null;
    }
    inputParam = inputParam.trim();
    RsaKey.appendTxtFile("签名步骤", "【" + inputParam + "】\r\n\r\n");
    Integer step = Integer.valueOf(1);
    step = Integer.valueOf(step.intValue() + 1);

    String removeGatewayUrl = GenerateRsaUtil.removeUrlHead("签名步骤", inputParam, step);
    if (inputParam.equals(removeGatewayUrl))
      step = Integer.valueOf(2);
    else {
      step = Integer.valueOf(3);
    }
    inputParam = removeGatewayUrl;
    inputParam = GenerateRsaUtil.removeUrlEmptyParams(inputParam, "签名步骤");
    RsaKey.appendTxtFile("签名步骤", step + ".剔除空值参数：\r\n");
    RsaKey.appendTxtFile("签名步骤", "【" + inputParam + "】\r\n\r\n");
    step = Integer.valueOf(step.intValue() + 1);

    if ((StringUtil.isNotEmpty(inputParam)) && (inputParam.contains("sign="))) {
      inputParam = GenerateRsaUtil.removeUrlParams(inputParam, new String[] { "sign" });
    }

    inputParam = SupportUtil.sortParams(inputParam);
    RsaKey.appendTxtFile("签名步骤", step + ".排序：\r\n");
    RsaKey.appendTxtFile("签名步骤", "【" + inputParam + "】\r\n\r\n");
    this.waitCheckSignStr = inputParam;
    step = Integer.valueOf(step.intValue() + 1);

    return inputParam;
  }
}