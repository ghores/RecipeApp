package com.example.recipeapp.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun RecyclerView.setupRecyclerView(myLayoutManager: RecyclerView.LayoutManager, myAdapter: RecyclerView.Adapter<*>) {
    this.apply {
        layoutManager = myLayoutManager
        adapter = myAdapter
        setHasFixedSize(true)
    }
}