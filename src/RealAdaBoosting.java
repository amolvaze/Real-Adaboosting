// Program for Real AdaBoosting by Amol Vaze (Net_Id:- asv130130)
import java.io.FileNotFoundException;
import java.util.ArrayList;
public class RealAdaBoosting {

	// Main Function
	public static void main(String[] args) throws FileNotFoundException {
        // application logic here
        double e = 2.7182818284;
        // Reading the input file from the reading class
      new ReadFile("adaboost-5.txt");
        ArrayList<Double> flist = new ArrayList<>();
        double bound = 1.0;
     // Loop runs through all number of examples
        for (int i = 0; i < ReadFile.n; i++) {
            flist.add(0.0);
        }

        System.out.println("Real Adaboosting: ");
        // Looping through the input read file
        for (int l = 0; l < ReadFile.T; l++) {
            //-------------

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("--------------------Iteration:" + (l+1) + "-------------------------------------------------");
            double minG = 500.0;
            int x_pos = 0;
            boolean flag = true;
            double z = 0;
            double final_prp=0.0,final_prn=0.0,final_pwp=0.0,final_pwn=0.0;
            
               // Calculations of Pr+, Pr-, Pw+, Pw- 
            for (int i = 0; i < ReadFile.n - 1; i++) {
                double pr_p = 0.0, pr_n = 0.0, pw_p = 0.0, pw_n = 0.0;
                for (int j = 0; j <= i; j++) {
                    if (ReadFile.Y.get(j) == -1) {
                        pw_n = pw_n + ReadFile.Prob_List.get(j);
                    } else {
                        pr_p = pr_p + ReadFile.Prob_List.get(j);
                    }
                }
                for (int j = i + 1; j < ReadFile.n; j++) {
                    if (ReadFile.Y.get(j) == 1) {
                        pw_p = pw_p + ReadFile.Prob_List.get(j);
                    } else {
                        pr_n = pr_n + ReadFile.Prob_List.get(j);
                    }
                }
               double G = 0.0;

            // Calculation of G error value   
               G = Math.sqrt(pr_p * pw_n) + Math.sqrt(pw_p * pr_n);
               
         // Comparing the values of G & minG
                if (minG > G) {
                    minG = G;
                    x_pos = i;
                    flag = true;
                    final_prp=pr_p;
                    final_prn = pr_n;
                    final_pwp=pw_p;
                    final_pwn = pw_n;
                }
            }

          for (int i = 0; i < ReadFile.n - 1; i++) {
                double pr_p = 0.0, pr_n = 0.0, pw_p = 0.0, pw_n = 0.0;
               for (int j = 0; j <= i; j++) {
                    if (ReadFile.Y.get(j) == -1) {
                        pr_n = pr_n + ReadFile.Prob_List.get(j);
                    } else {
                        pw_p = pw_p + ReadFile.Prob_List.get(j);
                    }
                }
                for (int j = i + 1; j < ReadFile.n; j++) {
                    if (ReadFile.Y.get(j) == 1) {
                        pr_p = pr_p + ReadFile.Prob_List.get(j);
                    } else {
                        pw_n = pw_n + ReadFile.Prob_List.get(j);
                    }
                }
              double G = 0.0;

              // Calculation of G error value  
              G = Math.sqrt(pr_p * pw_n) + Math.sqrt(pw_p * pr_n);
                
             // Comparing the values of G & minG
                if (minG > G) {
                    minG = G;
                    x_pos = i;
                    flag = false;
                    final_prp=pr_p;
                    final_prn = pr_n;
                    final_pwp=pw_p;
                    final_pwn = pw_n;
                    
                }

            }

         if (flag == true) {
                System.out.println("1.The selected weak classifier is: x<" + (ReadFile.X.get(x_pos) + ReadFile.X.get(x_pos + 1)) / 2);
            } else {
                System.out.println("1.The selected weak classifier is: x>" + (ReadFile.X.get(x_pos) + ReadFile.X.get(x_pos + 1)) / 2);

            }
            
            System.out.println("2.The G error value of ht: " + minG);
           
            //---------------------------Classifier h(x)---------------------------------------------//

            ArrayList<Integer> h = new ArrayList<>();
            if (flag == true) {
                for (int i = 0; i <= x_pos; i++) {
                    h.add(1);
                }
                for (int i = x_pos + 1; i < ReadFile.n; i++) {
                    h.add(-1);
                }
            }

            if (flag == false) {
                for (int i = 0; i <= x_pos; i++) {
                    h.add(-1);
                }
                for (int i = x_pos + 1; i < ReadFile.n; i++) {
                    h.add(1);
                }
            }
            

            //----------------------------------Calculation of Ct+ Ct- ---------------------------------------------------
            double Ct_p = 0.0, Ct_n = 0.0;
            Ct_p = 0.5 * Math.log((final_prp + ReadFile.epsilon) / (final_pwn + ReadFile.epsilon));
            Ct_n = 0.5 * Math.log((final_pwp + ReadFile.epsilon) / (final_prn + ReadFile.epsilon));
            System.out.println("3.The Weights Ct+, Ct- ");
            System.out.println("Ct+: " + Ct_p);
            System.out.println("Ct-: " + Ct_n);
            //-------------------Set Pre normalized Probabilities calculations ------------------------------------/
           for (int i = 0; i < ReadFile.n; i++) {
                double tempP = 0.0;
                if (h.get(i) == 1) {
                    tempP = ReadFile.Prob_List.get(i) * Math.pow(e, -(ReadFile.Y.get(i) * Ct_p));
                   // System.out.println("Pre Norm p: " + tempP);
                    ReadFile.Prob_List.set(i, tempP);
                } else {
                    tempP = ReadFile.Prob_List.get(i) * Math.pow(e, -(ReadFile.Y.get(i) * Ct_n));
                   // System.out.println("Pre Norm p: " + tempP);
                    ReadFile.Prob_List.set(i, tempP);
                }
            }
           //--------------------------------------------Calculation Of Z---------------------------------------------
            for (int i = 0; i < ReadFile.n; i++) // calculate z
            {
                z = z + ReadFile.Prob_List.get(i);
            }
            System.out.println("4.The probabilities normalization factor: " + z);
            //-----------------------------------Calculate New Normalized Probabilities -------------------------------------------------------////////////
            for (int i = 0; i < ReadFile.n; i++) // calculate new p
            {
                double newp = ReadFile.Prob_List.get(i) / z;
                ReadFile.Prob_List.set(i, newp);
            }
            System.out.println("5.The Probabilities after normalization: ");
            for (int i = 0; i < ReadFile.n; i++) // calculate new p
            {
                System.out.print(ReadFile.Prob_List.get(i) + " ");
            }
            System.out.println("");

            //-------------Calculate f(x)-----------------------------
            double tempf = 0.0;

            //----------------------

            if (flag == true) {
                for (int i = 0; i <= x_pos; i++) {
                    tempf = flist.get(i) + Ct_p;
                    flist.set(i, tempf);
                }
                for (int i = x_pos + 1; i < ReadFile.n; i++) {
                    tempf = flist.get(i) + Ct_n;
                    flist.set(i, tempf);
                }
            }

            if (flag == false) {
                for (int i = 0; i <= x_pos; i++) {
                    tempf = flist.get(i) + Ct_n;
                    flist.set(i, tempf);
                }
                for (int i = x_pos + 1; i < ReadFile.n; i++) {
                    tempf = flist.get(i) + Ct_p;
                    flist.set(i, tempf);
                }
            }
           //---------------------

            System.out.println("6.The values ft(xi) for each one of the examples: ");
            for (int i = 0; i < ReadFile.n; i++) // calculate new probabilities
            {
                System.out.print(flist.get(i) + " ");

            }
            System.out.println("");
            //---------------------------Calculation Of Boosted Error Et----------------------------
            int err_count = 0;
            for (int i = 0; i < ReadFile.n; i++) {
                if (flist.get(i) > 0) {
                    if (ReadFile.Y.get(i) == -1) {
                        err_count = err_count + 1;
                    }
                } else {
                    if (ReadFile.Y.get(i) == 1) {
                        err_count = err_count + 1;
                    }
                }
            }

            double Boosted_Error = (double) err_count / (double) ReadFile.n;
            System.out.println("7.The Error of Boosted Classifier: " + Boosted_Error);
            //---------------------------Calculation Of Bound----------------
            bound = bound * z;
            System.out.println("8.The Bound on Et: " + bound);
            //----------------------------------------------------------------------------------
        }



    }
	
	

}
