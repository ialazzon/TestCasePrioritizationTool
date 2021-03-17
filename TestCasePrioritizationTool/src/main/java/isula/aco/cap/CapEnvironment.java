package isula.aco.cap;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;


import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The Environment type is for storing problem-specific information. In the TSP scenario, is only relateed
 * to the number of cities.
 */
public class CapEnvironment extends Environment {

    private static Logger logger = Logger.getLogger(CapEnvironment.class.getName());

    //private final int numberOfCities;

    public CapEnvironment(double[][] problemGraph) throws InvalidInputException {
        super(problemGraph);
        //this.numberOfCities = problemGraph.length;
        //logger.info("Number of cities: " + numberOfCities);
        //m = num_test_cases;
        //n= num_test_cases;
    }


    //public int getNumberOfCities() {
        //return getProblemRepresentation().length;
   //}
    
    public static int[][] coverage_matrix;

    public static int num_rules;
    public static int num_test_cases;

    public  static int m;
    public  static int n;


    /**
     * The pheromone matrix in the TSP problem stores a pheromone value per city and per position of this city on
     * the route. That explains the dimensions selected for the pheromone matrix.
     *
     * @return Pheromone matrix instance.
     */
    @Override
    protected double[][] createPheromoneMatrix() {
    	return new double[n][m];
    }
    
    
    

	
	public int allocation[];
	
	public double cost() {
		if(allocation==null) return 0.0;
		// Get fitness
        int[] TR = new int[num_rules];
        for(int i=0; i< num_rules; i++) {
        	int j=0;
        	for(j=0; j< allocation.length; j++) {
        		//System.out.println(Arrays.toString(allocation));
        		if(coverage_matrix[i][allocation[j]]==1) break;
        	}
        	TR[i] = j+1;
        }
        	
        int TR_sum = 0;
        for(int i = 0; i< num_rules; i++) 
        	TR_sum += TR[i];
        double APRD = 100 * (1 - (double) TR_sum/(num_rules * n) + 1.0/(2*n));
        
        double tran_APRD = APRD + 100.0/(2*n);
        
        return 1.0/(tran_APRD + Float.MIN_VALUE) ;
	}
	
	
	
	public boolean isValid() {
		
		if(allocation==null) return true;
		for(int i= 0; i<allocation.length; i++) if(allocation[i]<0 || allocation[i]>=m) return false;
		for(int i=0; i<allocation.length; i++) {
			int X = allocation[i];
			for(int j=0; j<allocation.length; j++) 
				if(j!=i && allocation[i]==allocation[j])
					return false;
		}
			
		
		return true;
	}
    
}
