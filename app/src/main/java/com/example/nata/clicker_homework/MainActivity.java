package com.example.nata.clicker_homework;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView money;
    String COUNT_MONEY = "MONEY";
    String UP_MONEY= "UP_MONEY";
    String COST_UP_MONEY= "COST_UP_MONEY";
    String UP_CLICK ="UP_CLICK";
    String COST_UP_CLICK ="COST_UP_CLICK";
    int count_money=0;
    int up_money=1;
    int cost_up_money=100;
    int up_click=1;
    int cost_up_click=100;
    ImageButton imageButton;
    Button upgrade;

    SharedPreferences sp;

    AlertDialog.Builder dialog_upgrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setParams();
        initView();
        dialog_upgrade = new AlertDialog.Builder(MainActivity.this);

        money.setText(Integer.toString(count_money));

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                count_money += up_money;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        money.setText(String.valueOf(count_money));
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count_money += up_click;
                money.setText(String.valueOf(count_money));
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
                dialog_upgrade.show();
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(COUNT_MONEY,count_money);
        editor.putInt(UP_MONEY,up_money);
        editor.putInt(COST_UP_MONEY,cost_up_money);
        editor.putInt(UP_CLICK, up_click);
        editor.putInt(COST_UP_CLICK, cost_up_click);
        editor.commit();
    }

    void createDialog(){
        dialog_upgrade.setTitle("Upgrade!");
        dialog_upgrade.setPositiveButton("Добавить деньги за нажатие: "+cost_up_click, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(count_money>=cost_up_click){
                    sp = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(UP_CLICK, up_click+1);
                    count_money-=cost_up_click;
                    editor.putInt(COST_UP_CLICK,cost_up_click*=5);
                    editor.commit();
                    setParams();
                    Toast.makeText(getApplicationContext(), "Улучшение куплено!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Нехватает денег!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog_upgrade.setNegativeButton("Добавить деньги за секунду: "+ cost_up_money, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(count_money>=cost_up_money){
                    sp = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(UP_MONEY, up_money+1);
                    count_money-=cost_up_money;
                    editor.putInt(COST_UP_MONEY,cost_up_money*=5);
                    editor.commit();
                    setParams();
                    Toast.makeText(getApplicationContext(), "Улучшение куплено!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Нехватает денег!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog_upgrade.setCancelable(true);
        dialog_upgrade.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    void setParams(){
        sp = getPreferences(MODE_PRIVATE);
        count_money = sp.getInt(COUNT_MONEY, 0);
        cost_up_money = sp.getInt(COST_UP_MONEY, 100);
        up_money = sp.getInt(UP_MONEY, 1);
        cost_up_click = sp.getInt(COST_UP_CLICK, 100);
        up_click = sp.getInt(UP_CLICK, 1);
    }

    void initView (){
        money = findViewById(R.id.money);
        imageButton = findViewById(R.id.ib);
        upgrade = findViewById(R.id.upgrade);
    }
}
