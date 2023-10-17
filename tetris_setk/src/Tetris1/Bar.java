package Tetris1;

public class Bar extends Piece {
	public Bar(TetrisData data) {
		  super(data, 1,2);
			c[0] = 1;		r[0] = 0;
			c[1] = 0;		r[1] = 0;
			c[2] = -1;		r[2] = 0;
			c[3] = -2;		r[3] = 0;
	}
	/* 블럭의 색깔 */
/*	public int getType() {  
		return 1;
	}
	/*블럭의 회전*/
/*public int roteType() {
		return 2;
	}*/
}
