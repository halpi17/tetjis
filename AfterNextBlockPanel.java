import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/*
 * Created on 2006/12/09
 */

public class AfterNextBlockPanel extends JPanel {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 200;

    private Block afterNextBlock;
    private Image blockImage;

    public AfterNextBlockPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 次のブロックを描画
        if (afterNextBlock != null) {
            afterNextBlock.drawInPanel(g);
        }
    }

    /**
     * 次のブロックをセット
     * 
     * @param afterNextBlock 次の次のブロック
     * @param blockImage ブロックイメージ
     */
    public void set(Block afterNextBlock) {
        this.afterNextBlock = afterNextBlock;
        repaint();
    }
}
