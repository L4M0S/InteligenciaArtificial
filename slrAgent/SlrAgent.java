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
package slrAgent;

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
class Slr {
    
    static double x[]= {1,2,3,4,5,6,7,8,9,10};
    static double y[]= {2,4,6,8,10,12,14,16,18,20};
    static int n=x.length;    
    static double B0 = beta0();
    static double B1 = beta1();

    public static double Ds(){ //Determinante del sistema
        double sum1=0;
        double sum2=0;
        
        for(int i=0;i<=n-1;i++){
            sum1+= Math.pow(x[i],2);
        }
        for(int i=0;i<=n-1;i++){
            sum2+= x[i];
        }
        
     return ( n * sum1 ) - ( sum2 * sum2 );   
    }
    
    public static double Dbeta0(){ //Determinante Beta 0
        double sum1=0;
        double sum2=0;
        double sum3=0;
        double sum4=0;
        
        for(int i=0;i<=n-1;i++){
            sum1+= y[i];
        }
        for(int i=0;i<=n-1;i++){
            sum2+= Math.pow(x[i],2);
        }
        for(int i=0;i<=n-1;i++){
            sum3+= x[i] * y[i];
        }
        for(int i=0;i<=n-1;i++){
            sum4+= x[i];
        }
        
     return ( sum1 * sum2 ) - ( sum3 * sum4 );   
    }
    
    public static double Dbeta1(){ //Determinante Beta 1
        double sum1=0;
        double sum2=0;
        double sum3=0;
        
        for(int i=0;i<=n-1;i++){
            sum1+= x[i] * y[i];
        }
        for(int i=0;i<=n-1;i++){
            sum2+= x[i];
        }
        for(int i=0;i<=n-1;i++){
            sum3+= y[i];
        }
                
     return ( n * sum1 ) - ( sum2 * sum3 );  
    }
    
    public static double beta0(){ //Beta 0
        return Dbeta0() / Ds();
    }
        
    public static double beta1(){ //Beta 1
        return Dbeta1() / Ds();    
    }
    
    public static double predict(double valx) {
        double valy = B0 + ( B1 * valx );
        
        return valy;
    }
    
}


public class SlrAgent extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");

    addBehaviour(new MySlrAgentBehaviour());

  } 

  private class MySlrAgentBehaviour extends OneShotBehaviour {
   
    public void action() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("val x: ");
        String cadena = sc.nextLine();
        
        double valx=Double.parseDouble(cadena);
        
        Slr mySlr= new Slr();
        
        double B0 = mySlr.beta0();
        double B1 = mySlr.beta1();  
        
        System.out.println("Ecuacion: y = B0 + B1 * x");
        System.out.format("          y = %.2f + %.2f * %.2f\n",B0,B1,valx);
        System.out.format("          y = %.2f\n",mySlr.predict(valx));
    } 

    public int onEnd() {
      System.out.println("Agent "+getLocalName()+" finished.");
      myAgent.doDelete();
      return super.onEnd();
    } 
  } 
}