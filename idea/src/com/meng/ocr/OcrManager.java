package com.meng.ocr;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.meng.Autoreply;
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

    public boolean checkOcr(long fromGroup, long fromQQ, String msg, File[] imageFiles) {
        JSONObject response;
        try {
            if (imageFiles[0] != null) {
                response = faceYoutu.GeneralOcr(imageFiles[0].getAbsolutePath());
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        StringBuilder sb = new StringBuilder();
        OcrJavaBean ocrJavaBean = new Gson().fromJson(response.toString(), OcrJavaBean.class);
        ArrayList<OcrJavaBean.Items> items = ocrJavaBean.items;
        sb.append("结果:");
        for (OcrJavaBean.Items s : items) {
            sb.append("\n").append(s.itemstring);
        }
        Autoreply.sendMessage(fromGroup, fromQQ, sb.toString());
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
