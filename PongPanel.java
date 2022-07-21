import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class PongPanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 1024;
	static final int SCREEN_HEIGHT = 384;
	static final int MOVE_SIZE = 12;
	static final int BALL_SIZE = 16;
	int delay = 6;
	final int RACKETY = 128;
	final int RACKETX = 8;
	int p1Y = (SCREEN_HEIGHT - RACKETY)/2;
	int p2Y = (SCREEN_HEIGHT - RACKETY)/2;
	int hits;
	int ballX;
	int ballY;
	int bdY = 0;
	int bdX = 6;
	char direction1 = 'N';
	char direction2 = 'N';
	boolean running = false;
	Timer timer;
	Random random;
	
	PongPanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(new Color(0,0,30));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newBall();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {

			g.setColor(new Color(50,50,70));
			g.fillRect(SCREEN_WIDTH/2 - 3, 0, 6, SCREEN_HEIGHT/2 - 60);
			g.setFont( new Font("",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.fillRect(SCREEN_WIDTH/2 - 3, SCREEN_HEIGHT/2 + 40, 6, SCREEN_HEIGHT);
			g.drawString(""+hits, (SCREEN_WIDTH - metrics.stringWidth(""+hits))/2, SCREEN_HEIGHT/2);
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			int red = hits*8;
			if (red>255) red = 255;
			Color b = new Color(255,255-red,255-red);
			g.setColor(b);
			g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
			g.setColor(new Color(220,230,255));
			g.fillRect(0, p1Y, RACKETX, RACKETY);
			g.fillRect(SCREEN_WIDTH-RACKETX, p2Y, RACKETX, RACKETY);
		} else {
			gameOver(g);
		}
		
	}
	public void newBall(){
		ballX = SCREEN_WIDTH/2;
		ballY = SCREEN_HEIGHT/2;
		if (Math.random()>0.5) {
			bdX *= -1;
		}
		bdY = 0;//(int)(Math.random() * 5) - 2;
	}
	public void move(){
		ballX += bdX;
		ballY += bdY;
		switch(direction1) {
		case 'U':
			if (p1Y > 0) {
				p1Y -= MOVE_SIZE;	
			}
			break;
		case 'D':
			if (p1Y < SCREEN_HEIGHT - RACKETY) {
				p1Y += MOVE_SIZE;	
			}
		}
		switch(direction2) {
		case 'U':
			if (p2Y > 0) {
				p2Y -= MOVE_SIZE;	
			}
			break;
		case 'D':
			if (p2Y < SCREEN_HEIGHT - RACKETY) {
				p2Y += MOVE_SIZE;	
			}
		}
		
	}
	public void checkBall() {
		if(bdX > 0 && (p2Y <= ballY +BALL_SIZE && ballY <= p2Y + RACKETY) && (ballX > SCREEN_WIDTH - (RACKETX + BALL_SIZE) )) {
			hits++;
			if (hits % 20 == 0) {
				bdX /= 1.15;
				delay-=2;
			} else {
				bdX++;	
			}
			bdY = (ballY-p2Y - (RACKETY)/2)/4 + bdY/2;
			if (bdY > 8) {
				bdY = 8;
			}
			if (bdY < -8) {
				bdY = -8;
			}
			bdX *= -1;
			bdY *= 0.75;
		}
		if(bdX < 0 && (p1Y <= ballY+BALL_SIZE && ballY <= p1Y + RACKETY) && (ballX < RACKETX)) {
			hits++;
			if (hits % 20 == 0) {
				bdX /= 1.15;
				delay-=2;
			} else {
				bdX--;	
			}
			bdY = (ballY-p1Y - (RACKETY)/2)/4 + (bdY/2);
			if (bdY > 8) {
				bdY = 8;
			}
			if (bdY < -8) {
				bdY = -8;
			}
			bdX *= -1;
			bdY *= 0.75;
		}
		if (ballX > SCREEN_WIDTH+(hits/2) || ballX < -BALL_SIZE-(hits/2)) {
			running = false;
		}
	}
	
	
	public void checkCollision() {
		if(ballY <= 0 || ballY >= SCREEN_HEIGHT - BALL_SIZE) {
			bdY *= -1;;
		}
	}
	public void gameOver(Graphics g) {
		String winner = ((bdX > 0) ? "Left" : "Right");
		g.setColor(new Color(50,50,70));
		g.setFont( new Font("",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString(winner + " wins", (SCREEN_WIDTH - metrics2.stringWidth(winner + " wins"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkBall();
			checkCollision();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
					direction2 = 'U';
				break;
			case KeyEvent.VK_DOWN:
					direction2 = 'D';
				break;
			}
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
					direction1 = 'U';
				break;
			case KeyEvent.VK_S:
					direction1 = 'D';
				break;
			}
		}
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
						direction2 = 'N';
					break;
				case KeyEvent.VK_DOWN:
						direction2 = 'N';
					break;
				}
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
						direction1 = 'N';
					break;
				case KeyEvent.VK_S:
						direction1 = 'N';
					break;
				}
		}
	}
}