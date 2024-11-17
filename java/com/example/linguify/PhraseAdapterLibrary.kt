package com.example.linguify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PhraseAdapterLibrary(context: Context, private val phrases: List<Phrase>) : ArrayAdapter<Phrase>(context, 0, phrases) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val phrase = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

        // Show the phrase itself
        view.findViewById<TextView>(android.R.id.text1).text = phrase?.phrase

        return view
    }

    fun updatePhraseList(newPhrases: List<Phrase>) {
        clear()
        addAll(newPhrases)
        notifyDataSetChanged()
    }
}
