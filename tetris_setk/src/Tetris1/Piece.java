package Tetris1;

import java.awt.Point;
import java.io.Serializable;

import Tetris1.TetrisData;

public abstract class Piece implements Serializable {
	final int DOWN = 0;  // 방향 지정
	final int LEFT = 1;
	final int RIGHT = 2;
	protected int r[];   // Y축 좌표 배열
	protected int c[];   // X축 좌표 배열
	protected TetrisData data;  // 테트리스 내부 데이터
	protected Point center; // 조각의 중심 좌표
	protected int type;
	protected int roteType;
	
	
	public Piece(TetrisData data, int type , int roteType) {
		r = new int[4];
		c = new int[4];
		this.data = data;
		this.type = type;
		this.roteType = roteType;
		center = new Point(5,0);
	}
	public int getType() {return type;}
	public int roteType() {return roteType;}
 
	public int getX() { return center.x; }
	public int getY() { return center.y; }
	public boolean copy(){  // 값 복사
		boolean value = false;
		int x = getX();
		int y = getY();
		if(getMinY() + y <= 0) { // 게임 종료 상황
			value = true;
		}
 
		for(int i=0; i < 4; i++) {
			data.setAt(y + r[i], x + c[i], getType());
		}
		return value;
	}
 
	public boolean isOverlap(int dir){ // 다른 조각과 겹치는지 파악
		int x = getX();
		int y = getY();
		switch(dir) {
		case 0 : // 아래
			for(int i=0; i < r.length; i++) {
				if(data.getAt(y+r[i]+1, x+c[i]) != 0) {
					return true;
				}
			}
			break;
		case 1 : // 왼쪽
			for(int i=0; i < r.length; i++) {
				if(data.getAt(y+r[i], x+c[i]-1) != 0) {
					return true;
				}
			}
			break;
		case 2 : // 오른쪽
			for(int i=0; i < r.length; i++) {
				if(data.getAt(y+r[i], x+c[i] + 1) != 0) {
					return true;
				}
			}
			break;
		}
		return false;
	}
 
	public int getMinX() {
		int min = c[0];
		for(int i=1; i < c.length; i++) {
			if(c[i] < min) {
				min = c[i];
			}
		}
		return min;
	}
 
	public int getMaxX() {
		int max = c[0];
		for(int i=1; i < c.length; i++) {
			if(c[i] > max) {
				max = c[i];
			}
		}
		return max;
	}
 
	public int getMinY() {
		int min = r[0];
		for(int i=1; i < r.length; i++) {
			if(r[i] < min) {
				min = r[i];
			}
		}
		return min;
	}
 
	public int getMaxY() {
		int max = r[0];
		for(int i=1; i < r.length; i++) {
			if(r[i] > max) {
				max = r[i];
			}
		}
		return max;
	}
	
	

	public boolean moveDown() { // 아래로 이동
		if(center.y + getMaxY() + 1 < TetrisData.ROW) {
			if(isOverlap(DOWN) != true) {
				center.y++;
			} else {
				return true;
			}
		} 
		else { return true; }

		return false;
	}

	public void moveLeft() {  // 왼쪽으로 이동
		if(center.x + getMinX() -1 >= 0)
			if(isOverlap(LEFT) != true) {center.x--;}
			else return;
	}

	public void moveRight() {  // 오른쪽으로 이동
		if(center.x + getMaxX() + 1 < TetrisData.COL)
			if(isOverlap(RIGHT) != true) {center.x++;}
			else return;
	}

	public void rotate() {
	    int rc = roteType();
	    if (rc <= 1) return;

	    int[] currentR = r.clone();
	    int[] currentC = c.clone();
	    int currentX = getX();

	    // 시도할 회전
	    rotate4();

	    // 화면을 벗어난 경우
	    if (isOutOfPiece()) {
	        int newX = currentX;
	        if (currentX < 0) {
	            // 왼쪽 벽을 벗어난 경우, 오른쪽으로 밀어넣기
	            newX -= currentX;
	        } else if (currentX + getMaxX() >= TetrisData.COL) {
	            // 오른쪽 벽을 벗어난 경우, 왼쪽으로 밀어넣기
	            newX -= currentX + getMaxX() - TetrisData.COL + 1;
	        }
	        center.x = newX;

	        // 다시 회전을 시도
	        rotate4();

	        // 다시 회전한 후 범위를 벗어나거나 다른 블록과 겹치는 경우
	        if (isOutOfPiece() || isOverlap(3)) {
	            // 원래대로 돌려놓기
	            r = currentR;
	            c = currentC;
	            center.x = currentX;
	        }
	    } else if (isOverlap(3)) {
	        // 회전한 후 다른 블록과 겹치는 경우
	        // 원래대로 돌려놓기
	        r = currentR;
	        c = currentC;
	        center.x = currentX;
	        
	    }
	    checkDis(currentR, currentC);
	}
	private void checkDis(int[] tempR, int[] tempC) {
	    int minX = getMinX();
	    int maxX = getMaxX();
	    int minY = getMinY();
	    int maxY = getMaxY();

	    for (int i = minX; i <= maxX; i++) {
	        for (int j = minY; j <= maxY; j++) {
	            if (!isOverlap(3)) {
	            	return;
	            }
	        }
	    }
	    
		r = tempR;
		c = tempC;
	}

	private boolean isOutOfPiece() {
		int tmpR; int tmpC;
	    for (int i = 0; i < this.r.length; i++) {
	        tmpR = getY() + r[i];
	        tmpC = getX() + c[i];
	        if(tmpR >= TetrisData.ROW || tmpC < 0 || tmpC >= TetrisData.COL) {
	        	return true;
	        }
	    }
	    return false;
	}
	
	
	
	/*private boolean blmetbl() {
		int x = center.x;
        int y = center.y;
        
        for (int i = 0; i < r.length; i++) {
            int newX = x + c[i];
            int newY = y + r[i];

            if (newX >= 0 && newX < TetrisData.COL && newY >= 0 && newY < TetrisData.ROW) {
                if (data.getAt(newY, newX) != 0) {
                    //회전된 도형이 블록과 겹침
                    return true;
                }
            }
        }
        
        return false;
	}*/

	public void rotate4() {   // 조각 회전
		for(int i = 0; i < 4; i++) {
			int temp = c[i];
			c[i] = -r[i];
			r[i] = temp;
		}
	}
	//추가
	public void PieceGhost(Piece PieceGhost) {
		PieceGhost.r = this.r;
		PieceGhost.c = this.c;
		PieceGhost.center.x=this.center.x;
		PieceGhost.center.y=this.center.y;
		//PieceGhost.type = this.getType();
		while(!PieceGhost.moveDown()) {}
	}
	// 추가
	public void save(Piece save) {
		save.r=this.r;
		save.c = this.c;
		save.type = this.getType();
		save.roteType=this.roteType();

	}
	
	
	
	public String dataToString() {
		String str =
		r[0] + "." + r[1] + "." + r[2] + "." + r[3] + "." +
		c[0] + "." + c[1] + "." + c[2] + "." + c[3] + "." +
		center.x + "." + center.y + "." + getType();
		//0~3 r
		//4~7 c
		//8~9 center x, y
		return str;
	}
	
	public void stringToData(String str) {
		String[] fixedStr = str.split("\\.");
		for(int i = 0; i < 11; i++) {
			switch(i/4) {
			case 0:
				r[i] = Integer.parseInt(fixedStr[i]);
				break;
			case 1:
				c[i%4] = Integer.parseInt(fixedStr[i]);
				break;
			case 2:
				if(i%4 == 0) {
					center.x = Integer.parseInt(fixedStr[i]);
				} else if(i%4 == 1){
					center.y = Integer.parseInt(fixedStr[i]);
				} else {
					type = Integer.parseInt(fixedStr[i]);
				}
				break;
			}
		}
		
	}
	public void resetPositionPiece() {
		center.x =5;
		center.y= -1;
	}
	
}
