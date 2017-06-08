package decrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class decrypter {

	private String line;
	private String text;
	ArrayList<Character> letters = new ArrayList<>();
	ArrayList<Integer> counts = new ArrayList<>();
	HashMap<Integer, Character> map = new LinkedHashMap<Integer, Character>();
	String cleanString;
	ArrayList<String> sortedWordlist;
	
	public decrypter(){

	}
	
	public static void main(String[] args) {	
		decrypter dc = new decrypter();
		try {
			dc.readFile();
			dc.decrypt();
		} catch (FileNotFoundException e) {
			System.out.println("there is no file to read");
			e.printStackTrace();
		}
	}

	private void readFile() throws FileNotFoundException {
		Scanner fileIn = new Scanner(new File("C:/Users/Theor/Desktop/Eclipse/workspace/itsicherheit/sample.txt_enc"));
		while (fileIn.hasNextLine()) {
			line = fileIn.nextLine();
			if (text == null) {
				text = line;
			}else{
				text = text.concat(line);
			}
		}	
		fileIn.close();
	}
	
	private void decrypt(){
		countLetters();
		printLetterStats();
		makeWords();
		cleanString = cleanString.replace("f", "e"); // most used char is e
		//cleanString = cleanString.replaceAll("(?<=t).?", "h"); // first char after t --> most often syllable th
		//cleanString = cleanString.replaceAll(".?(?=e)", "h"); // first char before e --> second most often syllable he TODO inserts not replaces -> wrong
//		cleanString = cleanString.replace("v", "i"); // single chars probably are I or a
//		cleanString = cleanString.replace("t", "a"); // single chars probably are I or a
		// d seems to be the same char (d) --> idea
	}
	
	private void countLetters(){
		int i = 0;
		while (text.length() > i) {
			char letter = text.charAt(i);
			int index = letters.indexOf(letter);
			
			if (-1 == index) {
				letters.add(letter);
				counts.add(1);
			}else{
				counts.set(index, counts.get(index) + 1);
			}
			i++;		
		}
	}
	
	private void makeWords(){
		char test = map.get(counts.get(0)); // most existing char
		cleanString = text.replace(test, ' '); // see words
		String[] words = cleanString.split(" ");
		ArrayList<String> wordlist = new ArrayList<>(Arrays.asList(words));
		sortedWordlist = new ArrayList<>();
		int i = 0; // index of list
		int y = 1; // wordlenght
		while (wordlist.size() != sortedWordlist.size()) {
			String word = wordlist.get(i);
			if (word.length() == y) {
				sortedWordlist.add(word);
			}
			i++;
			if (wordlist.size() == i){
				y++;
				i = 0;
			}		
		}
	}
	
	
	private void printLetterStats(){
		int i = 0;
		while(letters.size() > i){
			map.put(counts.get(i),letters.get(i));
			i++;
		}
		Collections.sort(counts);
		Collections.reverse(counts);
		for (Integer integer : counts) {
			System.out.println(String.format("Letter %s Häufigkeit %d", map.get(integer), integer));
		}
	}
}
