package practice.testdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class TestDataCreateThread implements Runnable {

    String folder;
    String nameOfOutputFileName;
    String extension;
    int fileBranchNumber;
    int number;

    public TestDataCreateThread(String folder, String nameOfOutputFileName, 
        String extension, int fileBranchNumber, int number) {
        this.folder = folder;
        this.nameOfOutputFileName = nameOfOutputFileName;
        this.extension = extension;
        this.fileBranchNumber = fileBranchNumber;
        this.number = number;
    }

    @Override
    public void run() {
        // スレッドIDを出力する
        System.out.println(Thread.currentThread().getId());

        String fileName = folder + nameOfOutputFileName +"_" + String.valueOf(fileBranchNumber) + extension;
        File file = new File(fileName);

        if(Files.exists(Paths.get(fileName))){
            System.out.println("※ファイルを上書きします。");
        }

        int primary_key = fileBranchNumber == 1 ? 1 : (fileBranchNumber - 1) * number + 1; 

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

                for (int i = 1 ; i <= number; i++, primary_key++) {

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
                        System.out.print("このファイルの最後の番号は：" + String.valueOf(primary_key) + " ");
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
}
