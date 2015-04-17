package com.gamezone.chess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.gamezone.chess.ViewConsts.*;

/**
 * Created by gaoxiong on 15-4-8.
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
  Main_Activity activity;
  Paint paint;
  int curAlpha = 0;
  int sleepSpan = 50;
  Bitmap[] logos = new Bitmap[2];
  Bitmap curLogo;
  int curX;
  int curY;

  public WelcomeView(Main_Activity activity) {
    super(activity);
    this.activity = activity;
    this.getHolder().addCallback(this);
    paint = new Paint();
    paint.setAntiAlias(true);

    float xZoom = ViewConsts.xZoom;
    if (xZoom < 1) {
      xZoom *= 1.5f;
    }
    logos[0] = scaleToFit(BitmapFactory.decodeResource(activity.getResources(), R.drawable.baina), xZoom);
    logos[1] = scaleToFit(BitmapFactory.decodeResource(activity.getResources(), R.drawable.bnkjs), xZoom);
  }

  public void onDraw(Canvas canvas) {
    drawCanvas(canvas);
  }

  private void drawCanvas(Canvas canvas) {
    paint.setColor(Color.BLACK);
    paint.setAlpha(255);
    canvas.drawRect(0, 0, width, height, paint);

    if (curLogo == null) {
      return;
    }
    paint.setAlpha(curAlpha);
    canvas.drawBitmap(curLogo, curX, curY, paint);
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    new Thread() {
      public void run() {
        for (Bitmap bm : logos) {
          curLogo = bm;
          curX = (int) (width / 2 - bm.getWidth() / 2);
          curY = (int) (height / 2 - bm.getHeight() / 2);
          for(int i = 255; i > -10; i = i - 10)
          {
            curAlpha = i;
            if(curAlpha < 0) {
              curAlpha = 0;
            }
            SurfaceHolder myHolder = WelcomeView.this.getHolder();
            Canvas canvas = myHolder.lockCanvas();
            try {
              synchronized (myHolder) {
                drawCanvas(canvas);
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              if (canvas != null) {
                myHolder.unlockCanvasAndPost(canvas);
              }
            }
            try {
              if (i == 255) {
                Thread.sleep(1000);
              }
              Thread.sleep(sleepSpan);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        activity.handler.sendEmptyMessage(0);
      }
    }.start();
  }
}
