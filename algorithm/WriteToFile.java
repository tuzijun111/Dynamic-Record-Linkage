package algorithm;

import java.io.*;

public class WriteToFile {

    public static void Print_Write() {
        PrintWriter pw = null;
        int a = 2;
        try {
            File file = new File("/Users/binbingu/Documents/Codes/Write-test/ChangeHistory.txt");
            pw = new PrintWriter(file);
            pw.println("destroy "+ a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pw.close();
    }

    public static void main(String[] args) throws Exception {
//读取a.txt
        File pathFile = new File("/Users/binbingu/Documents/Codes/Write-test/input.txt");//可使用绝对或相对路径
        if(pathFile.isFile() && pathFile.exists()){
            FileInputStream is = new FileInputStream(pathFile);
            InputStreamReader isReader = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isReader);
// BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFile),"utf-8"));
            File writeFl = new File("/Users/binbingu/Documents/Codes/Write-test/output.txt");
            if (writeFl.exists()) {  //if the file exists, then delete it and then create it so that we can get a null file every time
                writeFl.delete();
            }

            writeFl.createNewFile();//创建新文件
//            if (!writeFl.exists()) {
//                writeFl.createNewFile();
//            }
            FileOutputStream fs = new FileOutputStream(writeFl,true);//true设置为追加内容 默认false;
            OutputStreamWriter ow = new OutputStreamWriter(fs,"utf-8");
            BufferedWriter bw= new BufferedWriter(ow);
// BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFl,true)));

            StringBuffer nb = new StringBuffer();
            String lineTxt = null;
            while((lineTxt=br.readLine())!=null){
                StringBuffer sb = new StringBuffer();
                sb.append(lineTxt + "\n");//"\n"读取换行
//写入
                bw.write(sb.toString());//   +"\r" 写入换行 else the same with the original file
            }
            System.out.println(nb.toString());



            isReader.close();
            br.close();
            bw.flush();
            bw.close();

        }else{
            throw new Exception("文件不存在");
        }


    }



//    public static void main(String args[]) {
//        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
//
//            /* 读入TXT文件 */
//            String pathname = "/Users/binbingu/Documents/Codes/Write-test/input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
//            File filename = new File(pathname); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(filename)); // 建立一个输入流对象reader
//            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
//            String line = "";
//            line = br.readLine();
//            ArrayList<String> data = new ArrayList<String>();
//            while (line != null) {
//                line = br.readLine(); // 一次读入一行数据
//                data.add(line);
//            }
//
//            /* 写入Txt文件 */
//            File writename = new File("/Users/binbingu/Documents/Codes/Write-test/output.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
//            writename.createNewFile(); // 创建新文件
//            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//            for (int i=0; i< data.size(); i++)
//                out.write("\r\n");
//
//            out.write("我会写入文件啦\r\n"); // \r\n即为换行
//            out.flush(); // 把缓存区内容压入文件
//            out.close(); // 最后记得关闭文件
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



}
