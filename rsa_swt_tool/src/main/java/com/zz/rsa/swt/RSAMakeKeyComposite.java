package com.zz.rsa.swt;

import com.zz.rsa.bean.Config;
import com.zz.rsa.bean.Env;
import com.zz.rsa.utils.GenerateRsaUtil;
import com.zz.rsa.utils.RsaKey;
import com.zz.rsa.utils.SupportUtil;
import com.alipay.util.key.KeyTool;
import com.alipay.util.key.KeyType;
import com.swt.util.listener.KeyListenerUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public class RSAMakeKeyComposite extends Composite
{
  private Text text_privatekey;
  private Text text_publickey;
  private Label label_notice;
  private Button button_radio_java;
  private Button button_radio_other;
  private Button button_rsa1024;
  private Button button_rsa2048;
  private String privateKeyPath;

  public static void main(String[] args)
  {
    RSAWindow.main(args);
  }

  public RSAMakeKeyComposite(Composite parent, int style)
  {
    super(parent, style);
    setLayout(new FormLayout());
    Display display = parent.getDisplay();

   /* Link link_url = new Link(this, 0);
    link_url.setText("<A>使用说明</A>");
    link_url.setToolTipText("点击查看生成密钥使用说明");
    link_url.setFont(new Font(display, "宋体", 10, 0));

    FormData fd_link_url = new FormData();
    link_url.setLayoutData(fd_link_url);
    fd_link_url.top = new FormAttachment(0, 10);
    fd_link_url.left = new FormAttachment(0, 700);
    fd_link_url.height = 20;
    fd_link_url.width = 80;

    link_url.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        RsaKey.openBrowserForUrl("https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=105971&docType=1");
      }
    });
    Link update_link_url = new Link(this, 0);
    update_link_url.setText("<A>支付平台RSA签名升级公告</A>");
    update_link_url.setToolTipText("点击查看支付平台RSA签名升级公告");
    update_link_url.setFont(new Font(display, "宋体", 10, 0));

    FormData fd_update_link_url = new FormData();
    update_link_url.setLayoutData(fd_update_link_url);
    fd_update_link_url.top = new FormAttachment(0, 10);
    fd_update_link_url.left = new FormAttachment(0, 90);
    fd_update_link_url.height = 20;
    fd_update_link_url.width = 300;

    update_link_url.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        RsaKey.openBrowserForUrl("https://open.alipay.com/platform/announcement.htm?id=2");
      }
    });*/
    //生成key按钮
    Button mk_key_btn = new Button(this, 0);
    mk_key_btn.setText("Generate key");

    FormData fd_mk_key_btn = new FormData();
    fd_mk_key_btn.left = new FormAttachment(0, 110);
    mk_key_btn.setLayoutData(fd_mk_key_btn);
    fd_mk_key_btn.height = 35;
    fd_mk_key_btn.width = 80;

    final Button open_key_btn = new Button(this, 0);
    open_key_btn.setToolTipText("Need to generate a public key!");
    fd_mk_key_btn.right = new FormAttachment(open_key_btn, -10);
    open_key_btn.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e) {
        try {
          if (StringUtils.isBlank(RSAMakeKeyComposite.this.privateKeyPath)) {
            //打开文件夹失败，可能文件目录不存在，请重新生成密钥
            RSAMakeKeyComposite.this.label_notice.setText("Failed to open the folder, the file directory may not exist, please regenerate the key\n");
            return;
          }

          if (Env.isMac())
          {
            GenerateRsaUtil.runCMD(new String[] { "open", RSAMakeKeyComposite.this.privateKeyPath });
          }
          else {
            GenerateRsaUtil.runCMD("cmd /c start \"\"  \"" + RSAMakeKeyComposite.this.privateKeyPath + "\"");
          }
        } catch (Exception e1) {
          //打开私钥文件失败，原因：系统异常
          //打开文件夹失败，可能文件目录不存在，请重新生成密钥
          GenerateRsaUtil.info("Failed to open private key file, reason: system exception {}", new Object[] { e1 });
          RSAMakeKeyComposite.this.label_notice.setText("Failed to open the folder, the file directory may not exist, please regenerate the key\n");
          return;
        }
      }
    });
    //打开密钥文件路径
    open_key_btn.setText("open Key File Path");
    open_key_btn.setEnabled(false);

    FormData fd_op_key_btn = new FormData();
    fd_op_key_btn.left = new FormAttachment(0, 206);
    open_key_btn.setLayoutData(fd_op_key_btn);
    fd_op_key_btn.height = 35;
    fd_op_key_btn.width = 160;

    final Button clip_button = new Button(this, 8);
    //点击复制文本框中信息至剪切板
    clip_button.setToolTipText("Click to copy the information in the text box to the clipboard");
    clip_button.setText("Copy private key");
    clip_button.setEnabled(false);
    FormData fd_clip_button = new FormData();

    clip_button.setLayoutData(fd_clip_button);

    clip_button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e) {
        String output = RSAMakeKeyComposite.this.text_privatekey.getText().trim();
        if ((output == null) || ("".equals(output))) {
          //文本框中无文本信息
          RSAMakeKeyComposite.this.label_notice.setText("No text information in the text box\n");
          return;
        }

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(output);
        clip.setContents(tText, null);
        //已复制私钥至剪切板
        RSAMakeKeyComposite.this.label_notice.setText("The private key has been copied to the clipboard\n");
      }
    });
    this.text_privatekey = new Text(this, 2626);

    FormData fd_text_privatekey = new FormData();
    fd_text_privatekey.bottom = new FormAttachment(70);
    fd_text_privatekey.top = new FormAttachment(mk_key_btn, 10);
    fd_text_privatekey.right = new FormAttachment(100, -10);
    this.text_privatekey.setLayoutData(fd_text_privatekey);
    this.text_privatekey.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_privatekey));

    fd_clip_button.bottom = new FormAttachment(this.text_privatekey, 250);
    fd_clip_button.left = new FormAttachment(this.text_privatekey, -100);
    fd_clip_button.height = 35;
    fd_clip_button.width = 80;
    final Button clip_public_button = new Button(this, 8);
    //点击复制公钥文本框中信息至剪切板
    clip_public_button.setToolTipText("Click to copy the information in the public key text box to the clipboard");
    clip_public_button.setText("Copy public key");
    clip_public_button.setEnabled(false);
    FormData fd_clip_public_button = new FormData();

    clip_public_button.setLayoutData(fd_clip_public_button);

    clip_public_button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e) {
        String output = RSAMakeKeyComposite.this.text_publickey.getText().trim();
        if ((output == null) || ("".equals(output))) {
          RSAMakeKeyComposite.this.label_notice.setText("No text information in the text box\n");
          return;
        }

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(output);
        clip.setContents(tText, null);
        //已复制公钥至剪切板
        RSAMakeKeyComposite.this.label_notice.setText("The public key has been copied to the clipboard\n");
      }
    });
    /**
     * 上传公钥
     */
    Label lblDevLanguage_upload = new Label(this, 0);
    lblDevLanguage_upload.setText("please upload the public key to platform");
    FormData lblDevLanguage_form_a = new FormData();
    lblDevLanguage_upload.setLayoutData(lblDevLanguage_form_a);


//    Link link = new Link(this, 0);
//    link.setText("<A>上传公钥</A>");
//    link.setToolTipText("点击上传公钥到开放平台");
//    link.setFont(new Font(display, "宋体", 10, 0));
//
//    FormData fd_link = new FormData();
//    link.setLayoutData(fd_link);
//
//    link.addSelectionListener(new SelectionAdapter() {
//      @Override
//      public void widgetSelected(SelectionEvent event) {
//        RsaKey.openBrowserForUrl("https://openhome.alipay.com/platform/keyManage.htm");
//      }
//    });

    Label label = new Label(this, 0);
    fd_text_privatekey.left = new FormAttachment(label, 5);
    FormData fd_label = new FormData();
    fd_label.left = new FormAttachment(1, 0);
    label.setLayoutData(fd_label);
    label.setText("privateKey:");

    Label label_1 = new Label(this, 0);
    label_1.setText("publicKey:");
    FormData fd_label_1 = new FormData();
    fd_label_1.top = new FormAttachment(label, 130);
    fd_label_1.left = new FormAttachment(1, 0);
    fd_label_1.bottom = new FormAttachment(100, 10);
    label_1.setLayoutData(fd_label_1);

    this.text_publickey = new Text(this, 2626);

    FormData fd_text_publickey = new FormData();
    fd_text_publickey.top = new FormAttachment(this.text_privatekey, 15);
    fd_text_publickey.left = new FormAttachment(label_1, 5);
    fd_text_publickey.right = new FormAttachment(100, -10);
    fd_text_publickey.bottom = new FormAttachment(100, -60);
    this.text_publickey.setLayoutData(fd_text_publickey);
    this.text_publickey.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_publickey));

  /*  fd_link.top = new FormAttachment(this.text_publickey, 15);
    fd_link.left = new FormAttachment(this.text_publickey, -90);
    fd_link.height = 35;
    fd_link.width = 80;*/

    lblDevLanguage_form_a.top =   new FormAttachment(this.text_publickey, 35);
    lblDevLanguage_form_a.left =  new FormAttachment(this.text_publickey, -280);
    lblDevLanguage_form_a.height = 30;
    lblDevLanguage_form_a.width = 300;

    fd_clip_public_button.bottom = new FormAttachment(this.text_publickey, 107);
    fd_clip_public_button.left = new FormAttachment(this.text_publickey, -100);
    fd_clip_public_button.height = 35;
    fd_clip_public_button.width = 80;

    Group group_devLanguage = new Group(this, 32);
    FormData fd_group_devLanguage = new FormData();
    fd_group_devLanguage.top = new FormAttachment(0, 25);
    fd_group_devLanguage.left = new FormAttachment(label, 5);
    fd_group_devLanguage.right = new FormAttachment(100, -10);
    fd_group_devLanguage.height = 25;
    group_devLanguage.setLayoutData(fd_group_devLanguage);

    Group group = new Group(this, 32);
    fd_mk_key_btn.top = new FormAttachment(group, 5);
    fd_op_key_btn.top = new FormAttachment(group, 5);
    fd_label.top = new FormAttachment(group, 169);
    FormData fd_group = new FormData();
    fd_group.top = new FormAttachment(group_devLanguage, 5);
    fd_group.left = new FormAttachment(label, 5);
    fd_group.right = new FormAttachment(100, -10);
    fd_group.height = 25;
    group.setLayoutData(fd_group);

    this.button_radio_java = new Button(group_devLanguage, 16);
    this.button_radio_java.setSelection(true);
    this.button_radio_java.setLocation(5, 18);
    this.button_radio_java.setSize(72, 13);
    this.button_radio_java.setText("PKCS8(for JAVA)");
    this.button_radio_java.pack();

    this.button_radio_other = new Button(group_devLanguage, 16);
    this.button_radio_other.setLocation(160, 18);
    this.button_radio_other.setSize(72, 13);
    this.button_radio_other.setText("PKCS1(not JAVA)");
    this.button_radio_other.pack();

    this.button_rsa1024 = new Button(group, 16);
    this.button_rsa1024.setLocation(160, 18);
    this.button_rsa1024.setSize(72, 13);
    this.button_rsa1024.setText("1024");
    this.button_rsa1024.pack();

    this.button_rsa2048 = new Button(group, 16);
    this.button_rsa2048.setSelection(true);
    this.button_rsa2048.setLocation(5, 18);
    this.button_rsa2048.setSize(72, 13);
    this.button_rsa2048.setText("2048");
    this.button_rsa2048.pack();

    Label lblDevLanguage = new Label(this, 0);
    lblDevLanguage.setText("Key format：");
    FormData lblDevLanguage_form = new FormData();
    lblDevLanguage_form.top = new FormAttachment(1, 35);
    lblDevLanguage_form.left = new FormAttachment(1, 0);
    lblDevLanguage_form.height = 30;
    lblDevLanguage_form.width = 70;
    lblDevLanguage.setLayoutData(lblDevLanguage_form);

    Label lblNewLabel = new Label(this, 0);
    lblNewLabel.setText("Key length：");
    FormData lblNewLabel_form = new FormData();
    lblNewLabel_form.top = new FormAttachment(group_devLanguage, 20);
    lblNewLabel_form.left = new FormAttachment(1, 0);
    lblNewLabel_form.height = 30;
    lblNewLabel_form.width = 70;
    lblNewLabel.setLayoutData(lblNewLabel_form);

    this.label_notice = new Label(this, 64);
    this.label_notice.setText("\r\n");
    FormData fd_label_2 = new FormData();
    fd_label_2.left = new FormAttachment(0, 100);
    fd_label_2.top = new FormAttachment(this.text_publickey, 2);
    fd_label_2.height = 60;
    fd_label_2.width = 550;

    this.label_notice.setLayoutData(fd_label_2);

    mk_key_btn.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e) {
        KeyType keyType = RSAMakeKeyComposite.this.getKeyType();
        String devLanguage = RSAMakeKeyComposite.this.getDevLanguage();
        try
        {
          if (Config.DEVLEPMENT_LANGUAGE_JAVA.equals(devLanguage)) {
            String[] result = KeyTool.generateKey(keyType);
            if ((result == null) || (result.length != 2)) {
              //生成密钥失败
              RSAMakeKeyComposite.this.label_notice.setText("Failed to generate key!\n");
            }
            RSAMakeKeyComposite.this.text_privatekey.setText(result[0]);
            RSAMakeKeyComposite.this.text_publickey.setText(result[1]);
            RSAMakeKeyComposite.this.privateKeyPath = SupportUtil.writeKeyFile(new String[] { "merchant privateKey" + keyType.getLength(), "merchant publicKey" + keyType.getLength() }, result);
            RSAMakeKeyComposite.this.label_notice.setText("The key file was successfully saved to【" + RSAMakeKeyComposite.this.privateKeyPath + "】,Click the 'open Key File Path' button to view");
          }
          else {
            String privateKeyFile = GenerateRsaUtil.mkRsaPrivateKeyFile(String.valueOf(keyType.getLength()));

            RSAMakeKeyComposite.this.privateKeyPath = privateKeyFile.substring(0, 
              privateKeyFile.lastIndexOf(File.separator));

            //公钥生成失败，请重新生成公钥
            String publicKeyFile = GenerateRsaUtil.mkRsaPublicKeyFile(privateKeyFile, Integer.valueOf(keyType.getLength()));
            if (StringUtils.isBlank(publicKeyFile)) {
              RSAMakeKeyComposite.this.label_notice.setText("Public key generation failed, please regenerate the key\n");
              return;
            }
            String publicKey = GenerateRsaUtil.getKeyFromFile(publicKeyFile);
            if (StringUtils.isBlank(publicKey)) {
              RSAMakeKeyComposite.this.label_notice.setText("Public key generation failed, please regenerate public key\n");
              return;
            }
            String privateKey = GenerateRsaUtil.getKeyFromFile(privateKeyFile);
            if (StringUtils.isBlank(publicKey)) {
              RSAMakeKeyComposite.this.label_notice.setText("Public key generation failed, please regenerate public key\n");
              return;
            }
            RSAMakeKeyComposite.this.text_privatekey.setText(privateKey);
            RSAMakeKeyComposite.this.text_publickey.setText(publicKey);
            GenerateRsaUtil.clearHeadBottom(Integer.valueOf(keyType.getLength()));
            RSAMakeKeyComposite.this.label_notice.setText("The key file was successfully saved to [" + RSAMakeKeyComposite.this.privateKeyPath + "],Click the 'Open Key File Path' button to view");
          }
          open_key_btn.setEnabled(true);
          clip_button.setEnabled(true);
          clip_public_button.setEnabled(true);
        } catch (Exception e1) {
          RSAMakeKeyComposite.this.label_notice.setText("Generate key exception。\n");
          e1.printStackTrace();
        }
      }
    });
  }

  @Override
  protected void checkSubclass()
  {
  }

  public KeyType getKeyType()
  {
    if (this.button_rsa1024.getSelection()) {
      return KeyType.RSA1024;
    }
    if (this.button_rsa2048.getSelection()) {
      return KeyType.RSA2048;
    }
    return KeyType.RSA1024;
  }

  public String getDevLanguage()
  {
    if (this.button_radio_java.getSelection()) {
      return Config.DEVLEPMENT_LANGUAGE_JAVA;
    }
    if (this.button_radio_other.getSelection()) {
      return Config.DEVLEPMENT_LANGUAGE_OTHER;
    }
    return Config.DEVLEPMENT_LANGUAGE_OTHER;
  }

  public String getDateString()
  {
    return new Date().toLocaleString();
  }
}