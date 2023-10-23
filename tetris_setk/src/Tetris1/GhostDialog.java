package Tetris1;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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

import Tetris1.ThemeDialog.Choice;

import javax.swing.JDialog;
import javax.swing.JButton;


public class GhostDialog extends JDialog {
    private Choice userChoice;
    private Color backgroundColor;
    private Color shapeColor;
    private Color canvasColor;
    private Color ghostBlockColor;
    private TetrisCanvas tetrisCanvas;
//    private TetrisPreview tetrisPreview;
//    private TetrisNetworkCanvas netCanvas;
//    private TetrisNetworkPreview netPreview;

    public enum Choice { OK, CANCEL }

    public GhostDialog(JFrame parent, TetrisCanvas tetrisCanvas) {
    	
        super(parent, "고스트블럭 색 설정", true);
        this.tetrisCanvas = tetrisCanvas;
//        this.tetrisPreview = tetrisPreview;
//        this.netCanvas =netCanvas;
//        this.netPreview = netPreview;
    	userChoice = null;
        setLayout(new FlowLayout());
        
        

        // 고스트블럭 색 설정
        JButton ghostblockColorset = new JButton("고스트블럭 색 설정");
        ghostblockColorset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ghostBlockColor = JColorChooser.showDialog(GhostDialog.this, "배경색 선택", ghostBlockColor);
                tetrisCanvas.setGhostBlockColor(ghostBlockColor); // TetrisCanvas에 배경색 설정

            }
        });


        // 초기화 버튼
        JButton resetButton = new JButton("초기화");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisCanvas.resetghostColor(ghostBlockColor); // TetrisCanvas의 테마 초기화

            }
        });

        // 확인 및 취소 버튼
        JButton okButton = new JButton("확인");
        JButton cancelButton = new JButton("닫기");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userChoice = Choice.OK;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userChoice = Choice.CANCEL;
                dispose();
            }
        });

        // 버튼을 다이얼로그에 추가
        add(ghostblockColorset);
        add(resetButton);
        add(okButton);
        add(cancelButton);
        

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
   

    public Choice getChoice() {
        return userChoice;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getShapeColor() {
        return shapeColor;
    }
    
    public Color getcanvasColor() {
    	return canvasColor;
    }
    
    public Color getGhostBlockColor() {
    	return ghostBlockColor;
    }
   }
