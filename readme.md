# GoodNumberPicker

`GoodNumberPicker` is a Android library that provides a number picker with customizable UI. It was
developed as alternative to the default `NumberPicker` widget, which is not customizable enough and
has some issues. The library does not have many options itself, but it is easy to expand. Feel free
to request new features or report bugs.

## Getting Started

### Gradle

Add dependency to your `build.gradle` file:

```groovy
implementation 'FILL_IT'
```

### Layout

Add view to your layout:

```xml

<tech.stonks.goodnumberpicker.GoodNumberPicker android:id="@+id/number_picker"
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

| Field                              | Description                                                                                                                                |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `style`                            | Style of the picker, see `GoodNumberPicker.Style`. Basically it contains all the fields that are available in xml                          |
| `onSelectedPositionChanged`        | Listener that will be called when selected position changes. By default, it will be called when user stops scrolling.                      |
| `publishUpdatesOnlyOnAnimationEnd` | If set to `true`, `onSelectedPositionChanged` will be called only when animation ends. Otherwise, it will be called on every scroll event. |
| `selectedPosition`                 | You can set it to change selected position programmatically.                                                                               |
| `items`                            | List of items that will be displayed in the picker.                                                                                        |
| `itemFormatter`                    | Formatter that will be used to transform items                                                                                             |
| `pickerOverlay`                    | Overlay that will be drawn above picked values. By default it will display two lines                                                       |
| `visibleItems`                     | Number of visible items in the picker                                                                                                      |

## Formatting items
You can format each item by applying to it modified style. Here I will show few examples how you can format items.

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
            style.copy(
                textStyle = style.textStyle.copy(
                    textColor = color
                )
            )
        )
    }
}
```

### Different colors based on an index
This example shows how to assign different textColor for various items based on their position. In the same way you can change any field of style to customize item.
```kotlin
numberPicker.onSelectedPositionChanged = { index, item ->
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
```

## Custom Items

## Custom Overlay