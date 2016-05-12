import java.util.ArrayList;
import java.util.Arrays;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;

class Frame{
	@Override
	public String toString() {
		return "Frame [width=" + width + ", height=" + height + ", refNum="
				+ refNum + ", endNs=" + endNs + "]";
	}
	int width;
	int height;
	int refNum;
	int endNs;
	boolean[][] alphaMap;
}
/*
 * In the future, should extend some class that provides a format with image array so we can add a class that would include a location and a reference to it.
 */
public class Protagonist extends Sprite{
	ArrayList<ArrayList<Frame>> imageArray;
	//Define this as the frame we change animation
	int curTime;
	int curAnimType;
	int curFrame;
	public Protagonist(GL2 gl){
		loc = new double[] { 0, 0 };
		imageArray = new ArrayList<ArrayList<Frame>>();
		for(int i = 0; i < 4; i++)
			imageArray.add(new ArrayList<Frame>());
		
		int[] temp = new int[2];
		for(int i = 0; i <= 9; i++)
		{
			Frame lFrame = new Frame();
			Ret r = JavaFramework.glTexImageTGAFile(gl, "Kirby-Sprites/Walking-Left-0"+(9-i)+ "-00.tga", temp);
			lFrame.refNum = r.refnum;
			lFrame.width = temp[0];
			lFrame.height = temp[1];
			lFrame.endNs = (i + 1) * 100;
			lFrame.alphaMap = r.alphaGrid;
			imageArray.get(0).add(lFrame);
			Frame rFrame = new Frame();
			
			r = JavaFramework.glTexImageTGAFile(gl, "Kirby-Sprites/Walking-Right-0"+ i + "-00.tga", temp);
			rFrame.refNum = r.refnum;
			rFrame.width = temp[0];
			rFrame.height = temp[1];
			rFrame.endNs = (i + 1) * 100;
			rFrame.alphaMap = r.alphaGrid;
			imageArray.get(1).add(rFrame);
		}
		Frame sleft = new Frame();
		Ret r = JavaFramework.glTexImageTGAFile(gl, "Kirby-Sprites/Standing-Left.tga", temp);
		sleft.alphaMap = r.alphaGrid;
		sleft.refNum = r.refnum;
		System.out.println(Arrays.toString(spriteSize));
		sleft.width = temp[0];
		sleft.height = temp[1];
		sleft.endNs = 1000;
		imageArray.get(2).add(sleft);
		
		Frame sright = new Frame();
		r = JavaFramework.glTexImageTGAFile(gl, "Kirby-Sprites/Standing-Right.tga", spriteSize);
		sright.refNum = r.refnum;
		sright.alphaMap = r.alphaGrid;
		sright.width = temp[0];
		sright.height = temp[1];
		sright.endNs = 1000;
		imageArray.get(3).add(sright);
		startAnimation(3);
	}
	public int startAnimation(int animNum){
		curFrame = 0;
		curTime = 0;
		curAnimType = animNum;
		return curAnimType;
	}
	/*
	 * delta is the change in time since last
	 */
	public Frame changeAnimation(int delta){
		curTime += delta;
		int size = imageArray.get(curAnimType).size();
		ArrayList<Frame> curarr = imageArray.get(curAnimType);
		int maxNs = curarr.get(size - 1).endNs;
		if(curTime >= maxNs)
		{
			curTime = curTime % maxNs;
			curFrame = 0;
		}
		for(; curFrame < size; curFrame++)
		{
			if(curarr.get(curFrame).endNs >= curTime){
				return curarr.get(curFrame);
			}
		}
		return null;//?? can't happen
	}
	public void onOverlapY(int[] temp, Sprite s2)//Problem with this definition: what do we do when we want multiple types of interaction varying per sprite?
	{
		super.onOverlapY(temp, s2);
		
	}
	public void onOverlapX(int[] temp, Sprite s2)//Problem with this definition: what do we do when we want multiple types of interaction varying per sprite?
	{
		super.onOverlapX(temp, s2);
	}
	public static void main(String[] args)
	{
		GLProfile gl2Profile;

        try {
            // Make sure we have a recent version of OpenGL
            gl2Profile = GLProfile.get(GLProfile.GL2);
        }
        catch (GLException ex) {
            System.out.println("OpenGL max supported version is too low.");
            System.exit(1);
            return;
        }

        // Create the window and OpenGL context.
        GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
        window.setSize(640, 480);
        window.setTitle("Java Framework");
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

        // Setup OpenGL state.
        window.getContext().makeCurrent();
        GL2 gl = window.getGL().getGL2();
		Protagonist p = new Protagonist(gl);
		p.startAnimation(0);
		Frame f = p.changeAnimation(400);
		System.out.println(f.endNs);
		System.out.println(f.height);
		System.out.println(f.width);
		System.out.println(f.refNum);
	}
}
