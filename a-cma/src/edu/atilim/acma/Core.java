package edu.atilim.acma;

import java.util.Random;

import edu.atilim.acma.search.BeamSearch;
import edu.atilim.acma.search.HillClimbing;
import edu.atilim.acma.search.ModSimAnn;
import edu.atilim.acma.test.RastriginSolution;
import edu.atilim.acma.util.Log;

public class Core {
	public static void main(String[] args) {
		Random rand = new Random();
		
		/*
		 * Rastrigin's function has a global optimum at (0, 0) with score 0.
		 * This test creates a random solution in a square, around (0, 0), from (-50, -50) to (50, 50)
		 * then executes hill climbing on it to find an optimal solution.
		 * RastriginSolution is an example solution implementation but the Hill Climbing
		 * implementation should run with anything implementing "Solution" interface,
		 * which we will implement in edu.atilim.acma.uml.Design eventually.
		 * This one should just provide a decent testing environment for all algorithm implementations.
		 * 
		 * A detailed LOG of Hill Climbing is generated in acma.log file within the project directory.
		 * Hill Climbing can be modified according to needs, the two constructor parameters are
		 * restart count and restart depth respectively.
		 * 
		 * This one with 200 on both runs pretty fast on my desktop with a significant reach to global optimum.
		 * 
		 * Best I got:
		 * Score of initial solution (22,137127, 7,110455): -1036,464253
		 * Score of final solution (-0,010443, 0,000279): -0,021752
		 * 
		 * Holaleya!
		 * 
		 * - Ekin
		 */
		
		// Add output log, to see details
		Log.addOutput("acma.log");
		
		// Get a starting point between (-50, -50) and (50, 50)
		double x = (rand.nextDouble() * 100) - 50.0;
		double y = (rand.nextDouble() * 100) - 50.0;
		
		// Create the initial solution
		RastriginSolution initial = new RastriginSolution(x, y);
		
		// Print score
		System.out.printf("Score of initial solution (%f, %f): %f\n", initial.getX(), initial.getY(), initial.score());
		System.out.println("Patience now.. Go get a coffee or something.");
		
		// Run Hill Climbing
		// HillClimbing hc = new HillClimbing(200, 200);
		// BeamSearch bc = new BeamSearch(500, 200);
		ModSimAnn msa = new ModSimAnn(10000, 4);
		// RastriginSolution finl = (RastriginSolution)hc.run(initial);
		// RastriginSolution finl2 = (RastriginSolution)bc.run(initial);
		RastriginSolution finl3 = (RastriginSolution)msa.run(initial);
		// Print score
		// System.out.printf("Score of final solution of Hill Climbing (%f, %f): %f\n", finl.getX(), finl.getY(), finl.score()); //Dere tepe týrmanýr
		// System.out.printf("Score of final solution of Beam Search (%f, %f): %f\n", finl2.getX(), finl2.getY(), finl2.score()); //Fasülyeden algoritma
		System.out.printf("Score of final solution of Mod Sim Annealing (%f, %f): %f\n", finl3.getX(), finl3.getY(), finl3.score()); //Dengesiz algoritma
	}
}
