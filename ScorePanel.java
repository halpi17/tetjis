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
    // パネルサイズ
    public static final int WIDTH = 96;
    public static final int HEIGHT = 16;
    private Image blockImage;
    // スコア
    private static int score;

    public ScorePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        if ( MainPanel.getGameFlag() ) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // スコアを描画
            g.setColor(Color.RED);
            DecimalFormat formatter = new DecimalFormat("000000");
            // フォントを作成
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
     * スコアをセットする
     * 
     * @param score スコア
     */
    public void setScore(int score) {
        this.score = score;

        repaint();
    }

    /**
     * スコアをプラスする
     * 
     * @param d プラス分
     */
    public void upScore(int d) {
        score += d;

        repaint();
    }
    public void loadImage(String filename) {
        // ブロックのイメージを読み込む
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
