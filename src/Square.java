import java.util.LinkedList;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

public class Square extends StackPane implements Comparable<Square>
{
	private Boolean isAccessible, isStart, hasBall, isFinish = false;

	private Square up = null, down = null, left = null, right = null, cameFrom;

	public Square getUp()
	{
		return up;
	}

	public void setUp(Square up)
	{
		this.up = up;
	}

	public Square getDown()
	{
		return down;
	}

	public void setDown(Square down)
	{
		this.down = down;
	}

	public Square getLeft()
	{
		return left;
	}

	public void setLeft(Square left)
	{
		this.left = left;
	}

	public Square getRight()
	{
		return right;
	}

	public void setRight(Square right)
	{
		this.right = right;
	}

	private LinkedList<Square> neibourghsList = new LinkedList<Square>();

	private int gScore, fScore;

	public int getgScore()
	{
		return gScore;
	}

	public void setgScore(int gScore)
	{
		this.gScore = gScore;
	}

	public int getfScore()
	{
		return fScore;
	}

	public void setfScore(int hScore)
	{
		this.fScore = hScore;
	}

	private int xPos, yPos;

	public Square(int x, int y, Boolean isAccessible, Boolean isStart,
			Boolean hasBall)
	{
		this.xPos = x;
		this.yPos = y;
		this.isAccessible = isAccessible;
		this.isStart = isStart;
		this.hasBall = hasBall;

		// create rectangle
		Rectangle rectangle = new Rectangle(50, 50);
		rectangle.setStroke(Color.BLACK);
		if ( isAccessible )
			rectangle.setFill(Color.ANTIQUEWHITE);
		else
			rectangle.setFill(Color.DARKGREY);

		if ( isStart )
			rectangle.setFill(Color.GREEN);

		getChildren().add(rectangle);

		if ( hasBall )
		{
			rectangle.setFill(Color.ANTIQUEWHITE);
			Circle circ = new Circle(15);
			circ.setFill(Color.BLACK);
			getChildren().add(circ);
		}

	}

	public int getxPos()
	{
		return xPos;
	}

	public void setSquaresAround(Square up, Square right, Square down,
			Square left)
	{
		this.up = up;
		this.right = up;
		this.down = down;
		this.left = left;
	}

	public void setxPos(int xPos)
	{
		this.xPos = xPos;
	}

	public int getyPos()
	{
		return yPos;
	}

	public void setyPos(int yPos)
	{
		this.yPos = yPos;
	}

	public Boolean getIsFinish()
	{
		return isFinish;
	}

	public void setIsFinish(Boolean isFinish)
	{
		this.isFinish = isFinish;
		Rectangle rectangle = new Rectangle(50, 50);
		rectangle.setStroke(Color.BLACK);
		rectangle.setFill(Color.RED);

		getChildren().add(rectangle);
	}

	public void addDot()
	{
		Circle circle = new Circle(25, 25, 5);
		circle.setFill(Color.BURLYWOOD);

		getChildren().add(circle);
	}

	public Square getCameFrom()
	{
		return cameFrom;
	}

	public void setCameFrom(Square cameFrom)
	{
		this.cameFrom = cameFrom;
	}

	@Override
	public int compareTo(Square arg0)
	{
		// TODO Auto-generated method stub
		if ( fScore > arg0.getfScore() )
			return 1;
		else if ( fScore == arg0.getfScore() )
			return 0;
		else
			return -1;
	}

}