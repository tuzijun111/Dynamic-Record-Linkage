package algorithm;

import java.io.*;
import java.util.*;


public class DSframe {
    public static void main(String[] args) {


    }

    public static void Naive(ArrayList<ArrayList<String>> abc, ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Batch.txt");

        if (file.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
            file.delete();
        }
        file.createNewFile();
        for (int i=1;i<abc.size();i++){
            ttt.add(i);
            inter = Cluster.OneByOne(inter, ttt, Cluster.Parameter.threshold, ad);
            ttt.clear();
        }
        System.out.println("Naive------------------------"+inter);
    }

    public static void DBindexGreedy (ArrayList<ArrayList<Integer>> inter,  ArrayList<Integer> ttt, double ad[][]) throws IOException {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Batch.txt");
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
        for (int i=0;i<10;i++) {
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
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch20.txt");
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch50.txt");

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
            if((i%50==0)|| i==abc.size()-1){

                update++;
                System.out.println(ttt);
                Cluster.ClusterData result = Cluster.IncrementalDB(inter, ttt, Cluster.Parameter.threshold, ad);   //this method is quite costly when we consider each record as a snapshot
                inter = result.inter;
                merge = merge + result.merge;
                split = split + result.split;
                loop = loop + result.loop;
                ttt.clear();    //remove it as the batch size =1

                System.out.println("Update = " + update);

                if (update !=1){
                    //analyse the changes happened on previous clusters
                    ArrayList<ArrayList<Integer>> c1 = new ArrayList<ArrayList<Integer>>();
                    for (int m = 0; m < precluster.size(); m++) {
                        for (int n = 0; n < inter.size(); n++){
                            if (Exist(precluster.get(m), inter.get(n))) {
                                Integer temp1[] = new Integer[precluster.get(m).size()];
                                Integer temp2[] = new Integer[inter.get(n).size()];
                                for (int l = 0; l<temp1.length; l++){
                                    temp1[l] = precluster.get(m).get(l);
                                    System.out.println("temp1[l])=  " + temp1[l]);
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
                    System.out.println("Iteration " + update);
                    System.out.println("Previous Cluster Results have changed into " + c1);
                    System.out.println("New Cluster Results " + inter);
                    pw.println("Iteration " + update);
                    pw.println("Previous Cluster Results have changed into " + c1);
                    pw.println();
                    pw.println("New Cluster Results " + inter);
                    pw.println();
                    c1.removeAll(c1);
                    precluster = (ArrayList<ArrayList<Integer>>) deepCopy(inter);
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
                    pw.println("Iteration " + update);
                    pw.println("New Cluster Results " + inter);
                    pw.println();
                }

            }
        }




        int a[] = new int [inter.size()];
        for (int i =0; i< inter.size(); i++){
            a[i] = inter.get(i).size();
        }
        Arrays.sort(a);
        for (int i =a.length-1; i>= 0; i--){
            System.out.print(a[i]+" ");
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

    private static Integer[] getJ(Integer[] m, Integer[] n)
    {
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




}
