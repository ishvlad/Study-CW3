package app.backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import layer.mImage;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.Utils;


public class VideoLoader {
	public static boolean hasNext = false;
	
	@SuppressWarnings("deprecation")
	public static mImage getImagesList(String path, int index) {
		mImage result = null;
		
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
		
		IPacket packet = IPacket.make();
		
		long start = (index + 1) * (1000) * 1000;
		boolean readyToExit = false;
		hasNext = false;
		
		END: while (ic.readNextPacket(packet) >= 0) {
			if (packet.getStreamIndex() == videoStreamId) {
				IVideoPicture picture = IVideoPicture.make(isc.getPixelType(), isc.getWidth(), isc.getHeight());
				int offset = 0;
				while (offset < packet.getSize()) {
					int bytesDecoded = isc.decodeVideo(picture, packet, offset);
					
					if (bytesDecoded < 0)
						throw new RuntimeException("got error decoding video");
					offset += bytesDecoded;
					
					if (picture.isComplete()) {
						IVideoPicture newPic = picture;
						
						long timestamp = picture.getTimeStamp();
						if (timestamp > start) {
							if (readyToExit) {
								hasNext = true;
								break END;
							}
							result = new mImage(Utils.videoPictureToImage(newPic), timestamp);
							start += 100*1000;
							readyToExit = true;
						}
					}
				}
			}
		}

		return result;
	}
	
	public static LinkedList<mImage> getImage(String path) throws IOException {
		LinkedList<mImage> result = new LinkedList<mImage>();
		
		result.add(new mImage(ImageIO.read(new File(path)), 0));
		
		return result;
	}
	
	public static void setIdentity(String path, LinkedList<BufferedImage> images) {
		IMediaWriter writer = ToolFactory.makeWriter(path);
		writer.addVideoStream(0, 0, images.getFirst().getWidth(), images.getFirst().getHeight());
		long start = System.nanoTime();
		
		for(BufferedImage img:images) {
			writer.encodeVideo(0, img, System.nanoTime()-start, TimeUnit.NANOSECONDS);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		writer.close();
	}
	
}
