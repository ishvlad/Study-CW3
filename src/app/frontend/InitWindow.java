package app.frontend;

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import app.backend.Finder;

public class InitWindow implements WindowListener, ActionListener{
	private Frame mFrame;

	private TextField firstVideoTextField;
	private TextField secondVideoTextField;
	private TextField minSizeOfSegment;
	private TextField borderOfIntencive;

	private Button firstVideoButton;
	private Button secondVideoButton;
	private Button autoParams;
	private Button start;

	private static InitWindow state = null;

	private InitWindow() {
		mFrame = new Frame("Find the differences in the two videos");
		mFrame.setLayout(null);
		mFrame.setSize(500, 270);
		mFrame.setResizable(false);
		mFrame.addWindowListener(this);
		//
		Label firstLabel = new Label("1 video: ");
		mFrame.add(firstLabel);
		firstLabel.setBounds(5, 40, 60, 30);

		firstVideoTextField = new TextField();
		mFrame.add(firstVideoTextField);
		firstVideoTextField.setBounds(70, 42, 320, 26);

		firstVideoButton = new Button("Browse");
		mFrame.add(firstVideoButton);
		firstVideoButton.setBounds(395, 40, 100, 30);
		firstVideoButton.addActionListener(this);
		//
		Label secondLabel = new Label("2 video: ");
		mFrame.add(secondLabel);
		secondLabel.setBounds(5, 80, 60, 30);

		secondVideoTextField = new TextField();
		mFrame.add(secondVideoTextField);
		secondVideoTextField.setBounds(70, 82, 320, 26);

		secondVideoButton = new Button("Browse");
		mFrame.add(secondVideoButton);
		secondVideoButton.setBounds(395, 80, 100, 30);
		secondVideoButton.addActionListener(this);
		//
		Label paramsLabel = new Label("Parametrs:", Label.RIGHT);
		mFrame.add(paramsLabel);
		paramsLabel.setBounds(5, 125, 245, 15);

		autoParams = new Button("Auto");
		mFrame.add(autoParams);
		autoParams.setBounds(255, 123, 100, 20);
		autoParams.addActionListener(this);
		//
		Label minSize = new Label("Min size of segment: ");
		mFrame.add(minSize);
		minSize.setBounds(5, 150, 150, 30);

		minSizeOfSegment = new TextField();
		mFrame.add(minSizeOfSegment);
		minSizeOfSegment.setBounds(160, 150, 45, 30);
		//
		Label border = new Label("Border of Intensive: ");
		mFrame.add(border);
		border.setBounds(5, 185, 150, 30);

		borderOfIntencive = new TextField();
		mFrame.add(borderOfIntencive);
		borderOfIntencive.setBounds(160, 185, 45, 30);
		//
		start = new Button("Start");
		mFrame.add(start);
		start.setBounds(200, 230, 100, 30);
		start.addActionListener(this);
		//
		state = this;
	}

	public static InitWindow getInstance() {
		if (state == null) {
			return new InitWindow();
		} else {
			return state;
		}
	}

	public void show(){
		if (!mFrame.isVisible()) {
			mFrame.setVisible(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object caller = e.getSource();
		freeze();

		if(caller.equals(firstVideoButton)) {
			FileDialog fdlg = new FileDialog(mFrame, "Open first video", FileDialog.LOAD);
			fdlg.setMultipleMode(true);
			fdlg.setVisible(true);
			fdlg.setAlwaysOnTop(true);

			String firstPath = fdlg.getDirectory() + fdlg.getFile();
			if (!Finder.getInstance().setFirstPath(firstPath)) {
				firstVideoTextField.setText("could not open video");
				unfreeze();
				return;
			} else {
				firstVideoTextField.setText(firstPath);
			}
		} else if (caller.equals(secondVideoButton)) {
			FileDialog fdlg = new FileDialog(mFrame, "Open second video", FileDialog.LOAD);
			fdlg.setVisible(true);
			fdlg.setAlwaysOnTop(true);

			String secondPath = fdlg.getDirectory() + fdlg.getFile();

			if (!Finder.getInstance().setSecondPath(secondPath)) {
				secondVideoTextField.setText("could not open video");
				unfreeze();
				return;
			} else {
				secondVideoTextField.setText(secondPath);
			}			
		} else if (caller.equals(autoParams)) {
			minSizeOfSegment.setText("Auto");
			borderOfIntencive.setText("Auto");
		} else if (caller.equals(start)) {
			if(Finder.getInstance().isReady() && setMinSize(minSizeOfSegment.getText()) && 
					setBorder(borderOfIntencive.getText())) {	
				startAnalyze();
			}
		}
		unfreeze();
	}
	@Override
	public void windowActivated(WindowEvent e) {

	}
	@Override
	public void windowClosed(WindowEvent e) {

	}
	@Override
	public void windowClosing(WindowEvent e) {
		mFrame.setVisible(false);
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

	private void freeze() {
		firstVideoButton.setEnabled(false);
		secondVideoButton.setEnabled(false);
		autoParams.setEnabled(false);
		start.setEnabled(false);
	}
	private void unfreeze() {
		firstVideoButton.setEnabled(true);
		secondVideoButton.setEnabled(true);
		autoParams.setEnabled(true);
		start.setEnabled(true);
	}

	private void hide(){
		if (mFrame.isVisible()) {
			mFrame.setVisible(false);
		}
	}
	
	private void startAnalyze() {
		FinalWindow finalWindow = new FinalWindow();
		hide();
		finalWindow.show();
	}

	private boolean setMinSize(String src) {
		if(src.equals("Auto")) {
			Finder.getInstance().setMinSize(-1);
			return true;
		} 

		int result = -1;
		try {
			result = Integer.parseInt(src);
		} catch (NumberFormatException er) {
			minSizeOfSegment.setText("NaN");
			return false;
		}
		if (result >= 0) {
			Finder.getInstance().setMinSize(result);
			return true;
		} else {
			minSizeOfSegment.setText(">=0");
			return false;
		}

	}
	private boolean setBorder(String src) {
		if(src.equals("Auto")) {
			Finder.getInstance().setIntenciveBorder(-1);
			return true;
		} 

		Double result = -1.0;
		try {
			result = Double.parseDouble(src);
		} catch (NumberFormatException er) {
			borderOfIntencive.setText("NaN");
			return false;
		}
		if (result >= 0) {
			Finder.getInstance().setIntenciveBorder(result);
			return true;
		} else {
			borderOfIntencive.setText(">=0");
			return false;
		}
	}

}
