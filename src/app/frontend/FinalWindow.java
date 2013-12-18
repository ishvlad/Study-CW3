package app.frontend;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import layer.MyPanel;
import layer.mImage;
import app.backend.Finder;
import app.backend.Finder.ImageType;


public class FinalWindow implements WindowListener, ActionListener{
	private Frame mFrame;
	
	private Label segments;
	
	private MyPanel imgPanel;

	private Button backButton;
	private Button prevButton;
	private Button nextButton;
	private Button button1;
	private Button button2;
	
	public FinalWindow() {
		mImage firstImage = Finder.getInstance().getImage(ImageType.DAUTHER);
		
		mFrame = new Frame("Find the differences in the two videos");
		mFrame.setLayout(null);
		mFrame.setSize(firstImage.getWidth() + 10 > 300 ? firstImage.getWidth() + 10 : 300, 
			firstImage.getHeight() + 115);
		mFrame.setResizable(false);
		mFrame.addWindowListener(this);
		//
		backButton = new Button("Back");
		mFrame.add(backButton);
		backButton.setBounds(5, 32, 75, 26);
		backButton.addActionListener(this);
		
		button1 = new Button("1");
		mFrame.add(button1);
		button1.setBounds(90, 32, 15, 26);
		button1.addActionListener(this);
		
		button2 = new Button("2");
		mFrame.add(button2);
		button2.setBounds(105, 32, 15, 26);
		button2.addActionListener(this);
		
		segments = new Label("Objects: " + Integer.toString(firstImage.segments), Label.RIGHT);
		mFrame.add(segments);
		segments.setBounds(mFrame.getWidth() - 100, 30, 90, 30);
		//
		
		prevButton = new Button("Prev");
		mFrame.add(prevButton);
		prevButton.setBounds(mFrame.getWidth() - 160, 72, 75, 26);
		prevButton.addActionListener(this);
		prevButton.setEnabled(false);
		
		nextButton = new Button("Next");
		mFrame.add(nextButton);
		nextButton.setBounds(mFrame.getWidth() - 80, 72, 75, 26);
		nextButton.addActionListener(this);
		if (!Finder.getInstance().canInc()) {
			nextButton.setEnabled(false);
		}
		//
		imgPanel = new MyPanel(firstImage);
		mFrame.add(imgPanel);
		imgPanel.setBounds(5, 110, mFrame.getWidth()-10, firstImage.getHeight());
	}
	
	public void show(){
		if (!mFrame.isVisible()) {
			mFrame.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object caller = e.getSource();
		
		if(caller.equals(backButton)) {
			mFrame.dispose();
			Finder.getInstance().clearScreen();
			InitWindow.getInstance().show();
		} else if(caller.equals(nextButton)) {
			Finder.getInstance().inc();
			if (!Finder.getInstance().canInc()) {
				nextButton.setEnabled(false);
			}
			imgPanel.changeImg(Finder.getInstance().getImage(ImageType.DAUTHER));
			imgPanel.paint(imgPanel.getGraphics());
			prevButton.setEnabled(true);
			segments.setText("Objects: " + Integer.toString(Finder.getInstance().getImage(ImageType.DAUTHER).segments));
		} else if(caller.equals(prevButton)) {
			Finder.getInstance().dec();
			if (!Finder.getInstance().canDec()) {
				prevButton.setEnabled(false);
			}
			imgPanel.changeImg(Finder.getInstance().getImage(ImageType.DAUTHER));
			imgPanel.paint(imgPanel.getGraphics());
			nextButton.setEnabled(true);
			segments.setText("Objects: " + Integer.toString(Finder.getInstance().getImage(ImageType.DAUTHER).segments));
		} else if(caller.equals(button1)) {
			new SubWindow(Finder.getInstance().getImage(ImageType.FATHER)).show();
		}else if (caller.equals(button2)) {
			new SubWindow(Finder.getInstance().getImage(ImageType.MOTHER)).show();
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
		System.exit(0);
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
