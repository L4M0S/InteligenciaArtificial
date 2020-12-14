/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

//package examples.behaviours;
package mlrAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.Scanner;

/**
 * This example shows the basic usage of JADE behaviours.<br>
 * More in details this agent executes a <code>CyclicBehaviour</code> that shows
 * a printout at each round and a generic behaviour that performs four successive
 * "dummy" operations. The second operation in particular involves adding a
 * <code>OneShotBehaviour</code>. When the generic behaviour completes the
 * agent terminates.
 * @author Giovanni Caire - TILAB
 */
class Mlr {
        /*static double x1[]= {41.9,43.4,43.9,44.5,47.3,47.5,47.9,50.2,52.8,53.2,56.7,57,63.5,65.3,71.1,77,77.8};
        static double x2[]= {29.1,29.3,29.5,29.7,29.9,30.3,30.5,30.7,30.8,30.9,31.5,31.7,31.9,32,32.1,32.5,32.9};
        static double y[]= {251.3,251.3,248.3,267.5,273,276.5,270.3,274.9,285,290,297,302.5,304.5,309.3,321.7,330.7,349};
        */

        static double x1[]= {1,2,3,4,5,6,7,8,9,10};
        static double x2[]= {2,4,6,8,10,12,14,16,18,20};
        static double y[]= {3,6,9,12,15,18,21,24,27,30};
        
        static int n=x1.length;
        static double B0 = beta0();
        static double B1 = beta1();
        static double B2 = beta2();

    public static double sum(double x[]){
        double val=0;
        for(int i=0;i<=x.length-1;i++){
            val+= x[i];
        }
        return val;
    }
    public static double sum(double x[],double y[]){
        double val=0;
        for(int i=0;i<=x.length-1;i++){
            val+= x[i]*y[i];
        }
        return val;
    }
    public static double sum(double x[],int y){
        double val=0;
        for(int i=0;i<=x.length-1;i++){
            val+= Math.pow(x[i],y);
        }
        return val;
    }
    public static double Ds(){ //Determinante del sistema
                
     return ( n * sum(x1,2) * sum(x2,2) ) + ( sum(x1) * sum(x1,x2) * sum(x2) ) + ( sum(x2) * sum(x1) * sum(x1,x2) )
          - ( sum(x2) * sum(x1,2) *  sum(x2) ) - ( sum(x1,x2) * sum(x1,x2) * n ) - ( sum(x2,2) * sum(x1) * sum(x1) );   
    }
    
    public static double Dbeta0(){ //Determinante Beta 0
                
     return ( sum(y) * sum(x1,2) * sum(x2,2) ) + ( sum(x1) * sum(x1,x2) * sum(x2,y) ) + ( sum(x2) * sum(x1,y) * sum(x1,x2) ) 
          - ( sum(x2,y) * sum(x1,2) * sum(x2) ) - ( sum(x1,x2) * sum(x1,x2) * sum(y) ) - ( sum(x2,2) * sum(x1,y) * sum(x1) );   
    }
    
    public static double Dbeta1(){ //Determinante Beta 1
                        
     return ( n * sum(x1,y) * sum(x2,2) ) + ( sum(y) * sum(x1,x2) * sum(x2) ) + ( sum(x2) * sum(x1) * sum(x2,y) ) 
          - ( sum(x2) * sum(x1,y) * sum(x2) ) - ( sum(x2,y) * sum(x1,x2) * n ) - ( sum(x2,2) * sum(x1) * sum(y) );  
    }
    
    public static double Dbeta2(){ //Determinante Beta 2
                        
     return ( n * sum(x1,2) * sum(x2,y) ) + ( sum(x1) * sum(x1,y) * sum(x2) ) + ( sum(y) * sum(x1) * sum(x1,x2) ) 
          - ( sum(x2) * sum(x1,2) * sum(y) ) - ( sum(x1,x2) * sum(x1,y) * n ) - ( sum(x2,y) * sum(x1) * sum(x1) );  
    }
    ///////////////////////////////////////////////////////////////////////////
    public static double beta0(){ //Beta 0
        return Dbeta0() / Ds();
    }
    
    public static double beta1(){ //Beta 1
        return Dbeta1() / Ds();    
    }
    
    public static double beta2(){ //Beta 2
        return Dbeta2() / Ds();    
    }
    ///////////////////////////////////////////////////////////////////////////
    public static double predict(double valx1,double valx2) {
                
        double valy = B0 + ( B1 * valx1 ) + ( B2 * valx2 );
        
        return valy;
    }
    
}


public class MlrAgent extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");

    addBehaviour(new MyMlrAgentBehaviour());

  } 

  private class MyMlrAgentBehaviour extends OneShotBehaviour {
   
    public void action() {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("val x1: ");
        String cadena1 = sc.nextLine();
        System.out.print("val x2: ");
        String cadena2 = sc.nextLine();
        
        double valx1=Double.parseDouble(cadena1);
        double valx2=Double.parseDouble(cadena2);
        
        
        Mlr myMlr= new Mlr();
        
        double B0 = myMlr.beta0();
        double B1 = myMlr.beta1();
        double B2 = myMlr.beta2();  
        
        System.out.println("Ecuacion: y = B0 + B1*X1 + B2*X2");
        System.out.format("          y = %.2f + %.2f * %.2f + %.2f * %.2f\n",B0,B1,valx1,B2,valx2);
        System.out.format("          y = %.2f\n",myMlr.predict(valx1,valx2));
    } 

    public int onEnd() {
      System.out.println("Agent "+getLocalName()+" finished.");
      myAgent.doDelete();
      return super.onEnd();
    } 
  } 
}