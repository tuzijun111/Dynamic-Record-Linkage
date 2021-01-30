package algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


public class MLbased {
    public static void main(String[] args) throws IOException {
        PythonScript("MergeModel.py");
    }

    public static ArrayList<ArrayList<Integer>> ClusterToFile(ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> vector, ArrayList<Hashtable> adj) throws IOException {
        File file1 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Intra.txt");
        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Inter.txt");
        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size1.txt");
        File file4 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size2.txt");
        //File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/change.txt");

        if (file1.exists()) { file1.delete(); }
        if (file2.exists()) { file2.delete(); }
        if (file3.exists()) { file3.delete(); }
        if (file4.exists()) { file4.delete(); }

        file1.createNewFile(); file2.createNewFile(); file3.createNewFile(); file4.createNewFile();

        FileWriter xw1 = null; FileWriter xw2 = null; FileWriter xw3 = null; FileWriter xw4 = null; //FileWriter xw5 = null;

        xw1 = new FileWriter(file1, true);
        xw2 = new FileWriter(file2, true);
        xw3 = new FileWriter(file3, true);
        xw4 = new FileWriter(file4, true);
        //xw5 = new FileWriter(file5, true);

        PrintWriter pw1 = new PrintWriter(xw1);
        PrintWriter pw2 = new PrintWriter(xw2);
        PrintWriter pw3 = new PrintWriter(xw3);
        PrintWriter pw4 = new PrintWriter(xw4);
        //PrintWriter pw5 = new PrintWriter(xw5);
        ArrayList<Integer> ttt = new ArrayList<Integer>();
        for (int i=1679;i<1879;i++){
            ttt.add(i);
            vector.add(new ArrayList<Integer>());
            vector.get(vector.size()-1).add(i);
            ttt.clear();
        }
        for (int i=0; i<vector.size(); i++){
            pw1.print(Cluster.Intra_Cluster(vector.get(i), adj)+ ",");
            pw2.print(Cluster.MinInter3(i, vector, adj) + ",");
            pw3.print(vector.get(i).size() + ",");
            pw4.print(Cluster.MinInter3Cluster(i, vector, adj) + ",");

            pw1.flush();
            pw2.flush();
            pw3.flush();
            pw4.flush();
        }

        try {
            xw1.flush(); xw2.flush();xw3.flush();xw4.flush();
            pw1.close(); pw2.close();pw3.close();pw4.close();
            xw1.close(); xw2.close();xw3.close();xw4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vector;
    }

    public static ArrayList<ArrayList<Integer>> MergeConverge(ArrayList<ArrayList<Integer>> vector, ArrayList<Hashtable> adj) throws IOException{
    //public static void MergeConverge() throws IOException{
        //read the prediction results from a file, and save them into an arraylist
        long startTime1 = System.currentTimeMillis();
        ArrayList<double []> prediction = new ArrayList<double[]>();
        try {
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/merge1output.txt"));
            String line = null;
            int index = 0;
            while ((line = reade.readLine()) != null) {
                int s = 0;
                String dd [] = line.split(" ");
                double [] item = new double[dd.length]; //used to store the 2-d array
                for (String ss : dd){
                    if (ss!= null){
                        item[s++] = Double.parseDouble(ss);
                    }
                }
                prediction.add(item);
            }
            System.out.println("Prediction: "+prediction.size());

//            for (int j = 0; j<prediction.size(); j++){
//                System.out.print((int) prediction.get(j)[0]+" ");
//                System.out.print(prediction.get(j)[1]);
//                System.out.println();
//            }

            //store those clusters which are predicted as "change"
            ArrayList <Integer> aa = new ArrayList<Integer>();   //used to store candidate clusters with its index i.e. an integer
            for (int j = 0; j<prediction.size(); j++){
                if(prediction.get(j)[1]>0.4){   //the threshold should be 0.5, but we tend to guarantee that we find all the clusters which are possible to change
                    aa.add((int)prediction.get(j)[0]);
                }
            }
            System.out.println("AA" + aa.size());
            ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();  //used to store the candidates
            //need to deepcopy
//            for (int i = 0; i< aa.size(); i++){
//
//            }
            //remove those clusters in queue from vector
            for (int i = aa.size()-1; i>= 0; i--){
                queue.add(vector.get(aa.get(i)));
                vector.remove(aa.get(i));
            }

            //The problem here is that we need not check all the clusters in vector, we just need to check those new single clusters i.e. new records.
            //This is because existing clusters has been stable, otherwise it can not be taken as a final result. This means existing clusters can not merge with themselves.
            //This kind of analyis should be put into the paper. It is a good point.
            int loop =0;
            double temp = Cluster.DBindex111(vector, queue, adj);
            int wrongprediction = aa.size(); //used to indicate whether there are clusters to be merged or not.
            while (queue.size()>0){
                loop ++;
                if(queue.size()==1){
                    vector.add(queue.get(0));
                    queue.remove(0);
                    wrongprediction --;
                    break;
                }
                Cluster.Para res1 = Cluster.MinInterForMerge(queue.get(0), queue, adj);
                int cluster = res1.cluster;
//                vector.add(queue.get(cluster));
//                queue.remove(cluster);
//                queue.remove(0);

                int a111 = queue.get(cluster).size();
                queue.get(cluster).addAll(queue.get(0));
                if (Cluster.DBindex222(vector, queue, adj) < temp) {
                    temp = Cluster.DBindex222(vector, queue, adj);
                    vector.add(queue.get(cluster));
                    queue.remove(cluster);
                    queue.remove(0);
                } else {   //if queue.get(0) can merge with no clusters, then we put it to the end of the vector. This strategy will guarantee that
                    // Because we only try to merge it with the cluster which has the largest intervalue with it
                    vector.add(queue.get(0));
                    Cluster.removeFrom(queue.get(cluster), a111);
                    queue.remove(0);
                    wrongprediction --;
                }
            }
            long endTime1 = System.currentTimeMillis();
            System.out.println("Running time for rounds：" + (endTime1 - startTime1) + "ms");

            System.out.println("WrongPrediction: " + wrongprediction);
            //ClusterToFile1(vector, array1);
            if (wrongprediction != 0) {
                //output the parameters of the new clustering
                ClusterToFile1(vector, adj);
                PythonScript("MergeModelPredict.py");
                MergeConverge(vector, adj);
            }

//            long endTime2 = System.currentTimeMillis();
//            System.out.println("Running time for the new algorithm：" + (endTime2 - startTime2) + "ms");
//            System.out.println("The number of loop is: "+loop);
        }

        //Clearn empty arraylist in vector

        catch (Exception e) {
            e.printStackTrace();
        }

        return vector;
    }

    public static ArrayList<ArrayList<Integer>> SplitConverge(ArrayList<ArrayList<Integer>> vector, ArrayList<Hashtable> adj) throws IOException{
        //public static void MergeConverge() throws IOException{
        //read the prediction results from a file, and save them into an arraylist
        long startTime1 = System.currentTimeMillis();
        ArrayList<double []> prediction = new ArrayList<double[]>();
        try {
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/split1output.txt"));
            String line = null;
            int index = 0;
            while ((line = reade.readLine()) != null) {
                int s = 0;
                String dd [] = line.split(" ");
                double [] item = new double[dd.length]; //used to store the 2-d array
                for (String ss : dd){
                    if (ss!= null){
                        item[s++] = Double.parseDouble(ss);
                    }
                }
                prediction.add(item);
            }

//            for (int j = 0; j<prediction.size(); j++){
//                System.out.print((int) prediction.get(j)[0]+" ");
//                System.out.print(prediction.get(j)[1]);
//                System.out.println();
//            }

            //store those clusters which are predicted as "change"
            ArrayList <Integer> aa = new ArrayList<Integer>();   //used to store candidate clusters with its index i.e. an integer
            for (int j = 0; j<prediction.size(); j++){
                if(prediction.get(j)[1]>0.4){   //the threshold should be 0.5, but we tend to guarantee that we find all the clusters which are possible to change
                    aa.add((int)prediction.get(j)[0]);
                }
            }
            ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();  //used to store the candidates
            //need to deepcopy
//            for (int i = 0; i< aa.size(); i++){
//
//            }
            //remove those clusters in queue from vector
            double temp = Cluster.DBindex(vector, adj);
//            for (int i = aa.size()-1; i>= 0; i--){
//                queue.add(vector.get(aa.get(i)));
//                vector.remove(aa.get(i));
//            }

            //int wrongprediction = aa.size(); //used to indicate whether there are clusters to be merged or not.
            for (int i = aa.size()-1; i>= 0; i--){
                if(vector.get(aa.get(i)).size()>1) {
                    Cluster.Para res1 = Cluster.MinInterForSplit(vector.get(aa.get(i)), adj);
                    int cluster = res1.cluster;

                    vector.add(new ArrayList<>());
                    vector.get(vector.size() - 1).add(vector.get(aa.get(i)).get(cluster));

                    if (Cluster.DBindex(vector, adj) < temp) {
                        temp = Cluster.DBindex(vector, adj);
                    } else {
                        vector.get(aa.get(i)).add(vector.get(aa.get(i)).get(cluster));
                        vector.remove(vector.size() - 1);
                    }
                }
            }
            ClusterToFile2(vector, adj);

            long endTime1 = System.currentTimeMillis();
            System.out.println("Running time for rounds：" + (endTime1 - startTime1) + "ms");

//            if (wrongprediction != 0) {
//                //output the parameters of the new clustering
//                ClusterToFile2(vector, array1);
//                PythonScript("SplitModelPredict.py");
//                SplitConverge(vector, array1);
//            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return vector;
    }


    public static void PythonScript(String modelname) throws IOException{
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python /Users/binbingu/Documents/Codes/Python/MLmodel/"+modelname);// 执行py文件

//            //get the output, but this will produce much time cost
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void ClusterToFile1(ArrayList<ArrayList<Integer>> vector, ArrayList<Hashtable> adj) throws IOException {
        File file1 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Intra.txt");
        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Inter.txt");
        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size1.txt");
        File file4 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size2.txt");
        //File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/change.txt");

        if (file1.exists()) { file1.delete(); }
        if (file2.exists()) { file2.delete(); }
        if (file3.exists()) { file3.delete(); }
        if (file4.exists()) { file4.delete(); }

        file1.createNewFile(); file2.createNewFile(); file3.createNewFile(); file4.createNewFile();

        FileWriter xw1 = null; FileWriter xw2 = null; FileWriter xw3 = null; FileWriter xw4 = null; //FileWriter xw5 = null;

        xw1 = new FileWriter(file1, true);
        xw2 = new FileWriter(file2, true);
        xw3 = new FileWriter(file3, true);
        xw4 = new FileWriter(file4, true);
        //xw5 = new FileWriter(file5, true);

        PrintWriter pw1 = new PrintWriter(xw1);
        PrintWriter pw2 = new PrintWriter(xw2);
        PrintWriter pw3 = new PrintWriter(xw3);
        PrintWriter pw4 = new PrintWriter(xw4);
        //PrintWriter pw5 = new PrintWriter(xw5);
        for (int i=0; i<vector.size(); i++){
            pw1.print(Cluster.Intra_Cluster(vector.get(i), adj)+ ",");
            pw2.print(Cluster.MinInter3(i, vector, adj) + ",");
            pw3.print(vector.get(i).size() + ",");
            pw4.print(Cluster.MinInter3Cluster(i, vector, adj) + ",");

            pw1.flush();
            pw2.flush();
            pw3.flush();
            pw4.flush();
        }

        try {
            xw1.flush(); xw2.flush();xw3.flush();xw4.flush();
            pw1.close(); pw2.close();pw3.close();pw4.close();
            xw1.close(); xw2.close();xw3.close();xw4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ClusterToFile2(ArrayList<ArrayList<Integer>> vector, ArrayList<Hashtable> adj) throws IOException {
        File file1 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Intra_S.txt");
        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Inter_S.txt");
        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/Size1_S.txt");
        //File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/prediction/change.txt");

        if (file1.exists()) { file1.delete(); }
        if (file2.exists()) { file2.delete(); }
        if (file3.exists()) { file3.delete(); }

        file1.createNewFile(); file2.createNewFile(); file3.createNewFile();

        FileWriter xw1 = null; FileWriter xw2 = null; FileWriter xw3 = null;

        xw1 = new FileWriter(file1, true);
        xw2 = new FileWriter(file2, true);
        xw3 = new FileWriter(file3, true);
        //xw5 = new FileWriter(file5, true);

        PrintWriter pw1 = new PrintWriter(xw1);
        PrintWriter pw2 = new PrintWriter(xw2);
        PrintWriter pw3 = new PrintWriter(xw3);
        //PrintWriter pw5 = new PrintWriter(xw5);
        for (int i=0; i<vector.size(); i++){
            pw1.print(Cluster.Intra_Cluster(vector.get(i), adj)+ ",");
            pw2.print(Cluster.MinInter3(i, vector, adj) + ",");
            pw3.print(vector.get(i).size() + ",");
            pw1.flush();
            pw2.flush();
            pw3.flush();
        }
        try {
            xw1.flush(); xw2.flush();xw3.flush();
            pw1.close(); pw2.close();pw3.close();
            xw1.close(); xw2.close();xw3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static Cluster.ClusterData Model(ArrayList<ArrayList<Integer>> vector, ArrayList<Integer> array, double k, ArrayList<Hashtable> adj) throws IOException {
        int i = 0;
        int j = 0;
        int p = 0;
        int merge = 0;
        int split = 0;
        int move = 0;
        int loop = 0;

//        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Merge1000.txt");
//        File file1 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/DB-Split1000.txt");
//        File file2 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Intra.txt");
//        File file3 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Inter.txt");
//        File file4 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/change.txt");
//
//        File file5 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Intra_S.txt");
//        File file6 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Inter_S.txt");
//        File file7 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/change_S.txt");
//
//        File file8 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Size1.txt");
//        File file9 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/Size2.txt");
//        File file10 = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/DB/test.txt");
//
//
//
//        FileWriter xw = null;
//        FileWriter xw1 = null; FileWriter xw2 = null; FileWriter xw3 = null; FileWriter xw4 = null;
//
//        FileWriter xw5 = null; FileWriter xw6 = null; FileWriter xw7 = null; FileWriter xw8 = null; FileWriter xw9 = null;
//        FileWriter xw10 = null;
//
//        xw = new FileWriter(file, true);
//        xw1 = new FileWriter(file1, true);
//        xw2 = new FileWriter(file2, true);
//        xw3 = new FileWriter(file3, true);
//        xw4 = new FileWriter(file4, true);
//        xw5 = new FileWriter(file5, true);
//        xw6 = new FileWriter(file6, true);
//        xw7 = new FileWriter(file7, true);
//        xw8 = new FileWriter(file8, true);
//        xw9 = new FileWriter(file9, true);
//        xw10 = new FileWriter(file10, true);
//
//        PrintWriter pw = new PrintWriter(xw);
//        PrintWriter pw1 = new PrintWriter(xw1);
//        PrintWriter pw2 = new PrintWriter(xw2);
//        PrintWriter pw3 = new PrintWriter(xw3);
//        PrintWriter pw4 = new PrintWriter(xw4);
//        PrintWriter pw5 = new PrintWriter(xw5);
//        PrintWriter pw6 = new PrintWriter(xw6);
//        PrintWriter pw7 = new PrintWriter(xw7);
//        PrintWriter pw8 = new PrintWriter(xw8);
//        PrintWriter pw9 = new PrintWriter(xw9);
//        PrintWriter pw10 = new PrintWriter(xw10);


        ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();
        for (i = 0; i < array.size(); i++) {
            queue.add(new ArrayList<Integer>());
            queue.get(i).add(array.get(i));
        }
        //Step 1: address new records i.e. queue

        ArrayList <Integer> a1 = new ArrayList <Integer>();


        while (queue.size()>0){
            double temp = Cluster.DBindex111(vector, queue, adj);
            boolean change = false;
            Cluster.ClusterVorQ res1 = Cluster.WhichCluster(queue.get(0), vector, queue, adj);
            if ((res1.inter < 0.4)&& Cluster.Intra_Cluster(queue.get(0), adj)<0.3){
                if (res1.VorQ == 1){
                     {
                        vector.get(res1.cluster).addAll(queue.get(0));
                        queue.remove(0);
                       //  if (Cluster.DBindex111(vector, queue, array1) < temp)
                    }
                }
                else{

                        queue.get(res1.cluster).addAll(queue.get(0));
                        queue.remove(0);
                   // if (Cluster.DBindex222(vector, queue, array1) < temp)

                }
            }
            else{
                vector.add(queue.get(0));
                queue.remove(0);
            }
            //also need to address records in "vector", current solution is not completely correct

//            if (change == false)
//                break;
        }


//        while (queue.size() > 0) {
//            double temp = Cluster.DBindex111(vector, queue, array1);
//            boolean change = false;  //用来记录cluster是否改变
//            ArrayList<Integer> b111;
//            for (i = vector.size() - 1; i >= 0; i--) {          //compare queue.get(0) with vector
//                if (Cluster.IsConnected(queue.get(0), vector.get(i), k, array1) == false) {    //we do need to judge IsConnected every time.
//                    continue;
//                } else {
//                    loop++;
//                    ArrayList<Integer> bb = new ArrayList<Integer>();  //use it to store the original cluster
//                    int a111 = vector.get(i).size();
//                    vector.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
//                    if (Cluster.DBindex222(vector, queue, array1) < temp) {
//                        for (j = 0; j < a111; j++)
//                            bb.add(vector.get(i).get(j));
//
//                        System.out.println(queue.get(0) + " Merge With " + bb + " into " + vector.get(i));
//                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
//                        z1++;
//                        //pw.println(z1 + " "+Intra_Cluster(vector.get(i), array1));
//                        pw2.print(Cluster.Intra_Cluster(vector.get(i), array1) + ",");
//                        pw3.print(Cluster.MinInter(queue.get(0),bb, vector, queue, array1) + ",");
//                        //pw3.print(Inter_Cluster(queue.get(0), bb, array1) + ",");
//                        pw4.print("1,");
//                        pw8.print(queue.get(0).size() + ",");
//                        pw9.print(Cluster.MinInterCluster(queue.get(0),bb, vector, queue, array1) + ",");
//                        if(Cluster.MinInter(queue.get(0), bb,vector, queue, array1)==0.5)
//                        {pw10.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
//                            pw10.println("Inter: "+ Cluster.Inter_Cluster(queue.get(0), bb,array1));
//                            pw10.flush();
//
//                        }
//
////                    pw2.println(Intra_Cluster(vector.get(i), array1) + ",");
////                    pw3.println(Inter_Cluster(queue.get(0), bb, array1) + ",");
////                    pw4.println("1,");
//                        //record when the merge happens
////                        System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
////                        +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(bb, array1)+" to "+Intra_Cluster(vector.get(i),array1));
////                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
////                                +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(bb, array1))+" to "+ (1- Intra_Cluster(vector.get(i),array1)));
//                        pw.flush();
//                        pw2.flush();
//                        pw3.flush();
//                        pw4.flush(); pw8.flush(); pw9.flush();
//                        bb.clear();
//                        //temp = DBindex222(vector, queue, k, array1);
//                        queue.add(vector.get(i));
//                        queue.remove(0);
//                        vector.remove(i);
//                        change = true;
//                        merge++;
//                        break;
//                    } else {
//                        Cluster.removeFrom(vector.get(i), a111);      //maybe there is an existing method to do this
//                        if (ne_sample_size < 100) {
//                            pw2.print(Cluster.Intra_Cluster(queue.get(0), array1) + ",");
//                            pw3.print(Cluster.MinInter1(queue.get(0), vector, queue, array1) + ",");
//                            //pw3.print(Inter_Cluster(queue.get(0), vector.get(i), array1) + ",");
//                            pw4.print("0,");
//                            pw8.print(queue.get(0).size() + ",");
//                            pw9.print(Cluster.MinInter1Cluster(queue.get(0), vector, queue, array1) + ",");
////                        pw2.println(Intra_Cluster(vector.get(i), array1) + ",");
////                        pw3.println(Inter_Cluster(queue.get(0), vector.get(i), array1) + ",");
////                        pw4.println("0,");
//                            ne_sample_size++;
//                            pw2.flush();
//                            pw3.flush();
//                            pw4.flush(); pw8.flush(); pw9.flush();
//                        }
//                    }
//                }
//            }
//            if (change == false) {
//                for (i = queue.size() - 1; i > 0; i--) {          //compare queue.get(0) with queue except itself
//                    if (Cluster.IsConnected(queue.get(0), queue.get(i), k, array1) == false)    //we do need to judge IsConnected every time.
//                    {
//                        continue;
//                    } else {
//                        loop++;
//                        int a11 = vector.size();
//                        ArrayList<Integer> cc = new ArrayList<Integer>();
//                        int a1111 = queue.get(i).size();     //记录此时长度，方便remove
//                        queue.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
//                        if (Cluster.DBindex222(vector, queue, array1) < temp) {
//                            //use it to store the original cluster
//                            for (j = 0; j < a1111; j++)
//                                cc.add(queue.get(i).get(j));
//
//                            System.out.println(queue.get(0) + " Merge With " + cc + " into " + queue.get(i));
//                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
//                            z1++;
//                            pw2.print(Cluster.Intra_Cluster(queue.get(0), array1) + ",");
//                            pw3.print(Cluster.MinInter(queue.get(0), cc, vector, queue, array1) + ",");
//                            pw8.print(queue.get(0).size() + ",");
//                            pw9.print(Cluster.MinInterCluster(queue.get(0),cc, vector, queue, array1) + ",");
//                            //pw3.print(Inter_Cluster(queue.get(0), cc, array1) + ",");
//                            pw4.print("1,");
//                            if(Cluster.MinInter(queue.get(0),cc, vector, queue, array1)==0.5)
//                            {pw10.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
//                                pw10.println("Inter: "+ Cluster.Inter_Cluster(queue.get(0), cc,array1));
//                                pw10.flush();
//                            }
//
////                        pw2.println(Intra_Cluster(queue.get(0), array1) + ",");
////                        pw3.println(Inter_Cluster(queue.get(0), cc, array1) + ",");
////                        pw4.println("1,");
//
////                            System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
////                                    +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(cc, array1)+" to "+Intra_Cluster(queue.get(i),array1));
////                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
////                                    +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(cc, array1))+" to "+ (1- Intra_Cluster(queue.get(i),array1)));
//                            pw.flush();
//                            pw2.flush();
//                            pw3.flush();
//                            pw4.flush(); pw8.flush(); pw9.flush();
//                            cc.clear();
//                            //temp = DBindex222(vector, queue, k, array1);
//                            queue.add(queue.get(i));
//                            queue.remove(i);
//                            queue.remove(0);
//                            change = true;
//                            merge++;
//                            break;
//                        } else {
//                            Cluster.removeFrom(queue.get(i), a1111);      //maybe there is an existing method to do this
//                            if (ne_sample_size < 100) {
//                                pw2.print(Cluster.Intra_Cluster(queue.get(i), array1) + ",");
//                                pw3.print(Cluster.MinInter1(queue.get(0), vector, queue, array1) + ",");
//                                //pw3.print(Inter_Cluster(queue.get(i), queue.get(0), array1) + ",");
//                                pw4.print("0,");
//                                pw8.print(queue.get(0).size() + ",");
//                                pw9.print(Cluster.MinInter1Cluster(queue.get(0), vector, queue, array1) + ",");
////                            pw2.println(Intra_Cluster(queue.get(i), array1) + ",");
////                            pw3.println(Inter_Cluster(queue.get(i), queue.get(0), array1) + ",");
////                            pw4.println("0,");
//                                ne_sample_size++;
//                                pw2.flush();
//                                pw3.flush();
//                                pw4.flush();
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (change == false & queue.get(0).size() > 1) {      //try to Split, if b111.size()=1, we do not need to split
//                vector.add(new ArrayList<>());
//                for (i = 0; i < queue.get(0).size(); i++)
//                    vector.get(vector.size() - 1).add(queue.get(0).get(i));
//                //vector.add(queue.get(0));       //note that if we directly add queue.get(0), we actually are using queue.get(0), it is dynamic
//                vector.add(new ArrayList<>());
//                for (p = queue.get(0).size() - 1; p >= 0; p--) {
//                    loop++;
//                    vector.get(vector.size() - 1).add(vector.get(vector.size() - 2).get(0));    //add b111.get(p) to next row
//                    vector.get(vector.size() - 2).remove(0);   //delete b111.get(p) from the last row
//                    //System.out.println("Db Split "+DBindex111(vector, queue, array1));
//                    //System.out.println("temp "+temp);
//                    if (Cluster.DBindex222(vector, queue, array1) < temp) {
//                        System.out.println(queue.get(0) + " Split into " + vector.get(vector.size() - 1) + " and " + vector.get(vector.size() - 2));
//                        pw1.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
//                        z2++;
//                        //pw1.println(z2 + " "+Intra_Cluster(queue.get(0), array1));
////                        System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2)+" ; " +
////                                "The inter-similarity between them is: "+ (1- Inter_Cluster(vector.get(vector.size() - 1), vector.get(vector.size() - 2), array1))
////                                +"  ; The intra-similarity of the new cluster change from "+
////                                (1- Intra_Cluster(queue.get(0), array1))+" to "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1))+
////                                " and "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1)));
////                        pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
//                        pw1.flush();
//                        temp = Cluster.DBindex222(vector, queue, array1);
//                        change = true;
//                        split++;
//                    } else {
//                        vector.get(vector.size() - 2).add(vector.get(vector.size() - 1).get(vector.get(vector.size() - 1).size() - 1));  // else put the value into its original position.
//                        vector.get(vector.size() - 1).remove(vector.get(vector.size() - 1).size() - 1);
//                    }
//                }
//
//                if (change == true) {
//
//                    queue.add(vector.get(vector.size() - 1));
//                    queue.add(vector.get(vector.size() - 2));
//                    vector.remove(vector.size() - 1);
//                    vector.remove(vector.size() - 1);
//
//                    pw5.print(Cluster.Intra_Cluster(queue.get(0), array1) + ",");
//                    pw6.print(Cluster.MinInter2(queue.get(0), vector, queue, array1) + ",");
//                    pw7.print("1,");
//                    pw5.flush();
//                    pw6.flush();
//                    pw7.flush();
//
//                    queue.remove(0);
//
//                } else {
//                    vector.remove(vector.size() - 1);
//                    vector.remove(vector.size() - 1);
//                    if (ne_sample_size1 < 10) {
//                        pw5.print(Cluster.Intra_Cluster(queue.get(0), array1) + ",");
//                        pw6.print(Cluster.MinInter1(queue.get(0), vector, queue, array1) + ",");
//                        pw7.print("0,");
//                        ne_sample_size1++;
//                        pw5.flush();
//                        pw6.flush();
//                        pw7.flush();
//                    }
//                }
//            }
///*
//        if (change==false ) {    //try to move
//            {
//            }
//        }
//*/
//            if (change == false){
//                //if (change == false & q3==q11)
//                //System.out.println("No change any more");
//                vector.add(queue.get(0));
//                queue.remove(0);
//            }
//
//        }
//        //System.out.println("Merge= "+ merge);
//        //System.out.println("Split= "+ split);
//        try {
//            xw.flush();
//            xw1.flush();
//            xw2.flush();xw3.flush();xw4.flush();
//            xw5.flush();xw6.flush();xw7.flush(); pw8.flush(); pw9.flush(); pw10.flush();
//            pw.close();
//            pw1.close();
//            pw2.close();pw3.close();pw4.close();
//            pw5.close();pw6.close();pw7.close(); pw8.close(); pw9.close(); pw10.close();
//            xw.close();
//            xw1.close();
//            xw2.close();xw3.close();xw4.close();
//            xw5.close();xw6.close();xw7.close(); xw8.close(); xw9.close(); xw10.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Cluster.ClusterData(vector, merge, split, move, loop);
    }

}
