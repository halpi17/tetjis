import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2006/04/23
 */

public class Tetris extends JFrame {
    public Tetris() {
        // �^�C�g����ݒ�
        setTitle("GoGoSILENTSIREN");
        // �T�C�Y�ύX�s��
        setResizable(false);
        
        Container contentPane = getContentPane();
        // �E���p�l��
        JPanel rightPanel = new JPanel();
        // �����p�l��
        JPanel leftPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        leftPanel.setLayout(new BorderLayout());
        // �X�R�A�\���p�l��
        ScorePanel scorePanel = new ScorePanel();
        // �z�[���h�u���b�N�p�l��
        HoldBlockPanel holdBlockPanel = new HoldBlockPanel();
        // ���̃u���b�N�p�l��
        NextBlockPanel nextBlockPanel = new NextBlockPanel();
        StartPanel startPanel = new StartPanel();
        rightPanel.add(scorePanel, BorderLayout.NORTH);
        rightPanel.add(nextBlockPanel, BorderLayout.CENTER);
        leftPanel.add(holdBlockPanel, BorderLayout.NORTH);
        
        // ���C���p�l�����쐬���ăt���[���ɒǉ�
        MainPanel mainPanel = new MainPanel(scorePanel, nextBlockPanel, holdBlockPanel, startPanel);
        contentPane.add(rightPanel, BorderLayout.EAST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(leftPanel, BorderLayout.WEST);

        // �p�l���T�C�Y�ɍ��킹�ăt���[���T�C�Y�������ݒ�
        pack();
    }

    public static void main(String[] args) {
        Tetris frame = new Tetris();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
