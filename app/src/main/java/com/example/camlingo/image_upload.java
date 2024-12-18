package com.example.camlingo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;


public class image_upload extends AppCompatActivity {
    private ImageButton cameraButton;
    private TextView textView4;
    private Bitmap imageToIdentify;
    private String engText;
    private String freText;
    String fileName = "testImg1.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_upload);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Check For/Grant Camera Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        // Initialize Buttons
        Button identifyButton = findViewById(R.id.button_identify);
        Button btnTranslateEnglish = findViewById(R.id.btnTranslateEnglish);
        Button btnTranslateFrench = findViewById(R.id.btnTranslateFrench);
        textView4 = findViewById(R.id.textView4);

        cameraButton = findViewById(R.id.imageButton2);
        //imageToIdentify = AssetUtilities.assetsToBitmap(this,fileName);
        if(imageToIdentify != null){
            //cameraButton.setImageBitmap(imageToIdentify);
        }
        // onClickListener Setup
        identifyButton.setOnClickListener(view -> identifyImage());
        btnTranslateEnglish.setOnClickListener(view -> translateToEnglish());
        btnTranslateFrench.setOnClickListener(view -> translateToFrench());
        cameraButton.setOnClickListener(view -> openGallery());


    }
    //IdentifyImage function for identifyButton
    private void identifyImage() {
        if (imageToIdentify == null){
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();

        }

        ImageLabeler lbler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        InputImage mlInputImage = InputImage.fromBitmap(imageToIdentify,0);
        StringBuilder txtOutput = new StringBuilder();

        lbler.process(mlInputImage)
                .addOnSuccessListener(labels ->{
                    if(labels.isEmpty()){
                        textView4.setText("No Objects Selected/Identified");
                    } else {
                        ImageLabel highConfidence = labels.get(0);
                        //Match highest confidence guess
                        for (ImageLabel label : labels){
                            Log.d("IdentityM:", label.getText() + "-" +label.getConfidence());
                            if(label.getConfidence() > highConfidence.getConfidence()){
                                highConfidence = label;
                            }
                        }
                        String tmp = highConfidence.getText();
                        float confidence = highConfidence.getConfidence();
                        engText = tmp;
                        txtOutput.append("English \n\n").append(tmp);//.append(" : "); //.append(confidence).append("\n");
                    }
                    textView4.setText(txtOutput.toString());

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,"Identification Failed",Toast.LENGTH_SHORT).show();
                });

    }
    //Translate to English
    private void translateToEnglish() {
        if (freText == null || freText.isEmpty()) {
            Toast.makeText(this, "No text to translate", Toast.LENGTH_SHORT).show();
            return;
        }
        textView4.setText("English \n\n" +engText);

    }

    //Translate to French
    private void translateToFrench() {
        // Ensure engText is not null or empty
        if (engText == null || engText.isEmpty()) {
            Toast.makeText(this, "No text to translate", Toast.LENGTH_SHORT).show();
            return;
        }


        //Download French Dictionary
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();
        Translator englishToFrench = Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        // Download the translation model if needed
        englishToFrench.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    Log.i(MainActivity.class.getSimpleName(), "Model downloaded");
                    // Translate the text
                    englishToFrench.translate(engText)
                            .addOnSuccessListener(tmpfreText -> {
                                Log.i(MainActivity.class.getSimpleName(), "Translation successful");
                                StringBuilder txtOutput = new StringBuilder();
                                txtOutput.append("French \n\n").append(tmpfreText);
                                textView4.setText(txtOutput.toString());
                                freText = tmpfreText;
                            })
                            .addOnFailureListener(e -> {
                                Log.e(MainActivity.class.getSimpleName(), "Translation failed", e);
                                Toast.makeText(this, "Translation failed", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(MainActivity.class.getSimpleName(), "Model download failed", e);
                    Toast.makeText(this, "Model download failed", Toast.LENGTH_SHORT).show();
                });
    }



    //openGallery function for cameraButton
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Uri userImgUri = intent.getData();
        if(userImgUri != null){
            try {
                imageToIdentify = MediaStore.Images.Media.getBitmap(this.getContentResolver(), userImgUri);
                
                //Show image on button
                cameraButton.setImageBitmap(imageToIdentify);
            } catch (IOException e){
                Log.d(image_upload.class.getSimpleName(), "Image Selection Failed");
            }
        }

    }

}