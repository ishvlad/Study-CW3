package app;

import app.backend.Finder;
import app.backend.Finder.ImageType;
import app.frontend.InitWindow;

public class CourseWorkApp {
	public static void main(String[] args) {
		if(args.length == 0) {
			test();
		} else {
			InitWindow firstWindow = InitWindow.getInstance();
			firstWindow.show();
		}
	}

	private static void test() {
		String dir = "";//"/home/vlad/Pictures/test/";
		Finder mFinder = Finder.getInstance();
		
		for(int i = 6; i <= 6; i++) {
			System.out.println("Test #" + Integer.toString(i));
			mFinder.setFirstPath(dir + Integer.toString(i) + ".1.jpg");
			mFinder.setSecondPath(dir + Integer.toString(i) + ".2.jpg");
			
			for(int size = 0; size <= 1000; size+=4) {
				System.out.print("\t");
				System.out.print(size);
			}
			System.out.println();
			
			for(double border = 0; border < 769; border+=0.5) {
				mFinder.setIntenciveBorder(border);
				System.out.print(border);
				System.out.print("\t");
				
				for(int size = 0; size < 1000; size+=4) {
					mFinder.setMinSize(size);
					int objects = mFinder.getImage(ImageType.DAUTHER).segments;

					System.out.print(objects);
					System.out.print("\t");
					
					mFinder.clearScreen();
				}
				System.out.println();
			}
		}
	}
}
