import javax.swing.JFrame;

public class PongGame extends JFrame{

	PongGame(){
			
		this.add(new PongPanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
}