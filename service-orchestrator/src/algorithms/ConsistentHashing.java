package algorithms;

import models.Node;
import models.Request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class ConsistentHashing implements Router{
    /*
    Why ConcurrentHashMap, ConcurrentSkipListMap, CopyOnWriteArrayList?
    Because:
    Multiple threads might add/remove nodes, or route requests at the same time.
    These are thread-safe collections that prevent weird errors like ConcurrentModificationException.
     */
    /*
    Use concurrent collections when you have multiple threads reading and writing at the same time.
    But if you are doing multiple-step operations, be extra careful — or use atomic methods if available.
     */
    /*
    Collection	Problem
    HashMap	Not thread-safe. If two threads put() at the same time, it can corrupt the structure (like infinite loops during resize!).
    ArrayList	Not thread-safe. If two threads add() at the same time, you can get ArrayIndexOutOfBoundsException or lost elements.
    You could wrap them using Collections.synchronizedXXX like:
    Map<K,V> map = Collections.synchronizedMap(new HashMap<>());
    List<T> list = Collections.synchronizedList(new ArrayList<>());
    Problem:
    Now it's thread-safe.
    But performance drops badly because only one thread can access the entire map or list at a time, even if they are touching completely different keys or indexes.


     */
    private final Map<Node , List<Long>> nodePositions;
    private final ConcurrentSkipListMap<Long , Node> nodeMappings;
    private final Function<String , Long> hashFunctions;
    private final int pointMultiplier;

    public ConsistentHashing(Function<String , Long> hashFunctions , int pointMultiplier){
        if(pointMultiplier==0){
            throw new IllegalArgumentException();
        }
        this.pointMultiplier = pointMultiplier;
        this.hashFunctions = hashFunctions;
        this.nodePositions = new ConcurrentHashMap<>();
        this.nodeMappings = new ConcurrentSkipListMap<>();
    }


    @Override
    public void addNode(Node node) {
        nodePositions.put(node , new CopyOnWriteArrayList<>());
        for(int i=0 ; i<pointMultiplier ; i++){
            for (int j=0 ; j< node.getWeight() ; j++){
                final var point = hashFunctions.apply((i*pointMultiplier + j)+node.getId());
                nodePositions.get(node).add(point);
                nodeMappings.put(point , node);
            }
        }

    }

    @Override
    public void removeNode(Node node) {
        for (Long point : nodePositions.remove(node)){
            nodeMappings.remove(point);
        }

    }

    @Override
    public Node getAssignedNode(Request request) {
        final var key = hashFunctions.apply(request.getId());
        final var entry = nodeMappings.higherEntry(key);
        if(entry==null){
            return nodeMappings.firstEntry().getValue();
        }else{
            return entry.getValue();
        }
    }
}
