package c_version;

public class NXC {
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

	public static void main(String[] args) {

		int counter = 0;

		int[][] arrayToFollow = aStarAlgorithm(0, 0, 13, 3);
		for (int[] row : arrayToFollow) {
			if (row[0] != -1) {
				System.out.println(row[0] + " " + row[1]
						+ " , parrent nodes -> " + row[2] + " " + row[3]);
				counter++;
			}
		}

		int[][] arrayToFollow2 = findPath(arrayToFollow, counter);
		for (int[] row : arrayToFollow2) {

			if (row[0] != -1) {

				System.out.println(row[0] + " " + row[1]);
			}
		}

	}

	public static int[][] aStarAlgorithm(int startX, int startY, int finishX,
			int finishY) {
		int[][] openSet = new int[ARRAY_LENGTH][2];
		int openSetHead = -1;
		int openSetTail = 0;

		int[][] closedSet = new int[ARRAY_LENGTH][2];
		int closedSetHead = 0;
		int closedSetTail = 0;

		// int[][] listOfNodesToFollow = new int[ARRAY_LENGTH][2];
		// int listOfNodesPointer = 0;

		int[][] cameFrom = new int[ARRAY_LENGTH][4];
		int cameFromPointer = 0;

		// TO-DO This are not initialised
		int[][] gScoresMap = new int[COLUMNS][ROWS];
		int[][] fScoresMap = new int[COLUMNS][ROWS];

		// Initialising
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			openSet[i][0] = -1;
			openSet[i][1] = -1;
			closedSet[i][0] = -1;
			closedSet[i][1] = -1;
			// listOfNodesToFollow[i][0] = -1;
			// listOfNodesToFollow[i][1] = -1;
			// listOfNodesToFollow[i][2] = -1;
			// listOfNodesToFollow[i][3] = -1;

			cameFrom[i][0] = -1;
			cameFrom[i][1] = -1;
			cameFrom[i][2] = -1;
			cameFrom[i][3] = -1;
		}

		// Adding nodes not accessible into closedSet!
		for (int X = 0; X < COLUMNS; X++) {
			for (int Y = 0; Y < ROWS; Y++) {
				if (map[X][Y] == 0) {
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

		while (openSetHead > -1) {
			int[] current = getMinHeuristic(openSet, fScoresMap, openSetHead,
					openSetTail);

			int currentX = current[0];
			int currentY = current[1];
			int index = current[2];

			if (currentX == finishX && currentY == finishY) {
				// TO-DO
				// Implement reconstructing the path
				// listOfNodesToFollow = reconstructPath(current);

				// listOfNodesToFollow[listOfNodesPointer][0] = finishX;
				// listOfNodesToFollow[listOfNodesPointer][1] = finishY;
				// listOfNodesPointer++;

				System.out.println("We have reached the finish! YAAAAY");
				return cameFrom;
			}

			openSet = remove(openSet, index);
			openSetTail--;
			closedSet[closedSetTail][0] = currentX;
			closedSet[closedSetTail][1] = currentY;
			closedSetTail++;

			// for each neighbour of the current node check if it is already in
			// closed set, if not continue to setting its gScore by getting
			// gScore of the current node and adding distance between current
			// node and neighbour (which in this case is basically 1)
			// if openSet does not contain neigbourNode and tentative score is
			// smaller than gScore of this neighbourNode

			int[][] currentNeighbour = getNeighbours(currentX, currentY);

			for (int i = 0; i < 4; i++) {
				int tentative = 0;
				int currentNeighbourX = currentNeighbour[i][0];
				int currentNeighbourY = currentNeighbour[i][1];
				if (currentNeighbourX == -1 || currentNeighbourY == -1)
					continue;
				if (contains(closedSet, closedSetHead, closedSetTail,
						currentNeighbourX, currentNeighbourY))
					continue;

				// Could have used method calculateShortestDsitanceTo, but it's
				// always equal to 1 in this case!
				tentative = gScoresMap[currentX][currentY] + 1;

				if (!contains(openSet, openSetHead, openSetTail,
						currentNeighbourX, currentNeighbourY)
						|| tentative < gScoresMap[currentNeighbourX][currentNeighbourY]) {
					// listOfNodesToFollow[listOfNodesPointer][0] = currentX;
					// listOfNodesToFollow[listOfNodesPointer][1] = currentY;
					// listOfNodesPointer++;

					cameFrom[cameFromPointer][0] = currentNeighbourX;
					cameFrom[cameFromPointer][1] = currentNeighbourY;
					cameFrom[cameFromPointer][2] = currentX;
					cameFrom[cameFromPointer][3] = currentY;
					cameFromPointer++;

					gScoresMap[currentNeighbourX][currentNeighbourY] = tentative;
					fScoresMap[currentNeighbourX][currentNeighbourY] = tentative
							+ calculateShortestDistanceTo(currentNeighbourX,
									currentNeighbourY, finishX, finishY);

					if (!contains(openSet, openSetHead, openSetTail,
							currentNeighbourX, currentNeighbourY)) {
						openSet[openSetTail][0] = currentNeighbourX;
						openSet[openSetTail][1] = currentNeighbourY;
						openSetTail++;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Method for getting the shortest distance from given position to the given
	 * position.
	 * 
	 * @param startX
	 *            Starting X
	 * @param startY
	 *            Starting Y
	 * @param finishX
	 *            Finishing X
	 * @param finishY
	 *            Finishing Y
	 * @return Returns integer which is the distance to travel. Note: it is NOT
	 *         the diagonal distance to the goal
	 */
	public static int calculateShortestDistanceTo(int startX, int startY,
			int finishX, int finishY) {
		return Math.abs(startX - finishX) + Math.abs(startY - finishY);
	}

	public static int[] getMinHeuristic(int[][] list, int[][] fScoreMap,
			int head, int tail) {
		int[] arrayToReturn = new int[3];
		int currX = list[head][0];
		int currY = list[head][1];
		int candidate = 0;
		int currentMin = fScoreMap[currX][currY];

		arrayToReturn[0] = currX;
		arrayToReturn[1] = currY;
		arrayToReturn[2] = head;

		for (int i = head + 1; i < tail; i++) {
			currX = list[i][0];
			currY = list[i][1];
			candidate = fScoreMap[currX][currY];

			if (currentMin > candidate) {
				currentMin = candidate;
				arrayToReturn[0] = currX;
				arrayToReturn[1] = currY;
				arrayToReturn[2] = i;
			}
		}
		return arrayToReturn;
	}

	/**
	 * Method for removing given row from the array. Removes the row and moves
	 * all the rows after removed one.
	 * 
	 * @param openSet
	 *            2D array from which the row is to be removed.
	 * @param indexToRemove
	 *            Row index to be removed.
	 * @return
	 */
	public static int[][] remove(int[][] openSet, int indexToRemove) {
		int[][] matrixToReturn = openSet;
		for (int i = indexToRemove; i < ARRAY_LENGTH - 1; i++) {
			matrixToReturn[i][0] = openSet[i + 1][0];
			matrixToReturn[i][1] = openSet[i + 1][1];
		}

		matrixToReturn[ARRAY_LENGTH - 1][0] = -1;
		matrixToReturn[ARRAY_LENGTH - 1][1] = -1;
		return matrixToReturn;
	}

	/**
	 * Method for getting neighbours of current nodes.
	 * 
	 * @param x
	 *            X coordinate of current node
	 * @param y
	 *            Y coordinate of current node
	 * @return Array[4][2] of positions of neighbour nodes. UP is 0th element,
	 *         RIGHT is 1st element, DOWN is 2nd element, LEFT is 3rd element.
	 *         If current node doesn't have one or more of neighbours their
	 *         coordinates are set to -1.
	 */
	public static int[][] getNeighbours(int x, int y) {
		int[][] arrayToReturn = new int[4][2];
		// Setting UP
		if (y > 0) {
			arrayToReturn[0][0] = x;
			arrayToReturn[0][1] = y - 1;
		} else {
			arrayToReturn[0][0] = -1;
			arrayToReturn[0][1] = -1;
		}
		// Setting RIGHT
		if (x < COLUMNS - 1) {
			arrayToReturn[1][0] = x + 1;
			arrayToReturn[1][1] = y;
		} else {
			arrayToReturn[1][0] = -1;
			arrayToReturn[1][1] = -1;
		}
		// Setting DOWN
		if (y < ROWS - 1) {
			arrayToReturn[2][0] = x;
			arrayToReturn[2][1] = y + 1;
		} else {
			arrayToReturn[2][0] = -1;
			arrayToReturn[2][1] = -1;
		}
		// Setting LEFT
		if (x > 0) {
			arrayToReturn[3][0] = x - 1;
			arrayToReturn[3][1] = y;
		} else {
			arrayToReturn[3][0] = -1;
			arrayToReturn[3][1] = -1;
		}
		return arrayToReturn;
	}

	public static boolean contains(int[][] closedSet, int head, int tail,
			int x, int y) {
		for (int i = head; i < tail; i++) {
			if (closedSet[i][0] == x && closedSet[i][1] == y)
				return true;
		}
		return false;
	}

	public static int[][] findPath(int[][] cameFrom, int tail) {

		int[][] outp = new int[ARRAY_LENGTH][2];

		for (int i = 0; i < outp.length; i++) {

			outp[i][0] = -1;
			outp[i][1] = -1;

		}

		int outpPointer = 0;

		int nextX;
		int nextY;
		int elementIndex = 10; // whatever

		tail--;

		nextX = cameFrom[tail][2];
		nextY = cameFrom[tail][3];
		outp[outpPointer][0] = cameFrom[tail][0];
		outp[outpPointer][1] = cameFrom[tail][1];
		outpPointer++;

		while (elementIndex != 0) {

			outp[outpPointer][0] = nextX;
			outp[outpPointer][1] = nextY;
			outpPointer++;

			for (int i = 0; i < tail; i++) {


				if (cameFrom[i][0] == nextX && cameFrom[i][1] == nextY) {

					nextX = cameFrom[i][2];
					nextY = cameFrom[i][3];
					elementIndex = i;
					break;

				}

			}

		}

		outp[outpPointer][0] = nextX;
		outp[outpPointer][1] = nextY;
		outpPointer++;
		return outp;
	}
}
