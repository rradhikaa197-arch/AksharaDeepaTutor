package com.aksharadeep.tutor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aksharadeep.tutor.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshUI()
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        viewModel.completedCount.observe(viewLifecycleOwner) { done ->
            viewModel.totalCount.observe(viewLifecycleOwner) { total ->
                if (total > 0) {
                    val percent = (done * 100) / total
                    binding.dashboardProgress.progress = percent
                    binding.dashboardProgressText.text = "$done / $total chapters completed"
                }
            }
        }

        viewModel.subjectStrengths.observe(viewLifecycleOwner) { strengths ->
            if (strengths.isNotEmpty()) {
                val best = strengths.maxByOrNull { it.avgScore }
                val weak = strengths.minByOrNull { it.avgScore }
                binding.dashboardProgressText.text =
                    "🏆 Best: ${best?.subject ?: "-"}  |  ⚠️ Weak: ${weak?.subject ?: "-"}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}