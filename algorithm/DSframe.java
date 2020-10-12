package algorithm;

import java.io.*;
import java.util.*;


public class DSframe {
    public static void main(String[] args) {

    }

    public static void Naive(ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Change_Batch1000.txt");

        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        for (int i=1;i<abc.size();i++){
            ttt.add(i);
            inter = Cluster.OneByOne(inter, ttt, Cluster.Parameter.threshold, ad);
            ttt.clear();
        }
        //translate the mark of records by adding 1 i.e. 0>1, 1>2, ...
//        for (int i=0;i<inter.size();i++){
//            for (int j=0;j<inter.get(i).size();j++) {
//                inter.get(i).set(j, inter.get(i).get(j)+1);
//            }
//        }
        System.out.println("Naive------------------------"+inter);
    }

    public static void DBindexGreedy (ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException {
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
                Cluster.ClusterData result= Cluster.DBGreedy(inter, j, ad);
                inter = result.inter;
                move = move + result.move;
            }
            temp[i] = Cluster.DBindex(inter, ad);
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

    public static void IncreGreedy (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException, ClassNotFoundException {
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
                Cluster.ClusterData result = Cluster.IncreForCorr(inter, ttt, Cluster.Parameter.threshold, ad);
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
                    System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
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
                        System.out.println(" Intra_Sim = "+Cluster.Intra_Cluster(inter.get(k), ad));
                        if(Cluster.Intra_Cluster(inter.get(k), ad)<0) {
                            for (int s1 = 0; s1 < inter.get(k).size(); s1++) {
                                for (int s2 = 0; s2 < inter.get(k).size(); s2++) {
                                    System.out.println(" SimAd = " + ad[inter.get(k).get(s1)][inter.get(k).get(s2)]);
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
                    System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
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

    public static void IncreGreedyForDBindex (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException, ClassNotFoundException {
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
                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);   //this method is quite costly when we consider each record as a snapshot
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

    public static ArrayList<ArrayList<Integer>> MLForDBindex (ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter, ArrayList<Integer> ttt, double ad[][]) throws IOException, ClassNotFoundException {

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
                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);   //this method is quite costly when we consider each record as a snapshot
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



}
