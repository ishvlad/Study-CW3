package layer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class mImage {
	private BufferedImage mImg;
	private ArrayList<Pixel> mPixels;
	public boolean modify;
	public int segments = 0;
	
	public mImage(BufferedImage img, long time) {
		mImg = img;
		modify = false;
		
		mPixels = new ArrayList<Pixel>(getHeight()*getWidth());
    	for(int j = 0; j < getHeight(); j++) {
			for(int i = 0; i < getWidth(); i++)
				mPixels.add(new Pixel(i, j, mImg.getRGB(i, j)));
		}
	}
    
    private int getPosition(int x, int y){
    	return y*getWidth() + x;
    }
    
    
    public void write(){
    	for(int j = 0; j < getHeight(); j++) {
			for(int i = 0; i < getWidth(); i++)
				if (j != getHeight()-1 || i != getWidth()-1)
					mImg.setRGB(i, j, mPixels.get(getPosition(i, j)).getRGB());
		}
    }

	public int getWidth() {
		return mImg.getWidth();
	}

	public void print(){
		for(Pixel item:mPixels) {
			int count = 0;
			for(Pixel item1:mPixels) {
				if(Segment.Find(item) != Segment.Find(item1)) {
					count++;
				}
			}
			//if (count != 0) {
				System.out.println(Integer.toString(mPixels.indexOf(item)) + " " + count);
			//}
		}
	}
	
	public int getHeight() {
		return mImg.getHeight();
	}

	public Pixel getPixel(int x, int y) {
		return mPixels.get(getPosition(x, y));
	}
	

	public ArrayList<Pixel> getImage() {
		return mPixels;
	}
	
	public Image getImageForPaint(){
		return mImg;
	}
}