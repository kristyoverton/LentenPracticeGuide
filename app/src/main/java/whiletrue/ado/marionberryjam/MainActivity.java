package whiletrue.ado.marionberryjam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PracticeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String dayInPeriod = "1";
    String weekInPeriod = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showIntroOnFirstRun();
        dayInPeriod = calculateDayInPeriod(new DateTime(), getResources());
        weekInPeriod = calculateWeekInPeriod(dayInPeriod);

        //set header text
        setHeader();

        //load cards
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Set<String> dismissedIDs = getSharedPreferences("MarionberryJam", Context.MODE_PRIVATE).getStringSet("LENT_DISMISSED", new HashSet<String>());
        String jsonString = readJsonAsset(weekInPeriod);

        mAdapter = new PracticeAdapter(jsonString, dismissedIDs, this, dayInPeriod);

        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new PracticeTouchHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

    }

    private void showIntroOnFirstRun() {
        SharedPreferences prefs = getSharedPreferences("MarionberryJam", Context.MODE_PRIVATE);
        if (prefs.getBoolean("LENT_2017_FIRST_TIME", true)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }

    private String readJsonAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void setHeader() {
        TextView tv = (TextView) findViewById(R.id.date_string);
        DateTime now = new DateTime();
        DateTimeFormatter f = DateTimeFormat.forPattern("MMM dd");
        String shortDate = f.print(now);
        tv.setText(shortDate);
        try {
            String json = readJsonAsset("header");
            JSONObject obj = new JSONObject(json).getJSONObject(weekInPeriod);
            tv = (TextView) findViewById(R.id.header_title);
            tv.setText(obj.optString("title", ""));
        } catch (JSONException e) {
        }
    }

    private String calculateDayInPeriod(DateTime now, Resources resources) {

        try {
            JSONObject json = new JSONObject(readJsonAsset("meta"));
            DateTime Day1 = new DateTime(json.get("day1"));
            DateTime LastDay = new DateTime(json.get("lastDay"));

            if (now.isBefore(Day1) || now.isAfter(LastDay)) {
                return "0";
            }

            int day = Days.daysBetween(Day1, now).getDays() + 1;
            return String.valueOf(day);
        } catch (JSONException e) {
        }
        return "";
    }

    private static String calculateWeekInPeriod(String day) {
        int week = (Integer.valueOf(day) % 7 == 0) ? Integer.valueOf(day) / 7 : + Integer.valueOf(day) / 7 + 1;
        return String.valueOf(week);
    }


}
