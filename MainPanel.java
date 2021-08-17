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
    private static int startfrag = 0; // スタートメニューのフラグ
    private static boolean menufrag = true; // メニューとゲーム中の選択フラグ
    private static boolean tmpfrag = false; //ゲームオーバー後のフラグ
    private static int overfrag = 0; //ゲームオーバー後のフラグ
    private static boolean gameFlag = false;
    private Image img;
    // パネルサイズ
    public static final int WIDTH = 256;
    public static final int HEIGHT = 416;

    // フィールド
    private Field field;
    // ブロック
    private static Block block;
    // 次のブロック
    private static Block nextBlock;
    // 次のブロック
    private static Block afterNextBlock;
    // ホールドブロック
    private Block holdBlock;
    // ホールドブロックと現在のブロックの交換のための一時変数
    private Block tmpBlock;
    // ブロックのイメージ
    // private Image blockIma
    
    // ゲームループ用スレッド
    private Thread gameLoop;
    
    // スコアパネルへの参照
    private ScorePanel scorePanel;
    
    // 次のブロックパネルへの参照
    private NextBlockPanel nextBlockPanel;

    // ホールドパネルへの参照
    private HoldBlockPanel holdBlockPanel;

    private StartPanel startPanel;
    
    // サウンドエンジン
    private WaveEngine waveEngine = new WaveEngine();
    
    // サウンド名
    private static final String[] soundNames = {"start", "kacha", "puyo", "end", "move", "ent", "del"};
    private static final String[] soundFiles = {"se/police-whistle1.wav", "se/kachi42.wav", "se/puyon1.wav", "se/police-whistle2.wav", "se/cursor7.wav", "se/decision9.wav", "se/excellent1.wav"};


    public int score;
    
    // キー入力の受付の可否
    private static boolean keyflag;

    // ホールド入力の受付の可否
    private static boolean holdFlag;

    // ゲームを一時停止しているか
    private static boolean stopFlag;

    private static boolean finishFlag;

    public MainPanel(ScorePanel scorePanel, NextBlockPanel nextBlockPanel, HoldBlockPanel holdBlockPanel, StartPanel startPanel) {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);
        
        this.scorePanel = scorePanel;
        this.nextBlockPanel = nextBlockPanel;
        this.holdBlockPanel = holdBlockPanel;
        this.startPanel = startPanel;
        field = new Field();
        // だひょなー
        //blockImage = Toolkit.getDefaultToolkit().getImage("Image/2.png");
        
        // 使用文字の読込
        VanishData.makeCharList();
        // 辞書の読込
        VanishData.makeDic();
        //  キー入力を受け付ける
        keyUnlock();
        //  ホールドを可能に
        holdUnlock();
        // ポーズ機能は使えないなぁ
        //　ゲームは一時停止していません
        //  stopFlag = false;
        addKeyListener(this);
        
        // サウンドをロード
        loadSound();
        

        try {
        	// BGMをロード
        	MidiEngine.load("bgm/tetrisb.mid");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        finishFlag = false;
        // ゲームループ開始
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
    	// BGMスタート！
        MidiEngine.play(0);
        //開始SE
    	waveEngine.play("start");
        while (true) {
        	int chain = 1;
        	int cnt = 1;
            // ブロックを下方向へ移動する
            boolean isFixed = block.move(Block.DOWN);
            if (isFixed) {  // ブロックが固定されたら
            	// ブロックを削除
                block.init();
                // かちゃって鳴らす
                waveEngine.play("kacha");
                // 下にブロックがない場合落とす
                fall();
                // 消える熟語がある間
                while ( field.vanishWeight() ) {
                    // 同時消しの回数だけループ
                    for ( int j = 0; j < field.getVanishListSize(); j++ ) {
                        // fieldに削除データをset
                        field.setVanish(j);
                        repaint();
                        ThreadSleep(1000);
                        // 削除しスコアを更新       
                        score = field.vanish();
                        // 消去音鳴らす
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
                // ホールドとキー入力を可能に
                keyUnlock();
                holdUnlock();
                // ブロックの更新   
                block = nextBlock;
                // 次のブロックの更新
            	nextBlock = afterNextBlock;
                // 次の次のブロックの作成
                afterNextBlock = createBlock(field);
                // 次のブロックをnextBlockPanelにセット
                nextBlockPanel.set(nextBlock, afterNextBlock);
            }
            if ( finishFlag ) {
                keyUnlock();
                break;
            }
            repaint();
            ThreadSleep(500);
        }
        // BGMおわり！
        MidiEngine.stop();
        //終了SE
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
            // フィールドを描画
            field.draw(g);
            // ブロックを描画
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
            if (key == KeyEvent.VK_LEFT) { // ブロックを左へ移動
                block.move(Block.LEFT);
            } else if (key == KeyEvent.VK_RIGHT) { // ブロックを右へ移動
                block.move(Block.RIGHT);
            } else if (key == KeyEvent.VK_DOWN) { // ブロックを下へ移動
                block.move(Block.DOWN);
            } else if ( key == KeyEvent.VK_UP ) { // ブロックを瞬間落下
                block.move(Block.FALL);
            } else if (key == KeyEvent.VK_A) { // ブロックを左回転
                block.turn(Block.LEFT);
            } else if (key == KeyEvent.VK_S) { // ブロックを右回転
                block.turn(Block.RIGHT);
            } else if (key == KeyEvent.VK_W) { // ブロックをホールド
                // ホールドが可能ならば
                if ( holdFlag ) {
                    // ホールド
                    blockHold();
                }
            }
            repaint();
        } else if (menufrag == true) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
            	//移動SE
            	waveEngine.play("move");
                if (startfrag == 0) {
                } else {
                    startfrag--;
                    repaint();
                }
            } else if (key == KeyEvent.VK_DOWN) {
            	//移動SE
            	waveEngine.play("move");
                if (startfrag == 3) {
                } else {
                    startfrag++;
                    repaint();
                }
            } else if (key == KeyEvent.VK_ENTER) {
            	//決定SE
            	waveEngine.play("ent");
                menufrag = false;
                // ゲームループ開始
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
            	//決定SE
            	waveEngine.play("ent");
                menufrag = true;
                repaint();
            }
        } else if (finishFlag == true) {
            int key = e.getKeyCode();
            field = new Field();
            if (key == KeyEvent.VK_LEFT) {
            	//移動SE
            	waveEngine.play("move");
                overfrag = 0;
                repaint();
            } else if (key == KeyEvent.VK_RIGHT) {
            	//移動SE
            	waveEngine.play("move");
                overfrag = 1;
                repaint();
            } else if (key == KeyEvent.VK_ENTER) {
            	//決定SE
            	waveEngine.play("ent");
                finishFlag = false;
                startfrag = 0;
                keyUnlock();
                if (overfrag == 0) {
                    gameLoop = new Thread(this);
                    // ゲームループ開始
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
     * ランダムに次のブロックを作成
     * 
     * @param field フィールドへの参照
     * @return ランダムに生成されたブロック
     */
    private Block createBlock(Field field) {
        // 乱数を用いてランダムにブロックを作る
        return new Block(field);
    }
    
    public void BacgroundPanel() {
        // ブロックのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource("image/3131_2.jpg"));
        img = icon.getImage();        
    }
    

    public void blockHold(){
        if ( holdBlock == null ) {
            // ホールドブロック更新
            holdBlock = block;
            // ブロックの更新
            block = nextBlock;
            nextBlock = afterNextBlock;
            afterNextBlock = createBlock(field);
            nextBlockPanel.set(nextBlock, afterNextBlock);
        } else {
            tmpBlock = holdBlock;
            holdBlock = block;
            // ホールドからブロックを戻して初期位置に。
            block = tmpBlock;
            block.posSet();
            // 一度ホールドから戻したブロックはホールドできない
            holdLock();
        }
        // ホールドブロックをholdBlockPanelにセット
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

        // サウンドをロード
        for (int i = 0; i < soundNames.length; i++) {
            waveEngine.load(soundNames[i], soundFiles[i]);
        }
    }
    
}
