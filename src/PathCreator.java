import java.util.ArrayList;
import java.util.LinkedList;

public class PathCreator
{
	private ArrayList<String> listOfInstructions;

	public PathCreator(LinkedList<Square> list)
	{
		listOfInstructions = listOfInstructions(list);

	}

	public enum Direction
	{
		UP, DOWN, RIGHT, LEFT
	}

	private ArrayList<String> listOfInstructions(LinkedList<Square> list)
	{
		listOfInstructions = new ArrayList<String>();

		Square prev = null;
		Direction direction = Direction.UP;

		for (Square sqr : list)
		{
			if ( prev == null )
			{
				prev = sqr;
				continue;
			}
			else
			{
				int xDiff = sqr.getxPos() - prev.getxPos();
				int yDiff = sqr.getyPos() - prev.getyPos();

				if ( xDiff == -1 )
				{
					if ( direction == Direction.UP )
					{
						listOfInstructions.add("TURN LEFT");
						direction = Direction.LEFT;
					}
					if ( direction == Direction.DOWN )
					{
						listOfInstructions.add("TURN RIGHT");
						direction = Direction.RIGHT;
					}
				}
				else if ( xDiff == 1 )
				{
					if ( direction == Direction.DOWN )
					{
						listOfInstructions.add("TURN LEFT");
						direction = Direction.LEFT;
					}
					if ( direction == Direction.UP )
					{
						listOfInstructions.add("TURN RIGHT");
						direction = Direction.RIGHT;
					}

				}

				else if ( yDiff == -1 )
				{
					if ( direction == Direction.LEFT )
					{
						listOfInstructions.add("TURN LEFT");
						direction = Direction.DOWN;
					}
					if ( direction == Direction.RIGHT )
					{
						listOfInstructions.add("TURN RIGHT");
						direction = Direction.UP;
					}
				}
				else if ( yDiff == 1 )
				{
					if ( direction == Direction.LEFT )
					{
						listOfInstructions.add("TURN LEFT");
						direction = Direction.UP;
					}
					if ( direction == Direction.RIGHT )
					{
						listOfInstructions.add("TURN RIGHT");
						direction = Direction.DOWN;
					}
				}
				prev = sqr;
				listOfInstructions.add("GO FORWARD");
			}

		}

		return listOfInstructions;
	}

	public void printInstructions()
	{
		for (String str : listOfInstructions)
		{
			System.out.println(str);
		}
	}
}
