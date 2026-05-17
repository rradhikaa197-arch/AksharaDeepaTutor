package com.aksharadeep.tutor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aksharadeep.tutor.R
import com.aksharadeep.tutor.databinding.FragmentSyllabusBinding
import com.aksharadeep.tutor.ui.adapter.SubjectAdapter

class SyllabusFragment : Fragment() {

    private var _binding: FragmentSyllabusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSyllabusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewModel.chapters.observe(viewLifecycleOwner) { chapters ->
            val grouped = chapters.groupBy { it.subject }
            binding.chapterRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.chapterRecyclerView.adapter = SubjectAdapter(
                grouped = grouped,
                onMarkDone = { chapter -> viewModel.markChapterComplete(chapter) },
                onStartQuiz = { chapter ->
                    findNavController().navigate(
                        R.id.action_syllabus_to_quiz,
                        bundleOf(
                            "chapterId" to chapter.id,
                            "subject" to chapter.subject,
                            "chapterName" to chapter.name
                        )
                    )
                }
            )
            val done = chapters.count { it.isCompleted }
            val total = chapters.size
            binding.overallProgress.progress = if (total > 0) (done * 100) / total else 0
            binding.progressText.text = "$done of $total chapters completed"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}