package com.example.roundedspan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;

import java.lang.ref.WeakReference;

/**
 * Created by yongwei on 3/8/16.
 */

public class RoundedTagSpan extends ReplacementSpan implements LineHeightSpan {
    private Context context;
    private int cornerRadius;
    private int backgroundColor;
    private int textColor;
    private Typeface typeface;
    private int tagTextSize;
    private int tagTextVerticalPadding;
    private int tagTextHorizontalPadding;
    private int iconTextSpacePadding;
    private int tagWhiteSpacePadding;
    private int originalTextSize;
    private WeakReference<Drawable> drawableRef;
    private WeakReference<Bitmap> leftIconBitmapRef;
    private int leftIconDrawableResource;

    private int originalTextBoundHeight;
    private int tagTextBoundHeight;
    private int scaleTagToTextCenter;

    Paint.FontMetrics originalFontMetrics;
    Paint.FontMetrics tagFontMetrics;
    private int chooseHeightAddTop;
    private int chooseHeighAddBottom;

    public RoundedTagSpan(Context context, int cornerRadiusResource, int backgroundColorResource,
                          int textColorResource, String typefaceAsset, int tagTextSizeDimenResource,
                          int tagTextVerticalPaddingDimenResource, int tagTextHorizontalPaddingDimenResource,
                          int iconTextSpacePaddingDimenResource, int tagWhiteSpacePaddingDimenResource,
                          int originalTextSize, int leftIconDrawableResource) {
        super();
        this.context = context;
        setCornerRadiusDimenResource(cornerRadiusResource);
        setBackgroundColorResource(backgroundColorResource);
        setTextColorResource(textColorResource);
        setTypeface(typefaceAsset);
        setTagTextSizeDimenResource(tagTextSizeDimenResource);
        setTagTextVerticalPaddingDimenResource(tagTextVerticalPaddingDimenResource);
        setTagTextHorizontalPaddingDimenResource(tagTextHorizontalPaddingDimenResource);
        setIconTextSpacePaddingDimenResource(iconTextSpacePaddingDimenResource);
        setTagWhiteSpacePaddingDimenResource(tagWhiteSpacePaddingDimenResource);
        setOriginalTextSize(originalTextSize);
        setLeftIconDrawableResource(leftIconDrawableResource);

        /* Measure the height of the text, as paint.getFontMetrics().alignment will
         * have spacing on top. Calculate the halved difference of text height, to derive how
         * much tag has to move. Both tag and text start from the same baseline.
         */
        originalTextBoundHeight = measureTextBoundHeight(originalTextSize);
        tagTextBoundHeight = measureTextBoundHeight(tagTextSize);
        scaleTagToTextCenter = (originalTextBoundHeight - tagTextBoundHeight) / 2;

        measureChooseHeightRequired();
    }

    private void setLeftIconDrawableResource(int leftIconDrawableResource) {
        this.leftIconDrawableResource = leftIconDrawableResource;
    }

    public void setBackgroundColorResource(int colorResource) {
        setBackgroundColor(colorResource == 0 ? 0 : context.getResources().getColor(colorResource));
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColorResource(int colorResource) {
        setTextColor(colorResource == 0 ? 0 : context.getResources().getColor(colorResource));
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTagTextSizeDimenResource(int dimenResource) {
        setTagTextSize(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setTagTextSize(int tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    public void setTagTextVerticalPaddingDimenResource(int dimenResource) {
        setTagTextVerticalPadding(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setTagTextVerticalPadding(int tagTextVerticalPadding) {
        this.tagTextVerticalPadding = tagTextVerticalPadding;
    }

    public void setTagTextHorizontalPaddingDimenResource(int dimenResource) {
        setTagTextHorizontalPadding(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setTagTextHorizontalPadding(int tagTextHorizontalPadding) {
        this.tagTextHorizontalPadding = tagTextHorizontalPadding;
    }

    public void setIconTextSpacePaddingDimenResource(int dimenResource) {
        setIconTextSpacePadding(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setIconTextSpacePadding(int iconTextSpacePadding) {
        this.iconTextSpacePadding = iconTextSpacePadding;
    }

    public void setTagWhiteSpacePaddingDimenResource(int dimenResource) {
        setTagWhiteSpacePadding(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setTagWhiteSpacePadding(int tagWhiteSpacePadding) {
        this.tagWhiteSpacePadding = tagWhiteSpacePadding;
    }

    public void setOriginalTextSizeDimenResource(int dimenResource) {
        setOriginalTextSize(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setOriginalTextSize(int originalTextSize) {
        this.originalTextSize = originalTextSize;
    }

    public void setTypeface(String asset) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), asset));
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setCornerRadiusDimenResource(int dimenResource) {
        setCornerRadius(dimenResource == 0 ? 0 :
                context.getResources().getDimensionPixelSize(dimenResource));
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                     int bottom, Paint paint) {
        // initialise values
        Bitmap bitmap = getCachedIconBitmap();
        float bitmapWidthWithPadding = 0;
        float bitmapTop = 0;
        float bitmapStart = 0;

        if (getCachedIconBitmap() != null) {
            bitmapWidthWithPadding = bitmap.getWidth() + iconTextSpacePadding;
            bitmapTop = y - (getCachedIconBitmap().getHeight() / 2) - (originalTextBoundHeight / 2);
            bitmapStart = x + tagTextHorizontalPadding + tagWhiteSpacePadding;
        }

        float rectBottom = (y + tagTextVerticalPadding) - scaleTagToTextCenter;
        float rectTop = (y - tagTextVerticalPadding) - tagTextBoundHeight - scaleTagToTextCenter;
        float rectStart = x + tagWhiteSpacePadding;
        float rectEnd = x + measureText(paint, text, start, end) + bitmapWidthWithPadding +
                (tagTextHorizontalPadding + tagTextHorizontalPadding) + tagWhiteSpacePadding;

        float textBottom = y - scaleTagToTextCenter;
        float textStart = x + bitmapWidthWithPadding + tagTextHorizontalPadding + tagWhiteSpacePadding;

        // draw canvas,
        // note: order is important
        paint.setTypeface(typeface);
        paint.setTextSize(tagTextSize);
        paint.setColor(backgroundColor);

        RectF rect = new RectF(rectStart, rectTop, rectEnd, rectBottom);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, bitmapStart, bitmapTop, paint);
        }

        paint.setColor(textColor);

        canvas.drawText(text, start, end, textStart, textBottom, paint);
    }

    @Nullable
    private Bitmap getCachedIconBitmap() {
        WeakReference<Bitmap> wr = leftIconBitmapRef;
        Bitmap bitmap = null;

        if (wr != null)
            bitmap = wr.get();

        if (bitmap == null) {
            bitmap = drawableToBitmap(getCachedDrawable());
            leftIconBitmapRef = new WeakReference<>(bitmap);
        }

        return bitmap;
    }

    @Nullable
    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = drawableRef;
        Drawable drawable = null;

        if (wr != null)
            drawable = wr.get();

        if (drawable == null) {
            try {
                drawable = ResourcesCompat.getDrawable(context.getResources(), leftIconDrawableResource, null);
            } catch (Resources.NotFoundException e) {
            }
            drawableRef = new WeakReference<>(drawable);
        }

        return drawable;
    }

    @Nullable
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.setTextSize(tagTextSize);
        int bitmapWidthWithPadding = getCachedIconBitmap() == null ? 0 :
                (getCachedIconBitmap().getWidth() + iconTextSpacePadding);
        return Math.round(paint.measureText(text, start, end) +
                bitmapWidthWithPadding +
                (tagTextHorizontalPadding + tagTextHorizontalPadding) +
                (tagWhiteSpacePadding + tagWhiteSpacePadding));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        paint.setTypeface(typeface);
        paint.setTextSize(tagTextSize);
        return paint.measureText(text, start, end);
    }

    private int measureTextHeight(Paint paint, String text) {
        Rect result = new Rect();
        paint.getTextBounds(text.toString(), 0, text.toString().length(), result);
        return result.height();
    }

    private int measureTextBoundHeight(int originalTextSize) {
        // note might need to set type face if original and tag typeface different
        Paint paint = new Paint();
        paint.setTextSize(originalTextSize);
        return measureTextHeight(paint, "T");
    }

    /* initialised measurements to determine if we need to override height of span in chooseHeight()
     * if tag will be longer than original font's top/bottom, we will calculate the difference in
     * height of tag and original text height.
     */
    private void measureChooseHeightRequired() {
        Paint originalSizePaint = new Paint();
        originalSizePaint.setTextSize(originalTextSize);
        originalSizePaint.setTypeface(typeface);

        Paint tagSizePaint = new Paint();
        tagSizePaint.setTextSize(tagTextSize);
        tagSizePaint.setTypeface(typeface);

        originalFontMetrics = originalSizePaint.getFontMetrics();
        tagFontMetrics = tagSizePaint.getFontMetrics();

        chooseHeightAddTop = tagTextVerticalPadding - ((int) -(originalFontMetrics.top - tagFontMetrics.top)) - scaleTagToTextCenter;
        chooseHeighAddBottom = tagTextVerticalPadding - ((int) (originalFontMetrics.bottom - tagFontMetrics.bottom) - scaleTagToTextCenter);
    }

    // this method is called to measure height of span
    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
        if (chooseHeightAddTop > 0) {
            fm.top = (int) originalFontMetrics.top - chooseHeightAddTop;
            fm.ascent = (int) originalFontMetrics.ascent - chooseHeightAddTop;
        }
        if (chooseHeighAddBottom > 0) {
            fm.bottom = (int) originalFontMetrics.bottom + chooseHeighAddBottom;
            fm.descent = (int) originalFontMetrics.descent + chooseHeighAddBottom;
        }
    }
}
