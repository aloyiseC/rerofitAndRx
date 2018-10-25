package laiwei.rxandretrofit.bean;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by laiwei on 2017/9/5 0005.
 */
public class QuestionType{

    public static final int QUESTION_TYPE_FOLLOW = 0;
    private String name;
    private String id;
    private String categoryId;
    private int noteId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QuestionType() {}

    private QuestionType(JSONObject data) throws JSONException {
        name = data.optString("name");
        id = data.optString("id");
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getNoteId() {
        return noteId;
    }
}
