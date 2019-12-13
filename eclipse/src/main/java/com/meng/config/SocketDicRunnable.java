package com.meng.config;

import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class SocketDicRunnable implements Runnable {
    Socket socket = null;
    ConfigManager configManager;

    public SocketDicRunnable(ConfigManager configManager, ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.configManager = configManager;
        InetAddress address = socket.getInetAddress();
        System.out.println("当前客户端的IP ： " + address.getHostAddress());
    }

    @Override
    public void run() {

        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String string = dataInputStream.readUTF();
            // string = new String(Base64.decryptBASE64(string), "utf-8");
            System.out.println("服务器读取客户端的：" + string);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String fileName = Autoreply.appDirectory + "dic\\dic"
                    + string.substring(0, string.indexOf(".")).replace("get", "").replace("write", "") + ".json";
            System.out.println(fileName);
            if (string.startsWith("get")) {
                dataOutputStream.writeUTF(Tools.FileTool.readString(fileName));
            } else if (string.startsWith("write")) {
                File file = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                outputStreamWriter.write(string.substring(string.indexOf(".") + 1));
                outputStreamWriter.flush();
                fileOutputStream.close();
                dataOutputStream.writeUTF("ok");
            } else {
                dataOutputStream.writeUTF("fafafa");
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
