package com.dnc.loc.utils;

/**
 */
public class Click {
	private static long _lastClickTime;  
	/** fast double click */
	public static boolean isFastClick() {
		long now = System.currentTimeMillis();
		long diff = now - _lastClickTime;
		if (0 < diff && diff < 500) {
			return true;
		}
		_lastClickTime = now;
		return false;
	}
    public static boolean isFastClick(long limit) {
        long now = System.currentTimeMillis();
        long diff = now - _lastClickTime;
        if (0 < diff && diff < limit) {
            return true;
        }
        _lastClickTime = now;
        return false;
    }

}
 