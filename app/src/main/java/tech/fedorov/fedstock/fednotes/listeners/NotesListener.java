package tech.fedorov.fedstock.fednotes.listeners;

import tech.fedorov.fedstock.fednotes.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
