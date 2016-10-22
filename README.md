# Spannable for android

RoundedTagSpan extends ReplacementSpan to allow usage in TextView.

## Features
* icon in span
* different font size
* do not have to deliberately add " "(space char) in string to seperate string
* top/bottom/start/end margins in span
* color
* corner radius

## Example Code
### Creating a span
```
String finalString = String.format(TAG_FORMAT, string);
SpannableString tagSpan = new SpannableString(finalString);
RoundedTagSpan span = new RoundedTagSpan(context,
        CORNER_RADIUS_DIMEN,
        backgroundColorResource,
        android.R.color.white,
        "fonts/Roboto-Regular.ttf",
        TAG_TEXT_SIZE_DIMEN,
        TAG_TEXT_VERTICAL_PADDING_DIMEN,
        TAG_TEXT_HORIZONTAL_PADDING_DIMEN,
        ICON_TEXT_SPACE_PADDING_DIMEN,
        TAG_WHITE_SPACE_PADDING_DIMEN,
        (int) originalTextView.getTextSize(), //for different textsize
        iconDrawableResource);
tagSpan.setSpan(span, 0, finalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
```

### Appending it to textview
```
SpannableString spannableString = RoundedTagSpannableUtils.getSpan(getBaseContext(), "TAG");
textview.append(spannableString);
```

## Sample
<img src="https://github.com/vertigogarden/rounded-tag-span/blob/master/Screenshot_20161011-000028.png" width="300">   Similar Text Size

<img src="https://github.com/vertigogarden/rounded-tag-span/blob/master/Screenshot_20161011-000239.png" width="300">   Smaller Text Size

<img src="https://github.com/vertigogarden/rounded-tag-span/blob/master/Screenshot_20161011-000321.png" width="300">   Bigger Text Size

