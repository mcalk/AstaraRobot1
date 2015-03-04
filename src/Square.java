import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

public class Square extends StackPane
{
    private Boolean isAccessible, isStart, hasBall, isFinish = false;
    private int heuristic;
    private int xPos, yPos;

    public Square(int x, int y, Boolean isAccessible, Boolean isStart, Boolean hasBall)
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

}