import java.util.*;

public class Main {
	public static void main(String[] args) {
		HashMap<String,Boolean> ques=new HashMap<>();
		ques.put("橙姓八云", false);
		
		ques.put("东方风神录擦弹没有任何加成", true);
		ques.put("东方风神录中魔理沙有一个高火力bug", true);
		ques.put("东方风神录中全避犬走椛后犬走椛会变成乌鸦飞走", true);
		ques.put("东方风神录4面某些地方会让灵梦的诱导失效", true);
		ques.put("秋静叶对人类友好度比较高", true);
		ques.put("秋静叶在冬天时会很没精神", true);
		ques.put("「信仰之山」easy难度比normal难度困难",true);
		
		
		System.out.println("{");
		for (Map.Entry<String,Boolean> entry:ques.entrySet()) {
			System.out.println("\"" + entry.getKey() + "\"" + ":" + entry.getValue() + ",");
		}
	}
}
