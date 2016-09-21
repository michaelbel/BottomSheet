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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomSheetGrid extends LinearLayout {

    private TextView textView;
    private ImageView iconView;

    public BottomSheetGrid(Context context) {
        super(context);

        setOrientation(VERTICAL);
        setPadding(Utils.dp(getContext(), 24), Utils.dp(getContext(), 16),
                Utils.dp(getContext(), 24), Utils.dp(getContext(), 16));

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                Utils.dp(getContext(), 48),
                Utils.dp(getContext(), 48)
        );
        params1.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        iconView.setLayoutParams(params1);
        addView(iconView);

        textView = new TextView(context);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params2.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        textView.setLayoutParams(params2);
        addView(textView);
    }

    public BottomSheetGrid setIcon(@DrawableRes int resId, @ColorInt int color) {
        Drawable icon = ContextCompat.getDrawable(getContext(), resId);
        icon.clearColorFilter();
        icon.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        iconView.setImageDrawable(icon);
        return this;
    }

    public BottomSheetGrid setText(@NonNull CharSequence text, @ColorInt int color) {
        textView.setText(text);
        textView.setTextColor(color);
        return this;
    }
}