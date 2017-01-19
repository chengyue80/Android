package com.zbar.lib.decode;

import android.os.Handler;

public interface ScanResultListener {
	
	public void handleDecode(String result);
	
	public Handler getHandler();

}
