package GA_implementation;

import java.util.Arrays;

/**
 * Main, executive class for the Traveling Salesman Problem.
 * 
 * We don't have a real list of cities, so we randomly generate a number of them
 * on a 100x100 map.
 * 
 * The TSP requires that each city is visited once and only once, so we have to
 * be careful when initializing a random Individual and also when applying
 * crossover and mutation. Check out the GeneticAlgorithm class for
 * implementations of crossover and mutation for this problem.
 * 
 * @author bkanber
 *
 */
public class GA {
	
	public static int[] getOrderings(int n_rules, int n_test_cases, int[][] cov_matrix){
		num_test_cases=n_test_cases;
		coverage_matrix = cov_matrix;
		num_rules = n_rules;
		
		return doOneRun();
	}
	
	public static int maxGenerations = 1000;
	
	//Rows represent the rules; Columns represent test cases
	//row_i_j =1 if Rule i is executed by test case j, and 0 otherwise
	private static int[][] coverage_matrix;
	
		
	private static int num_rules;
	private static int num_test_cases;
	
	public static void main(String[] args) {
		for(int x=0; x<30; x++)
			doOneRun();
	}
	
	public static int[] doOneRun() {
		// Initial GA
				GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.3, 0.7, 2, 10);

				// Initialize population
				Population population = ga.initPopulation(num_test_cases);

				// Evaluate population
				ga.evalPopulation(population, coverage_matrix, num_rules, num_test_cases);

				
				//System.out.println("Start Fitness (APRD): " + population.getFittest(0).getFitness());

				// Keep track of current generation
				int generation = 1;
				// Start evolution loop
				while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
					// Print fittest individual from population
					//System.out.println("G"+generation+" Best Fitness (APRD): " + population.getFittest(0).getFitness());

					// Apply crossover
					population = ga.crossoverPopulation(population);

					// Apply mutation
					population = ga.mutatePopulation(population);

					// Evaluate population
					ga.evalPopulation(population, coverage_matrix, num_rules, num_test_cases);

					// Increment the current generation
					generation++;
				}
				
				//System.out.println("Stopped after " + maxGenerations + " generations.");
				//System.out.println("Best Fitness (APRD): " + population.getFittest(0).getFitness());
				return population.getFittest(0).getChromosome();
	}
}
