
public class Camera {
	public int x;
	public int y; 
	public int width;
	public int height;
	public int maxWidth;
	public int maxHeight;
	boolean top = false;
	boolean bottom = false;
	boolean left = false;
	boolean right = false;
	public Camera(int maxWidth, int maxHeight, int width, int height, int x, int y){
		this.x = x; 
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		moveCamera(0,0);//to establish bounds
	}
	
	/**
	 * 
	 * @param deltaX
	 * @param deltaY
	 * @return true if the camera was able to move as requested, false otherwise.
	 */
	public boolean moveCamera(int deltaX, int deltaY)
	{
		x += deltaX;
		y += deltaY;
		if(x < 0)
		{
			left = true;
			x = 0;
		}
		if(y < 0)
		{
			top = true;
			y = 0;
		}
		if(x + width > maxWidth)
		{
			x = maxWidth - width;
			right = true;
		}
		if(y + height > maxHeight)
		{
			System.out.print("Bottom locked?");
			System.out.println(deltaX + ", " + deltaY );
			y = maxHeight - height;
			bottom = true;
		}
		return false;
	}
	public void center(double[] location){
		x = (int)(location[0] - .5 * width);
		y = (int)(location[1] - .5 * height);
		moveCamera(0,0);
	}
	/**
	 *
	 * @param spriteX
	 * @param spriteY
	 * @return an array of the directions they can move in the order up down left right to stay on screen.
	 * if any are false, given sprite is 
	 */
	public boolean[] canMove(double spriteX, double spriteY, double sWidth, double sHeight)
	{
		boolean[] ret = new boolean[4];
		ret[0] = spriteY > y;
		ret[1] = spriteY + sHeight < y + height;
		ret[2] = spriteX > x;
		ret[3] = spriteX + sWidth < x + width;
		return ret;
	}
	public void unlockcamera(){
		top = false;
		bottom = false;
		left = false;
		right = false;
	}
	public boolean isOnScreen(double spriteX, double spriteY, double sWidth, double sHeight)
	{
		return spriteY - sHeight > y || spriteY < y + height || spriteX - sWidth> x || sWidth < x + width;
	}
	public boolean[] pastLockedCoordinates(double sX, double sY, double sWidth, double sHeight)
	{
		boolean[] xyLocked = new boolean[]{!left && !right, !top && !bottom};
		if(top && sY > y + .5 * height)
		{
			xyLocked[1] = true;
			top = false;
			
		}
		if(bottom && sY + sHeight < y + .6 * height)
		{
			xyLocked[1] = true;
			bottom = false;
		}
		if(left && sX > x + .5 * width)
		{
			xyLocked[0] = true;
			left = false;
		}
		if(right && sX + sWidth < x + .5 * width)
		{
			xyLocked[0] = true;
			right = false;
		}
		return xyLocked;
	}
}
