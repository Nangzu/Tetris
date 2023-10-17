package Tetris1;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.io.File; //

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

public class MyTetris extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TetrisClient client = null;
	private boolean isMusicPlaying = false;
	private TetrisNetworkPreview netPreview = null;
	
	public MyTetris() {
		setTitle("테트리스");
		setSize(275*4, 650);
		
		
		
		
		GridLayout layout = new GridLayout(1,4);
		setLayout(layout);
		TetrisCanvas tetrisCanvas = new TetrisCanvas(this);
		TetrisNetworkCanvas netCanvas = new TetrisNetworkCanvas();
		createMenu(tetrisCanvas, netCanvas);
		TetrisPreview preview = new TetrisPreview(tetrisCanvas.getData());
		netPreview = new TetrisNetworkPreview(netCanvas.getData());
		tetrisCanvas.setTetrisPreview(preview);
		//netCanvas.setTetrisPreview(netPreview);
		add(tetrisCanvas);
		add(preview);
		add(netCanvas);
		add(netPreview);

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		Timer musicTimer = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isMusicPlaying) {
					Music();
				}
			}
		});
		musicTimer.start();
	}
	
	public void createMenu(TetrisCanvas tetrisCanvas, TetrisNetworkCanvas netCanvas) {
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu gameMenu = new JMenu("게임");
		mb.add(gameMenu);
		
		JMenuItem startItem = new JMenuItem("시작");
		JMenuItem exitItem = new JMenuItem("종료");
		gameMenu.add(startItem);
		gameMenu.add(exitItem);
		startItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Music();
				tetrisCanvas.start();
				netCanvas.start();
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tetrisCanvas.stop();
				netCanvas.stop();
			}
		});
		
		JMenu networkMenu = new JMenu("네트워크");
		mb.add(networkMenu);
		
		JMenuItem serverItem = new JMenuItem("서버 생성...");
		networkMenu.add(serverItem);
		JMenuItem clientItem = new JMenuItem("서버 접속...");
		networkMenu.add(clientItem);
		
		serverItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerDialog dialog = new ServerDialog();
				dialog.setVisible(true);
				if(dialog.userChoice == ServerDialog.Choice.OK)
				{
					TetrisServer server = new TetrisServer(dialog.getPortNumber());
					server.start();	
					serverItem.setEnabled(false);
				}
			}
		});
		
		clientItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientDialog dialog = new ClientDialog();
				dialog.setVisible(true);
				if(dialog.userChoice == ClientDialog.Choice.OK)
				{
					client = new TetrisClient(tetrisCanvas, netCanvas, netPreview, dialog.getHost(), dialog.getPortNumber());
					client.start();
					clientItem.setEnabled(false);
				}
				
			}
		});
		// 고스트블럭 색 변경
		JMenu optionMenu = new JMenu("옵션");
		mb.add(optionMenu);
		
		JMenuItem setGhostBlock = new JMenuItem("고스트블럭 색 설정");
		optionMenu.add(setGhostBlock);
		JMenuItem setTema = new JMenuItem("테트리스 테마 설정");
		optionMenu.add(setTema);
		
		
		//고스트블럭 설정할 
		/*setGhostBlock.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ServerDialog dialog = new ServerDialog();
				dialog.setVisible(true);
				if(dialog.userChoice == ServerDialog.Choice.OK)
				{
					TetrisServer server = new TetrisServer(dialog.getPortNumber());
					server.start();	
					serverItem.setEnabled(false);
				}
			}
		});
		
		setTema.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ServerDialog dialog = new ServerDialog();
				dialog.setVisible(true);
				if(dialog.userChoice == ServerDialog.Choice.OK)
				{
					TetrisServer server = new TetrisServer(dialog.getPortNumber());
					server.start();	
					serverItem.setEnabled(false);
				}
			}
		});*/
		
		
		
	}
	public void Music() {
		if(!isMusicPlaying) {
			File bgm;
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			
			bgm = new File("Music/bgmaa.wav");
			
			Clip clip;
			try {
				stream = AudioSystem.getAudioInputStream(bgm);
				format = stream.getFormat();
				info = new DataLine.Info(Clip.class, format);
				clip =(Clip)AudioSystem.getLine(info);
				
				
				clip.addLineListener(new LineListener() {
					@Override
					public void update(LineEvent event) {
						if(event.getType() == LineEvent.Type.STOP) {
							event.getLine().close();
							Music();
						}
					}
				});
				
				clip.open(stream);
				clip.start();
				isMusicPlaying = true;
				} catch (LineUnavailableException e) {
					System.out.println("LineUnavailableException: " + e.getMessage());
				} catch (UnsupportedAudioFileException e) {
					System.out.println("UnsupportedAudioFileException: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					}
			}
	}
	
//	public void playSound() {
//		if(!isMusicPlaying) {
//			File bgm1;
//			AudioInputStream stream1;
//			AudioFormat format1;
//			DataLine.Info info1;
//			
//			bgm1 = new File("effect/bottom.wav");
//			
//			Clip clip;
//			try {
//				stream1 = AudioSystem.getAudioInputStream(bgm1);
//				format1 = stream1.getFormat();
//				info1 = new DataLine.Info(Clip.class, format1);
//				clip =(Clip)AudioSystem.getLine(info1);
//				
//				
//				clip.addLineListener(new LineListener() {
//					@Override
//					public void update(LineEvent event) {
//						if(event.getType() == LineEvent.Type.STOP) {
//							event.getLine().close();
//							playSound();
//						}
//					}
//				});
//				
//				clip.open(stream1);
//				clip.start();
//				isMusicPlaying = true;
//				} catch (LineUnavailableException e) {
//					System.out.println("LineUnavailableException: " + e.getMessage());
//				} catch (UnsupportedAudioFileException e) {
//					System.out.println("UnsupportedAudioFileException: " + e.getMessage());
//				} catch (IOException e) {
//					System.out.println("IOException: " + e.getMessage());
//				} catch (Exception e) {
//					System.out.println("Error: " + e.getMessage());
//					}
//			}
//	}
	

	


	
	public static void main(String[] args) {
		new MyTetris();
	}

	public void refresh() {
		if(client != null)
			client.send();
	}

}
