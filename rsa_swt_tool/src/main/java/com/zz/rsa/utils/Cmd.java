package com.zz.rsa.utils;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class Cmd
{
  private Process process;
  private static BufferedReader br;

  public Cmd()
  {
  }

  public Cmd(String commandLineString)
    throws IOException
  {
    this.process = exeCmd(commandLineString);
  }

  public Cmd(String[] commandLineArray) throws IOException {
    String commandLineString = "";
    for (String string : commandLineArray) {
      commandLineString = commandLineString + string;
    }
    this.process = exeCmd(commandLineString);
  }

  public Process getProcess() {
    return this.process;
  }

  public void exec(String commandLineString)
    throws IOException
  {
    this.process = exeCmd(commandLineString);
  }

  public void execWriteBytes(String commandLine)
    throws IOException
  {
    DataOutputStream dos = new DataOutputStream(this.process.getOutputStream());
    dos.writeBytes(commandLine + "\n");
    dos.flush();
  }

  public int exec(String commandLineString, long timeout)
    throws IOException, TimeoutException, InterruptedException
  {
    this.process = exeCmd(commandLineString);
    WaitForThread wait = new WaitForThread(this.process);
    wait.start();
    try
    {
      wait.join(timeout);
      if (wait.exit != null) {
        int i = wait.exit.intValue(); return i;
      }
      System.out.println("超时异常。");
      wait.interrupt();
      this.process.destroy();
    }
    catch (InterruptedException e)
    {
      System.out.println("中断 超时异常。");
      wait.interrupt();
      this.process.destroy();
    }
    finally
    {
      this.process.destroy();
    }
    return 0;
  }

  private Process exeCmd(String commandLineString)
    throws IOException
  {
    Runtime runtime = Runtime.getRuntime();
    return runtime.exec(commandLineString);
  }

  public int getExitCode()
    throws InterruptedException
  {
    return this.process.waitFor();
  }

  public void close()
    throws IOException, InterruptedException
  {
    this.process.getOutputStream().close();
    this.process.getInputStream().close();
    this.process.getErrorStream().close();
    if (getExitCode() != 0) {
      this.process.destroy();
      this.process = null;
    }
  }

  public void forceClose(long waitTime)
    throws IOException, InterruptedException
  {
    Thread.sleep(waitTime);
    this.process.getOutputStream().flush();
    this.process.getOutputStream().close();
    this.process.getInputStream().close();
    this.process.getErrorStream().close();
    this.process.destroy();
    this.process = null;
  }

  public void openFileOnWindows(String path) throws IOException {
    exec("start " + path);
  }

  public String getOutputString()
    throws InterruptedException, IOException
  {
    if (getExitCode() == 0) {
      return inputStream2String(this.process.getInputStream());
    }
    return inputStream2String(this.process.getErrorStream());
  }

  public String getOutputString(long waitTime)
    throws InterruptedException, IOException
  {
    Thread.sleep(waitTime);
    String output = inputStream2String(this.process.getInputStream());
    this.process.getInputStream().close();
    this.process.getErrorStream().close();
    this.process.destroy();
    return output;
  }

  public void inputCmd(String cmd) {
    if (this.process == null) {
      return;
    }
    InputStream is = String2InputStream(cmd);

    DataOutputStream os = new DataOutputStream(this.process.getOutputStream());
    try {
      os.writeBytes(cmd + "\n");
      os.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printInputStream()
  {
    new Thread(new StreamDrainer(this.process.getInputStream())).start();
  }

  public void writeInputStream(String filePath)
  {
    new Thread(new StreamWriter(this.process.getInputStream(), filePath))
      .start();
  }

  public void printErrorStream()
  {
    new Thread(new StreamDrainer(this.process.getErrorStream())).start();
  }

  public ByteArrayOutputStream getOutputStreamAsync()
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    PrintWriter pw = new PrintWriter(baos);
    new Thread(new StreamDrainer(this.process.getInputStream(), pw)).start();
    return baos;
  }

  public String getOutputStringAsync(long waitTime)
    throws InterruptedException, IOException
  {
    ByteArrayOutputStream baos = getOutputStreamAsync();
    Thread.sleep(waitTime);
    String str = baos.toString();
    baos.close();
    return str;
  }

  public static void main(String[] args)
  {
    Cmd cmd = null;
    String commandLineString = "adb shell";
    try {
      cmd = new Cmd(commandLineString);
      cmd.inputCmd("ls");
      cmd.printInputStream();
      cmd.printErrorStream();

      int exitValue = cmd.getExitCode();

      cmd.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static InputStream String2InputStream(String str)
  {
    ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
    return stream;
  }

  private static String inputStream2String(InputStream is) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(is));
    StringBuffer buffer = new StringBuffer();
    String line = "";
    while ((line = in.readLine()) != null) {
      buffer.append(line + "\r\n");
    }
    return buffer.toString();
  }

  private static boolean writeInputStream(String fileName, InputStream is, boolean append)
    throws IOException
  {
    FileOutputStream fos = null;

    int ch = 0;
    fos = new FileOutputStream(fileName, append);
    while ((is != null) && ((ch = is.read()) != -1)) {
      fos.write(ch);
    }
    fos.close();
    is.close();

    return true;
  }

  public static class StreamDrainer
    implements Runnable
  {
    private InputStream ins;
    private PrintWriter pw;

    public StreamDrainer(InputStream ins)
    {
      this.ins = ins;
    }

    public StreamDrainer(InputStream ins, PrintWriter pw) {
      this.ins = ins;
      this.pw = pw;
    }

    public void run() {
      try {
        Cmd.br = new BufferedReader(new InputStreamReader(this.ins));
        String line = null;
        while ((line = Cmd.br.readLine()) != null)
          outputLine(line);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    private void outputLine(String line) {
      if (this.pw == null) {
        System.out.println(line);
      } else {
        this.pw.println(line);
        this.pw.flush();
      }
    }
  }

  public static class StreamWriter
    implements Runnable
  {
    private InputStream ins;
    private String filePath;

    public StreamWriter(InputStream ins, String filePath)
    {
      this.ins = ins;
      this.filePath = filePath;
    }

    public void run() {
      try {
        Cmd.writeInputStream(this.filePath, this.ins, true);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static class WaitForThread extends Thread
  {
    private final Process process;
    private Integer exit;

    private WaitForThread(Process process)
    {
      this.process = process;
    }

    public void run() {
      try {
        this.exit = Integer.valueOf(this.process.waitFor());
      }
      catch (InterruptedException ignore)
      {
      }
    }
  }
}