/**
 * Created by johnfranklin on 5/18/16.
 */
class Portal extends Sprite
{
	private int teleportXTile;
	private int teleportYTile;//Easier to think about this way.

	public Portal(int tileX, int tileY)//tileX will be green value, tileY will be red.
	{
		this.gravity = 0;
		this.teleportXTile = tileX;//magic numbers, but can't give
		this.teleportYTile = tileY;
		this.spriteRef = JavaFramework.glTexImageTGAFile(JavaFramework.gl, "Kirby-Enemy/Portal.tga", spriteSize).refnum;
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
		if(s2 instanceof Protagonist && (overlapArr[1] != 0))
		{
			overlapCall();
		}
	}
	public void overlapCall()
	{
		MusicPlayer.linking.play();
		JavaFramework.moveToTile(new int[]{teleportXTile, teleportYTile});
	}
}
