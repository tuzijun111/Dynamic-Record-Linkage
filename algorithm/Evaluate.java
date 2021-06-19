package algorithm;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Evaluate {

    public static void main(String[] args) throws IOException{
        //InPairForAnyNum();
        //ArrayList<Hashtable> adj = new ArrayList<Hashtable>();
        ArrayList<Integer> temp = new ArrayList<>();
        Hashtable numbers = new Hashtable();

        Enumeration enkey = numbers.keys();
        while (enkey.hasMoreElements()) {
            Object aa = enkey.nextElement();
            temp.add((int)aa);
            System.out.println("here is key:" + aa);
        }
        System.out.println(temp);

    }
   //structure for precision, recall and F1
    static class Metric {
        double precision;
        double recall;
        double F1;
        public Metric(double precision, double recall, double F1)
        {
            this.precision = precision;
            this.recall = recall;
            this.F1 = F1;
        }
    }


    //compute Precision, Recall and F1 for music dataset
//    5 sources
//----------
//    TID: a unique record's id (in the complete dataset).
//    CID: cluster id (records having the same CID are duplicate)
//    CTID: a unique id within a cluster (if two records belong to the same cluster they will have the same CID but different CTIDs). These ids (CTID) start with 1 and grow until cluster size.
//            SourceID: identifies to which source a record belongs (there are five sources). The sources are deduplicated.
//    Id: the original id from the source. Each source has its own Id-Format. Uniqueness is not guaranteed!! (can be ignored).
//    number: track or song number in the album.
//            length: the length of the track.
//            artist: the interpreter (artist or band) of the track.
//    year: date of publication.
//            language: language of the track.

//    public static Metric PRF_Music(List<Pair> Gold, ArrayList<ArrayList<Integer>> Prediction) throws IOException {
//        ArrayList<ArrayList<Integer>> ab = new ArrayList<ArrayList<Integer>>();
//        ab = ClusterTo2DMusic(Prediction);
//        ArrayList<Integer> item = new ArrayList<Integer>();
//        List<Pair> list = new ArrayList<Pair>();
//        for (int i=0;i<ab.size();i++) {
//            Pair<Integer, Integer> B =  new Pair <Integer, Integer> (ab.get(i).get(0), ab.get(i).get(1));
//            list.add(B);
//        }
//
//        double precision, recall, F1;
//        precision = recall = 0;
//        F1 = F1(Gold, list);
//        return new Metric(precision, recall, F1);
//
//
//    }


    public static List<Pair> Goldpair(String filepath) throws IOException{
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
        data = DataPro.CsvData(filepath);

        for (int j =0; j<data.size(); j++){
            //for (int j =0; j<10; j++){
            int x1 = Integer.parseInt(data.get(j).get(0));
            int x2 = Integer.parseInt(data.get(j).get(1));
            cluster.add(new ArrayList<Integer>());
            cluster.get(cluster.size()-1).add(x1);
            cluster.get(cluster.size()-1).add(x2);
        }

//store the ground-truth in pairs
        List<Pair> list1 = new ArrayList<Pair>();
        for (int i=0;i<cluster.size();i++) {
            Pair<Integer, Integer> A =  new Pair <Integer, Integer> (cluster.get(i).get(0), cluster.get(i).get(1));
            //Pair<Integer, Integer> A =  new Pair <Integer, Integer> (1, 1);
            list1.add(A);
        }
        return list1;

    }

    //return in a arraylist which is different with Goldpair()
    public static ArrayList<ArrayList<Integer>> Goldpair1(String filepath) throws IOException{
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
        data = DataPro.CsvData(filepath);

        for (int j =0; j<data.size(); j++){
            //for (int j =0; j<10; j++){
            int x1 = Integer.parseInt(data.get(j).get(0));
            int x2 = Integer.parseInt(data.get(j).get(1));
            cluster.add(new ArrayList<Integer>());
            cluster.get(cluster.size()-1).add(x1);
            cluster.get(cluster.size()-1).add(x2);
        }
        return cluster;
    }


    //groundtruth propressing
    public static ArrayList GoldData() throws IOException{
        ArrayList<ArrayList<Integer>> data1 = new ArrayList<ArrayList<Integer>>();
        try {
            //BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/data-raw/cora_gold.csv"));
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/data-raw/cora_gold50.csv"));
            String line = null;
            int index = 0;
            while ((line = reade.readLine()) != null) {
                if(index==0)       //skip the 0-th row; id1, id2
                    index++;
                else
                {
                    ArrayList<Integer> item = new ArrayList<Integer>();
                    String label[] = line.split(";");
                    item.add(Integer.parseInt(label[0]));
                    item.add(Integer.parseInt(label[1]));
                    data1.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data1;
    }

//translate the groundtruth into pairs
    public static void IntoPairs() throws IOException{
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
        data = DataPro.CsvData("/Users/binbingu/Documents/Datasets/Music Brzinz/20K.csv");
        ArrayList<Integer> item = new ArrayList<Integer>();
        for (int i =0; i<10000; i++){
            cluster.add(new ArrayList<Integer>());
        }
        for (int j =0; j<data.size(); j++){
            //for (int j =0; j<10; j++){
            int x2 = Integer.parseInt(data.get(j).get(1));
            int x1 = Integer.parseInt(data.get(j).get(0));
            cluster.get(x2-1).add(x1);
        }


        //File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/gold.txt");
        File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair.txt");
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);

        for(int i =0; i<cluster.size(); i++) {
            if (cluster.get(i).size() != 1) {
                for (int j = 0; j < cluster.get(i).size() - 1; j++) {
                    for (int k = j + 1; k < cluster.get(i).size(); k++) {
                        pw.println(cluster.get(i).get(j) + "," + cluster.get(i).get(k));
                    }

                }
                pw.flush();
            }
        }

//        for(int i =0; i<cluster.size(); i++){
//            for(int j =0; j<cluster.get(i).size(); j++) {
//                if(j!=(cluster.get(i).size())-1)
//                    pw.print(cluster.get(i).get(j)+",");
//                else
//                    pw.println(cluster.get(i).get(j));
//            }
//            pw.flush();
//        }

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void InPairForAnyNum() throws IOException{
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Integer>> cluster = new ArrayList<ArrayList<Integer>>();
        data = DataPro.CsvData("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair.csv");

        for (int j =0; j<data.size(); j++){
            //for (int j =0; j<10; j++){
            int x1 = Integer.parseInt(data.get(j).get(0));
            int x2 = Integer.parseInt(data.get(j).get(1));
            if (x1<=15375&&x2<=15375){
                cluster.add(new ArrayList<Integer>());
                cluster.get(cluster.size()-1).add(x1);
                cluster.get(cluster.size()-1).add(x2);
            }
        }


        //File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/gold.txt");
        File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/goldpair15.3K.txt");
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);

        for(int i =0; i<cluster.size(); i++) {
            pw.println(cluster.get(i).get(0) + "," + cluster.get(i).get(1));
            pw.flush();
        }


        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static double[][] CoraBlock(ArrayList<ArrayList<String>> data)
    {
        //blocking with the number of words, partition items
        double admatrix[][] = new double[data.size()][data.size()];
        for (int i=0;i<data.size();i++){
            for (int j=0;j<data.size();j++) {
                if (j==i){
                    admatrix[i][j]=1;
                    continue;                   //we do not want to compare the same item
                }
                //try to block according to date value but failed
                /*
                if( (data.get(i).get(3)!="")& (data.get(j).get(3)!="")   ){
                    if(data.get(i).get(3)!= data.get(j).get(3)) {
                        admatrix[i][j] = 0;
                        System.out.println(admatrix[i][j]);
                        continue;
                    }
                }

                 */
                String ss1[]=data.get(i).get(0).split(",");
                String ss2[]=data.get(j).get(0).split(",");
                String ss3[]=data.get(i).get(1).split(",");
                String ss4[]=data.get(j).get(1).split(",");
                int t = Math.abs(ss1.length - ss2.length);
                int q = Math.abs(ss3.length - ss4.length);
                if (t>3 ) {        //blocking with the length of arrays
                    admatrix[i][j] = 0;
                }
                else if(ss3.length!=0& ss3.length!=0 &q>2){
                    admatrix[i][j] = 0;
                }
                else{
                    //double te = admatrix[i][j];
                    if((data.get(i).get(1)!="") & (data.get(j).get(1)!="")) {         //need to revise jaccardsim for title attribute
                        admatrix[i][j] = SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0));
                        //admatrix[i][j] = Math.max(SimFunction.Jaccardsim(data.get(i).get(0), data.get(j).get(0)), SimFunction.Jaccardsim(data.get(i).get(1), data.get(j).get(1)));
                        if(admatrix[i][j]<Cluster.Parameter.threshold)
                            admatrix[i][j]=0;
                    }
                    else {
                        //admatrix[i][j] = SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0));
                        //admatrix[i][j] =SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0));
                        if(admatrix[i][j]<Cluster.Parameter.threshold)
                            admatrix[i][j]=0;
                    }
                }
            }
        }
        return admatrix;
    }

//transform the arraylist into pairs
    public static ArrayList ClusterTo2D (ArrayList<ArrayList<Integer>> inter ){
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<inter.size(); i++){
            if (inter.get(i).size()==1)
                continue;
            else {
                for (int m = 0; m < inter.get(i).size() - 1; m++) {
                    for (int n = m + 1; n < inter.get(i).size(); n++) {
                        ArrayList<Integer> tt = new ArrayList<Integer>();
                        tt.add(inter.get(i).get(m)+1);          //because the records are labeled from 0, so we need to plus one to match the gold data
                        tt.add(inter.get(i).get(n)+1);
                        data.add(tt);
                    }
                }
            }
        }
        return data;
    }

    //the difference between ClusterTo2DMuisic and ClusterTo2D is that the records of Music dataset start from 1 not 0
    public static ArrayList ClusterTo2DMusic (ArrayList<ArrayList<Integer>> inter ){
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<inter.size(); i++){
            if (inter.get(i).size()==1)
                continue;
            else {
                for (int m = 0; m < inter.get(i).size() - 1; m++) {
                    for (int n = m + 1; n < inter.get(i).size(); n++) {
                        ArrayList<Integer> tt = new ArrayList<Integer>();
                        tt.add(inter.get(i).get(m));          //because the records are labeled from 0, so we need to plus one to match the gold data
                        tt.add(inter.get(i).get(n));
                        data.add(tt);
                    }
                }
            }
        }
        return data;
    }

    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T> List<T> nonintersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(!list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static Metric PRF(List<Pair> Gold, List<Pair> Prediction)
    {
        double f1=0;
        double precision=0;
        double recall=0;
        precision= (float)intersection(Gold, Prediction).size()/Prediction.size();
        recall= (float)intersection(Gold, Prediction).size()/Gold.size();
        f1=(float)(2*precision*recall)/(precision+recall);
        return new Metric(precision, recall, f1);
    }

    public static void Count(ArrayList<Hashtable> adj){
        int count9 = 0;
        int count85 = 0;
        int count8 = 0;
        int count75 = 0;
        int count7 = 0;
        for (int i = 0; i< adj.size() ; i++){
            for (int j = 0; j< adj.size() ; j++){
                if(adj.get(i).get(j)==null){
                    continue;
                }
                else {
                    if ((double) adj.get(i).get(j) >= 0.9) {
                        count9++;
                        continue;
                    } else {
                        if ((double) adj.get(i).get(j) >= 0.85) {
                            count85++;
                            continue;
                        } else {
                            if ((double) adj.get(i).get(j) >= 0.8) {
                                count8++;
                                continue;
                            } else {
                                if ((double) adj.get(i).get(j) >= 0.75) {
                                    count75++;
                                    continue;
                                } else {
                                    if ((double) adj.get(i).get(j) >= 0.7) {
                                        count7++;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("The number of paris whose value is larger or equal to 0.9 "+count9);
        System.out.println("The number of paris whose value is larger or equal to 0.85 "+count85);
        System.out.println("The number of paris whose value is larger or equal to 0.8 "+count8);
        System.out.println("The number of paris whose value is larger or equal to 0.75 "+count75);
        System.out.println("The number of paris whose value is larger or equal to 0.7 "+count7);
    }


}
