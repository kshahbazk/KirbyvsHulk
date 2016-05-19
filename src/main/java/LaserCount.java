/**
 * Created by johnfranklin on 5/18/16.
 */
class LaserCount extends Sprite{
	static int[] spriteSizeStatic = new int[2];
	static int[] numberarray = new int[]{JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/0.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/1.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/2.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/3.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/4.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/5.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/6.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/7.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/8.tga", spriteSizeStatic).refnum,JavaFramework.glTexImageTGAFile(JavaFramework.gl,"Kirby-Fonts/9.tga", spriteSizeStatic).refnum};
	public LaserCount()
	{
		spriteSize = LaserCount.spriteSizeStatic;
	}
	void updateSprite(int currcount){
		spriteRef = numberarray[currcount];
	}

}
