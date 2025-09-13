package practice.testdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestDataCreate {

    public static void main(String[] args) {

        System.out.println("処理開始");
        long startTime = System.currentTimeMillis();

        //★★★出力先のフォルダを指定してください★★★
        String folder = "C:\\Users\\circu\\PycharmProjects\\myJavaPractice\\";

        //★★★作成するファイル名を指定してください★★★
        String nameOfOutputFileName = "testdata";

        //★★★1ファイルあたりの作成行数を指定してください★★★
        int number = 5000000;

        //★★★最初の出力ファイルの枝番を指定してください★★★
        int FileBranchNumber = 1;

        //★★★最後の出力ファイルの枝番を指定してください★★★
        int finalFileBranchNumber = 20;

        String extension = ".csv";
        int primary_key = 1;

        for (; FileBranchNumber <= finalFileBranchNumber; FileBranchNumber++) {

            String fileName = folder + nameOfOutputFileName +"_" + String.valueOf(FileBranchNumber) + extension;
            File file = new File(fileName);

            if(Files.exists(Paths.get(fileName))){
                System.out.println("※ファイルを上書きします。");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

                for (int i = 1 ; i <= number; i++,primary_key++) {

                    bw.append(String.valueOf(String.format("%08d", primary_key)));
                    bw.append(",");
                    bw.append("カラム1");
                    bw.append(",");
                    bw.append("カラム2");
                    bw.append(",");
                    bw.append("カラム3");
                    bw.append(",");
                    bw.append("カラム4");
                    bw.append(",");
                    bw.append("カラム5");
                    bw.append(",");
                    bw.append("カラム6");
                    bw.append(",");
                    bw.append("カラム7");
                    bw.append(",");
                    bw.append("カラム8");
                    bw.append(",");
                    bw.append("カラム9");
                    bw.append("\n");

                    if (i == number) {
                        System.out.print("このファイルの最後の番号は：");
                        System.out.println(primary_key);
                    }
                }

                bw.flush();
                System.out.print(Paths.get(fileName).normalize().toString());
                System.out.println("を出力しました。");
                System.out.println("------------------------");

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {

            }
        }
        System.out.println("処理終了");
        long endTime = System.currentTimeMillis();
        System.out.println("処理時間：" + (endTime - startTime) + " ms");
    }
}
