package Tetris1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioFormat; // 
import javax.sound.sampled.AudioInputStream; //
import javax.sound.sampled.AudioSystem; //
import javax.sound.sampled.Clip; //
import javax.sound.sampled.DataLine; //
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.Timer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class TetrisCanvas extends JPanel implements Runnable, KeyListener, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Thread worker;
	protected TetrisData data;
	protected Piece   current;
	protected Piece newBlock;
	protected Piece PieceGhost;
	protected Piece save;
	protected Piece tmp;	
	private boolean isMusicPlaying = false;
	protected boolean makeNew;
	protected boolean savePiece = true;
	protected boolean stop;
	//그래픽스 함수를 사용하기 위한 클래스
	private Graphics bufferGraphics = null;
	//bufferGraphics로 그림을 그릴 때 실제로 그려지는 가상 버퍼
	private Image offscreen; 
	//화면의 크기를 저장하는 변수
	private Dimension dim;
	private TetrisPreview preview;
	private MyTetris myTetris;
	// 효과음
	
	public TetrisCanvas(MyTetris t) {
		this.myTetris = t;
		data = new TetrisData();
		addKeyListener(this);		
		addComponentListener(this);
	}
	
		
//		try {
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("effect/bottom.wav")); // 소리 파일 경로에 따라 수정
//            audioClip = AudioSystem.getClip();
	
//            audioClip.open(audioStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//	public void playSound() {
//		try {
//	        // 오디오 파일 경로 설정 (여기에서는 "effect/bottom.wav" 파일을 사용)
//	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("effect/bottom.wav"));
//	        
//	        // 클립 생성
//	        Clip audioClip = AudioSystem.getClip();
//	        
//	        // 클립에 오디오 스트림 열기
//	        audioClip.open(audioStream);
//	        
//	        // 소리 재생 위치를 처음으로 리셋
//	        audioClip.setFramePosition(0);
//	        
//	        // 소리 재생
//	        audioClip.start();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//    }
	
	public void setTetrisPreview(TetrisPreview preview) {
		this.preview = preview;
	}
	
	//버퍼 초기 함수
	public void initBufferd() {
        dim = getSize();
        System.out.println(dim.getSize());
        //화면의 크기를 가져온다.
        setBackground(Color.white);
        //배경 색깔을 흰색으로 변경한다. 
        offscreen = createImage(dim.width,dim.height);
        //화면 크기와 똑같은 가상 버퍼(이미지)를 생성한다.
        bufferGraphics = offscreen.getGraphics(); 
        //가상버퍼(이미지)로 부터 그래픽스 객체를 얻어옴
	}
	
	public void start() {    // 게임 시작
		data.clear();
		worker = new Thread(this);
		worker.start();
		
		
		int random = (int)(Math.random() * Integer.MAX_VALUE) % 7;
		switch(random){
		case 0:
			current = new Bar(data);
			break;
		case 1:
			current = new Tee(data);
			break;
		case 2:
			current = new El(data);
			break;
		case 3:
			current = new Er(data);
			break;
		case 4:
			current = new O(data);
			break;
		case 5:
			current = new S(data);
			break;
		case 6:
			current = new Z(data);
			break;
		default:
			if(random % 2 == 0)
				current = new Tee(data);
			else current = new El(data);
		}
		PieceGhost= new Bar(data);
		createBlock();
		preview.setCurrentBlock(newBlock); // 다음 블럭 보기
		current.PieceGhost(PieceGhost);
		
		makeNew = false;
		stop = false;
		requestFocus();
		repaint();
	}

	// 레벨초기화
	public void levelreset(){ 
		Constant.resetlevel();
	}
	
	public void resetLine() {
		data.resetLine();
	}

	public void stop() {
		stop = true;
		current = null;
		JOptionPane.showMessageDialog(this,"게임끝\n점수 : " + data.score);
		data.score = 0;
		levelreset();
		resetLine();
	}
	
	public void paint(Graphics g) {

		//화면을 지운다. 지우지 않으면 이전그림이 그대로 남아 잔상이 생김
		bufferGraphics.clearRect(0,0,dim.width,dim.height); 
		
		//쌓인 조각들 그리기
		for(int i = 0; i < TetrisData.ROW; i++) {
			for(int k = 0; k < TetrisData.COL; k++) {
				if(data.getAt(i, k) == 0) {
					bufferGraphics.setColor(Constant.getColor(data.getAt(i, k)));
					bufferGraphics.draw3DRect(Constant.margin/2 + Constant.w * k,
							Constant.margin/2 + Constant.w * i, Constant.w, Constant.w, true);
				} else {
					bufferGraphics.setColor(Constant.getColor(data.getAt(i, k)));
					bufferGraphics.fill3DRect(Constant.margin/2 + Constant.w * k, 
							Constant.margin/2 + Constant.w * i, Constant.w, Constant.w, true);
				}
			}
		}
		//도형 고스트
		if(PieceGhost != null) {
			for(int i = 0; i < 4; i++) {
				bufferGraphics.setColor(Color.gray);
				bufferGraphics.fill3DRect(Constant.margin/2 + Constant.w * (PieceGhost.getX()+PieceGhost.c[i]), 
						Constant.margin/2 + Constant.w * (PieceGhost.getY()+PieceGhost.r[i]), 
						Constant.w, Constant.w, true);
			}
		}
		
		
		// 현재 내려오고 있는 테트리스 조각 그리
		if(current != null){
			for(int i = 0; i < 4; i++) {
				bufferGraphics.setColor(Constant.getColor(current.getType()));
				bufferGraphics.fill3DRect(Constant.margin/2 + Constant.w * (current.getX()+current.c[i]), 
						Constant.margin/2 + Constant.w * (current.getY()+current.r[i]), 
						Constant.w, Constant.w, true);
			}
		}
		
		
		
		bufferGraphics.setColor(Color.black);
		bufferGraphics.drawString("현재 점수 : " + data.score, 10, 525);
		
		bufferGraphics.setColor(Color.black);
		bufferGraphics.drawString("지운 줄 : " + data.getLine(), 190, 525);
		
		bufferGraphics.setColor(Color.black);
		bufferGraphics.drawString("난이도 : " + Constant.level+"/10", 190, 545);
		
		//가상버퍼(이미지)를 원본 버퍼에 복사
		g.drawImage(offscreen,0,0,this);
		
		myTetris.refresh();
	}

	public Dimension getPreferredSize(){ // 테트리스 판의 크기 지정
		int tw = Constant.w * TetrisData.COL + Constant.margin;
		int th = Constant.w * TetrisData.ROW + Constant.margin;
		
		return new Dimension(tw, th);
	}
	
	private void createBlock() {
		int random = (int)(Math.random() * Integer.MAX_VALUE) % 7;
		switch(random){
		case 0:
			newBlock = new Bar(data);
			break;
		case 1:
			newBlock = new Tee(data);
			break;
		case 2:
			newBlock = new El(data);
			break;
		case 3:
			newBlock = new Er(data);
			break;
		case 4:
			newBlock = new O(data);
			break;
		case 5:
			newBlock = new S(data);
			break;
		case 6:
			newBlock = new Z(data);
			break;
		default:
			if(random % 2 == 0)
				newBlock = new Tee(data);
			else newBlock = new El(data);
		}
	}
	public void run(){
		
		while(!stop) {
			try {
				if(makeNew){ // 새로운 테트리스 조각 만들기
					if(newBlock !=null) {
						current = newBlock;
						PieceGhost= new Bar(data);
						current.PieceGhost(PieceGhost);
						}
					
					newBlock = null;
					createBlock();
					preview.setCurrentBlock(newBlock); //미리보기
					makeNew = false;
				} else { // 현재 만들어진 테트리스 조각 아래로 이동
					if(current.moveDown()){

						makeNew = true;
						if(current.copy()){
							stop();
//							int score = data.getLine() * 175 * Constant.level;
							JOptionPane.showMessageDialog(this,"게임끝\n점수 : " + data.score);
						}
						current = null;
						PieceGhost=null;
						// 가만히 놔두었을 때
						data.removeLines();
						savePiece = true;
					} else {
						//화면 밖으로 나가지 않도록 체크
						if(isOutOfScreen(current)) {
							current.moveDown(); //조각 이전위치로 이동
						}
					}
					
				}
				repaint();
				Thread.sleep(Constant.interval/Constant.level);
			} catch(Exception e){ }
		}
	}
	
	private boolean isOutOfScreen(Piece piece) {
		for(int i = 0; i < 4; i++) {
			int row = piece.getY() + piece.r[i];
			int col = piece.getX() + piece.c[i];
			if(row >= TetrisData.ROW || col >= TetrisData.COL) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }
	
	// 키보드를 이용해서 테트리스 조각 제어
	@Override
	public void keyPressed(KeyEvent e) {
		if(current == null) return;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: // 왼쪽 화살표
			current.moveLeft();
			current.PieceGhost(PieceGhost);
			repaint();
			break;
		case KeyEvent.VK_RIGHT:  // 오른쪽 화살표
			current.moveRight();
			current.PieceGhost(PieceGhost);
			repaint();
			break;
		case KeyEvent.VK_UP:  // 윗쪽 화살표
			current.rotate();
			current.PieceGhost(PieceGhost);
			repaint();
			break;
		case KeyEvent.VK_DOWN:  // 아랫쪽 화살표
			boolean temp = current.moveDown();
			if(temp){
				makeNew = true;
				if(current.copy()){
					stop();
//					int score = data.getLine() * 175 * Constant.level;
				}
				//  아래 방향키를 눌렀을 때
				current=null;
				PieceGhost=null;
				data.removeLines();
				savePiece = true;
				worker.interrupt();
				
			}
			repaint();
			
			
			break;
		case 32: // 스페이스바
			blockset();
			repaint();
			break;
			
		case KeyEvent.VK_C: //C 키
			blocksave();
			repaint();
			break;
		}
	}
	public void blockset() {
		while(!current.moveDown()) {
		}
		//playSound();
		makeNew = true;
		if (current.copy()) {
			stop();
		}
		data.removeLines();
		savePiece = true;
		worker.interrupt();
	}
	
	public void blocksave() {
		if(savePiece) {
			if(save == null) {
				save = new Bar(data);
			}
			else {
				save.save(tmp = new Bar(data));
			}
			
			current.save(save);
			
			if(tmp == null) {
			current = null;
			PieceGhost = null;

			makeNew=true;
			worker.interrupt();
			}
			else {
				tmp.save(current);
				current.center.x =5;
				current.center.y= 0;
				current.roteType = tmp.roteType();
				current.PieceGhost(PieceGhost);
			}
			
			preview.setSaveBlock(save);
			savePiece = false;
		}
	}
	
	public TetrisData getData() {
		return data;
	}
	
	public Piece getNewBlock() {
		return newBlock;
	}
	
	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
	public void componentResized(ComponentEvent e) {
		System.out.println("resize");
		initBufferd();
	}
	
	@Override
	public void componentMoved(ComponentEvent e) { }
	
	@Override
	public void componentShown(ComponentEvent e) { }
	
	@Override
	public void componentHidden(ComponentEvent e) { }
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
}
