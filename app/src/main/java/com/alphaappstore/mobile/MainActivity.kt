package com.alphaappstore.mobile

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val density = resources.displayMetrics.density
        val padding = (24 * density).toInt()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(ContextCompat.getColor(context, R.color.alpha_background))
            setPadding(padding, padding, padding, padding)
        }

        val title = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 28f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(context, R.color.alpha_accent))
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "V1 build is live - shipped straight from CI."
            textSize = 15f
            setTextColor(ContextCompat.getColor(context, R.color.alpha_text))
            gravity = Gravity.CENTER
            setPadding(0, (12 * density).toInt(), 0, 0)
        }

        val version = TextView(this).apply {
            text = "versionName 1.0.0 - versionCode 1"
            textSize = 12f
            setTextColor(ContextCompat.getColor(context, R.color.alpha_text))
            gravity = Gravity.CENTER
            setPadding(0, (8 * density).toInt(), 0, 0)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(version)
        setContentView(root)
    }
}
