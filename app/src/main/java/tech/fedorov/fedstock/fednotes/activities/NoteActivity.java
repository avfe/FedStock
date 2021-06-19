package tech.fedorov.fedstock.fednotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tech.fedorov.fedstock.R;
import tech.fedorov.fedstock.fednotes.database.NotesDB;
import tech.fedorov.fedstock.fednotes.entities.Note;

public class NoteActivity extends AppCompatActivity {
    private ImageButton backButton;
    private ImageButton doneButton;
    private ImageButton deleteButton;
    private EditText title;
    private EditText inputField;
    private TextView dateTime;
    private AlertDialog deleteAlert;

    private Note alreadyAvailableNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        backButton = findViewById(R.id.back_button);
        doneButton = findViewById(R.id.done_button);
        title = findViewById(R.id.title);
        inputField = findViewById(R.id.input_field);
        dateTime = findViewById(R.id.date_time);
        deleteButton = findViewById(R.id.delete_button);

        dateTime.setText(getCurrentTime());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        } else if (savedInstanceState != null && savedInstanceState.containsKey("alreadyAvailableNote")) {
            alreadyAvailableNote = (Note) savedInstanceState.getSerializable("alreadyAvailableNote");
            setViewOrUpdateNote();
        }

        if (alreadyAvailableNote != null) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteAlert();
                }
            });
        }
    }

    private void showDeleteAlert() {
        if (deleteAlert == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.delete_note,
                    (ViewGroup) findViewById(R.id.layout_delete_note)
            );
            builder.setView(view);
            deleteAlert = builder.create();
            if (deleteAlert.getWindow() != null) {
                deleteAlert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    @SuppressLint("StaticFieldLeak")
                    class DeleteNote extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDB.getDatabase(getApplicationContext())
                                    .noteDAO()
                                    .delete(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNote().execute();

                }
            });

            view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlert.dismiss();
                }
            });
        }

        deleteAlert.show();
    }

    private void setViewOrUpdateNote() {
        title.setText(alreadyAvailableNote.getTitle());
        inputField.setText(alreadyAvailableNote.getNoteBody());
        dateTime.setText(alreadyAvailableNote.getDateTime());

    }

    private void saveNote() {
        if (title.getText().toString().trim().isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.title_cant_be_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputField.getText().toString().trim().isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.note_cant_be_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setDateTime(dateTime.getText().toString());
        note.setTitle(title.getText().toString());
        note.setNoteBody(inputField.getText().toString());

        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDB.getDatabase(getApplicationContext()).noteDAO().insert(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNote().execute();
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        return currentDateTime;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (alreadyAvailableNote != null) {
            outState.putSerializable("alreadyAvailableNote", alreadyAvailableNote);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deleteButton.setOnClickListener(null);
        doneButton.setOnClickListener(null);
    }
}
