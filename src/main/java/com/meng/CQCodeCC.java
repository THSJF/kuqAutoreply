package com.meng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.IniFile;
import com.sobte.cqp.jcq.message.CQCode;
import com.sobte.cqp.jcq.message.CoolQCode;
import com.sobte.cqp.jcq.util.StringHelper;

public class CQCodeCC extends CQCode {

	public CQImage getCQImage(String code) {
		try {
			// 获取相对路径
			String path = StringHelper.stringConcat("data", File.separator, "image", File.separator,
					new CoolQCode(code).get("image", "file"), ".cqimg");
			return new CQImage(new IniFile(new File(path)));
		} catch (Exception e) {
			return null;
		}
	}

	public List<CQImage> getCQImages(String code) {
		List<CQImage> list = new ArrayList<CQImage>();
		List<String> imgs = new CoolQCode(code).gets("image", "file");
		try {
			for (String file : imgs) {
				if (file != null) {
					String path = StringHelper.stringConcat("data", File.separator, "image", File.separator, file,
							".cqimg");
					File iniFile = new File(path);
					if (iniFile.exists() && iniFile.canRead())
						list.add(new CQImage(new IniFile(iniFile)));
				}
			}
		} catch (Exception e) {
		}
		return list;
	}
}
