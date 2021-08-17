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
    // �u���b�N�̃T�C�Y
    public static final int ROW = 3;
    public static final int COL = 3;

    // 1�}�X�̃T�C�Y
    public static final int TILE_SIZE = Field.TILE_SIZE;

    // �ړ�����
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final int DOWN = 2;
    public static final int FALL = 3;

    public int blockFolm; // �c�̂Ƃ�0 ���̂Ƃ�1
    // �u���b�N�̌`���i�[
    private int[][] block = new int[ROW][COL];
    private static Random random;
    // �ʒu�i�P�ʁF�}�X�j
    private Point pos;
    // �t�B�[���h�ւ̎Q��
    private Field field;

    private Image blockImage;

    public Block(Field field) {
        this.field = field;
        init();
        // �l�p���u���b�N���쐬
        // ������
        // ������
        // ������
        this.block[0][1] = RandomInt.RandomInt();
        this.block[1][1] = RandomInt.RandomInt();
        // �u���b�N�̌`�͏c
        this.blockFolm = 0;
        // �����ʒu���Z�b�g
        posSet();
    }

    /**
     * �u���b�N�̏�����
     */
    public void init() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                block[i][j] = 0;
            }
        }
    }

    /**
     * �u���b�N�̕`��
     * 
     * @param g �`��I�u�W�F�N�g
     */
    public void draw(Graphics g) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (block[i][j] != 0) {
                    loadImage("image/" + block[i][j] + ".png");
                    // pos�̈ʒu����Ƃ���_�ɒ��ӁI
                    g.drawImage(blockImage, (pos.x + j) * TILE_SIZE,
                            (pos.y + i-1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    /**
     * dir�̕����Ƀu���b�N���ړ�
     * 
     * @param dir ����
     * @return �t�B�[���h�ɌŒ肳�ꂽ��true��Ԃ�
     */
    public boolean move(int dir) {
        switch (dir) {
            case LEFT :
                Point newPos = new Point(pos.x - 1, pos.y);
                if (field.isMovable(newPos, block)) {  // �Փ˂��Ȃ���Έʒu���X�V
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
                } else {  // �ړ��ł��Ȃ������̃u���b�N�ƂԂ��遁�Œ肷��
                    // �u���b�N���t�B�[���h�ɌŒ肷��
                    field.fixBlock(pos, block);
                    // ���C���p�l���̃L�[���͂����b�N����
                    MainPanel.keyLock();
                    // �Œ肳�ꂽ��true��Ԃ�
                    return true;
                }
                break;
            case FALL :
                newPos = new Point(pos.x, pos.y + 1);
                // �u���b�N�����܂ŗ��Ƃ�
                while ( field.isMovable(newPos, block) ) {
                    pos = newPos;
                    newPos = new Point(pos.x, pos.y+1);
                }
                // �u���b�N���t�B�[���h�ɌŒ肷��
                field.fixBlock(pos, block);
                // ���C���p�l���̃L�[���͂����b�N����
                MainPanel.keyLock();
                // �Œ肳�ꂽ��true��Ԃ�
                return true;
        }        
        return false;
    }


    /**
     * �u���b�N����]������
     */
    public void turn(int dir) {
        // ��]�����u���b�N
        int[][] turnedBlock = new int[ROW][COL];
        if ( dir == RIGHT ) { // �E��]
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    turnedBlock[j][ROW - 1 - i] = block[i][j];
                }
            }
        } else {  // ����]
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    turnedBlock[ROW - 1 - j][i] = block[i][j];
                }
            }            
        }

        // ��]�\�����ׂ�
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
        // �u���b�N�̃C���[�W��ǂݍ���
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
