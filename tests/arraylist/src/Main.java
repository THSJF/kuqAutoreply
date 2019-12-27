import java.util.*;

public class Main {
	public static void main(String[] args) {
		ArrayList<ReportBean> reportList=new ArrayList<>();
		reportList.add(new Main.ReportBean(1));
		reportList.add(new Main.ReportBean(2));
		reportList.add(new Main.ReportBean(3));
		reportList.add(new Main.ReportBean(4));

		reportList.add(reportList.remove(0));
		for (ReportBean rbg:reportList) {
			System.out.println(rbg);
		}
	}
	public static class ReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		public ReportBean(long l) {
			g = l;
		}
		@Override
		public String toString() {
			return String.format("群:%d", g);
		}
	}

}
