package Tetris1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StartCanvas extends JFrame {
    public   Clip clip;
	private StartCanvas startCanvas;
//	private SoundManager clip;
	
	private SoundManager soundManager ;
	
	
    public StartCanvas(SoundManager soundManager) {
        setTitle("Tetris Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        this.soundManager=soundManager;
        
        
        
        JLabel label = new JLabel("테트리스에 어서 오세요!!");
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("시작하기");
        JButton exitButton = new JButton("나가기");
        JButton soundControlButton = new JButton("소리 매니저");
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(soundControlButton);
        

        add(buttonPanel, BorderLayout.SOUTH);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	soundManager.stop();
            	
                dispose();
                new MyTetris(soundManager).setVisible(true);
            }
            
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundManager.stop();
                System.exit(0);
            }
        });
        
        soundControlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSoundControlDialog();
            }
        });

        soundManager.play();

        setLocationRelativeTo(null);
    }


//    public void stopMusic() {
//        if (clip != null) {
//            clip.stop();
//            clip.close();
//        }
//    }
    
    private void showSoundControlDialog() {
    //    SoundManager soundManager = new SoundManager(); // Replace with your SoundManager instantiation code
        SoundControlDialog soundControlDialog = new SoundControlDialog(this, soundManager, startCanvas);
        soundControlDialog.pack();
        soundControlDialog.setLocationRelativeTo(this);
        soundControlDialog.setVisible(true);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SoundManager soundManager = new SoundManager();
                StartCanvas startCanvas = new StartCanvas(soundManager);
                startCanvas.setVisible(true);
            }
        });
    }
    public Clip getClip() {
        return clip;
    }
    
    public void stop() {
		if(clip !=null)
		{
			clip.stop();
			clip.setFramePosition(0); // 재생 위치를 처음으로
			clip.close();
		}
    	
    }
    
    
}