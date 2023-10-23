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
import javax.swing.JDialog;
import javax.swing.JButton;

public class ThemeDialog extends JDialog {
    private Choice userChoice;
    private Color backgroundColor;
    private Color shapeColor;
    private TetrisCanvas tetrisCanvas;

    public enum Choice { OK, CANCEL }

    public ThemeDialog(JFrame parent) {
    	
        super(parent, "테트리스 테마 설정", true);
        userChoice = null;
        setLayout(new FlowLayout());
        
        

        // 배경색 선택 버튼
        JButton backgroundColorButton = new JButton("배경색 선택");
        backgroundColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backgroundColor = JColorChooser.showDialog(ThemeDialog.this, "배경색 선택", backgroundColor);
                tetrisCanvas.setBackgroundColor(backgroundColor); // TetrisCanvas에 배경색 설정
            }
        });

        // 도형 색상 선택 버튼
        JButton shapeColorButton = new JButton("도형 색상 선택");
        shapeColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeColor = JColorChooser.showDialog(ThemeDialog.this, "도형 색상 선택", shapeColor);
                tetrisCanvas.setShapeColor(shapeColor); // TetrisCanvas에 도형 색상 설정
            }
        });

        // 확인 및 취소 버튼
        JButton okButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");

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
        add(backgroundColorButton);
        add(shapeColorButton);
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
}