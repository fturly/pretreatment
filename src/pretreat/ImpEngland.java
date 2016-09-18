package pretreat;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.csvreader.CsvReader;

import tools.AccessFile;

public class ImpEngland {

	public static void main(String[] args) {
//		String sourcePath = "/home/fxl/DATA/dynamodb/";
//		String pretreatPath = "/home/fxl/DATA/pretreat/";

		 String sourcePath = "D:\\DATA\\dynamodb\\";
		 String pretreatPath = "D:\\DATA\\pretreat\\";

		 String mdbFile = pretreatPath+"201606-IMP.mdb";
		 
		 //get connect
		 AccessFile access = new AccessFile();
		 Connection conn = access.getConnection(mdbFile);
		 
		 //create table
		 String sql = "create table import (YEAR VARCHAR,MONTH VARCHAR,IMPORTER VARCHAR,ADDRESS VARCHAR,POSTAL VARCHAR,HS_CODE VARCHAR,HS_CODE_DESC VARCHAR)";
		 int create = access.executeUpdateAccess(conn, sql);
		 System.out.println("create table status:"+create);
		 
		 pretreat(access, conn, sourcePath, mdbFile);
		 
	}

	/**
	 * controller
	 * @param file
	 * @param csvPath
	 */
	private static void pretreat(AccessFile access, Connection conn, String sourcePath, String mdbFile) {

		 int count = 0;
		 int recordCount = 0;
		 //origin file
		 File pathFile = new File(sourcePath);
		 for (File folder : pathFile.listFiles()) {
			 for (File file : folder.listFiles()) {
				 System.out.println("----------start-" + file.getName());
					// read
					ArrayList<String[]> list = readSource(file);
					count += list.size();
					// write
					recordCount += writeMDB(list, conn, access);
					System.out.println("----------end---" + file.getName() + "--------");
			 }
		 }
		 
		 //close connection
		 boolean closeConn = access.closeConn(conn);
		 System.out.println("close connection:"+closeConn);
		 System.out.println("count:"+count+"|recordCount:"+recordCount);
	}

	/**
	 * reading
	 * @param file
	 * @return
	 */
	private static ArrayList<String[]> readSource(File file) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		try {
			CsvReader reader = new CsvReader(file.toString(), ',');
			reader.readHeaders();
			//line
			while (reader.readRecord()) {
				list.add(reader.getValues());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @param list
	 * @param exportPath
	 */
	private static int writeMDB(ArrayList<String[]> list, Connection conn, AccessFile access) {
		int sum = 0;
		try {
			Statement stmt = conn.createStatement();
			
			//write record
			for (String[] line : list) {
				//convert record
				ArrayList<String[]> records = convert(line);
				sum += records.size();
				for(String[] record : records){
					String sql = "insert into import (YEAR,MONTH,IMPORTER,ADDRESS,POSTAL,HS_CODE,HS_CODE_DESC) values(?,?,?,?,?,?,?)";
					
					access.reExecuteUpdateAccess(conn, sql, record);
				}
			}
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return sum;
	}
	
	/**
	 * convert object
	 * @param line
	 * @return
	 */
	private static ArrayList<String[]> convert(String[] line ){
		SimpleDateFormat sdfM = new SimpleDateFormat("MMM yyyy", Locale.UK);
		SimpleDateFormat sdfL = new SimpleDateFormat("MMM-yyyy", Locale.UK);
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		ArrayList<String[]> records = new ArrayList<String[]>();
		String[] record = new String[7];
		record[2] = line[0].trim();
		record[3] = line[4].trim()+" "+line[5].trim()+" "+line[6].trim()+" "+line[7].trim();
		record[4] = line[8].trim();
		record[5] = line[1].trim();
		record[6] = line[2].trim().toUpperCase();
		//Date
		String[] dateAry = line[3].split(",");
		for(String dateStr : dateAry){
			try {
				Date date = null;
				if (dateStr.trim().contains("-")) {
					date = sdfL.parse(dateStr.trim());
				}else if (dateStr.trim().contains(" ")) {
					date = sdfM.parse(dateStr.trim());
				}
				record[0] = sdfYear.format(date);
				record[1] = sdfMonth.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//add array
			records.add(record.clone());
		}
		
		return records;
	}
	
}
