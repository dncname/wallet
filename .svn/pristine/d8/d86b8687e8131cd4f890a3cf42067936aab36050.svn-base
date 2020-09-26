package com.zlw.base.ui.utils;

import android.widget.TextView;

import com.zlw.base.R;


public class Countdown {
	public static void start(final TextView view) {
		final long oldTime = System.currentTimeMillis();
		Runnable task = new Runnable() {
			@Override
			public void run() {
				long newTime = System.currentTimeMillis();
				long timer = 60 - (newTime - oldTime) / 1000;
				if (timer <= 0) {
					stop(view);
					return;
				}
				view.setText(view.getContext().getString(R.string.hd_second, (int) timer));
				view.setEnabled(false);
				view.postDelayed(this, 1000);
			}
		};
		view.setTag(task);
		view.setEnabled(false);
		view.postDelayed(task, 1000);
	}

	public static void stop(final TextView view) {
		if (view.getTag() instanceof Runnable) {
			view.removeCallbacks((Runnable) view.getTag());
			view.setTag(null);
			view.setEnabled(true);
			view.setText(view.getContext().getString(R.string.hd_send_auth_code));
		}
	}
}