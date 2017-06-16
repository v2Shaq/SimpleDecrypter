package decrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Theor
 *
 */

public class Decrypter {

	private String text;
	private String cleanString;
	private String filepath;
	
	private ArrayList<Character> letters;
	private ArrayList<Integer> counts;
	private ArrayList<String> sortedWordlist;
	
	private HashMap<Integer, Character> map;
	private static Set<String> dict;
	private static Set<String> textset;
	private HashMap<Character, ArrayList<Character>> decoderTable;
	
	
	public Decrypter(){
		letters = new ArrayList<>();
		counts = new ArrayList<>();
		map = new LinkedHashMap<Integer, Character>();

		filepath = "C:/Users/Theor/Desktop/Eclipse/workspace/itsicherheit/";
	}
	
	public static void main(String[] args) {
		Decrypter dc = new Decrypter();
		try {
			textset = dc.readFile( "sample.txt_enc");
			dict = dc.readFile( "3kMonstCommonWords.txt");
		} catch (FileNotFoundException e) {
			System.out.println("there is no file to read");
			e.printStackTrace();
		}
		dc.decrypt();
	}

	
	/**
	 * 
	 */
	private void makeDeocderTable(){
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		ArrayList<Character> table = new ArrayList<>();
		char letter;
		int i = 0;
		while (i <= alphabet.length()) {
			letter = alphabet.charAt(i);
			table.add(letter);
			i++;
		}
		while (i <= alphabet.length()) {
			letter = alphabet.charAt(i);
			decoderTable.put(letter, table);
			i++;
		}
		
	}
	
	/**
	 * reads the file
	 * @param filename	name of the file
	 * @param output	set of lines of the file
	 * @throws FileNotFoundException
	 */
	private Set<String> readFile(String filename) throws FileNotFoundException {
		Set<String> output = new HashSet<>();
		Scanner fileIn = new Scanner(new File(filepath + filename));
		while (fileIn.hasNextLine()) {
			output.add(fileIn.nextLine());
		}	
		fileIn.close();
		return output;
	}
	
	
	/**
	 * concats the set of encrypted lines to one string
	 */
	private void maketoString(){
		for (String line : textset) {
			if (text == null) {
				text = line;
			}else{
				text = text.concat(line);
			}
		}
	}
	
	
	/**
	 * decrypts the string
	 */
	private void decrypt(){
		makeDeocderTable();
		maketoString();
		countLetters();
		printLetterStats();
		makeWords();
		
//		 set most used char to e
		ArrayList<Character> e = new ArrayList<Character>();
		e.add('e');
		updateTable(letters.get(1), e);
		
		decOneLetterWords();
		decTwoLetterWords();
		// d seems to be the same char (d) --> idea
	}
	
	private void updateTable(char keyChar, ArrayList<Character> valueChars){
		Iterator<Entry<Character, ArrayList<Character>>> it = decoderTable.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Character, ArrayList<Character>> pair = it.next();
	        
	        if(!pair.getKey().equals(keyChar)){
	        	for (Character letter : pair.getValue()) {
	        		for (Character valueChar : valueChars) {
	        			if(letter.equals(valueChar)){
	        				pair.getValue().remove(letter);
	        			}
	        		}
	        	}
	        }else{
	        	pair.setValue(valueChars);
	        }
	}
	}
	/**
	 * decrypts words of length one
	 */
	private void decOneLetterWords(){
		ArrayList<String> words1 = new ArrayList<>();
		words1 = getWordsOfSameLength(1);
		ArrayList<Character> singleAIchars = new ArrayList<>();
		for (String word : words1) {
			char c= word.charAt(0);
			if(!singleAIchars.contains(c)){
				singleAIchars.add(c);
			}
		}
		
		ArrayList<Character> ai = new ArrayList<Character>();
		ai.add('a');
		ai.add('i');
		updateTable(singleAIchars.get(0), ai);
		updateTable(singleAIchars.get(1), ai);

//		cleanString = cleanString.replace("v", "i"); // single chars probably are I or a
//		cleanString = cleanString.replace("t", "a"); // single chars probably are I or a
	}
	
	/**
	 * decrypts words of length two
	 */
	private void decTwoLetterWords(){
		ArrayList<String> words2 = new ArrayList<>();
		getWordsOfSameLength(2);
	}
	
	/**
	 * decrypts words of length three
	 */
	private void decThreeLetterWords(){
		ArrayList<String> words3 = new ArrayList<>();
		getWordsOfSameLength(3);
		//cleanString = cleanString.replaceAll("(?<=t).?", "h"); // first char after t --> most often syllable th
		//cleanString = cleanString.replaceAll(".?(?=e)", "h"); // first char before e --> second most often syllable he TODO inserts not replaces -> wrong
	}
	
	/**
	 * @param wordlength	length of the searched word
	 * @return				list of all words with length of wordlength
	 */
	private ArrayList<String> getWordsOfSameLength(int wordlength){
		ArrayList<String> words = new ArrayList<>();
		for (String word : sortedWordlist) {
			if(word.length() != wordlength){
				break;
			}
			words.add(word);
		}
		return words;
	}
	
	/**
	 * counts the number of each letter in the encrypted string
	 */
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
	
	
	/**
	 * splits the encrypted string into words
	 */
	private void makeWords(){
		char mostChar = map.get(counts.get(0)); // most existing char
		cleanString = text.replace(mostChar, ' '); // see words
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
	
	
	/**
	 * prints the probablility of each letter
	 */
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
