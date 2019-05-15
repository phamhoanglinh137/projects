package question2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author Linh Pham
 *
 */
public class Main {
  
  static Node rootNode;
  static List<NavigableMap<Integer, Node>> nodeLs;
  
  public static void main(String[] args) {
    rootNode = initiateNode();
    nodeLs = new ArrayList<NavigableMap<Integer, Node>>();
    
    process(rootNode, 0);
    
    System.out.println(rootNode);
  }
  
  public static void process(Node node, int level) {
    if(nodeLs.size() == level) nodeLs.add(new TreeMap<Integer, Node>()); 
    
    nodeLs.get(level).put(findRightNode(node, level), node);
    
    if(node.getNodes() != null && node.getNodes().size() > 0) {
      node.getNodes().forEach(n -> {
        process(n, level + 1);
      });
    }
  }
  
  public static int findRightNode(Node node, int level) {
    NavigableMap<Integer, Node> nodeInLevel = nodeLs.get(level);
    if(nodeInLevel.lastEntry() != null) {
      nodeInLevel.lastEntry().getValue().setRight(node);
      return nodeInLevel.lastEntry().getKey() + 1;
    }
    return 0;
  }
  
  public static Node initiateNode() {
    Node lv1Node1 = new Node(null, "level1_node1", Arrays.asList(new Node("level2_node1"), new Node("level2_node2")));
    Node lv1Node2 = new Node("level1_node2");
    Node lv1Node3 = new Node(null, "level1_node3", Arrays.asList(new Node("level2_node3")));
    return new Node(null, "root", Arrays.asList(lv1Node1, lv1Node2, lv1Node3));
  }
  
}
