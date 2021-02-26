package com.htc.zip.demo;

public interface IZipCallback {
	void onStart();
    void onProgress(int percentDone);
    void onFinish(boolean success);
}
