package com.xxs.sdk.player;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * 自定义网络 音乐播放器工具类
 * 
 * @author xiongxs
 * @date 2015-05-25
 */
public class XMusicPlayer implements OnPreparedListener, OnCompletionListener,
		OnBufferingUpdateListener {
	/** 单利对象 */
	public static XMusicPlayer xPlayer;
	/** 多媒体播放器 */
	private MediaPlayer mediaPlayer;
	/** 当前网络音乐播放地址 */
	private String musicurl;

	/** 单利模式获取唯一对象 */
	public XMusicPlayer getMethod() {
		if (xPlayer == null) {
			xPlayer = new XMusicPlayer();
		}
		return xPlayer;
	}

	/** 构造函数 */
	public XMusicPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	public void play(String url) {
		if (musicurl.equals(url) && mediaPlayer.isPlaying()) {

		} else {
			try {
				mediaPlayer.setDataSource(url);
				new Thread() {
					public void run() {
						try {
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {

						}
					};
				}.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
