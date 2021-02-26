package com.htc.zip.demo;


import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jUtil;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ZipManager {
	private static final int     WHAT_START    = 100;
    private static final int     WHAT_FINISH   = 101;
    private static final int     WHAT_PROGRESS = 102;
	
	public static void debug(boolean debug) {
        ZipLog.config(debug);
    }
	
	public static void zip(String targetPath, String destinationFilePath, IZipCallback callback) {
	        zip(targetPath, destinationFilePath, "", callback);
	}
	
    public static void zip(String targetPath, String destinationFilePath, String password, IZipCallback callback) {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(targetPath) || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFilePath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("zip: targetPath=" + targetPath + " , destinationFilePath=" + destinationFilePath + " , password=" + password);
        try {
        	ZipFile zipFile = new ZipFile(destinationFilePath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.STORE);
            
            if (password.length() > 0) {
            	parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.AES);
                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
                zipFile.setPassword(password.toCharArray());
            }
            
            zipFile.setRunInThread(true);
            File targetFile = new File(targetPath);
            if(!targetFile.canRead()) {
            	targetFile.setReadable(true);
            }
            if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            } else {
                zipFile.addFile(targetFile, parameters);
            }
            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("zip: Exception=" + e.getMessage());
        }
    }

    public static void zip(ArrayList<File> list, String destinationFilePath, String password, final IZipCallback callback) {
        if (list == null || list.size() == 0 || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFilePath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("zip: list=" + list.toString() + " , destinationFilePath=" + destinationFilePath + " , password=" + password);
        
        try {
        	ZipFile zipFile = new ZipFile(destinationFilePath);
        	ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.STORE);
            if (password.length() > 0) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.AES);
                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
                zipFile.setPassword(password.toCharArray());
            }
            
            zipFile.setRunInThread(true);
            zipFile.addFiles(list, parameters);
            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("zip: Exception=" + e.getMessage());
        }
    }

    public static void zip(ArrayList<File> list, String destinationFilePath, IZipCallback callback) {
        zip(list, destinationFilePath, "", callback);
    }

    public static void unzip(String targetZipFilePath, String destinationFolderPath, IZipCallback callback) {
        unzip(targetZipFilePath, destinationFolderPath, "", callback);
    }

    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password, final IZipCallback callback) {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(targetZipFilePath) || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFolderPath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("unzip: targetZipFilePath=" + targetZipFilePath + " , destinationFolderPath=" + destinationFolderPath + " , password=" + password);
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted() && Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.setRunInThread(true);
            zipFile.extractAll(destinationFolderPath);
            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("unzip: Exception=" + e.getMessage());
        }
    }
    
    
    private static void timerMsg(final IZipCallback callback, ZipFile zipFile) {
        if (callback == null) return;
        final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	switch (progressMonitor.getCurrentTask()) {
                case NONE:
                    System.out.println("no operation being performed");
                    break;
                case ADD_ENTRY:
                    System.out.println("Add operation");
                    break;
                case EXTRACT_ENTRY:
                    System.out.println("Extract operation");
                    break;
                case REMOVE_ENTRY:
                    System.out.println("Remove operation");
                    break;
                case CALCULATE_CRC:
                    System.out.println("Calcualting CRC");
                    break;
                case MERGE_ZIP_FILES:
                    System.out.println("Merge operation");
                    break;
                default:
                    System.out.println("invalid operation");
                    break;
                }
            	
            	ZipLog.debug("Actual -> " + progressMonitor.getPercentDone());
                if (progressMonitor.getResult() == ProgressMonitor.Result.SUCCESS) {
                	ZipLog.debug("Finalizado");
                    this.cancel();
                    timer.purge();
                }
            }
        }, 0, 300);
    }

}
