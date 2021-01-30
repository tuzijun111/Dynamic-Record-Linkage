package algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdjMatrix {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        AttributeAndCount result = WeigthForMusic(AdjMatrix.CosineNgram());
        ArrayList<ArrayList<String>> gramarray = result.gramarray;
        ArrayList<ArrayList<Double>> weight = result.weight;
        double ad[][] = new double[gramarray.size()][gramarray.size()];

        //ad = MusicAdj(gramarray, weight);
//        for (int i=0;i<ad.length;i++) {
//            System.out.println(Arrays.toString(ad[i]));
//        }
//        for (int i=0;i<ad.length;i++) {
//            for (int j=0;j<ad.length;j++) {
//                if(ad[i][j]>1)
//                System.out.println(i+ " "+" "+ j+ " "+ ad[i][j]);
//            }
//        }


//        for (int i = 0; i< gramarray.size(); i++){
//            for (int j = 0; j< gramarray.size(); j++){
//                if(j==i)
//                    continue;
//                if(i==42&&j==167)
//                    System.out.println(i+" "+j+" is "+ SimFunction.CosineForMusic(gramarray.get(i), gramarray.get(j), weight.get(i), weight.get(j)));
//            }
//        }

    }

    static class AttributeAndCount {
        ArrayList<ArrayList<String>> gramarray = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Double>> weight = new ArrayList<ArrayList<Double>>();

        public AttributeAndCount(ArrayList<ArrayList<String>> gramarray, ArrayList<ArrayList<Double>> weight)
        {
            this.gramarray = gramarray;
            this.weight = weight;
        }
    }


    public static double[][] MusicAdj(ArrayList<ArrayList<String>> gramarray, ArrayList<ArrayList<Double>> weight) throws IOException, ClassNotFoundException {
//    artist+title+album
        double admatrix[][] = new double[gramarray.size()][gramarray.size()];
        ArrayList<ArrayList<Integer>> block = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> dict = new ArrayList<String>();
        for (int i = 0; i< gramarray.size(); i++){
            for (int j = 0; j< gramarray.get(i).size(); j++) {
                if (gramarray.get(i).get(j).matches(".* .*")&&(!IsIn(gramarray.get(i).get(j), dict))){
                    block.add(new ArrayList<Integer>());
                    dict.add(gramarray.get(i).get(j));
                    block.get(block.size()-1).add(i);
                }
                else {
                    int p = IsInPosition(gramarray.get(i).get(j), dict);
                    if (p != -1) {
                        block.get(p).add(i);
                    }
                }
            }
        }

        //assign the records into different blocks

        //records only compare with the other records in the same block
        for (int i = 0; i< block.size(); i++){
            for (int j = 0; j< block.get(i).size(); j++){
                for (int k = j+1; k< block.get(i).size(); k++){
                    int x11 = block.get(i).get(j);
                    int x12 = block.get(i).get(k);
                    double x1 = SimFunction.CosineForMusic(gramarray.get(x11), gramarray.get(x12), weight.get(x11), weight.get(x12));
                    admatrix[x11][x12] = x1;
                }
            }
        }

//        //fully comparison
//        for (int i = 0; i< gramarray.size(); i++){
//            for (int j = i; j< gramarray.size(); j++){
//                if(j==i){
//                    admatrix[i][j]=0;
//                    continue;
//                }
//                double x1 = SimFunction.CosineForMusic(gramarray.get(i), gramarray.get(j), weight.get(i), weight.get(j));
//                admatrix[i][j] = x1;
//            }
//        }
        //let ad[i][j]=a[j][i]
        for (int i = 0; i< gramarray.size(); i++){
            for (int j = 0; j<i; j++){
                admatrix[i][j] = admatrix[j][i];
            }
        }

        return admatrix;
    }

    //public static double CosineNgram(String stri1, String stri2, int n) throws IOException { //n=3 for trigram
    public static ArrayList<ArrayList<String>> CosineNgram() throws IOException { //n=3 for trigram
        double sim = 0;
        ArrayList<ArrayList<String>> abc = new ArrayList<ArrayList<String>>();
        abc = DataPro.CsvData("/Users/binbingu/Documents/Datasets/Music Brzinz/20K.csv");
        ArrayList<ArrayList<String>> item = new ArrayList<ArrayList<String>>();
        for (int i=0;i<abc.size();i++) {
            //if(i==12||i==13||i==14||i==15||i==16||i==17||i==161) {
                item.add(new ArrayList<String>());
                String s = abc.get(i).get(8) + " " + abc.get(i).get(6) + " " + abc.get(i).get(9);//artist + title + album
                String gram[] = s.split(" ");
                for (int j = 0; j < gram.length; j++) {
                    if (!gram[j].isEmpty()) {
                        String lower = gram[j].toLowerCase();  //translate all strings to lower case
                        item.get(item.size() - 1).add(lower);
                    }
                }
            //}

        }
        ArrayList<ArrayList<String>> gramarray = new ArrayList<ArrayList<String>>();
        for (int i=0;i<item.size();i++) {
            gramarray.add(new ArrayList<String>());
            for (int j = 0; j < item.get(i).size(); j++){
                String s1 = item.get(i).get(j);
                if(s1.length()<=3) {
                    gramarray.get(gramarray.size() - 1).add(s1 + "!");   //The first trigram produced from each term is marked at the right end by the addition of the symbol `!'
                    gramarray.get(gramarray.size() - 1).add(s1.substring(0,1)+"#");     // the first letter of the term is marked by adding the character `#' to the right and included as an attribute
                    gramarray.get(gramarray.size() - 1).add(s1);
                }
                else {
                    //if (k==0)  //The first trigram produced from each term derived from STR is marked at the right end by the addition of the symbol `!'
                    gramarray.get(gramarray.size() - 1).add(s1.substring(0, 3)+"!");
                    gramarray.get(gramarray.size() - 1).add(s1.substring(0, 1)+"#");
                    for (int k = 0; k <= s1.length() - 3; k++) {
                        gramarray.get(gramarray.size() - 1).add(s1.substring(k, k + 3));

                    }
                }
            }
            for (int j = 0; j < item.get(i).size()-1; j++){
                String s11 = item.get(i).get(j);
                String s12 = item.get(i).get(j+1);
                gramarray.get(gramarray.size() - 1).add(s11.substring(0,1)+" "+s12.substring(0,1));
            }

        }
//        for (int i =0; i<gramarray.size(); i++) {
//            System.out.println(gramarray.get(i));
//            System.out.println(gramarray.get(i).size());
//        }

        //All attributes are given global weights of the form sqrt(log(N/n_t)). These are relatively standard inverse document frequency weights.
        // Here N represents the collection size, which in this case is the number of phrases in the set we are studying. The value nt is the frequency of occurrence throughout the collection of the attribute being weighted.
        //Each attribute is also given a local weight which is log(1+f_t) where ft is the number of times the attribute is seen in the particular phrase where the local weight is to be applied.

        return gramarray;

    }

    public static AttributeAndCount WeigthForMusic(ArrayList<ArrayList<String>> gramarray){
        //count the number of attributes for each phrase (i.e. gramarray.get(i)) and remove duplicates
        ArrayList<ArrayList<Integer>> count = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < gramarray.size(); i++){
            count.add(new ArrayList<Integer>());
            for (int j = 0; j < gramarray.get(i).size(); j++){
                int temp =0;
                if(gramarray.get(i).get(j).matches(".*!$")){   //symbol `!' and the result is included as an attribute with a local count of 2.
                    temp =2;
                }
                else {
                    temp = 1; //the initial number of attributes in gramarray
                }
                for (int k = j+1; k < gramarray.get(i).size(); k++){
                    if(gramarray.get(i).get(k).equals(gramarray.get(i).get(j))){
                        gramarray.get(i).remove(k);  //remove duplicates, count+1
                        temp++;        //but if we find more strings which are the same with , we still +1 instead of +2
                        k--;
                    }
                }
                count.get(count.size()-1).add(temp);
            }
        }
//        for(int i = 0; i< count.size(); i++){
//            System.out.println(count.get(i));
//            System.out.println(count.get(i).size());
//        }

        //now we get gramarray without duplicates for a phrase (or a line i.e. gramarray.get(i))
        ArrayList<ArrayList<Integer>> glocount = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> checked = new ArrayList<String>();
        ArrayList<Integer> checkedcount = new ArrayList<Integer>();
        for (int i = 0; i < gramarray.size(); i++){
            glocount.add(new ArrayList<Integer>());
            for (int j = 0; j < gramarray.get(i).size(); j++){
                int globalcount = 1;
                int x = IsInCount(gramarray.get(i).get(j), checked, checkedcount); //judge if a phrase contains an specific attribute
                if(x!=0){
                    glocount.get(glocount.size()-1).add(x);
                    continue;
                }
                for (int k = i+1; k < gramarray.size(); k++){
                    if(IsIn(gramarray.get(i).get(j), gramarray.get(k))) {
                        globalcount++;
                        continue;
                    }
                }
                glocount.get(glocount.size()-1).add(globalcount);   //store global count, the arraylist is the same with the new gramarray
                checked.add(gramarray.get(i).get(j)); //store the attributes which have been checked
                checkedcount.add(globalcount); //store the counts of values which have been checked
            }
        }

//        for(int i = 0; i< glocount.size(); i++){
//            System.out.println(glocount.get(i));
//            System.out.println(glocount.get(i).size());
//        }
        ArrayList<ArrayList<Double>> weight = new ArrayList<ArrayList<Double>>();  //store the final weight for each attribute
        for(int i = 0; i< glocount.size(); i++){
            weight.add(new ArrayList<Double>());
            for(int j = 0; j< glocount.get(i).size(); j++) {
                double m1 = log(1 + count.get(i).get(j), 10)* Math.sqrt(log(glocount.size()/glocount.get(i).get(j), 10));
                weight.get(weight.size()-1).add(m1);
            }
        }
//        for(int i = 0; i< weight.size(); i++){
//            System.out.println(weight.get(i));
//            System.out.println(weight.get(i).size());
//        }
        return new AttributeAndCount(gramarray, weight);
    }

    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
     }



    public static boolean IsIn(String s, ArrayList<String> array){
        boolean x = false;
        for(int i=0; i<array.size(); i++){
            if (array.get(i).equals(s)){
                x = true;
                break;
            }
        }
        return x;
    }
    public static int IsInPosition(String s, ArrayList<String> array){
        int x = -1;
        for(int i=0; i<array.size(); i++){
            if (array.get(i).equals(s)){
                x = i;
                break;
            }
        }
        return x;
    }

    public static int IsInCount(String s, ArrayList<String> array, ArrayList<Integer> checkedcount){
        int x = 0;
        for(int i=0; i<array.size(); i++){
            if (array.get(i).equals(s)){
                x = checkedcount.get(i);
                break;
            }

        }
        return x;
    }




    public static double[][] SyntheticAdj(ArrayList<ArrayList<String>> data) {
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
                //admatrix[i][j] = (a[1]+a[2]+ a[3])/3;
                admatrix[i][j] = (a[2]+ a[3])/2;
                //admatrix[i][j] = a[3];

            }
        }
        return admatrix;
    }
}
