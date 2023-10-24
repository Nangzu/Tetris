package Tetris1;

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

public class TetrisData {
	public static final int ROW = 20;
	public static final int COL = 10;
	private int data[][];  // ROW x COL 의 배열
	public  int line;      // 지운 줄 수
	//private int grade;     // 단계
	
	public int score=0;
	private MyTetris myTetris;
	
	
	
	public TetrisData() {


		data = new int[ROW][COL];
		clear();
	}
	
	public int getAt(int x, int y) {
		if(x <0 || x >= ROW || y < 0 || y >= COL)
			return 0;
		return data[x][y];
	}
	
	public void setAt(int x, int y, int v) {
		if (x >= 0 && x < ROW && y >= 0 && y < COL) {
		data[x][y] = v;
	}
	}
	
	public int getLine() {
		return line;
	}
	public void resetLine() {
		line =0;
	}
	public synchronized void removeLines() {
		NEXT:
		for(int i = ROW-1; i >= 0; i--){
			boolean done = true;
			for(int k = 0; k < COL; k++){
				if(data[i][k] == 0) {
					done = false;
					continue NEXT;
				}
			}
			if(done){
				line++;
				for(int x = i; x > 0; x--) {
					for(int y = 0; y < COL; y++){
						data[x][y] = data[x-1][y];
					}
				}
				if(i != 0){
					for(int y = 0; y < COL; y++){
						data[0][y] = 0;
					}
				}
				i++; // 지워질 라인이 두줄 이상일때 맨 아랫줄 윗칸이 지워지지 않는 버그 수정
				score+=100;
				levelup();
				crashsound();
			}
		}
	}
	/* grade값 반환
	public int levelset(){
		return grade;
	}*/
	public void levelup() { // 최대 10레벨까지
		if(Constant.getLevel()<11) {
			if(score % 100 ==0) {
				Constant.increaseLevel(1);
				//grade ++;
			}
		}
	}
	/* grade 레벨 리셋
	public void resetgrade() {
		grade =0;
	}*/
	
	
	public void clear() {   // data 배열 초기화
		for(int i=0; i < ROW; i++){
			for(int k = 0; k < COL; k++){
				data[i][k] = 0;
			}
		}
	}
	
	public void dump() {   // data 배열 내용 출력
		for(int i=0; i < ROW; i++) {
			for(int k = 0; k < COL; k++) {
				System.out.print(data[i][k] + " ");
			}
			System.out.println();
		}
	}
	
	public void loadNetworkData(String input) {
		clear();
		String[] dataArray = input.split(",");
		int count = 0;
		for(int i = 0; i < TetrisData.ROW; i++) {
			for(int k = 0; k < TetrisData.COL; k++) {
				setAt(i, k, Integer.parseInt(dataArray[count]));
				count++;
			}
		}
	}
	
	public String saveNetworkData() {
		StringBuilder output = new StringBuilder("");
		for(int i = 0; i < TetrisData.ROW; i++) {
			for(int k = 0; k < TetrisData.COL; k++) {
				String data = String.valueOf(getAt(i, k));
				output.append(data+",");
			}
		}
		String result = output.toString();
		return result;
	}
	
	public void crashsound() {
	    File bgm1;
	    AudioInputStream stream1;
	    AudioFormat format1;
	    DataLine.Info info1;

	    bgm1 = new File("effect/crashSound.wav");

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
	
}
