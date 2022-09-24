package tech.stonks.goodnumberpicker.example

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import tech.stonks.goodnumberpicker.overlay.DrawablePickerOverlay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val numberPicker = findViewById<tech.stonks.goodnumberpicker.GoodNumberPicker>(
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