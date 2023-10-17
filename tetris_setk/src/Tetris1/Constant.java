package Tetris1;

import java.awt.Color;

public class Constant {
	public static final int w = 25;
	public static final int margin = 20;
	public static final int interval = 2000;
	public static int     	level = 1;
	private static Color[] colors = null;  // 테트리스 배경 및 조각 색
	
	public static Color getColor(int index) {
		if(colors == null) {
			colors = new Color[8];
			colors[0] = new Color(80, 80, 80); // 배경색(검은회색)
			colors[1] = new Color(255, 0, 0); //빨간색
			colors[2] = new Color(0, 255, 0); //녹색
			colors[3] = new Color(0, 200, 255); //노란색
			colors[4] = new Color(255, 255, 0); //하늘색
			colors[5] = new Color(255, 150, 0); //황토색
			colors[6] = new Color(210, 0, 240); //보라색
			colors[7] = new Color(40, 0, 240); //파란색
		}
		return colors[index];
	}
	
	public static int getLevel() {
		return level;
	}
	
	public static void increaseLevel(int amount) {
		level += amount;
	}
	
	public static void resetlevel() {
		level = 1;
	}
	
}
