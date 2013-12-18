package layer;

public class Edge {
	private Pixel mSrc, mDst;
	private boolean mWeight;
	
	public Edge(Pixel src, Pixel dst) {
		mSrc = src;
		mDst = dst;
		mWeight = src.isChanged() && dst.isChanged(); 
	}
	
	public boolean getWeight(){ return mWeight; }
	public Pixel getSource(){ return mSrc; }
	public Pixel getDestination(){ return mDst; }
}
