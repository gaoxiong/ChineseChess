package com.gamezone.chess;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;

import static com.gamezone.chess.ViewConsts.height;
import static com.gamezone.chess.ViewConsts.initChessViewFinal;
import static com.gamezone.chess.ViewConsts.isNoPlaySound;
import static com.gamezone.chess.ViewConsts.startX;
import static com.gamezone.chess.ViewConsts.startY;
import static com.gamezone.chess.ViewConsts.width;
import static com.gamezone.chess.ViewConsts.xZoom;
import static com.gamezone.chess.ViewConsts.yBuffer;
import static com.gamezone.chess.ViewConsts.yZoom;

/**
 * Created by gaoxiong on 2015/4/27.
 */
public class GameMainViewActivity extends Activity {

  SoundPool soundPool;
  HashMap<Integer, Integer> soundPoolMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    );
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    int tmpHeight = displayMetrics.heightPixels;
    int tmpWidth = displayMetrics.widthPixels;
    initScreen(tmpWidth, tmpHeight);
    initSound();

    setContentView(R.layout.main);
    initAds();
  }

  public void initScreen(int targetWidth, int targetHeight) {

    int tmpHeight = targetHeight;
    int tmpWidth = targetWidth;

    if (tmpHeight > tmpWidth) {
      height = tmpHeight;
      width = tmpWidth;
    } else {
      height = tmpWidth;
      width = tmpHeight;
    }

    float zoomx = width / 480;
    float zoomy = height / 800;

    if (zoomx > zoomy) {
      xZoom = yZoom = zoomy;
    } else {
      xZoom = yZoom = zoomx;
    }
    startX = (width - 480 * xZoom) / 2;
    startY = (height - 800 * yZoom) / 2 + yBuffer;
    initChessViewFinal();
  }

  private void initAds() {
    AdView mAdViewTop = (AdView) findViewById(R.id.adViewTop);
    AdRequest adRequestTop = new AdRequest.Builder().build();
    mAdViewTop.loadAd(adRequestTop);
    int topWidth = mAdViewTop.getMeasuredWidth();
    int topHeight = mAdViewTop.getMeasuredHeight();

    AdView mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);
    AdRequest adRequestBottom = new AdRequest.Builder().build();
    mAdViewBottom.loadAd(adRequestBottom);
    int bottomWidth = mAdViewBottom.getMeasuredWidth();
    int bottomHeight = mAdViewBottom.getMeasuredHeight();
  }

  private void initSound() {
    soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
    soundPoolMap = new HashMap<Integer, Integer>();
    soundPoolMap.put(1, soundPool.load(this, R.raw.noxiaqi, 1));
    soundPoolMap.put(2, soundPool.load(this, R.raw.dong, 1));
    soundPoolMap.put(4, soundPool.load(this, R.raw.win, 1));
    soundPoolMap.put(5, soundPool.load(this, R.raw.loss, 1));
  }

  public void playSound(int sound, int loop) {
    if (!isNoPlaySound) {
      return;
    }
    AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    float volume = streamVolumeCurrent / streamVolumeMax;
    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
  }
}
