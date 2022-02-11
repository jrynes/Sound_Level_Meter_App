package com.mixpanel.android.mpmetrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Survey implements Parcelable {
    public static Creator<Survey> CREATOR;
    private final int mCollectionId;
    private final JSONObject mDescription;
    private final int mId;
    private final List<Question> mQuestions;

    /* renamed from: com.mixpanel.android.mpmetrics.Survey.1 */
    static class C10981 implements Creator<Survey> {
        C10981() {
        }

        public Survey createFromParcel(Parcel source) {
            try {
                return new Survey(new JSONObject(source.readString()));
            } catch (JSONException e) {
                throw new RuntimeException("Corrupted JSON object written to survey parcel.", e);
            } catch (BadDecideObjectException e2) {
                throw new RuntimeException("Unexpected or incomplete object written to survey parcel.", e2);
            }
        }

        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    }

    public class Question {
        private final List<String> mChoices;
        private final String mPrompt;
        private final int mQuestionId;
        private final String mQuestionType;

        private Question(JSONObject question) throws JSONException, BadDecideObjectException {
            this.mQuestionId = question.getInt(Name.MARK);
            this.mQuestionType = question.getString(Send.TYPE);
            this.mPrompt = question.getString(SettingsJsonConstants.PROMPT_KEY);
            List<String> choicesList = Collections.emptyList();
            if (question.has("extra_data")) {
                JSONObject extraData = question.getJSONObject("extra_data");
                if (extraData.has("$choices")) {
                    JSONArray choices = extraData.getJSONArray("$choices");
                    choicesList = new ArrayList(choices.length());
                    for (int i = 0; i < choices.length(); i++) {
                        choicesList.add(choices.getString(i));
                    }
                }
            }
            this.mChoices = Collections.unmodifiableList(choicesList);
            if (getType() == QuestionType.MULTIPLE_CHOICE && this.mChoices.size() == 0) {
                throw new BadDecideObjectException("Question is multiple choice but has no answers:" + question.toString());
            }
        }

        public int getId() {
            return this.mQuestionId;
        }

        public String getPrompt() {
            return this.mPrompt;
        }

        public List<String> getChoices() {
            return this.mChoices;
        }

        public QuestionType getType() {
            if (QuestionType.MULTIPLE_CHOICE.toString().equals(this.mQuestionType)) {
                return QuestionType.MULTIPLE_CHOICE;
            }
            if (QuestionType.TEXT.toString().equals(this.mQuestionType)) {
                return QuestionType.TEXT;
            }
            return QuestionType.UNKNOWN;
        }
    }

    public enum QuestionType {
        UNKNOWN {
            public String toString() {
                return "*unknown_type*";
            }
        },
        MULTIPLE_CHOICE {
            public String toString() {
                return "multiple_choice";
            }
        },
        TEXT {
            public String toString() {
                return "text";
            }
        };
    }

    static {
        CREATOR = new C10981();
    }

    Survey(JSONObject description) throws BadDecideObjectException {
        try {
            this.mDescription = description;
            this.mId = description.getInt(Name.MARK);
            this.mCollectionId = description.getJSONArray("collections").getJSONObject(0).getInt(Name.MARK);
            JSONArray questionsJArray = description.getJSONArray("questions");
            if (questionsJArray.length() == 0) {
                throw new BadDecideObjectException("Survey has no questions.");
            }
            List<Question> questionsList = new ArrayList(questionsJArray.length());
            for (int i = 0; i < questionsJArray.length(); i++) {
                questionsList.add(new Question(questionsJArray.getJSONObject(i), null));
            }
            this.mQuestions = Collections.unmodifiableList(questionsList);
        } catch (JSONException e) {
            throw new BadDecideObjectException("Survey JSON was unexpected or bad", e);
        }
    }

    String toJSON() {
        return this.mDescription.toString();
    }

    public int getId() {
        return this.mId;
    }

    public int getCollectionId() {
        return this.mCollectionId;
    }

    public List<Question> getQuestions() {
        return this.mQuestions;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toJSON());
    }
}
