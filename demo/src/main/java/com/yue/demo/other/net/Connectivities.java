package com.yue.demo.other.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v4.net.ConnectivityManagerCompat;

/**
 * @author chengyue
 */
public class Connectivities {
    private Connectivities() {
    }

    /**
     * ��鵱ǰWIFI�Ƿ����ӣ�������˼�����Ƿ����ӣ������ǲ���WIFI
     * 
     * @param context
     * @return true��ʾ��ǰ���紦������״̬������WIFI�����򷵻�false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()
                && ConnectivityManager.TYPE_WIFI == info.getType()) {
            return true;
        }
        return false;
    }

    /**
     * ��鵱ǰGPRS�Ƿ����ӣ�������˼�����Ƿ����ӣ������ǲ���GPRS
     * 
     * @param context
     * @return true��ʾ��ǰ���紦������״̬������GPRS�����򷵻�false
     */
    public static boolean isGprsConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()
                && ConnectivityManager.TYPE_MOBILE == info.getType()) {
            return true;
        }
        return false;
    }

    /**
     * ��鵱ǰ�Ƿ�����
     * 
     * @param context
     * @return true��ʾ��ǰ���紦������״̬�����򷵻�false
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * �Դ���ݴ���ʱ����Ҫ���ø÷��������жϣ�����������У�Ӧ����ʾ�û�
     * 
     * @param context
     * @return true��ʾ�������У�false��ʾ������
     */
    public static boolean isActiveNetworkMetered(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return ConnectivityManagerCompat.isActiveNetworkMetered(cm);
    }

    public static Intent registerReceiver(Context context,
            ConnectivityChangeReceiver receiver) {
        return context.registerReceiver(receiver,
                ConnectivityChangeReceiver.FILTER);
    }

    public static void unregisterReceiver(Context context,
            ConnectivityChangeReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public static abstract class ConnectivityChangeReceiver extends
            BroadcastReceiver {
        public static final IntentFilter FILTER = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);

        @Override
        public final void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo gprsInfo = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            // �ж��Ƿ���Connected�¼�
            boolean wifiConnected = false;
            boolean gprsConnected = false;
            if (wifiInfo != null && wifiInfo.isConnected()) {
                wifiConnected = true;
            }
            if (gprsInfo != null && gprsInfo.isConnected()) {
                gprsConnected = true;
            }
            if (wifiConnected || gprsConnected) {
                onConnected();
                return;
            }

            // �ж��Ƿ���Disconnected�¼���ע�⣺�����м�״̬���¼����ϱ���Ӧ�ã��ϱ���Ӱ������
            boolean wifiDisconnected = false;
            boolean gprsDisconnected = false;
            if (wifiInfo == null || wifiInfo != null
                    && wifiInfo.getState() == State.DISCONNECTED) {
                wifiDisconnected = true;
            }
            if (gprsInfo == null || gprsInfo != null
                    && gprsInfo.getState() == State.DISCONNECTED) {
                gprsDisconnected = true;
            }
            if (wifiDisconnected && gprsDisconnected) {
                onDisconnected();
                return;
            }
        }

        protected abstract void onDisconnected();

        protected abstract void onConnected();
    }

    // /**
    // * ��ȡ��������
    // * @return
    // */
    // public boolean getIsGPRSNetworkType(Context context){
    // ConnectivityManager connetM = (ConnectivityManager)
    // context.getSystemService(Context.CONNECTIVITY_SERVICE);
    // String networkType =
    // NetworkUtil.getNetType(connetM.getActiveNetworkInfo());
    // if(!TextUtils.isEmpty(networkType) && !networkType.contains("none"))
    // {
    // return !networkType.contains("wifi");
    // }
    // return false;
    // }
}
