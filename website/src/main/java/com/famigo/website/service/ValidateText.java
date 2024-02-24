package com.famigo.website.Service;

public class ValidateText {
    // This function checks a password to ensure that it has at least one uppercase
    // letter
    // has at least eight characters and has no illegal characters
    public static boolean isPasswordValid(String password) {
        if (isTextSanitary(password) && !password.equals(password.toLowerCase()) && (password.length() > 7)) {
            return true;
        }
        return false;
    }

    // This function checks if a given text has only the basic ascii characters and
    // a few supplimentary characters (unicode 0-255)
    public static boolean isTextSanitary(String text) {
        if (text == null) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 255) {
                return false;
            }
        }

        return true;
    }
}
