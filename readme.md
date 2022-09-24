# GoodNumberPicker

`GoodPicker` is an Android library that provides a picker with customizable UI. It was
developed as alternative to the default `NumberPicker` widget, which is not customizable enough and
has some issues. The library does not have many options itself, but it is easy to expand so you can implement any UI you can imagine. Feel free
to request new features or report bugs.

## Examples

### Standard number picker
<img
alt="Standard number picker"
src="https://github.com/Stonks-tech/GoodNumberPicker/blob/master/media/number-picker.gif" height="200" />


### Number picker with custom overlay

<img
alt="Custom overlay example"
src="https://github.com/Stonks-tech/GoodNumberPicker/blob/master/media/custom-overlay.gif" 
height="200" />

### Drawable picker

<img
alt="Drawable picker"
src="https://github.com/Stonks-tech/GoodNumberPicker/blob/master/media/drawable-picker.gif"
height="200" />


## Getting Started

### Gradle

Add dependency to your `build.gradle` file:

```groovy
implementation 'FILL_IT'
```

### Layout

Add view to your layout:

```xml

<tech.stonks.goodpicker.GoodPicker android:id="@+id/number_picker"
    android:textColor="@color/picker_text_color" android:layout_margin="16dp"
    app:overlayColor="#00ff00" android:layout_marginHorizontal="16dp" android:font="@font/raleway"
    android:layout_gravity="center" android:layout_marginVertical="8dp" android:textFontWeight="900"
    android:textSize="30sp" app:layout_constraintTop_toBottomOf="@id/text_view"
    app:layout_constraintBottom_toBottomOf="parent" app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent" android:layout_width="50dp"
    android:layout_height="250dp" />
```

### Code

Listen to position selection

```kotlin
numberPicker.onSelectedPositionChanged = { position ->
    textView.text = "Position: $position"
}
```

## Customization possibilities

### XML

| Attribute                 | Description                                                                                                |
|---------------------------|------------------------------------------------------------------------------------------------------------|
| `app:overlayColor`        | Color of the overlay lines                                                                                 |
| `android:textColor`       | Color of the text, You might use selector with `state_selected` to specify text color for selected value   |
| `android:textSize`        | Size of the text                                                                                           |
| `android:textFontWeight`  | Weight of the text, will take effect only on API 28 or higher                                              |
| `android:font`            | Font of the text                                                                                           |
| `visibleItems`            | Number of visible items in the picker                                                                      |
| ------------------------- | ---------------------------------------------------------------------------------------------------------- |

### Code

You can also customize your number picker in code

| Field                              | Description                                                                                                                                           |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| `style`                            | Style of the picker, see `GoodNumberPicker.Style`. Basically it contains all the fields that are available in xml                                     |
| `onSelectedPositionChanged`        | Listener that will be called when selected position changes. By default, it will be called when user stops scrolling.                                 |
| `publishUpdatesOnlyOnAnimationEnd` | If set to `true`, `onSelectedPositionChanged` will be called only when animation ends. Otherwise, it will be called on every scroll event.            |
| `selectedPosition`                 | You can set it to change selected position programmatically.                                                                                          |
| `items`                            | List of items that will be displayed in the picker.                                                                                                   |
| `itemFormatter`                    | Formatter that will be used to transform items                                                                                                        |
| `pickerOverlay`                    | Overlay that will be drawn above picked values. By default it will display two lines                                                                  |
| `visibleItems`                     | Number of visible items in the picker                                                                                                                 |
| `tint`                             | Tint of the picker. It will be applied to all drawables in the picker. You can use selector with `state_selected` to specify tint for selected value. |
| `size`                             | Size of the drawable item.                                                                                                                            |

## Formatting items

You can format each item by applying to it modified style. Here I will show few examples how you can
format items.

There are a few ready to use formatters: 
 - `SelectedTextItemFormatter` applies text color from ColorStateList to text items. `state_selected` will be applied to selected item.
 - `SelectedDrawableItemFormatter` applies tint from ColorStateList to drawable items. `state_selected` will be applied to selected item
### Highlighting selected item

This example shows how to customize selected item.

```kotlin
numberPicker.itemFormatter = { index, item ->
    item.apply {
        val color = if (index == numberPicker.selectedPosition) {
            Color.RED
        } else {
            Color.BLACK
        }

        styleChanged(
            style.modifyCustomStyle<TextStyle> {
                this.copy(
                    textColor = color
                )
            }
        )
    }
}
```

### Different colors based on an index

This example shows how to assign different textColor for various items based on their position. In
the same way you can change any field of style to customize item.

```kotlin
numberPicker.onSelectedPositionChanged = { index, item ->
    
    item.apply {
        styleChanged(
            numberPicker.style.modifyCustomStyle<TextStyle> {
                this.copy(
                    textColor = when (index % 3) {
                        0 -> Color.RED
                        1 -> Color.GREEN
                        else -> Color.BLUE
                    }
                )
            }
        )
    }
}
```

## Custom Items
You can implement your completely custom item by extending `PickerItem` interface. It gives you possibility to draw anything on canvas in a `draw` method. Here example of implementation of DrawableNumberPickerItem:
```kotlin
class DrawablePickerItem(val drawable: Drawable) : PickerItem {
    private var _style: GoodPickerStyle = GoodPickerStyle.default
    override val style: GoodPickerStyle
        get() = _style
    private val _drawableStyle: DrawableStyle
        get() {
            return _style.getCustomStyle() ?: throw IllegalStateException("No drawable style found")
        }

    override fun draw(canvas: Canvas, y: Float, width: Int, height: Int) {
        val style = _drawableStyle
        canvas.save()
        canvas.translate(
            (width - style.size) / 2f, //here we center drawable
            y + ((height - style.size) / 2f),
        )
        drawable.setTint(style.tint) //before drawing we apply style
        drawable.setBounds(0, 0, style.size, style.size)
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun styleChanged(style: GoodPickerStyle) {
        _style = style //after assignment you might want to invalidate your field to update paint or other things
    }

    companion object {
        fun fromResource(context: Context, resId: Int): DrawablePickerItem {
            return DrawablePickerItem(
                ContextCompat.getDrawable(context, resId)!!
            )
        }
    }
}
```

## Custom Overlay
You can implement your completely custom overlay by extending `PickerOverlay` interface. It gives you possibility to draw anything on canvas in a `draw` method. Here example of implementation of `DrawablePickerOverlay`:
```kotlin
class DrawablePickerOverlay(
    private val _topDrawable: Drawable,
    private val _bottomDrawable: Drawable
) : PickerOverlay {
    private var _style: GoodPickerStyle = GoodPickerStyle.default
    override fun styleChanged(style: GoodPickerStyle) {
        _style = style
    }

    override fun draw(canvas: Canvas, centerItemRect: Rect) {
        _topDrawable.setBounds(
            0,
            0,
            canvas.width,
            centerItemRect.top
        )
        _topDrawable.draw(canvas)
        _bottomDrawable.setBounds(
            0,
            centerItemRect.bottom,
            canvas.width,
            canvas.height
        )
        _bottomDrawable.draw(canvas)
    }

    companion object {
        fun symmetric(topDrawable: Drawable): DrawablePickerOverlay {
            return DrawablePickerOverlay(topDrawable, RotateDrawable().apply {
                drawable = topDrawable.constantState?.newDrawable()!!.mutate()
                this.fromDegrees = 180f
            })
        }
    }
}
```