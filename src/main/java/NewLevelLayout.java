/**
 * Created by johnfranklin on 5/15/16.
 */
public class NewLevelLayout extends LevelLayout {
    public NewLevelLayout()
    {
        super.layout = new int[8][8];
        int[][] v = super.layout;
        for(int i = 0; i < v.length; i++)
            for(int j = 0; j < v[0].length;j++)
            {
                if(i == 0)
                {
                    if(j == 0 || j == v[0].length - 1)
                    {

                    }
                }
            }
    }

}
