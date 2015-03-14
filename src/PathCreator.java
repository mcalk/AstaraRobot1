import java.util.ArrayList;
import java.util.LinkedList;

public class PathCreator
{
	private ArrayList<String> listOfInstructions;

	public PathCreator(LinkedList<Square> list)
	{
		listOfInstructions = listOfInstructions(list);
		
	}

	private ArrayList<String> listOfInstructions(LinkedList<Square> list)
	{
		listOfInstructions = new ArrayList<String>();

		int cnt = 0;
		Square prev = null;

		for (Square sqr : list)
		{
			if ( cnt == 0 )
			{
				prev = sqr;
				cnt++;
			}
			else
			{
				int xDiff = sqr.getxPos() - prev.getxPos();
				int yDiff = sqr.getyPos() - prev.getyPos();

				if ( xDiff == -1 )
					listOfInstructions.add("GO LEFT");
				else if ( xDiff == 1 )
					listOfInstructions.add("GO RIGHT");
				else if ( yDiff == -1 )
					listOfInstructions.add("GO UP");
				else if ( yDiff == 1 )
					listOfInstructions.add("GO DOWN");

				prev = sqr;
			}
		}

		return listOfInstructions;
	}

	public void printInstructions()
	{
		for(String str:listOfInstructions)
		{
			System.out.println(str);
		}
	}
}
