package com.xxs.sdk.player;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.myinterface.XPlayerCallBack;

/**
 * 自定义网络视频播放器工具类
 *
 * @author xiongxs
 * @date 2015-05-25
 */
public class XVideoPlayer implements OnPreparedListener, OnCompletionListener,
		OnBufferingUpdateListener, Callback, OnErrorListener, OnInfoListener {
	/** 多媒体播放器 */
	private MediaPlayer mediaPlayer;
	/** 当前网络视频播放地址 */
	private String videourl;
	/** 播放器回调接口 */
	private XPlayerCallBack xPlayerCallBack;
	/** 当前播放时间 */
	private int currentposition;
	/**调用暂停方法是的当前播放时间*/
	private int lastposition;
	/** 用于加载视频的控件 */
	private SurfaceHolder surfaceHolder;
	/** 是否已经播放 */
	private boolean isplaying;
	/** 系统原本声音大小 */
	private int currentvoice;

	/** 构造函数进行必要的初始化工作 */
	public XVideoPlayer(SurfaceView surfaceView) {
		initMediaPlayer();
		LayoutParams lp = surfaceView.getLayoutParams();
		surfaceView.setLayoutParams(lp);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// Surface类型
		surfaceHolder.setKeepScreenOn(true);
	}

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnInfoListener(this);
	}

	/**
	 * 播放视频的方法
	 *
	 * @param url
	 *            网络视频地址
	 */
	public void playVideo(String url) {
		if (mediaPlayer == null) {
			isplaying = false;
			initMediaPlayer();
			if (!url.equals(videourl)){
				lastposition = 0;
			}
		}
		if (url.equals(videourl) && isplaying) {
			mediaPlayer.seekTo(currentposition);
			mediaPlayer.start();
		} else {
			isplaying = false;
			this.videourl = url;
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(videourl);
				new Thread() {
					public void run() {
						try {
							myHandler.sendEmptyMessage(1);
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							myHandler.sendEmptyMessage(0);
						}
					};
				}.start();
			} catch (Exception e) {
				xPlayerCallBack.onLoadFalse(0);
			}
		}
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					xPlayerCallBack.onLoadFalse(1);
					break;
				case 1:
					xPlayerCallBack.onPlayType(4);
					break;
				default:
					break;
			}
		}
	};

	/** 停止播放的方法 */
	public void stopVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			xPlayerCallBack.onPlayType(2);
			surfaceHolder.setKeepScreenOn(false);
		}
	}

	/** 暂停播放的方法 */
	public void pauseVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			lastposition = mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
			xPlayerCallBack.onPlayType(1);
			surfaceHolder.setKeepScreenOn(false);
		}
	}

	/** 播放的方法 */
	public void playVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(currentposition);
			mediaPlayer.start();
			xPlayerCallBack.onPlayType(0);
		}
	}

	/**
	 * 关闭或打开声音的方法
	 *
	 * @param isOpen
	 *            true 打开 false 关闭
	 */
	public void closeOrOpenVoice(boolean isOpen) {
		AudioManager audiomanage = (AudioManager) AppContext.mMainContext
				.getSystemService(Context.AUDIO_SERVICE);
		if (isOpen) {
			audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC,
					currentvoice, 0);
		} else {
			currentvoice = audiomanage
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		}
	}

	/** 获取播放器回调接口 */
	public XPlayerCallBack getxPlayerCallBack() {
		return xPlayerCallBack;
	}

	/** 设置播放器回调接口 */
	public void setxPlayerCallBack(XPlayerCallBack xPlayerCallBack) {
		this.xPlayerCallBack = xPlayerCallBack;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (mediaPlayer.isPlaying())
			xPlayerCallBack.onBufferUpdatePercent(percent);
		currentposition = mp.getCurrentPosition();
		if (mediaPlayer.isPlaying())
			xPlayerCallBack.onPlayPercent(currentposition * 100
					/ mediaPlayer.getDuration());
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		currentposition = mediaPlayer.getDuration();
		xPlayerCallBack.onPlayPercent(100);
		xPlayerCallBack.onPlayType(3);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		isplaying = true;
		mediaPlayer.seekTo(lastposition);
		mediaPlayer.start();
		xPlayerCallBack.onPlayType(0);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mediaPlayer.setDisplay(holder);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/** 释放资源 */
	public void releaseMethod() {
		videourl = null;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
			case MediaPlayer.MEDIA_ERROR_IO:
				xPlayerCallBack.onLoadFalse(1);
				break;
			case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
				xPlayerCallBack.onLoadFalse(2);
				break;
			case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
				xPlayerCallBack.onLoadFalse(3);
				break;
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				xPlayerCallBack.onLoadFalse(4);
				break;
			case MediaPlayer.MEDIA_ERROR_MALFORMED:
				xPlayerCallBack.onLoadFalse(4);
				break;

			default:
				xPlayerCallBack.onLoadFalse(4);
				break;
		}
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
			case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:// 开始播放
				xPlayerCallBack.onPlayType(0);
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:// 缓冲完毕
				xPlayerCallBack.onPlayType(0);
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:// 开始缓冲
				xPlayerCallBack.onPlayType(4);
				break;
			default:
				break;
		}
		return false;
	}

}
