package practice.fileCopy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

public class CopyXlsxFiles {

    private static final String SETTINGS_FILE_NAME = "settings.txt";

    public static void main(String[] args) {
        // settings.txt からコピー元／先を読み込み
        Settings settings = loadSettings(Paths.get(SETTINGS_FILE_NAME));
        if (settings == null) {
            // エラーメッセージは loadSettings 内で出力済み
            return;
        }

        Path folder1 = settings.copySource();
        Path folder2 = settings.copyDestination();

        if (!Files.isDirectory(folder1)) {
            System.out.println("フォルダ1がディレクトリではありません: " + folder1.toAbsolutePath());
            return;
        }
        if (!Files.isDirectory(folder2)) {
            System.out.println("フォルダ2がディレクトリではありません: " + folder2.toAbsolutePath());
            return;
        }

        try {
            // フォルダ2配下の「ファイル名 → Path の一覧」を作成
            Map<String, List<Path>> folder2Index = buildFileIndex(folder2);

            // フォルダ1配下の対象ファイルを走査・コピー
            processFolder(folder1, folder2Index);
        } catch (IOException e) {
            System.out.println("[ERROR] 全体処理で例外が発生しました: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /**
     * settings.txt を読み込み、コピー元／先を取得する。
     *
     * 期待する形式:
     *   Copy_source:C:\Users\circu\Downloads\テスト1
     *   Copy_destination:C:\Users\circu\Downloads\テスト2
     */
    private static Settings loadSettings(Path settingsPath) {
        if (!Files.exists(settingsPath)) {
            System.out.println("[ERROR] 設定ファイルが見つかりません: " + settingsPath.toAbsolutePath());
            return null;
        }

        String sourceStr = null;
        String destStr = null;

        try {
            for (String line : Files.readAllLines(settingsPath)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // 空行・コメント行はスキップ
                }

                int idx = line.indexOf(':');
                if (idx <= 0) {
                    System.out.println("[WARN] 不正な行をスキップします: " + line);
                    continue;
                }

                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim(); // ここに C:\Users\... が入る

                switch (key) {
                    case "Copy_source" -> sourceStr = value;
                    case "Copy_destination" -> destStr = value;
                    default ->
                        System.out.println("[WARN] 未知のキーをスキップします: " + key);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] 設定ファイルの読み込みに失敗しました: " + e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }

        if (sourceStr == null || destStr == null) {
            System.out.println("[ERROR] 設定ファイルに Copy_source または Copy_destination が不足しています。");
            return null;
        }

        Path source = Paths.get(sourceStr);
        Path destination = Paths.get(destStr);

        System.out.println("[INFO] Copy_source     = " + source.toAbsolutePath());
        System.out.println("[INFO] Copy_destination= " + destination.toAbsolutePath());

        return new Settings(source, destination);
    }

    /**
     * フォルダ2配下の全ファイルを走査して、
     * 「ファイル名 -> 同名ファイルのパス一覧」のインデックスを作る。
     */
    private static Map<String, List<Path>> buildFileIndex(Path root) throws IOException {
        Map<String, List<Path>> index = new HashMap<>();

        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                  .forEach(path -> {
                      String fileName = path.getFileName().toString();
                      index.computeIfAbsent(fileName, k -> new ArrayList<>())
                           .add(path);
                  });
        }

        return index;
    }

    /**
     * フォルダ1配下を再帰的に走査し、条件に合うファイルをフォルダ2側へコピー。
     */
    private static void processFolder(Path folder1, Map<String, List<Path>> folder2Index) throws IOException {
        try (Stream<Path> stream = Files.walk(folder1)) {
            stream.filter(Files::isRegularFile)
                  .filter(CopyXlsxFiles::isTargetFile)
                  .forEach(source -> handleFileCopy(source, folder2Index));
        }
    }

    /**
     * 対象条件（拡張子 .xlsx かつファイル名が "aaa" で始まる）を判定。
     */
    private static boolean isTargetFile(Path path) {
        String fileName = path.getFileName().toString();
        String lower = fileName.toLowerCase(Locale.ROOT);
        return fileName.startsWith("aaa") && lower.endsWith(".xlsx");
    }

    /**
     * 1ファイルごとのコピー処理。
     * - フォルダ2配下に同名ファイルがあれば上書きコピー
     * - なければファイル名を標準出力
     * - コピーに失敗した場合はエラー内容を標準出力
     */
    private static void handleFileCopy(Path source,
                                       Map<String, List<Path>> folder2Index) {
        String fileName = source.getFileName().toString();
        List<Path> destList = folder2Index.get(fileName);

        if (destList == null || destList.isEmpty()) {
            // 同名ファイルがフォルダ2配下に存在しなかった場合
            System.out.println("[NOT FOUND] フォルダ2に同名ファイルがありません: "
                    + fileName + " (from " + source.toAbsolutePath() + ")");
            return;
        }

        // 同名ファイルが存在する場合は、全てに対して上書きコピー
        for (Path dest : destList) {
            try {
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[COPIED] " + source.toAbsolutePath()
                        + " -> " + dest.toAbsolutePath());
            } catch (IOException e) {
                System.out.println("[ERROR] コピー失敗: " + source.toAbsolutePath()
                        + " -> " + dest.toAbsolutePath()
                        + " : " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * 設定値を保持するための簡単なレコード（Java 16+）
     */
    private record Settings(Path copySource, Path copyDestination) {
    }
}
