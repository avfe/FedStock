package tech.fedorov.fedstock.fednotes.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Title")
    private String title;

    @ColumnInfo(name = "Note_body")
    private String noteBody;

    @ColumnInfo(name = "Date_time")
    private String dateTime;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", noteBody='" + noteBody + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
