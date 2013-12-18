package app.backend;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import layer.Pixel;
import layer.Segment;
import layer.mImage;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStreamCoder;

public class Finder {
	private String path1, path2;
	
	private Type firstType, secondType;
	
	private int minSizeOfSegment;
	private double borderOfIntensive;
	
	private mImage fatherImage, motherImage, dautherImage;
	private int IndexOfImage;
	private Boolean canInc;
	
	private static Finder state = null;
	
	private Finder(){
		path1 = null;
		path2 = null;
		
		firstType = Type.DEFAULT;
		secondType = Type.DEFAULT;
		
		minSizeOfSegment = -2;
		borderOfIntensive = -2;
		
		clearScreen();
		
		state = this;
	}
	
	public static Finder getInstance() {
		if (state == null) {
			return new Finder();
		} else {
			return state;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setFirstVideoPath(String path) throws IllegalArgumentException, RuntimeException{
		IContainer ic = IContainer.make();
		if(ic.open(path, IContainer.Type.READ, null) < 0) 
			throw new IllegalArgumentException("could not open video");
		
		int videoStreamId = -1;
		IStreamCoder isc = null;
		
		for(int i = 0; i < ic.getNumStreams(); i++) {
			IStreamCoder itemSC = ic.getStream(i).getStreamCoder();
					
			if (itemSC.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoStreamId = i;
				isc = itemSC;
			}
		}
		
		if(videoStreamId == -1) 
			throw new RuntimeException("could not find stream");
		
		if(isc.open() < 0)
			throw new RuntimeException("could not open video decoder");
	}
	
	private mImage setImagePath(String path) throws IOException {
		return new mImage(ImageIO.read(new File(path)), 0);
	}
	
	public boolean setFirstPath(String path) {
		String type = path.split("\\.")[path.split("\\.").length-1];
		
		try {
			if (type.equals("jpg") || type.equals("jpeg") || type.equals("png")) {
				setImagePath(path);
				firstType = Type.IMAGE;
			} else {
				setFirstVideoPath(path);
				firstType = Type.VIDEO;
			}
		} catch (IOException e) {
			firstType = Type.DEFAULT;
			return false;
		} catch (IllegalArgumentException e1) {
			firstType = Type.DEFAULT;
			return false;
		} catch (RuntimeException e2) {
			firstType = Type.DEFAULT;
			return false;
		}
		
		path1 = path;
		return true;
	}
	
	public boolean setSecondPath(String path) {
		String type = path.split("\\.")[path.split("\\.").length-1];
		
		try {
			if (type.equals("jpg") || type.equals("jpeg") || type.equals("png")) {
				setImagePath(path);	
				secondType = Type.IMAGE;
			} else {
				setFirstVideoPath(path);
				secondType = Type.VIDEO;
			}
		} catch (IOException e) {
			secondType = Type.DEFAULT;
			return false;
		} catch (IllegalArgumentException e1) {
			secondType = Type.DEFAULT;
			return false;
		} catch (RuntimeException e2) {
			secondType = Type.DEFAULT;
			return false;
		}
		
		path2 = path;
		return true;
	}

	public boolean isReady() {
		if (firstType != Type.DEFAULT && secondType != Type.DEFAULT) {
			return true;
		} else {
			return false;
		}
	}

	public void inc(){
		if(canInc()) {
			IndexOfImage++;
			init();
		}
	}
	public void dec(){
		if(canDec()) {
			IndexOfImage--;
			init();
		}
	}
	public boolean canInc(){
		return canInc;
	}
	public boolean canDec(){
		return IndexOfImage != 0;
	}
	private void init() {
		mImage fromBuf = null;
		switch (firstType) {
		case VIDEO:
			fatherImage = VideoLoader.getImagesList(path1, IndexOfImage);
			fromBuf = VideoLoader.getImagesList(path1, IndexOfImage);
			canInc = VideoLoader.hasNext;
			break;
		case IMAGE:
			try {
				fatherImage = setImagePath(path1);
				fromBuf = setImagePath(path1);
			} catch (IOException e) {}
			canInc = false;
		default:
			break;
		}
		switch (secondType) {
		case VIDEO:
			motherImage = VideoLoader.getImagesList(path2, IndexOfImage);
			dautherImage = VideoLoader.getImagesList(path2, IndexOfImage);
			canInc &= VideoLoader.hasNext;
			break;
		case IMAGE:
			try {
				motherImage = setImagePath(path2);
				dautherImage = setImagePath(path2);
			} catch (IOException e) {}
			canInc = false;
		default:
			break;
		}
		
		ImageOperation.Subtraction(dautherImage, fromBuf, borderOfIntensive < 0 ? setIntenciveBorder() : borderOfIntensive);
		LinkedList<Segment> segments = ImageOperation.Segmentation(fromBuf, minSizeOfSegment < 0 ? setMinSize() : minSizeOfSegment);
		
		if(!segments.isEmpty()) {
			ImageOperation.Addition(ImageOperation.Sobel1(fromBuf, segments), dautherImage);
			dautherImage.modify = true;
		}
		dautherImage.write();
	}
	
	public mImage getImage(ImageType type) {
		if (dautherImage == null) {
			init();
		}
		
		switch (type) {
		case FATHER:
			return fatherImage;
		case MOTHER:
			return motherImage;
		case DAUTHER: 
			return dautherImage;
		}
		
		return null;
	}
	
	public void setMinSize(int size) {
		minSizeOfSegment = size;
	}
	
	public void setIntenciveBorder(double border) {
		borderOfIntensive = border;
	}
	
	public int setMinSize() {
		return fatherImage.getHeight() / 5;
	}
	
	public double setIntenciveBorder() {
		double sumOfIntency = 0;
		
		Iterator<Pixel> srcIt = fatherImage.getImage().iterator();
		Iterator<Pixel> dstIt = motherImage.getImage().iterator();
		
		while(srcIt.hasNext() && dstIt.hasNext()) {
			sumOfIntency += srcIt.next().dist(dstIt.next());
		}
		return sumOfIntency / (fatherImage.getHeight()*fatherImage.getWidth()) + 10;
	} 
	
	public void clearScreen(){
		dautherImage = null;
		fatherImage = null;
		motherImage = null;
		
		IndexOfImage = 0;
		canInc = true;
	}

	private enum Type { DEFAULT, VIDEO, IMAGE; }
	public enum ImageType {FATHER, MOTHER, DAUTHER; }
}
