package Tetris1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class TetrisClient extends Thread {
	private String host;
	private int port;
	private  BufferedReader  i;
    private  PrintWriter     o;
    private TetrisNetworkCanvas netCanvas;
    private TetrisCanvas tetrisCanvas;
    private TetrisNetworkPreview netPreview;
    private String key;
    private Piece save;
    

    
    public TetrisClient(TetrisCanvas tetrisCanvas, TetrisNetworkCanvas netCanvas, TetrisNetworkPreview netPreview, String host, int port) {
    	this.tetrisCanvas = tetrisCanvas;
    	this.netCanvas = netCanvas;
    	this.netPreview = netPreview;
    	this.host = host;
    	this.port = port;
    
    	// 상대방 세이브 도형 임의 데이터 입력
    	save = new Bar(new TetrisData());
    	
    	UUID uuid = UUID.randomUUID();
		key = uuid.toString()+";";
		System.out.println("My key: "+key);
    }
    
    public void send() {
    	String data = tetrisCanvas.getData().saveNetworkData();
    	int newBlock = tetrisCanvas.getNewBlock().getType();
		System.out.println("send: "+data);
		String current = tetrisCanvas.current.extracter();
		int score = tetrisCanvas.getData().score;// 추가
		int line = tetrisCanvas.getData().line;
		String save_s;//추가
		//String level_s = Constant.getLevel(); //상대방 레벨 보이기
		if(tetrisCanvas.save != null) {// 추가
			save_s = tetrisCanvas.save.extracter();
			o.println(key+data+";"+newBlock+";"+current+";"+score+";"+line+";"+save_s);
		} else {
			o.println(key+data+";"+newBlock+";"+current+";"+score+";"+line);
		}
    }
    
    
	public void run() {
		System.out.println("client start!");
		Socket s;
		try {
			s = new Socket(host, port);
			InputStream ins = s.getInputStream();
			OutputStream os = s.getOutputStream();
			i = new BufferedReader(new InputStreamReader(ins));
			o = new PrintWriter(new OutputStreamWriter(os), true);
			
			while (true) {
				String line = i.readLine();
				if(line.length() != 0)
				{
					String[] parsedData = line.split(";");
					String checkKey = parsedData[0]+";";
					if(!checkKey.equals(key) && parsedData.length > 1) {
						netCanvas.getData().loadNetworkData(parsedData[1]);
						switch(Integer.parseInt(parsedData[2])) {
						case 1: //bar
							netPreview.setnewBlock(new Bar(new TetrisData()));
							break;
						case 2: //Tee
							netPreview.setnewBlock(new Tee(new TetrisData()));
							break;
						case 3: //El
							netPreview.setnewBlock(new El(new TetrisData()));
							break;
						case 4: //Er
							netPreview.setnewBlock(new Er(new TetrisData()));
							break;
						case 5: //O
							netPreview.setnewBlock(new O(new TetrisData()));
							break;
						case 6: //S
							netPreview.setnewBlock(new S(new TetrisData()));
							break;
						case 7: //Z
							netPreview.setnewBlock(new Z(new TetrisData()));
							break;
						default:
							netPreview.setnewBlock(null);
							break;
						}
						Piece tmpP = new Bar(new TetrisData());
						tmpP.combinator(parsedData[3]);
						netCanvas.setCurrent(tmpP);
						
						//score 받음
						netCanvas.data.score=Integer.parseInt(parsedData[4]);
						//상대방 레벨 보이기
						//Constant.level=(parsedData[5]);
						// 상대방 지운 라인 보이기
						netCanvas.data.line=Integer.parseInt(parsedData[5]);
						
						if(parsedData.length == 7) { // 세이브 도형 받음
							save.combinator(parsedData[6]);
							netPreview.setNetSaveBlock(save);
						}
						//netPreview.setNetSaveBlock(tmp);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
