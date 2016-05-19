import java.util.LinkedList;

/**
 * Created by johnfranklin on 5/18/16.
 */
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
