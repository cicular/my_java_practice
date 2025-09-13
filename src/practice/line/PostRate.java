package practice.line;

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

public class PostRate {

	static Map<String, Integer> nameMap = new HashMap<String, Integer>();

	public static void main(String[] args) {

		File readFile = new File("テキトー.txt");

		String nameOfoutputFile = readFile.getName();
		File outputFile = new File("フォルダ" + nameOfoutputFile + ".csv");

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false))) {
			{

				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(readFile), "UTF-8"));

				String line;
				while ((line = br.readLine()) != null) {

					if (checkLine(line)) {

						int index = line.indexOf("\t", 6);
						if (index != -1) {
							nameMap.merge(line.substring(6, index), 1, Integer::sum);
						}
					}
				}

				List<Entry<String, Integer>> entriesList = new ArrayList<Entry<String, Integer>>(nameMap.entrySet());

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

		if (line.length() < 5) {
			return false;
		}

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
}