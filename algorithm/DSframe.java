package algorithm;

import java.io.*;
import java.util.*;


public class DSframe {
    public static void main(String[] args) {

    }

    public static void Naive(ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, ArrayList<Hashtable> adj) throws IOException {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Change_Batch1000.txt");

        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        for (int i=1;i<abc.size();i++){
            ttt.add(i);
            inter = Cluster.OneByOne(inter, ttt, Cluster.Parameter.threshold, adj);
            ttt.clear();
        }
        //System.out.println("Naive------------------------"+inter);
        System.out.println("Naive------------------------");

    }

//    public static void Naive(ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException {
//        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Change_Batch1000.txt");
//
//        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
//            file.delete();
//        }
//        file.createNewFile();
//        for (int i=1;i<abc.size();i++){
//            ttt.add(i);
//            inter = Cluster.OneByOne(inter, ttt, Cluster.Parameter.threshold, ad);
//            ttt.clear();
//        }
//        //System.out.println("Naive------------------------"+inter);
//        System.out.println("Naive------------------------");
//
//    }

    public static void DBindexGreedy (ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, ArrayList<Hashtable> adj) throws IOException {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Batch/Change_Batch1000.txt");
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);
        int merge=0;
        int split=0;
        int move=0;
        pw.println("Begin from the Naive method: ");
        pw.flush();
        double temp[]= new double[10];
        Arrays.fill(temp,0);
        for (int i=0;i<3;i++) {
            for (int j = 0; j < inter.size(); j++) {
                Cluster.ClusterData result= Cluster.DBGreedy(inter, j, adj);
                inter = result.inter;
                move = move + result.move;
            }
            temp[i] = Cluster.DBindex(inter, adj);
            if (i>0 && temp[i]== temp[i-1]) {
                System.out.println("The~"+ (i+1)+"th~" +"iteration converges");
                break;
            }
        }
        System.out.println("DBindexGreedy------------------------"+inter);
        System.out.println("The Number of Move= " + move);
        pw.println("The Number of Move for Batch Greedy = " + move);
        pw.flush();

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void IncreGreedy (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, ArrayList<Hashtable> adj) throws IOException, ClassNotFoundException {
        /*
        for (i=1;i<11;i++){
            ttt.add(i);
            inter = Cluster.IncrementalDB(inter, ttt, Experiment.Parameter.threshold, ad);
            ttt.clear();
        }
         */
        //also very costly, one way is to revise IsConnected Function and using more progressive blocking techniques
        //for (i=1;i<200;i++){

        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Incre.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch2.txt");
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/History_batch2000.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/History_batch5000.txt");

        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        FileWriter xw = null;
        xw = new FileWriter(file, true);

        PrintWriter pw = new PrintWriter(xw);
        int merge = 0;
        int split = 0;
        int loop = 0;
        int update = 0;  //record the number of updates
        ArrayList<ArrayList<Integer>> precluster = new ArrayList<ArrayList<Integer>>();
        for (int i=1;i<abc.size();i++){
            ttt.add(i);   //record linkage with batch size =1      that means the incremental way may be not polynomial
            //System.out.println("The~"+ i+ "-th round:");                                maybe as complex as stated in the paper which is n^6
            if((i%500==0)|| i==abc.size()-1){
                update++;
                //Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);   //this method is quite costly when we consider each record as a snapshot
                Cluster.ClusterData result = Cluster.IncreForCorr(inter, ttt, Cluster.Parameter.threshold, adj);
                inter = result.inter;
                merge = merge + result.merge;
                split = split + result.split;
                loop = loop + result.loop;
                ttt.clear();    //remove it as the batch size =1

                System.out.println("Update = " + update);

                if (update >1){
                    //analyse the changes happened on previous clusters
                    System.out.println("Iteration " + update);
                    pw.println("Iteration " + update);
                    pw.println("New Cluster Results " + inter);
                    pw.println();
                    ArrayList<ArrayList<Integer>> c1 = new ArrayList<ArrayList<Integer>>();
                    for (int m = 0; m < precluster.size(); m++) {
                        for (int n = 0; n < inter.size(); n++){
                            if (Exist(precluster.get(m), inter.get(n))) {
                                Integer temp1[] = new Integer[precluster.get(m).size()];
                                Integer temp2[] = new Integer[inter.get(n).size()];
                                for (int l = 0; l<temp1.length; l++){
                                    temp1[l] = precluster.get(m).get(l);
                                    //System.out.println("temp1[l])=  " + temp1[l]);
                                }
                                for (int l = 0; l<temp2.length; l++){
                                    temp2[l] = inter.get(n).get(l);
                                }
                                //compute intersection
                                Integer[] w = getJ(temp1, temp2);
                                ArrayList<Integer> temp = new ArrayList<Integer>();
                                for (int k = 0; k<w.length; k++)
                                {
                                    temp.add(w[k]);
                                }
                                c1.add(temp);
                                //break labe1B;
                            }
                        }
                    }

                    System.out.println("Previous Cluster Results have changed into " + c1);
                    System.out.println("New Cluster Results " + inter);
                    //System.out.println("Current Score: "+ Cluster.DBindex(inter, ad));
                    System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, adj));
                    pw.println("Previous Cluster Results have changed into " + c1);
                    //compute the intesection between c1 and previous cluster, i.e. the changes between previous clusters and the latter
                    // clusters when new records are added
                    //According to our observation, there seems only "split" scenarios. No merge will happen among previous clusters e.g. (0-200) new records(201-400)
                    pw.println();
                    pw.println("Changes happened among the records in previous clusters");
                    for (int m = 0; m < precluster.size(); m++) {
                        for (int n = 0; n < c1.size(); n++){
                            if (Exist(precluster.get(m), c1.get(n))) {
                                Integer temp3[] = new Integer[precluster.get(m).size()];
                                Integer temp4[] = new Integer[c1.get(n).size()];
                                for (int l = 0; l < temp3.length; l++) {
                                    temp3[l] = precluster.get(m).get(l);
                                }
                                for (int l = 0; l < temp4.length; l++) {
                                    temp4[l] = c1.get(n).get(l);
                                }
                                //compute intersection
                                Integer[] w = getJ(temp3, temp4);
                                if (w.length != precluster.get(m).size() ){
                                    pw.println(precluster.get(m)+ " Changes into "+ c1.get(n));
                                }
                            }
                        }
                    }
                    // This following codes compute the intesection between inter (i.e. new clusters) and previous cluster, and we will find some merge scenarios due to
                    // the new records added.
                    pw.println();
                    pw.println("Changes happened among all the records including the new records added");
                    for (int m = 0; m < precluster.size(); m++) {
                        for (int n = 0; n < inter.size(); n++){
                            if (Exist(precluster.get(m), inter.get(n))) {
                                Integer temp5[] = new Integer[precluster.get(m).size()];
                                Integer temp6[] = new Integer[inter.get(n).size()];
                                for (int l = 0; l < temp5.length; l++) {
                                    temp5[l] = precluster.get(m).get(l);
                                }
                                for (int l = 0; l < temp6.length; l++) {
                                    temp6[l] = inter.get(n).get(l);
                                }
                                //compute intersection
                                Integer[] x = getJ(temp5, temp6);
                                if (x.length != precluster.get(m).size() ){
                                    pw.println(precluster.get(m)+ " Changes into "+ inter.get(n));
                                }
                            }
                        }
                    }


                    pw.println();
//                    pw.println("New Cluster Results " + inter);
//                    pw.println();
                    c1.removeAll(c1);
                    precluster = (ArrayList<ArrayList<Integer>>) deepCopy(inter);
                    for (int k =0; k< inter.size(); k++){
                        System.out.println(" Intra_Sim = "+Cluster.Intra_Cluster(inter.get(k), adj));
                        if(Cluster.Intra_Cluster(inter.get(k), adj)<0) {
                            for (int s1 = 0; s1 < inter.get(k).size(); s1++) {
                                for (int s2 = 0; s2 < inter.get(k).size(); s2++) {
                                    System.out.println(" SimAd = " + adj.get(inter.get(k).get(s1)).get(inter.get(k).get(s2)));
                                }
                            }
                        }
                    }

                }
                else {
                    precluster = (ArrayList<ArrayList<Integer>>) deepCopy(inter);
                    System.out.println(precluster);
//                    for (int s = 0; s<inter.size(); i++ ){
//                        precluster.add(new ArrayList<Integer>());
//                        precluster.get(s) = deepCopy(inter.get(s));
//                    }

                    System.out.println("Iteration " + update);
                    System.out.println("New Cluster Results " + inter);
                    //System.out.println("Current Score: "+ Cluster.DBindex(inter, ad));
                    System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, adj));
//                    for(i=0; i<inter.size(); i++) {
//                        System.out.println(Cluster.IntraForCorr(inter.get(i), ad));
//                    }
//                    for(i=0; i<inter.size()-1; i++) {
//                        for(int j=i+1; j<inter.size(); j++) {
//                            System.out.println(Cluster.InterForCorr(inter.get(i), inter.get(j), ad));
//                        }
//                    }
                    pw.println("Iteration " + update);
                    pw.println("New Cluster Results " + inter);
                    pw.println();
                }
            }

        }
        // Analyze the parameters of each node
        // Avarage similarity with all records
        // Avarage similarity with records in the same cluster
        // The size of the cluster that the record belong to
        // Avarage similarity of clusters
        //Output: 1 or 0   1 means staying in the previous cluster, 0 means leaving from previous cluster

        int a[] = new int [inter.size()];
        for (int k =0; k< inter.size(); k++){
            a[k] = inter.get(k).size();
        }
        Arrays.sort(a);
        for (int l =a.length-1; l>= 0; l--){
            System.out.print(a[l]+" ");
        }
        System.out.println(" ");
        System.out.println("The Number of Merge= " + merge);
        System.out.println("The Number of Split= " + split);
        System.out.println("The Number of Loop= " + loop);
        //pw.println("The Number of Merge= " + merge);
        //pw.println("The Number of Split= " + split);
        pw.flush();

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void IncreGreedyForDBindex (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, ArrayList<Hashtable> adj) throws IOException, ClassNotFoundException {
            //also very costly, one way is to revise IsConnected Function and using more progressive blocking techniques
        //for (i=1;i<200;i++){
        File file11 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Merge1000.txt");
        File file12 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Split1000.txt");
        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra.txt");
        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter.txt");
        File file4 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change.txt");
        File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra_S.txt");
        File file6 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter_S.txt");
        File file7 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change_S.txt");
        File file8 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size1.txt");
        File file9 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size2.txt");
        File file10 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size1_S.txt");
        File file111 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/DBindexHistroy.txt");
        if (file11.exists()) { file11.delete(); }
        if (file12.exists()) { file12.delete(); }
        if (file2.exists()) { file2.delete(); }
        if (file3.exists()) { file3.delete(); }
        if (file4.exists()) { file4.delete(); }
        if (file5.exists()) { file5.delete(); }
        if (file6.exists()) { file6.delete(); }
        if (file7.exists()) { file7.delete(); }
        if (file8.exists()) { file8.delete(); }
        if (file9.exists()) { file9.delete(); }
        if (file10.exists()) { file10.delete(); }
        if (file111.exists()) { file111.delete(); }


        file11.createNewFile();
        file12.createNewFile();
        file2.createNewFile(); file3.createNewFile(); file4.createNewFile();
        file5.createNewFile(); file6.createNewFile(); file7.createNewFile();
        file8.createNewFile(); file9.createNewFile();
        file10.createNewFile();
        file111.createNewFile();

        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Incre.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch2.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DBHistory_batch1000.txt");
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DBHistory_batch1000.txt");


        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        FileWriter xw = null;
        xw = new FileWriter(file, true);

        PrintWriter pw = new PrintWriter(xw);
        int merge = 0;
        int split = 0;
        int move = 0;
        int loop = 0;
        int update = 0;  //record the number of updates
        ArrayList<ArrayList<Integer>> precluster = new ArrayList<ArrayList<Integer>>();
        for (int i=1;i<201;i++){
            ttt.add(i);   //record linkage with batch size =1      that means the incremental way may be not polynomial
            //System.out.println("The~"+ i+ "-th round:");                                maybe as complex as stated in the paper which is n^6
            if((i==200)){
            //if((i==678) || (i==878 || (i==1078)|| (i==1278)||(i==1478)||(i==1678)||(i==1878))){
                //if((i==678) || (i==878) || (i==1078)){
                update++;
                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, adj);   //this method is quite costly when we consider each record as a snapshot
                //Cluster.ClusterData result = Cluster.IncreForCorr(inter, ttt, Cluster.Parameter.threshold, ad);
                inter = result.inter;
                merge = merge + result.merge;
                split = split + result.split;
                move = move + result.move;
                loop = loop + result.loop;
                ttt.clear();    //remove it as the batch size =1

                System.out.println("Update = " + update);

//                if (update >1){
//                    //analyse the changes happened on previous clusters
//                    System.out.println("Iteration " + update);
//                    pw.println("Iteration " + update);
//                    pw.println("New Cluster Results " + inter);
//                    pw.println();
//                    ArrayList<ArrayList<Integer>> c1 = new ArrayList<ArrayList<Integer>>();
//                    for (int m = 0; m < precluster.size(); m++) {
//                        for (int n = 0; n < inter.size(); n++){
//                            if (Exist(precluster.get(m), inter.get(n))) {
//                                Integer temp1[] = new Integer[precluster.get(m).size()];
//                                Integer temp2[] = new Integer[inter.get(n).size()];
//                                for (int l = 0; l<temp1.length; l++){
//                                    temp1[l] = precluster.get(m).get(l);
//                                    //System.out.println("temp1[l])=  " + temp1[l]);
//                                }
//                                for (int l = 0; l<temp2.length; l++){
//                                    temp2[l] = inter.get(n).get(l);
//                                }
//                                //compute intersection
//                                Integer[] w = getJ(temp1, temp2);
//                                ArrayList<Integer> temp = new ArrayList<Integer>();
//                                for (int k = 0; k<w.length; k++)
//                                {
//                                    temp.add(w[k]);
//                                }
//                                c1.add(temp);
//                                //break labe1B;
//                            }
//                        }
//                    }
//
//                    System.out.println("Previous Cluster Results have changed into " + c1);
//                    System.out.println("New Cluster Results " + inter);
//                    System.out.println("Current Score: "+ Cluster.DBindex(inter, ad));
//                    //System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
//                    pw.println("Previous Cluster Results have changed into " + c1);
//
//                    //compute the intesection between c1 and previous cluster
//                    pw.println();
//                    pw.println("Changes happened among all the records including the new records added");
//                    for (int m = 0; m < precluster.size(); m++) {
//                        for (int n = 0; n < c1.size(); n++){
//                            if (Exist(precluster.get(m), c1.get(n))) {
//                                Integer temp3[] = new Integer[precluster.get(m).size()];
//                                Integer temp4[] = new Integer[c1.get(n).size()];
//                                for (int l = 0; l < temp3.length; l++) {
//                                    temp3[l] = precluster.get(m).get(l);
//                                }
//                                for (int l = 0; l < temp4.length; l++) {
//                                    temp4[l] = c1.get(n).get(l);
//                                }
//                                //compute intersection
//                                Integer[] w = getJ(temp3, temp4);
//                                if (w.length != precluster.get(m).size() ){
//                                    pw.println(precluster.get(m)+ " Changes into "+ c1.get(n));
//                                }
//                            }
//                        }
//                    }
//
//
//                    // This following codes compute the intesection between inter (i.e. new clusters) and previous cluster, and we will find some merge scenarios due to
//                    // the new records added.
//                    pw.println();
//                    pw.println("Changes happened among all the records including the new records added");
//                    for (int m = 0; m < precluster.size(); m++) {
//                        for (int n = 0; n < inter.size(); n++){
//                            if (Exist(precluster.get(m), inter.get(n))) {
//                                Integer temp5[] = new Integer[precluster.get(m).size()];
//                                Integer temp6[] = new Integer[inter.get(n).size()];
//                                for (int l = 0; l < temp5.length; l++) {
//                                    temp5[l] = precluster.get(m).get(l);
//                                }
//                                for (int l = 0; l < temp6.length; l++) {
//                                    temp6[l] = inter.get(n).get(l);
//                                }
//                                //compute intersection
//                                Integer[] x = getJ(temp5, temp6);
//                                if (x.length != precluster.get(m).size() ){
//                                    pw.println(precluster.get(m)+ " Changes into "+ inter.get(n));
//                                }
//                            }
//                        }
//                    }
//
//
//                    pw.println();
////                    pw.println("New Cluster Results " + inter);
////                    pw.println();
//                    c1.removeAll(c1);
//                    precluster = (ArrayList<ArrayList<Integer>>) deepCopy(inter);
////                    for (int k =0; k< inter.size(); k++){
////                        System.out.println(" Intra_Sim = "+Cluster.Intra_Cluster(inter.get(k), ad));
////                        if(Cluster.Intra_Cluster(inter.get(k), ad)<0) {
////                            for (int s1 = 0; s1 < inter.get(k).size(); s1++) {
////                                for (int s2 = 0; s2 < inter.get(k).size(); s2++) {
////                                    System.out.println(" SimAd = " + ad[inter.get(k).get(s1)][inter.get(k).get(s2)]);
////                                }
////                            }
////                        }
////                    }
//
//                }
//                else {
//                    precluster = (ArrayList<ArrayList<Integer>>) deepCopy(inter);
//                    System.out.println(precluster);
////                    for (int s = 0; s<inter.size(); i++ ){
////                        precluster.add(new ArrayList<Integer>());
////                        precluster.get(s) = deepCopy(inter.get(s));
////                    }
//
//                    System.out.println("Iteration " + update);
//                    System.out.println("New Cluster Results " + inter);
//                    System.out.println("Current Score: "+ Cluster.DBindex(inter, ad));
//                    //System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
////                    for(i=0; i<inter.size(); i++) {
////                        System.out.println(Cluster.IntraForCorr(inter.get(i), ad));
////                    }
////                    for(i=0; i<inter.size()-1; i++) {
////                        for(int j=i+1; j<inter.size(); j++) {
////                            System.out.println(Cluster.InterForCorr(inter.get(i), inter.get(j), ad));
////                        }
////                    }
//                    pw.println("Iteration " + update);
//                    pw.println("New Cluster Results " + inter);
//                    pw.println();
//                }

            }

        }

//        for (int i=1;i<400;i++) {
//            ttt.add(i);   //record linkage with batch size =1      that means the incremental way may be not polynomial
//            //System.out.println("The~"+ i+ "-th round:");                                maybe as complex as stated in the paper which is n^6
//            if ((i == 199)||(i==399)) {
//                //if((i==678) || (i==878) || (i==1078)){
//                update++;
//                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);   //this method is quite costly when we consider each record as a snapshot
//                //Cluster.ClusterData result = Cluster.IncreForCorr(inter, ttt, Cluster.Parameter.threshold, ad);
//                inter = result.inter;
//                merge = merge + result.merge;
//                split = split + result.split;
//                move = move + result.move;
//                loop = loop + result.loop;
//                ttt.clear();    //remove it as the batch size =1
//            }
//        }


                // Analyze the parameters of each node
        // Avarage similarity with all records
        // Avarage similarity with records in the same cluster
        // The size of the cluster that the record belong to
        // Avarage similarity of clusters
        //Output: 1 or 0   1 means staying in the previous cluster, 0 means leaving from previous cluster



        int a[] = new int [inter.size()];
        for (int k =0; k< inter.size(); k++){
            a[k] = inter.get(k).size();
        }
        int xxx= 0;
        Arrays.sort(a);
        for (int l =a.length-1; l>= 0; l--){
            System.out.print(a[l]+" ");
            xxx = xxx + a[l];
        }
        System.out.println();
        System.out.println("Total number of records: "+ xxx);
        System.out.println(" ");
        System.out.println("The Number of Merge= " + merge);
        System.out.println("The Number of Split= " + split);
        System.out.println("The Number of Move= " + move);
        System.out.println("The Number of Loop= " + loop);
        //pw.println("The Number of Merge= " + merge);
        //pw.println("The Number of Split= " + split);
        pw.flush();

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean Exist(ArrayList<Integer> a, ArrayList<Integer> b){
        boolean s = false;
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                int k1 = b.get(j);
                int k2 = a.get(i);
                if (k1 == k2) {
                    s = true;
                    break;
                }
            }

        }
        return s;

    }

    private static Integer[] getJ(Integer[] m, Integer[] n) {
        List<Integer> rs = new ArrayList<Integer>();
        // translate longer array into set
        Set<Integer> set = new HashSet<Integer>(Arrays.asList(m.length > n.length ? m : n));
        // search shorter array, least number of loops
        for (Integer i : m.length > n.length ? n : m) {
            if (set.contains(i)) {
                rs.add(i);
            }
        }
        Integer[] arr = {};
        return rs.toArray(arr);
    }

    public static ArrayList<ArrayList<Integer>> deepCopy(ArrayList<ArrayList<Integer>> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        ArrayList<ArrayList<Integer>> dest = (ArrayList<ArrayList<Integer>>) in.readObject();
        return dest;
    }

    public static ArrayList<ArrayList<Integer>> MLForDBindex (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter, ArrayList<Integer> ttt, ArrayList<Hashtable> adj) throws IOException, ClassNotFoundException {

        File file11 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Merge1000.txt");
        File file12 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Split1000.txt");
        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra.txt");
        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter.txt");
        File file4 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change.txt");
        File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Intra_S.txt");
        File file6 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Inter_S.txt");
        File file7 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/change_S.txt");
        File file8 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size1.txt");
        File file9 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size2.txt");
        File file10 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/train/Size1_S.txt");
        if (file11.exists()) { file11.delete(); }
        if (file12.exists()) { file12.delete(); }
        if (file2.exists()) { file2.delete(); }
        if (file3.exists()) { file3.delete(); }
        if (file4.exists()) { file4.delete(); }
        if (file5.exists()) { file5.delete(); }
        if (file6.exists()) { file6.delete(); }
        if (file7.exists()) { file7.delete(); }
        if (file8.exists()) { file8.delete(); }
        if (file9.exists()) { file9.delete(); }
        if (file10.exists()) { file10.delete(); }


        file11.createNewFile();
        file12.createNewFile();
        file2.createNewFile(); file3.createNewFile(); file4.createNewFile();
        file5.createNewFile(); file6.createNewFile(); file7.createNewFile();
        file8.createNewFile(); file9.createNewFile();
        file10.createNewFile();

        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Incre.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch2.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DBHistory_batch1000.txt");
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/ML1000.txt");


        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);

        int merge = 0;
        int split = 0;
        int loop = 0;
        int update = 0;  //record the number of updates
        ArrayList<ArrayList<Integer>> precluster = new ArrayList<ArrayList<Integer>>();
        for (int i=200;i<1679;i++){
            ttt.add(i);   //record linkage with batch size =1      that means the incremental way may be not polynomial
            //address the top 800 records first
            // the time cost is not always less when the batch size is larger, this is because each record is taken as a single cluster initially. This will lead to high time cost for incrementalDB
            if((i==1678)){
                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, adj);   //this method is quite costly when we consider each record as a snapshot
                //Cluster.ClusterData result = Cluster.IncreForCorr(inter, ttt, Cluster.Parameter.threshold, ad);
                inter = result.inter;
                merge = merge + result.merge;
                split = split + result.split;
                loop = loop + result.loop;
                ttt.clear();    //remove it as the batch size =1
            }

//            if(i==abc.size()-1){
////                MLbased.Model(inter, ttt, Cluster.Parameter.threshold, ad);
//                Cluster.ClusterData result1 = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);
//                int loop1 = result1.loop;
//                System.out.println("Loop for new records: " + loop1);
//                ttt.clear();
//            }

        }
        //output the paramters of inter and then use ML model to predict

        System.out.println(inter.size());
        int a[] = new int [inter.size()];
        for (int k =0; k< inter.size(); k++){
            a[k] = inter.get(k).size();
        }
        Arrays.sort(a);
        for (int l =a.length-1; l>= 0; l--){
            System.out.print(a[l]+" ");
        }
        System.out.println();
        pw.flush();

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The Number of Merge= " + merge);
        System.out.println("The Number of Split= " + split);
        System.out.println("The Number of Loop= " + loop);
        return inter;
    }


    //initial version

//    public static void main(String[] args) {
//        String fileName1= "/Users/binbingu/Documents/Datasets/DySM/source1.txt";
//        String fileName2= "/Users/binbingu/Documents/Datasets/DySM/source2.txt";
//        String fileName3= "/Users/binbingu/Documents/Datasets/DySM/source3.txt";
//        String fileName4= "/Users/binbingu/Documents/Datasets/DySM/source4.txt";
//        String fileName5= "/Users/binbingu/Documents/Datasets/DySM/source5.txt";
//        String fileName6= "/Users/binbingu/Documents/Datasets/DySM/source6.txt";
//        String fileName7= "/Users/binbingu/Documents/Datasets/DySM/source7.txt";
//        String fileName8= "/Users/binbingu/Documents/Datasets/DySM/source8.txt";
//        String fileName = "/Users/binbingu/Documents/Datasets/DySM/source.txt";
//
//        int i=0;
//        int j=0;
//        int k=0;
//
//
//        ArrayList<ArrayList<String>> source1 = SaveToArray.SaveArrayList(fileName1);
//        ArrayList <String> source2  = SaveToArray.SaveArray(fileName2);
//        ArrayList <String> source3  = SaveToArray.SaveArray(fileName3);
//        ArrayList <String> source4  = SaveToArray.SaveArray(fileName4);
//        ArrayList <String> source5  = SaveToArray.SaveArray(fileName5);
//        ArrayList <String> source6  = SaveToArray.SaveArray(fileName6);
//        ArrayList <String> source7  = SaveToArray.SaveArray(fileName7);
//        ArrayList <String> source8  = SaveToArray.SaveArray(fileName8);
//        ArrayList<String> Source = SaveToArray.SaveArray(fileName); //to do hashmap
//
//
//        ArrayList <ArrayList <String>> source= new ArrayList<ArrayList<String>>();
//
//        source.add(source3);
//        source.add(source4);
//        source.add(source5);
//        source.add(source6);
//        source.add(source7);
//        source.add(source8);
//
//        //Vector<Vector<String>> Source = SaveToArray.SaveVector(fileName1);
//        HashMap<String, Integer> map =new HashMap<>();
//        for (i=0;i<Source.size();i++){
//            map.put( Source.get(i), i);
//        }
//
//        System.out.println("The~"+ 1+ "-th round:");
//        //Matrix(source1, source2, threshold);
//        //System.out.println("The~"+ 2+ "-th round:");
//        //Matrix(Matrix(source1, source2, threshold), source3, threshold);
//
//
//
//    }

    //获取数组的长度，因为该数组存储的是数据库中的文件，所以我们一开始并不知道其长度
//    public static int VLarray(ArrayList<String> array){
//        int s =0;
//        int k= 0;
//        for (k=0; k< array.size(); k++) {
//            if (array.get(k) != null) {
//                s=k+1;
//            }
//            else {
//                //System.out.println("s"+ + s);
//                break;
//            }
//        }
//        return s;
//    }
//
//    public static int VLvector(ArrayList<ArrayList<String>> vector){
//        int s =0;
//        int k= 0;
//        for (k=0; k< vector.size(); k++) {
//            if (vector.get(k) != null) {
//                s=k+1;
//            }
//            else {
//                //System.out.println("s"+ + s);
//                break;
//            }
//        }
//        return s;
//    }


    /*
    public static ArrayList<ArrayList<String>> CenterCluster(ArrayList<ArrayList<String>> vector, ArrayList<String> array, double k) {
        //k是阈值
        //该算法检测初始分类中的每个点是否符合到该类中剩余点的平均距离小于阈值，若不小于，则拿出来作为自由点重新加入计算
        //该方法似乎不一定能converge
        int m= Experiment.VLvector(vector);
        int n = Experiment.VLarray(array);
        int i=0;
        int j=0;
        int p=0;
        int q2=0;
        int q3=0;
        int q4=0;
        ArrayList<String> label= new ArrayList<String>();
        label=new ArrayList<String>(array);
        double matrix[] [] = new double [m][n]; //vector和array的平均相似度矩阵
        for (i=0; i< m; i++) {      //两两比较相似度
            for (j=0; j< n; j++) {
                double sum= 0;
                for (p = 0; p < vector.get(i).size(); p++) {
                    sum = sum + SimFunction.Jaccardsim(vector.get(i).get(p), array.get(j));
                    //SimFunction t = new SimFunction(source1[j], source2[k]);
                    //System.out.println("Cosinesim=" + t.Cosinesim());
                }
                matrix[i][j] = sum/vector.get(i).size();
                //System.out.println(+i+"|"+j+"|"+matrix[i][j]);
            }
        }
       //找出array与vector的每一行的最大值
        for (i=0; i< m; i++) {
            double q1 = 0;   //应该在该位置初始化m,而不是一开始
            for (j = 0; j < n; j++) {
                if (matrix[i][j] > q1) {
                    q1 = matrix[i][j];   //取最大值
                    q2 = j;              //记录最大值的下标
                }
            }
            if (q1 >= k) {    //判断是否大于阈值
                vector.get(i).add(array.get(q2));
                System.out.println("Jaccardsim" + "(" + vector.get(i) + ") = " + q1);
                label.set(q2, null);
            }
        }

        //array中将不匹配的属性值加入vector中，记为新的类
        for (j = 0; j < label.size(); j++) {
            if (label.get(j)!=null) {
                vector.add(new ArrayList<String>());  //添加新的vector
                vector.get(m + q3).add(label.get(j));
                //System.out.println("j = " + j + ". Single attribute" + vector1.get(m+q3) );
                q3++;
            }
        }

        //重新计算每个类中的点到其他类的距离，取最大值重新分类
        int fix = vector.size();

        System.out.println("fix="+ fix);

        for (p = 0; p < fix; p++) {
            labelB:
            for (i = 0; i < vector.get(p).size(); i++) {
                //if (vector.get(p).size()==1)
                    //continue;
            double temp = 0;
            String temp1 = vector.get(p).get(0);

            //增加一个新的空类，该操作对应将某个attribute分到一个新类
                for (j = 0; j < fix; j++) {

                    //if (IsConnected(vector.get(p), vector.get(j), k)==true) {    //判断两个类有没有连接点
                       //break labelB;
                   // }
                    //else {
                        if (AvgSim(temp1, vector.get(j)) > temp) {
                            temp = AvgSim(temp1, vector.get(j));
                            q4 = j;    //需要记录最大值所在的类
                        }
                   //}
                 }
                vector.get(p).remove(0);
                vector.get(q4).add(temp1); //该轮最好的位置
            }
        }

        System.out.println("q4="+ q4);


        for (int l = 0; l < vector.size(); l++) {
            System.out.println(vector.get(l));
        }


        return vector;
    }

 */
//    public static ClusterData IncrementalDB(ArrayList<ArrayList<Integer>> vector, ArrayList<Integer> array, double k, double array1[][]) throws IOException {
//        //k是阈值
//        //该算法检测初始分类中的每个点是否符合到该类中剩余点的平均距离小于阈值，若不小于，则拿出来作为自由点重新加入计算
//        int i = 0;
//        int j = 0;
//        int p = 0;
//        int merge = 0;
//        int split = 0;
//        int move = 0;
//        int loop = 0;
//        // write println into ChangeHistory file
//        FileWriter xw = null;
//        try {
//            File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Incre.txt");
//            xw = new FileWriter(file, true);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        PrintWriter pw = new PrintWriter(xw);
//        ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();  //用来存储新的schema的队列
//        for (i = 0; i < array.size(); i++) {
//            queue.add(new ArrayList<Integer>());
//            queue.get(i).add(array.get(i));
//        }
//        //ClusterParameter initialization = DbindexPara1(vector, queue, array1);
//        ClusterParameter temp = DbindexPara1(vector, queue, array1);
//        double sum = temp.sum;
//        System.out.println("Sum1 " + sum);
//        System.out.println("Sum DB " + DBindex111(vector, queue, array1));
//        ArrayList<Double> intra_vector = temp.intra_vector;
//        ArrayList<ArrayList<Double>> inter_array = temp.inter;
//        while (queue.size() > 0) {
//            //ClusterParameter temp = DbindexPara2(vector, queue, array1, initialization.intra_vector, initialization.inter, vector.get(0), 0, 0);
//            //double sum = temp.sum;
//            System.out.println("Sum " + sum);
//
//            boolean change = false;  //用来记录cluster是否改变
//            ArrayList<Integer> b111;
//            for (i = vector.size() - 1; i >= 0; i--) {          //compare queue.get(0) with vector
//                if (IsConnected(queue.get(0), vector.get(i), k, array1) == false){    //we do need to judge IsConnected every time.
//                    continue;
//                }
//                else {
//                    loop ++;
//                    ArrayList <Integer> bb = new ArrayList<Integer>();  //use it to store the original cluster
//                    int a111 = vector.get(i).size();
//                    vector.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
//                    ArrayList<Double> intratemp = intra_vector;
//                    ArrayList<ArrayList<Double>> intertemp = inter_array;
//                    System.out.println(vector);
//                    ClusterParameter temp1 = DbindexPara2(vector, queue, array1, intra_vector, inter_array, vector.get(i), i, 1);
//                    System.out.println(vector);
//                    System.out.println(queue);
//                    System.out.println(temp1.intra_vector);
//                    for (int s= 0; s< temp1.inter.size(); s ++){                   //something wrong here
//                        System.out.println("Inter Array " +temp1.inter.get(s));
//                    }
//                    System.out.println("Sum 2 "+ temp1.sum);
//                    System.out.println("DBindex111 "+ DBindex222(vector, queue, array1));
//                    if (temp1.sum < sum) {
//                        for (j=0; j< a111; j++)
//                            bb.add(vector.get(i).get(j));
//
//                        System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
//                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
//                        //record when the merge happens
////                        System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
////                        +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(bb, array1)+" to "+Intra_Cluster(vector.get(i),array1));
////                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
////                                +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(bb, array1))+" to "+ (1- Intra_Cluster(vector.get(i),array1)));
//                        pw.flush();
//                        bb.clear();
//                        sum = temp1.sum;
//                        intra_vector = temp1.intra_vector;
//                        inter_array = temp1.inter;
//
//                        queue.add(vector.get(i));
//                        queue.remove(0);
//                        vector.remove(i);
//                        change = true;
//                        merge++;
//                        break;
//                    } else {
//                        intra_vector = intratemp;      //recover
//                        inter_array = intertemp;
//                        removeFrom(vector.get(i), a111);      //maybe there is an existing method to do this
//                    }
//                }
//            }
//            if (change == false & vector.size()>0) {
//                for (i = queue.size() - 1; i > 0; i--) {          //compare queue.get(0) with queue except itself
//                    if (IsConnected(queue.get(0), queue.get(i), k, array1) == false)    //we do need to judge IsConnected every time.
//                    {
//                        continue;
//                    } else {
//                        loop ++;
//                        int a11 = vector.size();
//                        ArrayList <Integer> cc = new ArrayList<Integer>();
//                        int a1111 = queue.get(i).size();     //记录此时长度，方便remove
//                        queue.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
//                        ArrayList<Double> intratemp = intra_vector;
//                        ArrayList<ArrayList<Double>> intertemp = inter_array;
//                        ClusterParameter temp2 = DbindexPara2(vector, queue, array1, intra_vector, inter_array, queue.get(i), i, 2);
//                        if (temp2.sum < sum) {
//                              //use it to store the original cluster
//                            for (j=0; j< a1111; j++)
//                                cc.add(queue.get(i).get(j));
//
//                            System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
//                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
////                            System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
////                                    +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(cc, array1)+" to "+Intra_Cluster(queue.get(i),array1));
////                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
////                                    +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(cc, array1))+" to "+ (1- Intra_Cluster(queue.get(i),array1)));
//                            pw.flush();
//                            cc.clear();
//                            sum = temp2.sum;
//                            intra_vector = temp2.intra_vector;
//                            inter_array = temp2.inter;
//
//                            queue.add(queue.get(i));
//                            queue.remove(i);
//                            queue.remove(0);
//                            change = true;
//                            merge++;
//                            break;
//                        } else {
//                            intra_vector = intratemp;      //recover
//                            inter_array = intertemp;
//                            removeFrom(queue.get(i), a1111);      //maybe there is an existing method to do this
//                        }
//                    }
//                }
//            }
//
//            if (change == false & queue.get(0).size() > 1 & vector.size() > 0 ) {      //try to Split, if b111.size()=1, we do not need to split
//                System.out.println("Vector Size " + vector.size());
//                vector.add(new ArrayList<>());
//                for (i=0; i< queue.get(0).size(); i++)
//                    vector.get(vector.size()-1).add(queue.get(0).get(i));
//                //vector.add(queue.get(0));       //note that if we directly add queue.get(0), we actually are using queue.get(0), it is dynamic
//                vector.add(new ArrayList<>());
//                for (p = queue.get(0).size() - 1; p >= 0; p--) {
//                    loop++;
//                    vector.get(vector.size() - 1).add(vector.get(vector.size() - 2).get(0));    //add b111.get(p) to next row
//                    vector.get(vector.size() - 2).remove(0);   //delete b111.get(p) from the last row
//                    //System.out.println("Db Split "+DBindex111(vector, queue, array1));
//                    //System.out.println("temp "+temp);
//                    ArrayList<Double> intratemp = intra_vector;
//                    ArrayList<ArrayList<Double>> intertemp = inter_array;
//                    System.out.println("Vector Size 1 " + vector.size());
//                    ClusterParameter temp3 = DbindexPara2(vector, queue, array1, intra_vector, inter_array, vector.get(0), 0, 3);
//                    if (temp3.sum < sum) {
//                        System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
//                        pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
////                        System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2)+" ; " +
////                                "The inter-similarity between them is: "+ (1- Inter_Cluster(vector.get(vector.size() - 1), vector.get(vector.size() - 2), array1))
////                                +"  ; The intra-similarity of the new cluster change from "+
////                                (1- Intra_Cluster(queue.get(0), array1))+" to "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1))+
////                                " and "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1)));
////                        pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
//                        pw.flush();
//                        sum = temp3.sum;
//                        intra_vector = temp3.intra_vector;
//                        inter_array = temp3.inter;
//
//                        change = true;
//                        split++;
//                    } else {
//                        intra_vector = intratemp;      //recover
//                        inter_array = intertemp;
//                        vector.get(vector.size() - 2).add(vector.get(vector.size() - 1).get(vector.get(vector.size() - 1).size() - 1));  // else put the value into its original position.
//                        vector.get(vector.size() - 1).remove(vector.get(vector.size() - 1).size() - 1);
//                    }
//                }
//                if (change == true) {
//                    queue.add(vector.get(vector.size() - 1));
//                    queue.add(vector.get(vector.size() - 2));
//                    vector.remove(vector.size() - 1);
//                    vector.remove(vector.size() - 1);
//                    queue.remove(0);
//
//                } else {
//                    vector.remove(vector.size() - 1);
//                    vector.remove(vector.size() - 1);
//                }
//            }
///*
//            if (change==false ) {    //try to move
//                {
//                }
//            }
// */
//            if (change == false){
//            //if (change == false & q3==q11)
//                //System.out.println("No change any more");
//                intra_vector = temp.intra_vector;      //recover
//                inter_array = temp.inter;
//                vector.add(queue.get(0));
//                queue.remove(0);
//            }
//
//        }
//        //System.out.println("Merge= "+ merge);
//        //System.out.println("Split= "+ split);
//        try {
//            xw.flush();
//            pw.close();
//            xw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ClusterData(vector, merge, split, move, loop);
//        }

//    public static ClusterParameter DbindexPara1(ArrayList <ArrayList<Integer>> vector, ArrayList<ArrayList <Integer>> queue, double array[][]){
//        //store intra and inter values
//        for (int i = 0; i < queue.size(); i++) {      //move queue to array1 we start from 1 because we do not want to remove 0 as it will change its original
//            vector.add(queue.get(i));                 //position due to the property of arraylist
//        }
//        ArrayList<Double> intravector = new ArrayList<Double>();
//        ArrayList<ArrayList<Double>> interarray = new ArrayList<ArrayList<Double>> ();
//        for (int i = 0; i < vector.size(); i++) {
//            intravector.add(Intra_Cluster(vector.get(i), array));
//        }
//        int a11 = vector.size();
//        //store inter distance in a 2d array
//        for (int i = 0; i < a11; i++) {
//            interarray.add(new ArrayList<Double>());
//            for (int j = 0; j < a11; j++) {
//                if (j == i) {
//                    interarray.get(i).add(0.0);
//                }
//                else
//                    interarray.get(i).add(Inter_Cluster(vector.get(i), vector.get(j), array));
//            }
//        }
//
//        int tt = intravector.size();
//        double array2[] = new double[tt];
//        for (int i = 0; i < tt; i++) {
//            double temp = 0;
//
//            for (int j = 0; j < tt; j++) {
//                if (j == i) continue;
//                double a = intravector.get(i);
//                double b = intravector.get(j);
//                double c = interarray.get(i).get(j);
//                if (temp < Fraction(a + b + 0.01, c + 0.001)) {
//                    temp = Fraction(a + b + 0.01, c + 0.001);
//                }
//
//            }
//
//            array2[i] = temp;
//        }
//        double sum = 0;
//        for (int i = 0; i < array2.length; i++) {
//            sum = sum + array2[i];
//        }
//        removeFrom(vector, vector.size()-queue.size());
//        return new ClusterParameter (sum, intravector, interarray);
//    }
//
//    public static ClusterParameter DbindexPara2(ArrayList <ArrayList<Integer>> vector, ArrayList<ArrayList <Integer>> queue, double array[][],
//                                                ArrayList<Double> intravector, ArrayList<ArrayList<Double>> interarray,
//                                                ArrayList<Integer> diff, int position, int l){
//        //adjust the current intravector and interarray
//        switch (l) {
//            case 0:   //for new records, also refer to initialization
//                break;
//            case 1:    //merge in vector
//                //double k1 = intravector.get(vector.size());
//                //intravector.set(vector.size(), 0.0);  //i.e. queue.get(0)
//                //double k2 = intravector.get(position);
//                //intravector.set(position, Intra_ClusterAdd(vector.get(position), queue.get(0), array, intravector.get(position)));
//                int a11= vector.size();
//                intravector.set(position, Intra_Cluster(vector.get(position), array));
//                intravector.remove(a11);
//                for (int i = 1; i < queue.size(); i++) {
//                    vector.add(queue.get(i));
//                }
//                interarray.remove(a11);
//                for (int i = 0; i < interarray.size(); i++) {
//                    interarray.get(i).remove(a11);
//                }
//
//                for (int i = 0; i < vector.size(); i++) {
//                    if (i == position) continue;
//                    double x = Inter_Cluster(diff, vector.get(i), array);
//                    interarray.get(i).set(position, x);   //column
//                    interarray.get(position).set(i, x);   //row
//                }
//                removeFrom(vector, vector.size() - queue.size()+1);
//                break;
//
//            case 2:
//                int a12= vector.size();
//                intravector.set(a12+ position, Intra_Cluster(queue.get(position), array));
//                intravector.remove(a12);
//                for (int i = 1; i < queue.size(); i++) {
//                    vector.add(queue.get(i));
//                }
//                interarray.remove(a12);
//                for (int i = 0; i < interarray.size(); i++) {
//                    interarray.get(i).remove(a12);
//                }
//                System.out.println(vector);
//                System.out.println(queue);
//                for (int i = 0; i < vector.size(); i++) {
//                    if (i == (a12+ position -1)) continue;
//                    double x = Inter_Cluster(diff, vector.get(i), array);
//                    System.out.println(interarray.get(i));
//                    interarray.get(i).set(a12+ position -1, x);   //column
//                    interarray.get(a12+ position -1).set(i, x);   //row
//                }
//                removeFrom(vector, vector.size() - queue.size()+1);
//                break;
//
//            case 3:
//                int a13= vector.size();
//                System.out.println(a13);
//                System.out.println(" size "+ interarray.size());
//                intravector.add(a13-2, Intra_Cluster(vector.get(a13-2), array));
//                intravector.set(a13-1, Intra_Cluster(vector.get(a13-1), array)); //do not need to remove a11, as we also need to add a row
//                for (int i = 1; i < queue.size(); i++) {
//                    vector.add(queue.get(i));
//                }
//                System.out.println("Q size "+ queue);
//                System.out.println("V size "+ vector);
//                interarray.add(a13-2, new ArrayList<Double>());
//                for (int i = 0; i < interarray.size()-1; i++) {
//                    interarray.get(a13-2).add(0.0);
//                }
//                System.out.println(interarray);
//                for (int i = 0; i < interarray.size(); i++) {
//                    interarray.get(i).add(a13-2, 0.0);
//                }
//
//                for (int i = 0; i < vector.size(); i++) {
//                    if (i != (a13 -2)) {
//                        double x = Inter_Cluster(vector.get(a13 - 2), vector.get(i), array);
//                        interarray.get(i).set(a13- 2, x);   //column
//                        interarray.get(a13 - 2).set(i, x);   //row
//                    }
//                    if (i != (a13 -1)) {
//                        double y = Inter_Cluster(vector.get(a13 - 1), vector.get(i), array);
//                        System.out.println("__i __ " + i +" __size "+ interarray.size());
//                        interarray.get(i).set(a13- 1, y);   //column
//                        interarray.get(a13 - 1).set(i, y);   //row
//                    }
//                }
//                removeFrom(vector, vector.size() - queue.size()+1);
//                break;
//        }
//
//        // adjust intervector and interarray to themselves
//          // queue(0) should not be computed as its has been merged into vector before this computing
//        int tt = intravector.size();
//        double array2[] = new double[tt];
//        for (int i = 0; i < tt; i++) {
//            double temp = 0;
//            for (int j = 0; j < tt; j++) {
//                if (j == i) continue;
//                double a = intravector.get(i);
//                double b = intravector.get(j);
//                double c = interarray.get(i).get(j);
//                if (temp < Fraction(a + b + 0.01, c + 0.001)) {
//                    temp = Fraction(a + b + 0.01, c + 0.001);
//                }
//            }
//
//            array2[i] = temp;
//        }
//        double sum = 0;
//        for (int i = 0; i < array2.length; i++) {
//            sum = sum + array2[i];
//        }
//        return new ClusterParameter (sum, intravector, interarray);
//
//    }




}
