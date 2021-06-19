package algorithm;

import java.io.IOException;
import java.util.ArrayList;

public class Experiment {
    public static void DBSCAN_Pro(ArrayList<ArrayList<String>> abc, double ad[][]) throws IOException {
        // For DBSCAN with Euclid distance
        //DataPro.WriteAdjMatrix("/Users/binbingu/Documents/Datasets/amazon-samples/Matrix/AdjMatrix100.txt", ad);
        ad = DataPro.ReadAdjMatrix("/Users/binbingu/Documents/Datasets/amazon-samples/Matrix/AdjMatrix100.txt", abc.size());
        String s1= "/Users/binbingu/Documents/Datasets/amazon-samples/Matrix/AdjMatrix100.txt";
        String s2= "/Users/binbingu/Documents/Datasets/amazon-samples/Matrix/AdjGraph100.txt";
        DataPro.AdjToGraph1(s1, s2, abc.size());
    }


    public static void Music_Pro(ArrayList<ArrayList<String>> abc) throws IOException, ClassNotFoundException {
        //        For Music
        AdjMatrix.AttributeAndCount result = AdjMatrix.WeigthForMusic(AdjMatrix.CosineNgram());
        ArrayList<ArrayList<String>> gramarray = result.gramarray;
        ArrayList<ArrayList<Double>> weight = result.weight;


        //write the adjacent matrix into a txt file
        double ad[][] = AdjMatrix.MusicAdj(gramarray, weight);
        DataPro.WriteAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt", ad);
        DataPro.WriteAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix1K.txt", ad);

        //read the adjacent matrix from a txt file
        ad = DataPro.ReadAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt", abc.size());  //19375 for 20K
        ad = DataPro.ReadAdjMatrix("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix1K.txt", abc.size());
        DataPro.AdjToGraph("/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt", ad);
        String s1 = "/Users/binbingu/Documents/Datasets/Music Brzinz/AdjMatrix20K.txt";
        String s2 = "/Users/binbingu/Documents/Datasets/Music Brzinz/AdjGraph20K.txt";
        DataPro.AdjToGraph1(s1, s2, abc.size());


    }


}
