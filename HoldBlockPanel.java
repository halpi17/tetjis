import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
/*
 * Created on 2006/12/09
 */

public class HoldBlockPanel extends JPanel {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 416;
    public static final int TILE_SIZE = Block.TILE_SIZE;
    private static Block holdBlock;
    private int[][] block;
    private Image blockImage;

    public HoldBlockPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        if ( MainPanel.getGameFlag() ) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.CYAN);
            g.fillRoundRect(16, 10, WIDTH-32, 100, 30, 30);
            Font font = new Font("Sazanami Gothic", Font.PLAIN, 16);
            g.setFont(font);
            g.setColor(Color.BLUE);
            g.drawString("HOLD", 27, 25);
        } else if ( MainPanel.getMenuFlag() || MainPanel.getStartFrag() == 3 ) {
            loadImage("image/titleimage2.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        } else {
            loadImage("image/background1.jpg");
            g.drawImage(blockImage, 0, 0, this);
            repaint();
        }
        if ( block == null ) {
            return;
        }
        // ホールドブロックを描画
        int k = 0;
        for (int i = 0; i < Block.ROW; i++) {
            for (int j = 0; j < Block.COL; j++) {
                if (block[i][j] != 0) {
                	loadImage("image/" + block[i][j] + ".png");
                	g.drawImage(blockImage, TILE_SIZE, TILE_SIZE * (k + 1), TILE_SIZE, TILE_SIZE, null);
                    k++;
                }
            }
        }
    }

    /**
     * ホールドブロックをセット
     * 
     * @param holdBlock ホールドブロック
     * @param blockImage ブロックイメージ
     */
    public void set() {
        repaint();
    }
    public void set(Block holdBlock) {
        this.holdBlock = holdBlock;
        this.block = holdBlock.getBlock();
        repaint();
    }

    public Block get() {
        return holdBlock;
    }

    public void loadImage(String filename) {
        // ブロックのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();        
    }

    public void holdReset() {
        block = null;
        repaint();
    }
}
