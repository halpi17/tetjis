import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;
/*
 * Created on 2006/04/23
 */

public class Field {
    // フィールドのサイズ（単位：マス）
    public static final int COL = 8;
    public static final int ROW = 14;
    ArrayList<Integer> list1 = new ArrayList<Integer>();
    ArrayList<VanishData> vanishList = new ArrayList<VanishData>();
    // マスのサイズ
    public static final int TILE_SIZE = 32;
    public static final int X = 1;
    public static final int Y = 0;
    // フィールド
    private int[][] field;
    private int[][] weight;
    private Point pos;
    private Image blockImage;
    private VanishData vanish;
    public Field() {
        field = new int[ROW][COL];
        weight = new int[ROW][COL];
        // フィールドを初期化
        init(field);
    }

    /**
     * フィールドを初期化する
     */
    public void init(int[][] field) {
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                // 壁をつくる
                if (x == 0 || x == COL - 1) {
                    field[y][x] = 1;
                } else if (y == ROW - 1) {
                    field[y][x] = 1;
                } else {
                    field[y][x] = 0;
                }
            }
        }
    }

    /**
     * フィールドを描画
     */

    public void imageDraw(int x, int y, Graphics g){
        loadImage("image/" + field[x][y] + ".png");
        // posの位置を基準とする点に注意！
        g.drawImage(blockImage, y * TILE_SIZE,
                    (x-1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);  
    }

    public void drawVanishArea(Graphics g){
        g.setColor(Color.MAGENTA);
        if ( vanish.dir == X ) { // 横方向に文字が消えるとき
            for ( int i = 0; i < vanish.len; i++ ) {
                loadImage("image/color/" + field[vanish.pos.y][vanish.pos.x+i] + ".png");
                g.drawImage(blockImage, (vanish.pos.x + i)* TILE_SIZE, (vanish.pos.y-1) * TILE_SIZE,
                                   TILE_SIZE, TILE_SIZE, null);
            }
        } else {
            for ( int i = 0; i < vanish.len; i++ ) { //縦方向に文字が消えるとき
                loadImage("image/color/" + field[vanish.pos.y+i][vanish.pos.x] + ".png");
                g.drawImage(blockImage, vanish.pos.x* TILE_SIZE, (vanish.pos.y+i-1) * TILE_SIZE, TILE_SIZE,
                           TILE_SIZE, null);
            }
        }        
    }
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);
        loadImage("image/batsu.png");
        g.drawImage(blockImage, 3 * TILE_SIZE,
                    0, TILE_SIZE, TILE_SIZE, null);
        for (int y = 1; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                if (field[y][x] != 0) {
                    loadImage("image/" + field[y][x] + ".png");
                    g.drawImage(blockImage, x * TILE_SIZE,
                                (y-1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);  
                }
            }
        }

                // 消える熟語がある場合
        if ( vanish != null ) {
            // 削除位置を示す
            drawVanishArea(g);
        }
    }

    /**
     * ブロックを移動できるか調べる
     * 
     * @param newPos ブロックの移動先座標
     * @param block ブロック
     * @return 移動できたらtrue
     */
    public boolean isMovable(Point newPos, int[][] block) {
        // block=1のマスすべてについて衝突しているか調べる
        // どれか1マスでも衝突してたら移動できない
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (block[i][j] != 0) { // 3x3内でブロックのあるマスのみ調べる
                    if (newPos.y + i < 0) { // そのマスが画面の上端外のとき
                        // ブロックのあるマスが壁のある0列目以下または
                        // COL-1列目以上に移動しようとしている場合は移動できない
                        if (newPos.x + j <= 0 || newPos.x + j >= COL - 1) {
                            return false;
                        }
                    } else if (field[newPos.y + i][newPos.x + j] != 0) { // フィールド内で
                        // 移動先にすでにブロック（壁含む）がある場合は移動できない
                        return false;
                    }
                }
            }
        }

        return true;
    }


    public boolean isStacked(){
        if ( field[1][3] == 0 ) {
            return false;
        }
        return true;
    }
    /**
     * 落ちきったブロックをボードに固定する
     * 
     * @param pos ブロックの位置
     * @param block ブロック
     */
    public void fixBlock(Point pos, int[][] block) {
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (block[i][j] != 0) {
                    if (pos.y + i < 0) continue;
                    field[pos.y + i][pos.x + j] = block[i][j];  // フィールドをブロックで埋める
                }
            }
        }
    }

    // ブロックが固定されたあとの自由落下
    public boolean fallBlock() {
        int x, y, iy;
        // まだ落とせるか
        boolean flag = false;
        for ( x = 1; x < COL-1; x++ ) {
            for ( y = ROW-2; y >= 0; y-- ) {
                if ( field[y][x] == 0 ) {
                    // 空の場所の読み飛ばし
                    for ( iy = y-1; iy>=0 && field[iy][x] == 0; iy-- );
                    if ( iy < 0 ) { break; }
                    // まだ落とせる！！
                    flag = true; 
                    for ( iy = y; iy >= 0; iy-- ) {
                        // 下に空白がある文字を1マスずつ落とす
                        if ( iy-1 >= 0 ) {
                            field[iy][x] = field[iy-1][x];
                        } else {
                            field[iy][x] = 0;
                        }
                    }
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 削除データを追加
     * 
     * @param x 削除位置のx座標
     * @param y 削除位置のy座標
     * @param dir 削除方向
     */
    public boolean addVanishList(int x, int y, int dir) {
        // list1を連結した文字が削除できる場合
        if ( VanishData.canVanish(list1) ) {
            pos = new Point(x, y);
            // 削除する熟語のリストに追加
            vanishList.add(new VanishData(pos, list1.size(), dir));
            return true;
        }
        return false;         
    }

    // 同時消しのための重み計算
    public boolean vanishWeight() {
        int x, y, iy, ix, i;
        boolean flag = false;
        // 削除データの初期化
        vanishList.clear();
        // 重みの初期化
        init(weight);
        for ( x = 1; x < COL-1; x++ ) {
            // 一番上(y=0)は消さない！
            for ( y = 1; y < ROW-1; y++ ) {
                if ( field[y][x] != 0 ) {
                    // 熟語の初期化
                    list1.clear();
                    // field[y][x]の文字(1文字目)をlist1に追加
                    list1.add(field[y][x]);
                    //  横方向の検査
                    for ( ix = x+1; ix < COL-1; ix++ ) {
                        //  field[y][x]の位置より右の文字列を1文字ずつ追加
                        list1.add(field[y][ix]);
                        //  list1を連結した文字が削除リストに追加された場合
                        if ( addVanishList(x, y, X) ) {
                            flag = true;
                            // 消去する熟語の位置の重みをインクリメント
                            for ( i = 0; i < list1.size(); i++ ) {
                                weight[y][x+i] += 1;
                            }
                        }              
                    }
                    // 熟語の初期化
                    list1.clear();
                    // field[y][x]の文字(1文字目)をlist1に追加
                    list1.add(field[y][x]);
                    // 縦方向の検査
                    for ( iy = y+1; iy < ROW - 1; iy++ ) {
                        list1.add(field[iy][x]);
                        if ( addVanishList(x, y, Y) ) {
                            flag = true;
                            for ( i = 0; i < list1.size(); i++ ) {
                                weight[y+i][x] += 1;
                            }
                        }

                    }
                }
            }
        }
        return flag;
    }

    public int vanish() {
    	int num;
        if ( vanish.dir == X ) {
            for ( int i = 0; i < vanish.len; i++ ) {
                weight[vanish.pos.y][vanish.pos.x+i] -= 1;
                if ( weight[vanish.pos.y][vanish.pos.x+i] <= 0 ) {
                    field[vanish.pos.y][vanish.pos.x+i] = 0;
                }
            }
            num = vanish.len;
            vanish = null;
            return num;
        } else if (vanish.dir == Y) {
            for ( int i = 0; i < vanish.len; i++ ) {
                weight[vanish.pos.y+i][vanish.pos.x] -= 1;
                if ( weight[vanish.pos.y+i][vanish.pos.x] <= 0 ) {
                    field[vanish.pos.y+i][vanish.pos.x] = 0;
                }
            }
            num = vanish.len;
            vanish = null;
            return num;
        }
        return 0;
    }

    public int getVanishListSize() {
        return vanishList.size(); 
    }

    public void loadImage(String filename) {
        // ブロックのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }

    
    public void setVanish(int index) {
        vanish = vanishList.get(index);
    }
    
    public boolean allDelite() {
    	int x;
    	
    	for ( x = 1; x < COL-2; x++ ) {
    		if ( field[ROW-2][x] != 0 ) {
    			return false;
    		}
    	}
    	return true;
    }
}
