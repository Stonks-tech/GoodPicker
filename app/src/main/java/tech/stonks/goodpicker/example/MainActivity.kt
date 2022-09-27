package tech.stonks.goodpicker.example

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import tech.stonks.goodpicker.overlay.DrawablePickerOverlay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<MaterialButton>(R.id.buttonFive)
        val numberPicker = findViewById<tech.stonks.goodpicker.GoodPicker>(
            R.id.number_picker
        )
        numberPicker.onSelectedPositionChanged = { position ->
            textView.text = "Position: $position"
        }

        numberPicker.pickerOverlay = DrawablePickerOverlay.symmetric(
            ContextCompat.getDrawable(
                this,
                R.drawable.overlay_top
            )!!
        )
        button.setOnClickListener {
            numberPicker.selectedPosition = 5
        }
        /* numberPicker.items = listOf(
             DrawableNumberPickerItem.fromResource(this, R.drawable.ic_bike),
             DrawableNumberPickerItem.fromResource(this, R.drawable.ic_car),
             DrawableNumberPickerItem.fromResource(this, R.drawable.ic_transit),
         )
         numberPicker.itemFormatter = SelectedDrawableItemFormatter(
             ContextCompat.getColorStateList(this, R.color.drawable_item_color)!!
         ) {
             it == numberPicker.selectedPosition
         }*/
    }
}