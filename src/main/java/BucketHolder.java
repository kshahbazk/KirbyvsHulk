import java.util.TreeSet;

/**
 * Created by johnfranklin on 5/18/16.
 */
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
		System.out.println(tempx + ", "+ tempy);
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
