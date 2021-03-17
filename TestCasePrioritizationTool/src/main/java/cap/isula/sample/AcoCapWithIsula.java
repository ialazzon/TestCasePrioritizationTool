package cap.isula.sample;

import isula.aco.*;
import isula.aco.algorithms.antsystem.OfflinePheromoneUpdate;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.RandomNodeSelection;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.cap.AntForCap;
import isula.aco.cap.CapEnvironment;
import isula.aco.exception.InvalidInputException;

import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.util.Scanner;
/**
 * This class solves the Berlin52 instance of the TSPLIB repository using an Ant System algorithm,
 * trying to emulate the procedure present in Section 6.3 of the Clever Algorithms book by
 * Jason Brownlee.
 */
public class AcoCapWithIsula {
	
	public static int[] getOrderings(int n_rules, int n_test_cases, int[][] cov_matrix){
		LogManager.getLogManager().reset();
		logger.info("ANT SYSTEM FOR THE Component Allocation Problem");

        double[][] problemRepresentation = null;//getRepresentationFromFile(fileName);
        
        
        try{
        	CapEnvironment.num_rules = n_rules;
        	CapEnvironment.num_test_cases = n_test_cases;
        	CapEnvironment.coverage_matrix = cov_matrix;
        	CapEnvironment.n = n_test_cases;
	        CapEnvironment.m = n_test_cases;
        	CapEnvironment environment = new CapEnvironment(problemRepresentation);
        	
	        
	        CapProblemConfiguration configurationProvider = new CapProblemConfiguration(environment);
	        
	        AntColony<Integer, CapEnvironment> colony = getAntColony(configurationProvider);
	        
	
	        AcoProblemSolver<Integer, CapEnvironment> solver = new AcoProblemSolver<>();
	        
	        solver.initialize(environment, colony, configurationProvider);
	        solver.addDaemonActions(new StartPheromoneMatrix<Integer, CapEnvironment>(),
	                new PerformEvaporation<Integer, CapEnvironment>());
	
	        solver.addDaemonActions(getPheromoneUpdatePolicy());
	
	        solver.getAntColony().addAntPolicies(new RandomNodeSelection<Integer, CapEnvironment>());

	        solver.solveProblem();
	        
	        String bestSolution = solver.getBestSolutionAsString();
	        int[] bestS = new int[environment.n];
	        Scanner s = new Scanner(bestSolution);
	        int i=0;
	        while(s.hasNext()) {
	        	bestS[i]=s.nextInt();
	        	i++;
	        }
	        //System.out.println(Arrays.toString(bestS));
	        //CapEnvironment e2 = new CapEnvironment(null);
	        //e2.allocation=bestS;
	        return bestS;
        }catch(Exception e) {
        	System.out.println(e);
        	return null;
        }
        //System.out.println(e2.isValid());
        //System.out.println(e2.cost());
        //System.out.println("APRD= "+1.0/e2.cost());
	}

    private static Logger logger = Logger.getLogger(AcoCapWithIsula.class.getName());
    
    /**
     * Produces an Ant Colony instance for the TSP problem.
     *
     * @param configurationProvider Algorithm configuration.
     * @return Ant Colony instance.
     */
    public static AntColony<Integer, CapEnvironment> getAntColony(final ConfigurationProvider configurationProvider) {
        return new AntColony<Integer, CapEnvironment>(configurationProvider.getNumberOfAnts()) {
            @Override
            protected Ant<Integer, CapEnvironment> createAnt(CapEnvironment environment) {
                int initialReference = 0;//new Random().nextInt(environment.n);
                return new AntForCap(environment.n);
            }
        };
    }

    /**
     * On TSP, the pheromone value update procedure depends on the distance of the generated routes.
     *
     * @return A daemon action that implements this procedure.
     */
    private static DaemonAction<Integer, CapEnvironment> getPheromoneUpdatePolicy() {
        return new OfflinePheromoneUpdate<Integer, CapEnvironment>() {
            @Override
            protected double getPheromoneDeposit(Ant<Integer, CapEnvironment> ant,
                                                 Integer positionInSolution,
                                                 Integer solutionComponent,
                                                 CapEnvironment environment,
                                                 ConfigurationProvider configurationProvider) {
                Double contribution = 1 / ant.getSolutionCost(environment);
                return contribution;
            }
        };
    }

    public static double[][] getRepresentationFromFile(String fileName) throws IOException {
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        File file = new File(AcoCapWithIsula.class.getClassLoader().getResource(fileName).getFile());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");

                if (tokens.length == 3) {
                    xCoordinates.add(Double.parseDouble(tokens[1]));
                    yCoordinates.add(Double.parseDouble(tokens[2]));
                }
            }
        }

        double[][] representation = new double[xCoordinates.size()][2];
        for (int index = 0; index < xCoordinates.size(); index += 1) {
            representation[index][0] = xCoordinates.get(index);
            representation[index][1] = yCoordinates.get(index);

        }

        return representation;
    }


}
