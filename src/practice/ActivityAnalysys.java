package practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAnalysys {

	static Map<String, Integer> postsMap = new HashMap<String, Integer>();

	public static void main(String[] args) {

		File readFile = new File("テキトー.txt");

		String nameOfoutputFile = readFile.getName();
		File outputFile = new File(
				"C:\\Users\\circu\\Documents\\fileIOTest\\output\\CharsCount" + nameOfoutputFile + ".csv");

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false))) {
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(readFile), "UTF-8"));

				String line;
				String accountName = "";

				boolean isFirstLine = false;
				String afterCharsAccountName = "";
				String emptyLine = "";

				while ((line = br.readLine()) != null) {

					if (checkLine(line)) {
						int index = line.indexOf("\t", 6);
						// -1は自動メッセージ
						if (index == -1) {
							accountName = "";
							continue;
						}

						// 名前取得
						accountName = line.substring(6, index);
						// 名前より後の文字を取得
						afterCharsAccountName = line.substring(++index, line.length());
						isFirstLine = true;
					}

					// 空行はカウントしない
					if (!accountName.equals(emptyLine)) {
						int count = postsMap.containsKey(accountName) ? postsMap.get(accountName) : 0;
						int numOfFirstLinechars = 0;
						int numOfchars = 0;

						if (count == 0) {
							numOfFirstLinechars = checkPostContent(afterCharsAccountName);
							postsMap.put(accountName, numOfFirstLinechars);
						} else {
							if (isFirstLine) {
								numOfFirstLinechars = checkPostContent(afterCharsAccountName);
								postsMap.replace(accountName, count + numOfFirstLinechars);
							} else {
								numOfchars = checkPostContent(line);
								postsMap.replace(accountName, count + numOfchars);
							}
						}
					}

					isFirstLine = false;
				}

				List<Entry<String, Integer>> entriesList = new ArrayList<Entry<String, Integer>>(postsMap.entrySet());

				// 比較関数Comparatorを使用してMap.Entryの値を比較する(昇順)
				Collections.sort(entriesList, new Comparator<Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						return obj1.getValue().compareTo(obj2.getValue());
					}
				});

				for (Entry<String, Integer> entry : entriesList) {
					System.out.println(entry.getKey() + " : " + entry.getValue());
					bw.write(entry.getKey() + ",");
					bw.write(String.valueOf(entry.getValue()));
					bw.newLine();
				}

				bw.flush();

				br.close();
				System.out.println("終了");
			}

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean checkLine(String line) {

		if (line.length() < 5)
			return false;

		String regex_num = "^([01][0-9]|2[0-3]):[0-5][0-9]";
		Pattern p = Pattern.compile(regex_num);
		Matcher m1 = p.matcher(line.substring(0, 5));
		boolean result = m1.matches();

		if (result) {
			return true;
		} else {
			return false;
		}
	}

	private static int checkPostContent(String afterCharsAccountName) {

		if (afterCharsAccountName.equals("[写真]"))
			return 10;

		if (afterCharsAccountName.equals("[ファイル]"))
			return 30;

		if (afterCharsAccountName.contains("https://"))
			return 20;

		if (afterCharsAccountName.contains("http://"))
			return 20;

		return afterCharsAccountName.length();
	}
}
