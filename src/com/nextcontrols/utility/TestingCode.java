package com.nextcontrols.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class TestingCode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String temp = "";
		 Pattern patternncs3001 = Pattern.compile("([\\+|-]{0,1})([0-9]+)((\\.[0-9]+)*)");
         Matcher matcherncs3001 = patternncs3001.matcher("HUMIDITY     LOW ALARM      30.4");
         if (!matcherncs3001.find()){
             ///throw new ScriptException("No temperature match");
         }
         else{
         	temp = matcherncs3001.group();
         }
       temp = temp.trim();
       JOptionPane.showMessageDialog(null, temp);
	}

}
