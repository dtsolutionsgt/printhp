package com.hp.retail.test;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;
import jpos.config.simple.SimpleEntryRegistry;
import jpos.config.simple.xml.SimpleXmlRegPopulator;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import static jpos.JposConst.JPOS_PN_ENABLED;

public class MenuActivity extends AppCompatActivity {

    private TextView lbl1;

    private POSPrinter printer = new POSPrinter();

    private String prdata;
    private String fname=Environment.getExternalStorageDirectory()+"/print.txt";

    final private static String TAG ="MenuActivity";
    String LF = ((char) 0x0a) + "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lbl1 = (TextView) findViewById(R.id.textView);lbl1.setText("Imprimiendo . . .");


        Handler mtimer = new Handler();
        Runnable mrunner=new Runnable() {
            @Override
            public void run() {
                onPrinter(null);
            }
        };
        mtimer.postDelayed(mrunner,200);

        Handler mtimer2 = new Handler();
        Runnable mrunner2=new Runnable() {
            @Override
            public void run() {
                moveTaskToBack (true);
             }
        };
        mtimer2.postDelayed(mrunner2,1000);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };




    private boolean createPrintData() {
        File ffile;
        BufferedReader dfile;
        StringBuilder textData = new StringBuilder();
        String ss;

        try {
            File file1 = new File(fname);
            ffile = new File(file1.getPath());
            FileInputStream fIn = new FileInputStream(ffile);
            dfile = new BufferedReader(new InputStreamReader(fIn));
        } catch (Exception e) {
            lbl1.setText("No se puede leer archivo de impresión : "+e.getMessage());
            return false;
        }

        try {

            prdata="";

            while ((ss = dfile.readLine()) != null) {
                prdata=prdata+ss+"\n";
            }

        } catch (Exception e) {
            lbl1.setText(e.getMessage()); prdata="";return false;
        }

        return true;
    }

    public void onPrinter(View view)  {

        try {

            printer.open("HPEngageOnePrimePrinter");

            printer.addStatusUpdateListener(new StatusUpdateListener() {
                @Override
                public void statusUpdateOccurred(StatusUpdateEvent statusUpdateEvent) {
                    Log.d(TAG, "###statusUpdateEvent: " + statusUpdateEvent.getStatus());
                    Message msg = new Message();
                    msg.what = statusUpdateEvent.getStatus();
                    handler.sendMessage(msg);
                }
            });

            printer.claim(3000);
            printer.setPowerNotify(JPOS_PN_ENABLED);

            try {
                Thread.sleep(500L);
            } catch (InterruptedException ie) {}

            printer.setDeviceEnabled(true);

            //String data ="Impresión By Erik\n\n\n\n\n ";
            //printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, data + "PrintNormal test\n\n\n\n\n" );

            prdata="";
            createPrintData();

            printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, prdata + "\n\n\n\n\n\n\n\n" );

            printer.close();

            finish();
            //printTest.test_receipt_line_parameters();

        } catch (Exception e) {
            Log.d(TAG, "" + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
