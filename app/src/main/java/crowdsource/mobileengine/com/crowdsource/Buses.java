package crowdsource.mobileengine.com.crowdsource;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Buses extends Activity {

    private ListView listOfBus;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_buses);
        listOfBus = (ListView)findViewById(R.id.list);


        String[] values = new String[] { "RC-10", "SB-210", "CR-71" };
        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.row_layout, R.id.firstLine, values);
        listOfBus.setAdapter(adapter);
        listOfBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + parent.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();*/

                SharedPreferences.Editor editbuspref = getSharedPreferences(Constants.USER_PREF_NAME,MODE_PRIVATE).edit();
                editbuspref.putString(Constants.USER_PREF_BUS_NO,String.valueOf(parent.getItemAtPosition(position)));
                editbuspref.commit();
                Intent busDetailIntent = new Intent(getApplicationContext(),BusDetail.class);
                startActivity(busDetailIntent);

            }
        });
    }



}
