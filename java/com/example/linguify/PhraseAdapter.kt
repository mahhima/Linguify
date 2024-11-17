package com.example.linguify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PhraseAdapter(context: Context, phrases: List<Phrase>) :
    ArrayAdapter<Phrase>(context, 0, phrases) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val phrase = getItem(position)

        val listItem = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textViewPhrase: TextView = listItem.findViewById(android.R.id.text1)
        textViewPhrase.text = phrase?.phrase

        return listItem
    }
}
