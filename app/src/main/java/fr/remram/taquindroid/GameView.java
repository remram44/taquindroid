package fr.remram.taquindroid;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * The custom View where the game actually happens.
 */
public class GameView extends View {

    private class Block {

        int x;
        int y;
        Bitmap bitmap;

    }

    public interface EndGameListener {

        void onGameEnded();

    }

    private int m_Width, m_Height;
    private Block[] m_Blocks = null;
    private int m_ActiveBlock = -1;
    private int[][] m_Grid = null;
    private int m_EmptyX, m_EmptyY;
    private long m_AnimBegan;
    private final Paint m_Paint = new Paint();
    private int m_TileSizeX;
    private int m_TileSizeY;
    private float m_TileAspect;
    private EndGameListener m_EndGameListener;

    public GameView(Context context, Bitmap image, int width, int height)
    {
        super(context);
        m_Width = width;
        m_Height = height;

        // Prepare the grid
        m_Grid = new int[m_Width][m_Height];
        for(int y = 0; y < m_Height; ++y)
            for(int x = 0; x < m_Width; ++x)
            {
                if(y == m_Height-1 && x == m_Width - 1)
                    m_Grid[x][y] = -1;
                else
                    m_Grid[x][y] = y*m_Width + x;
            }
        m_EmptyX = m_Width - 1; m_EmptyY = m_Height - 1;

        // Move the blocks randomly
        Random rand = new Random();
        int[][] dirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for(int m = 0; m < 1000; ++m)
        {
            int dir = rand.nextInt(4);
            int chx = m_EmptyX + dirs[dir][0];
            int chy = m_EmptyY + dirs[dir][1];
            if(0 <= chx && chx < m_Width && 0 <= chy && chy < m_Height)
            {
                m_Grid[m_EmptyX][m_EmptyY] = m_Grid[chx][chy];
                m_EmptyX = chx;
                m_EmptyY = chy;
                m_Grid[m_EmptyX][m_EmptyY] = -1;
            }
        }

        // Create the blocks
        int w = image.getWidth()/m_Width;
        int h = image.getHeight()/m_Height;
        m_TileAspect = (1.f*h)/w;
        m_Blocks = new Block[m_Width * m_Height - 1];
        for(int y = 0; y < m_Height; ++y)
            for(int x = 0; x < m_Width; ++x)
            {
                int id = m_Grid[x][y];
                if(id != -1)
                {
                    m_Blocks[id] = new Block();
                    m_Blocks[id].x = x;
                    m_Blocks[id].y = y;
                    int iy = id / m_Width;
                    int ix = id % m_Width;
                    m_Blocks[id].bitmap = Bitmap.createBitmap(image, ix*w, iy*h, w, h);
                }
            }

        // Recompute the tile size now that the aspect ratio of the tiles is available
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    public void setEndGameListener(EndGameListener listener)
    {
        m_EndGameListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if(m_TileAspect != 0)
        {
            float tileSizeX = w/m_Width;
            float tileSizeY = h/m_Height;
            m_TileSizeX = (int)Math.floor(Math.min(tileSizeX, tileSizeY/m_TileAspect));
            m_TileSizeY = (int)Math.floor(Math.min(tileSizeX*m_TileAspect, tileSizeY));
        }
    }

    private void drawBlock(Canvas canvas, float x, float y, Bitmap bitmap, int bordercolor)
    {
        float rx = x*m_TileSizeX;
        float ry = y*m_TileSizeY;
        canvas.drawBitmap(bitmap, null, new RectF(rx, ry, rx+m_TileSizeX, ry+m_TileSizeY), m_Paint);

        int x1 = Math.round((x  ) * m_TileSizeX) + 2;
        int x2 = Math.round((x+1) * m_TileSizeX) - 2;
        int y1 = Math.round((y  ) * m_TileSizeY) + 2;
        int y2 = Math.round((y+1) * m_TileSizeY) - 2;
        float[] points = {x1, y1, x2, y1,
                x2, y1, x2, y2,
                x2, y2, x1, y2,
                x1, y2, x1, y1
        };
        m_Paint.setColor(bordercolor);
        canvas.drawLines(points, m_Paint);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(m_Blocks != null)
        {
            // Animation for the active block
            if(m_ActiveBlock != -1 && SystemClock.uptimeMillis() - m_AnimBegan < 200)
            {
                int start_x, start_y;
                start_x = m_Blocks[m_ActiveBlock].x;
                start_y = m_Blocks[m_ActiveBlock].y;
                float x = start_x + (m_EmptyX - start_x) * (SystemClock.uptimeMillis() - m_AnimBegan)/200.f;
                float y = start_y + (m_EmptyY - start_y) * (SystemClock.uptimeMillis() - m_AnimBegan)/200.f;

                drawBlock(canvas, x, y,
                        m_Blocks[m_ActiveBlock].bitmap, 0xFFFF0000);
                invalidate(); // continue animating...
            }
            // Animation ended
            else if(m_ActiveBlock != -1)
            {
                int old_x = m_Blocks[m_ActiveBlock].x;
                int old_y = m_Blocks[m_ActiveBlock].y;
                m_Blocks[m_ActiveBlock].x = m_EmptyX;
                m_Blocks[m_ActiveBlock].y = m_EmptyY;
                m_EmptyX = old_x;
                m_EmptyY = old_y;
                m_Grid[m_EmptyX][m_EmptyY] = -1;
                m_Grid[m_Blocks[m_ActiveBlock].x][m_Blocks[m_ActiveBlock].y] = m_ActiveBlock;
                m_ActiveBlock = -1;
                checkEnd();
            }

            // Draw the other blocks
            for(int i = 0; i < m_Blocks.length; ++i)
            {
                if(m_ActiveBlock == i)
                    continue;
                drawBlock(canvas, m_Blocks[i].x, m_Blocks[i].y,
                        m_Blocks[i].bitmap, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // Find the correct block
            int x = Math.round(event.getX());
            int y = Math.round(event.getY());
            if(x < m_Width * m_TileSizeX && y < m_Height * m_TileSizeY)
            {
                int u = (int)Math.floor(x/m_TileSizeX);
                int v = (int)Math.floor(y/m_TileSizeY);
                // If this block can move...
                if(m_ActiveBlock == -1
                 && Math.abs(u-m_EmptyX) + Math.abs(v-m_EmptyY) == 1)
                {
                    m_ActiveBlock = m_Grid[u][v];
                    m_AnimBegan = SystemClock.uptimeMillis();
                    GameView.this.invalidate();
                    return true;
                }
            }
        }
        return false;
    }

    private void checkEnd()
    {
        for(int y = 0; y < m_Height; ++y)
            for(int x = 0; x < m_Width; ++x)
            {
                if(x == m_Width-1 && y == m_Height-1)
                    continue;
                if(m_Grid[x][y] != y*m_Width + x)
                    return ;
            }
        m_EndGameListener.onGameEnded();
    }

}
