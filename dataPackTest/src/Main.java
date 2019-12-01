import java.util.*;
import hh.*;

public class Main {
	public static void main(String[] args) {
		DataPack dp1=DataPack.encode(0, 1);
		dp1.writeString("askdkgkh");
		DataPack dp2=DataPack.decode(dp1.getData());
		System.out.println(dp2.readString());
	}
}
