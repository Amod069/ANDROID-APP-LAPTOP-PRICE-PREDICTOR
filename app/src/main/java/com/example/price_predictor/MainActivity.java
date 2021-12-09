package com.example.price_predictor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner company,type,ram,hdd,ssd,os,gpubrand,touchscreen,ips,cpubrand,resolution;
    String L_com,L_type,L_touch,L_ips,L_graphic,L_processor,L_screen_res,L_os,L_ram,L_ssd,L_hdd;
   String url="https://api-laptop-prices.herokuapp.com/predict",s;
    EditText weight,screensize;
    Integer L_touch_yes;
    Integer L_ips_yes;
    Float screen_size_float,weight_float;
    //float screen_size_int;
    // Integer res_2,res_1;
     Button submit;
     TextView predict_price;
    ProgressDialog progress1;

   //  double ppi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        company=findViewById(R.id.company_name);
        type=findViewById(R.id.type);
        ram=findViewById(R.id.ram);
        hdd=findViewById(R.id.hdd);
        ssd=findViewById(R.id.ssd);
        os=findViewById(R.id.os);
        gpubrand=findViewById(R.id.graphic_card);
        touchscreen=findViewById(R.id.touch);
        ips=findViewById(R.id.ips);
        cpubrand=findViewById(R.id.processor_brand);
        resolution=findViewById(R.id.screen_resolution);
        weight=findViewById(R.id.weight);
        screensize=findViewById(R.id.screensize);
        submit=findViewById(R.id.submit);
        predict_price=findViewById(R.id.predicted_value);
        progress1 = new ProgressDialog(this);
        progress1.setTitle("YOUR PRICE IS GETTING PREDICTED");
        progress1.setMessage("NOTE PREDICTED PRICE MAY VARY FROM \n 2000-3000RS UP-DOWN SOMETIME");
        progress1.setCancelable(false); // disable dismiss by tapping outside of the dialog



       submit.setOnClickListener(this);


        //ArrayAdapter for Company Type
        ArrayAdapter<CharSequence> company_type_adapter= ArrayAdapter.createFromResource(this,R.array.company_name,android.R.layout.simple_spinner_item);
        company_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        company.setAdapter(company_type_adapter);
        company.setOnItemSelectedListener(this);
        //ArrayAdapter for Type
        ArrayAdapter<CharSequence> type_adapter= ArrayAdapter.createFromResource(this,R.array.type,android.R.layout.simple_spinner_item);
       type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);
        type.setOnItemSelectedListener(this);
        //ArrayAdapter for RAM
        ArrayAdapter<CharSequence> ram_adapter= ArrayAdapter.createFromResource(this,R.array.ram,android.R.layout.simple_spinner_item);
        ram_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ram.setAdapter(ram_adapter);
        ram.setOnItemSelectedListener(this);
        //ArrayAdapter for touch
        ArrayAdapter<CharSequence> touch_adapter= ArrayAdapter.createFromResource(this,R.array.touch,android.R.layout.simple_spinner_item);
        touch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        touchscreen.setAdapter(touch_adapter);
        touchscreen.setOnItemSelectedListener(this);
        //ArrayAdapter for IPS
        ArrayAdapter<CharSequence> ips_adapter= ArrayAdapter.createFromResource(this,R.array.ips,android.R.layout.simple_spinner_item);
        ips_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ips.setAdapter(ips_adapter);
        ips.setOnItemSelectedListener(this);
        //ArrayAdapter for Screen Resolution
        ArrayAdapter<CharSequence> screen_res_adapter= ArrayAdapter.createFromResource(this,R.array.screen_res,android.R.layout.simple_spinner_item);
        screen_res_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resolution.setAdapter(screen_res_adapter);
        resolution.setOnItemSelectedListener(this);
        //ArrayAdapter for Processor
        ArrayAdapter<CharSequence> processor_adapter= ArrayAdapter.createFromResource(this,R.array.processor_brand,android.R.layout.simple_spinner_item);
        processor_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cpubrand.setAdapter(processor_adapter);
        cpubrand.setOnItemSelectedListener(this);
        //ArrayAdapter for HDD
        ArrayAdapter<CharSequence> hdd_adapter= ArrayAdapter.createFromResource(this,R.array.hdd,android.R.layout.simple_spinner_item);
        hdd_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hdd.setAdapter(hdd_adapter);
        hdd.setOnItemSelectedListener(this);
        //ArrayAdapter for SSD
        ArrayAdapter<CharSequence> ssd_adapter= ArrayAdapter.createFromResource(this,R.array.ssd,android.R.layout.simple_spinner_item);
        ssd_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssd.setAdapter(ssd_adapter);
        ssd.setOnItemSelectedListener(this);
        //ArrayAdapter for Graphics
        ArrayAdapter<CharSequence> graphics_adapter= ArrayAdapter.createFromResource(this,R.array.graphic_card,android.R.layout.simple_spinner_item);
        graphics_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpubrand.setAdapter(graphics_adapter);
        gpubrand.setOnItemSelectedListener(this);
        //ArrayAdapter for OS
        ArrayAdapter<CharSequence> os_adapter= ArrayAdapter.createFromResource(this,R.array.os,android.R.layout.simple_spinner_item);
        os_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        os.setAdapter(os_adapter);
        os.setOnItemSelectedListener(this);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.company_name:
                if(position!=0){

                    L_com=parent.getItemAtPosition(position).toString();
           // Toast.makeText(parent.getContext(), "Working" + L_com, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper Company Name",Toast.LENGTH_LONG).show();
                    }
            break;}

            case R.id.type:
                if(position!=0){
                L_type=parent.getItemAtPosition(position).toString();
               // Toast.makeText(parent.getContext(), "Working" + L_type, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper TYPE",Toast.LENGTH_LONG).show();
                    }
                break;}

            case R.id.ram:
                if(position!=0){
                L_ram=parent.getItemAtPosition(position).toString();
               // Toast.makeText(parent.getContext(), "Working" + L_ram, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper RAM SIZE",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.touch:
                if(position!=0){
                L_touch=parent.getItemAtPosition(position).toString();
                if (L_touch.equals("YES")){
                    L_touch_yes =1;
                }
                else {L_touch_yes=0;}
               //Toast.makeText(parent.getContext(), "Working " + L_touch, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper Touchscreen or Not",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.ips:
                if(position!=0){
                L_ips=parent.getItemAtPosition(position).toString();
                if(L_ips.equals("YES")){
                    L_ips_yes=1;
                }
                else
                    {L_ips_yes=0;}
              //  Toast.makeText(parent.getContext(),L_ips_yes, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper IPS DISPLAY or Not",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.screen_resolution:
                if(position!=0){
                L_screen_res=parent.getItemAtPosition(position).toString();
               /* res = ((String) L_screen_res).split("x");
                 res_1= Integer.parseInt(res[0]);
                 res_2=Integer.parseInt(res[1]);
                screen_size_int=Float.parseFloat(s);
                 ppi=Math.pow(((Math.pow(res_1,2))+(Math.pow(res_2,2))),0.5)/screen_size_int;
                    ppi_s = String.valueOf(ppi);*/
                    s =screensize.getText().toString();
                    screen_size_float=Float.parseFloat(s);
                    String w=weight.getText().toString();
                    weight_float=Float.parseFloat(w);

                   // Toast t1= Toast.makeText(parent.getContext(),L_screen_res, Toast.LENGTH_LONG);
                  //t1.show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper Screen_RESOLUTON",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.processor_brand:
                if(position!=0){
                L_processor=parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Working" + L_processor, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper Processor Type",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.hdd:
                if(position!=0){
                    L_hdd=parent.getItemAtPosition(position).toString();
                   // Toast.makeText(parent.getContext(), "Working" + L_hdd, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper HDD SIZE",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.ssd:
                if(position!=0){
                    L_ssd=parent.getItemAtPosition(position).toString();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper SSD SIZE",Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(parent.getContext(), "Working" + L_ssd, Toast.LENGTH_LONG).show();
                    break;}

            case R.id.graphic_card:
                if(position!=0){
                    L_graphic=parent.getItemAtPosition(position).toString();
                   // Toast.makeText(parent.getContext(), "Working" + L_graphic, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper Graphic CARD",Toast.LENGTH_LONG).show();
                    }
                    break;}

            case R.id.os:
                if(position!=0){
                    L_os=parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(), "Working" + L_os, Toast.LENGTH_LONG).show();
                    if(position==0){
                        Toast.makeText(getApplicationContext(),"Please Select Proper OPERATING SYSTEM",Toast.LENGTH_LONG).show();
                    }
                    break;}


            default:

        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {

        progress1.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject= new JSONObject(response);

                            String data = jsonObject.getString("PRICES");

                            predict_price.setText(data);
                            progress1.dismiss();
                            Toast price_toast=Toast.makeText(getApplicationContext(),"YOUR PRICE IS PREDICTED",Toast.LENGTH_LONG);
                            price_toast.show();

                        } catch (JSONException e) {
                            progress1.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress1.dismiss();
                    Toast err= Toast.makeText(getApplicationContext(),"no :  "+error.toString(),Toast.LENGTH_LONG);
                    predict_price.setText(error.toString());
                    err.show();

            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
               try {
                   params.put("company",L_com);
                   params.put("type", L_type);
                   params.put("ram", L_ram);
                   params.put("weight", weight.getText().toString());
                   params.put("touchscreen", L_touch);
                   params.put("ips", L_ips);
                   params.put("screensize", screensize.getText().toString());
                   params.put("resolution", L_screen_res);
                   params.put("cpubrand", L_processor);
                   params.put("hdd", L_hdd);
                   params.put("ssd", L_ssd);
                   params.put("gpubrand", L_graphic);
                   params.put("os",L_os);
               } catch (Exception e) {
                   e.printStackTrace();
                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
               }
                return params;

            }
        };
        
        
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        queue.getCache().clear();
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }


}