package org.gruppe2.ui.javafx.menu;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.PokerApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Åsmund on 28/04/2016.
 */
public class Credits extends StackPane{
    private static ArrayList<Node> nodes;
    private static SequentialTransition sequence;

    public Credits(){
        nodes  = new ArrayList<Node>();

        nodes.add(new Label("Åsmund"));
        nodes.add(new Label("Petter"));
        nodes.add(new Label("Zohar"));
        nodes.add(new Label("Mikal"));
        nodes.add(new Label("Cem"));
        nodes.add(new Label("Andreas"));
        nodes.add(new Label("Svein"));
        nodes.add(new Label("Daniel"));
        nodes.add(new Label("Runar"));
        nodes.add(new Label("Håkon K"));
        nodes.add(new Label("Kjetil"));
        nodes.add(new Label("Håkon T"));

        long seed = System.nanoTime();
        Collections.shuffle(nodes, new Random(seed));

        sequence = new SequentialTransition();
        UIResources.loadFXML(this);
        playCredits();
    }

    private void playCredits() {

        SequentialTransition sequence = new SequentialTransition();
        sequence.setCycleCount(Animation.INDEFINITE);

        addObjectsToCreditSequence(sequence, nodes);
        sequence.play();

    }

    private void addObjectsToCreditSequence(SequentialTransition sequence, ArrayList<Node> nodes){
        for(Node node: nodes){

            addObject(sequence, node);
        }
    }

    private void addObject(SequentialTransition sequence, Node node){

        Label label = (Label) node;
        int labelLengthModifier = label.getText().length()*5;


        Path path = new Path(new MoveTo(-2000, 0), new LineTo(labelLengthModifier, 0));
        Path path1 = new Path(new MoveTo(labelLengthModifier, 0), new LineTo(2000, 0));

        PathTransition transition = createPathTransition(path, node);
        PathTransition transition1 = createPathTransition(path1, node);

        sequence.getChildren().add(transition);
        sequence.getChildren().add(new PauseTransition(Duration.millis(2000)));
        sequence.getChildren().add(transition1);

        getChildren().add(node);


    }

    private PathTransition createPathTransition(Path path, Node node){
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2000));
        pathTransition.setCycleCount(1);

        pathTransition.setPath(path);

        pathTransition.setNode(node);

        return pathTransition;
    }

    public static void stop(){
        sequence.stop();
        nodes.clear();
    }
}
