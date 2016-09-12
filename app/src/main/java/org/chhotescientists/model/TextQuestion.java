package org.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.widget.EditText;

import org.chhotescientists.model.view_containers.ResultEditTextContainer;
import org.chhotescientists.model.view_containers.ResultViewContainer;

/**
 * See parent CheckInQuestion for documentation.
 */
public class TextQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "text";

    public TextQuestion(String question) {
        super(question);
    }

    public TextQuestion(Parcel in) {
        super(in);
    }

    @Override
    public ResultViewContainer getQuestionViewContainer(Activity activity) {
        EditText editText = new EditText(activity);
        return new ResultEditTextContainer(editText);
    }

    @Override
    public String getQuestionType() {
        return QUESTION_TYPE;
    }

    public static final Creator<TextQuestion> CREATOR = new Creator<TextQuestion>() {
        @Override
        public TextQuestion createFromParcel(Parcel in) {
            return new TextQuestion(in);
        }

        @Override
        public TextQuestion[] newArray(int size) {
            return new TextQuestion[size];
        }
    };
}
