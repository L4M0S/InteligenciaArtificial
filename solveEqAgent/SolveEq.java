package solveEqAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;



////////////////////////////////////////////////////////////////////////////////
class Individual {
    private int[] chromosome; 
    private double fitness= -1;
    
    public Individual(int chromosomeLength){
        this.chromosome= new int[chromosomeLength];
        for(int gene=0; gene<chromosomeLength; gene++){
            this.setGene(gene, (int) (10 * Math.random()));
        }
    }
    
    public int evaluate(){
        int a=this.chromosome[0];
        int b=this.chromosome[1];
        int c=this.chromosome[2];
        int d=this.chromosome[3];
        int e=this.chromosome[4];
        int f=this.chromosome[5];
        return a + (2*b) - (3*c) + d + (4*e) + f;
    }
    
    public int[] getChromosome(){
        return this.chromosome;
    }
    
    public int getChromosomeLength(){
        return this.chromosome.length;
    }
    
    public void setGene(int offset, int gene){
        this.chromosome[offset] = gene;
    }
    
    public int getGene(int offset){
        return this.chromosome[offset];
    }
    
    public void setFitness(double fitness){
        this.fitness = fitness;
    }
    
    public double getFitness(){
        return this.fitness;
    }
    
    public String toString(){
        String output="";
        for(int gene=0;gene<this.chromosome.length;gene++){
            output += this.chromosome[gene];
        }
        return output;
    }
}
////////////////////////////////////////////////////////////////////////////////
class Population {
    private Individual population[]; 
    private double populationFitness = -1;
    
    public Population(int populationSize){
        this.population=new Individual[populationSize];
    }
    
    public Population(int populationSize, int chromosomeLength){
        this.population= new Individual[populationSize];
        
        for(int individualCount=0; individualCount<populationSize; individualCount++){
            Individual individual=new Individual(chromosomeLength);
            this.population[individualCount]=individual;
        }
    }
    
    public Individual[] getIndividual(){
        return this.population;
    }
    
    public Individual getFittest(int offset){
        Arrays.sort(this.population, new Comparator<Individual>(){
            @Override
            public int compare(Individual i1, Individual i2){
                if(i1.getFitness()>i2.getFitness()){
                //if(o1.getValue()>o2.getValue()){
                    return -1;
                }
                return 0;
            }   
        });
        return this.population[offset];
    }
    
    public void setPopulationFitness(double fitness){
        this.populationFitness = fitness;
    }
    
    public double getPopulationFitness(){
        return this.populationFitness;
    }
    
    public int getSize(){
        return this.population.length;
    }
    
    public Individual setIndividual(int offset, Individual individual){
        return population[offset] = individual;
    }
    
    public Individual getIndividual(int offset){
        return population[offset];
    }
}
////////////////////////////////////////////////////////////////////////////////
class GeneticAlgorithm { 
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int value;
    
    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,int value){
    	this.populationSize=populationSize;
    	this.mutationRate=mutationRate;
    	this.crossoverRate=crossoverRate;
    	this.elitismCount=elitismCount;
        this.value=value;
    }

    public Population initPopulation(int chromosomeLength){
    	Population population=new Population(this.populationSize, chromosomeLength);
    	return population;
    }
    
    public double calcFitness(Individual individual){        
        int calc=individual.evaluate();
        double fitness=100-Math.abs(calc - this.value);
        individual.setFitness(fitness);
        
        return fitness;
    }
    
    public void evalPopulation(Population population){
        double populationFitness=0;
        for(Individual individual : population.getIndividual()){
            populationFitness += calcFitness(individual);
        }
        population.setPopulationFitness(populationFitness);
    }
    
    public boolean isTerminalCondition(Population population){
        for(Individual individual : population.getIndividual()){
            if(individual.evaluate()==this.value){
                return true;
            }
        }
        return false;
    }
    
    public Individual selectParent(Population population){
        Individual individuals[]= population.getIndividual();
        
        double populationFitness=population.getPopulationFitness();
        double rouletteWheelPosition=Math.random()*populationFitness;
        
        double spinWheel=0;
        for(Individual individual : individuals){
            spinWheel += individual.getFitness();
            if(spinWheel>=rouletteWheelPosition){
                return individual;
            }
        }
        return individuals[population.getSize()-1];
    }
    
    public Population crossoverPopulation(Population population){
        Population newPopulation=new Population(population.getSize());
        
        for(int populationIndex=0;populationIndex<population.getSize();populationIndex++){
            Individual parent1 = population.getFittest(populationIndex);
            
            if(this.crossoverRate>Math.random() && populationIndex>=this.elitismCount){
                Individual offspring=new Individual(parent1.getChromosomeLength());
                
                Individual parent2= selectParent(population);
                
                for(int geneIndex=0;geneIndex<parent1.getChromosomeLength();geneIndex++){
                    if(0.5>Math.random()){
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    }else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }
                
                newPopulation.setIndividual(populationIndex, offspring);
            }else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }
    
    public Population mutatePopulation(Population population){
        Population newPopulation=new Population(this.populationSize);
        
        for(int populationIndex=0; populationIndex < population.getSize(); populationIndex++){
            Individual individual = population.getFittest(populationIndex);
            
            for(int geneIndex=0; geneIndex < individual.getChromosomeLength(); geneIndex++){
                if(populationIndex > this.elitismCount){
                    if(this.mutationRate > Math.random()){
                        //int newGene=1;
                        int newGene=(int) (10 * Math.random());
                        if(individual.getGene(geneIndex)==newGene){
                            newGene=(int) (10 * Math.random());
                        }
                        individual.setGene(geneIndex, newGene);
                    }
                }
            }
            
            newPopulation.setIndividual(populationIndex, individual);
        }
        
        return newPopulation;
    }

    public void printSolution(Individual individual){
        int a=individual.getGene(0);
        int b=individual.getGene(1);
        int c=individual.getGene(2);
        int d=individual.getGene(3);
        int e=individual.getGene(4);
        int f=individual.getGene(5);
        System.out.println("Ecuacion: a + (2*b) - (2*c) + d + (4*e) + f = "+this.value);
        System.out.println("Ecuacion: "+a+" + (2*"+b+") - (2*"+c+") + "+d+" + (4*"+e+") + "+f+" = "+this.value);
    }
}
////////////////////////////////////////////////////////////////////////////////
public class SolveEq extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");

    addBehaviour(new MySolveEqAgentBehaviour());

  } 

  private class MySolveEqAgentBehaviour extends OneShotBehaviour {
   
    public void action() {
        DecimalFormat df2 = new DecimalFormat("#.##");
        
        GeneticAlgorithm genAl=new GeneticAlgorithm(6,0.01,0.95,0,29);
        Population population= genAl.initPopulation(6);
        genAl.evalPopulation(population);
        int generation=1;
                    
        while(!genAl.isTerminalCondition(population)){
            System.out.println("Generacion: "+ generation +" Population Fitness:"+ df2.format(population.getPopulationFitness()));
            for(int i=0; i<population.getSize();i++){
                System.out.println(population.getIndividual(i).toString()+ " Fitness:"+ population.getIndividual(i).getFitness()+" "+df2.format((population.getIndividual(i).getFitness()*100)/population.getPopulationFitness()));
            }
            
            population=genAl.crossoverPopulation(population);
            population=genAl.mutatePopulation(population);
            genAl.evalPopulation(population);
            generation++;
        }
        
        
        System.out.println("Generacion: "+ generation +" Population Fitness:"+ df2.format(population.getPopulationFitness()));
        for(int i=0; i<population.getSize();i++){
            System.out.println(population.getIndividual(i).toString()+ " Fitness:"+ population.getIndividual(i).getFitness()+" "+df2.format((population.getIndividual(i).getFitness()*100)/population.getPopulationFitness()));
        }
        
        
        System.out.println("\n\nGeneracion de la solucion: "+ generation+" Population Fitness: "+ df2.format(population.getPopulationFitness()));
        System.out.println("Solucion: "+ population.getFittest(0).toString()+" Fitness: "+ population.getIndividual(0).getFitness()+" "+df2.format((population.getIndividual(0).getFitness()*100)/population.getPopulationFitness()));
        population.getIndividual(0).evaluate();

        genAl.printSolution(population.getIndividual(0));

    } 

    public int onEnd() {
      System.out.println("Agent "+getLocalName()+" finished.");
      myAgent.doDelete();
      return super.onEnd();
    } 
}
}