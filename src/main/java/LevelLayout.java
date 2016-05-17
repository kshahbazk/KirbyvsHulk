import java.awt.Point;
import java.util.Arrays;


public class LevelLayout {
	@Override
	public String toString() {
		String str = "[";
		for(int i = 0; i < layout.length; i++)
		{
			str += "[";
			for(int j = 0; j < layout[i].length; j++)
			{
				str += layout[i][j] + ",";
			}
			str += "]";
		}
		str += "]";
		return str;
	}
	public int[][] layout;
	public Sprite[][] enemylayout;
	public LevelLayout()
	{
		enemylayout = new Sprite[30][200];
		layout = new int[30][200];
		boolean largeCloudStarted = false;
		for(int i = 0; i < layout.length - 2; i++)
		{
			for(int j = 0; j < layout[i].length; j++)
			{
				int roll = (int)(Math.random() * 20);
				if(largeCloudStarted)
				{
					layout[i][j] = 14;
					largeCloudStarted = false;
				}
				else if(roll > 18)
				{
					layout[i][j] = 13;
					largeCloudStarted = true;
				}	
				else if (roll > 17)
				{
					layout[i][j] = 15;
				}
				else
				{
					layout[i][j] = 17;
				}
			}
		}
		layout[layout.length - 2][0] = 0;
		layout[layout.length - 1][0] = 4;
		int count = 0;
		for(int j = 1; j < layout[layout.length - 2].length - 1; j++)
		{
			if(count == 0 && Math.random() * 5 < 2)
				count = 2;
			if(count == 2)
			{
				count--;
				//enemylayout[layout.length - 6][j] = new Enemy();
				layout[layout.length - 6][j] = 0;
				layout[layout.length - 5][j] = 8;
				layout[layout.length - 3][j] = 0;
				layout[layout.length - 2][j] = 5;
			}
			else if(count == 1)
			{
				count--;
				layout[layout.length - 6][j] = 2;
				layout[layout.length - 5][j] = 10;
				layout[layout.length - 3][j] = 2;
				layout[layout.length - 2][j] = 5;
			}
			else
			{
				layout[layout.length - 2][j] = 1;
			}
			layout[layout.length - 1][j] = 5;
			
		}
		layout[layout.length - 2][layout[layout.length - 2].length - 1] = 2;
		layout[layout.length - 1][layout[layout.length - 1].length - 1] = 6;
		//layout = new int[][]{{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14},{15,16,17}};
	}
	
}
