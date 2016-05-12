import java.util.ArrayList;

import com.jogamp.opengl.GL2;

class Tile{
	public Tile(int refNum, int solidityType)
	{
		this.refNum = refNum;
		this.solidityType = solidityType;
	}
	int refNum;
	int solidityType;//0 for not solid, 1 for completely solid, 2 for diagonal left, 3 for diagonal right
	boolean[][] alphaGrid;
	//should I remove this later to add Sonic style curves?
	//kirby generally doesn't do that, but want to make interesting vistas. 
	// no transparent tiles for now, so...
	
	
}
public class Background {
ArrayList<ArrayList<Tile>> refNumArray;
int tilex;
int tiley;
public Background(GL2 gl){
	tilex = 31;
	tiley = 31;
	refNumArray = new ArrayList<ArrayList<Tile>>();
	for(int i = 0; i < 3; i++)
	{
		
		ArrayList<Tile> v = new ArrayList<Tile>();
		refNumArray.add(v);
		for(int j = 0; j < 4; j++)
		{
			Ret r = JavaFramework.glTexImageTGAFile(gl, "Kirby-World-Sprites/Sunset_Tiles-"+j+"-"+ i +".tga", new int[]{0,0});
			Tile t = new Tile(r.refnum, 1);
			t.alphaGrid = r.alphaGrid;
			v.add(t);
		}
	}
	for(int i = 0; i < 2; i++)
	{
		ArrayList<Tile> v = new ArrayList<Tile>();
		refNumArray.add(v);
		for(int j = 0; j < 3; j++)
		{
			Ret r = JavaFramework.glTexImageTGAFile(gl, "Kirby-World-Sprites/Clouds-"+j+"-"+ i +".tga", new int[]{0,0});
			Tile t = new Tile(r.refnum,0);
			t.alphaGrid = r.alphaGrid;
			v.add(t);
		}
	}
}
//0 - 11 is sunset tiles:
//12- 17 is clouds tiles; 13 + 14 = large cloud, 15 is blank blue, 16 is small cloud.
public int retrieveRef(int n){
	if(n < 12)
		return refNumArray.get(n / 4).get(n % 4).refNum;
	else
		return refNumArray.get(n / 3 - 1).get(n % 3).refNum;
}
public int retrieveSolidity(int n){
	if(n < 12)
		return refNumArray.get(n / 4).get(n % 4).solidityType;
	else
		return refNumArray.get(n / 3 - 1).get(n % 3).solidityType;
}
}