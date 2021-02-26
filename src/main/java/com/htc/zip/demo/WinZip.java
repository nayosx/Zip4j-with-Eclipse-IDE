package com.htc.zip.demo;

import java.io.File;
import java.util.ArrayList;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class WinZip {
	
	
	public void createDemoZip() throws ZipException {
		
		String path_in = "C:\\Users\\nayos\\Documents\\demo\\holis.txt";
		String path_out = "C:\\Users\\nayos\\Documents\\testEncrypt.zip";
		String password = "1234";
		
		ZipManager zm = new ZipManager();
		
		zm.zip(path_in, path_out, password, new IZipCallback() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				System.out.print("Se a inciado la compresion");
				
			}
			
			@Override
			public void onProgress(int percentDone) {
				// TODO Auto-generated method stub
				System.out.print("Progreso de compresion: " + percentDone);
			}
			
			@Override
			public void onFinish(boolean success) {
				// TODO Auto-generated method stub
				
				System.out.print("finalizado");
				
			}
		});
	}

}
