package algorithm;


import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        //abc= DataPro.Cora();
        abc = DataPro.CsvData("/Users/binbingu/Documents/Datasets/Music Brzinz/1K.csv");
        //for (int i=0; i<abc.size(); i++)
        //System.out.println(abc.get(i));
        ArrayList<ArrayList<Integer>> source = new ArrayList<ArrayList<Integer>>();
        source.add(new ArrayList<Integer>());
        source.get(0).add(0);                     // each i corresponds to abc.get(i)
        //System.out.println(source.get(0));
        ArrayList<ArrayList<Integer>> inter = source;

        //For Music
//        AdjMatrix.AttributeAndCount result = AdjMatrix.WeigthForMusic(AdjMatrix.CosineNgram());
//        ArrayList<ArrayList<String>> gramarray = result.gramarray;
//        ArrayList<ArrayList<Double>> weight = result.weight;


        //write the adjacent matrix into a txt file
//        double ad[][] = AdjMatrix.MusicAdj(gramarray, weight);
//        DataPro.WriteAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt", ad);
        //DataPro.WriteAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix1K.txt", ad);

        //read the adjacent matrix from a txt file
        //double ad[][] = DataPro.ReadAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt", 19375);  //19375 for 20K
        //double ad[][] = DataPro.ReadAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix1K.txt", abc.size());
        //DataPro.AdjToGraph("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt", ad);
//        String s1 = "/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt";
//        String s2 = "/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt";
//        DataPro.AdjToGraph1(s1, s2, 19375);
        ArrayList<Hashtable> adj = DataPro.ReadGraph("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph1K.txt");


//        for (int i=0; i<adj.size(); i++) {
//            for (int j = 0; j < adj.get(i).size(); j++) {
//                System.out.println(adj.get(i));
//            }
//        }




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



        ArrayList<Integer> ttt = new ArrayList<Integer>();

        DSframe.Naive(abc, inter, ttt, adj);
//        DSframe.DBindexGreedy(inter, ttt, adj);
//        DSframe.IncreGreedy(abc, inter, ttt, adj);

        for (int i=0; i<inter.size(); i++)
        {
            if (inter.get(i).size()>1){
                System.out.println(inter.get(i));
            }
        }

//        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
//        cluster= Evaluate.Goldpair1("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair.csv");

//        for (int i = 0; i<cluster.size(); i++) {
//            System.out.println(adj.get(cluster.get(i).get(0)-1).get(cluster.get(i).get(1)-1));
//            System.out.println(abc.get(cluster.get(i).get(0)-1));
//            System.out.println(abc.get(cluster.get(i).get(1)-1));
//        }


        List<Pair> list1= Evaluate.Goldpair("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair1K.csv");
        //System.out.println(list1);

        ArrayList<ArrayList<Integer>> cd = new ArrayList<ArrayList<Integer>>();
        cd=Evaluate.ClusterTo2D(inter);           // pair for clustering results
        List<Pair> list2 = new ArrayList<Pair>();
        for (int i=0;i<cd.size();i++) {
            Pair<Integer, Integer> B =  new Pair <Integer, Integer> (cd.get(i).get(0), cd.get(i).get(1));
            list2.add(B);
        }
        System.out.println("F1 is: "+ Evaluate.F1(list1, list2));

        long endTime = System.currentTimeMillis();
        System.out.println("Running time：" + (endTime - startTime) + "ms");
        //System.out.println("Current DBindex: "+ Cluster.DBindex(inter, ad));



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




    }



}





