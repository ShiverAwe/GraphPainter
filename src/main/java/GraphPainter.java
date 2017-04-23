package main.java;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.TreeMap;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class GraphPainter extends Group{

    double centerX;
    double centerY;
    double radius;

    ArrayList< Circle > points = new ArrayList<>();

    double pointRadius = 15;
    Color pointColor = Color.BLACK;

    TreeMap< Integer, TreeMap< Integer, Arrow > > links = new TreeMap<>();

    Color linkColor = Color.RED;

    public void addNode(){
        Circle newNode = new Circle();
        newNode.setOnMouseClicked(event->{
             new GOThread(this, (Circle) event.getSource() ) .start();
        });
        points.add(newNode);
        this.getChildren().add(newNode);
        updatePosition();
    }

    public void addEdge(int i, int j) {
        if (hasEdge(i, j)) return;

        System.out.println("Adding edge "+ i + " " + j);
        Arrow link = new Arrow(
                getPointPositionX( i ),
                getPointPositionY( i ),
                getPointPositionX( j ),
                getPointPositionY( j )
        );

        link.setColor(Color.RED);

        if (!links.containsKey(i)) links.put(i, new TreeMap<>());

        links.get(i).put(j, link);
        this.getChildren().add(link);

    }

    public boolean alone (int i) {
        return !links.containsKey(i) || links.get(i).isEmpty();
    }
    public boolean hasEdge(int i, int j){
        return (!alone(i) && links.get(i).containsKey(j));
    }

    double getPointPositionX(double number){
        double angle = (2*Math.PI / points.size()) * number;
        double x = cos(angle);
        return (x * radius) + centerX;
    }

    double getPointPositionY(double number){
        double angle = (2*Math.PI / points.size()) * number;
        double y = sin(angle);
        return (y * radius) + centerY;
    }

    void updatePosition(){
        for (int i = 0; i < points.size(); i++) {

            points.get(i).setCenterX(getPointPositionX(i));
            points.get(i).setCenterY(getPointPositionY(i));
            points.get(i).setRadius(pointRadius);

            if (!alone(i)){
                for (int j = 0; j < points.size(); j++) {
                    if (hasEdge(i, j)){
                        Arrow link = links.get(i).get(j);
                        link.setPosition(
                            getPointPositionX( i ),
                            getPointPositionY( i ),
                            getPointPositionX( j ),
                            getPointPositionY( j )
                        );
                    }
                }
            }
        }
    }

    void setDefaultColors(){
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setRadius(pointRadius);
            points.get(i).setFill(pointColor);
            if (!alone(i)){
                for (int j = 0; j < points.size(); j++) {
                    if (hasEdge(i, j)){
                        Arrow link = links.get(i).get(j);
                        link.setColor(linkColor);
                    }
                }
            }
        }
    }

    public int size(){
        return points.size();
    }

    public void addRandomEdge(){
        int i; int j;
        do {
            i = (int) (Math.random() * points.size());
            j = (int) (Math.random() * points.size());
        } while (i == j || hasEdge(i, j));  // we don't want to add edges (i, i)

        addEdge(i, j);
    }

    public void GO(int number) {
        setDefaultColors();

        // To check if we have already been there
        VisitControl visitControl = new VisitControl(size());

        // In that color we paint path
        Color color = Color.BLUE; // new Color(Math.random(), Math.random(), Math.random(), 1);

        int prevNumber = number; // previous visited node;

        // while node has links to others
        while (!alone(number)) {
            points.get(number).setFill(color);
            visitControl.visit(number);

            // seeking next random available node.
            int nextNumber = -1; int noWhileTrue = 1000; boolean hasNext = true;
            while ((!hasEdge(number, nextNumber) || visitControl.isVisited(nextNumber) )) {
                nextNumber = (int) (size() * Math.random());
                if ( --noWhileTrue < 0 ) { hasNext = false ; break; }
            }
            if (!hasNext) {
                break;
            }

            visitControl.visit(nextNumber);
            points.get(nextNumber).setFill(color);
            System.out.println(number + " " + nextNumber + " " + hasEdge(number, nextNumber));
            links.get(number).get(nextNumber).setColor(color);

            if (hasEdge(prevNumber, number)) {
                links.get(prevNumber).get(number).setColor(color);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            prevNumber = number;
            number = nextNumber;
        }
        System.out.println("End of walking");

    }

    public GraphPainter(int centerX, int centerY, int radius){
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.setTranslateX(centerX - radius);
        this.setTranslateY(centerY - radius);
    }

    public GraphPainter(){
        this(200, 200, 150);
    }

}

class GOThread extends Thread{

    GraphPainter painter;
    Circle circle;


    public GOThread(GraphPainter painter, Circle circle) {
        this.painter = painter;
        this.circle = circle;
    }

    @Override
    public void run() {
        painter.GO(painter.points.indexOf(circle));
    }

}

class VisitControl {
    int [] values;
    int size;
    VisitControl (int size){
        this.size = size;
        values = new int [size];
        for (int i = 0; i < size; i++) {
            values[i] = 0;
        }
    }

    public boolean allVisited(){
        for (int i = 0; i < size; i++) {
            if (values[i] == 0) return false;
        }
        return true;
    }

    public void visit (int i) {
        if (i >= size || i < 0) return;
        values[i] = 1;
    }

    public boolean isVisited(int i){
        if (i >= size || i < 0) return true;
        return values[i]==1;
    }
}