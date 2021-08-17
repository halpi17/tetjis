import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Font;
/*
 * Created on 2006/12/09
 */

public class NextBlockPanel extends JPanel {
    public static final int WIDTH = 96;
    public static final int HEIGHT = MainPanel.HEIGHT-ScorePanel.HEIGHT;
    public static final int TILE_SIZE = Block.TILE_SIZE;
    private int[][] nextBlock;
    private int[][] afterNextBlock;
    private Image blockImage;
    public NextBlockPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        if ( MainPanel.getGameFlag()) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.PINK);
            g.fillRoundRect(11, 0, WIDTH-32, 100, 30, 25);
            Font font = new Font("Sazanami Gothic", Font.PLAIN, 16);
            g.fillRoundRect(24, TILE_SIZE*2, WIDTH-32, 100, 30, 25);
            g.setFont(font);
            g.setColor(Color.BLUE);
            g.drawString("NEXT", 21, 20);
        } else if ( MainPanel.getMenuFlag() || MainPanel.getStartFrag() == 3 ) {
            loadImage("image/titleimage4.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        } else {
            loadImage("image/background6.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        }

        if ( nextBlock == null || afterNextBlock == null ) {
            return;
        }
        
        int k = 0;
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (nextBlock[i][j] != 0) {
                	loadImage("image/" + nextBlock[i][j] + ".png");
                	g.drawImage(blockImage, TILE_SIZE-5, TILE_SIZE * (k + 1)-10, TILE_SIZE, TILE_SIZE, null);
                    k++;
                }
            }
        }

        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (afterNextBlock[i][j] != 0) {
                	loadImage("image/" + afterNextBlock[i][j] + ".png");
                	g.drawImage(blockImage, TILE_SIZE+8, TILE_SIZE * (k + 1)-10, TILE_SIZE, TILE_SIZE, null);
                    k++;
                }
            }
        }
    }

    /**
     * 次のブロックをセット
     * 
     * @param nextBlock 次のブロック
     * @param blockImage ブロックイメージ
     */
    public void set(Block nextBlock, Block afterNextBlock) {
        this.nextBlock = nextBlock.getBlock();
        this.afterNextBlock = afterNextBlock.getBlock();
        repaint();
    }

    public void loadImage(String filename) {
        // ブロックのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }

    public void nextBlockReset() {
        nextBlock = null;
        afterNextBlock = null;
        repaint();
    }
}
