package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileManage {
	public void createFile(String filePath) {
		try {
			File file = new File(filePath);
			File path = new File(file.getParentFile().getPath());
			
			//file directory
			if (!path.exists() && !path.isDirectory()) {
				path.mkdir();
			}

			//file
			if (!file.exists()) {
				file.createNewFile();
			} else {
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
