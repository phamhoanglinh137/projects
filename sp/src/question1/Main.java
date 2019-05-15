/**
 * 
 */
package question1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import question1.Plans.Plan;

/**
 * @author Linh Pham
 *
 */
public class Main {

  public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
    
    //Part 1 : Prepare data from data.json
    // take selected feature from user input, default is F1 and F2.
    String[] selectedFeatures = new String[] {"F1", "F2"};
    if(args.length > 0) {
      selectedFeatures = args;
    }
    List<Map<String, Plan>> featureLs = new ArrayList<Map<String, Plan>>();
    Plans allPlans = prepareData();
    
    //Part 2 : each selected feature will have list of plan.
    Stream.of(selectedFeatures).forEach(f -> {
      Map<String, Plan> planMapBySelectedFeature = new HashMap<String, Plan>();
      allPlans.getPlans().stream()
          .filter(p -> new HashSet<String>(p.getFeatures()).contains(f))
          .forEach(p -> {
            AtomicInteger replicas = new AtomicInteger(1);
            featureLs.forEach(map -> {
               if(map.containsKey(p.getName())) {
                 replicas.set(map.get(p.getName()).increaseReplicas());
               }
            });
            planMapBySelectedFeature.put(p.getName(), p.clone(replicas.get()));
          });
      featureLs.add(planMapBySelectedFeature);
     });
    
    //Part 3 : sort to find out which one is cheapest for each features and rmv duplicated plan.
    Comparator<Plan> cp = (cp1, cp2) -> {
      if(cp1.getName().equals(cp2.getName())) return 0;
      return cp1.costReplicas() < cp2.costReplicas() ? -1 : 1;
    };
    
    Set<Plan> finalLs = new TreeSet<Plan>(cp);
    featureLs.forEach(map -> {
      if(!map.isEmpty()) {
        TreeSet<Plan> featureSet = new TreeSet<Plan>(cp);
        featureSet.addAll(map.values());
        finalLs.add(featureSet.first());
      }
    });
    
    //Part4 : print result
    finalLs.forEach(p -> System.out.println("plan: " + p.getName() +", cost: ($) " + p.getCost()));
    System.out.println("Total Cost : ($) " +finalLs.stream().mapToDouble(p -> p.getCost()).sum());
  }
  
  public static Plans prepareData() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
    ObjectMapper objs = new ObjectMapper();
    return objs.readValue(Files.newInputStream(Paths.get(Main.class.getClassLoader()
      .getResource("question1_data.json").toURI())), Plans.class);
  }

}
