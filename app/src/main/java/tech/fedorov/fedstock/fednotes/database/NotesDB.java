package tech.fedorov.fedstock.fednotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tech.fedorov.fedstock.fednotes.dao.NoteDAO;
import tech.fedorov.fedstock.fednotes.entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDB extends RoomDatabase {

    private static NotesDB notesDB;
    private static final String DBNAME = "notes_database";

    public static synchronized NotesDB getDatabase(Context context) {
        if (notesDB == null) {
            notesDB = Room.databaseBuilder(context, NotesDB.class, DBNAME).build();
        }
        return notesDB;
    }

    public abstract NoteDAO noteDAO();
}
