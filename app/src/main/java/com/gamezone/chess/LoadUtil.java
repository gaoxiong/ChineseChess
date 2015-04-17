package com.gamezone.chess;

import java.util.Arrays;

import static com.gamezone.chess.ChessLoadUtil.ADVISOR_SPAN;
import static com.gamezone.chess.ChessLoadUtil.AWAY_HALF;
import static com.gamezone.chess.ChessLoadUtil.BISHOP_PIN;
import static com.gamezone.chess.ChessLoadUtil.BISHOP_SPAN;
import static com.gamezone.chess.ChessLoadUtil.DST;
import static com.gamezone.chess.ChessLoadUtil.HOME_HALF;
import static com.gamezone.chess.ChessLoadUtil.IN_BOARD;
import static com.gamezone.chess.ChessLoadUtil.IN_FORT;
import static com.gamezone.chess.ChessLoadUtil.KING_SPAN;
import static com.gamezone.chess.ChessLoadUtil.KNIGHT_PIN;
import static com.gamezone.chess.ChessLoadUtil.MOVE;
import static com.gamezone.chess.ChessLoadUtil.OPP_SIDE_TAG;
import static com.gamezone.chess.ChessLoadUtil.SAME_FILE;
import static com.gamezone.chess.ChessLoadUtil.SAME_HALF;
import static com.gamezone.chess.ChessLoadUtil.SAME_RANK;
import static com.gamezone.chess.ChessLoadUtil.SIDE_TAG;
import static com.gamezone.chess.ChessLoadUtil.SQUARE_FLIP;
import static com.gamezone.chess.ChessLoadUtil.SQUARE_FORWARD;
import static com.gamezone.chess.ChessLoadUtil.SRC;
import static com.gamezone.chess.Consts.ADVANCED_VALUE;
import static com.gamezone.chess.Consts.LIMIT_DEPTH;
import static com.gamezone.chess.Consts.MATE_VALUE;
import static com.gamezone.chess.Consts.MAX_GEN_MOVES;
import static com.gamezone.chess.Consts.PIECE_ADVISOR;
import static com.gamezone.chess.Consts.PIECE_BISHOP;
import static com.gamezone.chess.Consts.PIECE_CANNON;
import static com.gamezone.chess.Consts.PIECE_KING;
import static com.gamezone.chess.Consts.PIECE_KNIGHT;
import static com.gamezone.chess.Consts.PIECE_PAWN;
import static com.gamezone.chess.Consts.PIECE_ROOK;
import static com.gamezone.chess.Consts.WIN_VALUE;
import static com.gamezone.chess.Consts.ccAdvisorDelta;
import static com.gamezone.chess.Consts.ccKingDelta;
import static com.gamezone.chess.Consts.ccKnightCheckDelta;
import static com.gamezone.chess.Consts.ccKnightDelta;
import static com.gamezone.chess.Consts.cucpcStartup;
import static com.gamezone.chess.Consts.cucvlPiecePos;

public class LoadUtil {
  public static int sdPlayer=1;
  public static int ucpcSquares[]=new int[256];
  public static int vlWhite, vlBlack;
  public static int nDistance;
  public static int mvResult;
  public static int nHistoryTable[]=new int[65536];

  public static void initHistorytable()
  {
    for(int i=0;i<nHistoryTable.length;i++)
    {
      nHistoryTable[i]=0;
    }
  }
  public static void ChangeSide() {         // ½»»»×ß×Ó·½
    sdPlayer = 1 - sdPlayer;
  }
  public static void AddPiece(int sq, int pc) { // ÔÚÆåÅÌÉÏ·ÅÒ»Ã¶Æå×Ó
    ucpcSquares[sq] = pc;
    // ºì·½¼Ó·Ö£¬ºÚ·½(×¢Òâ"cucvlPiecePos"È¡ÖµÒªµßµ¹)¼õ·Ö
    if (pc < 16) {
      vlWhite += cucvlPiecePos[pc - 8][sq];
    } else {
      vlBlack += cucvlPiecePos[pc - 16][SQUARE_FLIP(sq)];
    }
  }
  public static void DelPiece(int sq, int pc) { // ´ÓÆåÅÌÉÏÄÃ×ßÒ»Ã¶Æå×Ó
    ucpcSquares[sq] = 0;
    // ºì·½¼õ·Ö£¬ºÚ·½(×¢Òâ"cucvlPiecePos"È¡ÖµÒªµßµ¹)¼Ó·Ö
    if (pc < 16) {
      vlWhite -= cucvlPiecePos[pc - 8][sq];
    } else {
      vlBlack -= cucvlPiecePos[pc - 16][SQUARE_FLIP(sq)];
    }
  }
  public static int Evaluate()  {      // ¾ÖÃæÆÀ¼Ûº¯Êý
    return (sdPlayer == 0 ? vlWhite - vlBlack : vlBlack - vlWhite) + ADVANCED_VALUE;
  }
  public static void UndoMakeMove(int mv, int pcCaptured) { // ³·Ïû×ßÒ»²½Æå
    nDistance --;
    ChangeSide();
    UndoMovePiece(mv, pcCaptured);
  }

  // ³õÊ¼»¯ÆåÅÌ
  public static void Startup() {
    int sq, pc;
    sdPlayer = vlWhite = vlBlack = nDistance = 0;
    for(int i=0;i<256;i++)
    {
      ucpcSquares[i]=0;
    }
    for (sq = 0; sq < 256; sq ++) {
      pc = cucpcStartup[sq];
      if (pc != 0) {
        AddPiece(sq, pc);

      }
    }
  }

  // °áÒ»²½ÆåµÄÆå×Ó
  public static int MovePiece(int mv) {
    int sqSrc, sqDst, pc, pcCaptured;
    sqSrc = SRC(mv);
    sqDst = DST(mv);
    pcCaptured = ucpcSquares[sqDst];
    if (pcCaptured != 0) {
      DelPiece(sqDst, pcCaptured);
    }
    pc = ucpcSquares[sqSrc];
    DelPiece(sqSrc, pc);
    AddPiece(sqDst, pc);
    return pcCaptured;
  }

  // ³·Ïû°áÒ»²½ÆåµÄÆå×Ó
  public static void UndoMovePiece(int mv, int pcCaptured) {
    int sqSrc, sqDst, pc;
    sqSrc = SRC(mv);
    sqDst = DST(mv);
    pc = ucpcSquares[sqDst];
    DelPiece(sqDst, pc);
    AddPiece(sqSrc, pc);
    if (pcCaptured != 0) {
      AddPiece(sqDst, pcCaptured);
    }}
  // ×ßÒ»²½Æå
  public static boolean MakeMove(int mv, int pcCaptured) {
    pcCaptured = MovePiece(mv);
    if (Checked()) {
      UndoMovePiece(mv, pcCaptured);
      return false;
    }
    ChangeSide();
    nDistance ++;
    return true;
  }

  // Éú³ÉËùÓÐ×ß·¨
  public static int GenerateMoves(Integer[] mvs)  {
    int i, j, nGenMoves, nDelta, sqSrc, sqDst;
    int pcSelfSide, pcOppSide, pcSrc, pcDst;
    // Éú³ÉËùÓÐ×ß·¨£¬ÐèÒª¾­¹ýÒÔÏÂ¼¸¸ö²½Öè£º

    nGenMoves = 0;
    pcSelfSide = SIDE_TAG(sdPlayer);
    pcOppSide = OPP_SIDE_TAG(sdPlayer);
    for (sqSrc = 0; sqSrc < 256; sqSrc ++) {

      // 1. ÕÒµ½Ò»¸ö±¾·½Æå×Ó£¬ÔÙ×öÒÔÏÂÅÐ¶Ï£º
      pcSrc = ucpcSquares[sqSrc];
      if ((pcSrc & pcSelfSide) == 0) {
        continue;
      }

      // 2. ¸ù¾ÝÆå×ÓÈ·¶¨×ß·¨
      switch (pcSrc - pcSelfSide) {
        case PIECE_KING:
          for (i = 0; i < 4; i ++) {
            sqDst = sqSrc + ccKingDelta[i];
            if (!IN_FORT(sqDst)) {
              continue;
            }
            pcDst = ucpcSquares[sqDst];
            if ((pcDst & pcSelfSide) == 0) {
              mvs[nGenMoves] = MOVE(sqSrc, sqDst);
              nGenMoves ++;
            }
          }
          break;
        case PIECE_ADVISOR:
          for (i = 0; i < 4; i ++) {
            sqDst = sqSrc + ccAdvisorDelta[i];
            if (!IN_FORT(sqDst)) {
              continue;
            }
            pcDst = ucpcSquares[sqDst];
            if ((pcDst & pcSelfSide) == 0) {
              mvs[nGenMoves] = MOVE(sqSrc, sqDst);
              nGenMoves ++;
            }
          }
          break;
        case PIECE_BISHOP:
          for (i = 0; i < 4; i ++) {
            sqDst = sqSrc + ccAdvisorDelta[i];
            if (!(IN_BOARD(sqDst) && HOME_HALF(sqDst, sdPlayer) && ucpcSquares[sqDst] == 0)) {
              continue;
            }
            sqDst += ccAdvisorDelta[i];
            pcDst = ucpcSquares[sqDst];
            if ((pcDst & pcSelfSide) == 0) {
              mvs[nGenMoves] = MOVE(sqSrc, sqDst);
              nGenMoves ++;
            }
          }
          break;
        case PIECE_KNIGHT:
          for (i = 0; i < 4; i ++) {
            sqDst = sqSrc + ccKingDelta[i];
            if (ucpcSquares[sqDst] != 0) {
              continue;
            }
            for (j = 0; j < 2; j ++) {
              sqDst = sqSrc + ccKnightDelta[i][j];
              if (!IN_BOARD(sqDst)) {
                continue;
              }
              pcDst = ucpcSquares[sqDst];
              if ((pcDst & pcSelfSide) == 0) {
                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                nGenMoves ++;
              }
            }
          }
          break;
        case PIECE_ROOK:
          for (i = 0; i < 4; i ++) {
            nDelta = ccKingDelta[i];
            sqDst = sqSrc + nDelta;
            while (IN_BOARD(sqDst)) {
              pcDst = ucpcSquares[sqDst];
              if (pcDst == 0) {
                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                nGenMoves ++;
              } else {
                if ((pcDst & pcOppSide) != 0) {
                  mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                  nGenMoves ++;
                }
                break;
              }
              sqDst += nDelta;
            }
          }
          break;
        case PIECE_CANNON:
          for (i = 0; i < 4; i ++) {
            nDelta = ccKingDelta[i];
            sqDst = sqSrc + nDelta;
            while (IN_BOARD(sqDst)) {
              pcDst = ucpcSquares[sqDst];
              if (pcDst == 0) {
                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                nGenMoves ++;
              } else {
                break;
              }
              sqDst += nDelta;
            }
            sqDst += nDelta;
            while (IN_BOARD(sqDst)) {
              pcDst = ucpcSquares[sqDst];
              if (pcDst != 0) {
                if ((pcDst & pcOppSide) != 0) {
                  mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                  nGenMoves ++;
                }
                break;
              }
              sqDst += nDelta;
            }
          }
          break;
        case PIECE_PAWN:
          sqDst = SQUARE_FORWARD(sqSrc, sdPlayer);
          if (IN_BOARD(sqDst)) {// ÅÐ¶ÏÆå×ÓÊÇ·ñÔÚÆåÅÌÖÐ
            pcDst = ucpcSquares[sqDst];
            if ((pcDst & pcSelfSide) == 0) {
              mvs[nGenMoves] = MOVE(sqSrc, sqDst);
              nGenMoves ++;
            }
          }
          if (AWAY_HALF(sqSrc, sdPlayer)) {
            for (nDelta = -1; nDelta <= 1; nDelta += 2) {
              sqDst = sqSrc + nDelta;
              if (IN_BOARD(sqDst)) {
                pcDst = ucpcSquares[sqDst];
                if ((pcDst & pcSelfSide) == 0) {
                  mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                  nGenMoves ++;
                }
              }
            }
          }
          break;
      }
    }
    return nGenMoves;
  }

  // ÅÐ¶Ï×ß·¨ÊÇ·ñºÏÀí
  public static boolean LegalMove(int mv)  {
    int sqSrc, sqDst, sqPin;
    int pcSelfSide, pcSrc, pcDst, nDelta;
    // ÅÐ¶Ï×ß·¨ÊÇ·ñºÏ·¨£¬ÐèÒª¾­¹ýÒÔÏÂµÄÅÐ¶Ï¹ý³Ì£º

    // 1. ÅÐ¶ÏÆðÊ¼¸ñÊÇ·ñÓÐ×Ô¼ºµÄÆå×Ó
    sqSrc = SRC(mv);
    pcSrc = ucpcSquares[sqSrc];
    pcSelfSide = SIDE_TAG(sdPlayer);
    if ((pcSrc & pcSelfSide) == 0) {
      return false;
    }

    // 2. ÅÐ¶ÏÄ¿±ê¸ñÊÇ·ñÓÐ×Ô¼ºµÄÆå×Ó
    sqDst = DST(mv);
    pcDst = ucpcSquares[sqDst];
    if ((pcDst & pcSelfSide) != 0) {
      return false;
    }

    // 3. ¸ù¾ÝÆå×ÓµÄÀàÐÍ¼ì²é×ß·¨ÊÇ·ñºÏÀí
    switch (pcSrc - pcSelfSide) {
      case PIECE_KING:
        return IN_FORT(sqDst) && KING_SPAN(sqSrc, sqDst);
      case PIECE_ADVISOR:
        return IN_FORT(sqDst) && ADVISOR_SPAN(sqSrc, sqDst);
      case PIECE_BISHOP:
        return SAME_HALF(sqSrc, sqDst) && BISHOP_SPAN(sqSrc, sqDst) &&
          ucpcSquares[BISHOP_PIN(sqSrc, sqDst)] == 0;
      case PIECE_KNIGHT:
        sqPin = KNIGHT_PIN(sqSrc, sqDst);
        return sqPin != sqSrc && ucpcSquares[sqPin] == 0;
      case PIECE_ROOK:
      case PIECE_CANNON:
        if (SAME_RANK(sqSrc, sqDst)) {
          nDelta = (sqDst < sqSrc ? -1 : 1);
        } else if (SAME_FILE(sqSrc, sqDst)) {
          nDelta = (sqDst < sqSrc ? -16 : 16);
        } else {
          return false;
        }
        sqPin = sqSrc + nDelta;
        while (sqPin != sqDst && ucpcSquares[sqPin] == 0) {
          sqPin += nDelta;
        }
        if (sqPin == sqDst) {
          return pcDst == 0 || pcSrc - pcSelfSide == PIECE_ROOK;
        } else if (pcDst != 0 && pcSrc - pcSelfSide == PIECE_CANNON) {
          sqPin += nDelta;
          while (sqPin != sqDst && ucpcSquares[sqPin] == 0) {
            sqPin += nDelta;
          }
          return sqPin == sqDst;
        } else {
          return false;
        }
      case PIECE_PAWN:
        if (AWAY_HALF(sqDst, sdPlayer) && (sqDst == sqSrc - 1 || sqDst == sqSrc + 1)) {
          return true;
        }
        return sqDst == SQUARE_FORWARD(sqSrc, sdPlayer);
      default:
        return false;
    }
  }

  // ÅÐ¶ÏÊÇ·ñ±»½«¾ü
  public static boolean Checked()  {
    int i, j, sqSrc, sqDst;
    int pcSelfSide, pcOppSide, pcDst, nDelta;
    pcSelfSide = SIDE_TAG(sdPlayer);
    pcOppSide = OPP_SIDE_TAG(sdPlayer);
    // ÕÒµ½ÆåÅÌÉÏµÄË§(½«)£¬ÔÙ×öÒÔÏÂÅÐ¶Ï£º

    for (sqSrc = 0; sqSrc < 256; sqSrc ++) {
      if (ucpcSquares[sqSrc] != pcSelfSide + PIECE_KING) {
        continue;
      }

      // 1. ÅÐ¶ÏÊÇ·ñ±»¶Ô·½µÄ±ø(×ä)½«¾ü
      if (ucpcSquares[SQUARE_FORWARD(sqSrc, sdPlayer)] == pcOppSide + PIECE_PAWN) {
        return true;
      }
      for (nDelta = -1; nDelta <= 1; nDelta += 2) {
        if (ucpcSquares[sqSrc + nDelta] == pcOppSide + PIECE_PAWN) {
          return true;
        }
      }

      // 2. ÅÐ¶ÏÊÇ·ñ±»¶Ô·½µÄÂí½«¾ü(ÒÔÊË(Ê¿)µÄ²½³¤µ±×÷ÂíÍÈ)
      for (i = 0; i < 4; i ++) {
        if (ucpcSquares[sqSrc + ccAdvisorDelta[i]] != 0) {
          continue;
        }
        for (j = 0; j < 2; j ++) {
          int pcDstt = ucpcSquares[sqSrc + ccKnightCheckDelta[i][j]];
          if (pcDstt == pcOppSide + PIECE_KNIGHT) {
            return true;
          }
        }
      }

      // 3. ÅÐ¶ÏÊÇ·ñ±»¶Ô·½µÄ³µ»òÅÚ½«¾ü(°üÀ¨½«Ë§¶ÔÁ³)
      for (i = 0; i < 4; i ++) {
        nDelta = ccKingDelta[i];
        sqDst = sqSrc + nDelta;
        while (IN_BOARD(sqDst)) {
          pcDst = ucpcSquares[sqDst];
          if (pcDst != 0) {
            if (pcDst == pcOppSide + PIECE_ROOK || pcDst == pcOppSide + PIECE_KING) {
              return true;
            }
            break;
          }
          sqDst += nDelta;
        }
        sqDst += nDelta;
        while (IN_BOARD(sqDst)) {
          pcDst = ucpcSquares[sqDst];
          if (pcDst != 0) {
            if (pcDst == pcOppSide + PIECE_CANNON) {
              return true;
            }
            break;
          }
          sqDst += nDelta;
        }
      }
      return false;
    }
    return false;
  }

  // ÅÐ¶ÏÊÇ·ñ±»É±
  public static boolean IsMate() {
    int i, nGenMoveNum, pcCaptured ;
    Integer mvs[]=new Integer[MAX_GEN_MOVES];

    nGenMoveNum = GenerateMoves(mvs);
    for (i = 0; i < nGenMoveNum; i ++) {
      pcCaptured = MovePiece(mvs[i]);
      if (!Checked()) {
        UndoMovePiece(mvs[i], pcCaptured);
        return false;
      } else {
        UndoMovePiece(mvs[i], pcCaptured);
      }
    }
    return true;
  }


  // ³¬³ö±ß½ç(Fail-Soft)µÄAlpha-BetaËÑË÷¹ý³Ì
  public static int SearchFull(int vlAlpha, int vlBeta, int nDepth) {
    int i=0, nGenMoves=0,pcCaptured=0;
    int vl, vlBest, mvBest;
    Integer mvs[]=new Integer[MAX_GEN_MOVES];
    // Ò»¸öAlpha-BetaÍêÈ«ËÑË÷·ÖÎªÒÔÏÂ¼¸¸ö½×¶Î

    // 1. µ½´ïË®Æ½Ïß£¬Ôò·µ»Ø¾ÖÃæÆÀ¼ÛÖµ
    if (nDepth == 0) {
      return Evaluate();
    }

    // 2. ³õÊ¼»¯×î¼ÑÖµºÍ×î¼Ñ×ß·¨
    vlBest = -MATE_VALUE;
    mvBest = 0;

    // 3. Éú³ÉÈ«²¿×ß·¨£¬²¢¸ù¾ÝÀúÊ·±íÅÅÐò
    nGenMoves = GenerateMoves(mvs);
    Arrays.sort(mvs, 0,nGenMoves,new MyComparator());

    // 4. ÖðÒ»×ßÕâÐ©×ß·¨£¬²¢½øÐÐµÝ¹é
    for (i = 0; i < nGenMoves; i ++) {
      pcCaptured=ucpcSquares[DST( mvs[i])];
      if (MakeMove(mvs[i], pcCaptured)) {
        vl = -SearchFull(-vlBeta, -vlAlpha, nDepth - 1);
        UndoMakeMove(mvs[i], pcCaptured);

        // 5. ½øÐÐAlpha-Beta´óÐ¡ÅÐ¶ÏºÍ½Ø¶Ï
        if (vl > vlBest) {    // ÕÒµ½×î¼ÑÖµ(µ«²»ÄÜÈ·¶¨ÊÇAlpha¡¢PV»¹ÊÇBeta×ß·¨)
          vlBest = vl;
          if (vl >= vlBeta) { // ÕÒµ½Ò»¸öBeta×ß·¨
            mvBest = mvs[i];
            break;
          }
          if (vl > vlAlpha) { // ÕÒµ½Ò»¸öPV×ß·¨
            mvBest = mvs[i];
            vlAlpha = vl;
          }
        }
      }
    }

    // 5. ËùÓÐ×ß·¨¶¼ËÑË÷ÍêÁË£¬°Ñ×î¼Ñ×ß·¨(²»ÄÜÊÇAlpha×ß·¨)±£´æµ½ÀúÊ·±í£¬·µ»Ø×î¼ÑÖµ
    if (vlBest == -MATE_VALUE) {
      // Èç¹ûÊÇÉ±Æå£¬¾Í¸ù¾ÝÉ±Æå²½Êý¸ø³öÆÀ¼Û
      return nDistance - MATE_VALUE;
    }
    if (mvBest != 0) {
      // Èç¹û²»ÊÇAlpha×ß·¨£¬¾Í½«×î¼Ñ×ß·¨±£´æµ½ÀúÊ·±í
      nHistoryTable[mvBest] += nDepth * nDepth;
      if (nDistance == 0) {
        // ËÑË÷¸ù½ÚµãÊ±£¬×ÜÊÇÓÐÒ»¸ö×î¼Ñ×ß·¨(ÒòÎªÈ«´°¿ÚËÑË÷²»»á³¬³ö±ß½ç)£¬½«Õâ¸ö×ß·¨±£´æÏÂÀ´
        mvResult = mvBest;
      }
    }
    return vlBest;
  }

  // µü´ú¼ÓÉîËÑË÷¹ý³Ì
  public static void SearchMain() {
    int i, vl;

    // ³õÊ¼»¯
    initHistorytable();
    nDistance = 0;
    long start=System.nanoTime();
    // µü´ú¼ÓÉî¹ý³Ì
    for (i = 1; i <= LIMIT_DEPTH; i ++) {
      vl = SearchFull(-MATE_VALUE, MATE_VALUE, i);
      // ËÑË÷µ½É±Æå£¬¾ÍÖÕÖ¹ËÑË÷
      if (vl > WIN_VALUE || vl < -WIN_VALUE) {
        break;
      }
      // ³¬¹ýÊ±¼ä£¬¾ÍÖÕÖ¹ËÑË÷
      if((System.nanoTime()-start)/1.e9>ViewConsts.thinkDeeplyTime)
        break;
    }
  }
}

