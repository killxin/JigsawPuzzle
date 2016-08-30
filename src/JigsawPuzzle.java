import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class BlankPoint {
	int x, y, off;

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		this.off = x *3 + y;
	}
}

public class JigsawPuzzle extends JFrame implements KeyListener {
	/**
	 * Simple jigsaw puzzle games
	 */
	private static final long serialVersionUID = -2213526858608694002L;

	String fileName = "Tulips.jpg";
	JPanel mainPanel = new JPanel();
	JLabel[][] btn = null;
	ImageIcon checkIcon[] = null;
	ImageIcon tempIcon[] = null;
	JLabel piclab = new JLabel();
	JLabel namelab = new JLabel("RAW PICTURE:");
	JButton start = new JButton("START");
	BlankPoint blank = new BlankPoint();

	public JigsawPuzzle() {
		this.setTitle("Jigsaw Puzzle");
		this.setSize(600, 500);
		this.setLayout(null);
		this.setLocation(200, 120);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel.setBounds(30, 30, 360, 360);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(mainPanel);

		namelab.setBounds(450, 30, 110, 15);
		piclab.setBounds(450, 50, 110, 110);

		this.add(namelab);
		this.add(piclab);

		addKeyListener(this);
		init();
		refresh();
	}

	public void init() {
		ImageIcon img = new ImageIcon(fileName);
		ImageIcon labImage = new ImageIcon();
		labImage.setImage(img.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
		piclab.setIcon(labImage);
		ArrayList<Image> imgs = splitImage(img, 3);
		tempIcon = new ImageIcon[9];
		checkIcon = new ImageIcon[9];
		btn = new JLabel[3][3];
		blank.setXY(2, 2);
		int k = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 2 && j == 2) {
					checkIcon[k] = null;
					tempIcon[k] = null;
					break;
				}
				checkIcon[k] = new ImageIcon(imgs.get(k).getScaledInstance(120, 120, Image.SCALE_DEFAULT));
				tempIcon[k] = checkIcon[k];
				k++;
			}
		}
	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		image = new ImageIcon(image).getImage();
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.OPAQUE);
		if (bimage == null) {
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		}
		Graphics g = bimage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public ArrayList<Image> splitImage(ImageIcon i, int n) {
		Image img = i.getImage();
		BufferedImage bi = toBufferedImage(img);
		int baseHeight = bi.getHeight();
		int baseWidth = bi.getWidth();
		int x = 0;
		int y = 0;
		ArrayList<Image> imgs = new ArrayList<Image>();
		int h = baseHeight / n;
		int w = baseWidth / n;
		for (int k = 0; k < n * n; k++) {
			Image temp = bi.getSubimage(x, y, w, h);
			imgs.add(temp);
			x += w;
			if (x == w * n) {
				x = 0;
				y += h;
			}
		}
		return imgs;
	}

	public void refresh() {
		mainPanel.removeAll();
		int k = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				btn[i][j] = new JLabel();
				btn[i][j].setBounds(j * 120, i * 120, 120, 120);
				btn[i][j].setBackground(Color.WHITE);
				btn[i][j].setIcon(tempIcon[k]);
				mainPanel.add(btn[i][j]);
				k++;
			}
		}
		mainPanel.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			exchange(blank.x, blank.y + 1);
			break;
		case KeyEvent.VK_RIGHT:
			exchange(blank.x, blank.y - 1);
			break;
		case KeyEvent.VK_DOWN:
			exchange(blank.x - 1, blank.y);
			break;
		case KeyEvent.VK_UP:
			exchange(blank.x + 1, blank.y);
			break;
		}
	}

	public void exchange(int xoff, int yoff) {
		if (xoff >= 0 && xoff <= 2 & yoff >= 0 && yoff <= 2) {
			int k = blank.off;
			blank.setXY(xoff, yoff);
			int koff = blank.off;
			ImageIcon temp = tempIcon[k];
			tempIcon[k] = tempIcon[koff];
			tempIcon[koff] = temp;
			refresh();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new JigsawPuzzle();
	}

}