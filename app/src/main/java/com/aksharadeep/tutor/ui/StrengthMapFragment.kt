package com.aksharadeep.tutor.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.aksharadeep.tutor.R

class StrengthMapFragment : Fragment(R.layout.fragment_strength_map) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radarChart = view.findViewById<RadarChart>(R.id.radarChart)
        val summaryText = view.findViewById<TextView>(R.id.strengthSummary)
        val gapText = view.findViewById<TextView>(R.id.gapAreasText)

        viewModel.subjectStrengths.observe(viewLifecycleOwner) { strengths ->
            if (strengths.isEmpty()) {
                summaryText.text = "Take some quizzes to see your Strength Map!"
                return@observe
            }

            val entries = strengths.map { s ->
                RadarEntry(viewModel.calculateStrengthPercent(s.avgScore))
            }
            val labels = strengths.map { it.subject }

            val dataset = RadarDataSet(entries, "Strength").apply {
                color = Color.parseColor("#1565C0")
                fillColor = Color.parseColor("#42A5F5")
                setDrawFilled(true)
                fillAlpha = 100
                lineWidth = 2f
                valueTextSize = 10f
                valueTextColor = Color.parseColor("#1565C0")
            }

            radarChart.apply {
                data = RadarData(dataset)
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    textSize = 12f
                    textColor = Color.parseColor("#333333")
                }
                yAxis.apply {
                    axisMaximum = 100f
                    axisMinimum = 0f
                    setDrawLabels(false)
                }
                description.isEnabled = false
                legend.isEnabled = true
                webColor = Color.parseColor("#BBDEFB")
                webColorInner = Color.parseColor("#E3F2FD")
                webLineWidth = 1f
                webLineWidthInner = 0.75f
                animateXY(1000, 1000)
                invalidate()
            }

            // Gap areas: subjects below 60%
            val gapAreas = strengths.filter {
                viewModel.calculateStrengthPercent(it.avgScore) < 60f
            }
            if (gapAreas.isEmpty()) {
                gapText.text = "✅ No major gaps found. Keep practising!"
                gapText.setTextColor(Color.parseColor("#388E3C"))
            } else {
                gapText.text = "⚠️ Gap Areas: ${gapAreas.joinToString(", ") { it.subject }}"
                gapText.setTextColor(Color.parseColor("#D32F2F"))
            }

            val best = strengths.maxByOrNull { it.avgScore }
            summaryText.text = "🏆 Strongest subject: ${best?.subject ?: "-"}"
        }
    }
}