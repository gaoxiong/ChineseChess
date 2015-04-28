package com.gamezone.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Stack;

import static com.gamezone.chess.ChessLoadUtil.DST;
import static com.gamezone.chess.LoadUtil.IsMate;
import static com.gamezone.chess.LoadUtil.LegalMove;
import static com.gamezone.chess.LoadUtil.MakeMove;
import static com.gamezone.chess.LoadUtil.SearchMain;
import static com.gamezone.chess.LoadUtil.mvResult;
import static com.gamezone.chess.ViewConsts.chessR;
import static com.gamezone.chess.ViewConsts.endTime;
import static com.gamezone.chess.ViewConsts.hardChooseFlag;
import static com.gamezone.chess.ViewConsts.hardCount;
import static com.gamezone.chess.ViewConsts.huiqiBS;
import static com.gamezone.chess.ViewConsts.isNoPlaySound;
import static com.gamezone.chess.ViewConsts.isNoStart;
import static com.gamezone.chess.ViewConsts.isNoTCNDChoose;
import static com.gamezone.chess.ViewConsts.scaleToFit;
import static com.gamezone.chess.ViewConsts.scoreWidth;
import static com.gamezone.chess.ViewConsts.settingsPopupFlag;
import static com.gamezone.chess.ViewConsts.shuJMFlag;
import static com.gamezone.chess.ViewConsts.startX;
import static com.gamezone.chess.ViewConsts.startY;
import static com.gamezone.chess.ViewConsts.windowHeight;
import static com.gamezone.chess.ViewConsts.windowWidth;
import static com.gamezone.chess.ViewConsts.windowXstartLeft;
import static com.gamezone.chess.ViewConsts.windowXstartRight;
import static com.gamezone.chess.ViewConsts.windowYstart;
import static com.gamezone.chess.ViewConsts.xSpan;
import static com.gamezone.chess.ViewConsts.xStartCK;
import static com.gamezone.chess.ViewConsts.xZoom;
import static com.gamezone.chess.ViewConsts.ySpan;
import static com.gamezone.chess.ViewConsts.yStartCK;
import static com.gamezone.chess.ViewConsts.yingJMFlag;
import static com.gamezone.chess.ViewConsts.zTime;

public class GameMainSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
  Context context;
  Bitmap[][] chessBitmap;
  Bitmap chessChosenFlag;
  Bitmap paotai;
  Bitmap paotai2;
  Bitmap paotai1;
  Bitmap chuhe;
  Paint paint;
  Bitmap chessQipanBg;
  Bitmap huiqi;
  Bitmap newGameBitmap;
  Bitmap[] iscore=new Bitmap[10];
  Bitmap dunhao;
  Bitmap bgImage;
  Bitmap menuBg;
  Bitmap iconLeftBottom;
  Bitmap isPlaySound;
  Bitmap noPlaySound;
  Bitmap startBitmap;
  Bitmap settingsBitmap;
  Bitmap suspend;
  Bitmap nandutiaoZ;
  Bitmap nonandutiaoZ;
  Bitmap guanggao1[]=new Bitmap[2];
  Bitmap guanggao2;

  Bitmap winJiemian;
  Bitmap loseJiemian;
  Bitmap fugaiTu;
  Bitmap queDinButton;
  Bitmap hardCountChooseBgOutline;
  Bitmap bgZoomOutline;
  int[][] color=new int[20][3];
  int length;
  int huiqibushu=0;
  float guanggao2X=0;
  boolean playChessflag;
  SurfaceHolder holder;
  Canvas canvas;
  int ucpcSquares[]=new int[256];
  Stack<StackPlayChess> stack=new Stack<StackPlayChess>();
  float xMove;
  float yMove;
  boolean isPlayerPlaying =true;
  boolean isFlage;
  boolean isPlayChessChosen = false;
  boolean threadFlag=true;
  int xzgz = 0;
  boolean flag;
  boolean huiqiFlag=false;
  boolean newGameFlag =false;
  boolean isnoNanDu=true;
  boolean dianjiNanDu;
  boolean dianjiXinJu;
  boolean isSettingBtnClicked;
  boolean dianjishengyin;
  boolean dianjiJDT;
  boolean dianjiQueDing;
  boolean nanduBXZ;
  int bzcol;
  int bzrow;

  float buttonY;
  float chessBuffer = 0.5f;
  float roundBuffer = 1.1f;
  float scale = 0.7f;
  float newGameBuffer;
  float addinBufferX;
  float spaceBufferX;
  float spaceBufferY;
  float settingsBuffer;
  float nanduBuffer;
  float huiqiBuffer;
  float soundBuffer;

  private int screenWidth, screenHeight;

  public GameMainSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context=context;
    this.getHolder().addCallback(this);
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(Color.WHITE);
    setFocusable(true);
    isNoStart=false;
    length=hardCount*4;
    initColor();
    LoadUtil.Startup();
    initArrays();
    initBitmap();
    LoadUtil.sdPlayer=0;
    endTime=zTime;
  }

  @Override
  public void onDraw(Canvas canvas) {
    drawCanvas(canvas);
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    this.holder=holder;
    canvas = null;
    try {
      canvas = holder.lockCanvas(null);
      synchronized (holder) {
        drawCanvas(canvas);
      }
    } finally {
      if (canvas != null) {
        holder.unlockCanvasAndPost(canvas);
      }
    }
    // time calc
    //newThread();
  }

  public void newThread()
  {
    new Thread(){
      @Override
      public void run()
      {
        while(threadFlag)
        {
          if (isNoStart)
          {
            if (endTime - 500<0)
            {
              if (!isPlayerPlaying)
              {
                yingJMFlag = true;
                LoadUtil.Startup();
                initArrays();
                endTime=zTime;
                isNoStart=false;
                dianjiJDT=false;
              } else {
                shuJMFlag=true;
                LoadUtil.Startup();
                initArrays();
                endTime=zTime;
                isNoStart=false;
                dianjiJDT=false;
              }
            } else {
              endTime-=500;
            }
          }
          guanggao2X-=10;
          if (guanggao2X<-400*xZoom)
          {
            guanggao2X=400*xZoom;
          }

          onDrawCanvas();
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
      }
    }.start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {}

  public void initArrays()
  {
    for(int i=0;i<256;i++)
    {
      ucpcSquares[i]=LoadUtil.ucpcSquares[i];
    }
  }

  private void drawCanvas(Canvas canvas) {
    canvas.drawColor(Color.argb(255, 0, 0, 0));
    Rect rectSrc = new Rect(0, 0, bgImage.getWidth(), bgImage.getHeight());
    RectF rectF = new RectF(startX, 0,
      ((GameMainViewActivity)context).getWindowManager().getDefaultDisplay().getWidth(),
      ((GameMainViewActivity)context).getWindowManager().getDefaultDisplay().getHeight());
    //canvas.drawBitmap(bgImage, rectSrc, rectF, null);

    if (isNoStart) {
      onDrawWindowindow(canvas, ViewConsts.startX, ViewConsts.startY);
      if (flag) {
//        float left=xMove>startX+5*xSpan?windowXstartLeft:windowXstartRight;
//        float top=windowYstart;
//        float right=left+windowWidth;
//        float bottom=top+windowHeight;
//        canvas.save();
//        canvas.clipRect(new RectF(left,top,right,bottom));
//        onDrawWindowindow(canvas, xStartCK, yStartCK);
//        canvas.restore();
//        canvas.drawBitmap(bgZoomOutline,left-6,top-6, null);
      }
    } else {
      canvas.drawBitmap(guanggao1[(int) ((Math.abs(guanggao2X/40)%2))], startX, startY, null);
    }

    onDrawWindowMenu(canvas,ViewConsts.startX,ViewConsts.startY);
    drawPopupWindows(canvas, yingJMFlag, shuJMFlag, settingsPopupFlag, hardChooseFlag);
  }

  private void drawPopupWindows(Canvas canvas, boolean winFlag, boolean loseFlag, boolean settingsFlag, boolean hardChooseFlag) {
    if (winFlag)
    {
      canvas.drawBitmap(fugaiTu,startX,startY, null);
      canvas.drawBitmap(winJiemian,startX+2.5f*xSpan,startY+5f*ySpan, null);
      if (dianjiQueDing) {
        canvas.drawBitmap(scaleToFit(queDinButton,0.8f),startX + 3.7f*xSpan,startY + 9.3f*ySpan, null);
      } else {
        canvas.drawBitmap(scaleToFit(queDinButton,0.6f), startX + 3.9f * xSpan, startY + 9.5f * ySpan, null);
      }
    } else if (loseFlag) {
      canvas.drawBitmap(fugaiTu,startX,startY, null);
      canvas.drawBitmap(loseJiemian,startX+2.5f*xSpan,startY+5f*ySpan, null);
      if (dianjiQueDing){
        canvas.drawBitmap(scaleToFit(queDinButton,0.8f),startX+3.7f*xSpan,startY+9.3f*ySpan, null);
      } else {
        canvas.drawBitmap(scaleToFit(queDinButton,0.6f), startX + 3.9f * xSpan, startY + 9.5f * ySpan, null);
      }
    } else if (settingsFlag) {
      drawSettings();
    } else if (hardChooseFlag) {
      drawHardChooseWindow();
    }
  }

  private void drawHardChooseWindow() {
//    if (!isNoStart&&nanduBXZ)
//    {
//      canvas.drawBitmap(hardCountChooseBgOutline,startX,startY+14.6f*ySpan, null);
//      Rect r = new Rect(50,50,200,200);
//
//      if (dianjiJDT)
//      {
//        length=(int)((xMove-startX)/(xZoom*20));
//        hardCount=(int)((xMove-startX)/(90*xZoom));
//        if (hardCount<1)
//        {
//          hardCount=1;
//        }
//        if (hardCount>5)
//        {
//          hardCount=5;
//        }
//        Consts.LIMIT_DEPTH=hardCount*6;
//      }
//
//
//      if (length<1)
//      {
//        length=1;
//      }else if (length>20)
//      {
//        length=20;
//      }
//      for(int i=0;i<length;i++)
//      {
//        paint.setARGB(color[i][0], color[i][1], color[i][2], 0);
//        r=new Rect((int) (startX+60*xZoom+i*xZoom*18),(int)(startY+15.3f*ySpan),
//          (int) (startX+60*xZoom+(i)*xZoom*18+13*xZoom),(int) (startY+15.3f*ySpan+32*xZoom));
//        canvas.drawRect(r, paint);
//      }
//
//    }else
//    {
//      canvas.drawBitmap(menuBg,startX,startY+14.6f*ySpan, null);
//      float left=startX+40*xZoom;
//      float top=startY+15f*ySpan;
//      float right=startX+9*xSpan;
//      float bottom=startY+15.2f*ySpan+40*xZoom;
//      canvas.save();
//      canvas.clipRect(new RectF(left,top,right,bottom));
//      canvas.drawBitmap(guanggao2,guanggao2X,startY+14.8f*ySpan, null);
//      canvas.restore();
//    }
  }

  private void drawSettings() {
//    canvas.drawBitmap(fugaiTu,startX,startY, null);
//    canvas.drawBitmap(loseJiemian,startX+2.5f*xSpan,startY+5f*ySpan, null);
//    canvas.drawBitmap(queDinButton, startX + 3.9f * xSpan, startY + 8.5f * ySpan, null);
  }

  public void onDrawWindowindow(Canvas canvas,float startX,float startY)
  {
    canvas.drawBitmap(chessQipanBg, startX, startY, null);

    paint.setColor(Color.RED);
    paint.setStrokeWidth(3);

    for (int i = 0; i < 10; i++) {
      canvas.drawLine(xSpan+startX,ySpan+ySpan*i+startY, startX+xSpan*9, startY+ySpan+ySpan*i, paint);
    }

    for (int i = 0; i < 9; i++) {
      canvas.drawLine(startX+xSpan+i*xSpan,startY+ySpan, startX+xSpan+xSpan*i, startY+ySpan*10, paint);
    }

    canvas.drawLine(startX+xSpan*4,startY+ySpan, startX+xSpan*6, startY+ySpan*3, paint);
    canvas.drawLine(startX+xSpan*6,startY+ySpan, startX+xSpan*4, startY+ySpan*3, paint);

    canvas.drawLine(startX+xSpan*4,startY+ySpan*8, startX+xSpan*6, startY+ySpan*10, paint);
    canvas.drawLine(startX+xSpan*6,startY+ySpan*8, startX+xSpan*4, startY+ySpan*10, paint);

    paint.setStrokeWidth(5);
    canvas.drawLine(startX+0.8f*xSpan,startY+0.8f*ySpan, startX+9.2f*xSpan, startY+0.8f*ySpan, paint);
    canvas.drawLine(startX+0.8f*xSpan,startY+0.8f*ySpan, startX+0.8f*xSpan, startY+10.2f*ySpan, paint);
    canvas.drawLine(startX+9.2f*xSpan,startY+0.8f*ySpan, startX+9.2f*xSpan, startY+10.2f*ySpan, paint);
    canvas.drawLine(startX+0.8f*xSpan, startY+10.2f*ySpan,startX+9.2f*xSpan, startY+10.2f*ySpan, paint);

    canvas.drawBitmap(chuhe,startX+xSpan+1.8f,startY+5*ySpan+1.0f, null);
    canvas.drawBitmap(paotai,startX+2*xSpan-chessR*0.86f,startY+3*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+2*xSpan-chessR*0.86f,startY+8*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+8*xSpan-chessR*0.86f,startY+3*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+8*xSpan-chessR*0.86f,startY+8*ySpan-chessR*0.86f, null);

    canvas.drawBitmap(paotai2,startX+1*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+3*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+5*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+7*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai1,startX+9*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);

    canvas.drawBitmap(paotai2,startX+1*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+3*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+5*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai,startX+7*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);
    canvas.drawBitmap(paotai1,startX+9*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);

    for(int i=0;i<10;i++)
    {
      for(int j=0;j<9;j++)
      {
        if (ucpcSquares[(i+3)*16+j+3]!=0)
        {
          canvas.drawBitmap(chessBitmap[ucpcSquares[(i+3)*16+j+3]/16][
            ucpcSquares[(i+3)*16+j+3]%8],startX+j*xSpan-chessR+xSpan,startY+i*ySpan-chessR+ySpan, null);
        }
      }
    }
    if (flag)
    {
      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.SRC(xzgz))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.SRC(xzgz))-2)*ySpan-chessR, null);
      canvas.drawBitmap(chessChosenFlag,startX+(bzcol+1)*xSpan-chessR,
        startY+(bzrow+1)*ySpan-chessR, null);

    }
    if (isPlayerPlaying && stack.size()>0)
    {
      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.SRC(mvResult))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.SRC(mvResult))-2)*ySpan-chessR, null);

      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.DST(mvResult))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.DST(mvResult))-2)*ySpan-chessR, null);
    }
    if (flag)
    {
      paint.setAlpha(200);
      canvas.drawBitmap(scaleToFit(chessBitmap[ucpcSquares[xzgz]/16][
        ucpcSquares[xzgz]%8],2),xMove-2*chessR,yMove-2*chessR, paint);
    }
  }

  private void drawLeftRoundIcon(Canvas canvas) {
    buttonY = startY + 11.6f * ySpan;
    if (playChessflag) {
      canvas.drawBitmap(scaleToFit(chessBitmap[1][0], 0.9f),
        startX + chessBuffer * xSpan,
        buttonY - 0.05f * ySpan, null);
    } else {
      canvas.drawBitmap(scaleToFit(chessBitmap[0][0], 0.9f),
        startX + chessBuffer * xSpan,
        buttonY - 0.05f * ySpan, null);
    }
  }

  private void drawNewGameButton(Canvas canvas) {
    if (newGameFlag) {
      canvas.drawBitmap(scaleToFit(newGameBitmap, scale),
        startX + newGameBuffer * xSpan,
        buttonY, null);
    } else {
      canvas.drawBitmap(scaleToFit(newGameBitmap, scale),
        startX + newGameBuffer * xSpan,
        buttonY, null);
    }
  }

  private void drawSettingsButton(Canvas canvas) {
    if (isSettingBtnClicked) {
      canvas.drawBitmap(scaleToFit(settingsBitmap, scale),
        startX + settingsBuffer * xSpan,
        buttonY,
        null);
    } else {
      canvas.drawBitmap(scaleToFit(settingsBitmap, scale),
        startX + settingsBuffer * xSpan,
        buttonY,
        null);
    }
  }

  private void drawHardChooseButton(Canvas canvas) {
    canvas.drawBitmap(scaleToFit(nandutiaoZ, scale),
      startX + nanduBuffer * xSpan,
      buttonY, null);
  }

  private void drawRegretButton(Canvas canvas) {
    if (huiqiFlag) {
      canvas.drawBitmap(scaleToFit(huiqi, scale),
        startX + huiqiBuffer * xSpan,
        buttonY,
        null);
    } else {
      canvas.drawBitmap(scaleToFit(huiqi, scale),
        startX + huiqiBuffer * xSpan,
        buttonY,
        null);
    }
  }

  private void drawSoundButton(Canvas canvas) {
    if (isNoPlaySound) {
      if (dianjishengyin) {
        canvas.drawBitmap(scaleToFit(isPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      } else {
        canvas.drawBitmap(scaleToFit(isPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      }
    } else {
      if (dianjishengyin) {
        canvas.drawBitmap(scaleToFit(noPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      } else {
        canvas.drawBitmap(scaleToFit(noPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      }
    }
  }

  public void onDrawWindowMenu(Canvas canvas,float startX,float startY) {
    canvas.drawBitmap(scaleToFit(menuBg, 1f), startX, startY + 11.0f * ySpan, null);
    //canvas.drawBitmap(iconLeftBottom, startX + 0.5f * xSpan, startY + 11.4f * ySpan, null);
    drawLeftRoundIcon(canvas);

    //»æÖÆÊ±¼ä
//    drawScoreStr(canvas,endTime/1000/60<10?"0"+endTime/1000/60:endTime/1000/60+"",
//      startX+3f*xSpan,startY+11.4f*ySpan);
//    canvas.drawBitmap(dunhao,startX+scoreWidth*2+3f*xSpan,startY+11.4f*ySpan, null);
//    drawScoreStr(canvas,endTime/1000%60<10?"0"+endTime/1000%60:endTime/1000%60+"",
//      scoreWidth*3+startX+3f*xSpan,startY+11.4f*ySpan);

    //canvas.drawBitmap(scaleToFit(menuBg,1f),startX,startY+12.8f*ySpan, null);
    newGameBuffer = chessBuffer + roundBuffer;
    addinBufferX = 1.8f;
    spaceBufferY = 0.68f;
    spaceBufferX = addinBufferX - 1.5f;
    drawNewGameButton(canvas);

    settingsBuffer = newGameBuffer + addinBufferX;
    drawSettingsButton(canvas);
//    if (isNoStart) {
//      if (isSettingBtnClicked) {
//        canvas.drawBitmap(scaleToFit(suspend, scale),
//          startX + settingsBuffer * xSpan,
//          buttonY,
//          null);
//      } else {
//        canvas.drawBitmap(scaleToFit(suspend, scale),
//          startX + settingsBuffer * xSpan,
//          buttonY,
//          null);
//      }
//    } else {
//      if (isSettingBtnClicked) {
//        canvas.drawBitmap(scaleToFit(startBitmap, scale),
//          startX + settingsBuffer * xSpan,
//          buttonY,
//          null);
//      } else {
//        canvas.drawBitmap(scaleToFit(startBitmap, scale),
//          startX + settingsBuffer * xSpan,
//          buttonY,
//          null);
//      }
//    }

    nanduBuffer = settingsBuffer + addinBufferX;
    drawHardChooseButton(canvas);
//    if (!isNoStart) {
//      if (dianjiNanDu) {
//        canvas.drawBitmap(scaleToFit(nandutiaoZ, scale),
//          startX + nanduBuffer * xSpan,
//          buttonY, null);
//      } else {
//        canvas.drawBitmap(scaleToFit(nandutiaoZ, scale),
//          startX + nanduBuffer * xSpan,
//          buttonY, null);
//      }
//    } else {
//      canvas.drawBitmap(scaleToFit(nonandutiaoZ, scale),
//        startX + nanduBuffer * xSpan,
//        buttonY, null);
//    }

    huiqiBuffer = nanduBuffer + addinBufferX;
    drawRegretButton(canvas);

    soundBuffer = huiqiBuffer + addinBufferX;
    drawSoundButton(canvas);
  }

  public void initColor()
  {
    int r=(200-61)/20;
    int g=(159-24)/20;
    int b=(107-6)/20;
    for(int i=0;i<20;i++)
    {

      color[i][0]=61+i*r;
      color[i][1]=24+i*g;
      color[i][2]=5+i*b;
    }
  }
  public void drawScoreStr(Canvas canvas,String s,float width,float height)//»æÖÆ×Ö·û´®·½·¨
  {
    //»æÖÆµÃ·Ö
    String scoreStr=s;
    if (s.length()<2)
    {
      s="0"+s;
    }
    for(int i=0;i<scoreStr.length();i++){//Ñ­»·»æÖÆµÃ·Ö
      int tempScore=scoreStr.charAt(i)-'0';
      canvas.drawBitmap(iscore[tempScore], width+i*scoreWidth,height, null);
    }
  }
  public void initBitmap()
  {
    float xZoom = ViewConsts.xZoom;

    bgZoomOutline = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijingkuangtu),xZoom);
    hardCountChooseBgOutline =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijing3),xZoom);
    fugaiTu=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.shuyingfugai),xZoom);
    winJiemian=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.yingjiemian),xZoom);
    loseJiemian=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.shuijiemian),xZoom);
    queDinButton=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.queding),xZoom);
    guanggao1[0]=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanggao1),xZoom);
    guanggao1[1]=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.floor),xZoom);
    guanggao2=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanggao2),xZoom);
    nonandutiaoZ=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nonandu),xZoom);
    nandutiaoZ=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nanduxuanz),xZoom);
    suspend=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.zhanting),xZoom);
    startBitmap =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.start),xZoom);
    settingsBitmap = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.settings),xZoom);
    isPlaySound=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.kaiqishengy),xZoom);
    noPlaySound=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanbishengy),xZoom);
    iconLeftBottom =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.caidanxiaqifang),xZoom);
    menuBg =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijing),xZoom);
    bgImage =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijintu),xZoom);
    newGameBitmap =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.chonwan),xZoom);
    huiqi = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.huiqi), xZoom);

    chessQipanBg =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.floor2),xZoom);
    chuhe=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.chuhe),xZoom);
    chessChosenFlag =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.selected),xZoom);
    iscore[0] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d0),xZoom);
    iscore[1] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d1),xZoom);
    iscore[2] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d2),xZoom);
    iscore[3] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d3),xZoom);
    iscore[4] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d4),xZoom);
    iscore[5] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d5),xZoom);
    iscore[6] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d6),xZoom);
    iscore[7] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d7),xZoom);
    iscore[8] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d8),xZoom);
    iscore[9] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d9),xZoom);

    dunhao=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.dunhao),xZoom);
    xZoom=xZoom*0.6f;
    paotai=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai),xZoom);
    paotai1=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai1),xZoom);
    paotai2=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai2),xZoom);
    xZoom=ViewConsts.xZoom*0.9f;

    chessBitmap=new Bitmap[][] {//Æå×Ó
      {
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.bk),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.ba),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.bb),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.bn),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.br),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.bc),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.bp),xZoom),

      },{
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rk),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.ra),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rb),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rn),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rr),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rc),xZoom),
        scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.rp),xZoom),
      }
    };
  }


  private boolean isOverPlayAgainBtn(MotionEvent e) {
    if (e.getX() > startX + newGameBuffer * xSpan &&
      e.getX() < startX + (settingsBuffer - spaceBufferX) * xSpan &&
      e.getY() > buttonY &&
      e.getY() < buttonY + spaceBufferY * xSpan) {
      return true;
    }
    return false;
  }

  private boolean isOverSettingsBtn(MotionEvent e) {
    if (e.getX() > startX + settingsBuffer * xSpan &&
      e.getX() < startX + (nanduBuffer - spaceBufferX) * xSpan &&
      e.getY() > buttonY &&
      e.getY() < buttonY + spaceBufferY * xSpan) {
      return true;
    }
    return false;
  }

  private boolean isOverHardChooseBtn(MotionEvent e) {
    if (e.getX() > startX + nanduBuffer * xSpan &&
      e.getX() < startX + (huiqiBuffer - spaceBufferX) * xSpan &&
      e.getY() > buttonY &&
      e.getY() < buttonY + spaceBufferY * xSpan) {
      return true;
    }
    return false;
  }

  private boolean isOverRegretBtn(MotionEvent e) {
    if (e.getX() > startX + huiqiBuffer * xSpan &&
      e.getX() < startX + (soundBuffer - spaceBufferX) * xSpan &&
      e.getY() > buttonY &&
      e.getY() < buttonY + spaceBufferY * xSpan) {
      return true;
    }
    return false;
  }

  private boolean isOverSoundBtn(MotionEvent e) {
    if (e.getX() > startX + soundBuffer * xSpan &&
      e.getX() < startX + (soundBuffer + roundBuffer - spaceBufferX) * xSpan &&
      e.getY() > buttonY &&
      e.getY() < buttonY + spaceBufferY * xSpan) {
      return true;
    }
    return false;
  }

  private boolean handleTouchEventDown(MotionEvent e) {
    if (e.getAction() != MotionEvent.ACTION_DOWN)
      return false;

    if (yingJMFlag || shuJMFlag) {
      if (e.getX()>startX+3.9f*xSpan&&e.getY()>startY+8.5f*ySpan&&
        e.getY()<startY+8.5f*ySpan+1.5f*ySpan&&e.getX()<startX+3.9f*xSpan+2*xSpan)
      {
        dianjiQueDing=true;
      }

      return true;
    } else if (settingsPopupFlag) {

    }
    if (isOverPlayAgainBtn(e)) {
      newGameFlag = true;
      return true;
    }
    if (isOverSettingsBtn(e)) {
      isSettingBtnClicked = true;
      return true;
    }
    if (isOverHardChooseBtn(e)) {
      dianjiNanDu = true;
      return true;
    }
    if (isOverRegretBtn(e)) {
      huiqiFlag = true;
      return true;
    }
    if (isOverSoundBtn(e)) {
      dianjishengyin = true;
      return true;
    }

    // hard choose area
    if (e.getX()>startX&&e.getY()>startY+15.3f*ySpan&&
      e.getY()<startY+15.3f*ySpan+1*ySpan&&e.getX()<startX+10f*xSpan+2*xSpan) {
      xMove=e.getX();
      dianjiJDT=true;
      return true;
    }

    // is over chess area?
    if ((bzrow+3)*16+bzcol+3<0||(bzrow+3)*16+bzcol+3>255||!isNoStart)
    {
      return false;
    }

    // is player's chess chosen?
    if (ucpcSquares[(bzrow+3)*16+bzcol+3]!=0 &&
      ucpcSquares[(bzrow+3)*16+bzcol+3]/16==0)
    {

      isPlayChessChosen = true;
      xzgz=(bzrow+3)*16+bzcol+3; // position of chosen chess

      flag=true;
      onDrawCanvas();
      return true;
    }
    return false;
  }

  private boolean handleTouchEventMove(MotionEvent e) {
    float x=xMove=e.getX();
    float y=yMove=e.getY();
    // some chess has been chosen, will move
    if (isPlayChessChosen)
    {

      if (x>48*10*xZoom+startX-windowWidth/2)
      {
        x=48*10*xZoom+startX-windowWidth/2;
      }
      else if (x<startX+windowWidth/2+0*48*xZoom){
        x=startX+windowWidth/2-0*48*xZoom;
      }
      if (y>48*11*xZoom+startY-windowHeight/2)
      {
        y=48*11*xZoom+startY-windowHeight/2;
      }
      else if (y<startY+windowHeight/2+0*48*xZoom){
        y=startY+windowHeight/2-0*48*xZoom;
      }
      if (xMove>startX+5*xSpan)
      {
        xStartCK=startX-(x-windowWidth/2-windowXstartLeft);
        yStartCK=startY-(y-windowHeight/2-windowYstart);

      }
      else{
        xStartCK=startX-(x-windowWidth/2-windowXstartRight);
        yStartCK=startY-(y-windowHeight/2-windowYstart);
      }
      onDrawCanvas();
      return true;
    }
    else
    {
      isPlayChessChosen =false;
      flag=false;
      onDrawCanvas();
      return true;
    }
  }

  private boolean handleTouchEventUp(MotionEvent e) {
    if (e.getAction() != MotionEvent.ACTION_UP) {
      return false;
    }
    //
    if (yingJMFlag||shuJMFlag)
    {
      if (dianjiQueDing==true)
      {
        if (e.getX()>startX+3.9f*xSpan&&e.getY()>startY+8.5f*ySpan&&//Ë­ÏÈÏÂ
          e.getY()<startY+8.5f*ySpan+1.5f*ySpan&&e.getX()<startX+3.9f*xSpan+2*xSpan)
        {
          shuJMFlag=false;
          yingJMFlag=false;
          LoadUtil.Startup();
          initArrays();
          LoadUtil.sdPlayer=0;
          endTime=zTime;
          isNoStart=false;
        }
        dianjiQueDing=false;
      }
      onDrawCanvas();

      return true;
    }

    if (isOverRegretBtn(e)) {
      if (huiqiFlag == true) {
        return doRegret();
      }
    } else if (huiqiFlag) {
      huiqiFlag = false;
      onDrawCanvas();
      return true;
    }

    if (isOverPlayAgainBtn(e)) {
      if (newGameFlag == true) {
        return doNewGame();
      }
      newGameFlag = false;
      onDrawCanvas();
      return true;
    }

    if (isSettingBtnClicked) {
      isSettingBtnClicked = false;
      if (isOverSettingsBtn(e)) {
        doPopupSettings();
      }
      onDrawCanvas();
      return true;
    }

    if (dianjiNanDu) {
      if (isOverHardChooseBtn(e)) {
        doHardBtnClicked();
      }
      dianjiNanDu = false;
      onDrawCanvas();
      return true;
    }

    if (dianjishengyin) {
      if (isOverSoundBtn(e)) {
        isNoPlaySound = !isNoPlaySound;
      }
      dianjishengyin = false;
      onDrawCanvas();
      return true;
    }

    if (isPlayChessChosen)
    {
      if ((bzrow+3)*16+bzcol+3<0||(bzrow+3)*16+bzcol+3>255)
      {
        isPlayChessChosen =false;
        flag=false;
        onDrawCanvas();
        return true;
      }
      if (bzrow<0||bzrow>9||bzcol<0||bzcol>8)
      {
        isPlayChessChosen =false;
        flag=false;
        onDrawCanvas();
        return true;
      }
      int sqDst = DST(xzgz+((bzrow+3)*16+bzcol+3)*256);
      int pcCaptured = ucpcSquares[sqDst];
      int mv=xzgz+((bzrow+3)*16+bzcol+3)*256;
      if (stack.size()>12&&
        mv==stack.get(stack.size()-4).mvResult&&
        stack.get(stack.size()-1).mvResult==stack.get(stack.size()-5).mvResult&&
        stack.get(stack.size()-5).mvResult==stack.get(stack.size()-9).mvResult&&
        stack.get(stack.size()-2).mvResult==stack.get(stack.size()-6).mvResult&&
        stack.get(stack.size()-6).mvResult==stack.get(stack.size()-10).mvResult&&
        stack.get(stack.size()-3).mvResult==stack.get(stack.size()-7).mvResult&&
        stack.get(stack.size()-3).mvResult==stack.get(stack.size()-11).mvResult&&
        stack.get(stack.size()-4).mvResult==stack.get(stack.size()-8).mvResult&&
        stack.get(stack.size()-8).mvResult==stack.get(stack.size()-12).mvResult){
        isPlayChessChosen =false;
        flag=false;
        onDrawCanvas();
        Toast.makeText(
          context,
          R.string.repeat_move_chess,
          Toast.LENGTH_SHORT).show();
        return true;
      }
      if (LegalMove(mv)) {
        if (MakeMove(mv, 0)) {
          initArrays();
          ((GameMainViewActivity)context).playSound(2, 1);
          huiqibushu=0;
          onDrawCanvas();
          stack.push(new StackPlayChess(xzgz+((bzrow+3)*16+bzcol+3)*256,pcCaptured));
          if (IsMate()) {
            LoadUtil.Startup();
            initArrays();
            yingJMFlag=true;
            ((GameMainViewActivity)context).playSound(4,1);
            onDrawCanvas();
          } else {
            new Thread(){
              @Override
              public void run()
              {
                endTime=zTime;
                playChessflag=true;
                isPlayerPlaying =false;
                onDrawCanvas();
                SearchMain();
                int sqDst = DST(mvResult);
                int pcCaptured = ucpcSquares[sqDst];
                MakeMove(mvResult, 0);

                stack.push(new StackPlayChess(mvResult,pcCaptured));
                initArrays();
                if (LoadUtil.IsMate())
                {
                  LoadUtil.Startup();
                  initArrays();
                  shuJMFlag=true;
                  ((GameMainViewActivity)context).playSound(5, 1);
                }else
                  ((GameMainViewActivity)context).playSound(2, 1);
                isPlayerPlaying =true;
                playChessflag=false;
                endTime=zTime;
                onDrawCanvas();
              }
            }.start();

          }
        }
        isPlayChessChosen =false;
        flag=false;
        onDrawCanvas();

      }
      else{
        isPlayChessChosen =false;
        flag=false;
        onDrawCanvas();
      }
    }
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    if (!isPlayerPlaying) {
      return false;
    }

    int col = (int) ((e.getX() - startX) / xSpan);
    int row = (int) ((e.getY() - startY) / ySpan);

    if (((e.getX() - col * xSpan - startX) * (e.getX() - col * xSpan - startX) +
      (e.getY()-row*ySpan-startY)*(e.getY()-row*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol = col-1;
      bzrow=row-1;
    }
    else if (((e.getX()-col*xSpan-startX)*(e.getX()-col*xSpan-startX)+
      (e.getY()-(row+1)*ySpan-startY)*(e.getY()-(row+1)*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col-1;
      bzrow=row;
    }
    else if (((e.getX()-(1+col)*xSpan-startX)*(e.getX()-(1+col)*xSpan-startX)+
      (e.getY()-(row+1)*ySpan-startY)*(e.getY()-(row+1)*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col;
      bzrow=row;
    }
    else if (
      ((e.getX()-(1+col)*xSpan-startX)*(e.getX()-(1+col)*xSpan-startX)+
        (e.getY()-row*ySpan-startY)*(e.getY()-row*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col;
      bzrow=row-1;
    }

    if (e.getAction() == MotionEvent.ACTION_DOWN) {
      return handleTouchEventDown(e);
    } else if (e.getAction()==MotionEvent.ACTION_MOVE) {
      return handleTouchEventMove(e);
    } else if (e.getAction()==MotionEvent.ACTION_UP) {
      return handleTouchEventUp(e);
    }

    return super.onTouchEvent(e);
  }

  private void doPopupSettings() {
    settingsPopupFlag = true;
    drawSettings();
  }

  private void doHardBtnClicked() {
    if (!isNoStart) {
      nanduBXZ = !nanduBXZ;
      isNoTCNDChoose = !isNoTCNDChoose;
    } else {
      isNoTCNDChoose = false;
      dianjiJDT = false;
    }
    isnoNanDu = !isnoNanDu;
  }

  private boolean doRegret() {
    if (!stack.empty() && stack.size() > 1) {
      if (huiqibushu > huiqiBS) {
        Toast.makeText(context, R.string.cannot_reget_anymore,
          Toast.LENGTH_SHORT).show();
        return true;
      }
      huiqibushu++;
      StackPlayChess chess = stack.pop();
      LoadUtil.UndoMovePiece(chess.mvResult, chess.pcCaptured);

      chess=stack.pop();
      LoadUtil.UndoMovePiece(chess.mvResult, chess.pcCaptured);
      if (!stack.empty()) {
        mvResult=stack.peek().mvResult;
      }
      initArrays();
    }
    huiqiFlag=false;
    onDrawCanvas();
    return false;
  }

  private boolean doNewGame() {
    // clear flags
    shuJMFlag = false;
    yingJMFlag = false;
    settingsPopupFlag = false;
    isNoStart = false;
    endTime = zTime;
    stack.clear();
    LoadUtil.Startup();
    initArrays();

    isNoStart = true;
    nanduBXZ = false;
    newGameFlag = false;
    if (!isNoStart) {
      dianjiJDT = false;
      isNoTCNDChoose = false;
    }
    onDrawCanvas();
    return false;
  }

  public void onDrawCanvas() {
    try {
      canvas = holder.lockCanvas(null);
      synchronized (holder) {
        drawCanvas(canvas);
      }
    } finally {
      if (canvas != null) {
        holder.unlockCanvasAndPost(canvas);
      }
    }
  }
}
