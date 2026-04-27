package com.example.prog7313poe.ui

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.example.prog7313poe.R
import com.example.prog7313poe.data.AppDatabase
import com.example.prog7313poe.model.Category
import com.example.prog7313poe.model.Expense
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class InsightsActivity : AppCompatActivity() {

    private val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    private var expenses: List<Expense> = emptyList()
    private var categories: List<Category> = emptyList()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "test_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)
        setupBottomNavigation(R.id.nav_insights)

        val db = AppDatabase.getDatabase(this)
        val heading = findViewById<TextView>(R.id.txtHeading)
        val container = findViewById<LinearLayout>(R.id.layoutInsights)

        applyGradient(heading)

        lifecycleScope.launch {
            seedDefaultCategoriesIfNeeded(db, userId)
        }

        lifecycleScope.launch {
            db.expenseDao().getAllExpenses().collect {
                expenses = it
                renderInsights(container)
            }
        }

        lifecycleScope.launch {
            db.categoryDao().getCategories(userId).collect {
                categories = it
                renderInsights(container)
            }
        }
    }

    private fun renderInsights(container: LinearLayout) {
        container.removeAllViews()

        if (categories.isEmpty()) {
            container.addView(
                createInsightCard(
                    "No categories yet: Add one to see insights.",
                    "#444444"
                )
            )
            return
        }

        categories.forEach { category ->
            val (text, color) = generateCategoryInsight(category)

            container.addView(
                createInsightCard(text, color)
            )
        }

        val tip = TextView(this)
        tip.text = "Tip: Try the 7 day challenge to save extra!"
        tip.setTextColor(android.graphics.Color.WHITE)
        tip.textSize = 18f
        tip.setTypeface(null, Typeface.BOLD)
        tip.gravity = Gravity.CENTER
        tip.setPadding(0, 24, 0, 0)

        container.addView(tip)
    }

    private fun generateCategoryInsight(category: Category): Pair<String, String> {
        val spent = category.spentAmount
        val budget = category.budgetLimit

        if (budget <= 0) {
            return Pair(
                "${category.name}: No budget set.",
                "#9E9E9E"
            )
        }

        val percent = ((spent / budget) * 100).toInt()
        val daysLeft = getDaysLeftInMonth()

        return when {
            spent == 0.0 -> Pair(
                "${category.name}: No spending yet. Budget is R${budget.toInt()}.",
                "#2196F3"
            )

            percent < 50 -> Pair(
                "${category.name}: Great job! Only $percent% used.",
                "#4CAF50"
            )

            percent in 50..99 -> Pair(
                "${category.name}: You're at $percent%. $daysLeft days left.",
                "#FFC107"
            )

            percent == 100 -> Pair(
                "${category.name}: Budget fully used.",
                "#9C27B0"
            )

            else -> Pair(
                "${category.name}: Over budget! R${spent.toInt()}/R${budget.toInt()}",
                "#F44336"
            )
        }
    }

    private fun getDaysLeftInMonth(): Int {
        val today = LocalDate.now()
        return today.lengthOfMonth() - today.dayOfMonth
    }

    private fun createInsightCard(text: String, color: String): TextView {
        val card = TextView(this)

        val parts = text.split(":", limit = 2)
        val title = parts.getOrNull(0) ?: ""
        val message = parts.getOrNull(1)?.trim() ?: ""

        val fullText = "$title\n$message"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        card.text = spannable
        card.textSize = 16f
        card.setLineSpacing(6f, 1.1f)
        card.setPadding(32, 28, 32, 28)

        if (color.equals("#FFC107", true)) {
            card.setTextColor(android.graphics.Color.BLACK)
        } else {
            card.setTextColor(android.graphics.Color.WHITE)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 24)
        card.layoutParams = params

        val background = GradientDrawable()
        background.setColor(color.toColorInt())
        background.cornerRadius = 32f

        card.background = background
        card.elevation = 10f

        return card
    }

    private fun parseExpenseDate(date: String): LocalDate? {
        return try {
            LocalDate.parse(date, dateFormatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(R.id.nav_insights)
    }

    private fun applyGradient(textView: TextView) {
        textView.viewTreeObserver.addOnGlobalLayoutListener {
            val width = textView.width.toFloat()

            val shader = LinearGradient(
                0f, 0f, width, textView.textSize,
                intArrayOf("#FFD700".toColorInt(), "#FF69B4".toColorInt()),
                null,
                Shader.TileMode.CLAMP
            )

            textView.paint.shader = shader
        }
    }
}
