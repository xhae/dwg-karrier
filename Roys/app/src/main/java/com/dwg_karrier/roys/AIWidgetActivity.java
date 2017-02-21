package com.dwg_karrier.roys;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIDialog;


public class AIWidgetActivity extends ActionBarActivity {

    private AIDialog aiDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_sample);
        final AIConfiguration config = new AIConfiguration("f0b9a41df1bf49fabab9886efd61168b",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);

        aiDialog = new AIDialog(this, config);
        aiDialog.setResultsListener(new AIDialog.AIDialogListener() {
            @Override
            public void onResult(final AIResponse aiResponse) {
                // TODO Process aiResponse
                aiDialog.close();
    //            Toast.makeText(getApplicationContext(), String.format("%s %s","Successful response: ",
    //                    aiResponse.getResult().getResolvedQuery()), Toast.LENGTH_SHORT).show();

                Result result = aiResponse.getResult();

                int contentType = 0; // 1 - read, 2 - view
                int timeValue = 0; // minute

                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    Log.d("**param","");
                    for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                        if(entry.getKey().equals("voiceAction")) {
                            if(entry.getValue().equals("viewer")) {
                                contentType = 1;
                            } else {
                                contentType = 2;
                            }
                        } else if(entry.getKey().equals("duration")) {
                            try {
                                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                                timeValue = jsonObject.get("amount").getAsInt();
                                if (jsonObject.get("unit").getAsString().equals("h")) {
                                    timeValue *= 60;
                                }
                            } catch (Exception e){
                                Toast.makeText(getApplicationContext(), "다시 말씀해 주세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                String viewtype = "";
                if(contentType == 1) viewtype = "read";
                else if(contentType == 2) viewtype = "show";

                if(timeValue !=0) {
                    Toast.makeText(getApplicationContext(), String.valueOf(timeValue) + "min / " + viewtype, Toast.LENGTH_SHORT).show();

                    Date curTime = new Date(System.currentTimeMillis());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curTime);
                    cal.add(Calendar.MINUTE, timeValue);
                    Date d = new Date(cal.getTimeInMillis());

                    Intent openRcmdList = new Intent(getApplicationContext(), ContentSwipe.class); // open Recommend Lists
                    openRcmdList.putExtra("finTime", d);
                    openRcmdList.putExtra("curTime", curTime);
                    startActivity(openRcmdList);

                } else {
                    Toast.makeText(getApplicationContext(), "다시 말씀해 주세요", Toast.LENGTH_SHORT).show();
                }
                AIWidgetActivity.this.finish();
            }

            @Override
            public void onError(final AIError aiError) {
                // TODO show error message
                aiDialog.close();
                Toast.makeText(getApplicationContext(), "다시 말씀해 주세요", Toast.LENGTH_SHORT).show();


                AIWidgetActivity.this.finish();
            }

            @Override
            public void onCancelled() {
                aiDialog.close();
                Toast.makeText(getApplicationContext(), "Process cancelled", Toast.LENGTH_SHORT).show();
                AIWidgetActivity.this.finish();
            }

        });

        aiDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
       //         Toast.makeText(getApplicationContext(), "Dialog dismissed by user", Toast.LENGTH_SHORT).show();
                AIWidgetActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        aiDialog.showAndListen();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
