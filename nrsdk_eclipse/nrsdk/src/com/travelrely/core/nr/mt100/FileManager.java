package com.travelrely.core.nr.mt100;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	
	private static final String TAG = "FileManger";
	
	private static File file;
	
	private static FileReader fr;
	
	private static BufferedReader br;
	
	private static long fileSize;
	
	private static long readSize;
	
	public static boolean fileOpen = false;
	
	public static int openFile() {
		try {
			String extDir = Environment.getExternalStorageDirectory().toString();
			file = new File(extDir+"/Download/MT100.hex");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			fileSize = file.length();
			Log.i(TAG,"file name: "+file.getName()+",size: "+fileSize);
			readSize = 0;
			fileOpen = true;
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void closeFile() {
		try {
			br.close();
			fr.close();
			fileOpen = false;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static String readLine() {
		String line = null;
		try {
			line = br.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
		if(line != null)
			readSize += line.length();
		return line;
	}
	
	public static long getFileSize() {
		return fileSize;
	}
	
	public static long getReadSize() {
		return readSize;
	}
	
}



