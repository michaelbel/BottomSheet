package org.michaelbel.bottomsheet.test;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomSheetCell extends FrameLayout {

    private TextView textView;
    private ImageView iconView;

    public BottomSheetCell(Context context) {
        super(context);

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER);

        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                Utils.dp(context, 24),
                Utils.dp(context, 24)
        );
        params1.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        params1.leftMargin = Utils.dp(context, 16);
        params1.rightMargin = Utils.dp(context, 16);

        iconView.setLayoutParams(params1);
        addView(iconView);

        textView = new TextView(context);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params2.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        params2.leftMargin = Utils.dp(context, 16);
        params2.rightMargin = Utils.dp(context, 16);

        textView.setLayoutParams(params2);
        addView(textView);
    }

    public BottomSheetCell setIcon(@DrawableRes int resId, @ColorInt int color) {
        if (resId != 0) {
            Drawable res = ContextCompat.getDrawable(getContext(), resId);
            res.clearColorFilter();
            res.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

            iconView.setImageDrawable(res);

            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params2.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
            params2.leftMargin = Utils.dp(getContext(), 72);
            params2.rightMargin = Utils.dp(getContext(), 16);

            textView.setLayoutParams(params2);
        } else {
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params2.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
            params2.leftMargin = Utils.dp(getContext(), 16);
            params2.rightMargin = Utils.dp(getContext(), 16);

            textView.setLayoutParams(params2);
        }

        return this;
    }

    public BottomSheetCell setText(@NonNull CharSequence text, @ColorInt int color) {
        textView.setText(text);
        textView.setTextColor(color);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 48), MeasureSpec.EXACTLY));
    }
}