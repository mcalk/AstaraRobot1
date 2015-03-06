import java.util.Collections;
import java.util.LinkedList;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RobotPathPlanning extends Application
{
	private int n = 9;
	private int m = 9;

	BorderPane borderPane = new BorderPane();
	GridPane root = new GridPane();

	Square[][] playfield = new Square[n][m];

	private int[][] map = { { 3, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 3, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 0, 0 }, { 3, 0, 0, 1, 0, 0, 3, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 3, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 3, 1, 1, 1, 1, 1, 1, 1, 2 } };

	private LinkedList<Square> listOfNodesToFollow;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Robot Path Planning");

		root.setAlignment(Pos.CENTER);

		Boolean isAvailable;
		Boolean isStart;
		Boolean isFinish;

		for (int i = 0; i < map.length; i++)
		{
			for (int j = 0; j < map[0].length; j++)
			{
				isFinish = false;
				isStart = false;

				if ( map[i][j] == 1 )
					isAvailable = true;
				else
					isAvailable = false;

				if ( map[i][j] == 2 )
					isStart = true;
				if ( map[i][j] == 3 )
					isFinish = true;

				Square square = new Square(i, j, isAvailable, isStart, isFinish);

				square.setOnMouseClicked(new EventHandler<MouseEvent>()
				{

					@Override
					public void handle(MouseEvent arg0)
					{
						// TODO Auto-generated method stub
						System.out.println("Neighbours of clicked square: ");
						for (Square sqr : square.getNeighbours())
						{
							System.out.println("( " + sqr.getxPos() + ", "
									+ sqr.getyPos() + ")");
						}
						// System.out.println("Clicked (" + square.getxPos()
						// + ", " + square.getyPos() + ")");
					}

				});

				playfield[i][j] = square;

				root.add(square, i, j);
			}

		}

		borderPane.setCenter(root);
		Button btn = new Button("Click to show the path");
		btn.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				animateWay();
			}
		});

		borderPane.setBottom(btn);

		Scene scene = new Scene(borderPane, 650, 650, Color.DARKVIOLET);
		System.out.println(calculateShortestDistanceTo(playfield[8][8],
				playfield[0][1]));

		this.genereteNeibours();

		this.calculateAStar(playfield[2][3], playfield[0][0]);

		for (Square sqr : listOfNodesToFollow)
		{
			System.out.println(sqr.toString());
		}

		stage.setScene(scene);
		stage.show();
	}

	public void calculateAStar(Square start, Square finish)
	{
		LinkedList<Square> openSet = new LinkedList<Square>();
		LinkedList<Square> closedSet = new LinkedList<Square>();

		// Adding nodes not accessible into closedSet!
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if ( map[i][j] == 0 )
					closedSet.add(playfield[i][j]);
			}
		}

		openSet.add(start);

		start.setgScore(0);
		start.setfScore(start.getgScore()
				+ calculateShortestDistanceTo(start, finish)); // Check

		while (!openSet.isEmpty())
		{
			Square current = getMinHeuristic(openSet);
			if ( current.equals(finish) )
				listOfNodesToFollow = reconstructPath(current.getCameFrom(),
						finish);

			openSet.remove(current);
			closedSet.add(current);

			for (Square neighbourNode : current.getNeighbours())
			{
				int tentative = 0;

				if ( closedSet.contains(neighbourNode) )
				{
					continue;
				}
				tentative = current.getgScore()
						+ calculateShortestDistanceTo(current, neighbourNode);

				if ( !openSet.contains(neighbourNode)
						|| tentative < neighbourNode.getgScore() )
				{
					neighbourNode.setCameFrom(current);
					neighbourNode.setgScore(tentative);
					neighbourNode
							.setfScore(neighbourNode.getgScore()
									+ calculateShortestDistanceTo(
											neighbourNode, finish));
					if ( !openSet.contains(neighbourNode) )
						openSet.add(neighbourNode);
				}
			}
		}

	}

	private LinkedList<Square> reconstructPath(Square cameFrom, Square finish)
	{
		LinkedList<Square> listToReturn = new LinkedList<Square>();

		while (!cameFrom.equals(finish))
		{
			listToReturn.add(cameFrom);
			cameFrom = cameFrom.getCameFrom();
			if ( cameFrom == null )
				break;
		}

		return listToReturn;
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
	public int calculateShortestDistanceTo(Square fromNode, Square toNode)
	{
		return Math.abs(fromNode.getxPos() - toNode.getxPos())
				+ Math.abs(fromNode.getyPos() - toNode.getyPos());
	}

	public void genereteNeibours()
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				Square square = playfield[i][j];

				Square up = new Square(true);
				Square down = new Square(true);
				Square right = new Square(true);
				Square left = new Square(true);

				// System.out.println("Current square position is ("
				// + square.getxPos() + ", " + square.getyPos() + ")");

				if ( j != 0 )
				{
					up = playfield[i][j - 1];
					// System.out.println("Up is ( " + up.getxPos() + ", "
					// + up.getyPos() + ")");
				}
				if ( i != n - 1 )
				{
					right = playfield[i + 1][j];
					// System.out.println("Right is ( " + right.getxPos() + ", "
					// + right.getyPos() + ")");
				}
				if ( i != 0 )
				{
					left = playfield[i - 1][j];
					// System.out.println("Left is ( " + left.getxPos() + ", "
					// + left.getyPos() + ")");
				}
				if ( j != m - 1 )
				{
					down = playfield[i][j + 1];
					// System.out.println("Down is ( " + down.getxPos() + ", "
					// + down.getyPos() + ")");
				}
				square.setSquaresAround(up, right, down, left);

			}
		}
	}

	public static Square getMinHeuristic(LinkedList<Square> list)
	{
		Square min = Collections.min(list);

		return min;
	}

	public void animateWay()
	{
		PathTransition pathTransition = new PathTransition();
		Path path = new Path();
		
		path.getElements().add(new MoveTo(25, 25));
		
		for (Square sqr : listOfNodesToFollow)
		{
			path.getElements().add(
					new LineTo(sqr.getxPos() * 50, sqr.getyPos() * 50));
		}
		path.setOpacity(0.5);

		Circle circle = new Circle(20, 20, 15);
		circle.setFill(Color.DARKBLUE);

		root.getChildren().add(path);
		root.getChildren().add(circle);

		pathTransition.setDuration(Duration.seconds(8.0));
		pathTransition.setDelay(Duration.seconds(.5));
		pathTransition.setPath(path);
		pathTransition.setNode(circle);
		// pathTransition
		// .setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(true);

		pathTransition.play();

	}
}
