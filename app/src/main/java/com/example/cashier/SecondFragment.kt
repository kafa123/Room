package com.example.cashier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cashier.database.Note
import com.example.cashier.database.NoteDao
import com.example.cashier.database.NoteRoomDatabase
import com.example.cashier.databinding.FragmentSecondBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var NotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(requireContext())
        NotesDao = db!!.noteDao()!!

        val receiveNote=arguments?.getSerializable("Note")as Note?

        with(_binding){
            binding?.inputTitle?.setText(receiveNote?.Title)
            binding?.inputContent?.setText(receiveNote?.Content)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        binding.submit.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            insert(Note(Content = binding.inputContent.text.toString(), Date = sdf.format(Date(System.currentTimeMillis())).toString(), Title = binding.inputTitle.text.toString()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun insert(note: Note){
        executorService.execute{NotesDao.insert(note)}
    }
    private fun update(note:Note){
        executorService.execute {
            // Fetch the note from the database by its ID or another unique identifier
            val existingNote = NotesDao.getNoteById(note.id) // Change this according to your database setup



            existingNote?.let {
                val updatedNote = it.copy(
                    Title = note.Title,
                    Content = note.Content,
                    Date = note.Date
                )

                // Update the note in the database
                NotesDao.update(it)
            }
        }
    }

}