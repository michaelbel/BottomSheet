/*
 * Copyright 2016 Michael Bel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.michaelbel.bottomsheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static android.support.annotation.RestrictTo.Scope.GROUP_ID;

@SuppressWarnings("all")
public class BottomSheet extends Dialog {

    private static final String TAG = BottomSheet.class.getSimpleName();

    public static final int LIST = 1;
    public static final int GRID = 2;

    @RestrictTo(GROUP_ID)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST, GRID})
    public @interface Type {}

    private boolean fullWidth;
    private boolean darkTheme;

    private @ColorInt int backgroundColor;
    private @ColorInt int titleTextColor;
    private @ColorInt int itemTextColor;
    private @ColorInt int iconColor;
    private int itemSelector;

    private View customView;
    private CharSequence titleText;

    private int contentType = LIST;

    private CharSequence[] mItems;
    private @StringRes int[] mItemsRes;
    private @DrawableRes int[] mIcons;

    private ArrayList<Item> items = new ArrayList<>();

    private ViewGroup containerView;
    private ContainerView container;
    private WindowInsets lastInsets;
    private Runnable startAnimationRunnable;
    private int layoutCount;
    private boolean dismissed;
    private OnClickListener onClickListener;
    private ColorDrawable backDrawable = new ColorDrawable(0xFF000000);
    private boolean allowCustomAnimation = true;
    private int touchSlop;
    private boolean useFastDismiss;
    private boolean focusable;
    private Drawable shadowDrawable;
    private static int backgroundPaddingTop;
    private static int backgroundPaddingLeft;
    private AnimatorSet currentSheetAnimation;

    private Point displaySize = new Point();
    private DisplayMetrics metrics = new DisplayMetrics();
    private Handler handler = new Handler(Looper.getMainLooper());

    private Callback bottomSheetCallBack;

    private class Item {

        public int icon;
        public CharSequence text;

        public Item(CharSequence text, int icon) {
            this.text = text;
            this.icon = icon;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (backgroundColor == 0) {
            backgroundColor = darkTheme ? 0xFF424242 : 0xFFFFFFFF;
        }

        if (titleTextColor == 0) {
            titleTextColor = darkTheme ? 0xB3FFFFFF : 0x8A000000;
        }

        if (itemTextColor == 0) {
            itemTextColor = darkTheme ? 0xFFFFFFFF : 0xDE000000;
        }

        if (iconColor == 0) {
            iconColor = darkTheme ? 0xFFFFFFFF : 0x8A000000;
        }

        if (itemSelector == 0) {
            itemSelector = darkTheme ? R.drawable.selectable_dark : R.drawable.selectable_light;
        }

        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(container, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (containerView == null) {
            containerView = new FrameLayout(getContext()) {
                @Override
                public boolean hasOverlappingRendering() {
                    return false;
                }
            };
            if (Build.VERSION.SDK_INT >= 16) {
                containerView.setBackground(shadowDrawable);
            } else {
                containerView.setBackgroundDrawable(shadowDrawable);
            }
            containerView.setPadding(0, backgroundPaddingTop, 0, Utils.dp(getContext(), 8));
        }

        if (Build.VERSION.SDK_INT >= 21) {
            containerView.setFitsSystemWindows(true);
        }

        containerView.setVisibility(View.INVISIBLE);
        containerView.setBackgroundColor(backgroundColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;

        containerView.setLayoutParams(params);
        container.addView(containerView, 0);

        if (customView != null) {
            if (customView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) customView.getParent();
                viewGroup.removeView(customView);
            }

            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) containerView.getLayoutParams();
            params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params1.gravity = Gravity.START | Gravity.TOP;

            containerView.addView(customView, params1);
        } else {
            int topOffset = 0;

            if (titleText != null) {
                TextView titleTextView = new TextView(getContext());
                titleTextView.setLines(1);
                titleTextView.setMaxLines(1);
                titleTextView.setSingleLine(true);
                titleTextView.setText(titleText);
                titleTextView.setTextColor(titleTextColor);
                titleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                titleTextView.setGravity(Gravity.CENTER_VERTICAL);

                FrameLayout.LayoutParams params0 = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dp(getContext(), 56));
                params0.gravity = Gravity.START | Gravity.TOP;
                params0.leftMargin = Utils.dp(getContext(), 16);
                params0.rightMargin = Utils.dp(getContext(), 16);

                titleTextView.setLayoutParams(params0);
                containerView.addView(titleTextView);
                titleTextView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                topOffset += 56;
            }

            BottomSheetAdapter adapter = new BottomSheetAdapter();

            if (mItems != null || mItemsRes != null) {
                if (contentType == LIST) {
                    FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin = Utils.dp(getContext(), topOffset);

                    ListView listView = new ListView(getContext());
                    listView.setSelector(itemSelector);
                    listView.setDividerHeight(0);
                    listView.setAdapter(adapter);
                    listView.setDrawSelectorOnTop(true);
                    listView.setVerticalScrollBarEnabled(false);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            dismissWithButtonClick(i);
                        }
                    });
                    listView.setLayoutParams(params2);
                    containerView.addView(listView);
                } else if (contentType == GRID) {
                    FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    GridView gridView = new GridView(getContext());
                    gridView.setSelector(itemSelector);
                    gridView.setAdapter(adapter);
                    gridView.setNumColumns(3);
                    gridView.setVerticalScrollBarEnabled(false);
                    gridView.setVerticalSpacing(Utils.dp(getContext(), 16));
                    gridView.setPadding(Utils.dp(getContext(), 0), Utils.dp(getContext(), topOffset + 8), Utils.dp(getContext(), 0), Utils.dp(getContext(), 16));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            dismissWithButtonClick(i);
                        }
                    });
                    gridView.setLayoutParams(params3);
                    containerView.addView(gridView);
                }

                if (mItems != null) {
                    for (int a = 0; a < mItems.length; a++) {
                        items.add(new Item(mItems[a], mIcons != null ? mIcons[a] : 0));
                    }
                } else {
                    for (int a = 0; a < mItemsRes.length; a++) {
                        items.add(new Item(getContext().getText(mItemsRes[a]), mIcons != null ? mIcons[a] : 0));
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }

        WindowManager.LayoutParams params4 = window.getAttributes();
        params4.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params4.gravity = Gravity.TOP | Gravity.START;
        params4.dimAmount = 0;
        params4.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        if (!focusable) {
            params4.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        }
        params4.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params4);
    }

    @Override
    public void show() {
        super.show();
        if (focusable) {
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        dismissed = false;
        cancelSheetAnimation();

        if (containerView.getMeasuredHeight() == 0) {
            containerView.measure(View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.AT_MOST));
        }

        backDrawable.setAlpha(0);

        if (Build.VERSION.SDK_INT >= 18) {
            layoutCount = 2;
            handler.postDelayed(startAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    if (startAnimationRunnable != this) {
                        return;
                    }

                    startAnimationRunnable = null;
                    startOpenAnimation();
                }
            }, 150);
        } else {
            startOpenAnimation();
        }

        if (bottomSheetCallBack != null) {
            bottomSheetCallBack.onOpen();
        }
    }

    @Override
    public void dismiss() {
        if (dismissed) {
            return;
        }

        dismissed = true;
        cancelSheetAnimation();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(containerView, "translationY", containerView.getMeasuredHeight() + Utils.dp(getContext(), 10)),
                ObjectAnimator.ofInt(backDrawable, "alpha", 0)
        );
        if (useFastDismiss) {
            int height = containerView.getMeasuredHeight();
            animatorSet.setDuration(Math.max(60, (int) (180 * (height - containerView.getTranslationY()) / (float) height)));
            useFastDismiss = false;
        } else {
            animatorSet.setDuration(180);
        }
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dismissInternal();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                }
            }
        });
        animatorSet.start();
        currentSheetAnimation = animatorSet;

        if (bottomSheetCallBack != null) {
            bottomSheetCallBack.onClose();
        }
    }

    public BottomSheet(Context context, boolean needFocus) {
        super(context, R.style.TransparentDialog);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            );
        }

        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledTouchSlop();

        Rect padding = new Rect();

        shadowDrawable = ContextCompat.getDrawable(context, R.drawable.sheet_shadow);

        shadowDrawable.getPadding(padding);
        backgroundPaddingLeft = padding.left;
        backgroundPaddingTop = padding.top;

        container = new ContainerView(getContext());
        if (Build.VERSION.SDK_INT >= 16) {
            container.setBackground(backDrawable);
        } else {
            container.setBackgroundDrawable(backDrawable);
        }

        focusable = needFocus;

        if (Build.VERSION.SDK_INT >= 21) {
            container.setFitsSystemWindows(true);
            container.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @SuppressLint("NewApi")
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    lastInsets = insets;
                    v.requestLayout();
                    return insets.consumeSystemWindowInsets();
                }
            });
            container.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        backDrawable.setAlpha(0);
    }

    private class ContainerView extends FrameLayout implements NestedScrollingParent {

        private int startedTrackingX;
        private int startedTrackingY;
        private int startedTrackingPointerId;

        private boolean maybeStartTracking = false;
        private boolean startedTracking = false;

        private AnimatorSet currentAnimation = null;
        private VelocityTracker velocityTracker = null;
        private NestedScrollingParentHelper nestedScrollingParentHelper;

        public ContainerView(Context context) {
            super(context);
            nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }

        @Override
        public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
            return !dismissed && nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && !canDismissWithSwipe();
        }

        @Override
        public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
            nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);

            if (dismissed) {
                return;
            }

            cancelCurrentAnimation();
        }

        @Override
        public void onStopNestedScroll(View target) {
            nestedScrollingParentHelper.onStopNestedScroll(target);
            if (dismissed) {
                return;
            }
            checkDismiss(0, 0);
        }

        @Override
        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dismissed) {
                return;
            }

            cancelCurrentAnimation();

            if (dyUnconsumed != 0) {
                float currentTranslation = containerView.getTranslationY();
                currentTranslation -= dyUnconsumed;

                if (currentTranslation < 0) {
                    currentTranslation = 0;
                }

                containerView.setTranslationY(currentTranslation);
            }
        }

        @Override
        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            if (dismissed) {
                return;
            }

            cancelCurrentAnimation();
            float currentTranslation = containerView.getTranslationY();

            if (currentTranslation > 0 && dy > 0) {
                currentTranslation -= dy;
                consumed[1] = dy;

                if (currentTranslation < 0) {
                    currentTranslation = 0;
                    consumed[1] += currentTranslation;
                }

                containerView.setTranslationY(currentTranslation);
            }
        }

        @Override
        public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        @Override
        public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public int getNestedScrollAxes() {
            return nestedScrollingParentHelper.getNestedScrollAxes();
        }

        private void checkDismiss(float velX, float velY) {
            float translationY = containerView.getTranslationY();
            boolean backAnimation = translationY < getPixelsInCM(0.8f, false) && (velY < 3500 || Math.abs(velY) < Math.abs(velX)) || velY < 0 && Math.abs(velY) >= 3500;

            if (!backAnimation) {
                boolean allowOld = allowCustomAnimation;
                allowCustomAnimation = false;
                useFastDismiss = true;
                dismiss();
                allowCustomAnimation = allowOld;
            } else {
                currentAnimation = new AnimatorSet();
                currentAnimation.playTogether(ObjectAnimator.ofFloat(containerView, "translationY", 0));
                currentAnimation.setDuration((int) (150 * (translationY / getPixelsInCM(0.8f, false))));
                currentAnimation.setInterpolator(new DecelerateInterpolator());
                currentAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (currentAnimation != null && currentAnimation.equals(animation)) {
                            currentAnimation = null;
                        }
                    }
                });
                currentAnimation.start();
            }
        }

        private void cancelCurrentAnimation() {
            if (currentAnimation != null) {
                currentAnimation.cancel();
                currentAnimation = null;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (dismissed) {
                return false;
            }

            if (ev != null && (ev.getAction() == MotionEvent.ACTION_DOWN ||
                    ev.getAction() == MotionEvent.ACTION_MOVE) && !startedTracking && !maybeStartTracking) {
                startedTrackingX = (int) ev.getX();
                startedTrackingY = (int) ev.getY();

                if (startedTrackingY < containerView.getTop() ||
                        startedTrackingX < containerView.getLeft() ||
                        startedTrackingX > containerView.getRight()) {
                    dismiss();
                    return true;
                }

                startedTrackingPointerId = ev.getPointerId(0);
                maybeStartTracking = true;
                cancelCurrentAnimation();

                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
            } else if (ev != null && ev.getAction() == MotionEvent.ACTION_MOVE &&
                    ev.getPointerId(0) == startedTrackingPointerId) {
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                }

                float dx = Math.abs((int) (ev.getX() - startedTrackingX));
                float dy = (int) ev.getY() - startedTrackingY;
                velocityTracker.addMovement(ev);

                if (maybeStartTracking && !startedTracking && (dy > 0 && dy / 3.0f > Math.abs(dx) && Math.abs(dy) >= touchSlop)) {
                    startedTrackingY = (int) ev.getY();
                    maybeStartTracking = false;
                    startedTracking = true;
                    requestDisallowInterceptTouchEvent(true);
                } else if (startedTracking) {
                    float translationY = containerView.getTranslationY();
                    translationY += dy;
                    if (translationY < 0) {
                        translationY = 0;
                    }

                    containerView.setTranslationY(translationY);
                    startedTrackingY = (int) ev.getY();
                }
            } else if (ev == null || ev.getPointerId(0) == startedTrackingPointerId && (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_POINTER_UP)) {
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                }

                velocityTracker.computeCurrentVelocity(1000);
                float translationY = containerView.getTranslationY();

                if (startedTracking || translationY != 0) {
                    checkDismiss(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
                    startedTracking = false;
                } else {
                    maybeStartTracking = false;
                    startedTracking = false;
                }

                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
            }

            return startedTracking;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            boolean isPortrait = width < height;

            if (lastInsets != null && Build.VERSION.SDK_INT >= 21) {
                width -= lastInsets.getSystemWindowInsetRight() + lastInsets.getSystemWindowInsetLeft();
                height -= lastInsets.getSystemWindowInsetBottom();
            }

            setMeasuredDimension(width, height);

            if (containerView != null) {
                if (fullWidth) {
                    containerView.measure(MeasureSpec.makeMeasureSpec(width +
                            backgroundPaddingLeft * 2, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                } else {
                    int widthSpec;

                    if (Utils.isTablet(getContext())) {
                        widthSpec = MeasureSpec.makeMeasureSpec((int)
                                (Math.min(displaySize.x, displaySize.y) * 0.8f) +
                                backgroundPaddingLeft * 2, MeasureSpec.EXACTLY);
                    } else {
                        widthSpec = MeasureSpec.makeMeasureSpec(isPortrait ? width +
                                backgroundPaddingLeft * 2 : (int) Math.max(width * 0.8f,
                                Math.min(Utils.dp(getContext(), 480), width)) +
                                backgroundPaddingLeft * 2, MeasureSpec.EXACTLY);
                    }

                    containerView.measure(widthSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                }
            }

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == GONE || child == containerView) {
                    continue;
                }
                measureChildWithMargins(child, MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        0, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY), 0);
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            layoutCount--;

            if (containerView != null) {
                int t = (bottom - top) - containerView.getMeasuredHeight();

                if (lastInsets != null && Build.VERSION.SDK_INT >= 21) {
                    left += lastInsets.getSystemWindowInsetLeft();
                    right += lastInsets.getSystemWindowInsetLeft();
                }

                int l = ((right - left) - containerView.getMeasuredWidth()) / 2;
                containerView.layout(l, t, l + containerView.getMeasuredWidth(), t + containerView.getMeasuredHeight());
            }

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == GONE || child == containerView) {
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int childLeft;
                int childTop;
                int gravity = lp.gravity;

                if (gravity == -1) {
                    gravity = Gravity.TOP | Gravity.START;//left
                }

                final int absoluteGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = (right - left - width) / 2 + lp.leftMargin - lp.rightMargin;
                        break;
                    case Gravity.END:
                        childLeft = right - width - lp.rightMargin;
                        break;
                    case Gravity.START:
                    default:
                        childLeft = lp.leftMargin;
                }

                switch (verticalGravity) {
                    case Gravity.TOP:
                        childTop = lp.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = (bottom - top - height) / 2 + lp.topMargin - lp.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = (bottom - top) - height - lp.bottomMargin;
                        break;
                    default:
                        childTop = lp.topMargin;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }

            if (layoutCount == 0 && startAnimationRunnable != null) {
                handler.removeCallbacks(startAnimationRunnable);
                startAnimationRunnable.run();
                startAnimationRunnable = null;
            }
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (canDismissWithSwipe()) {
                return onTouchEvent(event);
            }

            return super.onInterceptTouchEvent(event);
        }

        @Override
        public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (maybeStartTracking && !startedTracking) {
                onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

        @Override
        public boolean hasOverlappingRendering() {
            return false;
        }
    }

    private boolean canDismissWithSwipe() {
        return true;
    }

    private void cancelSheetAnimation() {
        if (currentSheetAnimation != null) {
            currentSheetAnimation.cancel();
            currentSheetAnimation = null;
        }
    }

    private void startOpenAnimation() {
        containerView.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 20) {
            container.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        containerView.setTranslationY(containerView.getMeasuredHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(containerView, "translationY", 0),
                ObjectAnimator.ofInt(backDrawable, "alpha", 51));
        animatorSet.setDuration(200);
        animatorSet.setStartDelay(20);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                    container.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                }
            }
        });
        animatorSet.start();
        currentSheetAnimation = animatorSet;
    }

    private void dismissWithButtonClick(final int viewId) {
        if (dismissed) {
            return;
        }

        dismissed = true;
        cancelSheetAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(containerView, "translationY", containerView.getMeasuredHeight() + Utils.dp(getContext(), 10)),
                ObjectAnimator.ofInt(backDrawable, "alpha", 0)
        );
        animatorSet.setDuration(180);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                    if (onClickListener != null) {
                        onClickListener.onClick(BottomSheet.this, viewId);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BottomSheet.super.dismiss();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (currentSheetAnimation != null && currentSheetAnimation.equals(animation)) {
                    currentSheetAnimation = null;
                }
            }
        });

        animatorSet.start();
        currentSheetAnimation = animatorSet;

        if (bottomSheetCallBack != null) {
            bottomSheetCallBack.onClose();
        }
    }

    private void dismissInternal() {
        super.dismiss();
    }

    private float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? metrics.xdpi : metrics.ydpi);
    }

    public static class Builder {

        private Context context;
        private BottomSheet bottomSheet;

        public Builder(@NonNull Context context) {
            this.context = context;
            bottomSheet = new BottomSheet(context, false);
        }

        public Builder(@NonNull Context context, boolean focus) {
            this.context = context;
            bottomSheet = new BottomSheet(context, focus);
        }

        public Builder(@NonNull Context context, @BoolRes int needFocus) {
            this.context = context;
            bottomSheet = new BottomSheet(context, context.getResources().getBoolean(needFocus));
        }

        public Builder setItems(@NonNull CharSequence[] items, final OnClickListener onClickListener) {
            bottomSheet.mItems = items;
            bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setContentType(@Type int type) {
            bottomSheet.contentType = type;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            bottomSheet.backgroundColor = color;
            return this;
        }

        public Builder setItems(@StringRes int[] items, final OnClickListener onClickListener) {
            bottomSheet.mItemsRes = items;
            bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setItems(@NonNull CharSequence[] items, int[] icons, final OnClickListener onClickListener) {
            bottomSheet.mItems = items;
            bottomSheet.mIcons = icons;
            bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setItems(@StringRes int[] items, int[] icons, final OnClickListener onClickListener) {
            bottomSheet.mItemsRes = items;
            bottomSheet.mIcons = icons;
            bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setCustomView(@NonNull View view) {
            bottomSheet.customView = view;
            return this;
        }

        public Builder setCustomView(@LayoutRes int layoutId) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            setCustomView(layoutInflater.inflate(layoutId, null));
            return this;
        }

        public Builder setTitle(@NonNull CharSequence title) {
            bottomSheet.titleText = title;
            return this;
        }

        public Builder setTitle(@StringRes int stringId) {
            setTitle(context.getText(stringId));
            return this;
        }

        public Builder setTitleTextColor(@ColorInt int color) {
            bottomSheet.titleTextColor = color;
            return this;
        }

        public Builder setItemTextColor(@ColorInt int color) {
            bottomSheet.itemTextColor = color;
            return this;
        }

        public Builder setDarkTheme(boolean value) {
            bottomSheet.darkTheme = value;
            return this;
        }

        public Builder setIconColor(@ColorInt int color) {
            bottomSheet.iconColor = color;
            return this;
        }

        public Builder setFullWidth(boolean value) {
            bottomSheet.fullWidth = value;
            return this;
        }

        public Builder setItemSelector(int selector) {
            bottomSheet.itemSelector = selector;
            return this;
        }

        public Builder setCallback(Callback callback) {
            bottomSheet.bottomSheetCallBack = callback;
            return this;
        }

        public BottomSheet show() {
            bottomSheet.show();
            return bottomSheet;
        }
    }

    private class BottomSheetAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);

            Item item = items.get(i);

            if (type == 0) {
                if (contentType == LIST) {
                    if (view == null) {
                        view = new BottomSheetCell(getContext());
                    }

                    BottomSheetCell cell = (BottomSheetCell) view;
                    cell.setIcon(item.icon, iconColor);
                    cell.setText(item.text, itemTextColor);
                } else {
                    if (view == null) {
                        view = new BottomSheetGrid(getContext());
                    }

                    BottomSheetGrid cell = (BottomSheetGrid) view;
                    cell.setIcon(item.icon, iconColor);
                    cell.setText(item.text, itemTextColor);
                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }
    }

    public interface Callback {

        void onOpen();

        void onClose();
    }
}