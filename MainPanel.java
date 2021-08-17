import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.io.*;

import javax.imageio.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/*
 * Created on 2006/04/23
 */

public class MainPanel extends JPanel implements KeyListener, Runnable {
    private static int startfrag = 0; // �X�^�[�g���j���[�̃t���O
    private static boolean menufrag = true; // ���j���[�ƃQ�[�����̑I���t���O
    private static boolean tmpfrag = false; //�Q�[���I�[�o�[��̃t���O
    private static int overfrag = 0; //�Q�[���I�[�o�[��̃t���O
    private static boolean gameFlag = false;
    private Image img;
    // �p�l���T�C�Y
    public static final int WIDTH = 256;
    public static final int HEIGHT = 416;

    // �t�B�[���h
    private Field field;
    // �u���b�N
    private static Block block;
    // ���̃u���b�N
    private static Block nextBlock;
    // ���̃u���b�N
    private static Block afterNextBlock;
    // �z�[���h�u���b�N
    private Block holdBlock;
    // �z�[���h�u���b�N�ƌ��݂̃u���b�N�̌����̂��߂̈ꎞ�ϐ�
    private Block tmpBlock;
    // �u���b�N�̃C���[�W
    // private Image blockIma
    
    // �Q�[�����[�v�p�X���b�h
    private Thread gameLoop;
    
    // �X�R�A�p�l���ւ̎Q��
    private ScorePanel scorePanel;
    
    // ���̃u���b�N�p�l���ւ̎Q��
    private NextBlockPanel nextBlockPanel;

    // �z�[���h�p�l���ւ̎Q��
    private HoldBlockPanel holdBlockPanel;

    private StartPanel startPanel;
    
    // �T�E���h�G���W��
    private WaveEngine waveEngine = new WaveEngine();
    
    // �T�E���h��
    private static final String[] soundNames = {"start", "kacha", "puyo", "end", "move", "ent", "del"};
    private static final String[] soundFiles = {"se/police-whistle1.wav", "se/kachi42.wav", "se/puyon1.wav", "se/police-whistle2.wav", "se/cursor7.wav", "se/decision9.wav", "se/excellent1.wav"};


    public int score;
    
    // �L�[���͂̎�t�̉�
    private static boolean keyflag;

    // �z�[���h���͂̎�t�̉�
    private static boolean holdFlag;

    // �Q�[�����ꎞ��~���Ă��邩
    private static boolean stopFlag;

    private static boolean finishFlag;

    public MainPanel(ScorePanel scorePanel, NextBlockPanel nextBlockPanel, HoldBlockPanel holdBlockPanel, StartPanel startPanel) {
        // �p�l���̐����T�C�Y��ݒ�Apack()����Ƃ��ɕK�v
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // �p�l�����L�[���͂��󂯕t����悤�ɂ���
        setFocusable(true);
        
        this.scorePanel = scorePanel;
        this.nextBlockPanel = nextBlockPanel;
        this.holdBlockPanel = holdBlockPanel;
        this.startPanel = startPanel;
        field = new Field();
        // ���Ђ�ȁ[
        //blockImage = Toolkit.getDefaultToolkit().getImage("Image/2.png");
        
        // �g�p�����̓Ǎ�
        VanishData.makeCharList();
        // �����̓Ǎ�
        VanishData.makeDic();
        //  �L�[���͂��󂯕t����
        keyUnlock();
        //  �z�[���h���\��
        holdUnlock();
        // �|�[�Y�@�\�͎g���Ȃ��Ȃ�
        //�@�Q�[���͈ꎞ��~���Ă��܂���
        //  stopFlag = false;
        addKeyListener(this);
        
        // �T�E���h�����[�h
        loadSound();
        

        try {
        	// BGM�����[�h
        	MidiEngine.load("bgm/tetrisb.mid");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        finishFlag = false;
        // �Q�[�����[�v�J�n
        // gameLoop = new Thread(this);
        // gameLoop.start();
    }

    public void ThreadSleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fall() {
        while ( field.fallBlock() ) {
            repaint();
            ThreadSleep(100);
        }
    }

    public void run() {
    	// BGM�X�^�[�g�I
        MidiEngine.play(0);
        //�J�nSE
    	waveEngine.play("start");
        while (true) {
        	int chain = 1;
        	int cnt = 1;
            // �u���b�N���������ֈړ�����
            boolean isFixed = block.move(Block.DOWN);
            if (isFixed) {  // �u���b�N���Œ肳�ꂽ��
            	// �u���b�N���폜
                block.init();
                // ��������Ė炷
                waveEngine.play("kacha");
                // ���Ƀu���b�N���Ȃ��ꍇ���Ƃ�
                fall();
                // ������n�ꂪ�����
                while ( field.vanishWeight() ) {
                    // ���������̉񐔂������[�v
                    for ( int j = 0; j < field.getVanishListSize(); j++ ) {
                        // field�ɍ폜�f�[�^��set
                        field.setVanish(j);
                        repaint();
                        ThreadSleep(1000);
                        // �폜���X�R�A���X�V       
                        score = field.vanish();
                        // �������炷
                        waveEngine.play("puyo");
                        scorePanel.upScore(score * 100 * chain * cnt);
                        cnt += 2;
                    }
                    cnt = 1;
                    chain *= 2;
                    fall();
                    ThreadSleep(300);
                   if (field.allDelite() == true) {
                	   waveEngine.play("del");
                	   scorePanel.upScore(500);
                   }
                }
                if ( field.isStacked() ) {
                    finishFlag = true;
                    // scorePanel.setScore(0);
                    holdBlockPanel.holdReset();
                    nextBlockPanel.nextBlockReset();
                    gameFlag = false;
                    repaint();
                    break;
                }
                // �z�[���h�ƃL�[���͂��\��
                keyUnlock();
                holdUnlock();
                // �u���b�N�̍X�V   
                block = nextBlock;
                // ���̃u���b�N�̍X�V
            	nextBlock = afterNextBlock;
                // ���̎��̃u���b�N�̍쐬
                afterNextBlock = createBlock(field);
                // ���̃u���b�N��nextBlockPanel�ɃZ�b�g
                nextBlockPanel.set(nextBlock, afterNextBlock);
            }
            if ( finishFlag ) {
                keyUnlock();
                break;
            }
            repaint();
            ThreadSleep(500);
        }
        // BGM�����I
        MidiEngine.stop();
        //�I��SE
    	waveEngine.play("end");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menufrag == true) {
            startPanel.startmenu(g);
        } else if ( finishFlag == true ){
            startPanel.overmenu(g);
        } else if (startfrag == 3){
            startPanel.method(g);
        } else {
            // �t�B�[���h��`��
            field.draw(g);
            // �u���b�N��`��
            block.draw(g);
        }
    }
    public static void keyLock(){
        keyflag = false;
    }

    public static void keyUnlock(){
        keyflag = true;
    }
    public void holdUnlock(){
        holdFlag = true;
    }

    public void holdLock() {
        holdFlag = false;
    }
    
    synchronized public void stopGame() {
        try{
            wait();
        }catch(Exception e){}
    }
    synchronized public void restartGame() {
        notify();
    }
    

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if ( keyflag == true && menufrag == false && startfrag != 3) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) { // �u���b�N�����ֈړ�
                block.move(Block.LEFT);
            } else if (key == KeyEvent.VK_RIGHT) { // �u���b�N���E�ֈړ�
                block.move(Block.RIGHT);
            } else if (key == KeyEvent.VK_DOWN) { // �u���b�N�����ֈړ�
                block.move(Block.DOWN);
            } else if ( key == KeyEvent.VK_UP ) { // �u���b�N���u�ԗ���
                block.move(Block.FALL);
            } else if (key == KeyEvent.VK_A) { // �u���b�N������]
                block.turn(Block.LEFT);
            } else if (key == KeyEvent.VK_S) { // �u���b�N���E��]
                block.turn(Block.RIGHT);
            } else if (key == KeyEvent.VK_W) { // �u���b�N���z�[���h
                // �z�[���h���\�Ȃ��
                if ( holdFlag ) {
                    // �z�[���h
                    blockHold();
                }
            }
            repaint();
        } else if (menufrag == true) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
            	//�ړ�SE
            	waveEngine.play("move");
                if (startfrag == 0) {
                } else {
                    startfrag--;
                    repaint();
                }
            } else if (key == KeyEvent.VK_DOWN) {
            	//�ړ�SE
            	waveEngine.play("move");
                if (startfrag == 3) {
                } else {
                    startfrag++;
                    repaint();
                }
            } else if (key == KeyEvent.VK_ENTER) {
            	//����SE
            	waveEngine.play("ent");
                menufrag = false;
                // �Q�[�����[�v�J�n
                if (startfrag == 3) {
                    repaint();
                } else {
                    gameLoop = new Thread(this);
                    RandomInt.setDifficulty(startfrag);
                    block = createBlock(field);
                    nextBlock = createBlock(field);
                    afterNextBlock = createBlock(field);
                    gameFlag = true;
                    nextBlockPanel.set(nextBlock, afterNextBlock);
                    scorePanel.setScore(0);
                    holdBlockPanel.set();
                    gameLoop.start();
                    repaint();
                }
            }
        } else if (startfrag == 3){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
            	//����SE
            	waveEngine.play("ent");
                menufrag = true;
                repaint();
            }
        } else if (finishFlag == true) {
            int key = e.getKeyCode();
            field = new Field();
            if (key == KeyEvent.VK_LEFT) {
            	//�ړ�SE
            	waveEngine.play("move");
                overfrag = 0;
                repaint();
            } else if (key == KeyEvent.VK_RIGHT) {
            	//�ړ�SE
            	waveEngine.play("move");
                overfrag = 1;
                repaint();
            } else if (key == KeyEvent.VK_ENTER) {
            	//����SE
            	waveEngine.play("ent");
                finishFlag = false;
                startfrag = 0;
                keyUnlock();
                if (overfrag == 0) {
                    gameLoop = new Thread(this);
                    // �Q�[�����[�v�J�n
                    block = createBlock(field);
                    nextBlock = createBlock(field);
                    afterNextBlock = createBlock(field);
                    gameFlag = true;
                    nextBlockPanel.set(nextBlock, afterNextBlock);
                    holdBlockPanel.set();
                    scorePanel.setScore(0);
                    gameLoop.start();
                    repaint();
                } else {
                    menufrag = true;
                    repaint();
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * �����_���Ɏ��̃u���b�N���쐬
     * 
     * @param field �t�B�[���h�ւ̎Q��
     * @return �����_���ɐ������ꂽ�u���b�N
     */
    private Block createBlock(Field field) {
        // ������p���ă����_���Ƀu���b�N�����
        return new Block(field);
    }
    
    public void BacgroundPanel() {
        // �u���b�N�̃C���[�W��ǂݍ���
        ImageIcon icon = new ImageIcon(getClass().getResource("image/3131_2.jpg"));
        img = icon.getImage();        
    }
    

    public void blockHold(){
        if ( holdBlock == null ) {
            // �z�[���h�u���b�N�X�V
            holdBlock = block;
            // �u���b�N�̍X�V
            block = nextBlock;
            nextBlock = afterNextBlock;
            afterNextBlock = createBlock(field);
            nextBlockPanel.set(nextBlock, afterNextBlock);
        } else {
            tmpBlock = holdBlock;
            holdBlock = block;
            // �z�[���h����u���b�N��߂��ď����ʒu�ɁB
            block = tmpBlock;
            block.posSet();
            // ��x�z�[���h����߂����u���b�N�̓z�[���h�ł��Ȃ�
            holdLock();
        }
        // �z�[���h�u���b�N��holdBlockPanel�ɃZ�b�g
        holdBlockPanel.set(holdBlock);
    }
    
    
    public static int getStartFrag() {
        return startfrag;
    }

    public static int getOverFrag() {
        return overfrag;
    }

    public static boolean getGameFlag() {
        return gameFlag;
    }
    
    public static boolean getMenuFlag() {
        return menufrag;
    }
    
    private void loadSound() {

        // �T�E���h�����[�h
        for (int i = 0; i < soundNames.length; i++) {
            waveEngine.load(soundNames[i], soundFiles[i]);
        }
    }
    
}
