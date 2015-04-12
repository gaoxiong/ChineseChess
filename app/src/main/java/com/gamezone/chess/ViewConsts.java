package com.gamezone.chess;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by gaoxiong on 15-4-8.
 */
public class ViewConsts {
  public static float yBuffer = 50f;
  public static float startX = 0;
  public static float startY = 0;

  public static float height;
  public static float width;

  public static float xZoom = 1f;
  public static float yZoom = 1f;

  public static boolean yingJMFlag;
  public static boolean shuJMFlag;
  public static int huiqiBS = 2;
  public static boolean isComputerPlayChess = false;
  public static boolean isHeqi = false;
  public static boolean isNoStart = false;
  public static boolean isNoTCNDChoose;
  public static int hardCount = 1;
  public static float thinkDeeplyTime = 0.3f;
  public static int zTime = 900000;
  public static int endTime = zTime;
  public static float xSpan = 48.0f * xZoom;
  public static float ySpan = 48.0f * yZoom;
  public static float scoreWidth = 7 * xZoom * 4f;
  public static float xStartCK;
  public static float yStartCK;
  public static float windowWidth = 200 * xZoom;
  public static float windowHeight = 400 * yZoom;
  public static float windowXstartLeft = startX + (5 * xSpan - windowWidth) / 2 * xZoom;
  public static float windowXstartRight = startY + 5 * xSpan + (5 * xSpan - windowWidth) / 2 * xZoom;
  public static float windowYstart = startY + ySpan * 4 * xZoom;
  public static float chessR = 30 * xZoom;
  public static float fblRatio = 0.6f * xZoom;

  public static boolean isNoPlaySound = true;

  public static Bitmap scaleToFit(Bitmap bm, float fblRatio) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    Matrix matrix = new Matrix();
    matrix.postScale(fblRatio, fblRatio);
    Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    return bmResult;
  }

  public static void initChessViewFinal() {
    xSpan = 48.0f * xZoom;
    ySpan = 48.0f * yZoom;

    scoreWidth = 7 * xZoom * 4f;
    windowWidth = 200 * xZoom;
    windowHeight = 250 * xZoom;

    windowXstartLeft = startX + (5 * xSpan - windowWidth) / 2 * xZoom;
    windowXstartRight = startY + 5 * xSpan + (5 * xSpan - windowWidth) / 2 * xZoom;
    windowYstart = startY + ySpan * 1 * xZoom;

    chessR = 30 * xZoom;
    fblRatio = 0.6f * xZoom;
  }
}
