package com.example.mixmate2.ui.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import com.example.mixmate2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static androidx.fragment.app.FragmentKt.setFragmentResultListener;

public class MainPlayer extends Fragment {
    TextView TopTitleTrack1;
    TextView TopTitleTrack2;
    SeekBar SpeedBar1;
    SeekBar SpeedBar2;
    TextView TitleTrack1;
    TextView TitleTrack2;
    ImageButton PlayTrack1;
    ImageButton PlayTrack2;
    SeekBar SeekBar1;
    SeekBar SeekBar2;
    String duration1;
    String duration2;
    SeekBar VolumeBar1;
    SeekBar VolumeBar2;
    public static MediaPlayer mediaPlayer1;
    public static MediaPlayer mediaPlayer2;
    ScheduledExecutorService timer1;
    ScheduledExecutorService timer2;

    ArrayList<Uri> uriQueue1 = new ArrayList<>();
    ArrayList<Uri> uriQueue2 = new ArrayList<>();
    public static final int maxVolume = 100;
    public static final int Choose_File = 99;
    public static final int Choose_File2 = 98;
    public static Context contextOfApplication;

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.fragment_player, container,false);
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

//        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
//                        // We use a String here, but any type that can be put in a Bundle is supported.
//                        String result = bundle.getString("bundleKey");
//                        // Do something with the result.
//                    }
//        });
        TopTitleTrack1 = view.findViewById(R.id.TopTitleTrack1);
        TopTitleTrack2 = view.findViewById(R.id.TopTitleTrack2);
        ImageButton SkipTrack1 = view.findViewById(R.id.SkipTrack1);
        ImageButton SkipTrack2 = view.findViewById(R.id.SkipTrack2);
        ToggleButton SetLooping1 = view.findViewById(R.id.SetLooping1);
        ToggleButton SetLooping2 = view.findViewById(R.id.SetLooping2);
        SeekBar SpeedBar1 = view.findViewById(R.id.SpeedBar1);
        SeekBar SpeedBar2 = view.findViewById(R.id.SpeedBar2);
        ImageButton ResetSpeed1 = view.findViewById(R.id.ResetSpeed1);
        ImageButton ResetSpeed2 = view.findViewById(R.id.ResetSpeed2);
        TextView SpeedText = view.findViewById(R.id.SpeedText);
        TextView SpeedText2 = view.findViewById(R.id.SpeedText2);
        Button ReverbButton = view.findViewById(R.id.ReverbButton);
        Button BassBoostButton = view.findViewById(R.id.BassBoostButton);

        ImageButton Track1EffectsButton = view.findViewById(R.id.Track1EffectsButton);
        ImageButton Track2EffectsButton = view.findViewById(R.id.Track2EffectsButton);
        Button FileTrack1 = view.findViewById(R.id.FileTrack1);
        Button FileTrack2 = view.findViewById(R.id.FileTrack2);
        PlayTrack1 = view.findViewById(R.id.PlayTrack1);
        PlayTrack2 = view.findViewById(R.id.PlayTrack2);
        TitleTrack1 = view.findViewById(R.id.TitleTrack1);
        TitleTrack2 = view.findViewById(R.id.TitleTrack2);
        TextView SongProgress1 = view.findViewById(R.id.SongProgress1);
        TextView SongProgress2 = view.findViewById(R.id.SongProgress2);
        SeekBar1 = view.findViewById(R.id.SeekBar1);
        SeekBar2 = view.findViewById(R.id.SeekBar2);
        VolumeBar1 = view.findViewById(R.id.VolumeBar1);
        VolumeBar2 = view.findViewById(R.id.VolumeBar2);


        /** ==================================================================
         *                      Top - Effects Panel
         * Skip
         */
        SkipTrack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer1 != null) {
                    mediaPlayer1.seekTo(mediaPlayer1.getDuration());
                    mediaPlayer1.start();
                }
            }
        });
        SkipTrack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer2 != null) {
                    mediaPlayer2.seekTo(mediaPlayer2.getDuration());
                    mediaPlayer2.start();
                }
            }
        });
        /**
         * Looping
         */
        SetLooping1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (mediaPlayer1 != null) {
                    if (isChecked) {
                        mediaPlayer1.setLooping(true);
                    } else {
                        mediaPlayer1.setLooping(false);
                    }
                } else {
                    SetLooping1.setChecked(false);
                }
            }
        });
        SetLooping2.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (mediaPlayer2 != null) {
                    if (isChecked) {
                        mediaPlayer2.setLooping(true);
                    } else {
                        mediaPlayer2.setLooping(false);
                    }
                } else {
                    SetLooping2.setChecked(false);
                }
            }
        });
        /**
         * Speed
         */
        SpeedBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((mediaPlayer1 != null) && (!mediaPlayer1.isPlaying())) {
                    mediaPlayer1.setPlaybackParams(
                            mediaPlayer1.getPlaybackParams().setSpeed(seekBar.getProgress() / 50f));
                    mediaPlayer1.pause();
                } else if (mediaPlayer1 != null){
                    mediaPlayer1.setPlaybackParams(
                            mediaPlayer1.getPlaybackParams().setSpeed(seekBar.getProgress() / 50f));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });
        SpeedBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((mediaPlayer2 != null) && (!mediaPlayer2.isPlaying())) {
                    mediaPlayer2.setPlaybackParams(
                            mediaPlayer2.getPlaybackParams().setSpeed(seekBar.getProgress() / 50f));
                    mediaPlayer2.pause();
                } else if (mediaPlayer2 != null){
                    mediaPlayer2.setPlaybackParams(
                            mediaPlayer2.getPlaybackParams().setSpeed(seekBar.getProgress() / 50f));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });
        ResetSpeed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mediaPlayer1 != null) && (!mediaPlayer1.isPlaying())) {
                    mediaPlayer1.setPlaybackParams(
                            mediaPlayer1.getPlaybackParams().setSpeed(1));
                    mediaPlayer1.pause();
                } else if (mediaPlayer1 != null) {
                    mediaPlayer1.setPlaybackParams(
                            mediaPlayer1.getPlaybackParams().setSpeed(1));
                }
                SpeedBar1.setProgress(50);
            }
        });
        ResetSpeed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mediaPlayer2 != null) && (!mediaPlayer2.isPlaying())) {
                    mediaPlayer2.setPlaybackParams(
                            mediaPlayer2.getPlaybackParams().setSpeed(1));
                    mediaPlayer2.pause();
                } else if (mediaPlayer2 != null) {
                    mediaPlayer2.setPlaybackParams(
                            mediaPlayer2.getPlaybackParams().setSpeed(1));
                }
                SpeedBar2.setProgress(50);
            }
        });
        /**
         * Extra Effects
         */
        ReverbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getActivity().getApplicationContext(),"Coming Soon.",
                        Toast. LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        });
        BassBoostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getActivity().getApplicationContext(),"Coming Soon.",
                        Toast. LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        });

        /** ==================================================================
         *                      Bottom - Media Playback
         * Open Settings
         */
        Track1EffectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReverbButton.setVisibility(View.VISIBLE);
                BassBoostButton.setVisibility(View.VISIBLE);
                TopTitleTrack1.setVisibility(View.VISIBLE);
                SkipTrack1.setVisibility(View.VISIBLE);
                SetLooping1.setVisibility(View.VISIBLE);
                SpeedBar1.setVisibility(View.VISIBLE);
                ResetSpeed1.setVisibility(View.VISIBLE);
                SpeedText.setVisibility(View.VISIBLE);
                TopTitleTrack2.setVisibility(View.GONE);
                SkipTrack2.setVisibility(View.GONE);
                SetLooping2.setVisibility(View.GONE);
                SpeedBar2.setVisibility(View.GONE);
                ResetSpeed2.setVisibility(View.GONE);
                SpeedText2.setVisibility(View.GONE);
            }
        });
        Track2EffectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReverbButton.setVisibility(View.VISIBLE);
                BassBoostButton.setVisibility(View.VISIBLE);
                TopTitleTrack1.setVisibility(View.GONE);
                SkipTrack1.setVisibility(View.GONE);
                SetLooping1.setVisibility(View.GONE);
                SpeedBar1.setVisibility(View.GONE);
                ResetSpeed1.setVisibility(View.GONE);
                SpeedText.setVisibility(View.GONE);
                TopTitleTrack2.setVisibility(View.VISIBLE);
                SkipTrack2.setVisibility(View.VISIBLE);
                SetLooping2.setVisibility(View.VISIBLE);
                SpeedBar2.setVisibility(View.VISIBLE);
                ResetSpeed2.setVisibility(View.VISIBLE);
                SpeedText2.setVisibility(View.VISIBLE);
            }
        });

        /**
         * Open File
         */
        FileTrack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, Choose_File);
            }
        });

        FileTrack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("audio/*");
                startActivityForResult(intent2, Choose_File2);
            }
        });

        /**
         * Play and Pause Buttons
         */
        PlayTrack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer1 != null) {
                    if (mediaPlayer1.isPlaying()){
                        mediaPlayer1.pause();
                        PlayTrack1.setImageResource(R.drawable.playtrack);
                        timer1.shutdown();
                    } else {
                        mediaPlayer1.start();
                        PlayTrack1.setImageResource(R.drawable.playtrackpause);

                        mediaPlayer1.setPlaybackParams(
                                mediaPlayer1.getPlaybackParams().setSpeed(SpeedBar1.getProgress() / 50f));
                        final float volume = (float) (1 - (Math.log(maxVolume - VolumeBar1.getProgress()) / Math.log(maxVolume)));
                        mediaPlayer1.setVolume(volume, volume);

                        timer1 = Executors.newScheduledThreadPool(1);
                        timer1.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer1 != null) {
                                    if (!SeekBar1.isPressed()) {
                                        SeekBar1.setProgress(mediaPlayer1.getCurrentPosition());
                                    }
                                }
                            }
                        }, 10, 10, TimeUnit.MILLISECONDS);
                    }
                }
            }
        });

        PlayTrack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer2 != null) {
                    if (mediaPlayer2.isPlaying()){
                        mediaPlayer2.pause();
                        PlayTrack2.setImageResource(R.drawable.playtrack);
                        timer2.shutdown();
                    } else {
                        //mediaPlayer2.setPlaybackParams(mediaPlayer2.getPlaybackParams().setSpeed(0.5f));
                        mediaPlayer2.start();
                        PlayTrack2.setImageResource(R.drawable.playtrackpause);

                        mediaPlayer2.setPlaybackParams(
                                mediaPlayer2.getPlaybackParams().setSpeed(SpeedBar2.getProgress() / 50f));
                        final float volume = (float) (1 - (Math.log(maxVolume - VolumeBar2.getProgress()) / Math.log(maxVolume)));
                        mediaPlayer2.setVolume(volume, volume);

                        timer2 = Executors.newScheduledThreadPool(1);
                        timer2.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer2 != null) {
                                    // reset the seek bar
                                    if (!SeekBar2.isPressed()) {
                                        SeekBar2.setProgress(mediaPlayer2.getCurrentPosition());
                                    }
                                }
                            }
                        }, 10, 10, TimeUnit.MILLISECONDS);
                    }
                }
            }
        });

        /**
         * Song Progress (Time) and Seek functionality
         */
        SeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer1 != null){
                    int currentMilliseconds = mediaPlayer1.getCurrentPosition();
                    long currentSeconds = currentMilliseconds / 1000;
                    long showMinutes = currentSeconds / 60;
                    long showSeconds = currentSeconds - (showMinutes * 60);
                    SongProgress1.setText(showMinutes + ":" + showSeconds + " / " + duration1);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer1 != null) {
                    mediaPlayer1.seekTo(seekBar.getProgress());
                }
            }
        });
        SeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer2 != null){
                    int currentMilliseconds = mediaPlayer2.getCurrentPosition();
                    long currentSeconds = currentMilliseconds / 1000;
                    long showMinutes = currentSeconds / 60;
                    long showSeconds = currentSeconds - (showMinutes * 60);
                    SongProgress2.setText(showMinutes + ":" + showSeconds + " / " + duration2);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer2 != null) {
                    mediaPlayer2.seekTo(seekBar.getProgress());
                }
            }
        });

        /**
         * Volume Bars of Tracks
         */
        VolumeBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //nothing
                if (mediaPlayer1 != null) {
                    // an equation is needed as volume does not increase linearly
                    final float volume = (float) (1 -
                            (Math.log(maxVolume - seekBar.getProgress()) / Math.log(maxVolume)));
                    mediaPlayer1.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });
        VolumeBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //nothing
                if (mediaPlayer2 != null) {
                    // Equation is needed as volume does not increase linearly
                    final float volume = (float) (1 -
                            (Math.log(maxVolume - seekBar.getProgress()) / Math.log(maxVolume)));
                    mediaPlayer2.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });

        PlayTrack1.setEnabled(false);
        PlayTrack2.setEnabled(false);
        VolumeBar1.setProgress(100);
        VolumeBar2.setProgress(100);
        SpeedBar1.setProgress(50);
        SpeedBar2.setProgress(50);
        SetLooping1.setText(null);
        SetLooping1.setTextOn(null);
        SetLooping1.setTextOff(null);
        SetLooping2.setText(null);
        SetLooping2.setTextOn(null);
        SetLooping2.setTextOff(null);
        TopTitleTrack1.setVisibility(View.GONE);
        SkipTrack1.setVisibility(View.GONE);
        SetLooping1.setVisibility(View.GONE);
        SpeedBar1.setVisibility(View.GONE);
        ResetSpeed1.setVisibility(View.GONE);
        SpeedText.setVisibility(View.GONE);
        TopTitleTrack2.setVisibility(View.GONE);
        SkipTrack2.setVisibility(View.GONE);
        SetLooping2.setVisibility(View.GONE);
        SpeedBar2.setVisibility(View.GONE);
        ResetSpeed2.setVisibility(View.GONE);
        SpeedText2.setVisibility(View.GONE);
        ReverbButton.setVisibility(View.GONE);
        BassBoostButton.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        return view;
    }

    /**
     * Open File Continued. Read the uri from the file selected to put into media player.
     * @param requestCode to tell the activity is ours
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Choose_File && resultCode == Activity.RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                uriQueue1.add(uri);
                if (mediaPlayer1 == null) {
                    createMediaPlayer(uri, 1);
                }
            }
        }
        else if (requestCode == Choose_File2 && resultCode == Activity.RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                uriQueue2.add(uri);
                if (mediaPlayer2 == null) {
                    createMediaPlayer(uri, 2);
                }
            }
        }
    }

    public void createMediaPlayer(Uri uri, Integer trackNumber){
        if (trackNumber == 1) {
            mediaPlayer1 = new MediaPlayer();
            mediaPlayer1.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer1.setDataSource(getActivity().getApplicationContext(), uri);
                mediaPlayer1.prepare();

                // Apply settings. This all happens without pressing play.

                String titleName = getNameFromUri(uri);
                String titleNameShort = titleName;
                if (titleName.length() > 21) {
                    titleNameShort = titleName.substring(0, 21) + "..";
                }
                TitleTrack1.setText(titleNameShort);
                if (titleName.length() > 42) {
                    titleName = titleName.substring(0, 42) + "..";
                }
                TopTitleTrack1.setText("Cue 1 (" + titleName + ")");
                PlayTrack1.setEnabled(true);
                PlayTrack1.setImageResource(R.drawable.playtrack);

                int maxMilliseconds = mediaPlayer1.getDuration();
                long maxSeconds = maxMilliseconds / 1000;
                long maxMinutes = maxSeconds / 60;
                long showMaxSeconds = maxSeconds - (maxMinutes * 60);
                duration1 = maxMinutes + ":" + showMaxSeconds;
                SeekBar1.setMax(maxMilliseconds);
                SeekBar1.setProgress(0);

                mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer(1);
                        uriQueue1.remove(0);
                        if (uriQueue1.size() > 0){
                            createMediaPlayer(uriQueue1.get(0), 1);
                            PlayTrack1.performClick();
                        } else {
                            PlayTrack1.setImageResource(R.drawable.playtrackdisabled);
                            PlayTrack1.setEnabled(false);
                        }
                    }
                });
            } catch (IOException e) {
                TitleTrack1.setText(e.toString());
            }
        }
        else if (trackNumber == 2){
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer2.setDataSource(getActivity().getApplicationContext(), uri);
                mediaPlayer2.prepare();

                String titleName = getNameFromUri(uri);
                String titleNameShort = titleName;
                if (titleName.length() > 21) {
                    titleNameShort = titleName.substring(0, 21) + "..";
                }
                TitleTrack2.setText(titleNameShort);
                if (titleName.length() > 42) {
                    titleName = titleName.substring(0, 42) + "..";
                }
                TopTitleTrack2.setText("Cue 2 (" + titleName + ")");
                PlayTrack2.setEnabled(true);
                PlayTrack2.setImageResource(R.drawable.playtrack);

                int maxMilliseconds = mediaPlayer2.getDuration();
                long maxSeconds = maxMilliseconds / 1000;
                long maxMinutes = maxSeconds / 60;
                long showMaxSeconds = maxSeconds - (maxMinutes * 60);
                duration2 = maxMinutes + ":" + showMaxSeconds;
                SeekBar2.setMax(maxMilliseconds);
                SeekBar2.setProgress(0);

                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer(2);
                        uriQueue2.remove(0);
                        if (uriQueue2.size() > 0){
                            createMediaPlayer(uriQueue2.get(0), 2);
                            PlayTrack2.performClick();
                        } else {
                            PlayTrack2.setImageResource(R.drawable.playtrackdisabled);
                            PlayTrack2.setEnabled(false);
                        }
                    }
                });
            } catch (IOException e) {
                //nothing
            }
        }
    }

    @SuppressLint("Range")
    public String getNameFromUri(Uri uri){
        String fileName = "";
        Cursor cursor = null;
        cursor = getActivity().getApplicationContext().getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileName;
    }

    /**
     * Called when a scene or game changes or ends.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer(1);
        releaseMediaPlayer(2);
    }

    /**
     * Called when media player is finished.
     * @param trackNumber Track 1 or 2
     */
    public void releaseMediaPlayer(Integer trackNumber) {
        if (trackNumber == 1) {
            if (timer1 != null) {
                timer1.shutdown();
            }
            if (mediaPlayer1 != null) {
                mediaPlayer1.release();
                mediaPlayer1 = null;
            }
            PlayTrack1.setEnabled(false);
            PlayTrack1.setImageResource(R.drawable.playtrackdisabled);
            TitleTrack1.setText("");
            SeekBar1.setMax(100);
            SeekBar1.setProgress(0);
        } else if (trackNumber == 2) {
            if (timer2 != null) {
                timer2.shutdown();
            }
            if (mediaPlayer2 != null) {
                mediaPlayer2.release();
                mediaPlayer2 = null;
            }
            PlayTrack2.setEnabled(false);
            PlayTrack2.setImageResource(R.drawable.playtrackdisabled);
            TitleTrack2.setText("");
            SeekBar2.setMax(100);
            SeekBar2.setProgress(0);
        }
    }
}
