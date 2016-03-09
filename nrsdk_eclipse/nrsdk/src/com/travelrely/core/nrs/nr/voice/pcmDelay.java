package com.travelrely.core.nrs.nr.voice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.R.integer;
import android.R.string;
import android.util.Log;

public class pcmDelay {
	static final private pcmDelay Instance = new pcmDelay();

	public boolean delayPcm(String pcmFilePath, int delayMileSecond) {

		try {
			File file = new File(pcmFilePath);
			if (!file.exists()) {
				return false;
			}

			FileInputStream ins = new FileInputStream(pcmFilePath);
			if (ins == null) {
				return false;
			}

			String dirPathString = file.getParent();
			String pcmFileNameString = file.getName();
			String delayFilePathString = dirPathString + "/"
					+ pcmFileNameString + ".delay";
			int delaySize = 320 / 20 * delayMileSecond; // 20ms 320byte
			Log.e("", "delay size is "+delaySize);
			byte[] padding = new byte[delaySize];

			FileOutputStream os = new FileOutputStream(delayFilePathString);
			if (os == null) {
				ins.close();
				return false;
			}
			os.write(padding);

			int payloadSize = 0;
			byte[] pcmContent = new byte[200];// 一百个short采样点
			int readCount=ins.read(pcmContent);
			while (readCount==pcmContent.length) {
				os.write(pcmContent);
				payloadSize+=pcmContent.length;
				readCount=ins.read(pcmContent);
			}
			if (readCount>0 && readCount!=pcmContent.length) {
				os.write(pcmContent,0,readCount);
				payloadSize+=readCount;
			}		
			
			ins.close();
			os.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return true;
	}

	public static pcmDelay getInstance() {
		return Instance;
	}
}
