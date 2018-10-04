package com.meng;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LivingManager extends Thread {

	private HashMap<Integer, LivingPerson> liveData = new HashMap<Integer, LivingPerson>();
	private int mapFlag = 0;
	private final String liveString = "\"live_time\":\"0000-00-00 00:00:00\"";

	public LivingManager() {
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				for (int i = 0; i < mapFlag; i++) {
					LivingPerson lPerson = liveData.get(i);
					String htmlData = open(lPerson.getLiveUrl());
					boolean living = htmlData.indexOf(liveString) == -1;
					/*
					 * if (living) { if (lPerson.isNeedTip()) {
					 * lPerson.setFlag(1); } else { lPerson.setFlag(2); } } else
					 * { if (lPerson.isNeedTip()) { lPerson.setFlag(3); } else {
					 * lPerson.setFlag(4); } }
					 */
					lPerson.setLiving(living);
					if (lPerson.getFlag() != 0) {
						if (living && lPerson.isNeedStartTip()) {
							lPerson.setFlag(1);
						}
						if (living && !lPerson.isNeedStartTip()) {
							lPerson.setFlag(2);
						}
						if (!living && !lPerson.isNeedStartTip()) {
							lPerson.setFlag(3);
						}
						if (!living && lPerson.isNeedStartTip()) {
							lPerson.setFlag(4);
						}
					}
					sendMsg(lPerson);
					sleep(3000);
				}
				sleep(15000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int getMapFlag() {
		return mapFlag;
	}

	public LivingPerson getPerson(int key) {
		return liveData.get(key);
	}

	public void addData(LivingPerson person) {
		liveData.put(mapFlag, person);
		mapFlag++;
	}

	private void sendMsg(LivingPerson p) {
		switch (p.getFlag()) {
		case 0:
			if (p.isLiving()) {
				p.setNeedStartTip(false);
				p.setFlag(2);
			} else {
				p.setNeedStartTip(true);
				p.setFlag(4);
			}
			break;
		case 1:
			String tmp = p.getName() + "直播开始啦大家快去奶" + p.getLiveUrl();
			System.out.println(tmp);
		//	Autoreply.sendGroupMessage(312342896L, tmp);// 学习
			Autoreply.sendGroupMessage(210341365L, tmp);// 水紫
			Autoreply.sendGroupMessage(855927922L, tmp);// 最速
			Autoreply.sendGroupMessage(859561731L, tmp);// 东芳直播间
			Autoreply.sendGroupMessage(826536230L, tmp);// stg闲聊群
		//	Autoreply.sendGroupMessage(348595763L, tmp);// 沙苗のSTG群
		//	Autoreply.sendGroupMessage(857548607L, tmp);// 恋萌萌粉丝群
			p.setNeedStartTip(false);
		case 2:
			p.setLiving(true);
			break;
		case 3:
			String tmp2 = p.getName() + "直播被奶死莉";
			System.out.println(tmp2);
		//	Autoreply.sendGroupMessage(312342896L, tmp2);// 学习
			Autoreply.sendGroupMessage(210341365L, tmp2);// 水紫
			Autoreply.sendGroupMessage(855927922L, tmp2);// 最速
			Autoreply.sendGroupMessage(859561731L, tmp2);// 东芳直播间
			Autoreply.sendGroupMessage(826536230L, tmp2);// stg闲聊群
		//	Autoreply.sendGroupMessage(348595763L, tmp2);// 沙苗のSTG群
		//	Autoreply.sendGroupMessage(857548607L, tmp2);// 恋萌萌粉丝群
			p.setNeedStartTip(true);
		case 4:
			p.setLiving(false);
			break;
		}
	}

	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private String open(String url) throws NoSuchAlgorithmException, KeyManagementException {
		InputStream in = null;
		OutputStream out = null;
		String str_return = "";
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.connect();
			InputStream is = conn.getInputStream();
			DataInputStream indata = new DataInputStream(is);
			String ret = "";
			while (ret != null) {
				ret = indata.readLine();
				if (ret != null && !ret.trim().equals("")) {
					str_return = str_return + ret;
				}
			}
			conn.disconnect();
		} catch (ConnectException e) {
			System.out.println("ConnectException");
			System.out.println(e);
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}

		}
		return str_return;
	}

}
