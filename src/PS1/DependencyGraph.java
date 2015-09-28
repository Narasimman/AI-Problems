package PS1;

import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class DependencyGraph {
  
  private DirectedGraph<Task, DefaultEdge> graph =
      new DefaultDirectedGraph<Task, DefaultEdge>(DefaultEdge.class);
  
  public DirectedGraph<Task, DefaultEdge> createDependencyGraph(List<Task> list, List<Integer> dep) {
    
    for(Task task : list) {
      graph.addVertex(task);     
    }
    
    for(int i = 0; i < dep.size(); i+=2) {
      graph.addEdge(list.get(dep.get(i)), list.get(dep.get(i + 1)));
    }
    //System.out.println(graph);
    return graph;
  }

}
