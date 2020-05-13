package algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ArrayList;


public class SaveToArray {
    public static ArrayList<String> SaveArray(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        ArrayList <String> str = new ArrayList<String>();
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            //int i = 0;
            int j = 0;

            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                str.add (tempString);
                //System.out.println("line " + line + ": " + str[i]);
                line++;
                //i++;
            }

            reader.close();
        } catch (IOException e) {
            ;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return str;
    }

    public static ArrayList<ArrayList<String>> SaveArrayList(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        var list = new ArrayList<ArrayList<String>>();
        /*for (int i=0; i<3; i++)
        {
            list.add( new ArrayList<String>());
        }
         */
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            int i = 0;
            int j = 0;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                list.add( new ArrayList<String>());
                 list.get(i).add(tempString);
                //System.out.println("line " + line + ": " + str[i]);
                line++;
                i++;
            }

            reader.close();
        } catch (IOException e) {
            ;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;


    }

/*
    public static ArrayList<ArrayList<String>> SaveArrayList111(ArrayList<String> array){
        File file = new File(fileName);
        BufferedReader reader = null;
        var list = new ArrayList<ArrayList<String>>();
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            int i = 0;
            int j = 0;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                list.add( new ArrayList<String>());
                list.get(i).add(tempString);
                //System.out.println("line " + line + ": " + str[i]);
                line++;
                i++;
            }

            reader.close();
        } catch (IOException e) {
            ;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;


    }

 */


}

