import java.util.*;
import java.util.regex.*;

public class Main
{
	public static void main(String[] args)
	{
		ArrayList<String> als=getString("int i=0;");
			for(String s:als){
			System.out.println(s);
		}
	}
	public static ArrayList<String> getString(String s) {

		ArrayList<String> strs = new ArrayList<String>();
		Pattern p = Pattern.compile("[^\\s=(int)]*");
		Matcher m = p.matcher(s);
		while(m.find()) {
			strs.add(m.group());

		}
		return strs;
		}
}
