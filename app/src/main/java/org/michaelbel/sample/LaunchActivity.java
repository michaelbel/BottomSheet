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

import org.michaelbel.bottomsheet.BottomSheet;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("all")
public class LaunchActivity extends AppCompatActivity {

    private boolean appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("main_config", MODE_PRIVATE);
        appTheme = preferences.getBoolean("app_theme", true);
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        SharedPreferences preferences = getSharedPreferences("main_config", MODE_PRIVATE);
        boolean appTheme = preferences.getBoolean("app_theme", true);
        super.setTheme(appTheme ? R.style.AppThemeLight : R.style.AppThemeDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem githubItem = menu.add(R.string.fork_on_github).setIcon(R.drawable.ic_github);
        githubItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        githubItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.to_github))));
                return true;
            }
        });

        MenuItem themeItem = menu.add(R.string.change_theme).setIcon(R.drawable.ic_theme);
        themeItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        themeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences preferences = getSharedPreferences("main_config", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                boolean appTheme = preferences.getBoolean("app_theme", true);
                editor.putBoolean("app_theme", !appTheme);
                editor.apply();
                recreate();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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

    @OnClick(R.id.callback_style)
    public void callbackClick(View v) {
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
        builder.setCallback(new BottomSheet.Callback() {
            @Override
            public void onOpen() {
                Toast.makeText(LaunchActivity.this, "BottomSheet is Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose() {
                Toast.makeText(LaunchActivity.this, "BottomSheet is Closed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}