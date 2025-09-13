package practice.testdata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestDataParallelCreate {
    
    public static void main(String[] args) {
        
        System.out.println("処理開始");
        final long startTime = System.currentTimeMillis();

        //★★★出力先のフォルダを指定してください★★★
        final String folder = "C:\\Users\\circu\\PycharmProjects\\myJavaPractice\\";

        //★★★作成するファイル名を指定してください★★★
        final String nameOfOutputFileName = "testdata";

        //★★★1ファイルあたりの作成行数を指定してください★★★
        final int number = 1000000;

        //★★★最初の出力ファイルの枝番を指定してください★★★
        int fileBranchNumber = 1;

        //★★★最後の出力ファイルの枝番を指定してください★★★
        int finalFileBranchNumber = 30;

        // ファイル拡張子
        final String extension = ".csv";
        // int primary_key = 1;

        ExecutorService exec = Executors.newFixedThreadPool(finalFileBranchNumber);
        for (; fileBranchNumber <= finalFileBranchNumber; fileBranchNumber++) {
            exec.submit(new TestDataCreateThread(folder, nameOfOutputFileName, extension, fileBranchNumber, number));
            System.out.println("スレッド呼び出し終わり");
        }

        exec.shutdown();

        // System.out.println("処理終了");
        // long endTime = System.currentTimeMillis();
        // System.out.println("処理時間：" + (endTime - startTime) + " ms");
    }
}
