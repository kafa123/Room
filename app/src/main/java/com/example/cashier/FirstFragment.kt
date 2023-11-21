package com.example.cashier


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.cashier.database.Note
import com.example.cashier.database.NoteDao
import com.example.cashier.database.NoteRoomDatabase
import com.example.cashier.databinding.FragmentFirstBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
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

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(requireContext())
        NotesDao = db!!.noteDao()!!
        with(_binding){
            
            _binding?.Lv?.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { adapterView, _, i, _ ->
                    val item = adapterView.adapter.getItem(i) as Note
                    delete(item)
                    true
                }
            binding?.Lv?.onItemClickListener = AdapterView.OnItemClickListener { adapterview, _, position, _ ->
                val selectedNote = adapterview.adapter.getItem(position) as Note

                val bundle = Bundle().apply {
                    putSerializable("Note",selectedNote)
                }
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment,bundle)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllNotes()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getAllNotes() {
        NotesDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
                requireContext(),
                android.R.layout.simple_list_item_1  , notes
            )
            binding.Lv.adapter = adapter
        }
    }
    private fun delete(note: Note) {
        executorService.execute { NotesDao.delete(note) }
    }
}