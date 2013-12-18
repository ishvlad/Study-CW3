package layer;


public class Segment{
	public static double BORDER_OF_INTENSIVE = 0.0;
	public int mSize;
	public int mRank;
	public Segment mParent;

	protected Segment(){
		mSize = 1;
		mRank = 0;
		mParent = this;
	}

	public static void Merge(Edge edge) {
		Segment src = Segment.Find(edge.getSource());
		Segment dst = Segment.Find(edge.getDestination());

		if (src == dst) {
			return;
		}

		if (src.mRank < dst.mRank) {
			dst.mSize += src.mSize;
			src.mParent = dst;
		} else {
			src.mSize += dst.mSize;
			dst.mParent = src;
			if (dst.mRank == src.mRank) {
				src.mRank++;
			}
		}
	};

	public static Segment Find(Segment src) {
		if(src.mParent != src) {
			src.mParent = Segment.Find(src.mParent);
		}
		return src.mParent;
	}
}
