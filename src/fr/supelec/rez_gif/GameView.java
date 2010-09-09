package fr.supelec.rez_gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * The custom View where the game actually happens.
 */
public class GameView extends View {

    private class Block {
        
        float x;
        float y;
        int num;
        
    }

    private int m_Width, m_Height;
    private Block[] m_Blocks = null;
    private int m_ActiveBlock = -1;
    private int[][] m_Grid = null;
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
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        int tileSizeX = (int)Math.floor(w/m_Width);
        int tileSizeY = (int)Math.floor(h/m_Height);
        m_TileSize = Math.min(tileSizeX, tileSizeY);
    }

    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(m_Blocks != null)
        {
            for(int i = 0; i < m_Blocks.length; ++i)
            {
                if(m_ActiveBlock == i)
                    m_Paint.setColor(0xFFFF0000);
                else
                    m_Paint.setColor(0xFFFFFFFF);
                int x1 = (int)Math.round(m_Blocks[i].x  ) * m_TileSize + 2;
                int x2 = (int)Math.round(m_Blocks[i].x+1) * m_TileSize - 2;
                int y1 = (int)Math.round(m_Blocks[i].y  ) * m_TileSize + 2;
                int y2 = (int)Math.round(m_Blocks[i].y+1) * m_TileSize - 2;
                float[] points = {x1, y1, x2, y1,
                        x2, y1, x2, y2,
                        x2, y2, x1, y2,
                        x1, y2, x1, y1
                };
                canvas.drawLines(points, m_Paint);

                m_Paint.setColor(0xFF0000FF);
                canvas.drawText(Integer.toString(m_Blocks[i].num),
                        (m_Blocks[i].x+0.5f)*m_TileSize, (m_Blocks[i].y+0.5f)*m_TileSize,
                        m_Paint);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            m_ActiveBlock = -1;
            invalidate();
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // Find the correct block
            int x = (int)Math.round(event.getX());
            int y = (int)Math.round(event.getY());
            if(x < m_Width * m_TileSize && y < m_Height * m_TileSize)
            {
                int u = (int)Math.floor(x/m_TileSize);
                int v = (int)Math.floor(y/m_TileSize);
                m_ActiveBlock = m_Grid[u][v];
                GameView.this.invalidate();
                return true;
            }
        }
        return false;
    }

}
