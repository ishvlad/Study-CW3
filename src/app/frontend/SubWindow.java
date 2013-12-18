package app.frontend;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import layer.MyPanel;
import layer.mImage;


public class SubWindow implements WindowListener{
	private Frame mFrame;
	
	private MyPanel imgPanel;
	
	public SubWindow(mImage img) {
		mFrame = new Frame("Find the differences in the two videos");
		mFrame.setLayout(null);
		mFrame.setSize(img.getWidth(), img.getHeight() + 25);
		mFrame.setResizable(false);
		mFrame.addWindowListener(this);
		
		imgPanel = new MyPanel(img);
		mFrame.add(imgPanel);
		imgPanel.setBounds(0, 25, img.getWidth(), img.getHeight());
	}
	
	public void show(){
		if (!mFrame.isVisible()) {
			mFrame.setVisible(true);
		}
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		mFrame.dispose();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		
	}
}
