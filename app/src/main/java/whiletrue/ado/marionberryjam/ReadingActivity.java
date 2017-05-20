package whiletrue.ado.marionberryjam;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.widget.TextView;


public class ReadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Intent intent = getIntent();
        String body = intent.getStringExtra("whiletrue.ado.marionberryjam.BODY");

        TextView tv = (TextView) findViewById(R.id.main_body);
        tv.setText(body);
        Linkify.addLinks(tv, Linkify.WEB_URLS);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        String test = intent.getStringExtra("whiletrue.ado.marionberryjam.BODY_TITLE");
        ab.setTitle(intent.getStringExtra("whiletrue.ado.marionberryjam.BODY_TITLE").toLowerCase());

        ab.setDisplayHomeAsUpEnabled(true);
    }
}
