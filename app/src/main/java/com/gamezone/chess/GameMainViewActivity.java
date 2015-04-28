package com.gamezone.chess;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;

import static com.gamezone.chess.ViewConsts.isNoPlaySound;

/**
 * Created by gaoxiong on 2015/4/27.
 */
public class GameMainViewActivity extends Activity {

  SoundPool soundPool;
  HashMap<Integer, Integer> soundPoolMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    this.getActionBar().hide();
    initSound();

    AdView mAdViewTop = (AdView) findViewById(R.id.adViewTop);
    AdRequest adRequestTop = new AdRequest.Builder().build();
    mAdViewTop.loadAd(adRequestTop);

    AdView mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);
    AdRequest adRequestBottom = new AdRequest.Builder().build();
    mAdViewBottom.loadAd(adRequestBottom);
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
