package com.aksharadeep.tutor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aksharadeep.tutor.databinding.FragmentDailyGoalBinding

class DailyGoalFragment : Fragment() {

    private var _binding: FragmentDailyGoalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDailyGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tips = listOf(
            "📌 Revise one chapter before starting a new one.",
            "🧠 Active recall is more effective than re-reading.",
            "⏱ Take a 5-min break after every 25 min of study.",
            "✍️ Write formulas daily to memorise them faster.",
            "🎯 Focus on your gap areas for better results."
        )
        binding.studyTipsText.text = tips.random()

        viewModel.quizzesToday.observe(viewLifecycleOwner) { count ->
            val target = 3
            val progress = minOf(count * 100 / target, 100)
            binding.goalProgressBar.progress = progress
            binding.goalStatusText.text = when {
                count == 0 -> "❌ No quizzes today. Start studying!"
                count < target -> "📖 $count / $target quizzes done. Keep going!"
                else -> "🎉 Daily goal achieved! Great work!"
            }
        }

        viewModel.completedCount.observe(viewLifecycleOwner) { done ->
            binding.streakText.text = "📚 Chapters mastered: $done"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}