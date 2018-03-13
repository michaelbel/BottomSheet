package org.michaelbel.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.BottomSheetCallback;
import org.michaelbel.bottomsheet.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("all")
public class LaunchActivity extends AppCompatActivity {

    private boolean theme;

    private int[] items1 = new int[] {
        R.string.share,
        R.string.upload,
        R.string.copy,
        R.string.print_this_page
    };

    private int[] items2 = new int[] {
        R.string.preview,
        R.string.share,
        R.string.get_link,
        R.string.make_copy
    };

    private int[] icons = new int[] {
        R.drawable.ic_share,
        R.drawable.ic_upload,
        R.drawable.ic_copy,
        R.drawable.ic_print
    };

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.main_view) public LinearLayout linearLayout;
    @BindView(R.id.dimmingSeekBar) public SeekBar dimmingSeekBar;
    @BindView(R.id.heightSeekBar) public SeekBar heightSeekBar;
    @BindView(R.id.dimmingText) public TextView dimmingText;
    @BindView(R.id.cellHeightText) public TextView cellHeightText;
    @BindView(R.id.callbackCheckBox) public CheckBox callbackCheckBox;
    @BindView(R.id.dividersCheckBox) public CheckBox dividersCheckBox;
    @BindView(R.id.fullWidthCheckBox) public CheckBox fullWidthCheckBox;
    @BindView(R.id.multilineCheckBox) public CheckBox multilineCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        /*toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this, SecondActivity.class));
            }
        });*/

        SharedPreferences prefs = getSharedPreferences("mainconfig", MODE_PRIVATE);
        theme = prefs.getBoolean("theme", true);

        dimmingSeekBar.setMin(0);
        dimmingSeekBar.setMax(255);
        dimmingSeekBar.setProgress(80);
        dimmingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dimmingText.setText(getString(R.string.WindowDimmingValue, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        dimmingText.setText(getString(R.string.WindowDimmingValue, dimmingSeekBar.getProgress()));

        heightSeekBar.setMin(0);
        heightSeekBar.setMax(16);
        heightSeekBar.setProgress(0);
        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cellHeightText.setText(getString(R.string.CellHeightValue, progress + 48));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        cellHeightText.setText(getString(R.string.CellHeightValue, heightSeekBar.getProgress() + 48));
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        SharedPreferences prefs = getSharedPreferences("mainconfig", MODE_PRIVATE);
        boolean appTheme = prefs.getBoolean("theme", true);
        super.setTheme(appTheme ? R.style.AppThemeLight : R.style.AppThemeDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.About)
            .setIcon(R.drawable.ic_about)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            .setOnMenuItemClickListener(item -> {
                View view = LayoutInflater.from(LaunchActivity.this).inflate(R.layout.about_view, null);
                TextView textView = view.findViewById(R.id.text_view);
                textView.setText(getString(R.string.VersionBuildDate, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.VERSION_DATE));

                AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                builder.setTitle(R.string.About);
                builder.setView(view);
                builder.setPositiveButton(R.string.Ok, null);
                builder.show();
                return true;
            });

        menu.add(R.string.ViewOnGithub)
            .setIcon(R.drawable.ic_github)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            .setOnMenuItemClickListener(menuItem -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.GithubUrl))));
                return true;
            });

        menu.add(R.string.ChangeTheme)
            .setIcon(R.drawable.ic_theme)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            .setOnMenuItemClickListener(menuItem -> {
                SharedPreferences prefs = getSharedPreferences("mainconfig", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                boolean appTheme = prefs.getBoolean("theme", true);
                editor.putBoolean("theme", !appTheme);
                editor.apply();
                recreate();
                return true;
            });

        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.listStyleButton)
    public void listStyleButtonClick(View view) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setWindowDimming(dimmingSeekBar.getProgress());
        builder.setDividers(dividersCheckBox.isChecked());
        builder.setFullWidth(fullWidthCheckBox.isChecked());
        builder.setCellHeight(Utils.dp(this, heightSeekBar.getProgress()  + 48));
        builder.setCallback(!callbackCheckBox.isChecked() ? null : new BottomSheetCallback() {
            @Override
            public void onShown() {
                showToast(R.string.Shown);
            }

            @Override
            public void onDismissed() {
                showToast(R.string.Dismissed);
            }
        });
        builder.setItems(items1, (dialogInterface, i) ->
            Toast.makeText(this, items1[i], Toast.LENGTH_SHORT).show()
        );
        builder.show();
    }

    @OnClick(R.id.listStyleTitleButton)
    public void listStyleTitleButtonClick(View view) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setWindowDimming(dimmingSeekBar.getProgress());
        builder.setDividers(dividersCheckBox.isChecked());
        builder.setFullWidth(fullWidthCheckBox.isChecked());
        builder.setCellHeight(Utils.dp(this, heightSeekBar.getProgress() + 48));
        builder.setCallback(!callbackCheckBox.isChecked() ? null : new BottomSheetCallback() {
            @Override
            public void onShown() {
                showToast(R.string.Shown);
            }

            @Override
            public void onDismissed() {
                showToast(R.string.Dismissed);
            }
        });
        builder.setTitle(R.string.MultilineText);
        builder.setTitleMultiline(multilineCheckBox.isChecked());
        builder.setItems(items1, (dialogInterface, i) ->
                Toast.makeText(this, items1[i], Toast.LENGTH_SHORT).show()
        );
        builder.show();
    }

    @OnClick(R.id.listStyleIconsButton)
    public void listStyleIconsButtonClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setWindowDimming(dimmingSeekBar.getProgress());
        builder.setDividers(dividersCheckBox.isChecked());
        builder.setFullWidth(fullWidthCheckBox.isChecked());
        builder.setCellHeight(Utils.dp(this, heightSeekBar.getProgress() + 48));
        builder.setCallback(!callbackCheckBox.isChecked() ? null : new BottomSheetCallback() {
            @Override
            public void onShown() {
                showToast(R.string.Shown);
            }

            @Override
            public void onDismissed() {
                showToast(R.string.Dismissed);
            }
        });
        builder.setItems(items1, icons, (dialogInterface, i) ->
            Toast.makeText(this, items1[i], Toast.LENGTH_SHORT).show()
        );
        builder.show();
    }

    @OnClick(R.id.inflateMenu)
    public void inflateMenuButtonClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setWindowDimming(dimmingSeekBar.getProgress());
        builder.setDividers(dividersCheckBox.isChecked());
        builder.setFullWidth(fullWidthCheckBox.isChecked());
        builder.setCellHeight(Utils.dp(this, heightSeekBar.getProgress() + 48));
        builder.setCallback(!callbackCheckBox.isChecked() ? null : new BottomSheetCallback() {
            @Override
            public void onShown() {
                showToast(R.string.Shown);
            }

            @Override
            public void onDismissed() {
                showToast(R.string.Dismissed);
            }
        });
        builder.setTitle(R.string.Actions);
        builder.setTitleMultiline(multilineCheckBox.isChecked());
        builder.setMenu(R.menu.menu_items_icons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LaunchActivity.this, "Item: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @OnClick(R.id.gridStyleButton)
    public void gridStyleButtonClick(View v) {
        int[] items = new int[] {
            R.string.gmail,
            R.string.hangout,
            R.string.google_plus,
            R.string.mail,
            R.string.message,
            R.string.copy,
            R.string.facebook,
            R.string.twitter
        };

        int[] icons = new int[] {
            R.drawable.ic_gmail,
            R.drawable.ic_hangouts,
            R.drawable.ic_google_plus,
            R.drawable.ic_mail,
            R.drawable.ic_message,
            R.drawable.ic_copy_48,
            R.drawable.ic_facebook,
            R.drawable.ic_twitter
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setContentType(BottomSheet.GRID);
        builder.setWindowDimming(dimmingSeekBar.getProgress());
        builder.setFullWidth(fullWidthCheckBox.isChecked());
        builder.setCallback(!callbackCheckBox.isChecked() ? null : new BottomSheetCallback() {
            @Override
            public void onShown() {
                showToast(R.string.Shown);
            }

            @Override
            public void onDismissed() {
                showToast(R.string.Dismissed);
            }
        });
        builder.setItems(items, icons, (dialogInterface, i) ->
            Toast.makeText(this, items[i], Toast.LENGTH_SHORT).show()
        );
        builder.show();
    }

    private void showToast(int textId) {
        Toast.makeText(LaunchActivity.this, textId, Toast.LENGTH_SHORT).show();
    }

//--------------------------------------------------------------------------------------------------

    /*@OnClick(R.id.custom_style)
    public void customStyleButtonClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle(R.string.actions);
        builder.setTitleTextColor(0xFFFFEB3B);
        builder.setItemTextColor(0xFFB2FF59);
        builder.setBackgroundColor(0xFF3F51B5);
        builder.setIconColor(0xFFEEFF41);
        builder.setItemSelector(R.drawable.selectable_custom);
        builder.setItems(items1, icons, (dialogInterface, i) ->
                Toast.makeText(this, items1[i], Toast.LENGTH_SHORT).show()
        );
        builder.show();
    }*/

    /*@OnClick(R.id.custom_view)
    public void customViewButtonClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setView(R.layout.custom_view);
        builder.show();
    }*/
}