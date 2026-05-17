package com.aksharadeep.tutor.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksharadeep.tutor.R
import com.aksharadeep.tutor.data.Chapter

class ChapterAdapter(
    private val chapters: List<Chapter>,
    private val onMarkDone: (Chapter) -> Unit,
    private val onStartQuiz: (Chapter) -> Unit
) : RecyclerView.Adapter<ChapterAdapter.ChapterVH>() {

    class ChapterVH(view: View) : RecyclerView.ViewHolder(view) {
        val chapterName: TextView = view.findViewById(R.id.chapterName)
        val statusIcon: ImageView = view.findViewById(R.id.statusIcon)
        val btnMarkDone: Button = view.findViewById(R.id.btnMarkDone)
        val btnQuiz: Button = view.findViewById(R.id.btnQuiz)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter, parent, false)
        return ChapterVH(view)
    }

    override fun onBindViewHolder(holder: ChapterVH, position: Int) {
        val chapter = chapters[position]
        holder.chapterName.text = chapter.name

        if (chapter.isCompleted) {
            holder.statusIcon.setImageResource(R.drawable.ic_check_circle)
            holder.statusIcon.setColorFilter(Color.parseColor("#4CAF50"))
            holder.btnMarkDone.text = "Done ✓"
            holder.btnMarkDone.isEnabled = false
            holder.btnMarkDone.alpha = 0.5f
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_circle_outline)
            holder.statusIcon.setColorFilter(Color.parseColor("#BDBDBD"))
            holder.btnMarkDone.text = "Mark Done"
            holder.btnMarkDone.isEnabled = true
            holder.btnMarkDone.alpha = 1.0f
        }

        holder.btnMarkDone.setOnClickListener { onMarkDone(chapter) }
        holder.btnQuiz.setOnClickListener { onStartQuiz(chapter) }
    }

    override fun getItemCount() = chapters.size
}