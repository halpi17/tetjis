import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2006/04/23
 */

public class Tetris extends JFrame {
    public Tetris() {
        // タイトルを設定
        setTitle("GoGoSILENTSIREN");
        // サイズ変更不可
        setResizable(false);
        
        Container contentPane = getContentPane();
        // 右側パネル
        JPanel rightPanel = new JPanel();
        // 左側パネル
        JPanel leftPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        leftPanel.setLayout(new BorderLayout());
        // スコア表示パネル
        ScorePanel scorePanel = new ScorePanel();
        // ホールドブロックパネル
        HoldBlockPanel holdBlockPanel = new HoldBlockPanel();
        // 次のブロックパネル
        NextBlockPanel nextBlockPanel = new NextBlockPanel();
        StartPanel startPanel = new StartPanel();
        rightPanel.add(scorePanel, BorderLayout.NORTH);
        rightPanel.add(nextBlockPanel, BorderLayout.CENTER);
        leftPanel.add(holdBlockPanel, BorderLayout.NORTH);
        
        // メインパネルを作成してフレームに追加
        MainPanel mainPanel = new MainPanel(scorePanel, nextBlockPanel, holdBlockPanel, startPanel);
        contentPane.add(rightPanel, BorderLayout.EAST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(leftPanel, BorderLayout.WEST);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Tetris frame = new Tetris();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
