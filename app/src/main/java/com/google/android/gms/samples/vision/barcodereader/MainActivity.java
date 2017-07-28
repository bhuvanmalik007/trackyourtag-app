/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    GPSTracker gps;
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    private TextView lat;
    private TextView lngt;
    private Button g,s;
    private String code;
    JSONObject json = new JSONObject();



    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        code="";
        lat = (TextView)findViewById(R.id.textView);
        lngt = (TextView)findViewById(R.id.textView2);

        g= (Button)findViewById(R.id.button);
        s= (Button)findViewById(R.id.button2);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);

        lat.setText("");
        lngt.setText("");



        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);
                if (gps.canGetLocation()) {
                    Double latitude = gps.getLatitude();
                    Double longitude = gps.getLongitude();
                    lat.setText(latitude.toString());
                    lngt.setText(longitude.toString());
                } else {
                    Toast.makeText(getBaseContext(), "Location Services not enabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", 0);

                String holderemail = pref.getString("email","");



                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                //String url ="http://trackyourtag.herokuapp.com/droid/testfromandroid/" + code + "/" + lat + "/" + lngt;
                //String url= "http://www.google.com";
                String url ="http://trackyourtagserver.herokuappapp.com/droid/" + code;


                JSONObject obj = new JSONObject();
                try {
                    obj.put("holderemail", holderemail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    obj.put("qrId", code);
                    obj.put("lat", lat.getText());
                    obj.put("long", lngt.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // RequestQueue queue = MyVolley.getRequestQueue();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT,url,obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response);
                                Toast.makeText(getApplicationContext(), "Response is: "+ response , Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "check your network/wifi connection and try again!" , Toast.LENGTH_LONG).show();
                            }
                        });
                queue.add(jsObjRequest);

                /*


// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.

                                Toast.makeText(getApplicationContext(), "Response is: "+ response , Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "check your network/wifi connection and try again!" , Toast.LENGTH_LONG).show();
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);     */



            }
        });
    }




    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText("Value discovered : " + barcode.displayValue);

                    code=barcode.displayValue;

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:

               /* Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Intent i = new Intent("android.intent.action.a");
                startActivity(i);      */
                break;

            case R.id.item2:

                Toast.makeText(this, "Logging Out..", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("authentication",0);
                editor.commit();
                Intent e = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(e);
                break;


        }
        return super.onMenuItemSelected(featureId, item);
    }

}


/*

JSONObject json = new JSONObject();
json.put("QR", barcode.displayValue);
json.put("Latitude", lat);
json.put("Longitude", lngt);
 */


