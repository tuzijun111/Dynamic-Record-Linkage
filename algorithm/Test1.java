package algorithm;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Test1 {
    public static void main(String[] args) throws IOException, InterruptedException {

//        String cmd = "cmd /c python /Users/binbingu/Documents/Codes/Python/MLmodel/test.py";

            // TODO Auto-generated method stub
        long startTime = System.currentTimeMillis();
            Process proc;
//            try {
                proc = Runtime.getRuntime().exec("python /Users/binbingu/Documents/Codes/Python/MLmodel/MergeModel.py");// 执行py文件

//                //用输入输出流来截取结果
//                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                String line = null;
//                while ((line = in.readLine()) != null) {
//                    System.out.println(line);
//                }
//                in.close();
//                proc.waitFor();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        long endTime = System.currentTimeMillis();
        System.out.println("Running time：" + (endTime - startTime) + "ms");



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



