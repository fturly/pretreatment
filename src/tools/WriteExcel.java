package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * 将Map中数据写入excel
 * @author xinliFu
 */
public class WriteExcel {

	/**
	 * 创建xls
	 * @param path
	 * @return
	 */
	public WritableWorkbook createWorkbook(String path) {
		try {
			File fileWrite = new File(path);
			File filePath = new File(fileWrite.getParentFile().getPath());
			if (!filePath .exists()  && !filePath .isDirectory()) {
				filePath.mkdir();
			}
			fileWrite.createNewFile();
			OutputStream os = new FileOutputStream(fileWrite);
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
}
