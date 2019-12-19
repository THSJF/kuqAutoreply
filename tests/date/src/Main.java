import java.text.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		/* H 0-23输出格式:2017-04-16 13:01:22*/
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}
}
