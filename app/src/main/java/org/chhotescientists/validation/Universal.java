package org.chhotescientists.validation;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DNYANESHWAR on 11/7/2015.
 */
public class Universal {

    /**
     * For the GCM.
     */


    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isValidMobile(String mobile) {
        Pattern pattern;
        Matcher matcher;
        String MOBILE_PATTERN = "^[789]\\d{9}$";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyACT(AutoCompleteTextView etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
