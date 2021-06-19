package tech.fedorov.fedstock.fednotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import tech.fedorov.fedstock.fednotes.entities.Note;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    List<Note> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

}
