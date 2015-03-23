import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RobotPathPlanning extends Application
{
	private static final int START_X = 8;
	private static final int START_Y = 8;
	private static final int FINISH_X = 0;
	private static final int FINISH_Y = 0;
	
	private final int COLUMNS = 18;
	private final int ROWS = 9;

	private Square startPoint;

	BorderPane borderPane = new BorderPane();
	GridPane root = new GridPane();

	Square[][] playfield = new Square[COLUMNS][ROWS];

	private int[][] map = { { 3, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 0, 0 }, { 3, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 3, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 3, 1, 1, 1, 1, 1, 1, 1, 2 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 3, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 3, 0, 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 3, 0, 0, 0, 0, 0, 0, 0, 0 } };

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

				if ( map[i][j] == 1 || map[i][j] == 3 )
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

		BorderPane.setAlignment(btn, Pos.CENTER);
		BorderPane.setMargin(btn, new Insets(0, 0, 30, 0));
		borderPane.setBottom(btn);

		Scene scene = new Scene(borderPane, 950, 650, Color.ALICEBLUE);

		this.genereteNeibours();

		this.calculateAStar(playfield[START_X][START_Y], playfield[FINISH_X][FINISH_Y]);

		PathCreator pathToFollow = new PathCreator(listOfNodesToFollow);
		pathToFollow.printInstructions();

		stage.setScene(scene);
		stage.show();
	}

	public void calculateAStar(Square start, Square finish)
	{
		LinkedList<Square> openSet = new LinkedList<Square>();
		LinkedList<Square> closedSet = new LinkedList<Square>();

		// Adding nodes not accessible into closedSet!
		for (int i = 0; i < COLUMNS; i++)
		{
			for (int j = 0; j < ROWS; j++)
			{
				if ( map[i][j] == 0 )
					closedSet.add(playfield[i][j]);
			}
		}

		openSet.add(start);
		startPoint = start;

		start.setgScore(0);
		start.setfScore(start.getgScore()
				+ calculateShortestDistanceTo(start, finish)); // Check

		while (!openSet.isEmpty())
		{
			Square current = getMinHeuristic(openSet);
			if ( current.equals(finish) )
			{

				listOfNodesToFollow = reconstructPath(current);
			}
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

	private LinkedList<Square> reconstructPath(Square cameFrom)
	{
		LinkedList<Square> listToReturn = new LinkedList<Square>();
		Stack<Square> stack = new Stack<>();

		while (cameFrom != null)
		{
			stack.push(cameFrom);
			cameFrom = cameFrom.getCameFrom();
		}

		while (!stack.isEmpty())
		{
			listToReturn.add(stack.pop());
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
		for (int i = 0; i < COLUMNS; i++)
		{
			for (int j = 0; j < ROWS; j++)
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
				if ( i != COLUMNS - 1 )
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
				if ( j != ROWS - 1 )
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


		System.out.println(startPoint.getxPos() + ", " + startPoint.getyPos());

		path.getElements().add(
				new MoveTo(startPoint.getxPos() * 50 + 25,
						startPoint.getyPos() * 50 + 25));


		
		//path.getElements().add(new MoveTo(25, 25));
		Iterator bla = listOfNodesToFollow.descendingIterator();
		
		// bla = listOfNodesToFollow.descendingIterator();
		

		path.getElements().add(new MoveTo(startPoint.getxPos()*50+25,startPoint.getyPos()*50+25));
		

		for (Square sqr : listOfNodesToFollow)
		{
			path.getElements()
					.add(new LineTo(sqr.getxPos() * 50 + 25,
							sqr.getyPos() * 50 + 25));
		}

		StackPane stackPane = new StackPane();
		Rectangle rectangle = new Rectangle(25, 40);
		Rectangle rectangleTracks = new Rectangle(35, 10);
		Circle circle = new Circle(5);

		rectangleTracks.setFill(Color.BLACK);
		rectangle.setFill(Color.BLACK);
		circle.setFill(Color.WHITE);

		stackPane.getChildren().add(rectangleTracks);
		stackPane.setAlignment(rectangleTracks, Pos.BOTTOM_CENTER);
		stackPane.getChildren().add(rectangle);
		stackPane.getChildren().add(circle);

		// root.getChildren().add(path);
		root.getChildren().add(stackPane);

		pathTransition.setDuration(Duration.seconds(4.0));
		pathTransition.setDelay(Duration.seconds(.5));
		pathTransition.setPath(path);
		pathTransition.setNode(stackPane);
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(true);

		pathTransition.play();

	}
}