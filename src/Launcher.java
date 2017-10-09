import java.util.Scanner;

public class Launcher {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ImageHandler game = new ImageHandler();
		
		input.close();
		
		game.reset();
		
		game.setVisible(true);
		game.playing = true;
		for(;;){
			game.paint();
		}
	}
}
