package algorithm;

import java.io.*;
import java.util.*;


public class Test1 {
    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>();
        test = Synthetic.DataPro10000();
//        for (int i = 0; i<test.size(); i++)
//            System.out.println(test.get(i));

        double admatrix[][] = new double[test.size()][test.size()];
        admatrix = Synthetic.SyntheticAdj(test);
        for (int i = 0; i<admatrix.length; i++)
            System.out.println(Arrays.toString(admatrix[i]));

        Count(admatrix);
    }

    public static void Count(double a[][]){
        int k = a.length;
        int count09 = 0;
        int count08 = 0;
        int count07 = 0;
        int count06 = 0;
        int count05 = 0;
        for (int i = 0; i< k ; i++){
            for (int j = i+1; j< k ; j++){
                if(a[i][j]>=0.9){
                     count09++;
                     continue;
                }
                else {
                    if (a[i][j]>= 0.8)
                    {
                        count08++;
                        continue;
                    }
                    else {
                        if(a[i][j]>= 0.7){
                            count07++;
                            continue;
                        }
                        else{
                            if(a[i][j]>=0.6){
                                count06++;
                                continue;
                            }
                            else {
                                if(a[i][j]>=0.5){
                                    count05++;
                                    continue;
                                }
                            }
                        }
                    }

                }
            }
        }
        System.out.println("The number of paris whose value is larger or equal to 0.9 "+count09);
        System.out.println("The number of paris whose value is larger or equal to 0.8 "+count08);
        System.out.println("The number of paris whose value is larger or equal to 0.7 "+count07);
        System.out.println("The number of paris whose value is larger or equal to 0.6 "+count06);
        System.out.println("The number of paris whose value is larger or equal to 0.5 "+count05);
    }
}



