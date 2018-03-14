package doit.study.droid.data.source.local.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public final String text;
    public final int questionId;

    public Tag(int id, String text, int questionId) {
        this.id = id;
        this.text = text;
        this.questionId = questionId;
    }

    @Ignore
    public Tag(String text, int questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getQuestionId() {
        return questionId;
    }
}