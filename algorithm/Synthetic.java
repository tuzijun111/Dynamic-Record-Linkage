package algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Synthetic {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /*

        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        abc= DataPro.CsvData("/Users/binbingu/Documents/Tool/febrl-0.4.2/dsgen/100.csv");
        //abc = Test.DataPro();
        //System.out.println("abc " + abc.size());

//        for (int i=0; i<abc.size(); i++)
//             System.out.println(abc.get(i));
        ArrayList<ArrayList<Integer>> source = new ArrayList<ArrayList<Integer>>();
        source.add(new ArrayList<Integer>());
        source.get(0).add(0);                     // each i corresponds to abc.get(i)
        //System.out.println(source.get(0));
        ArrayList<ArrayList<Integer>> inter = source;
        int i=0;
        int j=0;
        int k=0;
        double ad[][] = AdjMatrix.SyntheticAdj(abc);
        //double ad[][] = Test.CoraBlock(abc);


        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Adjacent-matrix1000.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/History_batch50.txt");

        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);
        for (i=0;i<ad.length;i++){
            pw.println(Arrays.toString(ad[i]));
        }
        pw.flush();

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> ttt = new ArrayList<Integer>();

        long startTime = System.currentTimeMillis();

        //Test1.Count(ad);
        //DSframe.Naive(abc, inter, ttt, ad);
        //DSframe.DBindexGreedy(inter, ttt, ad);
        //DSframe.IncreGreedy(abc, inter, ttt, ad);

        DSframe.IncreGreedyForDBindex(abc, inter, ttt, adj);


        //ArrayList<ArrayList<Integer>> ClusteringResult1 = DSframe.MLForDBindex(abc, inter, ttt, ad);

//        long startTime = System.currentTimeMillis();
//        MLbased.PythonScript("MergeModel.py");
//        MLbased.PythonScript("SplitModel.py");
//        ArrayList<ArrayList<Integer>> ClusteringResult2 = MLbased.ClusterToFile(abc, ClusteringResult1, ad);
//
//        MLbased.PythonScript("MergeModelPredict.py");
//        ArrayList<ArrayList<Integer>> ClusteringResult3 = MLbased.MergeConverge(ClusteringResult2, ad);

//
//        MLbased.ClusterToFile2(ClusteringResult3, ad);
//        System.out.println(ClusteringResult3);
//
//        MLbased.PythonScript("SplitModelPredict.py");
//
//        ArrayList<ArrayList<Integer>> ClusteringResult4 = MLbased.SplitConverge(ClusteringResult3, ad);





        long endTime = System.currentTimeMillis();
        System.out.println("Running time：" + (endTime - startTime) + "ms");
        //System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
        //System.out.println("Current DBindex: " + Cluster.DBindex(ClusteringResult3, ad));
        //System.out.println("Current DBindex: " + Cluster.DBindex(ClusteringResult1, ad));
        System.out.println("Current DBindex: "+ Cluster.DBindex(inter, ad));
        //System.out.println(ClusteringResult4);


//        int a[] = new int[ClusteringResult3.size()];
//        for (int s = 0; s < ClusteringResult3.size(); s++) {
//            a[s] = ClusteringResult3.get(s).size();
//        }
//        int xxx = 0;
//        Arrays.sort(a);
//        for (int l = a.length - 1; l >= 0; l--) {
//            System.out.print(a[l] + " ");
//            xxx = xxx + a[l];
//        }


         */

    }


}
