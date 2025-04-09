package models;

public class Node { // A server where requests can be sent.

    private final String id;
    private final int weight;
    private final String ipAddress;

    public Node(String id , String ipAddress){
        this(id,1,ipAddress);
    }
    public Node(String id,int weight, String ipAddress){
        this.id=id;
        this.weight=weight;
        this.ipAddress=ipAddress;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return this.id.equals(node.id);
    }
    /*
    Whenever you override the equals() method, it is important to also override hashCode() to ensure consistency between the two methods. If two objects are considered equal, they must have the same hash code.
     */

    @Override
    public int hashCode(){
        return id.hashCode();
    }

}
