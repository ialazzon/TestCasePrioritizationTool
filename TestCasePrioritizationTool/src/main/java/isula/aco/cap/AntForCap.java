package isula.aco.cap;

import isula.aco.Ant;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;

/**
 * An specialized Ant for building solutions for the TSP problem. It is designed according the algorithm described in
 * Section 6.3 of Clever Algorithms by Jason Brownlee.
 */
public class AntForCap extends Ant<Integer, CapEnvironment> {

    private static Logger logger = Logger.getLogger(AntForCap.class.getName());

    private static final double DELTA = Float.MIN_VALUE;
    private final int numberOfCities;
    private int initialReference=0;

    public AntForCap(int numberOfCities) {
        super();
        this.numberOfCities = numberOfCities;
        this.setSolution(new Integer[numberOfCities]);
    }

    @Override
    public void clear() {
        super.clear();
        this.initialReference = 0;
    }

    /**
     * On TSP, a solution is ready when all the cities are part of the solution.
     *
     * @param environment Environment instance with problem information.
     * @return True if the solution is ready.
     */
    @Override
    public boolean isSolutionReady(CapEnvironment environment) {
        return  getCurrentIndex() == environment.n;
    }


    /**
     * On TSP, the cost of a solution is the total distance traversed by the salesman.
     *
     * @param environment Environment instance with problem information.
     * @return Total distance.
     */
    @Override
    public double getSolutionCost(CapEnvironment environment) {
    		if(!isValid(environment)) return Integer.MAX_VALUE; 
    		else return getTotalDistance(getSolution(), environment);
    	
    }
    
    public boolean isValid(CapEnvironment environment) {
    	environment.allocation = convertToArray(getSolution());
    	return environment.isValid();
	
}


    /**
     * The heuristic contribution in TSP is related to the added travel distance given by selecting an specific component.
     * According to the algorithm on the book, when the solution is empty we take a random city as a reference.
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @return Heuristic contribution.
     */
    @Override
    public Double getHeuristicValue(Integer solutionComponent, Integer positionInSolution, CapEnvironment environment) {
    	//Integer lastComponent = this.initialReference;
        //if (getCurrentIndex() > 0) {
          //  lastComponent = this.getSolution()[getCurrentIndex() - 1];
        //}
    	
        
        double distance = getDistance(this.getSolution(), solutionComponent, getCurrentIndex(), environment) + DELTA;
        
        if(checkValid(this.getSolution(), solutionComponent, getCurrentIndex(), environment)) {
        		return 1.0/distance;
        }else
        	return 10000000.0;
    }

    /**
     * Just retrieves a value from the pheromone matrix.
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @return
     */
    @Override
    public Double getPheromoneTrailValue(Integer solutionComponent, Integer positionInSolution,
                                         CapEnvironment environment) {

    	double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    	//System.out.println("g"+" "+positionInSolution+ " "+solutionComponent+" "+Arrays.toString(convertToArray(this.getSolution())) +" "+pheromoneMatrix[positionInSolution][0]);
    	if (positionInSolution > 0)
    		return pheromoneMatrix[positionInSolution][this.getSolution()[positionInSolution-1]];
    	else return pheromoneMatrix[positionInSolution][0];
    	
    	/*Integer previousComponent = this.initialReference;
        if (positionInSolution > 0) {
            previousComponent = getSolution()[positionInSolution - 1];
        }

        double[][] pheromoneMatrix = environment.getPheromoneMatrix();
        return pheromoneMatrix[solutionComponent][previousComponent];*/
    }

    /**
     * On TSP, the neighbourhood is given by the non-visited cities.
     *
     * @param environment Environment instance with problem information.
     * @return
     */
    @Override
    public List<Integer> getNeighbourhood(CapEnvironment environment) {
        List<Integer> neighbourhood = new ArrayList<>();

        for(int i=0; i<environment.m; i++ )
        		if(!alreadyVisited(i))
        			neighbourhood.add(i);
        
        
        //neighbourhood.add(new Random().nextInt(environment.m));
        /*for (int cityIndex = 0; cityIndex < environment.n; cityIndex += 1) {
            if (!this.isNodeVisited(cityIndex)) {
                neighbourhood.add(cityIndex);
            }
        }*/

        return neighbourhood;
    }
    
    private boolean alreadyVisited(int i) {
    	if(this.getSolution()==null) return false;
    	int[] solution = convertToArray(this.getSolution());
    	
    	if(solution==null) return false;
    	for(int j=0; j<solution.length; j++)
    		if(solution[j]==i) return true;
    	return false;
    }


    /**
     * Just updates the pheromone matrix.
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @param value              New pheromone value.
     */
    @Override
    public void setPheromoneTrailValue(Integer solutionComponent, Integer positionInSolution,
                                       CapEnvironment environment, Double value) {
    	double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    	
    	pheromoneMatrix[positionInSolution][solutionComponent] = value;
    	//System.out.println("s"+" "+positionInSolution+ " "+solutionComponent+" "+Arrays.toString(convertToArray(this.getSolution())) );
    	
    	
        /*Integer previousComponent = this.initialReference;
        if (positionInSolution > 0) {
            previousComponent = getSolution()[positionInSolution - 1];
        }

        double[][] pheromoneMatrix = environment.getPheromoneMatrix();
        pheromoneMatrix[solutionComponent][previousComponent] = value;
        pheromoneMatrix[previousComponent][solutionComponent] = value;*/

    }


    /**
     * Calculates the total distance of a route for the salesman.
     *
     * @param route                 Route to evaluate.
     * @param problemRepresentation Coordinate information.
     * @return Total distance.
     */
    public static double getTotalDistance(Integer[] route, CapEnvironment environment) {
        environment.allocation = convertToArray(route);//Arrays.stream(route).mapToInt(Integer::intValue).toArray();
    	return environment.cost();
    	/*double totalDistance = 0.0;

        for (int solutionIndex = 1; solutionIndex < route.length; solutionIndex += 1) {
            int previousSolutionIndex = solutionIndex - 1;
            totalDistance += getDistance(route[previousSolutionIndex], route[solutionIndex], problemRepresentation);
        }

        totalDistance += getDistance(route[route.length - 1], route[0], problemRepresentation);

        return totalDistance;*/
    }

    /**
     * Calculates the distance between two cities.
     *
     * @param anIndex               Index of a city.
     * @param anotherIndex          Index of another city.
     * @param problemRepresentation Coordinate information.
     * @return Distance between these cities.
     */
    public static double getDistance(Integer[] solution, int SolutionComponent, int index, CapEnvironment environment) {
    	//System.out.println(Arrays.toString(convertToArray(solution))+" "+SolutionComponent+" "+index);
    	if(index<0) {
    		int s[] = new int[0];
    		s[0] = SolutionComponent;
    		return getTotalDistance(convertToIntegerArray(s), environment);
    	}
    	int s[] = convertToArray(solution);
    	int s0[] = new int[index];
    	int s1[] = new int[index+1];
    	for(int i=0; i<index; i++) {
    		s0[i]= s[i];
    		s1[i] = s[i];
    	}
    	s1[index]=SolutionComponent;
    		
    	
        return getTotalDistance(convertToIntegerArray(s1), environment) - 
        		getTotalDistance(convertToIntegerArray(s0), environment);

    }
    
    public static boolean checkValid(Integer[] solution, int SolutionComponent, int index, CapEnvironment environment) {
    	environment.allocation = convertToArray(solution);
    	return environment.isValid();
    }
    
    static int[] convertToArray(Integer[] a) {
    	if(a==null) return null;
    	int x=0;
    	for(x=0 ; x<a.length; x++)
    		if(a[x]==null) break;
    	if(x==0) return null;
    	int b[] = new int[x];
    	for(int i =0; i<x; i++)
    		b[i]=a[i];
    	return b;
    }
    
    static Integer[] convertToIntegerArray(int a[]) {
    	if(a==null) return null;
    	Integer b[] = new Integer[a.length];
    	for(int i =0; i<b.length; i++)
    		b[i]=a[i];
    	return b;
    }

    
    @Override
   public boolean isNodeVisited(Integer component) {
        
        return false;
    }

}
