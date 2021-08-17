import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.Image;
/*
 * Created on 2006/04/23
 */

public class Block {
    // ブロックのサイズ
    public static final int ROW = 3;
    public static final int COL = 3;

    // 1マスのサイズ
    public static final int TILE_SIZE = Field.TILE_SIZE;

    // 移動方向
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final int DOWN = 2;
    public static final int FALL = 3;

    public int blockFolm; // 縦のとき0 横のとき1
    // ブロックの形を格納
    private int[][] block = new int[ROW][COL];
    private static Random random;
    // 位置（単位：マス）
    private Point pos;
    // フィールドへの参照
    private Field field;

    private Image blockImage;

    public Block(Field field) {
        this.field = field;
        init();
        // 四角いブロックを作成
        // □■□
        // □■□
        // □□□
        this.block[0][1] = RandomInt.RandomInt();
        this.block[1][1] = RandomInt.RandomInt();
        // ブロックの形は縦
        this.blockFolm = 0;
        // 初期位置をセット
        posSet();
    }

    /**
     * ブロックの初期化
     */
    public void init() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                block[i][j] = 0;
            }
        }
    }

    /**
     * ブロックの描画
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (block[i][j] != 0) {
                    loadImage("image/" + block[i][j] + ".png");
                    // posの位置を基準とする点に注意！
                    g.drawImage(blockImage, (pos.x + j) * TILE_SIZE,
                            (pos.y + i-1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    /**
     * dirの方向にブロックを移動
     * 
     * @param dir 方向
     * @return フィールドに固定されたらtrueを返す
     */
    public boolean move(int dir) {
        switch (dir) {
            case LEFT :
                Point newPos = new Point(pos.x - 1, pos.y);
                if (field.isMovable(newPos, block)) {  // 衝突しなければ位置を更新
                    pos = newPos;
                }
                break;
            case RIGHT :
                newPos = new Point(pos.x + 1, pos.y);
                if (field.isMovable(newPos, block)) {
                    pos = newPos;
                }
                break;
            case DOWN :
                newPos = new Point(pos.x, pos.y + 1);
                if (field.isMovable(newPos, block)) {
                    pos = newPos;
                } else {  // 移動できない＝他のブロックとぶつかる＝固定する
                    // ブロックをフィールドに固定する
                    field.fixBlock(pos, block);
                    // メインパネルのキー入力をロックする
                    MainPanel.keyLock();
                    // 固定されたらtrueを返す
                    return true;
                }
                break;
            case FALL :
                newPos = new Point(pos.x, pos.y + 1);
                // ブロックを下まで落とす
                while ( field.isMovable(newPos, block) ) {
                    pos = newPos;
                    newPos = new Point(pos.x, pos.y+1);
                }
                // ブロックをフィールドに固定する
                field.fixBlock(pos, block);
                // メインパネルのキー入力をロックする
                MainPanel.keyLock();
                // 固定されたらtrueを返す
                return true;
        }        
        return false;
    }


    /**
     * ブロックを回転させる
     */
    public void turn(int dir) {
        // 回転したブロック
        int[][] turnedBlock = new int[ROW][COL];
        if ( dir == RIGHT ) { // 右回転
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    turnedBlock[j][ROW - 1 - i] = block[i][j];
                }
            }
        } else {  // 左回転
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    turnedBlock[ROW - 1 - j][i] = block[i][j];
                }
            }            
        }

        // 回転可能か調べる
        if (field.isMovable(pos, turnedBlock)) {
            block = turnedBlock;
            if ( blockFolm == 1 ) {
                blockFolm = 0;
            } else {
                blockFolm = 1;
            }
        } else {
            if ( blockFolm == 0 ) {
                Point newPos = new Point(pos.x - 1, pos.y);
                if ( field.isMovable(newPos, block) ) {
                    pos = newPos;
                    block = turnedBlock;
                    blockFolm = 1;
                    return;
                }

                newPos = new Point(pos.x + 1, pos.y);
                if ( field.isMovable(newPos, block) ) {
                    pos = newPos;
                    block = turnedBlock;
                    blockFolm = 1;
                    return;
                }
                turnedBlock = new int[ROW][COL];
                for ( int i = 0; i < ROW; i++ ) {
                    for ( int j = 0; j < COL; j++ ) {
                        turnedBlock[j][i] = block[COL-j-1][i];
                    }
                }
                if (field.isMovable(pos, turnedBlock)) {
                    block = turnedBlock;
                }
            }
        }
    }

    public void loadImage(String filename) {
        // ブロックのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }

    public void posSet() {
        pos = new Point(2, 0);
    }

    public int[][] getBlock(){
        return this.block;
    }
}
