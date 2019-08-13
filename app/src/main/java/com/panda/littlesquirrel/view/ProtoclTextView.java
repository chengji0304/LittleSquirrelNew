package com.panda.littlesquirrel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

/**
 * Created by jinjing on 2019/3/28.
 */

public class ProtoclTextView extends TextView {
    private int mLineY;
    private int mViewWidth;

    public ProtoclTextView(Context paramContext)
    {
        super(paramContext);
    }

    public ProtoclTextView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    private void drawScaledText(Canvas paramCanvas, int paramInt, String paramString, float paramFloat)
    {
        boolean bool = isFirstLineOfParagraph(paramInt, paramString);
        float f1 = 0.0F;
        if (bool)
        {
            paramCanvas.drawText("  ", 0.0F, this.mLineY, getPaint());
            f1 = 0.0F + StaticLayout.getDesiredWidth("  ", getPaint());
            paramString = paramString.substring(3);
        }
        float f2 = (this.mViewWidth - paramFloat) / paramString.length() - 1.0F;
        for (int i = 0; i < paramString.length(); i++)
        {
            String str = String.valueOf(paramString.charAt(i));
            float f3 = StaticLayout.getDesiredWidth(str, getPaint());
            paramCanvas.drawText(str, f1, this.mLineY, getPaint());
            f1 += f3 + f2;
        }
    }

    private boolean isFirstLineOfParagraph(int paramInt, String paramString)
    {
        return (paramString.length() > 3) && (paramString.charAt(0) == ' ') && (paramString.charAt(1) == ' ');
    }

    private boolean needScale(String paramString)
    {
        if (paramString.length() == 0);
        while (paramString.charAt(-1 + paramString.length()) == '\n')
            return false;
        return true;
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        TextPaint localTextPaint = getPaint();
        localTextPaint.setColor(getCurrentTextColor());
        localTextPaint.drawableState = getDrawableState();
        this.mViewWidth = getMeasuredWidth();
        String str1 = (String) getText();
        this.mLineY = 0;
        this.mLineY = ((int) (this.mLineY + getTextSize()));
        Layout localLayout = getLayout();
        int i = 0;


        }


    @Override
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    }
}
