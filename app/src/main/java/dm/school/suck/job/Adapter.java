package dm.school.suck.job;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Adapter extends ArrayAdapter<String> {
    ArrayList<DB_df> mission=new ArrayList<DB_df>();
    private final Activity context;
    private TextView TV_Price;
    private TextView Tv_Qty;
    private TextView Tv_Time;
    DBOpenHelper dbOpenHelper;
    String[] courses = { "即將抵達", "取件完成",
            "送件完成" };

    public Adapter(Activity context) {
        super(context, R.layout.item_row);
        // TODO Auto-generated constructor stub
          dbOpenHelper =new DBOpenHelper(context);
        mission=dbOpenHelper.getAllSites_df();

        this.context=context;
    }
    @Override
    public int getCount() {
        return  40;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_row, null,true);

        TV_Price=rowView.findViewById(R.id.tv_price);
        Tv_Qty=rowView.findViewById(R.id.tv_qty);
        Tv_Time=rowView.findViewById(R.id.tv_time);


        if(mission.size()>39){
            TV_Price.setText(mission.get(position).getPrice());
            if(mission.get(position).getStatus().equals("r")){
                TV_Price.setTextColor(Color.RED);
            }else if(mission.get(position).getStatus().equals("g")){
                TV_Price.setTextColor(Color.GREEN);
            }else {
                TV_Price.setTextColor(Color.BLACK);
            }
            Tv_Qty.setText(mission.get(position).getQty());
            Tv_Time.setText(mission.get(position).getTime());
        }

        return rowView;

    };

}