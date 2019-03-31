package com.meng.ocr;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.sobte.cqp.jcq.entity.CQImage;

public class OcrManager {

	// appid, secretid secretkey请到http://open.youtu.qq.com/获取
	// 请正确填写把下面的APP_ID、SECRET_ID和SECRET_KEY
	public static final String APP_ID = "10173140";
	public static final String SECRET_ID = "AKIDRmqfEXsNxHOFBrrpx2rVzDG3arCPs2Uh";
	public static final String SECRET_KEY = "71hGdBXfZIG1wWSLNI2YtCJrz62rIe8t";
	public static final String USER_ID = "2856986197"; // qq号
	public Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT, USER_ID);
	public Gson gson = new Gson();

	public boolean checkOcr(long fromGroup, long fromQQ, String msg) {
		JSONObject response;
		try {
			CQImage cqImage = Autoreply.CC.getCQImage(msg);
			File image = null;
			if (cqImage != null) {
				image = cqImage.download(Autoreply.appDirectory + "ocr/" + fromQQ,
						"ocr" + Autoreply.random.nextInt() + ".png");
			} else {
				return false;
			}
			response = faceYoutu.GeneralOcr(image.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		StringBuilder sb = new StringBuilder();
		OcrJavaBean ocrJavaBean = new Gson().fromJson(response.toString(), OcrJavaBean.class);
		ArrayList<OcrJavaBean.Items> items = ocrJavaBean.items;
		for (OcrJavaBean.Items s : items) {
			sb.append(s.itemstring);
		}
		System.out.println(sb.toString());
		if (Methods.isSetu(fromGroup, fromQQ, sb.toString()))
			return true;
		if (Methods.checkLook(fromGroup, sb.toString()))// 窥屏检测
			return true;
		if (Autoreply.biliLinkInfo.check(fromGroup, fromQQ, sb.toString()))// 比利比利链接详情
			return true;
		if (Methods.checkGou(fromGroup, sb.toString()))// 苟
			return true;
		if (Methods.checkMeng2(fromGroup, sb.toString()))// 萌2
			return true;
		if (Autoreply.updateManager.check(fromGroup, sb.toString()))
			return true;
		if (Autoreply.dicReplyManager.check(fromGroup, fromQQ, sb.toString()))// 根据词库触发回答
			return true;

		return true;
	}

	// public static void main(String[] args) {
	// try {
	// Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,
	// Youtu.API_YOUTU_END_POINT, USER_ID);
	// JSONObject response;
	// response=
	// faceYoutu.FaceCompareUrl("http://open.youtu.qq.com/content/img/slide-1.jpg","http://open.youtu.qq.com/content/img/slide-1.jpg");
	// response = faceYoutu.DetectFace("test.jpg",1);
	// response =
	// faceYoutu.ImageTerrorismUrl("http://open.youtu.qq.com/app/img/experience/terror/img_terror01.jpg");
	// response =
	// faceYoutu.CarClassifyUrl("http://open.youtu.qq.com/app/img/experience/car/car_01.jpg");
	// response =
	// faceYoutu.BizLicenseOcrUrl("http://open.youtu.qq.com/app/img/experience/char_general/ocr_yyzz_01.jpg");
	// response =
	// faceYoutu.CreditCardOcrUrl("http://open.youtu.qq.com/app/img/experience/char_general/ocr_card_1.jpg");
	// response =
	// faceYoutu.GeneralOcr("C:\\Users\\Administrator\\Desktop\\1.png");
	// get respose
	// System.out.println(response);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
