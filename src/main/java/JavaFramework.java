/**
 * Created by jared on 2/9/16.
 */


import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
//What depth should we use?
//3? 4? 5??
class Ret{
	//define manually without constructor. need to anyways.
	int refnum;
	boolean[][] alphaGrid;
	
}
class BucketHolder
{
	TreeSet<Sprite>[][] totalBucketGrid;
	int totalWidth;
	int totalHeight;
	int individualWidth;
	int individualHeight;
	//For the sake of sanity, keep depth small. Too big and this will take a VERY long time.
	public BucketHolder(int depth, int w, int h)
	{
		int maxLength = (int) Math.pow(4, depth);
		System.out.println("Maxlength: "+ maxLength);
		totalWidth = w;
		totalHeight = h;
		individualWidth = totalWidth/maxLength;
		individualHeight = totalHeight/maxLength;
		totalBucketGrid = (TreeSet<Sprite>[][])(new TreeSet[maxLength][maxLength]);
		for(int i = 0; i < maxLength; i++)
			for(int j = 0; j < maxLength; j++)
				totalBucketGrid[i][j] = new TreeSet<Sprite>();
	}
	//if it were a HUGE sprite, it could theoretically end up in many buckets. most likely outcome is 1-4 buckets if it's sized correctly.
	//don't need to worry about traversal with this. can just relocate to all buckets with this method on frames when it leaves the bucket.
	// should just put in the first bucket that fits. set 
	public void add(Sprite s1){
		putInBuckets(s1);
	}
	public void putInBuckets(Sprite s1){
		int tempx = (int)(s1.loc[0]/individualWidth);
		int tempy = (int)(s1.loc[1]/individualHeight);
		if(tempx < 0)
			tempx = 0;
		if (tempy < 0)
			tempy = 0;
		if(tempx >= totalBucketGrid.length)
			tempx = totalBucketGrid.length - 1;
		if(tempy >= totalBucketGrid.length)
			tempy = totalBucketGrid[0].length - 1;
		
		totalBucketGrid[tempx][tempy].add(s1);
	}
	public void removeFromBuckets(Sprite s1){
		int tempx = (int)(s1.prevloc[0]/individualWidth);
		int tempy = (int)(s1.prevloc[1]/individualHeight);
		if(tempx < 0)
			tempx = 0;
		if (tempy < 0)
			tempy = 0;
		if(tempx >= totalBucketGrid.length)
			tempx = totalBucketGrid.length - 1;
		if(tempy >= totalBucketGrid.length)
			tempy = totalBucketGrid[0].length - 1;
		
		totalBucketGrid[tempx][tempy].remove(s1);
	}
	//only removes and adds if bucket changes. Can efficiently call on all elements in a bucket.
	public void moveToRightBucket(Sprite s1){
		if((int)s1.prevloc[0]/individualWidth != (int)s1.loc[0]/individualWidth || (int)s1.prevloc[1]/individualHeight != (int)s1.loc[1]/individualHeight)
		{
			removeFromBuckets(s1);
			putInBuckets(s1);
		}
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return an index showing the distance from index at the corner of the camera of the current buckets on screen.
	 * Let's replace this with an array 
	 */
	public int[] bucketsOnScreen(int x, int y, int w, int h)
	{	
		
		int[] ret = new int[4];
		ret[0] = x/individualWidth;
		if(ret[0] < 0)
			ret[0] = 0;
		ret[1] = y/individualHeight;
		if(ret[1] < 0)
			ret[1] = 0;
		ret[2] = (x + w)/individualWidth + 1;
		if(ret[2] > this.totalBucketGrid.length - 1)
			ret[2] = this.totalBucketGrid.length-1;
		ret[3] = (y + h)/individualHeight + 1;
		if(ret[3] > this.totalBucketGrid[0].length - 1)
			ret[3] = this.totalBucketGrid[0].length-1;
		//System.out.println(Arrays.toString(ret));
		return ret;
				
	}
}
class Sprite implements Comparable<Sprite>{
	public void actForTimePassed(int ns, LinkedList<AITask> l){}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deleteMe ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(gravity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(loc);
		temp = Double.doubleToLongBits(maxFallSpeed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxvel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(prevloc);
		result = prime * result + spriteRef;
		result = prime * result + Arrays.hashCode(spriteSize);
		result = prime * result + Arrays.hashCode(vel);
		return result;
	}
	public void setRandomDirection()
	{
		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sprite other = (Sprite) obj;
		if (deleteMe != other.deleteMe)
			return false;
		if (Double.doubleToLongBits(gravity) != Double
				.doubleToLongBits(other.gravity))
			return false;
		if (!Arrays.equals(loc, other.loc))
			return false;
		if (Double.doubleToLongBits(maxFallSpeed) != Double
				.doubleToLongBits(other.maxFallSpeed))
			return false;
		if (Double.doubleToLongBits(maxvel) != Double
				.doubleToLongBits(other.maxvel))
			return false;
		if (!Arrays.equals(prevloc, other.prevloc))
			return false;
		if (spriteRef != other.spriteRef)
			return false;
		if (!Arrays.equals(spriteSize, other.spriteSize))
			return false;
		if (!Arrays.equals(vel, other.vel))
			return false;
		return true;
	}
	boolean grounded = false;
	double gravity = 0.4;
	double maxvel = 20;//not used yet
	double maxFallSpeed = 10;
	public boolean deleteMe;
	public double[] loc = new double[2];
	public double[] prevloc = new double[2];
	public int[] spriteSize = new int[2];
	public int spriteRef;
	public double[] vel = new double[2];
	public boolean deleted;
	public ArrayList<Rectangle> possibleAABBTiles(Background b, LevelLayout l){
		ArrayList<Rectangle> allAABBs = new ArrayList<Rectangle>();
		
		int tempxmax = ((int)loc[0] + spriteSize[0])/b.tilex + 1;
		if(tempxmax > l.layout[0].length - 1)
			tempxmax = l.layout[0].length - 1;
		int tempymax = ((int)loc[1] + spriteSize[1])/b.tiley + 1;
		if(tempymax > l.layout.length - 1)
			tempymax = l.layout.length - 1;
		int tempxmin = (int)loc[0]/b.tilex - 1;
		if(tempxmin < 0)
			tempxmin = 0;
		int tempymin = (int)loc[1]/b.tiley - 1;
		if(tempymin < 0)
			tempymin = 0;
		for(int i = tempxmin; i <= tempxmax; i++)
		{
			//if(this instanceof Protagonist)
				//System.out.println("{");
			for(int j = tempymin; j <= tempymax; j++)
			{
				if(this instanceof Protagonist && JavaFramework.debug)
				{		
					JavaFramework.glDrawSprite(JavaFramework.gl, JavaFramework.getProjTex(), i * b.tilex -JavaFramework.c.x, j * b.tiley  -JavaFramework.c.y, b.tilex, b.tiley);
				}
				if(b.retrieveSolidity(l.layout[j][i]) != 0)
				{
					if(this instanceof Protagonist && JavaFramework.debug)
						JavaFramework.glDrawSprite(JavaFramework.gl, JavaFramework.getEnemyTex(), i * b.tilex -JavaFramework.c.x, j * b.tiley  -JavaFramework.c.y, b.tilex, b.tiley);
					allAABBs.add(new Rectangle(i * b.tilex, j * b.tiley, b.tilex,b.tiley));
				}
				
			}
			
		}
		//if(this instanceof Protagonist && allAABBs.size() > 0)
			//System.out.println(Arrays.toString(allAABBs.toArray()));		
		return allAABBs;
				
	}
	public int[] AABBoverlap(Rectangle r) {
		// TODO Auto-generated method stub
		return AABBoverlap(r.x,r.y,r.width,r.height);
	}
	public int[] AABBoverlap(int x, int y, int w, int h)
	{
		Sprite s = new Sprite();
		s.loc[0] = x;
		s.loc[1] = y;
		s.spriteSize[0] = w;
		s.spriteSize[1] = h;
		return AABBoverlap(s);
	}
	public int[] AABBoverlap(Sprite s2)
	{
		int[] ret = new int[]{0,0};
		//right
		double rightDelta = loc[0] + spriteSize[0] - s2.loc[0];
		//bottom
		double lowDelta = loc[1] + spriteSize[1] - s2.loc[1];
		double leftDelta =  s2.loc[0] + s2.spriteSize[0] - loc[0];
		//left
		double highDelta = s2.loc[1] + s2.spriteSize[1] - loc[1];
		if(rightDelta >= 0 && leftDelta >= 0 && highDelta >= 0 && lowDelta >= 0)
		{
			if(rightDelta > leftDelta)
				ret[0] = (int)leftDelta;
			else
				ret[0] = -(int)rightDelta;
			if(lowDelta > highDelta)
				ret[1] = (int)highDelta;
			else
				ret[1] = -(int)lowDelta;
		}
		return ret;
			
			
	}

	/**
	 * not intended to be universal, just to guarantee prevloc is up to date.
	 * 
	 */
	public void updateX()
	{
		prevloc[0] = loc[0];
		loc[0] += vel[0];
	}
	public void updateY()
	{
		vel[1] += gravity; 
		if(vel[1] > maxFallSpeed)
			vel[1] = maxFallSpeed;
		prevloc[1] = loc[1];
		loc[1] += vel[1];
	}
	public void onOverlapX(int[] temp, Sprite s2)//Problem with this definition: what do we do when we want multiple types of interaction varying per sprite?
	{//also: can't remove self!
		if(temp[0] != 0)// both will be defined when there's overlap
		{
			//System.out.println("x: " + loc[0] +" y: " + loc[1] + " spriteref: " + spriteRef + " --- other x: " + s2.loc[0] +" y: " + s2.loc[1] + "spriteref: " + s2.spriteRef);
			loc[0] = prevloc[0];
			vel[0] = 0;
		}
	}
	public void onOverlapY(int[] temp, Sprite s2)//Problem with this definition: what do we do when we want multiple types of interaction varying per sprite?
	{//also: can't remove self!
		if(temp[0] != 0)// both will be defined when there's overlap
		{
			//System.out.println("x: " + loc[0] +" y: " + loc[1] + " spriteref: " + spriteRef + " --- other x: " + s2.loc[0] +" y: " + s2.loc[1] + "spriteref: " + s2.spriteRef);
			loc[1] = prevloc[1];
			if(!grounded)
			{
				vel[1] = 0;
				grounded = true;
			}
		}
	}
	/*Worry about this later.
	 * public int[] AABBcalc(Sprite s2)
	{
		
	}*/
	@Override
	public int compareTo(Sprite obj) {
	// TODO Auto-generated method stub
		
		if (loc[0] - obj.loc[0] != 0)
		{
			return 100000* (int) (loc[0] - obj.loc[0]);
		}
		if (loc[1] - obj.loc[1] != 0)
			return 100000 * (int) (loc[1] - obj.loc[1]);
		if(this.spriteRef != obj.spriteRef)
			return this.spriteRef - obj.spriteRef;
		if (vel[0] - obj.vel[0] != 0)
			return 100000* (int) (vel[0] - obj.vel[0]);
		if (vel[1] - obj.vel[1] != 0)
			return 100000* (int) (vel[1] - obj.vel[1]);
		if(spriteSize[0] - obj.spriteSize[0] != 0)
			return spriteSize[0] - obj.spriteSize[0];
		if(spriteSize[1] - obj.spriteSize[1] != 0)
			return spriteSize[1] - obj.spriteSize[1];
		
		return 0;
		
	}
}
class Projectile extends Sprite{
	int prevcurtime = 0;
	int curtime = 0;
	
	public void actForTimePassed(int ns, LinkedList<AITask> aiStack)
	{
		curtime += ns;
		if(curtime - prevcurtime > 6000)
		{
			deleteMe = true;
		}
	}
	public Projectile()
	{
		super();
		gravity = 0;
	}
	public void onOverlapX(int[] overlapArr, Sprite s2)
	{
		super.onOverlapX(overlapArr, s2);
		if(s2 instanceof Enemy && (overlapArr[0] != 0))
		{
			deleteMe = true;
		}
	}
	public void onOverlapY(int[] overlapArr, Sprite s2)
	{
		super.onOverlapY(overlapArr, s2);
		if(s2 instanceof Enemy && (overlapArr[0] != 0))
		{
			deleteMe = true;
		}
	}
}

class Enemy extends Sprite{
	int prevcurtime = 0;
	int curtime = 0;
	
	public void actForTimePassed(int ns, LinkedList<AITask> aiStack)
	{
		curtime += ns;
		if(curtime - prevcurtime > 1000)
		{
			prevcurtime = curtime;
			RandomTask r = new RandomTask(this, 7);
			aiStack.addFirst(r);
		}
	}
	public void onOverlapX(int[] overlapArr, Sprite s2)
	{
		super.onOverlapX(overlapArr, s2);
		if(s2 instanceof Projectile && (overlapArr[0] != 0))
		{
			deleteMe = true;
		}
	}
	public void onOverlapY(int[] overlapArr, Sprite s2)
	{
		super.onOverlapY(overlapArr, s2);
		if(s2 instanceof Projectile && (overlapArr[0] != 0))
		{
			deleteMe = true;
		}
	}
}

class LaserEnemy extends Enemy
{
	boolean facingRight;
}

class Portal extends Sprite
{
	private double teleportXTile;
	private double teleportYTile;//Easier to think about this way.
	public Portal(int tileX, int tileY)//tileX will be green value, tileY will be red.
	{
		this.teleportXTile = tileX * JavaFramework.b.tilex  + JavaFramework.b.tilex * .5;//magic numbers, but can't give
		this.teleportYTile = tileY * JavaFramework.b.tilex + JavaFramework.b.tilex;

	}

	public void onOverlapX(int[] overlapArr, Sprite s2)
	{
		super.onOverlapX(overlapArr, s2);
		if(s2 instanceof Protagonist && (overlapArr[0] != 0))//WARP!!!
		{
			overlapCall();

		}
	}
	public void onOverlapY(int[] overlapArr, Sprite s2)
	{
		super.onOverlapY(overlapArr, s2);
		if(s2 instanceof Projectile && (overlapArr[0] != 0))
		{
			overlapCall();
		}
	}
	public void overlapCall()
	{
		JavaFramework.setProtPos(new double[]{teleportXTile, teleportYTile});
		JavaFramework.fixCamera();
	}
}
interface AITask{
	public void doTask();
}
class RandomTask implements AITask{
	
	
	Sprite s1;
	private int vel;
	public RandomTask(Sprite s1, int vel)
	{
		this.s1 = s1;
		this.vel = vel;
	}
	public void doTask(){
		
		double dirangle = Math.random() * 2 * Math.PI;
		s1.vel[0] = vel * Math.cos(dirangle);
		s1.vel[1] = vel * Math.sin(dirangle);
	}
}
public class JavaFramework {
	//disables graphics, draws debug info for collision.
    public static boolean debug = false;
	// Set this to true to force the game to exit.
    private static boolean shouldExit;
    static Camera c;
    // The previous frame's keyboard state.
    private static boolean kbPrevState[] = new boolean[256];

    // The current frame's keyboard state.
    private static boolean kbState[] = new boolean[256];

    // Position of the sprite.
    //private static int[] loc = new int[] { 700, 700 };
    private static int projectilesFired = 0;
    // Texture for the sprite.
    private static int enemyTex;
    private static int projTex;
    // Size of the sprite.
    private static int[] enemySpriteSize = new int[2];
    private static double[] delta = new double[]{2,8};
	private static LevelLayout l = new LevelLayout();//set here to avoid window loading before ready
    static GL2 gl;
/*
 * GOALS:
 * Has a scrolling, tiled background that is bigger than the screen.
 * Displays an animated sprite on top of the background:
 * Protagonist class:
 * has position in animation, time to next animation:
 * 
Uses the arrow keys to move the image around the window and has a way to control the camera as well.
Does not allow the camera to leave the world
 */
	static BucketHolder buckets;
	static Protagonist p;
	static Background b;
	static int scWidth = 640;
	static int scHeight = 480;
	static boolean fired = false;
    public static void main(String[] args){
        GLProfile gl2Profile;

    	LinkedList<AITask> aitasks = new LinkedList<AITask>();
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
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                kbState[keyEvent.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                kbState[keyEvent.getKeyCode()] = false;
            }
        });

        // Setup OpenGL state.
        window.getContext().makeCurrent();
        gl = window.getGL().getGL2();

        gl.glViewport(0, 0, scWidth,scHeight);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glOrtho(0, 640, 480, 0, 0, 100);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        
        // Load the texture.
        // loads by filename, not using image.
        // returns an integer which is used to draw the image by command when you tell the system later to draw something.
        // No actual storage of images in Java.
        boolean[][] enemyAlphaMap;
        Ret r = glTexImageTGAFile(gl, "Mega-Man-transparent.tga", enemySpriteSize);
        int enemyTex = r.refnum;
        enemyAlphaMap = r.alphaGrid;
        int[] projSpriteSize = new int[2];
        boolean[][] projAlphaMap;
        r = glTexImageTGAFile(gl, "Kirby-Sprites/laser.tga", projSpriteSize);
        projSpriteSize[0] = 30;
		setProjTex(r.refnum);
		projAlphaMap =r.alphaGrid;
        // Textures for background?
        // Camera: 
        // The game loop
        long lastFrameNS;
        long currentFrameNS = System.nanoTime();
        long currentPhysicsFrameNS = currentFrameNS;
        b = new Background(gl);
        p = new Protagonist(gl);
        System.out.println(Arrays.toString(enemySpriteSize));
		buckets = new BucketHolder(2, l.layout[0].length * b.tilex,l.layout.length * b.tiley);
        for(int i = 0; i < 30; i++)
        {
        	Enemy p1 = new Enemy();
        	p1.loc[0] = (int) (Math.random() * (l.layout[0].length * b.tilex - enemySpriteSize[0]));
        	p1.loc[1] = (int) (Math.random() * (l.layout.length * b.tiley - enemySpriteSize[1]));
        	p1.vel[0] = -1 + (int) (Math.random() * 3);
        	p1.vel[1] = -1 + (int) (Math.random() * 3);
        	p1.spriteSize = enemySpriteSize;
        	p1.spriteRef = enemyTex;
        	System.out.println(p1.loc[0]);
        	System.out.println(p1.loc[1]);
        	System.out.println(p1.vel[0]);
        	System.out.println(p1.vel[1]);
        	
        	buckets.add(p1);
        }
        c = new Camera(l.layout[0].length * b.tilex, l.layout.length * b.tiley, scWidth, scHeight, (int)(p.loc[0] - 0.5 * scWidth), (int)(p.loc[1] - 0.5 * scHeight ));
        
        System.out.println(l);
        while (!shouldExit) {
            System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
            lastFrameNS = currentFrameNS;
            window.display();
            // Actually, this runs the entire OS message pump.
           
            if (!window.isVisible()) {
                shouldExit = true;
                break;
            }
            currentFrameNS = System.nanoTime();
            int deltaTimeNS = (int) ((currentFrameNS - lastFrameNS) / 1000000);
            // Game logic.
            if (kbState[KeyEvent.VK_ESCAPE]) {
                shouldExit = true;
            }
           
            //Putting in raw values for now due to chicken and egg scenario; 
            //can't know current frame until after inputs, but result of camLockedIn depends on current frame's size!
            // we could have a previous frame and work from that, but not needed right now.
            boolean[] directions;
            // moving this out of the do while loop so I can handle
            int[] bucketsOnScreen = buckets.bucketsOnScreen(c.x - 50, c.y - 50, c.width + 50, c.height + 50);
        	ArrayList<Sprite> enemies = new ArrayList<Sprite>();
        	System.out.println(bucketsOnScreen[0]);
        	System.out.println(bucketsOnScreen[1]);
        	int originalYstart = bucketsOnScreen[1];
			ArrayList<LaserEnemy> laserEnemiesOnScreen = new ArrayList<LaserEnemy>();

        	while(bucketsOnScreen[0] < bucketsOnScreen[2])
        	{
        		while(bucketsOnScreen[1] < bucketsOnScreen[3])
            	{
            		enemies.addAll(buckets.totalBucketGrid[bucketsOnScreen[0]][bucketsOnScreen[1]]);
            		bucketsOnScreen[1]++;
            	}
        		bucketsOnScreen[1] = originalYstart;
        		bucketsOnScreen[0]++;
        		
        	}
        	//check all enemies on screen for AI tasks.
        	// Do we want to check all enemies?
        	// guess it's up to me.
        	for(int i = 0; i < enemies.size(); i++)
        	{
        		enemies.get(i).actForTimePassed(deltaTimeNS,aitasks);
        	}
            do{
            	directions = c.canMove(p.loc[0], p.loc[1], 24, 20);
            	
            	
            	//extra leeway given to handle situations where a character is out of his bucket but it's not on screen
            	
            	
            	//position of protagonist is stable at this point?
            	// move camera afterwards?
            	//p background check
            	if((p.vel[0] < 0 && !directions[2]) || (p.vel[0] > 0 && !directions[3]))
            		p.vel[0] = 0;
            	if((p.vel[1] < 0 && !directions[0]) || (p.vel[1] > 0 && !directions[1]))
            		p.vel[1] = 0;
            	p.updateX();
            	ArrayList<Rectangle> a = p.possibleAABBTiles(b, l);
            	//System.out.println("{");
            	for(int q = 0; q < a.size(); q++)
            	{
            		System.out.println(q);
            		System.out.println(a.size());
            		int[] temp = p.AABBoverlap(a.get(q));
            		//System.out.println(temp[0]);
            		if(temp[0] != 0)
            		{
            			
            			p.onOverlapX(temp, null);
            			break;
            		}
            	}
            	//System.out.println("}");
				projectilesFired = 0;
            	for(int i = 0; i < enemies.size(); i++)
            	{

            		Sprite current1 = enemies.get(i);
            		
            		if(current1.deleteMe)
            		{
            			buckets.removeFromBuckets(current1);
            			enemies.remove(i);
            			if(i >= enemies.size())
            				break;//last element, continuing to avoid overflow
            			current1 = enemies.get(i);
            			
            		}
            		if(current1 instanceof Projectile)
            			projectilesFired++;
					else if(current1 instanceof LaserEnemy)
					{
						laserEnemiesOnScreen.add((LaserEnemy)current1);
					}
            		current1.updateX();
        			ArrayList<Rectangle> a2 = current1.possibleAABBTiles(b, l);
                	for(int v = 0; v < a2.size(); v++)
                	{
                		int[] temp = current1.AABBoverlap(a2.get(v));
                		if(temp[0] != 0)
                		{
                			//glDrawSprite(gl, projTex, a.get(v).x - c.x, a.get(v).y - c.y, a.get(v).width, a.get(v).height);
                			//glDrawSprite(gl, projTex, p.loc[0] - c.x, p.loc[1] - c.y, p.spriteSize[0], p.spriteSize[1]);
                			current1.onOverlapX(temp, null);
                			break;
                		}
                	}
                	
                	
            		int[] temp = p.AABBoverlap(current1);
            		p.onOverlapX(temp, current1);
            		current1.onOverlapX(temp, p);
            		//enemy background check
            		
            		for(int j = i - 1; j >= 0; j--)
            		{
            			Sprite current2 = enemies.get(j);		
            			int[] temp2 = current2.AABBoverlap(current1);
            			current2.onOverlapX(temp2, current1);
            			current1.onOverlapX(temp2, current2);
            		}
            		
            		
            	}
            	p.updateY();
            	
            	for(int i = 0; i < a.size(); i++)
            	{
            		int[] temp = p.AABBoverlap(a.get(i));
            		//glDrawSprite(gl, projTex, a.get(i).x - c.x, a.get(i).y - c.y, a.get(i).width, a.get(i).height);
        			
            		if(temp[0] != 0)
            		{
            			//glDrawSprite(gl, projTex, a.get(i).x - c.x, a.get(i).y - c.y, a.get(i).width, a.get(i).height);
            			//glDrawSprite(gl, projTex, p.loc[0] - c.x, p.loc[1] - c.y, p.spriteSize[0], p.spriteSize[1]);
            			p.onOverlapY(temp, null);
            			break;
            		}
            	}
            	for(int i = 0; i < enemies.size(); i++)
            	{
            		Sprite current1 = enemies.get(i);
            		current1.updateY();
        			ArrayList<Rectangle> a2 = current1.possibleAABBTiles(b, l);
                	for(int v = 0; v < a2.size(); v++)
                	{
                		int[] temp = current1.AABBoverlap(a2.get(v));
                		if(temp[0] != 0)
                		{
                			current1.onOverlapY(temp, null);
                			break;
                		}
                	}
                	
                	
            		int[] temp = p.AABBoverlap(current1);
            		p.onOverlapY(temp, current1);
            		current1.onOverlapY(temp, p);
            		//enemy background check
            		
            		for(int j = i - 1; j >= 0; j--)
            		{
            			Sprite current2 = enemies.get(j);		
            			int[] temp2 = current2.AABBoverlap(current1);
            			current2.onOverlapY(temp2, current1);
            			current1.onOverlapY(temp2, current2);
            			
            		}
            		buckets.moveToRightBucket(current1);
            		
            	}
            	
            	boolean[] camLockedIn = c.pastLockedCoordinates(p.loc[0], p.loc[1], 24, 20);
        		
            	c.moveCamera(camLockedIn[0] ? (int)(p.loc[0] - p.prevloc[0]) : 0 ,camLockedIn[1] ? (int)(p.loc[1] - p.prevloc[1]): 0);
            	//System.out.println("In loop!");
            	currentPhysicsFrameNS += 10000000;
            }while(currentPhysicsFrameNS < currentFrameNS);
           // System.out.println("Out of loop!");
            
            directions = c.canMove(p.loc[0], p.loc[1], 24, 20);
            if (directions[2] && kbState[KeyEvent.VK_LEFT]) {
            	
            	if(p.curAnimType != 0)
            	{
            		p.startAnimation(0);
            	}
            	
                p.vel[0] = -delta[0];
            }
            if (directions[3] && kbState[KeyEvent.VK_RIGHT]) {
                p.vel[0] = delta[0];
                if(p.curAnimType != 1)
            	{
            		p.startAnimation(1);
            	}
                //loc[0] -= 1;
            }
           
            if(!kbState[KeyEvent.VK_RIGHT] && !kbState[KeyEvent.VK_LEFT])
            {
            	if(p.curAnimType == 1)
            		p.startAnimation(3);
            	if(p.curAnimType == 0)
            		p.startAnimation(2);
            }
            
            if (directions[0] && kbState[KeyEvent.VK_UP] && !kbPrevState[KeyEvent.VK_UP] && (p.grounded || debug)) {
                p.vel[1] = -delta[1];
                p.grounded = false;
            }


            if(kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE] && projectilesFired < 4)
            {

				//Add line firing projectiles for each firingEnemy
            	Projectile newp = new Projectile();
            	System.out.println(p.curAnimType);
            	System.out.println(p.curAnimType%2);

					newp.loc = new double[]{p.loc[0] + ((p.curAnimType % 2) * p.spriteSize[0]) + ((p.curAnimType + 1) % 2) * -projSpriteSize[0], p.loc[1] + p.spriteSize[1] / 2};
					System.out.println(Arrays.toString(newp.loc));
					newp.vel = new double[]{(p.curAnimType % 2) * 3 + ((p.curAnimType + 1) % 2) * -3, 0};
					newp.spriteSize = new int[2];
					newp.spriteSize[0] = projSpriteSize[0];
					newp.spriteSize[1] = projSpriteSize[1];
				if(!fired) {
					newp.spriteSize[0] = newp.spriteSize[0] * 3 / 4;
				}
				fired = !fired;
            	newp.spriteRef = getProjTex();

            	buckets.add(newp);
            }
            
            if (!kbState[KeyEvent.VK_LEFT] && !kbState[KeyEvent.VK_RIGHT]) {
                p.vel[0] = 0;
            }
            //gl.glClearColor(0, 0, 0, 1);
            //gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            Frame f = p.changeAnimation(deltaTimeNS);
            //System.out.print("{");
            int camTileMaxX = (c.x + c.width)/b.tilex + 1;
            int camTileMaxY = (c.y + c.height)/b.tiley + 1;
            //if(!debug)
	            for(int i = c.y/ b.tiley; i < Math.min(camTileMaxY,l.layout.length); i++)
	            {
	            	//System.out.print("{");
	            	for(int j = c.x / b.tilex; j < Math.min(camTileMaxX,l.layout[i].length); j++)
	            	{
	            		int vv = b.retrieveRef(l.layout[i][j]);
	            		//System.out.print(vv + ", ");
	            		glDrawSprite(gl, vv, j * b.tilex - c.x, i * b.tiley - c.y, b.tilex, b.tiley);
	            	}
	            	//System.out.println("}");
	            }
            //System.out.print("}");
	           
            int newNs = 0;
            while(newNs < currentFrameNS + 10000000 && aitasks.size() > 0){
	        	newNs = (int)System.nanoTime();
	        	AITask s = aitasks.removeLast();
	        	s.doTask();
	        }
            bucketsOnScreen = buckets.bucketsOnScreen(c.x - 50, c.y - 50, c.width + 50, c.height + 50);
            //System.out.println(Arrays.toString(bucketsOnScreen));
            System.out.println("Drawn x top = " + bucketsOnScreen[0]);
            System.out.println("Drawn y top = " + bucketsOnScreen[1]);
            System.out.println("Drawn x bottom = " + bucketsOnScreen[2]);
            System.out.println("Drawn y bottom = " + bucketsOnScreen[3]);
            
            Sprite current;
            originalYstart = bucketsOnScreen[1];
            while(bucketsOnScreen[0] <= bucketsOnScreen[2])
        	{
        		while(bucketsOnScreen[1] <= bucketsOnScreen[3])
            	{
        			if(debug)
        			{
        				System.out.println("x coordinate:"+bucketsOnScreen[0] * buckets.individualWidth);
        				glDrawSprite(gl, projTex, bucketsOnScreen[0] * buckets.individualWidth - c.x, bucketsOnScreen[1] * buckets.individualHeight - c.y, buckets.individualWidth, buckets.individualHeight);
        			}
        			Iterator<Sprite> iter = buckets.totalBucketGrid[bucketsOnScreen[0]][bucketsOnScreen[1]].iterator();
            		while(iter.hasNext())
            		{
            			//System.out.println("In iterator!");
            			current = iter.next();

            				if(c.isOnScreen(current.loc[0], current.loc[1], current.spriteSize[0], current.spriteSize[1]) && !debug)
            					glDrawSprite(gl, current.spriteRef, current.loc[0] - c.x, current.loc[1] - c.y, current.spriteSize[0], current.spriteSize[1]);	

            		}
            		bucketsOnScreen[1]++;
            	}
        		//System.out.println("x coordinate after loop:"+bucketsOnScreen[0] * buckets.individualWidth);
        		bucketsOnScreen[1] = originalYstart;
        		bucketsOnScreen[0]++;
        		//System.out.println("x coordinate after increment:"+bucketsOnScreen[0] * buckets.individualWidth);
        		//System.out.println("x end coordinate after increment:"+bucketsOnScreen[2] * buckets.individualWidth);	
        	}
            glDrawSprite(gl, f.refNum, p.loc[0] - c.x, p.loc[1] - c.y, f.width, f.height);
            
            //System.out.println(f);
            // Present to the player.
            //window.swapBuffers();
            
            
        }
        System.exit(0);
    }
    //method to cast to int for double values for drawing.
    private static void glDrawSprite(GL2 gl, int spriteRef, double d,
			double e, int w, int h) {
		// TODO Auto-generated method stub
		glDrawSprite(gl, spriteRef, (int)d, (int)e, w, h);
	}

	// Load a file into an OpenGL texture and return that texture.
    public static Ret glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
    	final int BPP = 4;
    	Ret r = new Ret();
        DataInputStream file = null;
        try {
            // Open the file.
            file = new DataInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.err.format("File: %s -- Could not open for reading.", filename);
            return null;
        }

        try {
            // Skip first two bytes of data we don't need.
            file.skipBytes(2);

            // Read in the image type.  For our purposes the image type
            // should be either a 2 or a 3.
            int imageTypeCode = file.readByte();
            if (imageTypeCode != 2 && imageTypeCode != 3) {
                file.close();
                System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
                return null;
            }

            // Skip 9 bytes of data we don't need.
            file.skipBytes(9);

            int imageWidth = Short.reverseBytes(file.readShort());
            int imageHeight = Short.reverseBytes(file.readShort());
            int bitCount = file.readByte();
            file.skipBytes(1);

            // Allocate space for the image data and read it in.
            byte[] bytes = new byte[imageWidth * imageHeight * BPP];
            r.alphaGrid = new boolean[imageWidth][imageHeight];
            
            // Read in data.
            if (bitCount == 32) {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = file.readByte();
                   
                 // Read in data.
                  boolean isNonZero = (bytes[it * BPP + 3] != 0);
                  r.alphaGrid[it % imageWidth][it / imageWidth] = isNonZero;
                 
                }
            } else {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = -1;
                }
            }

            file.close();

            // Load into OpenGL
            int[] texArray = new int[1];
            gl.glGenTextures(1, texArray, 0);
            r.refnum = texArray[0];
            gl.glBindTexture(GL2.GL_TEXTURE_2D, r.refnum);
            gl.glTexImage2D(
                    GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0,
                    GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

            out_size[0] = imageWidth;
            out_size[1] = imageHeight;
            return r;
        }
        catch (IOException ex) {
            System.err.format("File: %s -- Unexpected end of file.", filename);
            return null;
        }
    }

    public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glColor3ub((byte)-1, (byte)-1, (byte)-1);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2i(x, y);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2i(x + w, y);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2i(x + w, y + h);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2i(x, y + h);
        }
        gl.glEnd();
    }
	public static int getProjTex() {
		return projTex;
	}
	public static void setProjTex(int projTex) {
		JavaFramework.projTex = projTex;
	}
	public static int getEnemyTex() {
		return enemyTex;
	}
	public static void setEnemyTex(int enemyTex) {
		JavaFramework.enemyTex = enemyTex;
	}

	public static void setLayout(LevelLayout layout) {
		l = layout;
	}

	public static void setProtPos(double[] ints) {
		p.loc = ints;
	}

	public static void fixCamera() {
		c = new Camera(l.layout[0].length * b.tilex, l.layout.length * b.tiley, scWidth, scHeight, (int)(p.loc[0] - 0.5 * scWidth), (int)(p.loc[1] - 0.5 * scHeight ));
	}

	public static void generateEnemies() {

	}
}
