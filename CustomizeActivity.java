package com.example.hunter.alliancepicker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomizeActivity extends AppCompatActivity {
    int rowNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        for(int cntr=0; cntr< ColorScheme.allColorSchemes.toArray().length; cntr++){
            Customize_CreateRow(ColorScheme.allColorSchemes.get(cntr).desc);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {return true;}
        return super.onOptionsItemSelected(item);
    }

    public void Customize_CreateRow(String dispText) {
        TableLayout tl = (TableLayout)findViewById(R.id.customizeTable);

        ScrollView sv = (ScrollView)findViewById(R.id.customize); // Allows for scrolling
        sv.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.EdgeBgColor));

        TableRow tr1 = new TableRow(this); // Builds Team Row
        tr1.setId(rowNum);
        tr1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textview = (TextView) getLayoutInflater().inflate(R.layout.customize_row, null); // Pick Layout

        // Get image button //
        String uri = "@drawable/cscheme_" + (rowNum+1);
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        ImageView screenshot = (ImageView)getLayoutInflater().inflate(R.layout.customize_image,null);
        Drawable res = getResources().getDrawable(imageResource);
        screenshot.setImageDrawable(res);

        tr1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorScheme.selectedColor = ColorScheme.allColorSchemes.get(v.getId());
                Intent myIntent = new Intent(CustomizeActivity.this, MainActivity.class);
                CustomizeActivity.this.startActivity(myIntent);
                CustomizeActivity.this.finish();
            }
        });

        // Set Description Text //
        textview.setId(5300 + rowNum);
        textview.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        textview.setText(dispText);

        // Alternate Colors on rows //
        if (rowNum % 2 == 0) {tr1.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {tr1.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}

        // Add everything to row //
        tr1.addView(screenshot);
        tr1.addView(textview);

        // Put it all together //
        tl.addView(tr1, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowNum++;
    }
}
