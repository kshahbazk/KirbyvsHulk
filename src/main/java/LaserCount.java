import java.util.LinkedList;

/**
 * Created by johnfranklin on 5/18/16.
 */
class LaserCount extends Sprite{

	public void actForTimePassed(int ns, LinkedList<AITask> aiStack)
	{
		spriteRef = numberarray[JavaFramework.projectilesFired % numberarray.length];

	}
	static int[] spriteSizeStatic = new int[]{25,25};
	static int[] numberarray = new int[]{JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/0.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/1.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/2.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/3.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/4.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/5.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/6.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/7.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/8.tga", new int[2]).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/9.tga", new int[2]).refnum};
	public LaserCount()
	{
		spriteSize = LaserCount.spriteSizeStatic;
		gravity = 0;
	}

}
