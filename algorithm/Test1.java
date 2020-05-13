package algorithm;

import java.io.*;
import java.util.*;


public class Test1 {


    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> a1 = new ArrayList<ArrayList<Integer>>();
        a1.add(new ArrayList<Integer>());
        a1.add(new ArrayList<Integer>());
        a1.add(new ArrayList<Integer>());
        a1.get(0).add(1);
        a1.get(0).add(2);
        a1.get(0).add(3);
        a1.get(1).add(4);
        a1.get(1).add(5);
        a1.get(2).add(6);

        for (int i = 0; i < a1.size(); i++)
            System.out.println(a1.get(i));

        ArrayList<ArrayList<Integer>> b1 = new ArrayList<ArrayList<Integer>>();
        b1.add(new ArrayList<Integer>());
        b1.add(new ArrayList<Integer>());
        b1.add(new ArrayList<Integer>());
        b1.add(new ArrayList<Integer>());
        b1.get(0).add(1);
        b1.get(0).add(2);
        b1.get(1).add(3);
        b1.get(1).add(4);
        b1.get(1).add(5);
        b1.get(1).add(6);
        b1.get(2).add(7);
        b1.get(2).add(8);
        b1.get(2).add(9);
        b1.get(2).add(10);
        b1.get(3).add(11);
        b1.get(3).add(12);

        for (int i = 0; i < b1.size(); i++)
            System.out.println(b1.get(i));

        ArrayList<ArrayList<Integer>> c1 = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < a1.size(); i++) {
            for (int j = 0; j < b1.size(); j++){
                if (Exist(a1.get(i), b1.get(j)) == true) {
                    Integer temp1[] = new Integer[a1.get(i).size()];
                    Integer temp2[] = new Integer[b1.get(j).size()];
                    for (int l = 0; l<temp1.length; l++){
                        temp1[l] = a1.get(i).get(l);
                    }
                    for (int l = 0; l<temp2.length; l++){
                        temp2[l] = b1.get(j).get(l);
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
        for (int i = 0; i < c1.size(); i++)
             System.out.println(c1.get(i));

    }


    public static boolean Exist(ArrayList<Integer> a, ArrayList<Integer> b){
        boolean s = false;
        labe1A:
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.get(j) == a.get(i)) {
                    s = true;
                    break labe1A;
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


}



