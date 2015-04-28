package com.gamezone.chess;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

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

enum WhichView {
  WELCOME_VIEW,
  GAME_VIEW
};

public class Main_Activity extends Activity {
  GameMainSurfaceView gameView;
  WhichView whichView;
  WelcomeView welcomeView;
  SoundPool soundPool;
  HashMap<Integer, Integer> soundPoolMap;

  public Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch(msg.what) {
        case 0:
          whichView = WhichView.GAME_VIEW;
          gotoGameView();
          break;
      }
    }
  };

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
    initScreen();
    initSound();
    gotoWelcomeView();
//    handler.sendEmptyMessage(0);
  }

  private void initScreen() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    int tmpHeight = displayMetrics.heightPixels;
    int tmpWidth = displayMetrics.widthPixels;

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

  private void gotoGameView() {
//    gameView = new GameMainSurfaceView(Main_Activity.this);
//    setContentView(gameView);
    whichView = WhichView.GAME_VIEW;
  }

  private void gotoWelcomeView() {
    if (welcomeView == null) {
      welcomeView = new WelcomeView(this);
    }
    setContentView(welcomeView);
    whichView = WhichView.WELCOME_VIEW;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent e) {
    if (keyCode != 4) {
      return true;
    }
    if (whichView == WhichView.WELCOME_VIEW) {
      return true;
    }
    if (whichView == WhichView.GAME_VIEW) {
      gameView.threadFlag = false;
      System.exit(0);
      return true;
    }
    System.exit(0);
    return true;
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
