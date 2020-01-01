import java.util.*;

public class Main {
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		for (int i=1;i < 25;++i) {
			cal.add(Calendar.HOUR_OF_DAY, 1);
			System.out.println(cal.getTime());
		}
	}
}
