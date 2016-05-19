import java.util.LinkedList;

/**
 * Created by johnfranklin on 5/18/16.
 */
class Projectile extends Sprite{
	int prevcurtime = 0;
	int curtime = 0;

	public void actForTimePassed(int ns, LinkedList<AITask> aiStack)
	{
		curtime += ns;
		if(curtime - prevcurtime > 10000)
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
