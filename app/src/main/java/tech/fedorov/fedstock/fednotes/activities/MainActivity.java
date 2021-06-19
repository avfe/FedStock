package tech.fedorov.fedstock.fednotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import tech.fedorov.fedstock.R;
import tech.fedorov.fedstock.fednotes.adapters.ListNotesAdapter;
import tech.fedorov.fedstock.fednotes.database.NotesDB;
import tech.fedorov.fedstock.fednotes.entities.Note;
import tech.fedorov.fedstock.fednotes.listeners.NotesListener;

public class MainActivity extends AppCompatActivity implements NotesListener {
    private EditText searchField;
    private ImageView addButton;
    private RecyclerView recyclerView;
    private List<Note> listNotes;
    private ListNotesAdapter listNotesAdapter;
    private int noteClickedPosition = -1;

    private final int SPAN_COUNT = 2;

    public static final int CODE_ADD_NOTE = 1;
    public static final int CODE_UPDATE_NOTE = 2;
    public static final int CODE_SHOW_NOTE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

        if (savedInstanceState != null && savedInstanceState.containsKey("noteClickedPosition")) {
            noteClickedPosition = (int) savedInstanceState.getInt("noteClickedPosition");
        }

        searchField = findViewById(R.id.search_field);

        addButton = findViewById(R.id.add_button);

        recyclerView = findViewById(R.id.recyclerView_notes);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        );
        listNotes = new ArrayList<>();
        listNotesAdapter = new ListNotesAdapter(listNotes, this);
        recyclerView.setAdapter(listNotesAdapter);

        searchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchField.setHint(getString(R.string.empty));
                } else {
                    searchField.setHint(getString(R.string.search_notes));
                }
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listNotesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listNotes.size() != 0) {
                    listNotesAdapter.searchNotes(s.toString());
                }
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivityForResult(noteIntent, CODE_ADD_NOTE);
            }
        });

        getNotes(CODE_SHOW_NOTE, false);
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, CODE_UPDATE_NOTE);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNotes extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDB.getDatabase(getApplicationContext()).noteDAO().getNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);

                if (requestCode == CODE_SHOW_NOTE) {
                    listNotes.addAll(notes);
                    listNotesAdapter.notifyDataSetChanged();
                } else if (requestCode == CODE_ADD_NOTE) {
                    listNotes.add(0, notes.get(0));
                    listNotesAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                } else if (requestCode == CODE_UPDATE_NOTE) {
                    listNotes.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        listNotesAdapter.notifyDataSetChanged();
                    } else {
                        listNotes.add(noteClickedPosition, notes.get(noteClickedPosition));
                        listNotesAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        new GetNotes().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(CODE_ADD_NOTE, false);
        } else if (requestCode == CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (noteClickedPosition != -1) {
            outState.putInt("noteClickedPosition", noteClickedPosition);
        }
    }

}