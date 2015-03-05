import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RobotPathPlanning extends Application {
	private int n = 9;
	private int m = 9;

	Square[][] playfield = new Square[n][m];

	private int[][] map = { { 3, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 3, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 0, 0 }, { 3, 0, 0, 1, 0, 0, 3, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 3, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 3, 1, 1, 1, 1, 1, 1, 1, 2 } };

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Robot Path Planning");

		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);

		Boolean isAvailable;
		Boolean isStart;
		Boolean isFinish;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				isFinish = false;
				isStart = false;

				if (map[i][j] == 1)
					isAvailable = true;
				else
					isAvailable = false;

				if (map[i][j] == 2)
					isStart = true;
				if (map[i][j] == 3)
					isFinish = true;

				Square square = new Square(i, j, isAvailable, isStart, isFinish);

				square.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Clicked (" + square.getxPos()
								+ ", " + square.getyPos() + ")");
					}

				});

				playfield[i][j] = square;

				root.add(square, i, j);
			}
		}

		Scene scene = new Scene(root, 650, 650, Color.DARKVIOLET);
		System.out.println(calculateShortestDistanceTo(playfield[8][8],
				playfield[0][1]));

		stage.setScene(scene);
		stage.show();
	}

	public LinkedList<Square> calculateAStar(Square start, Square finish) {
		LinkedList<Square> openSet = new LinkedList<Square>();
		LinkedList<Square> closedSet = new LinkedList<Square>();

		openSet.add(start);

		//Square cameFrom;

		start.setgScore(0);
		start.setfScore(start.getgScore()
				+ calculateShortestDistanceTo(start, finish)); // Check

		while (!openSet.isEmpty()) {
			Square current = getMinHeuristic(openSet);       //check make sure to update the square stored in playfield since current is a copy
			if (current.equals(finish)) {

				return reconstructPath(current.getCameFrom(), finish);

			}

			openSet.remove(current);
			closedSet.add(current);

			LinkedList<Square> currentNeigbors = new LinkedList<Square>();
			int x = current.getxPos();
			int y = current.getyPos();

			currentNeigbors.add(playfield[y + 1][x]);
			currentNeigbors.add(playfield[y - 1][x]);
			currentNeigbors.add(playfield[y][x + 1]); // check values of indexes
			currentNeigbors.add(playfield[y][x - 1]);

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
					neibourNode.setgScore(tentative);                        //check make sure to update the square stored in playfield since current is a copy
					neibourNode.setfScore(neibourNode.getgScore()
							+ calculateShortestDistanceTo(neibourNode, finish));
					if (!openSet.contains(neibourNode)) {
						openSet.add(neibourNode);
					}
				}

			}
		}
		
		return null;

	}

	private LinkedList<Square> reconstructPath(Square cameFrom, Square current) {
		// TODO Auto-generated method stub

		LinkedList<Square> path = new LinkedList<Square>();
		path.add(current);
		while (current.getCameFrom() != null) { // check (current in cameFrom)
			current = current.getCameFrom();
			path.add(current);
		}

		return path;
	}

	/**
	 * Method which calculates the shortest path to the distance, not taking
	 * into account the obstacles it can encounter on the way
	 * 
	 * @param fromNode
	 *            Node from which you want to calculate the distance.
	 * @param toNode
	 *            Node to which you want to calculate the distance.
	 * @return Distance value as an integer.
	 */
	public int calculateShortestDistanceTo(Square fromNode, Square toNode) {
		return Math.abs(fromNode.getxPos() - toNode.getxPos())
				+ Math.abs(fromNode.getyPos() - toNode.getyPos());
	}

	public static Square getMinHeuristic(LinkedList<Square> list) {
		Square min = Collections.min(list);

		return min;
	}
}
