package layer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;


public class MyPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private Image mImg;
	
	public MyPanel(mImage img) {
		mImg = img.getImageForPaint();
		Dimension size = new Dimension(img.getWidth(), img.getHeight());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(mImg, 0, 0, this);
		super.paint(g);
	}
	
	public void changeImg(mImage img) {
		mImg = img.getImageForPaint();
	}
}
