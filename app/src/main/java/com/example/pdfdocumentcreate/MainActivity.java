package com.example.pdfdocumentcreate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE = 100;
    Button pdfMake,xmlToPdf;
    EditText inputText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfMake = findViewById(R.id.pdfMake);
        xmlToPdf = findViewById(R.id.xmlToPdf);
        inputText = findViewById(R.id.inputText);

        pdfMake.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String input = inputText.getText().toString();

                        createPdf(input);
                    }
                }
        );

        xmlToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmlToPdfConvert();
            }
        });
        
    askPermission();    
    }

    private void xmlToPdfConvert() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            this.getDisplay().getRealMetrics(displayMetrics);
        }
        else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels,View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels,View.MeasureSpec.EXACTLY));
        view.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);

        PdfDocument document = new PdfDocument();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth,viewHeight,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        document.finishPage(page);




        // ================
        File downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadFile,fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            document.writeTo(fileOutputStream);
            document.close();
            fileOutputStream.close();
            Toast.makeText(this, "pdf convert successful", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("error", "createPdf: error" + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createPdf(String input) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,1920,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(42);


        float x = 300;
        float y = 500;

        canvas.drawText(input,x,y,paint);
        document.finishPage(page);


        File downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadFile,fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            document.writeTo(fileOutputStream);
            document.close();
            fileOutputStream.close();
            Toast.makeText(this, "pdf create successful", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("error", "createPdf: error" + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

}