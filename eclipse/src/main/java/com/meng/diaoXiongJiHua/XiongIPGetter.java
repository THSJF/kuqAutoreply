package com.meng.diaoXiongJiHua;

import com.meng.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class XiongIPGetter implements Runnable {

    public long fromQQ = 0;
    public boolean running = true;
    public int port = 0;
    public HashSet<String> hSet = new HashSet<>();

    public XiongIPGetter(long fromQQ, int port) {
        this.fromQQ = fromQQ;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check() {
        try {
            ServerSocket serverSocket = new ServerSocket(4000 + port);
            Socket socket;
            System.out.println("***服务器即将启动，等待客户端的链接***");
            while (running) {
                socket = serverSocket.accept();
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP ： " + address.getHostAddress());
                if (!hSet.contains(address.getHostAddress())) {
                    Autoreply.instence.threadPool.execute(new XiongServerThread(socket, fromQQ, "新的连接 ： " + address.getHostAddress() + "\n"));
                    hSet.add(address.getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	class XiongServerThread implements Runnable {
		Socket socket = null;
		long fromQQ = 0;
		String string = "";

		public XiongServerThread(Socket socket, long fromQQ, String string) {
			this.socket = socket;
			this.fromQQ = fromQQ;
			this.string = string;
		}

		@Override
		public void run() {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			OutputStream os = null;
			PrintWriter pw = null;
			try {
				// 获取一个输入流，并读取客户端的信息
				is = socket.getInputStream();
				isr = new InputStreamReader(is); // 将字节流转化为字符流
				br = new BufferedReader(isr); // 添加缓冲
				String info;
				// 循环读取数据
				while ((info = br.readLine()) != null) {
					System.out.println("客户端:" + info);
					if (info.startsWith("User-Agent")) {
						Autoreply.sendMessage(0, fromQQ, string + info);
					}
				}
				socket.shutdownInput(); // 关闭输入流
				// 获取输出流，响应客户端的请求
				os = socket.getOutputStream();
				pw = new PrintWriter(os); // 包装为打印流
				pw.write("欢迎你");
				pw.flush(); // 将缓存输出

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭资源
					if (pw != null)
						pw.close();
					if (os != null)
						os.close();
					if (is != null)
						is.close();
					if (isr != null)
						isr.close();
					if (br != null)
						br.close();
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
	}
}
