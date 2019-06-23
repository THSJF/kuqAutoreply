package com.meng.picEdit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import com.meng.Autoreply;

public class JingShenZhiZhuQQManager {

	private long fromGroup = 0;
	private File resultImageFile;
	private File headImageFile;

	public JingShenZhiZhuQQManager(long fromGroup, long fromQQ, String msg) {
		this.fromGroup = fromGroup;
		File files = new File(Autoreply.appDirectory + "jingshenzhizhu\\");
		if (!files.exists()) {
			files.mkdirs();
		}
		File files2 = new File(Autoreply.appDirectory + "user\\");
		if (!files2.exists()) {
			files2.mkdirs();
		}
		long id = Autoreply.instence.CC.getAt(msg);
		if (id == -1000 || id == -1) {
			id = fromQQ;
		}
		resultImageFile = new File(Autoreply.appDirectory + "jingshenzhizhu\\" + id + ".jpg");
		headImageFile = new File(Autoreply.appDirectory + "user\\" + id + ".jpg");
		boolean needRecreate = downloadPicture("http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin="
				+ id + "&;dst_uin=" + id + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC")
				| !resultImageFile.exists();
		if (needRecreate) {
			start(headImageFile);
		} else {
			try {
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void start(File headFile) {
		try {
			BufferedImage headImage = null;
			headImage = ImageIO.read(headFile);
			BufferedImage des1 = chgPic(Rotate(headImage, 346), 190);
			Image baseImage = null;
			baseImage = ImageIO.read(new File(Autoreply.appDirectory + "pic\\jingshenzhizhuback.png"));
			BufferedImage b = new BufferedImage(baseImage.getWidth(null), baseImage.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			b.getGraphics().drawImage(baseImage, 0, 0, null);
			b.getGraphics().drawImage(des1, -29, 30, null);
			ImageIO.write(b, "png", resultImageFile);
			Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(resultImageFile));
		} catch (Exception e) {
		}
	}

	private BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src_height;
				src_height = src_width;
				src_width = temp;
			}
		}
		double r = Math.sqrt(src_height * src_height + src_width * src_width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel % 90) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel % 90)) / 2;
		double angel_dalta_width = Math.atan((double) src_height / src_width);
		double angel_dalta_height = Math.atan((double) src_width / src_height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src_width + len_dalta_width * 2;
		int des_height = src_height + len_dalta_height * 2;
		Rectangle rect_des = new Rectangle(new Dimension(des_width, des_height));
		BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = res.createGraphics();
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(src, null, null);
		return res;
	}

	// 按比例压缩图片
	private BufferedImage chgPic(BufferedImage img, int newSize) {
		BufferedImage img2 = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		img2.getGraphics().drawImage(img, 0, 0, newSize, newSize, null);
		return img2;
	}

	private boolean downloadPicture(String urlList) {
		URL url = null;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			if (dataInputStream.available() == headImageFile.length()) {
				return false;
			}
			FileOutputStream fileOutputStream = new FileOutputStream(headImageFile);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
