package com.gamezone.chess;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by gaoxiong on 15-4-26.
 */
public class GameMainViewFragment extends Fragment {

  private GameView gameView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  private void init() {
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    RelativeLayout relativeLayout =
      (RelativeLayout) inflater.inflate(R.layout.game_main_view, container, false);
    if (relativeLayout == null) {
      return null;
    }
    gameView = new GameView(this.getActivity());
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.FILL_PARENT,
      RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    gameView.setLayoutParams(layoutParams);
    relativeLayout.addView(gameView);
    return relativeLayout;
  }

  public void setThreadFlag(boolean flag) {
    gameView.setThreadFlag(flag);
  }
}
