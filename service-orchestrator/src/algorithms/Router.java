package algorithms;

import models.Node;
import models.Request;

public interface Router { // An interface that defines how a service distributes requests to its nodes (load balancing strategy).

    void addNode(Node node);
    void removeNode(Node node);
    Node getAssignedNode(Request request);
}
