package com.aksharadeep.tutor.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {

    // ---- CHAPTERS ----

    @Query("SELECT * FROM chapters ORDER BY subject, name")
    fun getAllChapters(): LiveData<List<Chapter>>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getChapterById(id: Int): Chapter?

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Query("SELECT COUNT(*) FROM chapters WHERE isCompleted = 1")
    fun getCompletedChapterCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM chapters")
    fun getTotalChapterCount(): LiveData<Int>

    // ---- QUESTIONS ----

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE chapterId = :chapterId ORDER BY RANDOM() LIMIT 5")
    suspend fun getQuestionsForChapter(chapterId: Int): List<Question>

    @Query("SELECT COUNT(*) FROM questions WHERE chapterId = :chapterId")
    suspend fun getQuestionCountForChapter(chapterId: Int): Int

    // ---- QUIZ RESULTS ----

    @Insert
    suspend fun insertResult(result: QuizResult)

    @Query("SELECT subject, AVG(CAST(score AS REAL) / totalQuestions * 5) as avgScore FROM quiz_results GROUP BY subject")
    fun getSubjectStrengths(): LiveData<List<SubjectStrength>>

    @Query("SELECT * FROM quiz_results ORDER BY date DESC LIMIT 10")
    fun getRecentResults(): LiveData<List<QuizResult>>

    @Query("SELECT COUNT(*) FROM quiz_results WHERE date >= :todayStart")
    fun getQuizzesTodayCount(todayStart: Long): LiveData<Int>
}