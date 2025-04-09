package algorithms;

import models.Node;
import models.Request;

import java.util.ArrayList;
import java.util.List;

public class WeightedRoundRobin implements Router {
    /*
    ✔ It rotates between servers,
    ✔ gives preference to servers with higher weight,
    ✔ ensures thread safety (important when lots of users are sending requests).
     */
    private final List<Node> nodes;
    private int assignTo;
    private int currentAssignments;
    private final Object lock;

    public WeightedRoundRobin(){
        this.nodes = new ArrayList<>();
        this.assignTo=0;
        this.lock = new Object();
    }

    @Override
    public void addNode(Node node) {
        // Lock the object to prevent race conditions (two threads adding nodes at the same time).
        synchronized (this.lock){
            nodes.add(node);
        }
    }

    @Override
    public void removeNode(Node node) {
        synchronized (this.lock){
            nodes.remove(node);
            assignTo--;
            currentAssignments=0;
        }

    }

    @Override
    public Node getAssignedNode(Request request) {
        synchronized (this.lock){
            assignTo = (assignTo+nodes.size())%nodes.size();
            final var currentNode = nodes.get(assignTo);
            currentAssignments++;
            if(currentAssignments == currentNode.getWeight()){
                assignTo++;
                currentAssignments=0;
            }
            return currentNode;
        }
    }
}
