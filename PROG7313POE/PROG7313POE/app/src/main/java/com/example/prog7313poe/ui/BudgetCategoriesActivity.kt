package com.example.prog7313poe.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7313poe.R
import com.example.prog7313poe.viewModel.CategoryViewModel

class BudgetCategoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_categories)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCategories)
        val btnAdd = findViewById<Button>(R.id.btnAddCategory)

        adapter = CategoryAdapter(emptyList())

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        viewModel.categories.observe(this) { categories ->
            adapter.updateData(categories)
        }

        btnAdd.setOnClickListener {
            viewModel.addCategory(
                name = "Food",
                color = "#FF9800",
                budgetLimit = 3000.0
            )
        }
    }
}