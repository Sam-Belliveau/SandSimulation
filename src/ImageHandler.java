import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public final class ImageHandler extends JFrame {
	
	public boolean[][] world = new boolean[500][500];

	public JLabel outImage = new JLabel();
	
	public BufferedImage boardImage;
	
	public int[] roundedNumbers;
	
	public final int speedMultiplier = 5;
	
	public final int worldSize = 720;
	
	public final int imageSize = 720;
	
	public final double brushSize = 72;
	
	private int roundX, roundY;
	
	private boolean temp;
	
	private boolean bl, br;
	
	private boolean[][] movedFrom = new boolean[worldSize][worldSize];
	
	private final int Red = (new Color(255, 0, 0)).getRGB();
	private final int Background = (new Color(0, 128, 255)).getRGB();
	private final int Front = (new Color(255, 128, 0)).getRGB();
	
	public boolean playing = false;
	
	public void reset(){
		//System.setProperty("sun.java2d.opengl", "true");
		movedFrom = new boolean[worldSize][worldSize];
		boardImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		outImage.setPreferredSize(new Dimension(imageSize, imageSize));
		
		playing = false;
		
		roundedNumbers = new int[imageSize];
		
		world = new boolean[worldSize][worldSize];
		
		setSize(imageSize+10,imageSize+35);
		outImage.setBounds(0, 0, imageSize, imageSize);
		
		final int ratio = (imageSize/worldSize);
		for(int n = 0; n < imageSize; n++){
				roundedNumbers[n] = Math.round(n/ratio);
		}
	}
	
	public ImageHandler(){
		world = new boolean[worldSize][worldSize];
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);
		setTitle("Gravity!");
		getContentPane().setLayout(null);
		setSize(0,0);
		outImage.setBounds(0, 0, imageSize, imageSize);
		getContentPane().add(outImage);
		outImage.setMinimumSize(new Dimension(imageSize, imageSize));
		pack();
		setResizable(false);
		addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                reset();
                playing = true;
            }
		});
            
		getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	boolean ptemp = playing;
            	playing = false;
            	
            	roundX = Math.round(e.getX()/((imageSize/worldSize)));
            	roundY = Math.round(e.getY()/((imageSize/worldSize)));
                try{
                	for(double i = -brushSize; i < brushSize; i++){
        				for(double ii = -brushSize; ii < brushSize; ii++){
        					try{
        						if((i*i)+(ii*ii)<(brushSize*brushSize)){
        							world[(int) (roundX+i)][(int) (roundY+ii)] = !world[(int) (roundX+i)][(int) (roundY+ii)];
        						}
        					} catch (Exception q){}
                    	}
                	}
                } catch (ArrayIndexOutOfBoundsException a) {}
            	
            	playing = ptemp;
            }
            
            public void mouseReleased(MouseEvent e) {}
        });
	}
	
	public void paint(){
		if(playing){
			for(int i = 0; i < speedMultiplier; i ++){
				update();
			}
		}
		
		for(int x = 0; x < imageSize; x++){
			for(int y = 0; y < imageSize; y++){
				try{
					temp = world[roundedNumbers[x]][roundedNumbers[y]];
					if(temp){
						boardImage.setRGB(x, y, Front);
					} else {
						boardImage.setRGB(x, y, Background);
					}	
				}catch(Exception e){ boardImage.setRGB(x, y, Red); }
			}
		}
		outImage.setIcon(new ImageIcon(boardImage));
		
	}

	public void update(){
		for(int x = 0; x < worldSize; x++){
			for(int y = worldSize-1; y > -1; y--){
				movedFrom[x][y] = true;
				if(world[x][y]){
					try{
						if(!world[x][y+1] && movedFrom[x][y+1]){
							try{
								movedFrom[x][y] = false;
								world[x][y] = false;
								world[x][y+1] = true;
							} catch(Exception e){}
						} else {
							bl = true; br = true;
							try{
								if(!world[x-1][y] && !world[x-2][y]){
									bl = world[x-1][y+1];
								}
							} catch(Exception e){
								try{
									if(!world[x-1][y]){
										bl = world[x-1][y+1];
									}
								} catch(Exception q){}
							}
							try{
								if(!world[x+1][y] && !world[x+2][y]){
									br = world[x+1][y+1];
								} 
							} catch(Exception e){
								try{
									if(!world[x-1][y]){
										bl = world[x-1][y+1];
									}
								} catch(Exception q){}
							}
							
							if(!bl && br){
								world[x][y] = false;
								world[x-1][y+1] = true;
							} else if (!br && bl) {
								world[x][y] = false;
								world[x+1][y+1] = true;
							}
						}
					} catch(Exception e){}
				}
			}
		}
	}
}
