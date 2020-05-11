/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cplex;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Cplex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IloException {
        // TODO code application logic here
//        model1();
//        solveMIP(10);
//        miplibtest();
        model2();
    }
    
    public static void miplibtest(){
        try{
            IloCplex model = new IloCplex();
            model.importModel("aflow40b.mps");
            model.solve();
        } catch (IloException ex) {
            Logger.getLogger(Cplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    public static void model1(){
        try{
            IloCplex cplex = new IloCplex();            
            //variables
            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");                     
            //expressions
            IloLinearNumExpr objective = cplex.linearNumExpr();
            objective.addTerm(0.12, x);
            objective.addTerm(0.15, y);         
            //define objectve
            cplex.addMinimize(objective);           
            //define constrains
            cplex.addGe(cplex.sum(cplex.prod(60, x),cplex.prod(60, y)), 300);
            cplex.addGe(cplex.sum(cplex.prod(12, x),cplex.prod(6, y)), 36);
            cplex.addGe(cplex.sum(cplex.prod(10, x),cplex.prod(30, y)), 90);         
            //solve 
            if (cplex.solve()){
                System.out.println("Obj = " + cplex.getObjValue());
                System.out.println("x = " + cplex.getValue(x));
                System.out.println("y = " + cplex.getValue(y));
            }
            else{
                System.out.println("Model not solved!");
            }
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }
    
    public static void model2(){
        try{
            IloCplex cplex = new IloCplex();
            
            //nurses
            int i = 11;
            //shift
            int j = 42;
            Double[][] c = new Double[11][42];
            //variables
//            IloNumVar[][] x = cplex.boolVarArray[11][42];
            IloNumVar[][] x = new IloNumVar[i][j];

                        
            for(int k=0; k<i; k++){
                for (int q = 0; q<j; q++){
                    x[k][q] = cplex.boolVar();
                }
            }
            
            
            ArrayList<Integer> prwi = new ArrayList<Integer>();
            ArrayList<Integer> mesimeri = new ArrayList<Integer>();
            ArrayList<Integer> vradu = new ArrayList<Integer>();
            prwi.add(0);
            prwi.add(1);
            prwi.add(6);
            prwi.add(7);
            prwi.add(12);
            prwi.add(13);
            prwi.add(18);
            prwi.add(19);
            prwi.add(24);
            prwi.add(25);
            prwi.add(30);
            prwi.add(31);
            prwi.add(36);
            prwi.add(37);
            
            for(int k : prwi){
//                System.out.println(k);              
                mesimeri.add(k + 2);
                vradu.add(k + 4);     
//                System.out.println(prwi.get(k) + " - " + k);
            }
            for(int p=0; p<i; p++){
                for(int k : prwi){
                    c[p][k] = 1.0;                
                }
            }
            for(int k : mesimeri){
                c[0][k] = 1.0;
                c[1][k] = 1.01;
                c[2][k] = 1.0;
                c[3][k] = 1.02;
                c[4][k] = 1.01;
                c[5][k] = 1.01;
                c[6][k] = 1.02;
                c[7][k] = 1.03;
                c[8][k] = 1.0;
                c[9][k] = 1.02;
                c[10][k] = 1.01;
            }
            for(int k : vradu){
                c[0][k] = 1.2;
                c[1][k] = 1.2;
                c[2][k] = 1.1;
                c[3][k] = 1.2;
                c[4][k] = 1.2;
                c[5][k] = 1.0;
                c[6][k] = 1.1;
                c[7][k] = 1.0;
                c[8][k] = 1.1;
                c[9][k] = 1.1;
                c[10][k] = 1.2;
            }
            
            for(int k = 0; k<prwi.size(); k++){
                System.out.println(vradu.get(k));         
            }
           
            //expressions
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j; p++){
                    objective.addTerm(c[k][p], x[k][p]);
                }            
            }
                      
            //define objectve
            cplex.addMinimize(objective);
            
            //define constrains
            //basic
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int p=0; p<j; p++){
//                for(int p=0; p<j; p++){                  
                        expr.addTerm(1.0, x[k][p]);                   
                }
                cplex.addLe(expr, 5.0);
            }
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                if (k==2)continue;
                if (k==0 || k==5){
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
    //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 3.0);
                }else{
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
        //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 4.0);
                }             
            }

            for (int p = 0; p<j; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[0][p]); 
                expr.addTerm(1.0, x[1][p]); 
                expr.addTerm(1.0, x[2][p]); 
                expr.addTerm(1.0, x[3][p]); 
                expr.addTerm(1.0, x[4][p]); 
                expr.addTerm(1.0, x[5][p]); 
                expr.addTerm(1.0, x[6][p]); 
                expr.addTerm(1.0, x[7][p]); 
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p]); 
                cplex.addEq(expr, 1.0);
            } 
            //DE nurses not without TE
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            //sumvasiouxes mia vradia tin evdomada..
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[7][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j-5; p++){
//                    objective.addTerm(c[k][p], x[k][p]);
                    cplex.addLe(cplex.sum(cplex.prod(1, x[k][p]),cplex.prod(1, x[k][p+1]),cplex.prod(1, x[k][p+2]),cplex.prod(1, x[k][p+3]),
                            cplex.prod(1, x[k][p+4]),cplex.prod(1, x[k][p+5])), 1);
                }               
            }
            //week constrains
            //katerina
            cplex.addEq(x[3][5], 1.0);
            cplex.addEq(x[3][11], 1.0);
            cplex.addEq(x[3][21], 1.0);
            cplex.addEq(x[3][27], 1.0);
            cplex.addEq(x[3][41], 1.0);
            
            //vasiliki
            for (int p = 6; p<j; p++){
                cplex.addEq(x[2][p], 0.0);
            }
            cplex.addEq(x[2][0], 1.0);
            
            //agapi
            for (int p = 6; p<27; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            for (int p = 36; p<j; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            //maria gk
            for (int p = 24; p<j; p++){
                cplex.addEq(x[1][p], 0.0);
            }
            //valasia
            for (int p = 0; p<5; p++){
                cplex.addEq(x[4][p], 0.0);
            }
            //anastasia
            for (int p = 0; p<23; p++){
                cplex.addEq(x[5][p], 0.0);
            }
            //evgenia
            //no constrains for the 1st week
            //xaroula
            cplex.addEq(x[7][2], 0.0);
            cplex.addEq(x[7][3], 0.0);
            cplex.addEq(x[7][38], 0.0);
            cplex.addEq(x[7][39], 0.0);
            //parthena
            //no constrains for the 1st week
            //eleni
            for (int p = 18; p<29; p++){
                cplex.addEq(x[9][p], 0.0);
            }
            //mariaT
            //no constrains for the 1st week
            
            
            //solve 
            if (cplex.solve()){
                System.out.println("Obj = " + cplex.getObjValue());
                for (int k = 0; k<i; k++){
                    for (int p = 0; p<j; p++){
                        if (cplex.getValue(x[k][p])==1){
                            System.out.println("x["+k+"]["+p+"]" +"= " + cplex.getValue(x[k][p]));
                        }
                        
                    }              
                }
            }
            else{
                System.out.println("Model not solved!");
            }
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }


    public static void model3s(){
        try{
            IloCplex cplex = new IloCplex();
            
            //nurses
            int i = 11;
            //shift
            int j = 42;
            Double[][] c = new Double[11][42];
            //variables
//            IloNumVar[][] x = cplex.boolVarArray[11][42];
            IloNumVar[][] x = new IloNumVar[i][j];

                        
            for(int k=0; k<i; k++){
                for (int q = 0; q<j; q++){
                    x[k][q] = cplex.boolVar();
                }
            }
            
            
            ArrayList<Integer> prwi = new ArrayList<Integer>();
            ArrayList<Integer> mesimeri = new ArrayList<Integer>();
            ArrayList<Integer> vradu = new ArrayList<Integer>();
            prwi.add(0);
            prwi.add(1);
            prwi.add(6);
            prwi.add(7);
            prwi.add(12);
            prwi.add(13);
            prwi.add(18);
            prwi.add(19);
            prwi.add(24);
            prwi.add(25);
            prwi.add(30);
            prwi.add(31);
            prwi.add(36);
            prwi.add(37);
            
            for(int k : prwi){
//                System.out.println(k);              
                mesimeri.add(k + 2);
                vradu.add(k + 4);     
//                System.out.println(prwi.get(k) + " - " + k);
            }
            for(int p=0; p<i; p++){
                for(int k : prwi){
                    c[p][k] = 1.0;                
                }
            }
            for(int k : mesimeri){
                c[0][k] = 1.0;
                c[1][k] = 1.01;
                c[2][k] = 1.0;
                c[3][k] = 1.02;
                c[4][k] = 1.01;
                c[5][k] = 1.01;
                c[6][k] = 1.02;
                c[7][k] = 1.03;
                c[8][k] = 1.0;
                c[9][k] = 1.02;
                c[10][k] = 1.01;
            }
            for(int k : vradu){
                c[0][k] = 1.2;
                c[1][k] = 1.2;
                c[2][k] = 1.1;
                c[3][k] = 1.2;
                c[4][k] = 1.2;
                c[5][k] = 1.0;
                c[6][k] = 1.1;
                c[7][k] = 1.0;
                c[8][k] = 1.1;
                c[9][k] = 1.1;
                c[10][k] = 1.2;
            }
            
            for(int k = 0; k<prwi.size(); k++){
                System.out.println(vradu.get(k));         
            }
           
            //expressions
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j; p++){
                    objective.addTerm(c[k][p], x[k][p]);
                }            
            }
                      
            //define objectve
            cplex.addMinimize(objective);
            
            //define constrains
            //basic
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int p=0; p<j; p++){
//                for(int p=0; p<j; p++){                  
                        expr.addTerm(1.0, x[k][p]);                   
                }
                cplex.addLe(expr, 5.0);
            }
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                if (k==2)continue;
                if (k==0 || k==5){
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
    //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 3.0);
                }else{
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
        //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 4.0);
                }             
            }

            for (int p = 0; p<j; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[0][p]); 
                expr.addTerm(1.0, x[1][p]); 
                expr.addTerm(1.0, x[2][p]); 
                expr.addTerm(1.0, x[3][p]); 
                expr.addTerm(1.0, x[4][p]); 
                expr.addTerm(1.0, x[5][p]); 
                expr.addTerm(1.0, x[6][p]); 
                expr.addTerm(1.0, x[7][p]); 
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p]); 
                cplex.addEq(expr, 1.0);
            } 
            //DE nurses not without TE
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            //sumvasiouxes mia vradia tin evdomada..
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[7][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j-5; p++){
//                    objective.addTerm(c[k][p], x[k][p]);
                    cplex.addLe(cplex.sum(cplex.prod(1, x[k][p]),cplex.prod(1, x[k][p+1]),cplex.prod(1, x[k][p+2]),cplex.prod(1, x[k][p+3]),
                            cplex.prod(1, x[k][p+4]),cplex.prod(1, x[k][p+5])), 1);
                }               
            }
            //week constrains
            //katerina
            cplex.addEq(x[3][5], 1.0);
            cplex.addEq(x[3][11], 1.0);
            cplex.addEq(x[3][21], 1.0);
            cplex.addEq(x[3][27], 1.0);
            cplex.addEq(x[3][41], 1.0);
            
            //vasiliki
            for (int p = 6; p<j; p++){
                cplex.addEq(x[2][p], 0.0);
            }
            cplex.addEq(x[2][0], 1.0);
            
            //agapi
            for (int p = 6; p<27; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            for (int p = 36; p<j; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            //maria gk
            for (int p = 24; p<j; p++){
                cplex.addEq(x[1][p], 0.0);
            }
            //valasia
            for (int p = 0; p<5; p++){
                cplex.addEq(x[4][p], 0.0);
            }
            //anastasia
            for (int p = 0; p<23; p++){
                cplex.addEq(x[5][p], 0.0);
            }
            //evgenia
            //no constrains for the 1st week
            //xaroula
            cplex.addEq(x[7][2], 0.0);
            cplex.addEq(x[7][3], 0.0);
            cplex.addEq(x[7][38], 0.0);
            cplex.addEq(x[7][39], 0.0);
            //parthena
            //no constrains for the 1st week
            //eleni
            for (int p = 18; p<29; p++){
                cplex.addEq(x[9][p], 0.0);
            }
            //mariaT
            //no constrains for the 1st week
            
            
            //solve 
            if (cplex.solve()){
                System.out.println("Obj = " + cplex.getObjValue());
                for (int k = 0; k<i; k++){
                    for (int p = 0; p<j; p++){
                        if (cplex.getValue(x[k][p])==1){
                            System.out.println("x["+k+"]["+p+"]" +"= " + cplex.getValue(x[k][p]));
                        }
                        
                    }              
                }
            }
            else{
                System.out.println("Model not solved!");
            }
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }
    
    
    public static void model4s(){
        try{
            IloCplex cplex = new IloCplex();
            
            //nurses
            int i = 11;
            //shift
            int j = 42;
            Double[][] c = new Double[11][42];
            //variables
//            IloNumVar[][] x = cplex.boolVarArray[11][42];
            IloNumVar[][] x = new IloNumVar[i][j];

                        
            for(int k=0; k<i; k++){
                for (int q = 0; q<j; q++){
                    x[k][q] = cplex.boolVar();
                }
            }
            
            
            ArrayList<Integer> prwi = new ArrayList<Integer>();
            ArrayList<Integer> mesimeri = new ArrayList<Integer>();
            ArrayList<Integer> vradu = new ArrayList<Integer>();
            prwi.add(0);
            prwi.add(1);
            prwi.add(6);
            prwi.add(7);
            prwi.add(12);
            prwi.add(13);
            prwi.add(18);
            prwi.add(19);
            prwi.add(24);
            prwi.add(25);
            prwi.add(30);
            prwi.add(31);
            prwi.add(36);
            prwi.add(37);
            
            for(int k : prwi){
//                System.out.println(k);              
                mesimeri.add(k + 2);
                vradu.add(k + 4);     
//                System.out.println(prwi.get(k) + " - " + k);
            }
            for(int p=0; p<i; p++){
                for(int k : prwi){
                    c[p][k] = 1.0;                
                }
            }
            for(int k : mesimeri){
                c[0][k] = 1.0;
                c[1][k] = 1.01;
                c[2][k] = 1.0;
                c[3][k] = 1.02;
                c[4][k] = 1.01;
                c[5][k] = 1.01;
                c[6][k] = 1.02;
                c[7][k] = 1.03;
                c[8][k] = 1.0;
                c[9][k] = 1.02;
                c[10][k] = 1.01;
            }
            for(int k : vradu){
                c[0][k] = 1.2;
                c[1][k] = 1.2;
                c[2][k] = 1.1;
                c[3][k] = 1.2;
                c[4][k] = 1.2;
                c[5][k] = 1.0;
                c[6][k] = 1.1;
                c[7][k] = 1.0;
                c[8][k] = 1.1;
                c[9][k] = 1.1;
                c[10][k] = 1.2;
            }
            
            for(int k = 0; k<prwi.size(); k++){
                System.out.println(vradu.get(k));         
            }
           
            //expressions
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j; p++){
                    objective.addTerm(c[k][p], x[k][p]);
                }            
            }
                      
            //define objectve
            cplex.addMinimize(objective);
            
            //define constrains
            //basic
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int p=0; p<j; p++){
//                for(int p=0; p<j; p++){                  
                        expr.addTerm(1.0, x[k][p]);                   
                }
                cplex.addLe(expr, 5.0);
            }
            for(int k=0; k<i; k++){ 
//            for (int k=0; k<i; k++){
                if (k==2)continue;
                if (k==0 || k==5){
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
    //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 3.0);
                }else{
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int p=0; p<j; p++){
        //                for(int p=0; p<j; p++){                  
                            expr.addTerm(1.0, x[k][p]);                   
                    }
                    cplex.addGe(expr, 4.0);
                }             
            }

            for (int p = 0; p<j; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[0][p]); 
                expr.addTerm(1.0, x[1][p]); 
                expr.addTerm(1.0, x[2][p]); 
                expr.addTerm(1.0, x[3][p]); 
                expr.addTerm(1.0, x[4][p]); 
                expr.addTerm(1.0, x[5][p]); 
                expr.addTerm(1.0, x[6][p]); 
                expr.addTerm(1.0, x[7][p]); 
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p]); 
                cplex.addEq(expr, 1.0);
            } 
            //DE nurses not without TE
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[8][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[8][p+1]);                
                cplex.addLe(expr, 1.1);
            }
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[9][p]); 
                expr.addTerm(1.0, x[10][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            for (int p = 0; p<j-1; p++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][p]); 
                expr.addTerm(1.0, x[9][p+1]);                
                cplex.addLe(expr, 1.1);
            } 
            //sumvasiouxes mia vradia tin evdomada..
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[7][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            for(int k = 0; k<vradu.size(); k++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                expr.addTerm(1.0, x[10][vradu.get(k)]);                                
                cplex.addLe(expr, 1.1);       
            }
            
            for (int k = 0; k<i; k++){
                for (int p = 0; p<j-5; p++){
//                    objective.addTerm(c[k][p], x[k][p]);
                    cplex.addLe(cplex.sum(cplex.prod(1, x[k][p]),cplex.prod(1, x[k][p+1]),cplex.prod(1, x[k][p+2]),cplex.prod(1, x[k][p+3]),
                            cplex.prod(1, x[k][p+4]),cplex.prod(1, x[k][p+5])), 1);
                }               
            }
            //week constrains
            //katerina
            cplex.addEq(x[3][5], 1.0);
            cplex.addEq(x[3][11], 1.0);
            cplex.addEq(x[3][21], 1.0);
            cplex.addEq(x[3][27], 1.0);
            cplex.addEq(x[3][41], 1.0);
            
            //vasiliki
            for (int p = 6; p<j; p++){
                cplex.addEq(x[2][p], 0.0);
            }
            cplex.addEq(x[2][0], 1.0);
            
            //agapi
            for (int p = 6; p<27; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            for (int p = 36; p<j; p++){
                cplex.addEq(x[0][p], 0.0);
            }
            //maria gk
            for (int p = 24; p<j; p++){
                cplex.addEq(x[1][p], 0.0);
            }
            //valasia
            for (int p = 0; p<5; p++){
                cplex.addEq(x[4][p], 0.0);
            }
            //anastasia
            for (int p = 0; p<23; p++){
                cplex.addEq(x[5][p], 0.0);
            }
            //evgenia
            //no constrains for the 1st week
            //xaroula
            cplex.addEq(x[7][2], 0.0);
            cplex.addEq(x[7][3], 0.0);
            cplex.addEq(x[7][38], 0.0);
            cplex.addEq(x[7][39], 0.0);
            //parthena
            //no constrains for the 1st week
            //eleni
            for (int p = 18; p<29; p++){
                cplex.addEq(x[9][p], 0.0);
            }
            //mariaT
            //no constrains for the 1st week
            
            
            //solve 
            if (cplex.solve()){
                System.out.println("Obj = " + cplex.getObjValue());
                for (int k = 0; k<i; k++){
                    for (int p = 0; p<j; p++){
                        if (cplex.getValue(x[k][p])==1){
                            System.out.println("x["+k+"]["+p+"]" +"= " + cplex.getValue(x[k][p]));
                        }
                        
                    }              
                }
            }
            else{
                System.out.println("Model not solved!");
            }
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }
    
    public static void solveMIP(int n){
        
        //random data
        double[] xPos = new double[n];
        double[] yPos = new double[n];
        for (int i=0; i<n; i++){
            xPos[i] = Math.random()*100;
            yPos[i] = Math.random()*100;
        }
        double[][] c = new double[n][n];
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                c[i][j] = Math.sqrt(Math.pow(xPos[i] - xPos[j], 2) + Math.pow(yPos[i] - yPos[j], 2));
            }
        }
        
        //model
        try{
            IloCplex cplex = new IloCplex();
            
            //variables
            IloNumVar[][] x = new IloNumVar[n][];
            for(int i = 0; i<n; i++){
                x[i] = cplex.boolVarArray(n);
            }
            IloNumVar[] u =  cplex.numVarArray(n, 0, Double.MAX_VALUE);
            
            //define objectve
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if (j!=i){
                        obj.addTerm(c[i][j], x[i][j]);
                    }
                }
            }
            cplex.addMinimize(obj);
            
                                    
            //define constrains
            //basic
            for (int j=0; j<n; j++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for(int i=0; i<n; i++){
                    if (j!=i){
                        expr.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }
            for (int i=0; i<n; i++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for(int j=0; j<n; j++){
                    if (j!=i){
                        expr.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }
            for (int i=1; i<n; i++){
                
                for(int j=1; j<n; j++){
                    if (j!=i){
                        IloLinearNumExpr expr = cplex.linearNumExpr();
                        expr.addTerm(1.0, u[i]);
                        expr.addTerm(-1.0, u[j]);
                        expr.addTerm(n-1, x[i][j]);
                        cplex.addLe(expr, n-2);
                    }
                }              
            }
            
            //solve 
            cplex.solve();
//            for (int i=1; i<n; i++){
//                for(int j=1; j<n; j++){
//                    if (j!=i){
//                        System.out.println("x = " + cplex.getValue(x[1][3]));
//                    }
//                }
//            }
            //end
            cplex.end();
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }
    
       public static void solvemyMIP(int n){
        
        //random data
        double[] xPos = new double[n];
        double[] yPos = new double[n];
        for (int i=0; i<n; i++){
            xPos[i] = Math.random()*100;
            yPos[i] = Math.random()*100;
        }
        double[][] c = new double[n][n];
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                c[i][j] = Math.sqrt(Math.pow(xPos[i] - xPos[j], 2) + Math.pow(yPos[i] - yPos[j], 2));
            }
        }
        
        //model
        try{
            IloCplex cplex = new IloCplex();
            
            //variables
            IloNumVar[][] x = new IloNumVar[n][];
            for(int i = 0; i<n; i++){
                x[i] = cplex.boolVarArray(n);
            }
            IloNumVar[] u =  cplex.numVarArray(n, 0, Double.MAX_VALUE);
            
            //define objectve
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for (int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if (j!=i){
                        obj.addTerm(c[i][j], x[i][j]);
                    }
                }
            }
            cplex.addMinimize(obj);
            
                                    
            //define constrains
            for (int j=0; j<n; j++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for(int i=0; i<n; i++){
                    if (j!=i){
                        obj.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }
            for (int i=0; i<n; i++){
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for(int j=0; j<n; j++){
                    if (j!=i){
                        obj.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }
            for (int i=1; i<n; i++){
                
                for(int j=1; j<n; j++){
                    if (j!=i){
                        IloLinearNumExpr expr = cplex.linearNumExpr();
                        expr.addTerm(1.0, u[i]);
                        expr.addTerm(-1.0, u[j]);
                        expr.addTerm(n-1, x[i][j]);
                        cplex.addLe(expr, n-2);
                    }
                }              
            }
            
            //solve 
            cplex.solve();
//            for (int i=1; i<n; i++){
//                for(int j=1; j<n; j++){
//                    if (j!=i){
//                        System.out.println("x = " + cplex.getValue(x[1][3]));
//                    }
//                }
//            }
            //end
            cplex.end();
        }
        catch(IloException exc){
            exc.printStackTrace();
        }
    }
    
    
}

