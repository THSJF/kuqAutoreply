package com.meng.dice;
import java.util.*;
import com.meng.*;

public class DiceCommand {
	public static DiceCommand ins;

	private String[] cmdMsg;
	private int pos=0;
	public boolean check(long fromGrouo, long fromQQ, String msg) {
		if (msg.charAt(0) != '.') {
			return false;
		}
		cmdMsg = msg.split(" ");
		if (pos < cmdMsg.length) {
			switch (next()) {
				case ".r":
					String rs = next();
					Autoreply.sendMessage(fromGrouo, 0, String.format("%s投掷%s:D100 = %d", "uname", rs == null ?"": rs, Autoreply.ins.random.nextInt(100)));
					return true;
				case ".ra":
					String ras = next();
					Autoreply.sendMessage(fromGrouo, 0, String.format("%s进行检定:D100 = %d/%s", "uname", Autoreply.ins.random.nextInt(Integer.parseInt(ras)), ras));
					return true;
				case ".li":
					Autoreply.sendMessage(fromGrouo, 0, String.format("%s的疯狂发作-总结症状:\n1D10=%d\n症状: 狂躁：调查员患上一个新的狂躁症，在1D10=%d小时后恢复理智。在这次疯狂发作中，调查员将完全沉浸于其新的狂躁症状。这是否会被其他人理解（apparent to other people）则取决于守秘人和此调查员。\n1D100=%d\n具体狂躁症: 臆想症（Nosomania）：妄想自己正在被某种臆想出的疾病折磨。(KP也可以自行从狂躁症状表中选择其他症状)", "uname", Autoreply.ins.random.nextInt(11), Autoreply.ins.random.nextInt(11), Autoreply.ins.random.nextInt(101)));
					return true;
			}
		}
		return false;
	}
	private String next() {
		try {
			return cmdMsg[pos++];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}
