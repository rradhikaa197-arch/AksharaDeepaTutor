package com.aksharadeep.tutor.ui

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aksharadeep.tutor.R
import com.aksharadeep.tutor.data.Question
import kotlinx.coroutines.launch

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val viewModel: MainViewModel by activityViewModels()

    private var questions = listOf<Question>()
    private var currentIndex = 0
    private var score = 0
    private var quizFinished = false
    private var countdownTimer: CountDownTimer? = null

    private val TIMER_SECONDS = 30L

    // Views
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var questionCounter: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var optionsGroup: RadioGroup
    private lateinit var optA: RadioButton
    private lateinit var optB: RadioButton
    private lateinit var optC: RadioButton
    private lateinit var optD: RadioButton
    private lateinit var btnSubmit: Button
    private lateinit var reviewText: TextView
    private lateinit var resultLayout: LinearLayout
    private lateinit var scoreText: TextView
    private lateinit var btnRetry: Button
    private lateinit var btnBack: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        questionText = view.findViewById(R.id.questionText)
        timerText = view.findViewById(R.id.timerText)
        questionCounter = view.findViewById(R.id.questionCounter)
        progressBar = view.findViewById(R.id.quizProgressBar)
        optionsGroup = view.findViewById(R.id.optionsGroup)
        optA = view.findViewById(R.id.optA)
        optB = view.findViewById(R.id.optB)
        optC = view.findViewById(R.id.optC)
        optD = view.findViewById(R.id.optD)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        reviewText = view.findViewById(R.id.reviewText)
        resultLayout = view.findViewById(R.id.resultLayout)
        scoreText = view.findViewById(R.id.scoreText)
        btnRetry = view.findViewById(R.id.btnRetry)
        btnBack = view.findViewById(R.id.btnBack)

        val chapterId = arguments?.getInt("chapterId") ?: 1
        val subject = arguments?.getString("subject") ?: "Science"
        val chapterName = arguments?.getString("chapterName") ?: ""

        view.findViewById<TextView>(R.id.chapterNameText).text = chapterName

        // Load questions
        lifecycleScope.launch {
            questions = viewModel.getQuizQuestions(chapterId)
            if (questions.isEmpty()) {
                questionText.text = "No questions found for this chapter yet."
                btnSubmit.isEnabled = false
            } else {
                progressBar.max = questions.size
                showQuestion()
            }
        }

        btnSubmit.setOnClickListener {
            if (!quizFinished) {
                countdownTimer?.cancel()
                handleAnswer(chapterId, subject)
            }
        }

        btnRetry.setOnClickListener {
            // Reload quiz
            currentIndex = 0
            score = 0
            quizFinished = false
            resultLayout.visibility = View.GONE
            lifecycleScope.launch {
                questions = viewModel.getQuizQuestions(chapterId)
                showQuestion()
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showQuestion() {
        if (currentIndex >= questions.size) {
            showResult()
            return
        }

        val q = questions[currentIndex]
        questionText.text = "Q${currentIndex + 1}: ${q.questionText}"
        optA.text = "A.  ${q.optionA}"
        optB.text = "B.  ${q.optionB}"
        optC.text = "C.  ${q.optionC}"
        optD.text = "D.  ${q.optionD}"
        optionsGroup.clearCheck()
        reviewText.visibility = View.GONE
        btnSubmit.isEnabled = true
        questionCounter.text = "Question ${currentIndex + 1} of ${questions.size}"
        progressBar.progress = currentIndex

        // Enable all options
        listOf(optA, optB, optC, optD).forEach { it.isEnabled = true }

        startTimer()
    }

    private fun startTimer() {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(TIMER_SECONDS * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secs = millisUntilFinished / 1000
                timerText.text = "⏱ ${secs}s"
                timerText.setTextColor(
                    if (secs <= 10) Color.RED else Color.parseColor("#4CAF50")
                )
            }
            override fun onFinish() {
                timerText.text = "⏱ 0s"
                timerText.setTextColor(Color.RED)
                handleAnswer(
                    arguments?.getInt("chapterId") ?: 1,
                    arguments?.getString("subject") ?: "Science"
                )
            }
        }.start()
    }

    private fun handleAnswer(chapterId: Int, subject: String) {
        btnSubmit.isEnabled = false
        listOf(optA, optB, optC, optD).forEach { it.isEnabled = false }

        val selected = when (optionsGroup.checkedRadioButtonId) {
            R.id.optA -> "A"
            R.id.optB -> "B"
            R.id.optC -> "C"
            R.id.optD -> "D"
            else -> ""
        }
        val correct = questions[currentIndex].correctAnswer

        val correctText = when (correct) {
            "A" -> optA.text
            "B" -> optB.text
            "C" -> optC.text
            "D" -> optD.text
            else -> ""
        }

        if (selected == correct) {
            score++
            reviewText.text = "✅ Correct!"
            reviewText.setTextColor(Color.parseColor("#388E3C"))
        } else {
            reviewText.text = if (selected.isEmpty())
                "⏰ Time's up! Correct answer: $correctText"
            else
                "❌ Wrong! Correct answer: $correctText"
            reviewText.setTextColor(Color.parseColor("#D32F2F"))
        }
        reviewText.visibility = View.VISIBLE

        // Move to next question after 1.5 seconds
        view?.postDelayed({
            currentIndex++
            if (currentIndex >= questions.size) {
                viewModel.saveQuizResult(chapterId, subject, score)
                showResult()
            } else {
                showQuestion()
            }
        }, 1500)
    }

    private fun showResult() {
        quizFinished = true
        countdownTimer?.cancel()
        resultLayout.visibility = View.VISIBLE

        val percent = (score * 100) / questions.size
        scoreText.text = buildString {
            appendLine("Quiz Complete! 🎉")
            appendLine("Score: $score / ${questions.size}")
            appendLine("Percentage: $percent%")
            appendLine()
            append(when {
                percent >= 80 -> "🌟 Excellent! Keep it up!"
                percent >= 60 -> "👍 Good job! Review weak areas."
                percent >= 40 -> "📖 Average. Study more."
                else -> "💪 Keep practicing! Don't give up."
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
    }
}