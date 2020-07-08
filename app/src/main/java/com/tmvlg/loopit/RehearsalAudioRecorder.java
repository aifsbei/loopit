package com.tmvlg.loopit;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

public class RehearsalAudioRecorder
{
    /**
     * INITIALIZING : recorder is initializing;
     * READY : recorder has been initialized, recorder not yet started
     * RECORDING : recording
     * ERROR : reconstruction needed
     * STOPPED: reset needed
     */
    public enum State {INITIALIZING, READY, RECORDING, ERROR, STOPPED};

    public static final boolean RECORDING_UNCOMPRESSED = true;
    public static final boolean RECORDING_COMPRESSED = false;

    // The interval in which the recorded samples are output to the file
    // Used only in uncompressed mode
    private static final int TIMER_INTERVAL = 120;

    // Toggles uncompressed recording on/off; RECORDING_UNCOMPRESSED / RECORDING_COMPRESSED
    private boolean 		 rUncompressed;

    // Recorder used for uncompressed recording
    private AudioRecord 	 aRecorder = null;
    // Recorder used for compressed recording
    private MediaRecorder	 mRecorder = null;

    // Stores current amplitude (only in uncompressed mode)
    private int				 cAmplitude= 0;
    // Output file path
    private String			 fPath = null;

    // Recorder state; see State
    private State			 state;

    // File writer (only in uncompressed mode)
    private RandomAccessFile fWriter;
    private FileChannel		 fChannel;

    // Number of channels, sample rate, sample size(size in bits), buffer size, audio source, sample size(see AudioFormat)
    private short 			 nChannels;
    private int				 sRate;
    private short			 bSamples;
    private int				 bufferSize;
    private int				 aSource;
    private int				 aFormat;

    // Number of frames written to file on each output(only in uncompressed mode)
    private int				 framePeriod;

    // Buffer for output(only in uncompressed mode)
    private ShortBuffer		shBuffer;
    private ByteBuffer		bBuffer;

    // Number of bytes written to file after header(only in uncompressed mode)
    // after stop() is called, this size is written to the header/data chunk in the wave file
    private int				 payloadSize;


    // Apply gain - Supported only in uncompressed recording
    private double 			gain; // Gain converted from dB to multiplier;
    // Based on info found in WaveGain application
    // multiplier = 10 ^ (0.05 * dB)

    /**
     *
     * Returns the state of the recorder in a RehearsalAudioRecord.State typed object.
     * Useful, as no exceptions are thrown.
     *
     * @return recorder state
     */
    public State getState()
    {
        return state;
    }

    /*
     *
     * Method used for recording.
     *
     */
    private AudioRecord.OnRecordPositionUpdateListener updateListener = new AudioRecord.OnRecordPositionUpdateListener()
    {
        @Override
        public void onPeriodicNotification(AudioRecord recorder)
        {
            aRecorder.read(bBuffer, bBuffer.capacity()); // Fill buffer
            try
            {
                if (bSamples == 16)
                {
                    shBuffer.rewind();
                    int bLength = shBuffer.capacity(); // Faster than accessing buffer.capacity each time
                    for (int i=0; i<bLength; i++)
                    { // 16bit sample size
                        short curSample = (short) (shBuffer.get(i) * gain);
                        if (curSample > cAmplitude)
                        { // Check amplitude
                            cAmplitude = curSample;
                        }
                        shBuffer.put(curSample);
                    }
                }
                else
                { // 8bit sample size
                    int bLength = bBuffer.capacity(); // Faster than accessing buffer.capacity each time
                    bBuffer.rewind();
                    for (int i=0; i<bLength; i++)
                    {
                        byte curSample = (byte) (bBuffer.get(i) * gain);
                        if (curSample > cAmplitude)
                        { // Check amplitude
                            cAmplitude = curSample;
                        }
                        bBuffer.put(curSample);
                    }
                }
                bBuffer.rewind();
                fChannel.write(bBuffer); // Write buffer to file
                payloadSize += bBuffer.capacity();
            }
            catch (IOException e)
            {
                Log.e(RehearsalAudioRecorder.class.getName(), "Error occured in updateListener, recording is aborted");
                stop();
            }
        }

        @Override
        public void onMarkerReached(AudioRecord recorder)
        {
            // NOT USED
        }
    };

    /**
     *
     *
     * Default constructor
     *
     * Instantiates a new recorder, in case of compressed recording the parameters can be left as 0.
     * In case of errors, no exception is thrown, but the state is set to ERROR
     *
     */
    public RehearsalAudioRecorder(boolean uncompressed, int audioSource, int sampleRate, int channelConfig,
                                  int audioFormat)
    {
        try
        {
            rUncompressed = uncompressed;
            if (rUncompressed)
            { // RECORDING_UNCOMPRESSED
                if (audioFormat == AudioFormat.ENCODING_PCM_16BIT)
                {
                    bSamples = 16;
                }
                else
                {
                    bSamples = 8;
                }

                if (channelConfig == AudioFormat.CHANNEL_CONFIGURATION_MONO)
                {
                    nChannels = 1;
                }
                else
                {
                    nChannels = 2;
                }

                aSource = audioSource;
                sRate   = sampleRate;
                aFormat = audioFormat;

                framePeriod = sampleRate * TIMER_INTERVAL / 1000;
                bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
                if (bufferSize < AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat))
                { // Check to make sure buffer size is not smaller than the smallest allowed one
                    Log.d("tagtest ", "yes");
                    bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                    // Set frame period and timer interval accordingly
                    framePeriod = bufferSize / ( 2 * bSamples * nChannels / 8 );
                    Log.w(RehearsalAudioRecorder.class.getName(), "Increasing buffer size to " + Integer.toString(bufferSize));
                }
                aRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize);
                if (aRecorder.getState() != AudioRecord.STATE_INITIALIZED)
                    throw new Exception("AudioRecord initialization failed");
                aRecorder.setRecordPositionUpdateListener(updateListener);
                aRecorder.setPositionNotificationPeriod(framePeriod);
            } else
            { // RECORDING_COMPRESSED
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            cAmplitude = 0;
            gain = 1.0;
            fPath = null;
            state = State.INITIALIZING;
        } catch (Exception e)
        {
            if (e.getMessage() != null)
            {
                Log.e(RehearsalAudioRecorder.class.getName(), e.getMessage());
            }
            else
            {
                Log.e(RehearsalAudioRecorder.class.getName(), "Unknown error occured while initializing recording");
            }
            state = State.ERROR;
        }
    }

    /**
     *
     * Set gain, can be called in any state
     *
     */
    public void setGain(int dBGain)
    {
        gain = Math.pow(10, dBGain*0.05);
    }

    /**
     * Sets output file path, call directly after construction/reset.
     *
     * param output file path
     *
     */
    public void setOutputFile(String argPath)
    {
        try
        {
            if (state == State.INITIALIZING)
            {
                fPath = argPath;
                if (!rUncompressed)
                {
                    mRecorder.setOutputFile(fPath);
                }
            }
        }
        catch (Exception e)
        {
            if (e.getMessage() != null)
            {
                Log.e(RehearsalAudioRecorder.class.getName(), e.getMessage());
            }
            else
            {
                Log.e(RehearsalAudioRecorder.class.getName(), "Unknown error occured while setting output path");
            }
            state = State.ERROR;
        }
    }

    /**
     *
     * Returns the largest amplitude sampled since the last call to this method.
     *
     * @return returns the largest amplitude since the last call, or 0 when not in recording state.
     *
     */
    public int getMaxAmplitude()
    {
        if (state == State.RECORDING)
        {
            if (rUncompressed)
            {
                int result = cAmplitude;
                cAmplitude = 0;
                return result;
            }
            else
            {
                try
                {
                    return mRecorder.getMaxAmplitude();
                }
                catch (IllegalStateException e)
                {
                    return 0;
                }
            }
        }
        else
        {
            return 0;
        }
    }


    /**
     *
     * Prepares the recorder for recording, in case the recorder is not in the INITIALIZING state and the file path was not set
     * the recorder is set to the ERROR state, which makes a reconstruction necessary.
     * In case uncompressed recording is toggled, the header of the wave file is written.
     * In case of an exception, the state is changed to ERROR
     *
     */
    public void prepare()
    {
        try
        {
            if (state == State.INITIALIZING)
            {
                if (rUncompressed)
                {
                    if ((aRecorder.getState() == AudioRecord.STATE_INITIALIZED) & (fPath != null))
                    {
                        // write file header

                        fWriter = new RandomAccessFile(fPath, "rw");
                        fChannel= fWriter.getChannel();

                        fWriter.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed
                        fWriter.writeBytes("RIFF");
                        fWriter.writeInt(0); // Final file size not known yet, write 0
                        fWriter.writeBytes("WAVE");
                        fWriter.writeBytes("fmt ");
                        fWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
                        fWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
                        fWriter.writeShort(Short.reverseBytes(nChannels));// Number of channels, 1 for mono, 2 for stereo
                        fWriter.writeInt(Integer.reverseBytes(sRate)); // Sample rate
                        fWriter.writeInt(Integer.reverseBytes(sRate*bSamples*nChannels/8)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
                        fWriter.writeShort(Short.reverseBytes((short)(nChannels*bSamples/8))); // Block align, NumberOfChannels*BitsPerSample/8
                        fWriter.writeShort(Short.reverseBytes(bSamples)); // Bits per sample
                        fWriter.writeBytes("data");
                        fWriter.writeInt(0); // Data chunk size not known yet, write 0

                        bBuffer = ByteBuffer.allocateDirect(framePeriod*bSamples/8*nChannels);
                        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        bBuffer.rewind();
                        shBuffer = bBuffer.asShortBuffer();

                        state = State.READY;
                    }
                    else
                    {
                        Log.e(RehearsalAudioRecorder.class.getName(), "prepare() method called on uninitialized recorder");
                        state = State.ERROR;
                    }
                }
                else
                {
                    mRecorder.prepare();
                    state = State.READY;
                }
            }
            else
            {
                Log.e(RehearsalAudioRecorder.class.getName(), "prepare() method called on illegal state");
                release();
                state = State.ERROR;
            }
        }
        catch(Exception e)
        {
            if (e.getMessage() != null)
            {
                Log.e(RehearsalAudioRecorder.class.getName(), e.getMessage());
            }
            else
            {
                Log.e(RehearsalAudioRecorder.class.getName(), "Unknown error occured in prepare()");
            }
            state = State.ERROR;
        }
    }

    /**
     *
     *
     *  Releases the resources associated with this class, and removes the unnecessary files, when necessary
     *
     */
    public void release()
    {
        if (state == State.RECORDING)
        {
            stop();
        }
        else
        {
            if ((state == State.READY) & (rUncompressed))
            {
                try
                {
                    fWriter.close(); // Remove prepared file
                }
                catch (IOException e)
                {
                    Log.e(RehearsalAudioRecorder.class.getName(), "I/O exception occured while closing output file");
                }
                (new File(fPath)).delete();
            }
        }

        if (rUncompressed)
        {
            if (aRecorder != null)
            {
                aRecorder.release();
            }
        }
        else
        {
            if (mRecorder != null)
            {
                mRecorder.release();
            }
        }
    }

    /**
     *
     *
     * Resets the recorder to the INITIALIZING state, as if it was just created.
     * In case the class was in RECORDING state, the recording is stopped.
     * In case of exceptions the class is set to the ERROR state.
     *
     */
    public void reset()
    {
        try
        {
            if (state != State.ERROR)
            {
                release();
                fPath = null; // Reset file path
                cAmplitude = 0; // Reset amplitude
                gain = 1.0;
                if (rUncompressed)
                {
                    aRecorder = new AudioRecord(aSource, sRate, nChannels+1, aFormat, bufferSize);
                }
                else
                {
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                }
                state = State.INITIALIZING;
            }
        }
        catch (Exception e)
        {
            Log.e(RehearsalAudioRecorder.class.getName(), e.getMessage());
            state = State.ERROR;
        }
    }

    /**
     *
     *
     * Starts the recording, and sets the state to RECORDING.
     * Call after prepare().
     *
     */
    public void start()
    {
        if (state == State.READY)
        {
            if (rUncompressed)
            {
                payloadSize = 0;
                aRecorder.startRecording();
                aRecorder.read(bBuffer, bBuffer.capacity());
                bBuffer.rewind();
            }
            else
            {
                mRecorder.start();
            }
            state = State.RECORDING;
        }
        else
        {
            Log.e(RehearsalAudioRecorder.class.getName(), "start() called on illegal state");
            state = State.ERROR;
        }
    }

    /**
     *
     *
     *  Stops the recording, and sets the state to STOPPED.
     * In case of further usage, a reset is needed.
     * Also finalizes the wave file in case of uncompressed recording.
     *
     */
    public void stop()
    {
        if (state == State.RECORDING)
        {
            if (rUncompressed)
            {
                aRecorder.stop();

                try
                {
                    fWriter.seek(4); // Write size to RIFF header
                    fWriter.writeInt(Integer.reverseBytes(36+payloadSize));

                    fWriter.seek(40); // Write size to Subchunk2Size field
                    fWriter.writeInt(Integer.reverseBytes(payloadSize));

                    fWriter.close();
                }
                catch(IOException e)
                {
                    Log.e(RehearsalAudioRecorder.class.getName(), "I/O exception occured while closing output file");
                    state = State.ERROR;
                }
            }
            else
            {
                mRecorder.stop();
            }
            state = State.STOPPED;
        }
        else
        {
            Log.e(RehearsalAudioRecorder.class.getName(), "stop() called on illegal state");
            state = State.ERROR;
        }
    }
}