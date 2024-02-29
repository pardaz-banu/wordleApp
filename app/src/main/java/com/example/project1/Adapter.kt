package com.example.project1


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// Adapter.kt
class Adapter(
    var todos: List<Todo>,
    private val displayCorrectness: Boolean
) : RecyclerView.Adapter<Adapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.itemView.apply {
            val textNumView = findViewById<TextView>(R.id.textnum)
            textNumView.text = currentTodo.guessWord

            val textWordView = findViewById<TextView>(R.id.textword)
            textWordView.text = if (displayCorrectness) currentTodo.guessNumber else currentTodo.guessWord
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    // Function to add a new Todo to the list
    fun addTodo(todo: Todo) {
        todos = todos + todo
        notifyDataSetChanged()
    }

    fun clearTodos() {
        todos = emptyList()
        notifyDataSetChanged()
    }
}
