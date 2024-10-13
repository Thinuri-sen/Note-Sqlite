package com.example.notessqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notessqlite.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_update_note)

        // Correctly initializing the binding for activity_update layout
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        db = NoteDatabaseHelper(this)

        // Get the note ID from the intent
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish() // If no valid note ID, finish the activity
            return
        }

        // Fetch the note from the database using the note ID
        val note = db.getNoteByID(noteId)
        if (note != null) {
            // Populate the EditText fields with the existing note data
            binding.updateTitleEditText.setText(note.title)
            binding.updateContentEditText.setText(note.content)
        } else {
            finish() // If note not found, finish the activity
            return
        }

        // Save button listener to update the note
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()

            // Update the note object and save it in the database
            val updatedNote = Note(noteId, newTitle, newContent)
            db.updateNote(updatedNote)
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()

            // Finish the activity after saving changes
            finish()
        }

        // Handling window insets (like status bar) to apply padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}