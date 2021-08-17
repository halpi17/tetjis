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
    // �t�B�[���h�̃T�C�Y�i�P�ʁF�}�X�j
    public static final int COL = 8;
    public static final int ROW = 14;
    ArrayList<Integer> list1 = new ArrayList<Integer>();
    ArrayList<VanishData> vanishList = new ArrayList<VanishData>();
    // �}�X�̃T�C�Y
    public static final int TILE_SIZE = 32;
    public static final int X = 1;
    public static final int Y = 0;
    // �t�B�[���h
    private int[][] field;
    private int[][] weight;
    private Point pos;
    private Image blockImage;
    private VanishData vanish;
    public Field() {
        field = new int[ROW][COL];
        weight = new int[ROW][COL];
        // �t�B�[���h��������
        init(field);
    }

    /**
     * �t�B�[���h������������
     */
    public void init(int[][] field) {
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                // �ǂ�����
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
     * �t�B�[���h��`��
     */

    public void imageDraw(int x, int y, Graphics g){
        loadImage("image/" + field[x][y] + ".png");
        // pos�̈ʒu����Ƃ���_�ɒ��ӁI
        g.drawImage(blockImage, y * TILE_SIZE,
                    (x-1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);  
    }

    public void drawVanishArea(Graphics g){
        g.setColor(Color.MAGENTA);
        if ( vanish.dir == X ) { // �������ɕ�����������Ƃ�
            for ( int i = 0; i < vanish.len; i++ ) {
                loadImage("image/color/" + field[vanish.pos.y][vanish.pos.x+i] + ".png");
                g.drawImage(blockImage, (vanish.pos.x + i)* TILE_SIZE, (vanish.pos.y-1) * TILE_SIZE,
                                   TILE_SIZE, TILE_SIZE, null);
            }
        } else {
            for ( int i = 0; i < vanish.len; i++ ) { //�c�����ɕ�����������Ƃ�
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

                // ������n�ꂪ����ꍇ
        if ( vanish != null ) {
            // �폜�ʒu������
            drawVanishArea(g);
        }
    }

    /**
     * �u���b�N���ړ��ł��邩���ׂ�
     * 
     * @param newPos �u���b�N�̈ړ�����W
     * @param block �u���b�N
     * @return �ړ��ł�����true
     */
    public boolean isMovable(Point newPos, int[][] block) {
        // block=1�̃}�X���ׂĂɂ��ďՓ˂��Ă��邩���ׂ�
        // �ǂꂩ1�}�X�ł��Փ˂��Ă���ړ��ł��Ȃ�
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (block[i][j] != 0) { // 3x3���Ńu���b�N�̂���}�X�̂ݒ��ׂ�
                    if (newPos.y + i < 0) { // ���̃}�X����ʂ̏�[�O�̂Ƃ�
                        // �u���b�N�̂���}�X���ǂ̂���0��ڈȉ��܂���
                        // COL-1��ڈȏ�Ɉړ����悤�Ƃ��Ă���ꍇ�͈ړ��ł��Ȃ�
                        if (newPos.x + j <= 0 || newPos.x + j >= COL - 1) {
                            return false;
                        }
                    } else if (field[newPos.y + i][newPos.x + j] != 0) { // �t�B�[���h����
                        // �ړ���ɂ��łɃu���b�N�i�Ǌ܂ށj������ꍇ�͈ړ��ł��Ȃ�
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
     * �����������u���b�N���{�[�h�ɌŒ肷��
     * 
     * @param pos �u���b�N�̈ʒu
     * @param block �u���b�N
     */
    public void fixBlock(Point pos, int[][] block) {
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (block[i][j] != 0) {
                    if (pos.y + i < 0) continue;
                    field[pos.y + i][pos.x + j] = block[i][j];  // �t�B�[���h���u���b�N�Ŗ��߂�
                }
            }
        }
    }

    // �u���b�N���Œ肳�ꂽ���Ƃ̎��R����
    public boolean fallBlock() {
        int x, y, iy;
        // �܂����Ƃ��邩
        boolean flag = false;
        for ( x = 1; x < COL-1; x++ ) {
            for ( y = ROW-2; y >= 0; y-- ) {
                if ( field[y][x] == 0 ) {
                    // ��̏ꏊ�̓ǂݔ�΂�
                    for ( iy = y-1; iy>=0 && field[iy][x] == 0; iy-- );
                    if ( iy < 0 ) { break; }
                    // �܂����Ƃ���I�I
                    flag = true; 
                    for ( iy = y; iy >= 0; iy-- ) {
                        // ���ɋ󔒂����镶����1�}�X�����Ƃ�
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
     * �폜�f�[�^��ǉ�
     * 
     * @param x �폜�ʒu��x���W
     * @param y �폜�ʒu��y���W
     * @param dir �폜����
     */
    public boolean addVanishList(int x, int y, int dir) {
        // list1��A�������������폜�ł���ꍇ
        if ( VanishData.canVanish(list1) ) {
            pos = new Point(x, y);
            // �폜����n��̃��X�g�ɒǉ�
            vanishList.add(new VanishData(pos, list1.size(), dir));
            return true;
        }
        return false;         
    }

    // ���������̂��߂̏d�݌v�Z
    public boolean vanishWeight() {
        int x, y, iy, ix, i;
        boolean flag = false;
        // �폜�f�[�^�̏�����
        vanishList.clear();
        // �d�݂̏�����
        init(weight);
        for ( x = 1; x < COL-1; x++ ) {
            // ��ԏ�(y=0)�͏����Ȃ��I
            for ( y = 1; y < ROW-1; y++ ) {
                if ( field[y][x] != 0 ) {
                    // �n��̏�����
                    list1.clear();
                    // field[y][x]�̕���(1������)��list1�ɒǉ�
                    list1.add(field[y][x]);
                    //  �������̌���
                    for ( ix = x+1; ix < COL-1; ix++ ) {
                        //  field[y][x]�̈ʒu���E�̕������1�������ǉ�
                        list1.add(field[y][ix]);
                        //  list1��A�������������폜���X�g�ɒǉ����ꂽ�ꍇ
                        if ( addVanishList(x, y, X) ) {
                            flag = true;
                            // ��������n��̈ʒu�̏d�݂��C���N�������g
                            for ( i = 0; i < list1.size(); i++ ) {
                                weight[y][x+i] += 1;
                            }
                        }              
                    }
                    // �n��̏�����
                    list1.clear();
                    // field[y][x]�̕���(1������)��list1�ɒǉ�
                    list1.add(field[y][x]);
                    // �c�����̌���
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
        // �u���b�N�̃C���[�W��ǂݍ���
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
