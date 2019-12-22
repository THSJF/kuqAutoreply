package com.meng;

import com.meng.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class IPGetter implements Runnable {

    public long fromGroup = 0;
    public boolean running = true;
    public int port = 0;
    public HashSet<String> hSet = new HashSet<>();

    public IPGetter(long fromGroup, int port) {
        this.fromGroup = fromGroup;
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
                Autoreply.instence.threadPool.execute(new ServerRunnable(socket));
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP ： " + address.getHostAddress());
                hSet.add(address.getHostAddress());
                System.out.println("客户端的数量: " + hSet.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	class ServerRunnable implements Runnable {
		private Socket socket;

		public ServerRunnable(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			OutputStream os = null;
			PrintWriter pw = null;
			try {
				is = socket.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String info = null;
				while ((info = br.readLine()) != null) {
					System.out.println("客户端:" + info);
				}
				socket.shutdownInput();
				os = socket.getOutputStream();
				pw = new PrintWriter(os);
				pw.write("欢迎你");
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pw != null) {
						pw.close();
					}
					if (os != null) {
						os.close();
					}
					if (is != null) {
						is.close();
					}
					if (isr != null) {
						isr.close();
					}
					if (br != null) {
						br.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
