package com.gamezone.chess;

import static com.gamezone.chess.ChessLoadUtil.DST;
import static com.gamezone.chess.LoadUtil.IsMate;
import static com.gamezone.chess.LoadUtil.LegalMove;
import static com.gamezone.chess.LoadUtil.MakeMove;
import static com.gamezone.chess.LoadUtil.SearchMain;
import static com.gamezone.chess.LoadUtil.mvResult;
import static com.gamezone.chess.ViewConsts.*;

import java.util.Stack;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
  Main_Activity father;
  Bitmap[][] chessBitmap;//ÏóÆåÆå×ÓÍ¼Æ¬
  Bitmap chessChosenFlag;//±êÖ¾×ßÆå
  Bitmap paotai;//ÅÚÌ¨
  Bitmap paotai2;//ÅÚÌ¨±ßÑØµÄÊ±ºò
  Bitmap paotai1;
  Bitmap chuhe;//³þºÓÍ¼Æ¬
  Paint paint;//»­±Ê
  Bitmap chessQipanBg;//ÆåÅÌ±ß¿ò
  Bitmap huiqi;//»ÚÆåÍ¼Æ¬
  Bitmap chonWan;//ÖØÍæÎÄ×Ö
  Bitmap[] iscore=new Bitmap[10];//Êý×ÖÍ¼
  Bitmap dunhao;//¶ÙºÅ
  Bitmap bgImage;//±³¾°Í¼
  Bitmap menuBg;//²Ëµ¥±³¾°Í¼
  Bitmap iconLeftBottom;//±êÊ¶ÏÂÆå·½µÄ±³¾°
  Bitmap isPlaySound;//¿ªÆôÉùÒô
  Bitmap noPlaySound;//¹Ø±ÕÉùÒô
  Bitmap start;//¿ªÊ¼
  Bitmap suspend;//ÔÝÍ£
  Bitmap nandutiaoZ;//ÄÑ¶È
  Bitmap nonandutiaoZ;//ÄÑ¶È²»¿ÉÓÃ
  Bitmap guanggao1[]=new Bitmap[2];//¹ã¸æÌõ1
  Bitmap guanggao2;//¹ã¸æÌõ2

  Bitmap winJiemian;//Ó®½çÃæ
  Bitmap loseJiemian;//Êä½çÃæ
  Bitmap fugaiTu;//¸²¸ÇÍ¼
  Bitmap queDinButton;//È·¶¨°´Å¥
  Bitmap hardCountChooseBgOutline;//ÍÏ¶¯Ìõ±³¾°
  Bitmap bgZoomOutline;//¼ô²Ã¿ò±³¾°
  int[][] color=new int[20][3];
  int length;//ÄÑ¶ÈÊý
  int huiqibushu=0;
  float guanggao2X=0;//
  boolean playChessflag;//ÏÂÆå·½±êÖ¾Î»£¬falseÎªºÚ·½ÏÂÆå
  SurfaceHolder holder;//»­²¼
  Canvas canvas;
  int ucpcSquares[]=new int[256];
  Stack<StackPlayChess> stack=new Stack<StackPlayChess>();
  float xMove;
  float yMove;//ÒÆ¶¯×ø±ê
  boolean isPlayerPlaying =true;//´¥ÃþÊÇ·ñÓÐÐ§
  boolean isFlage;//ÊÇ·ñÎªµÚÒ»´ÎÏÂÆå
  boolean xzflag = false;//ÊÇ·ñÎªÑ¡ÖÐ£¬Ñ¡ÖÐÖÐÐèÒªÒÆ¶¯µÄ»°¾Í»æÖÆÒÆ¶¯
  boolean threadFlag=true;//Ïß³ÌÊÇ·ñÔËÐÐ
  int xzgz = 0;//Ñ¡ÖÐµÄ³õÊ¼¸ñ×Ó
  boolean flag;//ÊÇ·ñÎªÒÆ¶¯±êÖ¾
  boolean huiqiFlag=false;//µã»÷´¦Îª»ÚÆå±êÖ¾
  boolean chonwanFlag=false;//µã»÷´¦ÎªÖØÍæ±êÖ¾
  boolean isnoNanDu=true;//ÄÑ¶ÈÊÇ·ñ¿ÉÑ¡
  boolean dianjiNanDu;//µã»÷´¦ÎªÄÑ¶È
  boolean dianjiXinJu;//µã»÷´¦ÎªÐÂ¾Ö
  boolean dianjiKaiShi;//µã»÷´¦Îª¿ªÊ¼
  boolean dianjishengyin;//µã»÷ÎªÉùÒôÇøÓò
  boolean dianjiJDT;//µã»÷´¦ÎªÍÏ¶¯
  boolean dianjiQueDing;//µã»÷´¦ÎªÈ·¶¨°´Å¥
  boolean nanduBXZ;
  int bzcol;
  int bzrow;
  public GameView(Context context) {
    super(context);
    this.father=(Main_Activity)context;
    this.getHolder().addCallback(this);//ÉèÖÃÉúÃüÖÜÆÚ»Øµ÷½Ó¿ÚµÄÊµÏÖÕß
    paint = new Paint();//´´½¨»­±Ê
    paint.setAntiAlias(true);//´ò¿ª¿¹¾â³Ý
    isNoStart=false;
    length=hardCount*4;
    initColor();//³õÊ¼»¯ÄÑ¶È¹ö¶¯ÌõÑÕÉ«Êý×é
    LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
    initArrays();//³õÊ¼»¯Êý×é
    initBitmap(); ///³õÊ¼»¯Í¼Æ¬
    LoadUtil.sdPlayer=0;//ÏÂÆå·½Îª
    endTime=zTime;//×ÜÊ±¼ä
  }
  public void initArrays()
  {
    for(int i=0;i<256;i++)
    {
      ucpcSquares[i]=LoadUtil.ucpcSquares[i];
    }
  }
  @Override
  public void onDraw(Canvas canvas)
  {
    canvas.drawColor(Color.argb(255, 0, 0, 0));
    canvas.drawBitmap(bgImage, startX, 0, null);

    if (isNoStart) {
      onDrawWindowindow(canvas, ViewConsts.startX, ViewConsts.startY);
      if(flag) {
        float left=xMove>startX+5*xSpan?windowXstartLeft:windowXstartRight;
        float top=windowYstart;
        float right=left+windowWidth;
        float bottom=top+windowHeight;
        canvas.save();
        canvas.clipRect(new RectF(left,top,right,bottom));
        onDrawWindowindow(canvas, xStartCK, yStartCK);//Ð¡´°¿Ú
        canvas.restore();
        canvas.drawBitmap(bgZoomOutline,left-6,top-6, null);
      }
    } else {
      canvas.drawBitmap(guanggao1[(int) ((Math.abs(guanggao2X/40)%2))],startX, startY, null);
    }

    onDrawWindowMenu(canvas,ViewConsts.startX,ViewConsts.startY);
    if(yingJMFlag)//Èç¹ûÊÇÓ®ÁË
    {
      canvas.drawBitmap(fugaiTu,startX,startY, null);//»­¸²¸ÇÍ¼
      canvas.drawBitmap(winJiemian,startX+2.5f*xSpan,startY+5f*ySpan, null);//Ó®±³¾°¸ÇÍ¼
      if(dianjiQueDing)
      {
        canvas.drawBitmap(scaleToFit(queDinButton,1.2f),startX+3.7f*xSpan,startY+8.3f*ySpan, null);//È·¶¨°´Å¥
      }else
        canvas.drawBitmap(queDinButton,startX+3.9f*xSpan,startY+8.5f*ySpan, null);//È·¶¨°´Å¥
    }
    else if(shuJMFlag)//ÊäÁË
    {
      canvas.drawBitmap(fugaiTu,startX,startY, null);//»­¸²¸ÇÍ¼
      canvas.drawBitmap(loseJiemian,startX+2.5f*xSpan,startY+5f*ySpan, null);//Ó®±³¾°¸ÇÍ¼
      if(dianjiQueDing){
        canvas.drawBitmap(scaleToFit(queDinButton,1.2f),startX+3.7f*xSpan,startY+8.3f*ySpan, null);//È·¶¨°´Å¥
      }else
        canvas.drawBitmap(queDinButton,startX+3.9f*xSpan,startY+8.5f*ySpan, null);//È·¶¨°´Å¥
    }
  }

  public void onDrawWindowindow(Canvas canvas,float startX,float startY)
  {
    canvas.drawBitmap(chessQipanBg, startX, startY, null);

    //»æÖÆºìÉ«Ìî³ä¾ØÐÎ
    paint.setColor(Color.RED);//ÉèÖÃ»­±ÊÑÕÉ«

    paint.setColor(Color.RED);//ÉèÖÃ»­±ÊÑÕÉ«
    paint.setStrokeWidth(3);//ÉèÖÃÏßµÄ´ÖÏ¸

    for(int i=0;i<10;i++)//»­ºáÏß
    {
      canvas.drawLine(xSpan+startX,ySpan+ySpan*i+startY, startX+xSpan*9, startY+ySpan+ySpan*i, paint);
    }

    for(int i=0;i<9;i++)//»­ÊúÏß
    {
      canvas.drawLine(startX+xSpan+i*xSpan,startY+ySpan, startX+xSpan+xSpan*i, startY+ySpan*10, paint);
    }

    canvas.drawLine(startX+xSpan*4,startY+ySpan, startX+xSpan*6, startY+ySpan*3, paint);//»æÖÆ¾Å¹¬Ð±Ïß
    canvas.drawLine(startX+xSpan*6,startY+ySpan, startX+xSpan*4, startY+ySpan*3, paint);

    canvas.drawLine(startX+xSpan*4,startY+ySpan*8, startX+xSpan*6, startY+ySpan*10, paint);
    canvas.drawLine(startX+xSpan*6,startY+ySpan*8, startX+xSpan*4, startY+ySpan*10, paint);

    //»æÖÆ±ß¿ò
    paint.setStrokeWidth(5);//ÉèÖÃÏßµÄ´ÖÏ¸
    canvas.drawLine(startX+0.8f*xSpan,startY+0.8f*ySpan, startX+9.2f*xSpan, startY+0.8f*ySpan, paint);
    canvas.drawLine(startX+0.8f*xSpan,startY+0.8f*ySpan, startX+0.8f*xSpan, startY+10.2f*ySpan, paint);
    canvas.drawLine(startX+9.2f*xSpan,startY+0.8f*ySpan, startX+9.2f*xSpan, startY+10.2f*ySpan, paint);
    canvas.drawLine(startX+0.8f*xSpan, startY+10.2f*ySpan,startX+9.2f*xSpan, startY+10.2f*ySpan, paint);

    canvas.drawBitmap(chuhe,startX+xSpan+1.8f,startY+5*ySpan+1.0f, null);//»æÖÆ³þºÓ
    canvas.drawBitmap(paotai,startX+2*xSpan-chessR*0.86f,startY+3*ySpan-chessR*0.86f, null);//»æÖÆÅÚÌ¨
    canvas.drawBitmap(paotai,startX+2*xSpan-chessR*0.86f,startY+8*ySpan-chessR*0.86f, null);//»æÖÆÅÚÌ¨
    canvas.drawBitmap(paotai,startX+8*xSpan-chessR*0.86f,startY+3*ySpan-chessR*0.86f, null);//»æÖÆÅÚÌ¨
    canvas.drawBitmap(paotai,startX+8*xSpan-chessR*0.86f,startY+8*ySpan-chessR*0.86f, null);//»æÖÆÅÚÌ¨

    canvas.drawBitmap(paotai2,startX+1*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);//»æÖÆ±øÌ¨
    canvas.drawBitmap(paotai,startX+3*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai,startX+5*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai,startX+7*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai1,startX+9*xSpan-chessR*0.86f,startY+4*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨

    canvas.drawBitmap(paotai2,startX+1*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);//»æÖÆ±øÌ¨
    canvas.drawBitmap(paotai,startX+3*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai,startX+5*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai,startX+7*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨
    canvas.drawBitmap(paotai1,startX+9*xSpan-chessR*0.86f,startY+7*ySpan-chessR*0.86f, null);//»æÖÆ±øÅÚÌ¨

    //»­Æå×Ó
    for(int i=0;i<10;i++)//»æÖÆÆå×Ó
    {
      for(int j=0;j<9;j++)
      {
        if(ucpcSquares[(i+3)*16+j+3]!=0)
        {
          canvas.drawBitmap(chessBitmap[ucpcSquares[(i+3)*16+j+3]/16][
            ucpcSquares[(i+3)*16+j+3]%8],startX+j*xSpan-chessR+xSpan,startY+i*ySpan-chessR+ySpan, null);
        }
      }
    }
    if(flag)//»æÖÆÍÏÀ­Ð§¹û
    {
      //»æÖÆÑ¡ÖÐÒª×ßÆå×ÓµÄ±êÖ¾
      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.SRC(xzgz))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.SRC(xzgz))-2)*ySpan-chessR, null);
      canvas.drawBitmap(chessChosenFlag,startX+(bzcol+1)*xSpan-chessR,
        startY+(bzrow+1)*ySpan-chessR, null);//»æÖÆÒÆ¶¯Ê±ÒÆ¶¯µ½µÄÄ³¸ñ

    }
    if(isPlayerPlaying &&stack.size()>0)//»æÖÆµçÄÔÏÂÆåµÄ±êÖ¾
    {
      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.SRC(mvResult))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.SRC(mvResult))-2)*ySpan-chessR, null);

      canvas.drawBitmap(chessChosenFlag,startX+(ChessLoadUtil.FILE_X(ChessLoadUtil.DST(mvResult))-2)*xSpan-chessR,
        startY+(ChessLoadUtil.RANK_Y(ChessLoadUtil.DST(mvResult))-2)*ySpan-chessR, null);
    }
    if(flag)
    {
      paint.setAlpha(200);//»æÖÆ·Å´óµÄÆå×Ó
      canvas.drawBitmap(scaleToFit(chessBitmap[ucpcSquares[xzgz]/16][
        ucpcSquares[xzgz]%8],2),xMove-2*chessR,yMove-2*chessR, paint);
    }
    //»æÖÆ»ÚÆåµÈ²Ëµ¥
  }

  public void onDrawWindowMenu(Canvas canvas,float startX,float startY)
  {
    canvas.drawBitmap(scaleToFit(menuBg, 1f), startX, startY + 11.0f * ySpan, null);//²Ëµ¥±³¾°Í¼
    //canvas.drawBitmap(iconLeftBottom, startX + 0.5f * xSpan, startY + 11.4f * ySpan, null);

    float buttonY = startY + 11.6f * ySpan;
    float scale = 0.7f;
    float chessBuffer = 0.5f;
    if (playChessflag) {
      canvas.drawBitmap(scaleToFit(chessBitmap[0][0], 0.9f),
        startX + chessBuffer * xSpan,
        buttonY - 0.05f * ySpan, null);
    } else {
      canvas.drawBitmap(scaleToFit(chessBitmap[1][0], 0.9f),
        startX + chessBuffer * xSpan,
        buttonY - 0.05f * ySpan, null);
    }

    //»æÖÆÊ±¼ä
//    drawScoreStr(canvas,endTime/1000/60<10?"0"+endTime/1000/60:endTime/1000/60+"",
//      startX+3f*xSpan,startY+11.4f*ySpan);
//    canvas.drawBitmap(dunhao,startX+scoreWidth*2+3f*xSpan,startY+11.4f*ySpan, null);//¶ÙºÅ
//    drawScoreStr(canvas,endTime/1000%60<10?"0"+endTime/1000%60:endTime/1000%60+"",
//      scoreWidth*3+startX+3f*xSpan,startY+11.4f*ySpan);

    //canvas.drawBitmap(scaleToFit(menuBg,1f),startX,startY+12.8f*ySpan, null);//²Ëµ¥±³¾°Í¼
    float againBuffer = chessBuffer + 1.1f;
    float addinBuffer = 1.8f;
    if(chonwanFlag) {
      canvas.drawBitmap(scaleToFit(chonWan, scale),
        startX + againBuffer * xSpan,
        buttonY, null);//ÖØÍæ
    } else {
      canvas.drawBitmap(scaleToFit(chonWan, scale),
        startX + againBuffer * xSpan,
        buttonY, null);//ÖØÍæ
    }

    float startBuffer = againBuffer + addinBuffer;
    if(isNoStart) {
      if(dianjiKaiShi) {
        canvas.drawBitmap(scaleToFit(suspend, scale),
          startX + startBuffer * xSpan,
          buttonY,
          null);
      } else {
        canvas.drawBitmap(scaleToFit(suspend, scale),
          startX + startBuffer * xSpan,
          buttonY,
          null);
      }
    } else {
      if(dianjiKaiShi) {
        canvas.drawBitmap(scaleToFit(start, scale),
          startX + startBuffer * xSpan,
          buttonY,
          null);
      } else {
        canvas.drawBitmap(scaleToFit(start, scale),
          startX + startBuffer * xSpan,
          buttonY,
          null);
      }
    }

    float nanduBuffer = startBuffer + addinBuffer;
    if (!isNoStart) {
      if(dianjiNanDu) {
        canvas.drawBitmap(scaleToFit(nandutiaoZ, scale),
          startX + nanduBuffer * xSpan,
          buttonY, null);
      } else {
        canvas.drawBitmap(scaleToFit(nandutiaoZ, scale),
          startX + nanduBuffer * xSpan,
          buttonY, null);
      }
    } else {
      canvas.drawBitmap(scaleToFit(nonandutiaoZ, scale),
        startX + nanduBuffer * xSpan,
        buttonY, null);
    }

    float huiqiBuffer = nanduBuffer + addinBuffer;
    if (huiqiFlag) {
      canvas.drawBitmap(scaleToFit(huiqi, scale),
        startX + huiqiBuffer * xSpan,
        buttonY,
        null);//»ÚÆå
    } else {
      canvas.drawBitmap(scaleToFit(huiqi, scale),
        startX + huiqiBuffer * xSpan,
        buttonY,
        null);//»ÚÆå
    }

    float soundBuffer = huiqiBuffer + addinBuffer;
    if(isNoPlaySound) {
      if(dianjishengyin) {
        canvas.drawBitmap(scaleToFit(isPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      } else {
        canvas.drawBitmap(scaleToFit(isPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      }
    } else {
      if(dianjishengyin) {
        canvas.drawBitmap(scaleToFit(noPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      } else {
        canvas.drawBitmap(scaleToFit(noPlaySound, scale),
          startX + soundBuffer * xSpan,
          buttonY, null);
      }
    }

    if(!isNoStart&&nanduBXZ)//Èç¹ûÎª°´ÏÂÁËÄÑ¶È
    {

      canvas.drawBitmap(hardCountChooseBgOutline,startX,startY+14.6f*ySpan, null);
      Rect r=new Rect(50,50,200,200);

      if(dianjiJDT)
      {
        length=(int)((xMove-startX)/(xZoom*20));
        hardCount=(int)((xMove-startX)/(90*xZoom));
        if(hardCount<1)
        {
          hardCount=1;
        }
        if(hardCount>5)
        {
          hardCount=5;
        }
        Consts.LIMIT_DEPTH=hardCount*6;
      }


      if(length<1)
      {
        length=1;
      }else if(length>20)
      {
        length=20;
      }
      for(int i=0;i<length;i++)
      {
        paint.setARGB(color[i][0], color[i][1], color[i][2], 0);//ÉèÖÃ»­±ÊÑÕÉ«
        r=new Rect((int) (startX+60*xZoom+i*xZoom*18),(int)(startY+15.3f*ySpan),
          (int) (startX+60*xZoom+(i)*xZoom*18+13*xZoom),(int) (startY+15.3f*ySpan+32*xZoom));
        canvas.drawRect(r, paint);
      }

    }else
    {
      canvas.drawBitmap(menuBg,startX,startY+14.6f*ySpan, null);
      //»æÖÆ¹ã¸æ
      float left=startX+40*xZoom;
      float top=startY+15f*ySpan;
      float right=startX+9*xSpan;
      float bottom=startY+15.2f*ySpan+40*xZoom;
      canvas.save();
      canvas.clipRect(new RectF(left,top,right,bottom));
      canvas.drawBitmap(guanggao2,guanggao2X,startY+14.8f*ySpan, null);
      canvas.restore();
    }
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
    if(s.length()<2)
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

    bgZoomOutline = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijingkuangtu),xZoom);//¼ô²Ã¿ò
    hardCountChooseBgOutline =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijing3),xZoom);//ÄÑ¶ÈÑ¡ÔñÍÏ¶¯Ëü±³¾°
    fugaiTu=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.shuyingfugai),xZoom);//¸²¸ÇÍ¼
    winJiemian=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.yingjiemian),xZoom);//Ó®½çÃæ
    loseJiemian=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.shuijiemian),xZoom);//Êä½çÃæ
    queDinButton=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.queding),xZoom);//ÔÙÀ´Ò»¾Ö
    guanggao1[0]=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanggao1),xZoom);
    guanggao1[1]=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.floor),xZoom);
    guanggao2=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanggao2),xZoom);//¹ã¸æ12
    nonandutiaoZ=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nonandu),xZoom);//ÄÑ¶È
    nandutiaoZ=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nanduxuanz),xZoom);//ÄÑ¶È
    suspend=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.zhanting),xZoom);//ÔÝÍ£
    start=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.start),xZoom);//¿ªÊ¼
    isPlaySound=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.kaiqishengy),xZoom);//¿ªÆôÉùÒôÍ¼Æ¬
    noPlaySound=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.guanbishengy),xZoom);//¹Ø±ÕÉùÒôÍ¼Æ¬
    iconLeftBottom =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.caidanxiaqifang),xZoom);//²Ëµ¥ÏÂÆå·½±³¾°Í¼
    menuBg =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijing),xZoom);//²Ëµ¥±³¾°Í¼
    bgImage =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.beijintu),xZoom);//±³¾°Í¼
    chonWan=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.chonwan),xZoom);//ÖØÍæ
    huiqi = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.huiqi), xZoom);//»ÚÆå

    chessQipanBg =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.floor2),xZoom);//ÆåÅÌ
    chuhe=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.chuhe),xZoom);//³þºÓ
    chessChosenFlag =scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.selected),xZoom);//±êÖ¾Î»
    iscore[0] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d0),xZoom);//Êý×ÖÍ¼
    iscore[1] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d1),xZoom);
    iscore[2] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d2),xZoom);
    iscore[3] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d3),xZoom);
    iscore[4] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d4),xZoom);
    iscore[5] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d5),xZoom);
    iscore[6] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d6),xZoom);
    iscore[7] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d7),xZoom);
    iscore[8] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d8),xZoom);
    iscore[9] = scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d9),xZoom);

    dunhao=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.dunhao),xZoom);//¶ÙºÅ
    xZoom=xZoom*0.6f;
    paotai=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai),xZoom);//ÅÚÌ¨
    paotai1=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai1),xZoom);//ÅÚÌ¨2
    paotai2=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.paotai2),xZoom);//ÅÚÌ¨2
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

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {}
  @Override
  public void surfaceCreated(SurfaceHolder holder) {

    this.holder=holder;
    canvas = null;
    try {
        // Ëø¶¨Õû¸ö»­²¼£¬ÔÚÄÚ´æÒªÇó±È½Ï¸ßµÄÇé¿öÏÂ£¬½¨Òé²ÎÊý²»ÒªÎªnull
        canvas = holder.lockCanvas(null);
        synchronized (holder) {
          onDraw(canvas);//»æÖÆ
        }
    } finally {
      if (canvas != null) {
        //²¢ÊÍ·ÅËø
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
          if(isNoStart)
          {
            if (endTime - 500<0)
            {
              if (!isPlayerPlaying)//Èç¹ûµçÄÔÕýÔÚÏÂÆå£¬Ê±¼ä¶àÁË£¬ÔòÎªµçÄÔÊäÁË
              {
                yingJMFlag = true;
                LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
                initArrays();//³õÊ¼»¯Êý×é
                endTime=zTime;
                isNoStart=false;
                dianjiJDT=false;
              } else {//ÔòÎª×Ô¼ºÊäÁË
                shuJMFlag=true;
                LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
                initArrays();//³õÊ¼»¯Êý×é
                endTime=zTime;
                isNoStart=false;
                dianjiJDT=false;
              }
            } else {
              endTime-=500;
            }
          }
          guanggao2X-=10;
          if(guanggao2X<-400*xZoom)
          {
            guanggao2X=400*xZoom;
          }

          onDrawcanvas();//ÖØ»æ·½·¨
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
      bzcol=col-1;
      bzrow=row-1;//¿´ÆäÔÚÄÄÒ»¸ö¸ñ×ÓÉÏ
    }
    else if(((e.getX()-col*xSpan-startX)*(e.getX()-col*xSpan-startX)+
      (e.getY()-(row+1)*ySpan-startY)*(e.getY()-(row+1)*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col-1;
      bzrow=row;
    }
    else if(((e.getX()-(1+col)*xSpan-startX)*(e.getX()-(1+col)*xSpan-startX)+
      (e.getY()-(row+1)*ySpan-startY)*(e.getY()-(row+1)*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col;
      bzrow=row;
    }
    else if(
      ((e.getX()-(1+col)*xSpan-startX)*(e.getX()-(1+col)*xSpan-startX)+
        (e.getY()-row*ySpan-startY)*(e.getY()-row*ySpan-startY))<xSpan/2*xSpan/2)
    {
      bzcol=col;
      bzrow=row-1;
    }

    if(e.getAction() == MotionEvent.ACTION_DOWN)//Èç¹û°´ÏÂ
    {
      if(yingJMFlag||shuJMFlag)//Èç¹ûµ±Ç°ÎªÓ®½çÃæ
      {
        if(e.getX()>startX+3.9f*xSpan&&e.getY()>startY+8.5f*ySpan&&//Ë­ÏÈÏÂ
          e.getY()<startY+8.5f*ySpan+1.5f*ySpan&&e.getX()<startX+3.9f*xSpan+2*xSpan)
        {
          dianjiQueDing=true;
        }

        return true;
      }
      if(e.getX()>startX+0.5f*xSpan&&e.getY()>startY+13.3f*ySpan&&//ÐÂ¾Ö±êÖ¾
        e.getY()<startY+13.3f*ySpan+1*ySpan&&e.getX()<startX+0.5f*xSpan+2*xSpan)
      {
        chonwanFlag=true;
        return true;
      }
      if(e.getX()>startX+7f*xSpan&&e.getY()>startY+11.5f*ySpan&&//»ÚÆå±êÖ¾
        e.getY()<startY+11.5f*ySpan+1*ySpan&&e.getX()<startX+7f*xSpan+2*xSpan)
      {
        huiqiFlag=true;
        return true;
      }
      if(e.getX()>startX+3.3f*xSpan&&e.getY()>startY+13.5f*ySpan&&
        e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+3.3f*xSpan+2*xSpan)//¿ªÊ¼ÇøÓò
      {
        dianjiKaiShi=true;
        return true;
      }
      if(e.getX()>startX+5.8f*xSpan&&e.getY()>startY+13.5f*ySpan&&
        e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+5.8f*xSpan+2*xSpan)//ÄÑ¶ÈÇøÓò
      {
        dianjiNanDu=true;
        return true;
      }
      if(e.getX()>startX+8.3f*xSpan&&e.getY()>startY+13.5f*ySpan&&
        e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+8.3f*xSpan+2*xSpan)//ÉùÒôÇøÓò
      {
        dianjishengyin=true;
        return true;
      }

      if(e.getX()>startX&&e.getY()>startY+15.3f*ySpan&&
        e.getY()<startY+15.3f*ySpan+1*ySpan&&e.getX()<startX+10f*xSpan+2*xSpan)//½ø¶ÈÌõÇøÓò
      {
        xMove=e.getX();
        dianjiJDT=true;
        return true;
      }
      if((bzrow+3)*16+bzcol+3<0||(bzrow+3)*16+bzcol+3>255||!isNoStart)//Èç¹ûÃ»ÓÐ¿ªÊ¼
      {
        return false;
      }
      if(ucpcSquares[(bzrow+3)*16+bzcol+3]!=0&&ucpcSquares[(bzrow+3)*16+bzcol+3]/16==0)
      {//Èç¹ûÊÇ×Ô¼ºµÄÆå×Ó

        xzflag=true;
        xzgz=(bzrow+3)*16+bzcol+3;//Ñ¡ÖÐµÄ¸ñ×ÓÊÇÕâÃ´¶à

        flag=true;//Ñ¡ÖÐÁË
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }
    }
    else if(e.getAction()==MotionEvent.ACTION_MOVE)
    {
      float x=xMove=e.getX();
      float y=yMove=e.getY();
      if(xzflag)//Èç¹ûÒÑ¾­Ñ¡ÖÐ£¬Ôò´ËÎªÒª×ÅÆå
      {

        if(x>48*10*xZoom+startX-windowWidth/2)
        {
          x=48*10*xZoom+startX-windowWidth/2;
        }
        else if(x<startX+windowWidth/2+0*48*xZoom){
          x=startX+windowWidth/2-0*48*xZoom;
        }
        if(y>48*11*xZoom+startY-windowHeight/2)
        {
          y=48*11*xZoom+startY-windowHeight/2;
        }
        else if(y<startY+windowHeight/2+0*48*xZoom){
          y=startY+windowHeight/2-0*48*xZoom;
        }
        if(xMove>startX+5*xSpan)
        {
          xStartCK=startX-(x-windowWidth/2-windowXstartLeft);
          yStartCK=startY-(y-windowHeight/2-windowYstart);

        }
        else{
          xStartCK=startX-(x-windowWidth/2-windowXstartRight);
          yStartCK=startY-(y-windowHeight/2-windowYstart);
        }
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }
      else
      {
        xzflag=false;//»Ö¸´£¬²»ÎªÑ¡ÖÐ×´Ì¬
        flag=false;//Ñ¡ÖÐÁË
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }
    }
    else if(e.getAction()==MotionEvent.ACTION_UP)
    {

      if(yingJMFlag||shuJMFlag)//Èç¹ûµ±Ç°ÎªÓ®½çÃæ
      {
        if(dianjiQueDing==true)
        {
          if(e.getX()>startX+3.9f*xSpan&&e.getY()>startY+8.5f*ySpan&&//Ë­ÏÈÏÂ
            e.getY()<startY+8.5f*ySpan+1.5f*ySpan&&e.getX()<startX+3.9f*xSpan+2*xSpan)
          {
            shuJMFlag=false;
            yingJMFlag=false;
            LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
            initArrays();//³õÊ¼»¯Êý×é
            LoadUtil.sdPlayer=0;//ÏÂÆå·½Îª×Ô¼º
            endTime=zTime;//×ÜÊ±¼ä
            isNoStart=false;//ÎªÔÝÍ£×´Ì¬
          }
          dianjiQueDing=false;
        }
        onDrawcanvas();//ÖØ»æ·½·¨

        return true;
      }

      if(e.getX()>startX+7f*xSpan&&e.getY()>startY+11.5f*ySpan&&//»ÚÆå±êÖ¾
        e.getY()<startY+11.5f*ySpan+1*ySpan&&e.getX()<startX+7f*xSpan+2*xSpan)
      {//Èç¹ûÊÇ»ÚÆå
        if(huiqiFlag==true)
        {
          if(!stack.empty()&&stack.size()>1)
          {
            if(huiqibushu>huiqiBS)
            {
              Toast.makeText//Í¬Ê±·¢Toast.ÌáÊ¾Íæ¼Ò
                (
                  father,
                  "»ÚÆå²½ÊýÒÑ¾­³¬¹ý¹æ¶¨!",
                  Toast.LENGTH_SHORT
                ).show();
              return true;
            }
            huiqibushu++;
            StackPlayChess chess=stack.pop();
            LoadUtil.UndoMovePiece(chess.mvResult, chess.pcCaptured);

            chess=stack.pop();
            LoadUtil.UndoMovePiece(chess.mvResult, chess.pcCaptured);
            if(!stack.empty())
            {
              mvResult=stack.peek().mvResult;
            }
            initArrays();//Êý×é²Ù×÷
          }
          huiqiFlag=false;
          onDrawcanvas();//ÖØ»æ·½·¨
          return false;
        }

      }else if(huiqiFlag)
      {
        huiqiFlag=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }


      if(e.getX()>startX+0.5f*xSpan&&e.getY()>startY+13.3f*ySpan&&//ÐÂ¾Ö±êÖ¾
        e.getY()<startY+13.3f*ySpan+1*ySpan&&e.getX()<startX+0.5f*xSpan+2*xSpan)
      {
        if(chonwanFlag==true)
        {

          shuJMFlag=false;
          yingJMFlag=false;
          isNoStart=false;
          endTime=zTime;
          stack.clear();
          LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
          initArrays();//³õÊ¼»¯Êý×é
        }

        chonwanFlag=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }
      else if(chonwanFlag)
      {
        chonwanFlag=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }

      if(dianjiKaiShi)//¿ªÊ¼
      {
        if(e.getX()>startX+3.3f*xSpan&&e.getY()>startY+13.5f*ySpan&&
          e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+3.3f*xSpan+2*xSpan)//¿ªÊ¼ÇøÓò
        {
          //¸Ã´¦Îª½Óµã»÷¿ªÊ¼Ê±¸ù¾Ý²»Í¬Çé¿ö×ö²»Í¬²Ù×÷
          isNoStart=!isNoStart;
          nanduBXZ=false;
          if(!isNoStart)
          {
            dianjiJDT=false;//µã»÷ÍÏ¶¯
            isNoTCNDChoose=false;
          }
          dianjiKaiShi=false;
          onDrawcanvas();//ÖØ»æ·½·¨

        }
        dianjiKaiShi=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }


      if(dianjiNanDu)//ÄÑ¶ÈÇøÓò
      {
        if(e.getX()>startX+5.8f*xSpan&&e.getY()>startY+13.5f*ySpan&&
          e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+5.8f*xSpan+2*xSpan)//ÄÑ¶ÈÇøÓò
        {
          if(!isNoStart)//Èç¹ûÎªÔÝÍ£×´Ì¬ÏÂ£¬²Å¿ÉÓÃ
          {
            nanduBXZ=!nanduBXZ;
            isNoTCNDChoose=!isNoTCNDChoose;
          }
          else{
            isNoTCNDChoose=false;
            dianjiJDT=false;
          }

          isnoNanDu=!isnoNanDu;
        }

        dianjiNanDu=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }

      if(dianjishengyin)
      {
        if(e.getX()>startX+8.3f*xSpan&&e.getY()>startY+13.5f*ySpan&&
          e.getY()<startY+13.5f*ySpan+1*ySpan&&e.getX()<startX+8.3f*xSpan+2*xSpan)//ÉùÒôÇøÓò
        {
          isNoPlaySound=!isNoPlaySound;
        }

        dianjishengyin=false;
        onDrawcanvas();//ÖØ»æ·½·¨
        return true;
      }

      if(xzflag)//ÊÇ·ñÎªÑ¡ÔñµÄÆå×Ó
      {
        if((bzrow+3)*16+bzcol+3<0||(bzrow+3)*16+bzcol+3>255)
        {
          xzflag=false;
          flag=false;
          onDrawcanvas();//ÖØ»æ·½·¨
          return true;
        }
        if(bzrow<0||bzrow>9||bzcol<0||bzcol>8)
        {
          xzflag=false;
          flag=false;
          onDrawcanvas();//ÖØ»æ·½·¨
          return true;
        }
        int sqDst = DST(xzgz+((bzrow+3)*16+bzcol+3)*256);
        int pcCaptured = ucpcSquares[sqDst];//µÃµ½Ä¿µÄ¸ñ×ÓµÄÆå×Ó
        int mv=xzgz+((bzrow+3)*16+bzcol+3)*256;
        if(stack.size()>12&&//ÅÐ¶ÏÊÇ·ñ×ß³µéïê¤Æå
          mv==stack.get(stack.size()-4).mvResult&&
          stack.get(stack.size()-1).mvResult==stack.get(stack.size()-5).mvResult&&
          stack.get(stack.size()-5).mvResult==stack.get(stack.size()-9).mvResult&&
          stack.get(stack.size()-2).mvResult==stack.get(stack.size()-6).mvResult&&
          stack.get(stack.size()-6).mvResult==stack.get(stack.size()-10).mvResult&&
          stack.get(stack.size()-3).mvResult==stack.get(stack.size()-7).mvResult&&
          stack.get(stack.size()-3).mvResult==stack.get(stack.size()-11).mvResult&&
          stack.get(stack.size()-4).mvResult==stack.get(stack.size()-8).mvResult&&
          stack.get(stack.size()-8).mvResult==stack.get(stack.size()-12).mvResult){
          xzflag=false;
          flag=false;
          onDrawcanvas();//ÖØ»æ·½·¨
          Toast.makeText(//Í¬Ê±·¢Toast.ÌáÊ¾Íæ¼Ò
            father,
            "²»ÄÜÖØ¸´¾ÖÃæ×ß×Ó£¬ÇëÖØÐÂ×ß×Ó¡£",
            Toast.LENGTH_SHORT).show();
          return true;
        }
        if (LegalMove(mv)) {//Èç¹ûÏÂÆå·ûºÏ¹æÔò
          if (MakeMove(mv, 0)) {//Èç¹ûÃ»ÓÐ±»½«¾ü
            initArrays();//³õÊ¼»¯Êý×é
            father.playSound(2,1);//b²¥·ÅÉùÒôÍæ¼Ò×ßÆå
            huiqibushu=0;//»ÚÆå±êÖ¾ÖÃÁã
            onDrawcanvas();//ÖØ»æ·½·¨
            stack.push(new StackPlayChess(xzgz+((bzrow+3)*16+bzcol+3)*256,pcCaptured));//ÏÂÆå²½ÖèÈëÕ»
            if (IsMate()) {//Èç¹ûÍæ¼ÒÓ®ÁË
              LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
              initArrays();//³õÊ¼»¯Êý×é
              yingJMFlag=true;
              father.playSound(4,1);//b²¥·ÅÉùÒô,Ó®ÁË
              onDrawcanvas();//ÖØ»æ·½·¨
            } else {
//						          //µçÄÔ×ßÆå 
              new Thread(){//Æô¶¯Ò»¸öÏß³Ì½øÐÐµçÄÔÏÂÆå
                @Override
                public void run()
                {
                  endTime=zTime;//Ê±¼ä³õÊ¼»¯
                  playChessflag=true;//ÕýÔÚÏÂÆå
                  isPlayerPlaying =false;//ÕýÔÚÏÂÆå±êÖ¾
                  onDrawcanvas();//ÖØ»æ·½·¨
                  //µçÄÔ×ßÆå
                  SearchMain();
                  int sqDst = DST(mvResult);
                  int pcCaptured = ucpcSquares[sqDst];//µÃµ½Ä¿µÄ¸ñ×ÓµÄÆå×Ó
                  MakeMove(mvResult, 0);

                  stack.push(new StackPlayChess(mvResult,pcCaptured));//ÏÂÆå²½ÖèÈëÕ»
                  initArrays();//Êý×é²Ù×÷
                  if(LoadUtil.IsMate())//Èç¹ûµçÄÔÓ®ÁË£¬
                  {
                    LoadUtil.Startup();//³õÊ¼»¯ÆåÅÌ
                    initArrays();//³õÊ¼»¯Êý×é
                    shuJMFlag=true;
                    father.playSound(5,1);//b²¥·ÅÉùÒô,ÊäÁË
                  }else
                    father.playSound(2,1);//b²¥·ÅÉùÒô,µçÄÔÏÂÆåÁË
                  isPlayerPlaying =true;//ÏÂÍêÆå×Ó£¬Íæ¼Ò¿ÉÒÔ²Ù¿ØÁË¡£
                  playChessflag=false;
                  endTime=zTime;
                  onDrawcanvas();//ÖØ»æ·½·¨
                }
              }.start();

            }
          }
          xzflag=false;
          flag=false;
          onDrawcanvas();//ÖØ»æ·½·¨

        }
        else{
          xzflag=false;
          flag=false;
          onDrawcanvas();//ÖØ»æ·½·¨
        }


      }

      return true;
    }

    return super.onTouchEvent(e);
  }
  public void onDrawcanvas()
  {
    try {
      // Ëø¶¨Õû¸ö»­²¼£¬ÔÚÄÚ´æÒªÇó±È½Ï¸ßµÄÇé¿öÏÂ£¬½¨Òé²ÎÊý²»ÒªÎªnull
      canvas = holder.lockCanvas(null);
      synchronized (holder) {
        onDraw(canvas);//»æÖÆ
      }
    } finally {
      if (canvas != null) {
        //²¢ÊÍ·ÅËø
        holder.unlockCanvasAndPost(canvas);
      }
    }
  }
}
