package algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Synthetic {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        abc= DataPro10000();

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
        double ad[][] = SyntheticAdj(abc);
        for (i =0; i<ad.length; i++){
            System.out.println(Arrays.toString(ad[i]) +" ");
        }



//        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/Adjacent-matrix1000.txt");
//        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/Synthetic/History_batch50.txt");
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
        System.out.println("Current Score: "+ Cluster.ScoreForCorr(inter, ad));
        //System.out.println("Current DBindex: "+ Cluster.DBindex(inter, ad));

    }


    public static ArrayList<ArrayList<String>> DataPro10000() throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try {
            BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Tool/febrl-0.4.2/dsgen/1000.csv"));
            //BufferedReader reade = new BufferedReader(new FileReader("/Users/binbingu/Documents/Tool/febrl-0.4.2/dsgen/10000.csv"));
            String line = null;
            int index = 0;      //if we do not want to see the attributes, then skip the 1th(0) row.
            while ((line = reade.readLine()) != null) {
                if (index != 0) {
                ArrayList<String> item = new ArrayList<String>(); //store the attribute value by 1-d arraylist
                String attribute[] = line.split(",");

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


    public static double[][] SyntheticAdj(ArrayList<ArrayList<String>> data)
    {
        //blocking with the number of words, partition items
        double admatrix[][] = new double[data.size()][data.size()];
        //rec_id (0),  culture (1),  sex(2),  age(3),  date_of_birth(4),  title(5),  given_name(6),  surname(7),
        // state(8),  suburb(9),  postcode(10),  street_number(11),  address_1(12),  address_2(13),  phone_number(14),
        // soc_sec_id(15),  blocking_number(16),  family_role(17)
        for (int i=0;i<data.size();i++){
            for (int j=0;j<data.size();j++) {
                if (j==i){
                    admatrix[i][j]=0;
                    continue;                   //we do not want to compare the same item
                }
                double x1 = SimFunction.JaccardForSyn(data.get(i).get(4), data.get(j).get(4)); //data_of_brith
                //double x2 = SimFunction.JaccardForSyn(data.get(i).get(5), data.get(j).get(5)); //title
                double x3 = SimFunction.JaccardForSyn(data.get(i).get(6), data.get(j).get(6)); //given_name
                double x4 = SimFunction.JaccardForSyn(data.get(i).get(7), data.get(j).get(7)); //surname
                //double x5 = SimFunction.JaccardForSyn(data.get(i).get(12), data.get(j).get(12)); //address_1
                double x6 = SimFunction.JaccardForSyn(data.get(i).get(15), data.get(j).get(15)); //soc_sec_id
                double a[] = new double[4];
                a[0] = x1;
                a[1] = x3;
                a[2] = x4;
                a[3] = x6;
                Arrays.sort(a);
                admatrix[i][j] = (a[2]+ a[3])/2;
                //admatrix[i][j] = a[3];

            }
        }
        return admatrix;
    }
}
