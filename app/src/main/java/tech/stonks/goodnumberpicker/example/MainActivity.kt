package tech.stonks.goodnumberpicker.example

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val numberPicker =
            findViewById<tech.stonks.goodnumberpicker.GoodNumberPicker>(R.id.number_picker)
        numberPicker.onSelectedPositionChanged = { position ->
            textView.text = "Position: $position"
        }
        numberPicker.itemFormatter = { index, item ->
            item.apply {
                styleChanged(
                    numberPicker.style.copy(
                        textStyle = numberPicker.style.textStyle.copy(
                            textColor = when(index % 3) {
                                0 -> Color.RED
                                1 -> Color.GREEN
                                else -> Color.BLUE
                            }
                        )
                    )
                )
            }
        }
    }
}