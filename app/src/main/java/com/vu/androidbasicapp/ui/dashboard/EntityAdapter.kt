package com.vu.androidbasicapp.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vu.androidbasicapp.R
import com.vu.androidbasicapp.data.model.Entity

class EntityAdapter(
    private val onItemClick: (Entity) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    private val entities = mutableListOf<Entity>()

    fun submitList(newEntities: List<Entity>) {
        entities.clear()
        entities.addAll(newEntities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int = entities.size

    inner class EntityViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvEntityTitle: TextView = itemView.findViewById(R.id.tvEntityTitle)
        private val tvEntitySubtitle: TextView = itemView.findViewById(R.id.tvEntitySubtitle)
        private val tvEntityMeta: TextView = itemView.findViewById(R.id.tvEntityMeta)

        fun bind(entity: Entity) {
            tvEntityTitle.text = getTitle(entity)
            tvEntitySubtitle.text = getSubtitle(entity)
            tvEntityMeta.text = getSummary(entity)

            itemView.setOnClickListener {
                onItemClick(entity)
            }
        }
    }

    private fun getTitle(entity: Entity): String {
        return getValueByPossibleKeys(
            entity,
            listOf("courseName", "name", "title")
        ) ?: entity.entries
            .firstOrNull { !it.key.equals("description", ignoreCase = true) }
            ?.value
            ?.let { formatValue(it) }
        ?: "Untitled Entity"
    }

    private fun getSubtitle(entity: Entity): String {
        return getValueByPossibleKeys(
            entity,
            listOf("courseCode", "code", "category", "type", "instructor")
        ) ?: "Tap to view details"
    }

    private fun getSummary(entity: Entity): String {
        return entity
            .filterKeys { !it.equals("description", ignoreCase = true) }
            .entries
            .joinToString(separator = "\n") { entry ->
                "${formatKey(entry.key)}: ${formatValue(entry.value)}"
            }
    }

    private fun getValueByPossibleKeys(
        entity: Entity,
        possibleKeys: List<String>
    ): String? {
        for (targetKey in possibleKeys) {
            val matchedEntry = entity.entries.firstOrNull {
                it.key.equals(targetKey, ignoreCase = true)
            }

            if (matchedEntry?.value != null) {
                return formatValue(matchedEntry.value)
            }
        }

        return null
    }

    private fun formatKey(key: String): String {
        return key
            .replace(Regex("([a-z])([A-Z])"), "$1 $2")
            .replaceFirstChar { it.uppercase() }
    }

    private fun formatValue(value: Any?): String {
        return when (value) {
            null -> "N/A"
            is Double -> {
                if (value % 1.0 == 0.0) {
                    value.toInt().toString()
                } else {
                    value.toString()
                }
            }
            is Float -> {
                if (value % 1.0f == 0.0f) {
                    value.toInt().toString()
                } else {
                    value.toString()
                }
            }
            else -> value.toString()
        }
    }
}