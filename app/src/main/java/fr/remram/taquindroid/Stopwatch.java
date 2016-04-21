package fr.remram.taquindroid;

import android.os.SystemClock;

class Stopwatch {

    private long m_StartTime = -1;
    private long m_StopTime = -1;

    public void start()
    {
        if(m_StartTime == -1)
            m_StartTime = SystemClock.elapsedRealtime();
        else if(m_StopTime != -1)
        {
            long now = SystemClock.elapsedRealtime();
            long stopped_for = now - m_StopTime;
            m_StartTime += stopped_for;
            m_StopTime = -1;
        }
    }

    public void stop()
    {
        if(m_StopTime == -1)
            m_StopTime = SystemClock.elapsedRealtime();
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
        return now - m_StartTime - stopped_for;
    }

}
