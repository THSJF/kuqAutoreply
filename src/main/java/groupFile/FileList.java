package groupFile;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FileList {
	public int code;
	public FileListData data;
	@SerializedName("default")
	public int _default;
	public String message;
	public int subcode;

	public ArrayList<FileItem> getFileItemByName(String name) {
		ArrayList<FileItem> fileItems = new ArrayList<>();
		for (FileItem fileItem : data.item) {
			if (fileItem.filename.equals(name)) {
				fileItems.add(fileItem);
			}
		}
		return fileItems;
	}
}
