package com.vu.androidbasicapp.ui.details

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vu.androidbasicapp.R
import org.json.JSONObject

class DetailsActivity : AppCompatActivity() {

    private lateinit var tvDetailsTitle: TextView
    private lateinit var tvDetailsDescription: TextView
    private lateinit var detailsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        tvDetailsTitle = findViewById(R.id.tvDetailsTitle)
        tvDetailsDescription = findViewById(R.id.tvDetailsDescription)
        detailsContainer = findViewById(R.id.detailsContainer)

        val entityJson = intent.getStringExtra(EXTRA_ENTITY_JSON)

        if (entityJson.isNullOrBlank()) {
            tvDetailsTitle.text = getString(R.string.details_missing_title)
            tvDetailsDescription.text = getString(R.string.details_missing_message)
            return
        }

        displayEntityDetails(JSONObject(entityJson))
    }

    private fun displayEntityDetails(entity: JSONObject) {
        tvDetailsTitle.text = findDisplayTitle(entity)
        tvDetailsDescription.text = findDescription(entity)

        val keys = entity.keys()

        while (keys.hasNext()) {
            val key = keys.next()

            if (key.equals("description", ignoreCase = true)) {
                continue
            }

            val value = entity.opt(key)
            addDetailRow(formatKey(key), formatValue(value))
        }
    }

    private fun addDetailRow(label: String, value: String) {
        val labelView = TextView(this).apply {
            text = label
            textSize = 14f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 20
            }
        }

        val valueView = TextView(this).apply {
            text = value
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 6
            }
        }

        detailsContainer.addView(labelView)
        detailsContainer.addView(valueView)
    }

    private fun findDisplayTitle(entity: JSONObject): String {
        val possibleKeys = listOf("courseName", "name", "title", "subject", "unitName")

        for (targetKey in possibleKeys) {
            val matchedKey = findKeyIgnoreCase(entity, targetKey)
            if (matchedKey != null) {
                val value = entity.optString(matchedKey)
                if (value.isNotBlank()) {
                    return value
                }
            }
        }

        return getString(R.string.details_default_title)
    }

    private fun findDescription(entity: JSONObject): String {
        val descriptionKey = findKeyIgnoreCase(entity, "description")
        val description = descriptionKey?.let { entity.optString(it) }

        return if (description.isNullOrBlank()) {
            getString(R.string.details_no_description)
        } else {
            description
        }
    }

    private fun findKeyIgnoreCase(entity: JSONObject, targetKey: String): String? {
        val keys = entity.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            if (key.equals(targetKey, ignoreCase = true)) {
                return key
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
            null -> getString(R.string.details_not_available)
            JSONObject.NULL -> getString(R.string.details_not_available)
            else -> value.toString()
        }
    }

    companion object {
        const val EXTRA_ENTITY_JSON = "EXTRA_ENTITY_JSON"
    }
}