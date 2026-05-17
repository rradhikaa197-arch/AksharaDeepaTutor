package com.aksharadeep.tutor.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aksharadeep.tutor.data.AppDatabase
import com.aksharadeep.tutor.data.Chapter
import com.aksharadeep.tutor.data.QuizResult
import com.aksharadeep.tutor.data.SubjectStrength
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).dao()

    val chapters: LiveData<List<Chapter>> = dao.getAllChapters()
    val subjectStrengths: LiveData<List<SubjectStrength>> = dao.getSubjectStrengths()
    val completedCount: LiveData<Int> = dao.getCompletedChapterCount()
    val totalCount: LiveData<Int> = dao.getTotalChapterCount()
    val quizzesToday: LiveData<Int> = dao.getQuizzesTodayCount(getTodayStartMillis())

    fun markChapterComplete(chapter: Chapter) {
        viewModelScope.launch {
            dao.updateChapter(chapter.copy(isCompleted = true))
        }
    }

    suspend fun getQuizQuestions(chapterId: Int) =
        dao.getQuestionsForChapter(chapterId)

    fun saveQuizResult(chapterId: Int, subject: String, score: Int) {
        viewModelScope.launch {
            dao.insertResult(
                QuizResult(chapterId = chapterId, subject = subject, score = score)
            )
        }
    }

    fun calculateStrengthPercent(avgScore: Double): Float =
        ((avgScore / 5.0) * 100).toFloat().coerceIn(0f, 100f)

    private fun getTodayStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}