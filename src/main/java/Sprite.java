import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by johnfranklin on 5/18/16.
 */
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
	double gravity = 0.37;
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
			vel[1] = 0;
			if(!grounded)
			{
				JavaFramework.c.moveCamera(0,1);
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
