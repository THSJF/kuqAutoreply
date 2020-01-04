package jhh;

import java.util.*;

public class QA {
	public int id=0;
	public int d=0;
	public String q;
	public ArrayList<String> a = new ArrayList<>();
	public int t;//trueAns
	public String r;//reason
	@Override

	public String toString() {
		StringBuilder sb=new StringBuilder("题目ID:").append(id).append("\n");
		sb.append("难度:");
		switch (d) {
			case 0:
				sb.append("easy");
				break;
			case 1:
				sb.append("normal");
				break;
			case 2:
				sb.append("hard");
				break;
			case 3:
				sb.append("lunatic");
				break;
			case 4:
				sb.append("overdrive");
				break;
		}
		sb.append("\n\n").append(q).append("\n");
		int i=1;
		for (String s:a) {
			if (s.equals("")) {
				continue;
			}
			sb.append(i++).append(": ").append(s).append("\n");
		}
		return q;
	}
}


