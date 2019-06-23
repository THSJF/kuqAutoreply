package com.meng;

import java.io.File;
import java.io.IOException;

import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.IniFile;
import com.sobte.cqp.jcq.message.CQCode;
import com.sobte.cqp.jcq.message.CoolQCode;
import com.sobte.cqp.jcq.util.StringHelper;

public class CQCodeCC extends CQCode {

    @Override
    public CQImage getCQImage(String code) {
        try {
            // 获取相对路径
            String path = StringHelper.stringConcat("data", File.separator, "image", File.separator, new CoolQCode(code).get("image", "file"), ".cqimg");
            return new CQImage(new IniFile(new File(path)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String image(File path) {
        try {
            CQImage image;
            image = new CQImage(path, false);
            path = image.download("data/image/", image.getName());
            path.deleteOnExit();
            return StringHelper.stringConcat("[CQ:image,file=", path.getName(), "]");
        } catch (Exception e) {
            return null;
        }
    }
}
