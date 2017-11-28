/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxwordgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DEV-18
 */
public class GlobalStorage {

    public static String DictionaryName = ""; // temporary stores dictionary file name
    public static int subGameId = 0; // 1-animals,  2-spaces, 3-body parts 4-electronics 5-foods

    public static int TotalGameWords = 10; // total count of words on a level

    public static int CurrentInputedWords = 0; // counts inputed words
    public static int CorrectWords = 0; // counts correct words

    public static int CurrentGameLevel = 0; // current level - 1~9 for now.

    public static int LimitTime = 60; // time limit on a level(in seconds)
    public static int CounterTime = 0; // downcounter of timer(in seconds)

    public static String CurrentSelectedWord = ""; // current voiced word string

    public static int[] wordStoredDataLevels = new int[]{
        22, 33, 44, 55, 66, 77, 88, 99, 111, 111,   // for animals
        11, 22, 33, 44, 55, 66, 77, 88, 95, 102,    // for spaces
        11, 22, 33, 44, 55, 62, 67, 70, 73, 76,     // for body parts
        11, 22, 33, 44, 52, 55, 55, 55, 55, 55,     // for electronics
        11, 22, 33, 44, 55, 66, 77, 88, 99, 99};    // for foods

    public static List<String> wordStoredDatas = new ArrayList<>(); // total loaded word dictionary

    /*
        reads dictionary from file.    
    */
    
    public static void initialize() throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new FileReader("src/resource/"+GlobalStorage.DictionaryName));
        System.err.println(GlobalStorage.DictionaryName);
        wordStoredDatas.clear();
        try {
            String str;
            while ((str = in.readLine()) != null) {
                wordStoredDatas.add(str);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }

    }

}
