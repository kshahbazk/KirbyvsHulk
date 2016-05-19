import java.util.LinkedList;

/**
 * Created by johnfranklin on 5/18/16.
 */
class LaserEnemy extends Enemy
{
	boolean facingRight;
	public void actForTimePassed(int ns, LinkedList<AITask> aiStack)
	{}
	public LaserEnemy(boolean facingRight)
	{
		this.facingRight = facingRight;
		if(facingRight)
			this.spriteRef = JavaFramework.glTexImageTGAFile(JavaFramework.gl, "Kirby-Enemy/Laser-Enemy-Right.tga", spriteSize).refnum;
		else
			this.spriteRef = JavaFramework.glTexImageTGAFile(JavaFramework.gl, "Kirby-Enemy/Laser-Enemy-Left.tga", spriteSize).refnum;
		spriteSize = new int[]{31,31};
	}
}
