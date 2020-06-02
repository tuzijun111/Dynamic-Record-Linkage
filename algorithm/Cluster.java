package algorithm;

import java.io.*;
import java.util.*;

public class Cluster {


    static class Parameter {
        static double threshold=0.6;
    }

    static class ClusterData
    {
        ArrayList<ArrayList<Integer>> inter= new ArrayList<ArrayList<Integer>>();
        int merge; // To store the number of merge operation
        int split; // To store split
        int move; // To store move
        int loop; // To record the number of computation for DBindex
        public ClusterData(ArrayList<ArrayList<Integer>> inter, int merge, int split, int move, int loop)
        {
            this.inter = inter;
            this.merge = merge;
            this.split = split;
            this.move = move;
            this.loop = loop;
        }
    }
    //create a data structure for the parameters of clusters, e.g. intra distance (similarity) or inter distance
    //this structure will cost more memory to store these parameters but will improve efficiency
    static class ClusterParameter
    {
        double sum; //store the DBindex value
        ArrayList<Double> intra_vector;
        ArrayList<ArrayList<Double>> inter;
        public ClusterParameter(double sum, ArrayList<Double> intra_vector, ArrayList<ArrayList<Double>> inter){
            this.sum = sum;
            this.intra_vector = intra_vector;
            this.inter = inter;
        }
    }

    public static ClusterData IncrementalDB(ArrayList<ArrayList<Integer>> vector, ArrayList<Integer> array, double k, double array1[][]) throws IOException {
    //k是阈值
    //该算法检测初始分类中的每个点是否符合到该类中剩余点的平均距离小于阈值，若不小于，则拿出来作为自由点重新加入计算
    int i = 0;
    int j = 0;
    int p = 0;
    int merge = 0;
    int split = 0;
    int move = 0;
    int loop = 0;
    // write println into ChangeHistory file
    FileWriter xw = null;
    try {
        File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Incre.txt");
        //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch20.txt");
        xw = new FileWriter(file, true);
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    PrintWriter pw = new PrintWriter(xw);
    ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();  //用来存储新的schema的队列
    for (i = 0; i < array.size(); i++) {
        queue.add(new ArrayList<Integer>());
        queue.get(i).add(array.get(i));
    }
    //ClusterParameter initialization = DbindexPara1(vector, queue, array1);


    while (queue.size() > 0) {
        double temp = DBindex111(vector, queue, array1);
        boolean change = false;  //用来记录cluster是否改变
        ArrayList<Integer> b111;
        for (i = vector.size() - 1; i >= 0; i--) {          //compare queue.get(0) with vector
            if (IsConnected(queue.get(0), vector.get(i), k, array1) == false){    //we do need to judge IsConnected every time.
                continue;
            }
            else {
                loop ++;
                ArrayList <Integer> bb = new ArrayList<Integer>();  //use it to store the original cluster
                int a111 = vector.get(i).size();
                vector.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
                if (DBindex222(vector, queue, array1) < temp) {
                    for (j=0; j< a111; j++)
                        bb.add(vector.get(i).get(j));

                    System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
                    pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
                    //record when the merge happens
//                        System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
//                        +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(bb, array1)+" to "+Intra_Cluster(vector.get(i),array1));
//                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), bb, array1))
//                                +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(bb, array1))+" to "+ (1- Intra_Cluster(vector.get(i),array1)));
                    pw.flush();
                    bb.clear();
                    //temp = DBindex222(vector, queue, k, array1);
                    queue.add(vector.get(i));
                    queue.remove(0);
                    vector.remove(i);
                    change = true;
                    merge++;
                    break;
                } else {
                    removeFrom(vector.get(i), a111);      //maybe there is an existing method to do this
                }
            }
        }
        if (change == false) {
            for (i = queue.size() - 1; i > 0; i--) {          //compare queue.get(0) with queue except itself
                if (IsConnected(queue.get(0), queue.get(i), k, array1) == false)    //we do need to judge IsConnected every time.
                {
                    continue;
                } else {
                    loop ++;
                    int a11 = vector.size();
                    ArrayList <Integer> cc = new ArrayList<Integer>();
                    int a1111 = queue.get(i).size();     //记录此时长度，方便remove
                    queue.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
                    if (DBindex222(vector, queue, array1) < temp) {
                          //use it to store the original cluster
                        for (j=0; j< a1111; j++)
                            cc.add(queue.get(i).get(j));

                        System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
                        pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
//                            System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
//                                    +"  ; The intra-similarity of the new cluster change from "+ Intra_Cluster(cc, array1)+" to "+Intra_Cluster(queue.get(i),array1));
//                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i)+" ; The inter-similarity between them is: "+ (1- Inter_Cluster(queue.get(0), cc, array1))
//                                    +"  ; The intra-similarity of the new cluster change from "+ (1- Intra_Cluster(cc, array1))+" to "+ (1- Intra_Cluster(queue.get(i),array1)));
                        pw.flush();
                        cc.clear();
                        //temp = DBindex222(vector, queue, k, array1);
                        queue.add(queue.get(i));
                        queue.remove(i);
                        queue.remove(0);
                        change = true;
                        merge++;
                        break;
                    } else {
                        removeFrom(queue.get(i), a1111);      //maybe there is an existing method to do this
                    }
                }
            }
        }

        if (change == false & queue.get(0).size() > 1) {      //try to Split, if b111.size()=1, we do not need to split
            vector.add(new ArrayList<>());
            for (i=0; i< queue.get(0).size(); i++)
                vector.get(vector.size()-1).add(queue.get(0).get(i));
            //vector.add(queue.get(0));       //note that if we directly add queue.get(0), we actually are using queue.get(0), it is dynamic
            vector.add(new ArrayList<>());
            for (p = queue.get(0).size() - 1; p >= 0; p--) {
                loop++;
                vector.get(vector.size() - 1).add(vector.get(vector.size() - 2).get(0));    //add b111.get(p) to next row
                vector.get(vector.size() - 2).remove(0);   //delete b111.get(p) from the last row
                //System.out.println("Db Split "+DBindex111(vector, queue, array1));
                //System.out.println("temp "+temp);
                if (DBindex222(vector, queue, array1) < temp) {
                    System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
                    pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
//                        System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2)+" ; " +
//                                "The inter-similarity between them is: "+ (1- Inter_Cluster(vector.get(vector.size() - 1), vector.get(vector.size() - 2), array1))
//                                +"  ; The intra-similarity of the new cluster change from "+
//                                (1- Intra_Cluster(queue.get(0), array1))+" to "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1))+
//                                " and "+(1-Intra_Cluster(vector.get(vector.size() - 1),array1)));
//                        pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
                    pw.flush();
                    temp = DBindex222(vector, queue, array1);
                    change = true;
                    split++;
                } else {
                    vector.get(vector.size() - 2).add(vector.get(vector.size() - 1).get(vector.get(vector.size() - 1).size() - 1));  // else put the value into its original position.
                    vector.get(vector.size() - 1).remove(vector.get(vector.size() - 1).size() - 1);
                }
            }
            if (change == true) {
                queue.add(vector.get(vector.size() - 1));
                queue.add(vector.get(vector.size() - 2));
                vector.remove(vector.size() - 1);
                vector.remove(vector.size() - 1);
                queue.remove(0);

            } else {
                vector.remove(vector.size() - 1);
                vector.remove(vector.size() - 1);
            }
        }
/*
        if (change==false ) {    //try to move
            {
            }
        }
*/
        if (change == false){
        //if (change == false & q3==q11)
            //System.out.println("No change any more");
            vector.add(queue.get(0));
            queue.remove(0);
        }

    }
    //System.out.println("Merge= "+ merge);
    //System.out.println("Split= "+ split);
    try {
        xw.flush();
        pw.close();
        xw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return new ClusterData(vector, merge, split, move, loop);
    }




    public static ClusterData DBGreedy(ArrayList <ArrayList<Integer>> array, int k, double array1[][]) {
        int i = 0;
        int j = 0;
        int s = 0;
        int merge = 0;
        int split = 0;
        int move = 0;
        int loop = 0;
        // write println into ChangeHistory file
        FileWriter xw = null;
        try {
            File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Batch.txt");
            xw = new FileWriter(file, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(xw);
        int q1 = 0; //用来记录temp最大值所在位置
        //vector为空值的情况需要解决因为两个类的inter值无法正确计算
        int fix = array.get(k).size();
        //for (k = 0; k < array.size(); k++) {
        for (i = 0; i < fix; i++) {
            double temp = DBindex(array, array1);
            double temp111 = temp;
            if (array.get(k).size()>1) {
                int temp1 = array.get(k).get(0);
                array.get(k).remove(0);
                //增加一个新的空类，该操作对应将某个attribute分到一个新类
                //array.add(new Vector <String>());
                array.add(new ArrayList<Integer>());     //create a new empty cluster, this is like split a cluster into two cluters

                for (j = 0; j < array.size(); j++) {
                    ArrayList <Integer> cc = new ArrayList<Integer>();
                    int a111 = array.get(j).size();
                    if (j == k)
                        continue;
                    array.get(j).add(temp1); //将当前要移动的点依次加入其它类去计算DBindex值
                    if (DBindex(array, array1) < temp) {
                        move++;
                        for (s=0; s< a111; s++)
                            cc.add(array.get(j).get(s));
                        System.out.println("Move " + temp1 + " to " + cc);
                        pw.println("Move " + temp1 + " to " + cc);
                        pw.flush();
                        cc.clear();
                        temp = DBindex(array, array1);
                        q1 = j;    //需要记录最大值所在的类
                    }
                    array.get(j).remove(array.get(j).size() - 1);
                }
                if (temp == temp111) {
                    q1 = k;
                }
                if (q1 != array.size() - 1) {
                    array.remove(array.size() - 1);
                }
                array.get(q1).add(temp1); //该轮最好的位置
            }
        }
        /*
        //释放空链表的内存
        for (i = array.size()-1; i >=0; i--) {
            if (array.get(i).isEmpty())
            {
                array.remove(i);
            }
        }

         */
        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         return new ClusterData(array, merge, split, move, loop);
    }

    //this function is used to compare which cluster has been changed, so that we can efficiently compute DBindex
    public static void ChangeForDB (){

    }

    public static double DBindex(ArrayList <ArrayList<Integer>> array1, double array[][]) {
        double array2[] = new double[array1.size()]; //存储n个cluster的separation measure最大值
        Arrays.fill(array2, 0); //初始化所有元素为0
        int i = 0;
        int j = 0;
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                double temp = 0;
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            double a= Intra_Cluster(array1.get(i), array);
                            double b= Intra_Cluster(array1.get(j), array);
                            double c= Inter_Cluster(array1.get(i), array1.get(j), array);
                            if (temp < Fraction( a+b + 0.01, c + 0.001))
                            {
                                temp = Fraction(a+b + 0.01, c+ 0.001);
                            }
                        }
                    }
                }
                array2[i] = temp;
            }
        }
        double sum = 0;
        for (i = 0; i < array2.length; i++) {
            sum = sum + array2[i];
        }
        return sum;
    }

    public static double DBindex111(ArrayList <ArrayList<Integer>> array1, ArrayList<ArrayList <Integer>> queue, double array[][]) {
        double array2[] = new double[array1.size()+queue.size()]; //存储n个cluster的separation measure最大值
        Arrays.fill(array2, 0); //初始化所有元素为0
        int i = 0;
        int j = 0;
        ArrayList <ArrayList<Integer>> vectorqueue = new ArrayList <ArrayList<Integer>>();
        for (i = 0; i < queue.size(); i++) {      //move queue to array1 we start from 1 because we do not want to remove 0 as it will change its original
            array1.add(queue.get(i));                 //position due to the property of arraylist
        }
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                double temp = 0;
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            double a= Intra_Cluster(array1.get(i), array);
                            double b= Intra_Cluster(array1.get(j), array);
                            double c= Inter_Cluster(array1.get(i), array1.get(j), array);
                            if (temp < Fraction( a+b + 0.01, c + 0.001))
                            {
                                temp = Fraction(a+b + 0.01, c+ 0.001);
                            }
                        }
                    }
                }
                array2[i] = temp;
            }
        }

        double sum = 0;
        for (i = 0; i < array2.length; i++) {
            sum = sum + array2[i];
        }
        removeFrom(array1, array1.size()-queue.size());  //because DBindex111 will change the array1 even if we do not return array1

        return sum;
    }

//

    public static double DBindex222(ArrayList <ArrayList<Integer>> array1, ArrayList<ArrayList <Integer>> queue, double array[][]) {
        double array2[] = new double[array1.size()+queue.size()]; //存储n个cluster的separation measure最大值
        Arrays.fill(array2, 0); //初始化所有元素为0
        int i = 0;
        int j = 0;
        ArrayList <ArrayList<Integer>> vectorqueue = new ArrayList <ArrayList<Integer>>();
        for (i = 1; i < queue.size(); i++) {      //move queue to array1 we start from 1 because we do not want to remove 0 as it will change its original
            array1.add(queue.get(i));                 //position due to the property of arraylist
        }
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                double temp = 0;
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            double a= Intra_Cluster(array1.get(i), array);
                            double b= Intra_Cluster(array1.get(j), array);
                            double c= Inter_Cluster(array1.get(i), array1.get(j), array);
                            // if (temp < Fraction( a+b + 0.01, c + 0.001))
                            if (temp < Fraction( a+b + 0.01, c + 0.001))
                            {
                                // if (temp < Fraction( a+b + 0.01, c + 0.001))
                                temp = Fraction(a+b + 0.01, c+ 0.001);
                            }
                        }
                    }
                }
                array2[i] = temp;
            }
        }

        double sum = 0;
        for (i = 0; i < array2.length; i++) {
            sum = sum + array2[i];
        }
        removeFrom(array1, array1.size()-queue.size()+1);  //because DBindex111 will change the array1 even if we do not return array1

        return sum;
    }

    public static double Intra_Cluster(ArrayList <Integer> array1, double array[][]){
        double avg=0;
        double sum=0;
        int i=0;
        int j=0;
        if (array1.size()==1){
            avg=0;
        }
        else {
            for (i = 0; i < array1.size()-1; i++) {

                for (j = i + 1; j < array1.size(); j++) {
                    // if (i==j) continue;
                    //need to store the adjacent matrix, compute the similarity is very costly
                    //sum = sum + SimFunction.Jaccardsim(array1.get(i), array1.get(j));
                    sum = sum + array[array1.get(i)][array1.get(j)];
                }

            }
            avg = 1 - sum / (array1.size() * (array1.size() - 1) / 2);
        }
        return avg;
    }


  //this function is used to compute intra if new records are inserted
    public static double Intra_ClusterAdd(ArrayList <Integer> array1, ArrayList <Integer> array2, double array[][], double m){
        double avg=0;
        double sum=(1-m)*(array1.size() * (array1.size() - 1) / 2);
        int i=0;
        int j=0;
        for (i = 0; i < array2.size(); i++){
            array1.add(array2.get(i));
        }
        if (array1.size()==1){
            avg=0;
        }
        else {
            for (i = 0; i < array2.size(); i++) {
                for (j = 0; j < array1.size(); j++) {
                    sum = sum + array[array2.get(i)][array1.get(j)];
                }
            }
            sum = sum -array2.size();  //because the records in array2 have been compared with themselves once
            avg = 1 - sum / (array1.size() * (array1.size() - 1) / 2);
        }
        removeFrom(array1, array1.size()-array2.size()+1);
        return avg;
    }


    public static double Inter_Cluster(ArrayList <Integer> array1, ArrayList <Integer> array2, double array[][])
    {
        double avg=0;
        double sum=0;
        int i=0;
        int j=0;
        for (i=0; i<array1.size(); i++)
        {
            for (j=0; j< array2.size(); j++)
            {
                // need to store the adjacent matrix;
                //sum= sum + SimFunction.Jaccardsim(array1.get(i), array2.get(j));
                sum = sum + array[array1.get(i)][array2.get(j)];
            }
        }
        avg=1- sum/(array1.size()*array2.size());
        return avg;
    }
    // array2 is a 1d array


    public static ArrayList<Double> Bubbletsort (double array[][]) {
        //对二维数组进行排序
        ArrayList<Double> a1 = new ArrayList<Double>();  //用一维数组链表重新存数据
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j <array[0].length; j++){
                a1.add(array[i][j]);
            }
        }
        double temp=0;
        for (int i = 0; i < a1.size(); i++) {
            //外层循环，遍历次数
            for (int j = 0; j < a1.size() - i - 1; j++) {
                //内层循环，升序（如果前一个值比后一个值大，则交换）
                //内层循环一次，获取一个最大值
                if (a1.get(j) < a1.get(j + 1)) {
                    temp = a1.get(j + 1);
                    a1.set(j+1, a1.get(j));
                    a1.set(j, temp);
                }
            }
        }
        return a1;
    }



    public static double Fraction(double a, double b){
        double c= 0;
        c = a/b;
        return c;

    }

    public static double AvgSim(String s, ArrayList <String> array2)
    {
        double avg=0;
        double sum=0;
        int j=0;
            for (j=0; j< array2.size(); j++)
            {
                sum= sum + SimFunction.Jaccardsim(s, array2.get(j));
            }

        avg= sum/array2.size();
        return avg;

    }

    //判断是否连通，即判断两个类中是否有两个点的相似度大于 k

    public static boolean IsConnected( ArrayList<Integer> array1,  ArrayList<Integer> array2, double k, double array[][])
    {
        int i=0;
        int j=0;
        boolean a= false;
        labelA:
        for (i=0; i< array1.size(); i++){
            for (j=0; j< array2.size(); j++){
                if (array[array1.get(i)] [array2.get(j)]>=k )
                {
                    a=true;
                    break labelA;  //跳出多重循环
                }
            }
    }
        return a;
    }

    public static void removeFrom(ArrayList list, int pos) {
        List sublist = list.subList(pos, list.size());
        list.removeAll(sublist);
    }

    // m n 分别为数组长度，输出相似度二维矩阵,对于Jaccardsim，这里已经将大写全部转换为了小写
    public static ArrayList<ArrayList<Integer>> OneByOne(ArrayList<ArrayList<Integer>> vector1, ArrayList<Integer> array1, double k,  double array12[][]) {
        int m= vector1.size();
        int n = array1.size();
        int i=0;
        int j=0;
        int p=0;
        int q2=0;
        int q3=0;
        int q4=0;
        int merge = 0;
        int split = 0;
        int move = 0;
        // write println into ChangeHistory file
        FileWriter xw = null;
        try {
            File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-Batch.txt");
            xw = new FileWriter(file, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(xw);
        ArrayList<Integer> label= new ArrayList<Integer>();
        label=new ArrayList<Integer>(array1);  //将array1的值重新存到label中
        double matrix[] [] = new double [m][n];
        for (i=0; i< m; i++) {      //两两比较相似度
            for (j=0; j< n; j++) {
                double sum= 0;
                for (p = 0; p < vector1.get(i).size(); p++) {
                    //sum = sum + SimFunction.Jaccardsim(vector1.get(i).get(p), array1.get(j));
                    sum = sum + array12[vector1.get(i).get(p)][array1.get(j)];

                }
                matrix[i][j] = sum/vector1.get(i).size();
                //System.out.println(+i+"|"+j+"|"+matrix[i][j]);
            }
        }

        for (i=0; i< m; i++) {
            double q1 = 0;   //应该在该位置初始化m,而不是一开始
            for (j =0; j<n; j++) {
                if (matrix[i][j] > q1) {
                    q1 = matrix[i][j];
                    q2 = j;              //记录最大值的下标
                }
            }
            if (q1 > k) {
                vector1.get(i).add(array1.get(q2));
                pw.println("Move "+ array1.get(q2)+ " to "+ vector1.get(i));
                pw.flush();
                //System.out.println("Jaccardsim" + "(" + vector1.get(i)  + ") = " + q1);
                label.set(q2, null);
                //array1[q2]= String.valueOf(0);       //标记未匹配的属性值
            }

        }
        //array1中将不匹配的属性值加入vector1中
        for (j = 0; j < label.size(); j++) {
            if (label.get(j)!=null) {
                vector1.add(new ArrayList<Integer>());  //添加新的vector
                vector1.get(m + q3).add(label.get(j));
                //System.out.println("j = " + j + ". Single attribute" + vector1.get(m+q3) );
                q3++;
            }

        }

/*
        for (int l = 0; l < vector1.size(); l++) {
            System.out.println(vector1.get(l));
        }

 */
        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vector1;
    }

//Correlation Clustering
    public static double ScoreForCorr(ArrayList <ArrayList<Integer>> array1, double array[][]) {
        int i = 0;
        int j = 0;
        double sum = 0;
        double intra_sum = 0;
        double inter_sum = 0;
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                intra_sum = intra_sum + IntraForCorr(array1.get(i), array);
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            inter_sum = inter_sum + InterForCorr(array1.get(i), array1.get(j), array);
                        }
                    }
                }
            }
        }
        System.out.println("Intra_sum "+ intra_sum);
        System.out.println("Inter_sum "+inter_sum);
        sum = intra_sum + (inter_sum/2);
        return sum;
    }

    public static double IntraForCorr(ArrayList <Integer> array1, double array[][]){
        double avg=0;
        double sum=0;
        int i=0;
        int j=0;
        if (array1.size()==1){
            avg=0;
        }
        else {
            for (i = 0; i < array1.size()-1; i++) {

                for (j = i + 1; j < array1.size(); j++) {
                    // if (i==j) continue;
                    //need to store the adjacent matrix, compute the similarity is very costly
                    //sum = sum + SimFunction.Jaccardsim(array1.get(i), array1.get(j));
                    sum = sum + array[array1.get(i)][array1.get(j)];
                }

            }
            avg = (array1.size() * (array1.size() - 1) / 2) - sum;
        }
        return avg;
    }

    public static double InterForCorr(ArrayList <Integer> array1, ArrayList <Integer> array2, double array[][])
    {
        double sum=0;
        int i=0;
        int j=0;
        for (i=0; i<array1.size(); i++)
        {
            for (j=0; j< array2.size(); j++)
            {
                sum = sum + array[array1.get(i)][array2.get(j)];
            }
        }
        return sum;
    }

    public static double ScoreForCorr1(ArrayList <ArrayList<Integer>> array1, ArrayList<ArrayList <Integer>> queue, double array[][]) {
        int i = 0;
        int j = 0;
        double sum = 0;
        double intra_sum = 0;
        double inter_sum = 0;
        ArrayList <ArrayList<Integer>> vectorqueue = new ArrayList <ArrayList<Integer>>();
        for (i = 0; i < queue.size(); i++) {      //move queue to array1 we start from 1 because we do not want to remove 0 as it will change its original
            array1.add(queue.get(i));                 //position due to the property of arraylist
        }
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                double temp = 0;
                intra_sum = intra_sum + IntraForCorr(array1.get(i), array);
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            inter_sum = inter_sum + InterForCorr(array1.get(i), array1.get(j), array);
                        }
                    }
                }
            }
        }
        sum = intra_sum + inter_sum/2;
        removeFrom(array1, array1.size()-queue.size());  //because DBindex111 will change the array1 even if we do not return array1
        return sum;
    }

//

    public static double ScoreForCorr2(ArrayList <ArrayList<Integer>> array1, ArrayList<ArrayList <Integer>> queue, double array[][]) {
        int i = 0;
        int j = 0;
        double sum = 0;
        double intra_sum = 0;
        double inter_sum = 0;
        ArrayList <ArrayList<Integer>> vectorqueue = new ArrayList <ArrayList<Integer>>();
        for (i = 1; i < queue.size(); i++) {      //move queue to array1 we start from 1 because we do not want to remove 0 as it will change its original
            array1.add(queue.get(i));                 //position due to the property of arraylist
        }
        //遇到空类直接continue跳过
        for (i = 0; i < array1.size(); i++) {
            if (array1.get(i).isEmpty())
                continue;
            else {
                intra_sum = intra_sum + IntraForCorr(array1.get(i), array);
                for (j = 0; j < array1.size(); j++) {
                    if (j == i) continue;
                    else {
                        if (array1.get(j).isEmpty())
                            continue;
                        else {
                            inter_sum = inter_sum + InterForCorr(array1.get(i), array1.get(j), array);
                        }
                    }
                }
            }
        }

        sum = intra_sum + (inter_sum/2);
        removeFrom(array1, array1.size()-queue.size()+1);  //because DBindex111 will change the array1 even if we do not return array1
        return sum;
    }



    public static ClusterData IncreForCorr(ArrayList<ArrayList<Integer>> vector, ArrayList<Integer> array, double k, double array1[][]) throws IOException {
        int i = 0;
        int j = 0;
        int p = 0;
        int merge = 0;
        int split = 0;
        int move = 0;
        int loop = 0;
        // write println into ChangeHistory file
        FileWriter xw = null;
        try {
            File file = new File("/Users/binbingu/Documents/Codes/Write-test/Change-IncreForCorr.txt");
            //File file = new File("/Users/binbingu/Documents/Codes/Write-test/History_batch20.txt");
            xw = new FileWriter(file, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(xw);
        ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();  //用来存储新的schema的队列
        for (i = 0; i < array.size(); i++) {
            queue.add(new ArrayList<Integer>());
            queue.get(i).add(array.get(i));
        }
        System.out.println("1 "+vector);
        System.out.println("2 "+queue);

        while (queue.size() > 0) {
            double temp = ScoreForCorr1(vector, queue, array1);
            System.out.println("3 "+temp);
            boolean change = false;  //用来记录cluster是否改变
            ArrayList<Integer> b111;
            for (i = vector.size() - 1; i >= 0; i--) {          //compare queue.get(0) with vector
                if (IsConnected(queue.get(0), vector.get(i), k, array1) == false){    //we do need to judge IsConnected every time.
                    continue;
                }
                else {
                    loop ++;
                    ArrayList <Integer> bb = new ArrayList<Integer>();  //use it to store the original cluster
                    int a111 = vector.get(i).size();
                    vector.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
                    System.out.println(vector);
                    System.out.println(queue);
                    System.out.println("4 "+ScoreForCorr2(vector, queue, array1));
                    System.out.println("5 "+temp);
                    System.out.println("6 "+array1[0][2]);
                    if (ScoreForCorr2(vector, queue, array1) < temp) {
                        for (j=0; j< a111; j++)
                            bb.add(vector.get(i).get(j));

                        System.out.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
                        pw.println(queue.get(0)+ " Merge With " +bb + " into "+ vector.get(i));
                        pw.flush();
                        bb.clear();
                        queue.add(vector.get(i));
                        queue.remove(0);
                        vector.remove(i);
                        change = true;
                        merge++;
                        break;
                    }
                    else {
                        removeFrom(vector.get(i), a111);      //maybe there is an existing method to do this
                    }
                }
            }
            if (change == false) {
                for (i = queue.size() - 1; i > 0; i--) {          //compare queue.get(0) with queue except itself
                    if (IsConnected(queue.get(0), queue.get(i), k, array1) == false)    //we do need to judge IsConnected every time.
                    {
                        continue;
                    } else {
                        loop ++;
                        int a11 = vector.size();
                        ArrayList <Integer> cc = new ArrayList<Integer>();
                        int a1111 = queue.get(i).size();     //记录此时长度，方便remove
                        queue.get(i).addAll(queue.get(0));         //try to Merge   ;   add the arraylist b111 i.e. the first arraylist in queue
                        if (ScoreForCorr2(vector, queue, array1) < temp) {
                            //use it to store the original cluster
                            for (j=0; j< a1111; j++)
                                cc.add(queue.get(i).get(j));

                            System.out.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
                            pw.println(queue.get(0)+ " Merge With " +cc + " into "+ queue.get(i));
                            pw.flush();
                            cc.clear();
                            queue.add(queue.get(i));
                            queue.remove(i);
                            queue.remove(0);
                            change = true;
                            merge++;
                            break;
                        } else {
                            removeFrom(queue.get(i), a1111);      //maybe there is an existing method to do this
                        }
                    }
                }
            }

            if (change == false & queue.get(0).size() > 1) {      //try to Split, if b111.size()=1, we do not need to split
                vector.add(new ArrayList<>());
                for (i=0; i< queue.get(0).size(); i++)
                    vector.get(vector.size()-1).add(queue.get(0).get(i));
                //vector.add(queue.get(0));       //note that if we directly add queue.get(0), we actually are using queue.get(0), it is dynamic
                vector.add(new ArrayList<>());
                for (p = queue.get(0).size() - 1; p >= 0; p--) {
                    loop++;
                    vector.get(vector.size() - 1).add(vector.get(vector.size() - 2).get(0));    //add b111.get(p) to next row
                    vector.get(vector.size() - 2).remove(0);   //delete b111.get(p) from the last row
                    //System.out.println("Db Split "+DBindex111(vector, queue, array1));
                    //System.out.println("temp "+temp);
                    if (ScoreForCorr2(vector, queue, array1) < temp) {
                        System.out.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
                        pw.println(queue.get(0)+ " Split into "+ vector.get(vector.size() - 1)+ " and " + vector.get(vector.size() - 2));
                        pw.flush();
                        temp = ScoreForCorr2(vector, queue, array1);
                        change = true;
                        split++;
                    } else {
                        vector.get(vector.size() - 2).add(vector.get(vector.size() - 1).get(vector.get(vector.size() - 1).size() - 1));  // else put the value into its original position.
                        vector.get(vector.size() - 1).remove(vector.get(vector.size() - 1).size() - 1);
                    }
                }
                if (change == true) {
                    queue.add(vector.get(vector.size() - 1));
                    queue.add(vector.get(vector.size() - 2));
                    vector.remove(vector.size() - 1);
                    vector.remove(vector.size() - 1);
                    queue.remove(0);

                } else {
                    vector.remove(vector.size() - 1);
                    vector.remove(vector.size() - 1);
                }
            }
/*
            if (change==false ) {    //try to move
                {
                }
            }
 */
            if (change == false){
                vector.add(queue.get(0));
                queue.remove(0);
            }

        }
        try {
            xw.flush();
            pw.close();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ClusterData(vector, merge, split, move, loop);
    }


}




