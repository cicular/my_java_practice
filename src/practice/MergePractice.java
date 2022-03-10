
package practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * ファイルのマージを行う
 */
public class MergePractice {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        File directory = new File("入力ファイルのディレクトリ");
                // 入力ファイル一覧取得
        File[] filesArray = directory.listFiles();

        List<File> filesList = new ArrayList<File>();

        if(filesArray == null) return;

        // ファイルサイズ0バイト以外のものを処理対象にする
        for(File file:filesArray) {
            if(file.isFile() && file.length() != 0L) {
                filesList.add(file);
            }
        }

        // マージファイル
        File mergeFile = new File("出力ファイル");

        Path path = Paths.get(mergeFile.getAbsolutePath());

        // マージファイルが既にある場合は削除
        if(Files.exists(path)){
            try {
                Files.delete(path);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

        // 読込みと書込み
        for(File readfile :filesList) {
            System.out.println(readfile.getName());
            try(BufferedReader br = new BufferedReader(new FileReader(readfile))){
                // trueを指定することで追記モードにする
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(mergeFile,true))) {
                    String line;
                    while((line = br.readLine()) != null) {
                        if(checkLine(line)) {
                            bw.write(line);
                            bw.newLine();
                        }else {
                            System.out.println("エラー行がありました。:" + line);
                            bw.close();
                            return;
                        }
                    }
                    bw.flush();
                }catch(IOException ex) {
                    ex.printStackTrace();
                }
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("処理時間：" + (endTime - startTime) + " ms");

    }

        /*
         * ファイル内容の単項目チェックを行う
         */
    private static boolean checkLine(String line) {

        String[] strArray = line.split(",");

                // 以下は一例
        if(strArray.length != 2) {
            return false;
        }

        if(strArray[1].length() != 3) {
            return false;
        }

        return true;
    }
}
