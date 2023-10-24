package Tetris1;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JColorChooser;
import java.awt.Color;

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

//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;

public class MyTetris extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TetrisClient client = null;
	private boolean isMusicPlaying = false;
	private TetrisNetworkPreview netPreview = null;
	private SoundManager soundManager;
	private MyTetris myTetris;
	private StartCanvas startCanvas;
	
	
	public MyTetris(SoundManager soundManager) {
		setTitle("테트리스");
		setSize(1280, 650);
		
		
		this.soundManager = soundManager;
		
		GridLayout layout = new GridLayout(1,4);
		setLayout(layout);
		TetrisCanvas tetrisCanvas = new TetrisCanvas(this,soundManager);
		TetrisNetworkCanvas netCanvas = new TetrisNetworkCanvas();
		
		TetrisPreview preview = new TetrisPreview(tetrisCanvas.getData());
		TetrisNetworkPreview netPreview = new TetrisNetworkPreview(netCanvas.getData());
		tetrisCanvas.setTetrisPreview(preview);
		createMenu(tetrisCanvas, preview ,netCanvas ,netPreview );
		//netCanvas.setTetrisPreview(netPreview);
		add(tetrisCanvas);
		add(preview);
		add(netCanvas);
		add(netPreview);

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pack();
		setVisible(true);
		
		Timer musicTimer = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isMusicPlaying) {
					//Music();
				}
			}
		});
		musicTimer.start();
	}
	
	public void createMenu(TetrisCanvas tetrisCanvas,TetrisPreview preview, TetrisNetworkCanvas netCanvas, TetrisNetworkPreview netPreview) {
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
            	soundManager.setMusic("Music/bgm.wav");
            	soundManager.play();
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
		JMenuItem setTheme = new JMenuItem("테트리스 테마 설정");
		optionMenu.add(setTheme);
		JMenuItem musicSet = new JMenuItem("음악 매니저");
		optionMenu.add(musicSet);
//		JMenuItem setBackgroundColorItem = new JMenuItem("배경색 설정");
//		optionMenu.add(setBackgroundColorItem);
//		JMenuItem setShapeColorItem = new JMenuItem("도형 색상 설정");
//		optionMenu.add(setShapeColorItem);
//		
		
		//고스트블럭 설정할 
		setGhostBlock.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	GhostDialog ghostDialog = new GhostDialog(MyTetris.this, tetrisCanvas);
		    	ghostDialog.setVisible(true);
		    	
		    	if(ghostDialog.getChoice()==GhostDialog.Choice.OK) {
		    		Color ghostBlockColor = ghostDialog.getGhostBlockColor();
		    	}
		    	
		    	
//		        // 사용자에게 색상을 선택하도록 다이얼로그 표시
//		        Color selectedColor = JColorChooser.showDialog(MyTetris.this, "고스트 블록 색상 선택", tetrisCanvas.getGhostBlockColor());
//
//		        // 사용자가 색상을 선택하지 않고 취소한 경우, 선택된 색상이 null이 될 수 있음
//		        if (selectedColor != null) {
//		            // 선택한 색상을 TetrisCanvas로 전달하여 설정
//		            tetrisCanvas.setGhostBlockColor(selectedColor);
//		        }
		    }
		});
		
		setTheme.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // 테마 설정 다이얼로그를 표시
		        ThemeDialog themeDialog = new ThemeDialog(MyTetris.this, tetrisCanvas, preview, netCanvas, netPreview);
		        themeDialog.setVisible(true);

		        // 사용자가 테마를 설정하고 확인 버튼을 클릭한 경우, 색상을 가져와 테마를 변경
		        if (themeDialog.getChoice() == ThemeDialog.Choice.OK) {
		            Color backgroundColor = themeDialog.getBackgroundColor();
		            Color shapeColor = themeDialog.getShapeColor();
		            Color canvasColor = themeDialog.getcanvasColor();

		            // TetrisCanvas의 배경색 및 도형 색상 설정
		            tetrisCanvas.setBackground(backgroundColor);
		           // tetrisCanvas.getNewBlock().setColor(shapeColor);
		            preview.setBackground(backgroundColor);
		            netCanvas.setBackground(backgroundColor);
		            netPreview.setBackground(backgroundColor);
		            // 여기에서 줄의 색상 및 다른 구성 요소의 테마 설정을 추가
		            // 예: tetrisCanvas.getData().setLineColor(lineColor);

		            // 변경된 테마를 TetrisCanvas에 반영
		            tetrisCanvas.repaint();
		        }
		    }
		});
		musicSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSoundControlDialog();
            }
        });

//		setBackgroundColorItem.addActionListener(new ActionListener() {
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		        // 여기에 배경색 설정 다이얼로그를 띄우고 사용자에게 배경색 설정을 가능하게 하는 코드를 추가
//		        // 예를 들어, 다이얼로그에서 배경색을 선택하고 선택한 색상을 적용하는 코드를 추가
//		    }
//		});
//
//		setShapeColorItem.addActionListener(new ActionListener() {
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		        // 여기에 도형 색상 설정 다이얼로그를 띄우고 사용자에게 도형 색상 설정을 가능하게 하는 코드를 추가
//		        // 예를 들어, 다이얼로그에서 도형 색상을 선택하고 선택한 색상을 적용하는 코드를 추가
//		    }
//		});
		
//		setTema.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				ServerDialog dialog = new ServerDialog
//				dialog.setVisible(true);
//				if(dialog.userChoice == ServerDialog.Choice.OK)
//				{
//					TetrisServer server = new TetrisServer(dialog.getPortNumber());
//					server.start();	
//					serverItem.setEnabled(false);
//				}
//			}
//		});
//		
		
		
	}
    private void showSoundControlDialog() {
    //    SoundManager soundManager = new SoundManager(); // Replace with your SoundManager instantiation code
        SoundControlDialog soundControlDialog = new SoundControlDialog(this, soundManager, startCanvas);
        soundControlDialog.pack();
        soundControlDialog.setLocationRelativeTo(this);
        soundControlDialog.setVisible(true);
    }
//	public void Music() {
//		if(!isMusicPlaying) {
//			File bgm;
//			AudioInputStream stream;
//			AudioFormat format;
//			DataLine.Info info;
//			
//			bgm = new File("Music/bgm.wav");
//			
//			Clip clip;
//			try {
//				stream = AudioSystem.getAudioInputStream(bgm);
//				format = stream.getFormat();
//				info = new DataLine.Info(Clip.class, format);
//				clip =(Clip)AudioSystem.getLine(info);
//				
//				
//				clip.addLineListener(new LineListener() {
//					@Override
//					public void update(LineEvent event) {
//						if(event.getType() == LineEvent.Type.STOP) {
//							event.getLine().close();
//							Music();
//						}
//					}
//				});
//				
//				clip.open(stream);
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
	
	public void playSound() {
	    File bgm1;
	    AudioInputStream stream1;
	    AudioFormat format1;
	    DataLine.Info info1;

	    bgm1 = new File("effect/bottom.wav");

	    Clip clip;
	    try {
	        stream1 = AudioSystem.getAudioInputStream(bgm1);
	        format1 = stream1.getFormat();
	        info1 = new DataLine.Info(Clip.class, format1);
	        clip = (Clip) AudioSystem.getLine(info1);

	        clip.addLineListener(new LineListener() {
	            @Override
	            public void update(LineEvent event) {
	                if (event.getType() == LineEvent.Type.STOP) {
	                    event.getLine().close();
	                }
	            }
	        });

	        clip.open(stream1);
	        clip.start();
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

	

	
	

	


	
//	public static void main(String[] args) {
//		new MyTetris();
//	}

	public void refresh() {
		if(client != null)
			client.send();
	}

}