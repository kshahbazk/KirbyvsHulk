/**
 * Created by johnfranklin on 5/18/16.
 */
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
