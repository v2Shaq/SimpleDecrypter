
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Decrypter {

	private String line;
	private String text;
	ArrayList<Character> letters;
	ArrayList<Integer> counts;

	public Decrypter() {
		letters = new ArrayList<>();
		counts = new ArrayList<>();
	}

	public static void main(String[] args) {
		Decrypter dc = new Decrypter();
		try {
			dc.readFile();
			dc.decrypt();
		} catch (FileNotFoundException e) {
			System.out.println("there is no file to read");
			e.printStackTrace();
		}
	}

	private void readFile() throws FileNotFoundException {
		Scanner fileIn = new Scanner(new File("C:/Users/v2/Desktop/it_sec_ue1/sample.txt_enc"));
		while (fileIn.hasNextLine()) {
			line = fileIn.nextLine();
			if (text == null) {
				text = line;
			} else {
				text = text.concat(line);
			}
		}
		fileIn.close();
	}

	private void decrypt() {
		countLetters();
		printLetterStats();
		String cleanString = text.replace("" + map.get(counts.get(0)), " ");
		cleanString = cleanString.replace("" + map.get(counts.get(1)), "e");
		cleanString = cleanString.replace("" + map.get(counts.get(2)), "t");
		cleanString = cleanString.replace("" + map.get(counts.get(3)), "a");
		cleanString = cleanString.replace("" + map.get(counts.get(4)), "o");
		cleanString = cleanString.replace("" + map.get(counts.get(5)), "i");
		cleanString = cleanString.replace("" + map.get(counts.get(6)), "n");
		cleanString = cleanString.replace("" + map.get(counts.get(7)), "h");
		System.out.println(cleanString);
	}

	private void countLetters() {
		int i = 0;
		while (text.length() > i) {
			char letter = text.charAt(i);
			int index = letters.indexOf(letter);
			if (-1 == index) {
				letters.add(letter);
				counts.add(1);
			} else {
				counts.set(index, counts.get(index) + 1);
			}
			i++;
		}
	}//vor e -->th and 
    //am --> a wenn auch alleine
    

	HashMap<Integer, Character> map = new HashMap<>();

	private void printLetterStats() {
		countLetters();
		int i = 0;
		while (letters.size() > i) {
			map.put(counts.get(i), letters.get(i));
			// System.out.println(letters.get(i) + "-->" + counts.get(i));
			i++;
		}
		Collections.sort(counts);
		Collections.reverse(counts);
		for (Integer integer : counts) {
			System.out.printf("Buchstabe: %s Häufigkeit %d\n", map.get(integer), integer);
		}
	}
}