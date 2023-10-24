package Tetris1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Clip;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;




public class SoundControlDialog extends JDialog {
    private SoundManager soundManager;
	public Clip clip;
	private FloatControl volumeControl;
	private StartCanvas startCanvas;

    public SoundControlDialog(JFrame parent, SoundManager soundManager,StartCanvas startCanvas) {
        super(parent, "사운드 매니저", true);
        this.soundManager = soundManager;
        this.startCanvas = startCanvas;
        setLayout(new FlowLayout());

        JButton playMusic = new JButton("음악 시작");
        playMusic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	soundManager.setMusic("Music/bgm.wav");
            	soundManager.play();
            }
        });

        JButton stopMusic = new JButton("음악 정지");
        stopMusic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundManager.stop();
            }
        });
        
       
        JButton setVolumeButton = new JButton("볼륨 조절");
        setVolumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showVolumeControlDialog();
                
            }
        });
        
        
        JButton selectFileButton = new JButton("파일 선택");
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(SoundControlDialog.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    soundManager.setMusic(filePath);
                }
            }
        });

        add(playMusic);
        add(stopMusic);
        add(setVolumeButton);
        add(selectFileButton);
    }

    private void showVolumeControlDialog() {
        if (soundManager != null) {
            // 볼륨 조절 다이얼로그를 생성
            JDialog volumeDialog = new JDialog(this, "볼륨 조절", true);
            volumeDialog.setLayout(new FlowLayout());

            // 볼륨 슬라이더 생성
            JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (soundManager.getVolume() * 100));
            volumeSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int value = volumeSlider.getValue();
                    float volume = value / 100.0f;
                    soundManager.setVolume(volume);
                }
            });

            volumeDialog.add(volumeSlider);
            volumeDialog.pack();
            volumeDialog.setLocationRelativeTo(this);
            volumeDialog.setVisible(true);
        }
    }
    
    public Clip getClip() {
        return clip;
    }

    public FloatControl getVolumeControl() {
        return volumeControl;
    }
}
