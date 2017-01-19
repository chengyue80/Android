package com.iflytek.android.framework.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class ThreadWorker {
	
	static ExecutorService executorService;
	
	/**
	 * 网络线程池打消
	 */
	static int NET_POLL_SIZE=5;
	
	/**
	 * 线程池里跑runnable
	 * 
	 * @param runnable
	 * @return
	 */
	public static Future<?>  executeRunalle(Runnable runnable) {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(NET_POLL_SIZE);
		}
		return executorService.submit(runnable);
	}
	
	public static Future<?> execuse(boolean dialog,final Task task){
		if(dialog){
//			IDialog diagloer=IocContainer.getShare().get(IDialog.class);
//			Dialog pd=diagloer.showProgressDialog(task.mContext);
//			pd.setCancelable(false);
//			task.dialog=pd;
		}
		Future<?> future=executeRunalle(new Runnable() {
			
			public void run() {
				try {
					task.doInBackground();
				} catch (Exception e) {
					task.transfer(null, Task.TRANSFER_DOERROR);
					return;
				}
				task.transfer(null, Task.TRANSFER_DOUI);
			}
		});
		return future;
	}
	
}
