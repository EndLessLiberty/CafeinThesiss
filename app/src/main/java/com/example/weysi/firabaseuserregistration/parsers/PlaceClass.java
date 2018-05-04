package com.example.weysi.firabaseuserregistration.parsers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ListView;

import com.example.weysi.firabaseuserregistration.R;
import com.example.weysi.firabaseuserregistration.adapters.OzelAdapter;
import com.example.weysi.firabaseuserregistration.informations.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 1.11.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class PlaceClass extends AsyncTask< String,String, List<Data>> {
    final List<Data> dataList=new ArrayList<Data>();
    List<Bitmap> bitmapList = new ArrayList<Bitmap>();
    ListView lv;
    double x,y;
    Context context;


    public PlaceClass(ListView t, double x1, double y2, Context c){
        lv=t;
        x=x1;
        y=y2;
        context=c;

    }

    @Override
    protected List<Data> doInBackground(String... url) {

        DataParser dataParser=new DataParser();
        List<Data> dataList=dataParser.parser(x,y);
        //List<Data> dataList1=new ArrayList<Data>();
        //List<Bitmap> bitmapList=new ArrayList<Bitmap>();

        InputStream input=null;
        for(int i=0;i<dataList.size();i++) {
            try {
                //Data data = new Data();
                //data.setName(dataList.get(i).getName());

                //data.setPlaceId(dataList.get(i).getPlaceId());
                // Resim indiriyoruz
                input = new java.net.URL(dataList.get(i).getUrl()).openStream();

                dataList.get(i).setBitmap(BitmapFactory.decodeStream(input));
                //data.setBitmap(BitmapFactory.decodeStream(input));
                //dataList1.add(data);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        try {

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return  dataList;
    }

    @Override
    protected void onPostExecute( List<Data> result) {

        OzelAdapter adaptorumuz=new OzelAdapter(context, R.layout.satir_layout ,result);
        lv.setAdapter(adaptorumuz);

}}