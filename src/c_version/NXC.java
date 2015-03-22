package c_version;

public class NXC
{
	private static final int COLUMNS = 18;
	private static final int ROWS = 9;
	private static final int ARRAY_LENGTH = 400;

	private static int[][] map = { { 3, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 0, 0 }, { 3, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 3, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 3, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 3, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 3, 0, 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 3, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public static void main(String[] args)
	{
//		int[][] mapToCheck = {{0,0},{1,1},{2,2},{3,3},{4,4}};
//		System.out.println(mapToCheck.length);
//
//		int[][] newMap = remove(mapToCheck, 4);
//		for(int[] x:mapToCheck)
//		{
//			for(int num:x)
//			{
//				System.out.print(num);
//			}
//			System.out.println("");
//		}
//		System.out.println(newMap);
		
	}

	public int[][] aStartAlgorithm(int startX, int startY, int finishX,
			int finishY)
	{
		int[][] openSet = new int[ARRAY_LENGTH][2];
		int[][] closedSet = new int[ARRAY_LENGTH][2];
		int[][] gScoresMap = new int[COLUMNS][ROWS];
		int[][] fScoresMap = new int[COLUMNS][ROWS];

		int openSetHead = -1;
		int openSetTail = 0;

		int closedSetHead = 0;
		int closedSetTail = 0;

		// Initialising
		for (int i = 0; i < ARRAY_LENGTH; i++)
		{
			openSet[i][0] = -1;
			openSet[i][1] = -1;
			closedSet[i][0] = -1;
			closedSet[i][1] = -1;
		}

		// Adding nodes not accessible into closedSet!
		for (int X = 0; X < COLUMNS; X++)
		{
			for (int Y = 0; Y < ROWS; Y++)
			{
				if ( map[X][Y] == 0 )
				{
					closedSet[closedSetTail][0] = X;
					closedSet[closedSetTail][1] = Y;
					closedSetTail++;
				}
			}
		}

		// Adding start point to openSet.
		openSet[openSetTail][0] = startX;
		openSet[openSetTail][1] = startY;
		openSetTail++;
		openSetHead++;

		while (openSetHead > -1)
		{
			int[] current = getMinHeuristic(openSet, gScoresMap, openSetHead,
					openSetTail);

			int currentX = current[0];
			int currentY = current[1];
			int index = current[2];

			if ( currentX == finishX && currentY == finishY )
			{
				// TO-DO
				// Implement reconstructing the path
				// listOfNodesToFollow = reconstructPath(current);
				System.out.println("We have reached the finish! YAAAAY");
			}
			
			openSet = remove(openSet, index);
			closedSet[closedSetTail][0] = currentX;
			closedSet[closedSetTail][1] = currentY;
			closedSetTail++;
		}

		return null;
	}

	public int calculateShortestDistanceTo(int startX, int startY, int finishX,
			int finishY)
	{
		return Math.abs(startX - finishX) + Math.abs(startY - finishY);
	}

	public int[] getMinHeuristic(int[][] list, int[][] gScoreMap, int head,
			int tail)
	{
		int[] arrayToReturn = new int[3];
		int currX = list[head][0];
		int currY = list[head][1];
		int candidate = 0;
		int currentMin = gScoreMap[currX][currY];

		arrayToReturn[0] = currX;
		arrayToReturn[1] = currY;
		arrayToReturn[2] = head;

		for (int i = head + 1; i < tail; i++)
		{
			currX = list[i][0];
			currY = list[i][1];
			candidate = gScoreMap[currX][currY];

			if ( currentMin > candidate )
			{
				currentMin = candidate;
				arrayToReturn[0] = currX;
				arrayToReturn[1] = currY;
				arrayToReturn[2] = i;
			}
		}
		return arrayToReturn;
	}

	public static int[][] remove(int[][] openSet, int indexToRemove)
	{
		int[][] matrixToReturn = openSet;
		for (int i = indexToRemove ; i < ARRAY_LENGTH-1; i++) 
		{
			matrixToReturn[i][0] = openSet[i+1][0];
			matrixToReturn[i][1] = openSet[i+1][1];
		}
		matrixToReturn[ARRAY_LENGTH-1][0] = -1;
		matrixToReturn[ARRAY_LENGTH-1][1] = -1;
		return matrixToReturn;
	}
}
