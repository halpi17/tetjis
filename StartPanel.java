import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.text.DecimalFormat;
/*
 * Created on 2006/12/09
 */

public class StartPanel extends JPanel {
    public static final int WIDTH = MainPanel.WIDTH;
    public static final int HEIGHT = MainPanel.HEIGHT;
    public static final int TILE_SIZE = Block.TILE_SIZE;
    private int[][] nextBlock;
    private int[][] afterNextBlock;
    private Image blockImage;
    private int startfrag = 0; // �X�^�[�g���j���[�̃t���O
    private boolean menufrag = true; // ���j���[�ƃQ�[�����̑I���t���O
    private boolean tmpfrag = false; //�Q�[���I�[�o�[��̃t���O
    private int overfrag = 0; //�Q�[���I�[�o�[��̃t���O
    public StartPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        startmenu(g);
    }

    public void loadImage(String filename) {
        // �u���b�N�̃C���[�W��ǂݍ���
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }

    public void startmenu(Graphics g) {
        loadImage("image/titleimage3.png");
        startfrag = MainPanel.getStartFrag();
        g.drawImage(blockImage, 0, 0, this);
        Font font1 = new Font("HGP�n�p�p�߯�ߑ�", Font.BOLD, 40);
        g.setColor(Color.RED);
        g.setFont(font1);
        g.drawString("�����e�g���X", 0, 45);
        if (startfrag == 0) {
            Font font2 = new Font("���C���I", Font.BOLD, 30);
            g.setFont(font2);
            g.setColor(Color.ORANGE);
            g.drawString("����", 30, 130);
            g.setColor(Color.BLACK);
            g.drawString("����", 20, 180);
            g.drawString("���h", 20, 230);
            g.drawString("�������", 20, 280);
        } else if (startfrag == 1) {
            Font font2 = new Font("���C���I", Font.BOLD, 30);
            g.setFont(font2);
            g.setColor(Color.BLACK);
            g.drawString("����", 20, 130);
            g.setColor(Color.ORANGE);
            g.drawString("����", 30, 180);
            g.setColor(Color.BLACK);
            g.drawString("���h", 20, 230); 
            g.drawString("�������", 20, 280);
        } else if (startfrag == 2) {
            Font font2 = new Font("���C���I", Font.BOLD, 30);
            g.setFont(font2);
            g.setColor(Color.BLACK);
            g.drawString("����", 20, 130);
            g.drawString("����", 20, 180);
            g.setColor(Color.ORANGE);
            g.drawString("���h", 30, 230); 
            g.setColor(Color.BLACK);
            g.drawString("�������", 20, 280);
        } else if (startfrag == 3) {
            Font font2 = new Font("���C���I", Font.BOLD, 30);
            g.setFont(font2);
            g.setColor(Color.BLACK);
            g.drawString("����", 20, 130);
            g.drawString("����", 20, 180);
            g.drawString("���h", 20, 230); 
            g.setColor(Color.ORANGE);
            g.drawString("�������", 30, 280);
        }
    }

    public void method(Graphics g) {
        Font font1 = new Font("HGP�n�p�p�߯�ߑ�", Font.BOLD, 40);
        Font font2 = new Font("MS�S�V�b�N��", Font.BOLD, 20);
        loadImage("image/titleimage3.jpg");
        g.drawImage(blockImage, 0, 0, this);
        
        // g.setColor(Color.RED);
        // g.setFont(font1);
        // g.drawString("�����e�g���X", 0, 45);
        g.setColor(Color.MAGENTA);
        g.setFont(font2);
        g.drawString("������@", 30, 110);
        g.drawString("A ... ����]", 15, 140);
        g.drawString("S ... �E��]", 15, 160);
        g.drawString("�� ... �E�ړ�", 15, 180);
        g.drawString("�� ... ���ړ�", 15, 200);
        g.drawString("�� ... ���ړ�", 15, 220);
        g.drawString("�� ... �u�ԗ���", 15, 240);
        g.drawString("W ... �z�[���h", 15, 260);
        g.setColor(Color.BLUE);
        g.drawString("���j���[�ɖ߂�", 5, 340);
    }

    public void overmenu(Graphics g) {
        DecimalFormat formatter = new DecimalFormat("000000");
        Font font1 = new Font("HGP�n�p�p�߯�ߑ�", Font.BOLD, 40);
        Font font2 = new Font("MS�S�V�b�N��", Font.BOLD, 20);
        loadImage("image/background2.jpg");
        g.drawImage(blockImage, 0, 0, this);
        overfrag = MainPanel.getOverFrag();
        g.setColor(Color.RED);
        g.setFont(font1);
        g.drawString("GAME OVER", 0, 180);
        g.setFont(font2);
        g.setColor(Color.ORANGE);
        g.drawString("SCORE�F" + formatter.format(ScorePanel.getScore()), 40, 325);
        if (overfrag == 0) {
            g.setColor(Color.BLUE);
            g.setFont(font2);
            g.drawString("������x�I", 00, 360);
            g.setColor(Color.BLACK);
            g.setFont(font2);
            g.drawString("�^�C�g����ʂ�", 105, 370);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(font2);
            g.drawString("������x�I", 00, 370);
            g.setColor(Color.BLUE);
            g.setFont(font2);
            g.drawString("�^�C�g����ʂ�", 105, 360);
        }
    }
}
