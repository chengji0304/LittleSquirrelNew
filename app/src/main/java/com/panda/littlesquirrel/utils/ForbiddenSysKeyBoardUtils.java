package com.panda.littlesquirrel.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.panda.littlesquirrel.base.BaseActivity;

import java.lang.reflect.Method;

/**
 * 禁用系统键盘，禁止长按事件
 */
public class ForbiddenSysKeyBoardUtils {



    /**
     * 禁用系统键盘，禁止长按事件
     *
     * @param editText
     */
    public static void bannedSysKeyBoard(BaseActivity context, EditText editText) {
        //屏蔽系统键盘并显示光标
        if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            context.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //屏蔽editview长按事件
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

}
