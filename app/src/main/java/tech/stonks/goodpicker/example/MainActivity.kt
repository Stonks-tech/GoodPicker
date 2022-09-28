package tech.stonks.goodpicker.example

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import tech.stonks.goodpicker.overlay.DrawablePickerOverlay

class MainActivity : AppCompatActivity() {
    private val _bottomSheet by lazy {
        TestBottomSheet(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<MaterialButton>(R.id.buttonFive)
        val numberPicker = findViewById<tech.stonks.goodpicker.GoodPicker>(
            R.id.number_picker
        )
        numberPicker.selectedPosition = 5
        numberPicker.onSelectedPositionChanged = { position ->
            textView.text = "Position: $position"
        }
        button.setOnClickListener {
            if(!_bottomSheet.isShowing) {
                _bottomSheet.show()
            }
        }
    }
}