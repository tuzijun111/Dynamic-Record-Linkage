package algorithm;

import javafx.util.Pair;

import java.io.*;
import java.util.*;



public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        abc= DataPro();
        //for (int i=0; i<abc.size(); i++)
        //System.out.println(abc.get(i));
        ArrayList<ArrayList<Integer>> source = new ArrayList<ArrayList<Integer>>();
        source.add( new ArrayList<Integer>());
        source.get(0).add(0);                     // each i corresponds to abc.get(i)
        //System.out.println(source.get(0));
        ArrayList<ArrayList<Integer>> inter = source;
        int i=0;
        int j=0;
        int k=0;
        double ad[][] = CoraBlock(abc);

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


//        DSframe.Naive(abc, inter, ttt, ad);
//        DSframe.DBindexGreedy(inter, ttt, ad);
        DSframe.IncreGreedy(abc, inter, ttt, ad);


        long endTime = System.currentTimeMillis();
        System.out.println("Running time：" + (endTime - startTime) + "ms");
        System.out.println("Current DBindex: "+ Cluster.DBindex(inter, ad));


//        long startTime2 = System.currentTimeMillis();
//
//        ArrayList<ArrayList<Integer>> ab = new ArrayList<ArrayList<Integer>>();
//        ab= Test.GoldData();                  //pair for groundtruth
//        List<Pair> list1 = new ArrayList<Pair>();
//        for (i=0;i<ab.size();i++) {
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


    public static ArrayList<ArrayList<String>> DataPro() throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        try {
            //BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/cora_clean.csv"));
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/new500.csv"));
            String line = null;
            int index = 0;
            while ((line = reade.readLine()) != null) {
                if((index % 3)== 1) {
                    ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                    String author[] = line.split("<author>|</author>");
                    String title[] = line.split("<title>|</title>");
                    String journal[] = line.split("<journal>|</journal>" );
                    String date[] = line.split("<date>|</date>");
                    if (author.length == 3) {
                        String author11 = author[1].replaceAll("[\"]", ""); //remove "" and , //we keep "," in order to split words
                        // System.out.println("author: " + author11);
                        item.add(author11);
                    }
                    /*
                    else if (author.length == 4) {
                        String author1 = author[1].replaceAll("[\"]", "");
                        String author2 = author[2].replaceAll("[\"]", "");
                        String author11 = author1 + " " + author2;
                        //System.out.println("author: " + author11);
                        item.add(author11);
                    }
                     */
                    else{
                        //item.get(index).add("null");
                        item.add("");  //just initialize the value and it means null value
                    }
                    //Split journal attribute

                    if (title.length == 3) {
                        String title11 = title[1].replaceAll("[\",]", "");
                        // System.out.println("title: " + title11);
                        item.add(title11);
                    }
                    /*
                    else if(title.length==4){
                        String title1 = title[1].replaceAll("[\",]", "");
                        String title2 = title[2].replaceAll("[\",]", "");
                        String title11 = title1+" "+ title2;
                        //System.out.println("journal: " + title11);
                        item.add(title11);
                    }
                     */
                    else {
                        //item.get(index).add("null");
                        item.add("");
                    }
                    //Split journal attribute

                    if(journal.length==3) {
                        String journal11 = journal[1].replaceAll("[\",]", "");
                        //System.out.println("journal: " + journal11);
                        //item.get(index).add(journal11);
                        item.add(journal11);
                    }
                    /*
                    else if(journal.length==4){
                        String journal1 = journal[1].replaceAll("[\",]", "");
                        String journal2 = journal[2].replaceAll("[\",]", "");
                        String journal11 = journal1+" "+ journal2;
                        //System.out.println("journal: " + journal11);
                        //item.get(index).add(journal11);
                        item.add(journal11);
                    }
                     */
                    else {
                        //item.get(index).add("null");
                        item.add("");
                    }

                    if(date.length==3) {
                        String date11 = date[1].replaceAll("[\",]", "");
                        //System.out.println("journal: " + journal11);
                        //item.get(index).add(journal11);
                        item.add(date11);
                    }
                    /*
                    else if(date.length==4){
                        String date1 = date[1].replaceAll("[\",]", "");
                        String date2 = date[2].replaceAll("[\",]", "");
                        String date11 = date1+" "+ date2;
                        //System.out.println("journal: " + journal11);
                        //item.get(index).add(journal11);
                        item.add(date11);
                    }
                     */
                    else {
                        //item.get(index).add("null");
                        item.add("");
                    }
                    data.add(item); //add 1d-array into 2d-array and this is all the data
                }

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
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
                        admatrix[i][j] = Math.max(SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0)), SimFunction.Levensim(data.get(i).get(1), data.get(j).get(1)));
                        //admatrix[i][j] = Math.max(SimFunction.Jaccardsim(data.get(i).get(0), data.get(j).get(0)), SimFunction.Jaccardsim(data.get(i).get(1), data.get(j).get(1)));
                        if(admatrix[i][j]<Cluster.Parameter.threshold)
                            admatrix[i][j]=0;
                    }
                    else {
                        //admatrix[i][j] = SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0));
                        admatrix[i][j] =SimFunction.JaccardSetsim(data.get(i).get(0), data.get(j).get(0));
                        if(admatrix[i][j]<Cluster.Parameter.threshold)
                            admatrix[i][j]=0;
                    }
                }
               }
           }
        return admatrix;
    }


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

    public static double F1(List<Pair> Gold, List<Pair> Prediction)
    {
        double f1=0;
        double precision=0;
        double recall=0;
        precision= (float)intersection(Gold, Prediction).size()/Prediction.size();
        recall= (float)intersection(Gold, Prediction).size()/Gold.size();
        f1=(float)(2*precision*recall)/(precision+recall);
        return f1;
    }
}





