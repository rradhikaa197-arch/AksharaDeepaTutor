package com.aksharadeep.tutor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Chapter::class, Question::class, QuizResult::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "akshara_deepa_db"
                )
                    .addCallback(PrepopulateCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

// ============================================================
// Prepopulate: Chapters + 50+ Questions
// ============================================================

private class PrepopulateCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        insertChapters(db)
        insertQuestions(db)
    }

    private fun insertChapters(db: SupportSQLiteDatabase) {
        val chapters = listOf(
            // Science
            Triple(1, "Science", "Light - Reflection and Refraction"),
            Triple(2, "Science", "Chemical Reactions and Equations"),
            Triple(3, "Science", "Life Processes"),
            Triple(4, "Science", "Electricity"),
            Triple(5, "Science", "Magnetic Effects of Current"),
            // Math
            Triple(6, "Math", "Real Numbers"),
            Triple(7, "Math", "Polynomials"),
            Triple(8, "Math", "Quadratic Equations"),
            Triple(9, "Math", "Arithmetic Progressions"),
            Triple(10, "Math", "Triangles"),
            // Social Studies
            Triple(11, "Social", "Resources and Development"),
            Triple(12, "Social", "Nationalism in India"),
            Triple(13, "Social", "Money and Credit"),
            Triple(14, "Social", "Power Sharing"),
            Triple(15, "Social", "Democracy and Diversity")
        )
        chapters.forEach { (id, subject, name) ->
            db.execSQL(
                "INSERT INTO chapters (id, subject, name, isCompleted) VALUES ($id, '$subject', '$name', 0)"
            )
        }
    }

    private fun q(chapterId: Int, text: String, a: String, b: String, c: String, d: String, ans: String): String {
        val escaped = text.replace("'", "''")
        val ea = a.replace("'", "''")
        val eb = b.replace("'", "''")
        val ec = c.replace("'", "''")
        val ed = d.replace("'", "''")
        return "INSERT INTO questions (chapterId, questionText, optionA, optionB, optionC, optionD, correctAnswer) " +
                "VALUES ($chapterId, '$escaped', '$ea', '$eb', '$ec', '$ed', '$ans')"
    }

    private fun insertQuestions(db: SupportSQLiteDatabase) {

        // ---- SCIENCE: Chapter 1 - Light ----
        listOf(
            q(1, "Which type of mirror is used in car headlights?",
                "Convex", "Concave", "Plane", "Parabolic", "B"),
            q(1, "The angle of incidence is equal to angle of ___?",
                "Reflection", "Refraction", "Diffraction", "Absorption", "A"),
            q(1, "Which mirror always forms a virtual, erect and diminished image?",
                "Concave", "Plane", "Convex", "Parabolic", "C"),
            q(1, "The refractive index of glass with respect to air is 1.5. What is the speed of light in glass if speed in air is 3x10^8 m/s?",
                "4.5x10^8 m/s", "2x10^8 m/s", "1.5x10^8 m/s", "3x10^8 m/s", "B"),
            q(1, "Which phenomenon is responsible for the blue colour of the sky?",
                "Reflection", "Refraction", "Scattering", "Dispersion", "C"),
            q(1, "The focal length of a concave mirror is 10 cm. What is its radius of curvature?",
                "5 cm", "10 cm", "20 cm", "15 cm", "C"),
            q(1, "An object is placed at the centre of curvature of a concave mirror. Where does the image form?",
                "At focus", "At centre of curvature", "At infinity", "Between F and C", "B"),

            // ---- SCIENCE: Chapter 2 - Chemical Reactions ----
            q(2, "Which gas is produced when dilute HCl reacts with zinc?",
                "Oxygen", "Carbon dioxide", "Hydrogen", "Nitrogen", "C"),
            q(2, "The chemical formula of quick lime is?",
                "Ca(OH)2", "CaCO3", "CaO", "CaCl2", "C"),
            q(2, "Rusting of iron is an example of which type of reaction?",
                "Displacement", "Combination", "Oxidation", "Decomposition", "C"),
            q(2, "In a balanced chemical equation, the number of atoms of each element is ___?",
                "Different on both sides", "Same on both sides", "Zero", "Doubled", "B"),
            q(2, "Which of the following is a decomposition reaction?",
                "C + O2 -> CO2", "2H2O -> 2H2 + O2", "Fe + S -> FeS", "NaOH + HCl -> NaCl + H2O", "B"),

            // ---- SCIENCE: Chapter 3 - Life Processes ----
            q(3, "Which organelle is called the powerhouse of the cell?",
                "Nucleus", "Ribosome", "Mitochondria", "Chloroplast", "C"),
            q(3, "Photosynthesis occurs in which part of the plant cell?",
                "Mitochondria", "Nucleus", "Chloroplast", "Cell wall", "C"),
            q(3, "The raw materials for photosynthesis are?",
                "O2 and Glucose", "CO2 and Water", "N2 and Water", "CO2 and O2", "B"),
            q(3, "Which enzyme digests proteins in the stomach?",
                "Amylase", "Lipase", "Pepsin", "Trypsin", "C"),
            q(3, "The functional unit of the kidney is called?",
                "Neuron", "Nephron", "Axon", "Alveolus", "B"),

            // ---- SCIENCE: Chapter 4 - Electricity ----
            q(4, "Ohm's law states that V = ?",
                "I/R", "IR", "I+R", "I-R", "B"),
            q(4, "The SI unit of electric resistance is?",
                "Volt", "Ampere", "Ohm", "Watt", "C"),
            q(4, "In a series circuit, the current through each component is?",
                "Different", "Same", "Zero", "Doubled", "B"),
            q(4, "Power consumed by a device is P = ?",
                "V/I", "VI", "V+I", "V-I", "B"),
            q(4, "1 kWh is equal to how many joules?",
                "1000 J", "3600 J", "3,600,000 J", "36,000 J", "C"),

            // ---- MATH: Chapter 6 - Real Numbers ----
            q(6, "HCF of 12 and 18 is?",
                "3", "6", "12", "18", "B"),
            q(6, "Which of the following is an irrational number?",
                "0.5", "1/3", "√2", "0.25", "C"),
            q(6, "The product of two rational numbers is always?",
                "Irrational", "Rational", "Natural", "Prime", "B"),
            q(6, "LCM of 4 and 6 is?",
                "6", "10", "12", "24", "C"),
            q(6, "The decimal expansion of 1/7 is?",
                "Terminating", "Non-terminating repeating", "Non-terminating non-repeating", "Finite", "B"),
            q(6, "If HCF(a,b) = 4 and LCM(a,b) = 36, then a x b = ?",
                "9", "40", "144", "32", "C"),

            // ---- MATH: Chapter 7 - Polynomials ----
            q(7, "The degree of the polynomial 3x^2 + 5x - 2 is?",
                "0", "1", "2", "3", "C"),
            q(7, "If p(x) = x^2 - 5x + 6, what is p(2)?",
                "0", "2", "4", "6", "A"),
            q(7, "The number of zeroes of a quadratic polynomial is at most?",
                "1", "2", "3", "0", "B"),
            q(7, "Sum of zeroes of ax^2 + bx + c is?",
                "c/a", "-b/a", "b/a", "-c/a", "B"),
            q(7, "Product of zeroes of ax^2 + bx + c is?",
                "b/a", "-b/a", "c/a", "-c/a", "C"),

            // ---- MATH: Chapter 8 - Quadratic Equations ----
            q(8, "The discriminant of ax^2 + bx + c = 0 is?",
                "b^2 + 4ac", "b^2 - 4ac", "4ac - b^2", "b - 4ac", "B"),
            q(8, "If discriminant = 0, the quadratic equation has?",
                "No real roots", "Two distinct real roots", "Two equal real roots", "Imaginary roots", "C"),
            q(8, "Solve: x^2 - 5x + 6 = 0. What are the roots?",
                "2 and 4", "3 and 2", "1 and 6", "2 and 5", "B"),
            q(8, "Which method is used to find roots by completing the square?",
                "Factorisation", "Quadratic formula", "Completing the square", "Both B and C", "D"),
            q(8, "For what value of k does kx^2 + 2x + 1 = 0 have equal roots?",
                "k=0", "k=1", "k=2", "k=4", "B"),

            // ---- MATH: Chapter 9 - Arithmetic Progressions ----
            q(9, "The nth term of an AP is given by?",
                "a + (n-1)d", "a + nd", "a x n x d", "a - (n-1)d", "A"),
            q(9, "Common difference of 2, 5, 8, 11 is?",
                "2", "3", "4", "5", "B"),
            q(9, "Sum of first n natural numbers is?",
                "n(n+1)", "n(n+1)/2", "n^2", "n/2", "B"),
            q(9, "The 10th term of AP: 1, 4, 7, 10... is?",
                "28", "30", "29", "27", "A"),
            q(9, "How many terms of AP 1,2,3,... are needed to get sum 55?",
                "8", "9", "10", "11", "C"),

            // ---- SOCIAL: Chapter 12 - Nationalism in India ----
            q(12, "The Non-Cooperation Movement was launched in?",
                "1919", "1920", "1921", "1922", "B"),
            q(12, "Who gave the call for Purna Swaraj on 26 January 1930?",
                "Nehru", "Gandhi", "Subhash Bose", "Bhagat Singh", "A"),
            q(12, "The Rowlatt Act was passed in?",
                "1917", "1918", "1919", "1920", "C"),
            q(12, "The Jallianwala Bagh massacre took place at?",
                "Delhi", "Bombay", "Amritsar", "Lahore", "C"),
            q(12, "Salt Satyagraha took place in?",
                "1920", "1925", "1930", "1935", "C"),

            // ---- SOCIAL: Chapter 13 - Money and Credit ----
            q(13, "Which of the following is a formal source of credit?",
                "Moneylenders", "Friends", "Banks", "Relatives", "C"),
            q(13, "The Reserve Bank of India regulates?",
                "Schools", "Banks", "Hospitals", "Roads", "B"),
            q(13, "Collateral is?",
                "A type of loan", "An asset used as security for a loan", "A bank account", "Interest rate", "B"),
            q(13, "Self Help Groups primarily help?",
                "Big businesses", "Rural poor", "Government", "Multinational companies", "B"),
            q(13, "Which is NOT a function of money?",
                "Medium of exchange", "Store of value", "Production of goods", "Measure of value", "C"),

            // ---- SOCIAL: Chapter 14 - Power Sharing ----
            q(14, "Power sharing is the basic principle of?",
                "Dictatorship", "Monarchy", "Democracy", "Oligarchy", "C"),
            q(14, "Belgium chose a power sharing model to?",
                "Increase conflicts", "Prevent ethnic conflict", "Reduce economic growth", "Limit elections", "B"),
            q(14, "Horizontal power sharing means?",
                "Between central and state governments", "Among different organs of government", "Between social groups", "None of these", "B"),
            q(14, "Which country has a community government system?",
                "India", "Sri Lanka", "Belgium", "USA", "C"),
            q(14, "Federalism is an example of which type of power sharing?",
                "Horizontal", "Vertical", "Social", "Political", "B")

        ).forEach { sql -> db.execSQL(sql) }
    }
}