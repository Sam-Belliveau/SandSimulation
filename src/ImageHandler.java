import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

@SuppressWarnings("serial")
public final class ImageHandler extends JFrame {

	public final JLabel outImage = new JLabel();
	
	public BufferedImage boardImage;
	
	public int[] roundedNumbers;
	
	public final int worldSize = 720;
	
	public final int brushSize = 72;
	
	public boolean[][] world = new boolean[worldSize][worldSize];
	
	private final Color Background =  new Color(0, 128, 255);
	private final int Clear = (new Color(0, 128, 255)).getRGB() & (0 << 24) | 0x00ffffff;
	private final int Front = (new Color(255, 128, 0)).getRGB() & ((255 << 24) | 0x00ffffff);
	private final int Black = (new Color(0, 0, 0)).getRGB() & ((255 << 24) | 0x00ffffff);
	
	public boolean playing = false;
	
	public void reset(){
		//System.setProperty("sun.java2d.opengl", "true");
		boardImage = new BufferedImage(worldSize, worldSize, BufferedImage.TYPE_INT_ARGB);
		outImage.setPreferredSize(new Dimension(worldSize, worldSize));
		
		playing = false;
		
		world = new boolean[worldSize][worldSize];
		setResizable(true);
		
		getContentPane().setMinimumSize(new Dimension(worldSize, worldSize));
		outImage.setBounds(20, 20, worldSize, worldSize);
		outImage.setMinimumSize(new Dimension(worldSize, worldSize));
		
		setResizable(false);
		
		for(int x = 0; x < worldSize; x++){
			for(int y = 0; y < worldSize; y++){
				boardImage.setRGB(x, y, Clear);
				if(!(y < worldSize-5 && y > 4 && x < worldSize-5 && x > 4)){
					boardImage.setRGB(x, y, Black);
				}
			}
		}
	}
	
	public ImageHandler(){
		world = new boolean[worldSize][worldSize];
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);
		setTitle("Gravity!");
		getContentPane().setLayout(null);
		setSize(worldSize+45,worldSize+75);
		getContentPane().setBackground(Background);
		
		outImage.setBounds(20, 20, worldSize, worldSize);
		outImage.setMinimumSize(new Dimension(worldSize, worldSize));
		getContentPane().add(outImage);
		
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
            	try{
	            	boolean draw = !world[e.getX()-20][e.getY()-20];
	            	playing = false;
	            	for(int i = -brushSize; i < brushSize; i++){
	    				for(int ii = -brushSize; ii < brushSize; ii++){
	    					try{
	    						if((i*i)+(ii*ii)<(brushSize*brushSize)){
	        	    				if(!draw){
	        	    					world[e.getX()+i-20][e.getY()+ii-20] = false;
	        	    					boardImage.setRGB(e.getX()+i-20, e.getY()+ii-20, Clear);
	        	    				} else {
	        	    					world[e.getX()+i-20][e.getY()+ii-20] = true;
	        	    					boardImage.setRGB(e.getX()+i-20, e.getY()+ii-20, Front);
	        	    				}
	    						}
	    					} catch (ArrayIndexOutOfBoundsException q){}
	                	}
	            	}
            	} catch(Exception q){}
            	playing = ptemp;
            }
            
            public void mouseReleased(MouseEvent e) {}
        });
		
		reset();
		
		paint();
	}
	
	
	
	public void paint(){
		if(!playing){
			return;
		}
		
		boolean[][] movedFrom = new boolean[worldSize][worldSize];
		
		for(int x = 0; x < worldSize; x++){
			for(int y = worldSize-1; y > -1; y--){
				if(y < worldSize-5 && y > 4 && x < worldSize-5 && x > 4){
					if(world[x][y]){
						if(!world[x][y+1] && !movedFrom[x][y+1]){
							movedFrom[x][y] = true;
							
							world[x][y] = false;
							boardImage.setRGB(x, y, Clear);

							world[x][y+1] = true;
							boardImage.setRGB(x, y+1, Front);
						} else if (!world[x-1][y+1] ^ !world[x+1][y+1]){
							boolean bl = true, br = true;
							if(!world[x-1][y] && !world[x-2][y]){
								bl = world[x-1][y+1];
							}
							if(!world[x+1][y] && !world[x+2][y]){
								br = world[x+1][y+1];
							} 
							
							if(!bl && br){
								world[x][y] = false;
								boardImage.setRGB(x, y, Clear);

								world[x-1][y+1] = true;
								boardImage.setRGB(x-1, y+1, Front);
							} else if (!br && bl) {
								world[x][y] = false;
								boardImage.setRGB(x, y, Clear);

								world[x+1][y+1] = true;
								boardImage.setRGB(x+1, y+1, Front);
							} else {
								boardImage.setRGB(x, y, Front);
							}
						} 
					} 
				} else {
					boardImage.setRGB(x, y, Black);
				}
			} 
		}
		outImage.setIcon(new ImageIcon(boardImage));
	}
}
