import java.util.HashMap;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RobotPathPlanning extends Application
{
    private int n = 20;
    private int m = 10;

    Square[][] playfield =  new Square[n][m];

    private int[][] map = { { 0, 3, 1, 1, 1, 3, 1, 1, 1, 3 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 3, 1, 1, 1, 3, 1, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 3, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 } };

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Robot Path Planning");

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        // Testing a new GitHub comments
        Boolean isAvailable;
        Boolean isStart;
        Boolean isFinish;

        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                isFinish = false;
                isStart = false;

                if ( map[j][i] == 1 )
                    isAvailable = true;
                else
                    isAvailable = false;

                if ( map[j][i] == 2 )
                    isStart = true;
                if ( map[j][i] == 3 )
                    isFinish = true;

                Square square = new Square(j, i, isAvailable, isStart, isFinish);
                if ( i == 1 && j == 0 )
                    square.setIsFinish(true);
                // if(isAvailable)
                // square.addDot();

                playfield[j][i] = square;

                root.add(square, i, j);
            }
        }
        
        Scene scene = new Scene(root, 550, 550);

        System.out.println(calculateDistanceToGoal(playfield[9][9],playfield[0][1] ));
        
        stage.setScene(scene);
        stage.show();
    }

    public void calculateAStar(Square start, Square finish)
    {

        LinkedList<Square> openSet = new LinkedList<Square>();
        openSet.add(start);


    }

    public int calculateDistanceToGoal(Square currentNode, Square finalNode)
    {
        return Math.abs(currentNode.getxPos() - finalNode.getxPos())
                + Math.abs(currentNode.getyPos() - finalNode.getyPos());
    }
}