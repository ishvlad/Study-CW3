package layer;

public class Pixel extends Segment{
	private int mX, mY, mRGB;
	private boolean changeFlag;
	
	public Pixel(int x, int y, int RGB) {
		super();
		mX = x;
		mY = y;
		mRGB = RGB;
		changeFlag = false;
	}
	
	public int getX(){ return mX; }
	public int getY(){ return mY; }
	public int getRGB(){ return mRGB; }
	public void setRGB(int RGB){ mRGB = RGB; }
	
	public boolean isChanged() { return changeFlag; }
	public void changeFlag() { changeFlag = !changeFlag; }
	
	public double dist(Pixel dst) {
		int RGBdst = dst.getRGB();
		
		int redDist = ((mRGB >> 16) & 0xff) - ((RGBdst >> 16) & 0xff);
		int blueDist = ((mRGB >> 8) & 0xff) - ((RGBdst >> 8) & 0xff);
		int greenDist = (mRGB & 0xff) - (RGBdst & 0xff);
		
		return Math.sqrt(redDist * redDist + 
						 blueDist * blueDist +
						 greenDist * greenDist 
						 );
	}
}