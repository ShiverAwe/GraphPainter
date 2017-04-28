import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 * Created by Владимир on 23.04.2017.
 */
public class Arrow extends Group {
    Line line = new Line();
    Polygon triangle = new Polygon();
    {
        this.getChildren().addAll(line, triangle);
        triangle.setFill(Color.BLACK);
    }

    double startX;
    double startY;
    double endX;
    double endY;

    Color color = Color.BLACK;


    public void setPosition(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        updatePosition();
    }

    public Arrow(double startX, double startY, double endX, double endY) {
        this.setPosition(startX, startY, endX, endY);
    }

    public void updatePosition(){

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);

        triangle.getPoints().clear();
        // Calculating arrow points
        double diffX = endX - startX;
        double diffY = endY - startY;
        boolean reverse = diffY<0 ? false : true; // if arrow looks up - reverse it
        double div = diffX / diffY;
        double alpha = Math.atan(div) + (reverse?Math.PI:0); // +pi to turn arrow on 180
        double alphaPlus =  alpha + (1.0/10);
        double alphaMinus = alpha - (1.0/10);

        double X1 = Math.sin(alphaPlus) * 20 + endX;
        double Y1 = Math.cos(alphaPlus) * 20 + endY;
        double X2 = Math.sin(alphaMinus) * 20 + endX;
        double Y2 = Math.cos(alphaMinus) * 20 + endY;
        // Setting arrow points
        triangle.getPoints().addAll(
                endX, endY,
                X1, Y1,
                X2, Y2
        );
    }

    public void setColor(Color color) {
        this.color = color;
        line.setStroke(color);
        triangle.setStroke(color);
        triangle.setFill(color);
    }

    public Color getColor() {
        return color;
    }

    public void setWidth(int width) {
        this.line.setStrokeWidth(width);
    }
}
