import com.meng.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		MusicManager musicManager=new MusicManager();
		File f=musicManager.init(5,20);
		
		try {
			FileInputStream fis=new FileInputStream(f);
			byte[] bytes=new byte[44];
			fis.read(bytes);
			printArray(bytes);
			System.out.println(readInt(bytes,4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void printArray(byte[] bytes){
		for(int i=0;i<bytes.length;++i){
			System.out.print("0x"+Integer.toHexString(bytes[i]&0xff));
			System.out.print(" ");
			System.out.println((char)bytes[i]);
		}
	}
	
	public static int readInt(byte[] data, int pos) {
        return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
	}
	
}
