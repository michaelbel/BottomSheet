package org.michaelbel.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.michaelbel.bottomsheet.test.BottomSheet;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("unused")
public class LaunchActivity extends AppCompatActivity {

    private boolean appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("mainconfig", MODE_PRIVATE);
        appTheme = preferences.getBoolean("appTheme", true);
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        SharedPreferences preferences = getSharedPreferences("mainconfig", MODE_PRIVATE);
        boolean appTheme = preferences.getBoolean("appTheme", true);
        super.setTheme(appTheme ? R.style.AppThemeLight : R.style.AppThemeDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_github) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.to_github))));
        } else if (id == R.id.action_theme) {
            SharedPreferences preferences = getSharedPreferences("mainconfig", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            boolean appTheme = preferences.getBoolean("appTheme", true);

            editor.putBoolean("appTheme", !appTheme);
            editor.apply();

            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.list_style)
    public void listStyleButtonClick(View v) {
        final CharSequence[] items = new CharSequence[]{
                getString(R.string.share),
                getString(R.string.upload),
                getString(R.string.copy),
                getString(R.string.print_this_page)
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!appTheme);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LaunchActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }

    @OnClick(R.id.list_style_title)
    public void listStyleTitleButtonClick(View v) {
        final CharSequence[] items = new CharSequence[]{
                getString(R.string.preview),
                getString(R.string.share),
                getString(R.string.get_link),
                getString(R.string.make_copy)
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle(R.string.actions);
        builder.setDarkTheme(!appTheme);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LaunchActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }

    @OnClick(R.id.list_style_icons)
    public void listStyleIconsButtonClick(View v) {
        final CharSequence[] items = new CharSequence[]{
                getString(R.string.share),
                getString(R.string.upload),
                getString(R.string.copy),
                getString(R.string.print_this_page)
        };

        int[] icons = new int[] {
                R.drawable.ic_share,
                R.drawable.ic_upload,
                R.drawable.ic_copy,
                R.drawable.ic_printer
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!appTheme);
        builder.setItems(items, icons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LaunchActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }

    @OnClick(R.id.grid_style)
    public void gridStyleButtonClick(View v) {
        final int[] items = new int[]{
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
        builder.setDarkTheme(!appTheme);
        builder.setContentType(BottomSheet.GRID);
        builder.setItems(items, icons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LaunchActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }

    @OnClick(R.id.custom_style)
    public void customStyleButtonClick(View v) {
        final CharSequence[] items = new CharSequence[]{
                getString(R.string.share),
                getString(R.string.upload),
                getString(R.string.copy),
                getString(R.string.print_this_page)
        };

        int[] icons = new int[] {
                R.drawable.ic_share,
                R.drawable.ic_upload,
                R.drawable.ic_copy,
                R.drawable.ic_printer
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle(R.string.actions);
        builder.setTitleTextColor(0xFFFFEB3B);
        builder.setItemTextColor(0xFFB2FF59);
        builder.setBackgroundColor(0xFF3F51B5);
        builder.setIconColor(0xFFEEFF41);
        builder.setItemSelector(R.drawable.selectable_custom);
        builder.setItems(items, icons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LaunchActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }

    @OnClick(R.id.custom_view)
    public void customViewButtonClick(View v) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(!appTheme);
        builder.setCustomView(R.layout.custom_view);
        builder.show();
    }
}