package com.zdjer.win.view.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.decoding.ScanActivityHandler;
import com.mining.app.zxing.view.ViewfinderView;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.win.R;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.view.core.ScanTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class ScanActivity extends BaseActivity implements Callback {

    private ScanActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ScanTypes scanType = ScanTypes.Single;
    private LinearLayout llScanMutil = null;
    private ArrayList<String> lstBarCode = null;
    private SharedPreferences sharedPreferences = null;
    private String token = "";
    private Boolean isOnline = false;
    private String ip = "";
    private RecordBO record = null;
    private RecordBLO recordBlo = null;
    private int resultCode = 10;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.scan_viewfinder_view);

        llScanMutil = (LinearLayout) findViewById(R.id.scan_ll_mutil);

        //返回
        TextView tvBack = (TextView) findViewById(R.id.scan_tv_back);
        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.this.finish();
            }
        });

        //继续扫描
        /*Button btnContinue = (Button) findViewById(R.id.scan_btn_continue);
        btnContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                onResume();
            }
        });*/

        //结束扫描
        Button btnBreak = (Button) findViewById(R.id.scan_btn_break);
        btnBreak.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.this.setResult(10);
                ScanActivity.this.finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        initView();
    }

    /**
     * 初始化控件
     */
    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            scanType = ScanTypes.value(intent.getIntExtra("scanType", ScanTypes.Single.getValue()));
            Log.i("scanType:", String.valueOf(scanType));
        }
        if (ScanTypes.Single == scanType) {
            llScanMutil.setVisibility(View.INVISIBLE);
        } else if (ScanTypes.Mutil == scanType) {
            llScanMutil.setVisibility(View.VISIBLE);

            if (sharedPreferences == null) {
                sharedPreferences = getSharedPreferences(
                        "win", MODE_PRIVATE);
            }
            token = sharedPreferences.getString("currToken", "");
            isOnline = sharedPreferences.getBoolean("isonline", false);
            ip = sharedPreferences.getString("ip", "");
            record=(RecordBO)intent.getSerializableExtra("record");
        }
    }

    @Override
    public void initData() {

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scan_preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        try{
        Log.i("resultBarcode", "=========");
        Log.i("scanType:", String.valueOf(scanType));
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultBarcode = result.getText();
        if (resultBarcode.equals("")) {
            Toast.makeText(ScanActivity.this, R.string.scan_failed, Toast.LENGTH_SHORT).show();
        } else {
            if (resultBarcode.length() != 13) {
                String message = "条形码“" + resultBarcode + "”格式不正确。请扫描13位条形码！";
                Toast.makeText(ScanActivity.this, message, Toast.LENGTH_LONG).show();
                onPause();
                onResume();
                return;
            }
            // 确定
            if (ScanTypes.Single == scanType) {
                Intent intentBarcode = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultBarcode);
                intentBarcode.putExtras(bundle);
                this.setResult(resultCode, intentBarcode);
                Log.i("ScanTypes", "ScanTypes.Single");
                ScanActivity.this.finish();
            } else if (ScanTypes.Mutil == scanType) {
                if (recordBlo == null) {
                    recordBlo = new RecordBLO();
                }
                if (recordBlo.isExist(record.getRecordType(), resultBarcode)) {
                    Toast.makeText(ScanActivity.this, R.string.record_barcode_exist,
                            Toast.LENGTH_SHORT).show();
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error2);// 提示音
                    AudioHelper.startVibrator(this);
                    onPause();
                    onResume();
                    return;
                }
                StringBuffer msg = new StringBuffer("");
                Date createDate = DateHelper.getCurrDate();
                record.setCreateDate(createDate);
                record.setBarCode(resultBarcode);
                if (recordBlo.addRecord(isOnline, record, ip, token, msg)) {
                    Toast.makeText(ScanActivity.this, R.string.wms_common_save_success,
                            Toast.LENGTH_LONG).show();
                    AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                    onPause();
                    onResume();

                } else {
                    if (msg.toString().length() != 0) {
                        Toast.makeText(ScanActivity.this, msg.toString(), Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(ScanActivity.this, R.string.wms_common_save_failed,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        } catch (Exception e) {
            Toast.makeText(ScanActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new ScanActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.zdjer_beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {

    }
}