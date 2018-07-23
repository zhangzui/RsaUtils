package com.zz.rsa.swt;

import com.zz.rsa.bean.Config;
import com.zz.rsa.utils.RsaKey;
import com.zz.rsa.utils.SupportUtil;
import com.swt.util.dialog.FileDialogUtil;
import com.swt.util.listener.KeyListenerUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

public class RSAKeyMatchComposite extends Composite
{
  private Text text_private_key;
  private Text text_public_key;
  private Text text_output;
  private String privateKeyPath = Config.KEY_SAVE_PATH;

  private String inputPrivateKeyDemo = "Please enter the merchant application private key. This private key is the only document that the merchant calls the payment platform interface to conduct the transaction. Please keep it cautious!";
  private String inputPublicKeyDemo = "Please enter the merchant application public key, please make sure that it is the same as the application public key uploaded to the payment platform open platform!";

  public RSAKeyMatchComposite(Composite parent, int style)
  {
    super(parent, style);

    this.text_private_key = new Text(this, 2624);
    this.text_private_key.setBounds(100, 20, 670, 220);
    this.text_private_key.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_private_key));
    this.text_private_key.setForeground(getDisplay().getSystemColor(15));
    this.text_private_key.setText(this.inputPrivateKeyDemo);
    this.text_private_key.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSAKeyMatchComposite.this.inputPrivateKeyDemo.equals(RSAKeyMatchComposite.this.text_private_key.getText())) {
          RSAKeyMatchComposite.this.text_private_key.setText("");
          RSAKeyMatchComposite.this.text_private_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_private_key.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSAKeyMatchComposite.this.text_private_key.getText())) {
          RSAKeyMatchComposite.this.text_private_key.setText(RSAKeyMatchComposite.this.inputPrivateKeyDemo);
          RSAKeyMatchComposite.this.text_private_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label label = new Label(this, 0);
    label.setText("privateKey:");
    label.setBounds(5, 80, 90, 40);

    Button private_button = new Button(this, 0);
    private_button.setBounds(2, 130, 90, 35);
    private_button.setText("read in file");
    private_button.setToolTipText("Read the private key file into the text box and turn it into a line.");
    private_button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String file = FileDialogUtil.openGetFileDialog(RSAKeyMatchComposite.this.getShell());
        if ((!StringUtils.isEmpty(file)) && (new File(file).exists()))
          try {
            RSAKeyMatchComposite.this.text_private_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(2));
            RSAKeyMatchComposite.this.text_private_key.setText(RsaKey.getKeyFromFile(file));
          }
          catch (Exception e1) {
            e1.printStackTrace();
          }
      }
    });
    this.text_public_key = new Text(this, 2624);
    this.text_public_key.setBounds(100, 260, 670, 170);
    this.text_public_key.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_public_key));

    this.text_public_key.setForeground(getDisplay().getSystemColor(15));
    this.text_public_key.setText(this.inputPublicKeyDemo);
    this.text_public_key.addListener(15, new Listener() {
      public void handleEvent(Event e) {
        if (RSAKeyMatchComposite.this.inputPublicKeyDemo.equals(RSAKeyMatchComposite.this.text_public_key.getText())) {
          RSAKeyMatchComposite.this.text_public_key.setText("");
          RSAKeyMatchComposite.this.text_public_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(2));
        }
      }
    });
    this.text_public_key.addListener(16, new Listener() {
      public void handleEvent(Event e) {
        if (StringUtils.isEmpty(RSAKeyMatchComposite.this.text_public_key.getText())) {
          RSAKeyMatchComposite.this.text_public_key.setText(RSAKeyMatchComposite.this.inputPublicKeyDemo);
          RSAKeyMatchComposite.this.text_public_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(15));
        }
      }
    });
    Label label_1 = new Label(this, 0);
    label_1.setText("publicKey:");
    label_1.setBounds(5, 280, 90, 40);

    Button publice_button = new Button(this, 0);
    publice_button.setBounds(2, 330, 90, 35);
    publice_button.setText("read in file ");
    publice_button.setToolTipText("Read the public key file into the text box and turn it into a line.");
    publice_button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String file = FileDialogUtil.openGetFileDialog(RSAKeyMatchComposite.this.getShell());
        if ((!StringUtils.isEmpty(file)) && (new File(file).exists())) {
          try {
            RSAKeyMatchComposite.this.text_public_key.setForeground(RSAKeyMatchComposite.this.getDisplay().getSystemColor(2));
            RSAKeyMatchComposite.this.text_public_key.setText(RsaKey.getKeyFromFile(file));
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
      }
    });
    Button button_1 = new Button(this, 0);
    button_1.setToolTipText("Please fill in the private key and public key respectively in the application private key input box and the application public key input box.");
    button_1.setText("match");
    button_1.setBounds(370, 450, 100, 35);
    button_1.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        String textprivateKey = SupportUtil.filterLineSeparator(RSAKeyMatchComposite.this.text_private_key.getText().trim());
        String textpublicKey = SupportUtil.filterLineSeparator(RSAKeyMatchComposite.this.text_public_key.getText().trim());
        if (StringUtils.isEmpty(textprivateKey)) {
          RSAKeyMatchComposite.this.text_output.setText("Please enter the private key!");
          return;
        }
        if (StringUtils.isEmpty(textpublicKey)) {
          RSAKeyMatchComposite.this.text_output.setText("Please enter the public key!");
          return;
        }

        try
        {
          String keyLength = RsaKey.getKeyLength(textprivateKey);
          String privateKeyFilePath = RsaKey.convert2TempKeyFile(textprivateKey, keyLength);
          String publicKeyFileString = RsaKey.mkTempRsaPublicKeyFile(privateKeyFilePath, keyLength);
          String publicKey = RsaKey.getKeyFromFile(publicKeyFileString);
          if ((publicKey == null) || ("".equals(publicKey))) {
            RSAKeyMatchComposite.this.text_output.setText("An error occurred during key matching. Please confirm that the private key is correct, or there is no space in the file path!");
          }
          else if (textpublicKey.equals(publicKey))
            RSAKeyMatchComposite.this.text_output.setText("Matching is successful!");
          else {
            RSAKeyMatchComposite.this.text_output.setText("Match failed!");
          }

          new File(privateKeyFilePath).delete();
          new File(publicKeyFileString).delete();
        }
        catch (IOException e1) {
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
    Label label_2 = new Label(this, 0);
    label_2.setText("Match result:");
    label_2.setBounds(5, 530, 90, 40);

    this.text_output = new Text(this, 2624);
    this.text_output.setBounds(100, 505, 670, 60);
    this.text_output.addKeyListener(
      KeyListenerUtil.mkCtrlAKeyListener(this.text_output));
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