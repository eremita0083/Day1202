package com.example.day1202;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;

public class MyService extends Service {
	private MediaPlayer mp;
	private int currentId;
	private int exId;
	private int randomNumber;
	private boolean nowPlaying;
	private Random rand = new java.util.Random();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			// playの処理
			if (intent.getAction().equals("play")) {
				final int[] rawIds = bundle.getIntArray("musicId");
				currentId = rawIds[bundle.getInt("position")];
				if (mp == null) {
					mp = MediaPlayer.create(getApplicationContext(), currentId);
					mp.setLooping(true);
					mp.start();
					nowPlaying = true;
					exId = currentId; // mpのインスタンス化と同時に判別するためのIDを取得する
				} else if (mp != null) {
					if (mp.isPlaying()) {
						if (exId != currentId) {
							mp.stop();
							mp.reset();
							mp.release();
							mp = MediaPlayer.create(getApplicationContext(),
									currentId);
							mp.start();
							nowPlaying = true;
							exId = currentId;
						} else if (exId == currentId) {
							mp.pause();
						}
					} else if (!mp.isPlaying()) {
						if (exId != currentId) {
							mp.stop();
							mp.reset();
							mp.release();
							mp = MediaPlayer.create(getApplicationContext(),
									currentId);
							mp.start();
							nowPlaying = true;
							exId = currentId;
						} else if (exId == currentId) {
							mp.start();
							nowPlaying = true;
						}
					}
				}
			} else if (intent.getAction().equals("shufflePlay")) {
				final int[] rawIds = bundle.getIntArray("musicId");
				randomNumber = rand.nextInt(rawIds.length);
				int id = rawIds[randomNumber];
				if (mp != null) {
					mp.stop();
					mp.reset();
					mp.release();
				}
				mp = MediaPlayer.create(getApplicationContext(), id);
				mp.start();

				mp.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						Intent intent = new Intent(getApplicationContext(),MyService.class);
						intent.setAction("shufflePlay");
						intent.putExtra("musicId", rawIds);
						startService(intent);
					}
				});

			} else if (intent.getAction().equals("stop")) {
				if (mp != null) {
					mp.stop();
					mp.reset();
					mp.release();
					mp = null;
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mp.isPlaying()) {
			mp.stop();
		}
		mp.release();
		mp = null;
	}

}
