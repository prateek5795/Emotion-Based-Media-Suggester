package com.example.prateek.visionapitest.UI;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.visionapitest.AsyncTask.GetTokenTask;
import com.example.prateek.visionapitest.R;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String accessToken;
    static final int REQUEST_IMAGE_CAPTURE = 9;
    static final int REQUEST_GALLERY_IMAGE = 10;
    static final int REQUEST_CODE_PICK_ACCOUNT = 11;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 12;
    static final int REQUEST_PERMISSIONS = 13;
    private final String LOG_TAG = "MainActivity";
    private ImageView ivImage;
    private TextView tvJoyResult, tvAngerResult, tvSorrowResult, tvSurpriseResult;
    private TextView bShowMedia;
    LinearLayout llResult;
    Account mAccount;
    ProgressBar progJoy, progAnger, progSorrow, progSurprise;
    String moods[] = new String[4];
    int joyLikelihood = 0, angerLikelihood = 0, sorrowLikelihood = 0, surpriseLikelihood = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        ivImage.setOnClickListener(this);
        bShowMedia.setOnClickListener(this);
    }

    private void initialize() {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        llResult = (LinearLayout) findViewById(R.id.llResult);
        tvJoyResult = (TextView) findViewById(R.id.tvJoyResult);
        tvAngerResult = (TextView) findViewById(R.id.tvAngerResult);
        tvSorrowResult = (TextView) findViewById(R.id.tvSorrowResult);
        tvSurpriseResult = (TextView) findViewById(R.id.tvSurpriseResult);
        bShowMedia = (TextView) findViewById(R.id.bShowMedia);
        progJoy = (ProgressBar) findViewById(R.id.progJoy);
        progAnger = (ProgressBar) findViewById(R.id.progAnger);
        progSorrow = (ProgressBar) findViewById(R.id.progSorrow);
        progSurprise = (ProgressBar) findViewById(R.id.progSurprise);
    }

    private void launchImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), REQUEST_GALLERY_IMAGE);
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAuthToken();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                AccountManager am = AccountManager.get(this);
                Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                for (Account account : accounts) {
                    if (account.name.equals(email)) {
                        mAccount = account;
                        break;
                    }
                }
                getAuthToken();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                onTokenReceived(extra.getString("authtoken"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void detectFaces() {
        if (ivImage.getDrawable() != null) {
            final Bitmap myBitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();

            final Paint rectPaint = new Paint();
            rectPaint.setStrokeWidth(5);
            rectPaint.setColor(Color.RED);
            rectPaint.setStyle(Paint.Style.STROKE);

            final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(myBitmap, 0, 0, null);

            FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .build();

            if (!faceDetector.isOperational()) {
                Toast.makeText(MainActivity.this, "Facedetector could not be set up", Toast.LENGTH_SHORT).show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Face> sparseArray = faceDetector.detect(frame);

            for (int i = 0; i < sparseArray.size(); ++i) {
                Face face = sparseArray.valueAt(i);
                float x1 = face.getPosition().x;
                float y1 = face.getPosition().y;
                float x2 = x1 + face.getWidth();
                float y2 = y1 + face.getHeight();

                RectF rectF = new RectF(x1, y1, x2, y2);
                canvas.drawRoundRect(rectF, 2, 2, rectPaint);
            }
            ivImage.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                Bitmap bitmap = resizeBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                callCloudVision(bitmap);
                ivImage.setImageBitmap(bitmap);
                detectFaces();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "Null image was returned.");
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {

        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, credential);
                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature faceDetection = new Feature();
                    faceDetection.setType("FACE_DETECTION");
                    faceDetection.setMaxResults(10);
                    featureList.add(faceDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(LOG_TAG, "sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.e(LOG_TAG, "Request failed: " + e.getContent());
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Request failed: " + e.getMessage());
                }
                return "Cloud Vision API request failed.";
            }

            @Override
            protected void onPostExecute(String result) {
                llResult.setVisibility(View.VISIBLE);
                tvJoyResult.setText(moods[0]);
                tvAngerResult.setText(moods[1]);
                tvSorrowResult.setText(moods[2]);
                tvSurpriseResult.setText(moods[3]);
                calcProgress();
            }
        }.execute();
    }

    @NonNull
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("Results");
        //message.append("Face Data: \n");
        List<FaceAnnotation> faceData = response.getResponses().get(0).getFaceAnnotations();
        if (faceData != null) {
            for (FaceAnnotation face : faceData) {
                //message.append("Joy : " + face.getJoyLikelihood() + "\n");
                moods[0] = "Joy : " + face.getJoyLikelihood();
                //message.append("Anger : " + face.getAngerLikelihood() + "\n");
                moods[1] = "Anger : " + face.getAngerLikelihood();
                //message.append("Sorrow : " + face.getSorrowLikelihood() + "\n");
                moods[2] = "Sorrow : " + face.getSorrowLikelihood();
                //message.append("Surprise : " + face.getSurpriseLikelihood() + "\n");
                moods[3] = "Surprise : " + face.getSurpriseLikelihood();
            }
        }
        return message.toString();
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetTokenTask(MainActivity.this, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION, mAccount).execute();
        }
    }

    public void onTokenReceived(String token) {
        accessToken = token;
        launchImagePicker();
    }

    public void calcProgress() {

        if (moods[0].equals("Joy : VERY_UNLIKELY"))
            joyLikelihood = 5;
        else if (moods[0].equals("Joy : UNLIKELY"))
            joyLikelihood = 25;
        else if (moods[0].equals("Joy : POSSIBLE"))
            joyLikelihood = 50;
        else if (moods[0].equals("Joy : LIKELY"))
            joyLikelihood = 75;
        else if (moods[0].equals("Joy : VERY_LIKELY"))
            joyLikelihood = 100;
        else joyLikelihood = 0;

        if (moods[1].equals("Anger : VERY_UNLIKELY"))
            angerLikelihood = 5;
        else if (moods[1].equals("Anger : UNLIKELY"))
            angerLikelihood = 25;
        else if (moods[1].equals("Anger : POSSIBLE"))
            angerLikelihood = 50;
        else if (moods[1].equals("Anger : LIKELY"))
            angerLikelihood = 75;
        else if (moods[1].equals("Anger : VERY_LIKELY"))
            angerLikelihood = 100;
        else angerLikelihood = 0;

        if (moods[2].equals("Sorrow : VERY_UNLIKELY"))
            sorrowLikelihood = 5;
        else if (moods[2].equals("Sorrow : UNLIKELY"))
            sorrowLikelihood = 25;
        else if (moods[2].equals("Sorrow : POSSIBLE"))
            sorrowLikelihood = 50;
        else if (moods[2].equals("Sorrow : LIKELY"))
            sorrowLikelihood = 75;
        else if (moods[2].equals("Sorrow : VERY_LIKELY"))
            sorrowLikelihood = 100;
        else sorrowLikelihood = 0;

        if (moods[3].equals("Surprise : VERY_UNLIKELY"))
            surpriseLikelihood = 5;
        else if (moods[3].equals("Surprise : UNLIKELY"))
            surpriseLikelihood = 25;
        else if (moods[3].equals("Surprise : POSSIBLE"))
            surpriseLikelihood = 50;
        else if (moods[3].equals("Surprise : LIKELY"))
            surpriseLikelihood = 75;
        else if (moods[3].equals("Surprise : VERY_LIKELY"))
            surpriseLikelihood = 100;
        else surpriseLikelihood = 0;

        setProgress(joyLikelihood, angerLikelihood, sorrowLikelihood, surpriseLikelihood);
    }

    public void setProgress(int joy, int anger, int sorrow, int surprise) {
        Resources res = getResources();
        Drawable drawable1 = res.getDrawable(R.drawable.progress_background_joy);
        Drawable drawable2 = res.getDrawable(R.drawable.progress_background_anger);
        Drawable drawable3 = res.getDrawable(R.drawable.progress_background_sorrow);
        Drawable drawable4 = res.getDrawable(R.drawable.progress_background_surprise);


        ObjectAnimator animation1 = ObjectAnimator.ofInt(progJoy, "progress", 0, joy);
        animation1.setDuration(990);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.start();
        progJoy.setProgress(joy);   // Main Progress
        progJoy.setMax(100); // Maximum Progress
        progJoy.setProgressDrawable(drawable1);

        ObjectAnimator animation2 = ObjectAnimator.ofInt(progAnger, "progress", 0, anger);
        animation2.setDuration(990);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.start();
        progAnger.setProgress(anger);   // Main Progress
        progAnger.setMax(100); // Maximum Progress
        progAnger.setProgressDrawable(drawable2);

        ObjectAnimator animation3 = ObjectAnimator.ofInt(progSorrow, "progress", 0, sorrow);
        animation3.setDuration(990);
        animation3.setInterpolator(new DecelerateInterpolator());
        animation3.start();
        progSorrow.setProgress(sorrow);   // Main Progress
        progSorrow.setMax(100); // Maximum Progress
        progSorrow.setProgressDrawable(drawable3);

        ObjectAnimator animation4 = ObjectAnimator.ofInt(progSurprise, "progress", 0, surprise);
        animation4.setDuration(990);
        animation4.setInterpolator(new DecelerateInterpolator());
        animation4.start();
        progSurprise.setProgress(surprise);   // Main Progress
        progSurprise.setMax(100); // Maximum Progress
        progSurprise.setProgressDrawable(drawable4);

        bShowMedia.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivImage:
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSIONS);
                break;

            case R.id.bShowMedia:
                String s = "";
                if (joyLikelihood > angerLikelihood && joyLikelihood > sorrowLikelihood && joyLikelihood > surpriseLikelihood)
                    s = "joy";
                else if (angerLikelihood > joyLikelihood && angerLikelihood > sorrowLikelihood && angerLikelihood > surpriseLikelihood)
                    s = "anger";
                else if (sorrowLikelihood > joyLikelihood && sorrowLikelihood > angerLikelihood && sorrowLikelihood > surpriseLikelihood)
                    s = "sorrow";
                else if (surpriseLikelihood > joyLikelihood && surpriseLikelihood > angerLikelihood && surpriseLikelihood > sorrowLikelihood)
                    s = "surprise";
                else
                    s = "mix";
                Intent tabIntent = new Intent(MainActivity.this, TabData.class);
                tabIntent.putExtra("mood", s);
                startActivity(tabIntent);
                break;
        }
    }
}
