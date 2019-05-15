package question1;

import java.util.List;


/**
 * @author Linh Pham
 *
 */
public class Plans {
  
  private List<Plan> plans;
  
  public List<Plan> getPlans() {
    return plans;
  }

  public void setPlans(List<Plan> plans) {
    this.plans = plans;
  }
  
  public static class Plan {

    private String name;
    private double cost;
    private List<String> features;
    
    private int replicas;
    
    public Plan() {
      super();
    }
    
    public Plan(String name, double cost, int replicas) {
      this.name = name;
      this.cost = cost;
      this.replicas = replicas;
    }
    
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public double getCost() {
      return cost;
    }

    public void setCost(double cost) {
      this.cost = cost;
    }

    public List<String> getFeatures() {
      return features;
    }

    public void setFeatures(List<String> features) {
      this.features = features;
    }

    public int getReplicas() {
      return replicas;
    }

    public void setReplicas(int replicas) {
      this.replicas = replicas;
    }

    public int increaseReplicas() {
      return ++ replicas;
    }
    
    public double costReplicas() {
      return cost / replicas;
    }
    
    public Plan clone(int replicas) {
      return new Plan(this.name, this.cost, replicas);
    }
    
  }
}

