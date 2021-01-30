package algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class DataPro {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        double[][] temp;
        temp = ReadAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix.txt", 2);
        for (int i=0;i<temp.length;i++) {
            System.out.println(Arrays.toString(temp[i]));
        }
//        temp=new double[][]
//                {
//                        {1.0,2.0},{3.0,4.0}
//                };
//        WriteAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix.txt", temp);
    }



    // Cora data proprocessing
    public static ArrayList<ArrayList<String>> Cora() throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        try {
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/cora_clean.csv"));
            //BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Datasets/Cora/cora-master/400_1878.csv"));
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



    //Data Processing for csv file
    public static ArrayList<ArrayList<String>> CsvData(String filepath) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try {
            BufferedReader reade = new BufferedReader(new FileReader(filepath));
            String line = null;
            int index = 0;      //if we do not want to see the attributes, then skip the 1th(0) row.
            while ((line = reade.readLine()) != null) {
                if (index != 0) {
                    ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                    String attribute[] = line.split(",",12);

                    for (int i = 0; i < attribute.length; i++) {
                        item.add(attribute[i]);
                    }
                    data.add(item);
                }
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void WriteAdjMatrix(String filepath, double ad[][]) throws IOException {
        //File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix.txt");
        File file = new File(filepath);
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);

        for (int i=0;i<ad.length;i++) {
            for (int j=0;j<ad.length;j++) {
                if(j==ad.length-1)
                    pw.print(ad[i][j]);
                else
                    pw.print(ad[i][j]+",");
                pw.flush();
            }
            pw.println();
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

    public static double[][] ReadAdjMatrix(String filepath, int m) throws IOException {  //m is used to define the length of a 2-d array
        double temp[][] = new double[m][m];
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try{
            BufferedReader reade = new BufferedReader(new FileReader(filepath));
            String line = null;
            while ((line = reade.readLine()) != null) {
                ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                String item1[] = line.split(",");
                for (int i =0; i<item1.length; i++){
                    item.add(item1[i]);
                }
                data.add(item);
            }
            for (int i =0; i<data.size(); i++){
                for (int j =0; j<data.size(); j++){
                    temp[i][j]= Double.parseDouble(data.get(i).get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    public static void AdjToGraph(String filepath, double ad[][]) throws IOException {
        //File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix.txt");
        File file = new File(filepath);
        FileWriter xw = null;
        xw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(xw);


        for (int i=0;i<ad.length;i++) {
            pw.print(i+"="+0);  // the first position is the i-th vertex
            pw.flush();
            for (int j=0;j<ad.length;j++) {
                if(ad[i][j]>Cluster.Parameter.threshold) {
                    pw.print("," + j+"="+ad[i][j] );
                    pw.flush();
                }
            }
            pw.println();
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
//save the memory by not using a 2-d array i.e. ad[m][m]
    public static void AdjToGraph1(String filepath, String filepath1, int m) throws IOException {  //m is used to define the length of a 2-d array
        //File file = new File("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt");
        FileWriter xw = null;
        xw = new FileWriter(filepath1, true);
        PrintWriter pw = new PrintWriter(xw);

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try{
            BufferedReader reade = new BufferedReader(new FileReader(filepath));
            String line = null;
            int i = 0;
            while ((line = reade.readLine()) != null) {
                pw.print(i+"="+0);  // the first position is the i-th vertex
                pw.flush();
                ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                String item1[] = line.split(",");
                for (int j =0; j<item1.length; j++) {
                    if (Double.parseDouble(item1[j]) > Cluster.Parameter.threshold) {
                        pw.print("," + j + "=" + Double.parseDouble(item1[j]));
                        pw.flush();
                    }
                }
                pw.println();
                pw.flush();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Hashtable> ReadGraph(String filepath) throws IOException {  //m is used to define the length of a 2-d array
        ArrayList<Hashtable> adj = new ArrayList<Hashtable>();
        try{
            BufferedReader reade = new BufferedReader(new FileReader(filepath));
            String line = null;
            while ((line = reade.readLine()) != null) {
                adj.add(new Hashtable());
                ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                String item1[] = line.split(",");
                for (int i =item1.length-1; i>=0; i--){
                    String item2[] =item1[i].split("=");     //split a pair with =
                    adj.get(adj.size()-1).put(Integer.parseInt(item2[0]), Double.parseDouble(item2[1]));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adj;
    }



}
