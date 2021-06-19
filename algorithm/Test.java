package algorithm;


import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        //abc= DataPro.Cora();
        //abc = DataPro.CsvData("/Users/binbingu/Documents/Datasets/Music Brzinz/15.3K.csv");
        abc = DataPro.CsvData("/Users/binbingu/Documents/Datasets/amazon-samples/amazon100.csv");
//        for (int i=0; i<abc.size(); i++)
//            System.out.println(abc.get(i));
        int []array ={1};
        int minDis = Cluster.Parameter.minDis;
        double ad[][] = AdjMatrix.EuclidAdj_DB(abc, array, minDis);
//        for (int i = 0; i< ad.length; i++)
//            System.out.println(Arrays.toString(ad[i]));
//        System.out.println(ad.length);
//        System.out.println(ad[0].length);

        //Experiment.DBSCAN_Pro(abc, ad);       //data preprocessing (i.e. building a similarity or distance graph) for DBSCAN
        //Experiment.Music_Pro(abc);       //data preprocessing (i.e. building a similarity or distance graph) for Music


        ArrayList<ArrayList<Integer>> source = new ArrayList<ArrayList<Integer>>();
        source.add(new ArrayList<Integer>());
        source.get(0).add(0);                     // each i corresponds to abc.get(i)
        //System.out.println(source.get(0));
        ArrayList<ArrayList<Integer>> inter = source;



        //ArrayList<Hashtable> adj1 = DataPro.ReadGraph("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt");
        ArrayList<Hashtable> adj = DataPro.ReadGraph("/Users/binbingu/Documents/Datasets/amazon-samples/Matrix/AdjGraph100.txt");
//        System.out.println(adj.get(0).get(0));
//        System.out.println(adj.get(0).get(1));
//        Evaluate.Count(adj);
        //ArrayList<Hashtable> adj = new ArrayList<Hashtable>();

        int sy = abc.size();
        for (int i=0; i<sy; i++) {
            System.out.println(adj.get(i));
        }
        ArrayList<ArrayList<Integer>> cluster = Cluster.static_DBSCAN(adj);
        for (int s = 0 ; s < cluster.size(); s++)
            System.out.println(cluster.get(s));


/*
//        long endTime = System.currentTimeMillis();
//        System.out.println("Running time：" + (endTime - startTime) + "ms");


//        double ad[][] = Evaluate.CoraBlock(abc);

//        for (int i=0;i<ad.length;i++) {
//            System.out.println(Arrays.toString(ad[i]));
//        }


//        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Adjacent-matrix500.txt");
//        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch50.txt");
//
//        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
//            file.delete();
//        }
//        file.createNewFile();
//        FileWriter xw = null;
//        xw = new FileWriter(file, true);
//        PrintWriter pw = new PrintWriter(xw);
//        for (i=0;i<ad.length;i++){
//            pw.println(Arrays.toString(ad[i]));
//        }
//        pw.flush();
//
//        try {
//            xw.flush();
//            pw.close();
//            xw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        long startTime = System.currentTimeMillis();

        ArrayList<Integer> ttt = new ArrayList<Integer>();

        DSframe.Naive(abc, inter, ttt, adj);
//        DSframe.DBindexGreedy(inter, ttt, adj);
//        DSframe.IncreGreedy(abc, inter, ttt, adj);


        long endTime = System.currentTimeMillis();
        System.out.println("Running time：" + (endTime - startTime) + "ms");


//        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
//        cluster= Evaluate.Goldpair1("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair.csv");
//
//        for (int i = 0; i<cluster.size(); i++) {
//            System.out.println(adj.get(cluster.get(i).get(0)-1).get(cluster.get(i).get(1)-1));
//            System.out.println(abc.get(cluster.get(i).get(0)-1));
//            System.out.println(abc.get(cluster.get(i).get(1)-1));
//        }


//        List<Pair> list1= Evaluate.Goldpair("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair4K.csv");
//        //System.out.println(list1);
//
//        ArrayList<ArrayList<Integer>> cd = new ArrayList<ArrayList<Integer>>();
//        cd=Evaluate.ClusterTo2D(inter);           // pair for clustering results
//        List<Pair> list2 = new ArrayList<Pair>();
//        for (int i=0;i<cd.size();i++) {
//            Pair<Integer, Integer> B =  new Pair <Integer, Integer> (cd.get(i).get(0), cd.get(i).get(1));
//            list2.add(B);
//        }
//        Evaluate.Metric result = Evaluate.PRF(list1, list2);
//        System.out.println("Precision is: "+ result.precision);
//        System.out.println("Recall is: "+ result.recall);
//        System.out.println("F1 is: "+ result.F1);

        System.out.println("Current DBindex: "+ Cluster.DBindex(inter, adj));



//        System.out.println(Evaluate.intersection(list1, list2));
//
//        System.out.println(Evaluate.nonintersection(list1, list2));



//        long startTime2 = System.currentTimeMillis();
//
//        ArrayList<ArrayList<Integer>> ab = new ArrayList<ArrayList<Integer>>();
//        //ab= Evaluate.GoldData();                  //pair for groundtruth (Cora dataset)
//        List<Pair> list1 = new ArrayList<Pair>();
//        for (int i=0;i<ab.size();i++) {
//            Pair<Integer, Integer> A =  new Pair <Integer, Integer> (ab.get(i).get(0), ab.get(i).get(1));
//            //Pair<Integer, Integer> A =  new Pair <Integer, Integer> (1, 1);
//            list1.add(A);
//        }
//
//        ArrayList<ArrayList<Integer>> cd = new ArrayList<ArrayList<Integer>>();
//        cd=ClusterTo2D(inter);           // pair for clustering results
//        List<Pair> list2 = new ArrayList<Pair>();
//        for (i=0;i<cd.size();i++) {
//            Pair<Integer, Integer> B =  new Pair <Integer, Integer> (cd.get(i).get(0), cd.get(i).get(1));
//            //Pair<Integer, Integer> B =  new Pair <Integer, Integer> (1, 1);
//            list2.add(B);
//        }
//        //System.out.println("Ground Truth:");
//            //System.out.println(list1);
//
//        //System.out.println("Prediction:");
//            //System.out.println(list2);
//
//        long endTime2 = System.currentTimeMillis();
//        System.out.println("Running time：" + (endTime2 - startTime2) + "ms");
//        System.out.println("F1 is: "+ F1(list1, list2));
*/



    }



}





