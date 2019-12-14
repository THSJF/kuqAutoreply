import java.util.*;
import hh.*;

public class Main {
	public static void main(String[] args) {
		DataPack dp1=DataPack.encode(0, 1);
		dp1.writeString("askdkgkh");
		dp1.writeString("ggg发发发g");
		dp1.writeFloat(11.11f);
		dp1.writeDouble(22.22);
		DataPack dp2=DataPack.decode(dp1.getData());
		System.out.println(dp2.readString());
		System.out.println(dp2.readString());
		System.out.println(dp2.readFloat());
		System.out.println(dp2.readDouble());
	}
}
