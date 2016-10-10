package com.example.roundedspansampleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.roundedspan.RoundedTagSpan;

import java.util.List;

/**
 * Created by yongwei on 10/10/16.
 */

public class RoundedTagSpannableUtils {

    public static final String TAG_FORMAT = "%s";

    public static final int CORNER_RADIUS_DIMEN = R.dimen.rounded_tag_span_corner_radius;
    public static final int TAG_TEXT_SIZE_DIMEN = R.dimen.text_big;
    public static final int TAG_TEXT_VERTICAL_PADDING_DIMEN = R.dimen.tag_vertical_padding;
    public static final int TAG_TEXT_HORIZONTAL_PADDING_DIMEN = R.dimen.tag_horizontal_padding;
    public static final int ICON_TEXT_SPACE_PADDING_DIMEN = R.dimen.icon_space_padding;
    public static final int TAG_WHITE_SPACE_PADDING_DIMEN = R.dimen.tag_white_padding;

    public static SpannableString getSpan(@NonNull Context context,
                                          @NonNull String string,
                                          int backgroundColorResource,
                                          int drawableResource,
                                          @NonNull TextView originalTextView) {

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
                (int) originalTextView.getTextSize(),
                drawableResource);
        tagSpan.setSpan(span, 0, finalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return tagSpan;
    }
}