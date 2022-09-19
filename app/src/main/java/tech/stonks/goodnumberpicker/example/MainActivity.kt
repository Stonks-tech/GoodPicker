package tech.stonks.goodnumberpicker.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val numberPicker = findViewById<tech.stonks.goodnumberpicker.GoodNumberPicker>(R.id.number_picker)
        numberPicker.onSelectedPositionChanged = { position ->
            textView.text = "Position: $position"
        }
    }
}