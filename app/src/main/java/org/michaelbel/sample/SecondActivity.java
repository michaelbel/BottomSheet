package org.michaelbel.sample;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.michaelbel.bottomsheet.BottomSheet;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("all")
public class SecondActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

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

    @BindView(R.id.button)
    public Button button;

    @BindView(R.id.fab)
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("mainconfig", MODE_PRIVATE);
        theme = prefs.getBoolean("theme", true);

        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
        button.setText("Show dialog");

        fab.setOnClickListener(this);
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        SharedPreferences prefs = getSharedPreferences("mainconfig", MODE_PRIVATE);
        boolean appTheme = prefs.getBoolean("theme", true);
        super.setTheme(appTheme ? R.style.AppThemeLight : R.style.AppThemeDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!theme);
        builder.setDividers(true);
        builder.setTitle(R.string.Actions);
        builder.setFabBehavior(fab, BottomSheet.FAB_SLIDE_UP);
        builder.setMenu(R.menu.menu_items_icons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SecondActivity.this, "Item: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    public boolean onLongClick(View v) {
        ObjectAnimator fabTranslationAnimator = ObjectAnimator.ofFloat(fab, "translationY", 0);
        fabTranslationAnimator.setDuration(150);
        fabTranslationAnimator.start();
        return false;
    }
}