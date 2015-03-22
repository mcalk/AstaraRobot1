package c_version;

public class NXC
{
	public static void main(String[] args)
	{
		
	}
}

/*
LinkedList<Square> currentNeigbors = new LinkedList<Square>();
int x = current.getxPos();
int y = current.getyPos();

if (y + 1 < m) {

	currentNeigbors.add(playfield[y + 1][x]);

}

if (y - 1 >= 0) {

	currentNeigbors.add(playfield[y - 1][x]); // check values of
												// indexes

}
if (x + 1 < n) {

	currentNeigbors.add(playfield[y][x + 1]);

}
if (x - 1 >= 0) {

	currentNeigbors.add(playfield[y][x - 1]);

}

for (int i = 0; i < currentNeigbors.size(); i++) {

	Square neibourNode = currentNeigbors.get(i);

	if (closedSet.contains(neibourNode)) {
		continue;
	}
	int tentative = current.getgScore()
			+ calculateShortestDistanceTo(current, neibourNode);

	if (!openSet.contains(neibourNode)
			|| tentative < neibourNode.getgScore()) {
		neibourNode.setCameFrom(current);
		neibourNode.setgScore(tentative); // check make sure to
											// update the square
											// stored in playfield
											// since current is a
											// copy
		neibourNode.setfScore(neibourNode.getgScore()
				+ calculateShortestDistanceTo(neibourNode, finish));
		if (!openSet.contains(neibourNode)) {
			openSet.add(neibourNode);
		}
	}

}
*/