package tcp_tool;
import java.util.Arrays;

public class TC_SequenceEvaluator_APRD {
	public static double getAPRD(int n_rules, int n_test_cases, int[][] cov_matrix, int[] t_case_order ) {
		coverage_matrix = cov_matrix;
		test_case_order = t_case_order;
		num_test_cases=n_test_cases;
		num_rules = n_rules;
		
		int TF_sum = 0;
		int[] TF = new int[num_rules];
		
        initialize_TF(TF);
        //System.out.println(Arrays.toString(TF));
        for(int i = 0; i< TF.length; i++) 
        	TF_sum += TF[i];
        double APFD = 100 * (1 - (double) TF_sum/(num_test_cases * num_rules) + 1.0/(2*num_test_cases));
        
        return APFD;
	}
	
	
	private static int[][] coverage_matrix;
	private static int[] test_case_order;	
	private static int num_test_cases;
	private static int num_rules;

	public static void main(String args[]) {
		
		int TF_sum = 0;
		int[] TF = new int[num_rules];
		
        initialize_TF(TF);
        System.out.println(Arrays.toString(TF));
        for(int i = 0; i< TF.length; i++) 
        	TF_sum += TF[i];
        double APFD = 100 * (1 - (double) TF_sum/(num_test_cases * num_rules) + 1.0/(2*num_test_cases));
        
        
        System.out.printf("num_test_cases= %d, num_rules= %d%n", num_test_cases, num_rules);
        System.out.printf("TF_sum= %d%n", TF_sum);
        System.out.println("APFD= "+APFD + " %");
	}
	
	static void initialize_TF(int[] TF) {
		for(int i=0; i<num_rules; i++) {
			int count = 1;
			for(int j=0; j<num_test_cases; j++) {
				if(coverage_matrix[i][test_case_order[j]]==1) break;
				count++;
			}
			TF[i]=count;
		}
		
	}
}
