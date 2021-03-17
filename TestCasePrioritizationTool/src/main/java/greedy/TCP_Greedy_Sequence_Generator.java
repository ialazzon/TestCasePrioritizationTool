package greedy;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;

public class TCP_Greedy_Sequence_Generator {
	public static int[] getOrderings(int n_rules, int n_test_cases, int[][] cov_matrix){
		num_test_cases=n_test_cases;
		max_num_test_Cases=n_test_cases;
		coverage_matrix = cov_matrix;
		num_rules = n_rules;
		
		List<Node2> lNodes = new ArrayList<>();
		for(int i=0; i<max_num_test_Cases; i++) {
			int num_covered_rules = getNumCoveredRules(i);
			lNodes.add(new Node2(i, num_covered_rules));
			//System.out.println(num_covered_rules);
		}
		//try {
			Collections.sort(lNodes);
		//}catch(Exception e) {
			
		//}
		//System.out.print("[");
		ListIterator<Node2> iterator = lNodes.listIterator(); 
		int count = 0;
		int[] orderings = new int[num_test_cases];
		while (iterator.hasNext()) { 
            //System.out.print(iterator.next());
            //orderings[count]=iterator.next().tc_id;
			Node2 n = iterator.next();
            orderings[count] = n.tc_id;
            //System.out.print(n.tc_id+","+n.num_covered_rules+" ");
            count++;
			
            //if(count==num_test_cases) break;
            //System.out.print(",");
        } 
		//System.out.println("]");
		return orderings;
	}
	
	private static  int num_test_cases;
	private static int max_num_test_Cases;
	
	static int[][] coverage_matrix;
	
	
	private static int num_rules;
	
	public static void main(String args[]) {
		
		for(int x=0; x<30; x++) {
			List<Node2> lNodes = new ArrayList<>();
			for(int i=0; i<max_num_test_Cases; i++) {
				int num_covered_rules = getNumCoveredRules(i);
				lNodes.add(new Node2(i, num_covered_rules));
			}
			try {
				Collections.sort(lNodes);
			}catch(Exception e) {
				x--;
				continue;
			}
			System.out.print("[");
			ListIterator<Node2> iterator = lNodes.listIterator(); 
			int count = 0;
			while (iterator.hasNext()) { 
                System.out.print(iterator.next());
                count++;
                if(count==num_test_cases) break;
                System.out.print(",");
            } 
			System.out.println("]");
		}
		
	}
	
	public static int getNumCoveredRules(int col) {
		int count =0;
		for(int i=0; i<num_rules; i++) {
			if(coverage_matrix[i][col]==1) count++;
		}
		return count;
	}
	
}


class Node2 implements Comparable{
	int tc_id;
	int num_covered_rules;
	
	Node2(int tc_id, int num_covered_rules){
		this.tc_id = tc_id;
		this.num_covered_rules = num_covered_rules;
	}

	@Override
	public int compareTo(Object otherNode) {
		if(this.num_covered_rules>((Node2)otherNode).num_covered_rules) return -1;
		else if(this.num_covered_rules<((Node2)otherNode).num_covered_rules) return 1;
		else if(Math.random()>0.5) return 1;
		else return -1;
	}

	@Override
	public String toString() {
		return tc_id+"";
	}
	
	
	
	
}