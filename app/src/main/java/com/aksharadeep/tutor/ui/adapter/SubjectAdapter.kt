package com.aksharadeep.tutor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksharadeep.tutor.R
import com.aksharadeep.tutor.data.Chapter

class SubjectAdapter(
    private val grouped: Map<String, List<Chapter>>,
    private val onMarkDone: (Chapter) -> Unit,
    private val onStartQuiz: (Chapter) -> Unit
) : RecyclerView.Adapter<SubjectAdapter.SubjectVH>() {

    private val subjects = grouped.keys.toList()

    class SubjectVH(view: View) : RecyclerView.ViewHolder(view) {
        val subjectTitle: TextView = view.findViewById(R.id.subjectTitle)
        val subjectProgressBar: ProgressBar = view.findViewById(R.id.subjectProgress)
        val subjectProgressText: TextView = view.findViewById(R.id.subjectProgressText)
        val chapterList: RecyclerView = view.findViewById(R.id.chapterList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return SubjectVH(view)
    }

    override fun onBindViewHolder(holder: SubjectVH, position: Int) {
        val subject = subjects[position]
        val chapters = grouped[subject] ?: emptyList()
        val done = chapters.count { it.isCompleted }
        holder.subjectTitle.text = subject
        holder.subjectProgressBar.max = chapters.size
        holder.subjectProgressBar.progress = done
        holder.subjectProgressText.text = "$done/${chapters.size}"
        holder.chapterList.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.chapterList.adapter = ChapterAdapter(chapters, onMarkDone, onStartQuiz)
        holder.chapterList.isNestedScrollingEnabled = false
    }

    override fun getItemCount() = subjects.size
}