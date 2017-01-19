package com.wxcily.xunplayer.player;

public class PlayerConfig {
	public static final String VIDEO_CACHE_DIR = "xunplayer/Cache/VIDEO";// 视频缓存路径
	public static final int DEFAULT_BUF_SIZE = 512 * 1024;//缓冲内存大小
	public static final boolean DEFAULT_DEINTERLACE =false;//是否启用逐行扫描
	public static final float DEFAULT_STEREO_VOLUME = 1.0f;//音量大小
	public static final int DEFAULT_TIME_REFFRESH = 1000;//默认刷新时间
	public static final int DEFAULT_SHOW_TIME = 4000;//默认菜单弹出时间
}
