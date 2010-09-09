package fr.supelec.rez_gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        int num;
        
    }

    private int m_Width, m_Height;
    private Block[] m_Blocks = null;
    private int m_ActiveBlock = -1;
    private int[][] m_Grid = null;
    private int m_EmptyX, m_EmptyY;
    private long m_AnimBegan;
    private final Paint m_Paint = new Paint();
    private int m_TileSize;

    public GameView(Context context, int width, int height)
    {
        super(context);
        m_Width = width;
        m_Height = height;

        // Create the blocks
        m_Blocks = new Block[m_Width * m_Height - 1];
        m_Grid = new int[m_Width][m_Height];
        for(int y = 0; y < m_Height; ++y)
            for(int x = 0; x < m_Width; ++x)
            {
                if(y == m_Height-1 && x == m_Width - 1)
                    m_Grid[x][y] = -1;
                else
                {
                    m_Grid[x][y] = y*m_Width + x;
                    m_Blocks[y*m_Width+x] = new Block();
                    m_Blocks[y*m_Width+x].num = y*3 + x + 1;
                    m_Blocks[y*m_Width+x].x = x;
                    m_Blocks[y*m_Width+x].y = y;
                }
            }
        m_EmptyX = 2; m_EmptyY = 2;
        // TODO : place the blocks randomly
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        int tileSizeX = (int)Math.floor(w/m_Width);
        int tileSizeY = (int)Math.floor(h/m_Height);
        m_TileSize = Math.min(tileSizeX, tileSizeY);
    }

    private void drawBlock(Canvas canvas, float x, float y, String caption, int bordercolor, int captioncolor)
    {
        int x1 = (int)Math.round((x  ) * m_TileSize) + 2;
        int x2 = (int)Math.round((x+1) * m_TileSize) - 2;
        int y1 = (int)Math.round((y  ) * m_TileSize) + 2;
        int y2 = (int)Math.round((y+1) * m_TileSize) - 2;
        float[] points = {x1, y1, x2, y1,
                x2, y1, x2, y2,
                x2, y2, x1, y2,
                x1, y2, x1, y1
        };
        m_Paint.setColor(bordercolor);
        canvas.drawLines(points, m_Paint);

        m_Paint.setColor(captioncolor);
        canvas.drawText(caption,
                (x+0.5f)*m_TileSize, (y+0.5f)*m_TileSize,
                m_Paint);
    }

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
                        Integer.toString(m_Blocks[m_ActiveBlock].num), 0xFFFF0000, 0xFF0000FF);
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
            }
            
            // Draw the other blocks
            for(int i = 0; i < m_Blocks.length; ++i)
            {
                if(m_ActiveBlock == i)
                    continue;
                drawBlock(canvas, m_Blocks[i].x, m_Blocks[i].y,
                        Integer.toString(m_Blocks[i].num), 0xFFFFFFFF, 0xFF0000FF);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // Find the correct block
            int x = (int)Math.round(event.getX());
            int y = (int)Math.round(event.getY());
            if(x < m_Width * m_TileSize && y < m_Height * m_TileSize)
            {
                int u = (int)Math.floor(x/m_TileSize);
                int v = (int)Math.floor(y/m_TileSize);
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

}
