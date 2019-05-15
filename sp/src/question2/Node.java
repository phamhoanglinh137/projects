package question2;

import java.util.List;

/**
 * @author Linh Pham
 *
 */
public class Node {

  public Node right;

  public String name;

  public List<Node> nodes;

  public Node() {
    super();
  }

  public Node(Node right, String name, List<Node> nodes) {
    this.right = right;
    this.name = name;
    this.nodes = nodes;
  }

  public Node(String name) {
    this.name = name;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    return " Node  '" + name + "'" + (right != null? " has right node '" + right.getName() + "'." : " doesn't have right node. ")  + "\n" + (nodes != null ? nodes : "");
  }
}
