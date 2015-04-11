package com.gamezone.chess;

import static com.gamezone.chess.Consts.*;

public class ChessLoadUtil {

  // ÅÐ¶ÏÆå×ÓÊÇ·ñÔÚÆåÅÌÖÐ
  public static boolean IN_BOARD(int sq) {
    return ccInBoard[sq] != 0;
  }

  // ÅÐ¶ÏÆå×ÓÊÇ·ñÔÚ¾Å¹¬ÖÐ
  public static boolean IN_FORT(int sq) {
    return ccInFort[sq] != 0;
  }

  // »ñµÃ¸ñ×ÓµÄ×Ý×ø±êrow
  public static int RANK_Y(int sq) {
    return sq >> 4;
  }

  // »ñµÃ¸ñ×ÓµÄºá×ø±êcol
  public static int FILE_X(int sq) {
    return sq & 15;
  }

  // ¸ù¾Ý×Ý×ø±êºÍºá×ø±ê»ñµÃ¸ñ×Ó
  public static int COORD_XY(int x, int y) {
    return x + (y << 4);
  }

  // ·­×ª¸ñ×Ó
  public static int SQUARE_FLIP(int sq) {
    return 254 - sq;
  }

  // ×Ý×ø±êË®Æ½¾µÏñ
  public static int FILE_FLIP(int x) {
    return 14 - x;
  }

  // ºá×ø±ê´¹Ö±¾µÏñ
  public static int RANK_FLIP(int y) {
    return 15 - y;
  }

  // ¸ñ×ÓË®Æ½¾µÏñ
  public static int MIRROR_SQUARE(int sq) {
    return COORD_XY(FILE_FLIP(FILE_X(sq)), RANK_Y(sq));
  }

  // ¸ñ×ÓË®Æ½¾µÏñ
  public static int SQUARE_FORWARD(int sq, int sd) {
    return sq - 16 + (sd << 5);
  }

  // ×ß·¨ÊÇ·ñ·ûºÏË§(½«)µÄ²½³¤
  public static boolean KING_SPAN(int sqSrc, int sqDst) {
    return ccLegalSpan[sqDst - sqSrc + 256] == 1;
  }

  // ×ß·¨ÊÇ·ñ·ûºÏÊË(Ê¿)µÄ²½³¤
  public static boolean ADVISOR_SPAN(int sqSrc, int sqDst) {
    return ccLegalSpan[sqDst - sqSrc + 256] == 2;
  }

  // ×ß·¨ÊÇ·ñ·ûºÏÏà(Ïó)µÄ²½³¤
  public static boolean BISHOP_SPAN(int sqSrc, int sqDst) {
    return ccLegalSpan[sqDst - sqSrc + 256] == 3;
  }

  // Ïà(Ïó)ÑÛµÄÎ»ÖÃ
  public static int BISHOP_PIN(int sqSrc, int sqDst) {
    return (sqSrc + sqDst) >> 1;
  }

  // ÂíÍÈµÄÎ»ÖÃ
  public static int KNIGHT_PIN(int sqSrc, int sqDst) {
    return sqSrc + ccKnightPin[sqDst - sqSrc + 256];
  }

  // ÊÇ·ñÎ´¹ýºÓ
  public static boolean HOME_HALF(int sq, int sd) {
    return (sq & 0x80) != (sd << 7);
  }

  // ÊÇ·ñÒÑ¹ýºÓ
  public static boolean AWAY_HALF(int sq, int sd) {
    return (sq & 0x80) == (sd << 7);
  }

  // ÊÇ·ñÔÚºÓµÄÍ¬Ò»±ß
  public static boolean SAME_HALF(int sqSrc, int sqDst) {
    return ((sqSrc ^ sqDst) & 0x80) == 0;
  }

  // ÊÇ·ñÔÚÍ¬Ò»ÐÐ
  public static boolean SAME_RANK(int sqSrc, int sqDst) {
    return ((sqSrc ^ sqDst) & 0xf0) == 0;
  }

  // ÊÇ·ñÔÚÍ¬Ò»ÁÐ
  public static boolean SAME_FILE(int sqSrc, int sqDst) {
    return ((sqSrc ^ sqDst) & 0x0f) == 0;
  }

  // »ñµÃºìºÚ±ê¼Ç(ºì×ÓÊÇ8£¬ºÚ×ÓÊÇ16)
  public static int SIDE_TAG(int sd) {
    return 8 + (sd << 3);
  }

  // »ñµÃ¶Ô·½ºìºÚ±ê¼Ç
  public static int OPP_SIDE_TAG(int sd) {
    return 16 - (sd << 3);
  }

  // »ñµÃ×ß·¨µÄÆðµã
  public static int SRC(int mv) {
    return mv & 255;
  }

  // »ñµÃ×ß·¨µÄÖÕµã
  public static int DST(int mv) {
    return mv >> 8;
  }

  // ¸ù¾ÝÆðµãºÍÖÕµã»ñµÃ×ß·¨
  public static int MOVE(int sqSrc, int sqDst) {
    return sqSrc + sqDst * 256;
  }

  // ×ß·¨Ë®Æ½¾µÏñ
  public static int MIRROR_MOVE(int mv) {
    return MOVE(MIRROR_SQUARE(SRC(mv)), MIRROR_SQUARE(DST(mv)));
  }

}
