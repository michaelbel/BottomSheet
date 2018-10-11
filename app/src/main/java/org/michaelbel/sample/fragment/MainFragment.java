package org.michaelbel.sample.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.BottomSheetCallback;
import org.michaelbel.bottomsheet.Utils;
import org.michaelbel.sample.activity.MainActivity;
import org.michaelbel.sample.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    private int[] items = new int[] {
        R.string.share,
        R.string.upload,
        R.string.copy,
        R.string.print_this_page
    };

    /*private int[] items = new int[] {
        R.string.preview,
        R.string.share,
        R.string.get_link,
        R.string.make_copy
    };*/

    private int[] icons = new int[] {
        R.drawable.ic_share,
        R.drawable.ic_upload,
        R.drawable.ic_copy,
        R.drawable.ic_print
    };

    private boolean theme;
    private MainActivity activity;

    private Button dialogButton;

    private CheckBox titleCheckBox;
    private CheckBox titleMultiCheckBox;
    private CheckBox iconsCheckBox;
    private CheckBox dividersCheckBox;
    private CheckBox fullWidthCheckBox;
    private CheckBox callbackCheckBox;
    private CheckBox fabCheckBox;
    private CheckBox wdCheckBox;
    private CheckBox chCheckBox;

    private RadioButton slideUpRadio;
    private RadioButton showHideRadio;

    private SeekBar wdSeekBar;
    private SeekBar chSeekBar;

    private TextView wdText;
    private TextView chText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container);

        SharedPreferences prefs = activity.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        theme = prefs.getBoolean("theme", true);

        dialogButton = view.findViewById(R.id.dialog_button);
        dialogButton.setOnClickListener(this);

        titleCheckBox = view.findViewById(R.id.check_title);
        titleMultiCheckBox = view.findViewById(R.id.check_title_multi);
        iconsCheckBox = view.findViewById(R.id.check_icons);
        dividersCheckBox = view.findViewById(R.id.check_dividers);
        fullWidthCheckBox = view.findViewById(R.id.check_full_width);
        callbackCheckBox = view.findViewById(R.id.check_callback);
        fabCheckBox = view.findViewById(R.id.check_fab);
        fabCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            slideUpRadio.setEnabled(isChecked);
            showHideRadio.setEnabled(isChecked);
        });
        wdCheckBox = view.findViewById(R.id.check_wd);
        wdCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> wdSeekBar.setEnabled(isChecked));
        chCheckBox = view.findViewById(R.id.check_ch);
        chCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> chSeekBar.setEnabled(isChecked));

        slideUpRadio = view.findViewById(R.id.radio_slide_up);
        slideUpRadio.setChecked(true);
        slideUpRadio.setEnabled(false);
        slideUpRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            slideUpRadio.setChecked(isChecked);
            showHideRadio.setChecked(!isChecked);
        });

        showHideRadio = view.findViewById(R.id.radio_show_hide);
        showHideRadio.setChecked(false);
        showHideRadio.setEnabled(false);
        showHideRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showHideRadio.setChecked(isChecked);
            slideUpRadio.setChecked(!isChecked);
        });

        wdSeekBar = view.findViewById(R.id.wd_seekbar);
        wdSeekBar.setEnabled(false);
        wdSeekBar.setMax(255);
        wdSeekBar.setProgress(80);
        wdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wdText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        wdText = view.findViewById(R.id.wd_text);
        wdText.setText(String.valueOf(wdSeekBar.getProgress()));

        chSeekBar = view.findViewById(R.id.ch_seekbar);
        chSeekBar.setEnabled(false);
        chSeekBar.setMax(16);
        chSeekBar.setProgress(0);
        chSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chText.setText(String.valueOf(progress + 48));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        chText = view.findViewById(R.id.ch_text);
        chText.setText(String.valueOf(chSeekBar.getProgress() + 48));

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == dialogButton) {
            BottomSheet.Builder builder = new BottomSheet.Builder(activity);
            builder.setDarkTheme(!theme);
            if (titleCheckBox.isChecked()) {
                builder.setTitle(R.string.title_text);
            }
            if (titleMultiCheckBox.isChecked()) {
                builder.setTitle(R.string.title_multiline_text);
                builder.setTitleMultiline(true);
            }
            if (iconsCheckBox.isChecked()) {
                builder.setItems(items, icons, (dialogInterface, i) ->
                    Toast.makeText(activity, items[i], Toast.LENGTH_SHORT).show()
                );
            } else {
                builder.setItems(items, (dialogInterface, i) ->
                    Toast.makeText(activity, items[i], Toast.LENGTH_SHORT).show()
                );
            }
            builder.setDividers(dividersCheckBox.isChecked());
            builder.setFullWidth(fullWidthCheckBox.isChecked());
            if (callbackCheckBox.isChecked()) {
                builder.setCallback(new BottomSheetCallback() {
                    @Override
                    public void onShown() {
                        Toast.makeText(activity, R.string.shown, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismissed() {
                        Toast.makeText(activity, R.string.dismissed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (fabCheckBox.isChecked()) {
                if (slideUpRadio.isChecked()) {
                    builder.setFabBehavior(activity.fab, BottomSheet.FAB_SLIDE_UP);
                } else {
                    builder.setFabBehavior(activity.fab);
                }
            }
            if (wdCheckBox.isChecked()) {
                builder.setWindowDimming(wdSeekBar.getProgress());
            }
            if (chCheckBox.isChecked()) {
                builder.setCellHeight(Utils.dp(activity, chSeekBar.getProgress() + 48));
            }

            builder.show();
        }
    }
}