package com.zz.rsa.swt;

import com.zz.rsa.bean.Config;
import com.zz.rsa.bean.Env;
import com.zz.rsa.utils.GenerateRsaUtil;
import com.zz.rsa.utils.RsaKey;
import com.zz.rsa.utils.SupportUtil;
import com.swt.util.dialog.FileDialogUtil;
import com.swt.util.listener.KeyListenerUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

public class RSAKeyFormatComposite extends Composite
{
  private Text text_input_rsa;
  private Text text_output_rsa;
  private String privateKeyPath = Config.KEY_SAVE_PATH;

  private String inputPrivateKeyDemo = "请输入商户应用私钥，此私钥是商户调用支付平台接口进行交易的唯一凭证，请谨慎保管！";

  public RSAKeyFormatComposite(Composite parent, int style)
  {
    super(parent, style);

    final Button clip_public_button = new Button(this, 8);
    clip_public_button.setBounds(646, 543, 100, 35);
    clip_public_button.setToolTipText("点击复制文本框中信息至剪切板");
    clip_public_button.setText("复制");
    clip_public_button.setEnabled(false);
    FormData fd_clip_public_button = new FormData();

    clip_public_button.setLayoutData(fd_clip_public_button);

    clip_public_button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String output = RSAKeyFormatComposite.this.text_output_rsa.getText().trim();
        if ((output == null) || ("".equals(output))) {
          RSAKeyFormatComposite.this.text_output_rsa.setText("文本框中无文本信息。\n");
          return;
        }

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(output);
        clip.setContents(tText, null);
      }
    });
    this.text_input_rsa = new Text(this, 2624);
    this.text_input_rsa.setBounds(100, 60, 670, 240);
    this.text_input_rsa.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_input_rsa));
    this.text_input_rsa.setForeground(getDisplay().getSystemColor(15));
    this.text_input_rsa.setText(this.inputPrivateKeyDemo);
    this.text_input_rsa.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSAKeyFormatComposite.this.inputPrivateKeyDemo.equals(RSAKeyFormatComposite.this.text_input_rsa.getText())) {
          RSAKeyFormatComposite.this.text_input_rsa.setText("");
          RSAKeyFormatComposite.this.text_input_rsa.setForeground(RSAKeyFormatComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_input_rsa.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSAKeyFormatComposite.this.text_input_rsa.getText())) {
          RSAKeyFormatComposite.this.text_input_rsa.setText(RSAKeyFormatComposite.this.inputPrivateKeyDemo);
          RSAKeyFormatComposite.this.text_input_rsa.setForeground(RSAKeyFormatComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label label = new Label(this, 0);
    label.setText("merchant\r\n privateKey：");
    label.setBounds(5, 150, 90, 40);

    Label label_1 = new Label(this, 0);
    label_1.setText("out：");
    label_1.setBounds(5, 430, 90, 17);

    this.text_output_rsa = new Text(this, 2624);
    this.text_output_rsa.setBounds(100, 360, 670, 220);
    this.text_output_rsa.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_output_rsa));

    Button button = new Button(this, 0);
    button.setBounds(100, 15, 100, 35);
    button.setText("read in local file");
    button.setToolTipText("将密钥文件读入到文本框中，转成一行。");
    button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String file = FileDialogUtil.openGetFileDialog(RSAKeyFormatComposite.this.getShell());
        if ((!StringUtils.isEmpty(file)) && (new File(file).exists()))
          try {
            RSAKeyFormatComposite.this.text_input_rsa.setForeground(RSAKeyFormatComposite.this.getDisplay().getSystemColor(2));
            RSAKeyFormatComposite.this.text_input_rsa.setText(RsaKey.getKeyFromFile(file));
          }
          catch (Exception e1) {
            e1.printStackTrace();
          }
      }
    });
    final Button open_key_btn = new Button(this, 0);
    open_key_btn.setToolTipText("需要生成公钥！");
    open_key_btn.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        try {
          if (StringUtils.isEmpty(RSAKeyFormatComposite.this.privateKeyPath))
          {
            return;
          }

          if (Env.isMac())
          {
            GenerateRsaUtil.runCMD(new String[] { "open", RSAKeyFormatComposite.this.privateKeyPath });
          }
          else GenerateRsaUtil.runCMD("cmd /c start \" \"  \"" + RSAKeyFormatComposite.this.privateKeyPath + "\"");
        }
        catch (Exception e1)
        {
          GenerateRsaUtil.info("打开私钥文件失败，原因：系统异常 {}", new Object[] { e1 });

          return;
        }
      }
    });
    open_key_btn.setText("打开密钥文件路径");
    open_key_btn.setBounds(620, 313, 130, 35);
    open_key_btn.setEnabled(false);

    Button button_1 = new Button(this, 0);
    button_1.setToolTipText("请先选择私钥文件，此处调用OpenSSL.exe工具由私钥生成对应公钥");
    button_1.setText("生成公钥文件");
    button_1.setBounds(490, 313, 120, 35);
    button_1.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String privateKey = SupportUtil.filterLineSeparator(RSAKeyFormatComposite.this.text_input_rsa.getText().trim());
        if (StringUtils.isEmpty(privateKey)) {
          RSAKeyFormatComposite.this.text_output_rsa.setText("请放入私钥或私钥文件！");
          return;
        }
        try {
          int keyFormat = RsaKey.check_rsa_type(privateKey);
          switch (keyFormat) {
          case 1:
          case 2:
          case 4:
          case 5:
            String keyLength = RsaKey.getKeyLength(privateKey);
            String privateKeyFilePath = RsaKey.convert2KeyFile(privateKey, keyLength);
            String publicKeyFileString = 
              RsaKey.mkRsaPublicKeyFile(privateKeyFilePath, keyLength);
            clip_public_button.setEnabled(true);
            open_key_btn.setEnabled(true);
            String publicKey = RsaKey.getKeyFromFile(publicKeyFileString);
            RsaKey.clearHeadBottom(keyLength);
            RSAKeyFormatComposite.this.text_output_rsa.setText(publicKey);
            break;
          case 3:
          case 6:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的是公钥，请输入私钥！");
            break;
          default:
            RSAKeyFormatComposite.this.text_output_rsa.setText("私钥不合法，请输入1024/2048位的RSA PKCS1(非JAVA适用)私钥，并确认去掉了头尾多余信息、空格、换行。");
          }
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
        catch (InterruptedException e1) {
          e1.printStackTrace();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    Button btnpkcs = new Button(this, 0);
    btnpkcs.setToolTipText("请先选择私钥文件");
    btnpkcs.setText("转PKCS8(JAVA适用)私钥");
    btnpkcs.setBounds(100, 313, 180, 35);
    btnpkcs.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        try {
          String text_input_rsa_str = 
            SupportUtil.filterLineSeparator(RSAKeyFormatComposite.this.text_input_rsa.getText()
            .trim());
          int keyFormat = RsaKey.check_rsa_type(text_input_rsa_str);
          switch (keyFormat) {
          case 1:
            String pkcs8_1024 = 
              RsaKey.convert2pcks8(text_input_rsa_str, "1024");
            RsaKey.clearHeadBottom("1024");
            RSAKeyFormatComposite.this.text_output_rsa.setText(pkcs8_1024);
            clip_public_button.setEnabled(true);
            open_key_btn.setEnabled(true);
            break;
          case 2:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的已经是1024长度的PKCS8(JAVA适用)私钥");
            break;
          case 3:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的是1024长度的公钥，请输入私钥！");
            break;
          case 4:
            String pkcs8_2048 = 
              RsaKey.convert2pcks8(text_input_rsa_str, "2048");
            RsaKey.clearHeadBottom("2048");
            RSAKeyFormatComposite.this.text_output_rsa.setText(pkcs8_2048);
            clip_public_button.setEnabled(true);
            open_key_btn.setEnabled(true);
            break;
          case 5:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的已经是2048长度的PKCS8(JAVA适用)私钥");
            break;
          case 6:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的是2048长度的公钥，请输入私钥！");
            break;
          default:
            RSAKeyFormatComposite.this.text_output_rsa.setText("私钥不合法，请输入1024/2048位的RSA PKCS1(非JAVA适用)私钥，并确认去掉了头尾多余信息、空格、换行。");
          }
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
        }
      }
    });
    Button btnpkcs_no = new Button(this, 0);
    btnpkcs_no.setToolTipText("请先选择PKCS8(JAVA适用)的私钥文件");
    btnpkcs_no.setText("转PKCS1(非JAVA适用)私钥");
    btnpkcs_no.setBounds(290, 313, 190, 35);
    btnpkcs_no.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        try {
          String text_input_rsa_str = 
            SupportUtil.filterLineSeparator(RSAKeyFormatComposite.this.text_input_rsa.getText()
            .trim());
          int keyFormat = RsaKey.check_rsa_type(text_input_rsa_str);
          switch (keyFormat) {
          case 1:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的已经是1024长度的PKCS1(非JAVA适用)私钥");
            break;
          case 2:
            String pkcs8_1024 = 
              RsaKey.convertPcks82Original(text_input_rsa_str, "1024");
            RsaKey.clearHeadBottom("2048");
            RSAKeyFormatComposite.this.text_output_rsa.setText(pkcs8_1024);
            clip_public_button.setEnabled(true);
            open_key_btn.setEnabled(true);
            break;
          case 3:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的是1024长度的公钥，请输入私钥！");
            break;
          case 4:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的已经是2048长度的PKCS1(非JAVA适用)私钥！");
            break;
          case 5:
            String pkcs8_2048 = 
              RsaKey.convertPcks82Original(text_input_rsa_str, "2048");
            RsaKey.clearHeadBottom("2048");
            RSAKeyFormatComposite.this.text_output_rsa.setText(pkcs8_2048);
            clip_public_button.setEnabled(true);
            open_key_btn.setEnabled(true);

            break;
          case 6:
            RSAKeyFormatComposite.this.text_output_rsa.setText("输入的是2048长度的公钥，请输入私钥！");
            break;
          default:
            RSAKeyFormatComposite.this.text_output_rsa.setText("私钥不合法，请输入1024/2048位的RSA PKCS1(非JAVA适用)私钥，并确认去掉了头尾多余信息、空格、换行。");
          }

        }
        catch (Exception e1)
        {
          e1.printStackTrace();
        }
      }
    });
  }

  protected void checkSubclass()
  {
  }

  public static void main(String[] args)
  {
    RSAWindow.main(args);
    RSAWindow.tabFolder.setSelection(1);
  }
}