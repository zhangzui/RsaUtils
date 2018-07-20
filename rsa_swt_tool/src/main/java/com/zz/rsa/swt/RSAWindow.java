package com.zz.rsa.swt;

/**
 * @author zhangzuizui
 * @date 2018/7/20 15:44
 */

import com.zz.rsa.bean.Config;
import com.zz.rsa.bean.Env;
import com.zz.rsa.utils.SupportUtil;
import com.swt.util.menu.MyTabFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import java.io.File;
import java.io.IOException;

public class RSAWindow
{
    public static Shell shell;
    public static RSASignComposite rsaSignComposite;
    public static RSACheckSignComposite rsaCheckSignComposite;
    public static MyTabFolder mTabFolder;
    public static TabFolder tabFolder;

    public static void main(String[] args) {
        Display display = Display.getDefault();
        shell = new Shell(display, 64);
        shell.setText("  RSA签名验签工具(V1.4)  ");
        showLogo();

        if (Env.isWindow()) {
            String fileName = Config.KEY_SAVE_PATH + "应用私钥_tmp.txt";
            if ((fileName != null) && (fileName.contains("  "))) {
                MessageDialog.openWarning(shell, "警告",
                        "文件路径不能有连续两个以上空格！\r\n" + fileName);
                return;
            }
        }

        createComposite();

        shell.setSize(800, 680);
        shell.setLocation(300, 50);

        shell.open();
        shell.layout();
        shell.addShellListener(new ShellAdapter()
        {
            public void shellClosed(ShellEvent e)
            {
                super.shellClosed(e);
            }
        });
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        System.exit(0);
    }

    public static void showLogo() {
        String logoFile = "logo.png";

        if (!new File(logoFile).exists()) {
            try {
                SupportUtil.inputStream2File(RSAWindow.class
                        .getResourceAsStream("/img/logo.png"), logoFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        String os = Env.os_name.toUpperCase();
        if ((os.contains("XP")) || (os.contains("2003"))) {
            return;
        }
        if (new File(logoFile).exists()) {
            Image image = new Image(Display.getCurrent(), logoFile);
            shell.setImage(image);
        }
    }

    public static void createComposite()
    {
        shell.setLayout(new FillLayout());

        mTabFolder = new MyTabFolder(shell);
        tabFolder = mTabFolder.getTabFolder();

        RSAMakeKeyComposite rsaDsaComposite = new RSAMakeKeyComposite(tabFolder, 0);
        mTabFolder.addControl("GetKey", rsaDsaComposite);

        rsaSignComposite = new RSASignComposite(tabFolder, 0);
        mTabFolder.addControl("Sign", rsaSignComposite);

        rsaCheckSignComposite = new RSACheckSignComposite(tabFolder, 0);
        mTabFolder.addControl("CHeckSign", rsaCheckSignComposite);

//        RSAKeyFormatComposite sslKeyFormatComposite = new RSAKeyFormatComposite(tabFolder, 0);
//        mTabFolder.addControl("格式转换", sslKeyFormatComposite);

        RSAKeyMatchComposite rSAKeyMatchComposite = new RSAKeyMatchComposite(tabFolder, 0);
        mTabFolder.addControl("KeyMatch", rSAKeyMatchComposite);
    }
}
