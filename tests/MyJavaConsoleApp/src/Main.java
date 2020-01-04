import com.google.gson.*;
import com.google.gson.reflect.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import jhh.*;

public class Main {
	public static File qafile;
	public static ArrayList<QA> qaList=new ArrayList<>();
	public static void main(String[] args) {
		qafile = new File("/storage/emulated/0/AppProjects/kuqAutoreply/qa.json");
        if (!qafile.exists()) {
            saveData();
        }
        Type type = new TypeToken<ArrayList<QA>>() {
        }.getType();
        qaList = new Gson().fromJson(readString(qafile), type);
		int i=0;
		Scanner input = new Scanner(System.in);	
		//System.out.println(input.next().equals("e"));
		for (QA q:qaList) {
			System.out.println("\nnow:" + i++);
			if(q.d!=-1){
				continue;
			}
			System.out.println(q.toString());
			System.out.println("e-0 n-1 h-2 l-3 o-4");
			//System.out.println(input.next());
			switch (input.next()) {
				case"e":
					q.d = 0;
					System.out.println("难度e");
					break;
				case"n":
					q.d = 1;
					System.out.println("难度n");
					break;
				case"h":
					q.d = 2;
					System.out.println("难度h");
					break;
				case"l":
					q.d = 3;
					System.out.println("难度l");
					break;
				case"o":
					q.d = 4;
					System.out.println("难度o");
					break;
			}
				saveData();
		}
		
	}

	public static void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(qafile);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(new Gson().toJson(qaList));
			writer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public static String readString(File f) {
		String s = "{}";
		try {      
			if (!f.exists()) {
				f.createNewFile();
			}
			long filelength = f.length();
			byte[] filecontent = new byte[(int) filelength];
			FileInputStream in = new FileInputStream(f);
			in.read(filecontent);
			in.close();
			s = new String(filecontent, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}
