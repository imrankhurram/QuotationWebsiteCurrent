package com.nextcontrols.utility;

import java.util.regex.Pattern;

public class ValidateEmails {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");//added by adnan
   
	public static boolean validate(String s) {
        return EMAIL_PATTERN.matcher(s).matches();
    }
}
