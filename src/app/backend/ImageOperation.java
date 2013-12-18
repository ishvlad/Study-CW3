package app.backend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import layer.Edge;
import layer.MinAndMaxCoords;
import layer.Pixel;
import layer.Segment;
import layer.mImage;

public class ImageOperation {
	
	private static ArrayList<Edge> getSortEdges(mImage img) {

		ArrayList<Edge> edges = new ArrayList<>(2*img.getHeight()*img.getWidth());

		for(int j = 0; j < img.getHeight(); j++) {
			for(int i = 0; i < img.getWidth()-1; i++) {
				Edge itemEdge = new Edge(img.getPixel(i, j), img.getPixel(i+1, j));
				if (itemEdge.getWeight()) {
					edges.add(itemEdge);
				}
			}
		}

		for(int j = 0; j < img.getHeight()-1; j++) {
			for(int i = 0; i < img.getWidth(); i++) {
				Edge itemEdge = new Edge(img.getPixel(i, j), img.getPixel(i, j+1));
				if (itemEdge.getWeight()) {
					edges.add(itemEdge);
				}
			}
		}
		return edges;
	}
	
	private static LinkedList<Segment> deleteSmallSegments(
			mImage img, int minSizeOfSegment) {
		LinkedList<Segment> segments = new LinkedList<Segment>();
		
		for(Pixel item:img.getImage()) {
			if (item.mParent == item && item.mSize > minSizeOfSegment) {
				segments.add(item);
			} else if (Segment.Find(item).mSize < minSizeOfSegment) {
				item.setRGB(-1);
			}
		}
		img.segments = segments.size();
		return segments;
	}

	public static LinkedList<Segment> Segmentation(mImage img, int minSizeOfSegment) {
		
		for(Edge item:getSortEdges(img)) {
			Segment.Merge(item);
		}
		
		return deleteSmallSegments(img, minSizeOfSegment); 
	}
	
	public static void Addition(mImage from, mImage to) {
		Iterator<Pixel> fromIter = from.getImage().iterator();
		Iterator<Pixel> toIter = to.getImage().iterator();
		to.segments = from.segments;
		
		while(fromIter.hasNext() && toIter.hasNext()) {
			Pixel itemFrom = fromIter.next();
			Pixel itemTo = toIter.next();
			
			if(itemFrom.getRGB() != -1) {
				itemTo.setRGB(0xff);
			}
		}
	}
	
	public static void Subtraction(mImage from, mImage to, double borderOfIntensive) {	
		for(int j = 0; j < to.getHeight(); j++) {
			for(int i = 0; i < to.getWidth(); i++) {
				if(from.getPixel(i, j).dist(to.getPixel(i, j)) < borderOfIntensive) {
					to.getPixel(i, j).setRGB(-1);
				} else {
					to.getPixel(i, j).setRGB(0);
					to.getPixel(i, j).changeFlag();
				}
			}
		}
	}
	
	public static mImage Sobel(mImage img) {
		img.write();
		
		for(Pixel itemPixel:img.getImage()) {
			
			if(		itemPixel.isChanged() &&
					itemPixel.getX() > 0 && itemPixel.getX() < img.getWidth()-1 &&
					itemPixel.getY() > 0 && itemPixel.getY() < img.getHeight()-1 ) {
				int countOfGranPixels = 0;
				for(int i = -1; i < 1; i++) {
					for(int j = -1; j < 1; j++) { 
						if(!img.getImage().get(itemPixel.getX()+j +  
								(itemPixel.getY()+i)*img.getWidth()).isChanged()){
							countOfGranPixels++;
						}
					}
				}
				if (countOfGranPixels == 0) {
					itemPixel.setRGB(-1);
				} 
			}
		}
		return img;
	}
	
	public static mImage Sobel1(mImage img, LinkedList<Segment> segments) {
		img.write();
		
		MinAndMaxCoords[] mass = new MinAndMaxCoords[segments.size()];
		for(int i = 0; i < segments.size(); i++) {
			mass[i] = new MinAndMaxCoords();
		}
		for(Pixel itemPixel:img.getImage()) {
			if (itemPixel.getRGB() != -1) {
				int index = segments.indexOf(itemPixel.mParent);
				if(	index != -1 ) {
					mass[index].minX = mass[index].minX > itemPixel.getX() ? itemPixel.getX() : mass[index].minX;
					mass[index].maxX = mass[index].maxX < itemPixel.getX() ? itemPixel.getX() : mass[index].maxX;
					mass[index].minY = mass[index].minY > itemPixel.getY() ? itemPixel.getY() : mass[index].minY;
					mass[index].maxY = mass[index].maxY < itemPixel.getY() ? itemPixel.getY() : mass[index].maxY;
					
				}
				itemPixel.setRGB(-1);
			}
		}
		
		for (MinAndMaxCoords item:mass) {
			for(Pixel itemPixel:img.getImage()) {
				
				if(		((itemPixel.getX() == item.minX || itemPixel.getX() == item.maxX) && (itemPixel.getY() >= item.minY && itemPixel.getY() <= item.maxY)) ||
						((itemPixel.getX() >= item.minX && itemPixel.getX() <= item.maxX) && (itemPixel.getY() == item.minY || itemPixel.getY() == item.maxY))
						 ) {
					itemPixel.setRGB(0);
				} 
			}
		}
		return img;
	}
}
