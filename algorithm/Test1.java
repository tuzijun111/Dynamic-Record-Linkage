package algorithm;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Test1 {
    public static void main(String[] args) throws IOException, InterruptedException {

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
                    if (a[i][j]>= 0.85)
                    {
                        count08++;
                        continue;
                    }
                    else {
                        if(a[i][j]>= 0.8){
                            count07++;
                            continue;
                        }
                        else{
                            if(a[i][j]>=0.75){
                                count06++;
                                continue;
                            }
                            else {
                                if(a[i][j]>=0.7){
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
        System.out.println("The number of paris whose value is larger or equal to 0.85 "+count08);
        System.out.println("The number of paris whose value is larger or equal to 0.8 "+count07);
        System.out.println("The number of paris whose value is larger or equal to 0.75 "+count06);
        System.out.println("The number of paris whose value is larger or equal to 0.7 "+count05);
    }
}



