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

        // ���̃u���b�N��`��
        if (afterNextBlock != null) {
            afterNextBlock.drawInPanel(g);
        }
    }

    /**
     * ���̃u���b�N���Z�b�g
     * 
     * @param afterNextBlock ���̎��̃u���b�N
     * @param blockImage �u���b�N�C���[�W
     */
    public void set(Block afterNextBlock) {
        this.afterNextBlock = afterNextBlock;
        repaint();
    }
}
