import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Image;
/*
 * Created on 2006/07/16
 */

public class ScorePanel extends JPanel {
    // �p�l���T�C�Y
    public static final int WIDTH = 96;
    public static final int HEIGHT = 16;
    private Image blockImage;
    // �X�R�A
    private static int score;

    public ScorePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        if ( MainPanel.getGameFlag() ) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // �X�R�A��`��
            g.setColor(Color.RED);
            DecimalFormat formatter = new DecimalFormat("000000");
            // �t�H���g���쐬
            Font font = new Font("Ariel", Font.BOLD, 16);
            g.setFont(font);

            g.drawString(formatter.format(score), 16, 12);
        } else if ( MainPanel.getMenuFlag() || MainPanel.getStartFrag() == 3 ) {
            loadImage("image/titleimage5.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        } else {
            loadImage("image/background5.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        }
    }

    /**
     * �X�R�A���Z�b�g����
     * 
     * @param score �X�R�A
     */
    public void setScore(int score) {
        this.score = score;

        repaint();
    }

    /**
     * �X�R�A���v���X����
     * 
     * @param d �v���X��
     */
    public void upScore(int d) {
        score += d;

        repaint();
    }
    public void loadImage(String filename) {
        // �u���b�N�̃C���[�W��ǂݍ���
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }
    public static int getScore() {
        return score;
    }

    public void set() {
        repaint();
    }
}
