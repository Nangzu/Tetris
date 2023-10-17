package Tetris1;
public class TetrisData {
	public static final int ROW = 20;
	public static final int COL = 10;
	private int data[][];  // ROW x COL 의 배열
	public  int line;      // 지운 줄 수
	//private int grade;     // 단계
	
	public int score=0;
	
	
	
	
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
				i++; // 추가
				score+=100;
				levelup();

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
	
}
