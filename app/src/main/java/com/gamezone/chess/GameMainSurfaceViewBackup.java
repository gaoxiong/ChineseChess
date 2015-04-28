package com.gamezone.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by gaoxiong on 2015/4/27.
 */
public class GameMainSurfaceViewBackup extends SurfaceView implements SurfaceHolder.Callback {

  Context context;

  private SurfaceHolder holder;
  private Handler handler = new Handler();
  private Paint paint;
  private Canvas canvas;
  private Matrix matrix;

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {}

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {}

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
  }

  public GameMainSurfaceViewBackup(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    this.holder = this.getHolder();
    holder.addCallback(this);
    paint = new Paint();
    paint.setColor(Color.WHITE);
    paint.setAntiAlias(true);
    setFocusable(true);
  }

  public void draw() {
    try {
      canvas = holder.lockCanvas();
      canvas.drawRGB(0, 0, 0);
      canvas.save();

      //canvas.drawBitmap(bmp, matrix, paint);
      System.out.println("绘制图像了吗？");
      canvas.restore();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (canvas != null)
        holder.unlockCanvasAndPost(canvas);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return true;
  }
}
