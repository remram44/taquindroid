package fr.remram.taquindroid;

import android.os.SystemClock;
import android.util.Log;

public class Stopwatch {

    private long m_StartTime = -1;
    private long m_StopTime = -1;

    public void start()
    {
        Log.v("TIMER", String.format("start(); %d %d", m_StartTime, m_StopTime));
        if(m_StartTime == -1)
            m_StartTime = SystemClock.elapsedRealtime();
        else if(m_StopTime != -1)
        {
            long now = SystemClock.elapsedRealtime();
            long stopped_for = now - m_StopTime;
            m_StartTime += stopped_for;
            m_StopTime = -1;
        }
        Log.v("TIMER", String.format("    %d %d", m_StartTime, m_StopTime));
    }

    public void stop()
    {
        Log.v("TIMER", String.format("stop(); %d %d", m_StartTime, m_StopTime));
        if(m_StopTime == -1)
            m_StopTime = SystemClock.elapsedRealtime();
        Log.v("TIMER", String.format("    %d %d", m_StartTime, m_StopTime));
    }

    public boolean stopped()
    {
        return m_StopTime != -1;
    }

    public long milliseconds()
    {
        long now = SystemClock.elapsedRealtime();
        long stopped_for = 0;
        if(m_StopTime != -1)
            stopped_for = now - m_StopTime;
        Log.v("TIMER", String.format("? %d %d; %d - %d - %d = %d", m_StartTime, m_StopTime,
                now, m_StartTime, stopped_for,
                now - m_StartTime - stopped_for));
        return now - m_StartTime - stopped_for;
    }

}
