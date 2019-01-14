package com.ygs;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class ProgressInfo   {
    private int progress = 0;
    private  int barCounter =0;
    private boolean state=false;
    private  WinUser.WNDENUMPROC wndenumproc;
    public  boolean getState(){
     //   System.out.println("progress"+progress);
        state = progress>=100?true:false;
        return state;
    }
    public int getProgress() {
        return progress;
    }

    public WinUser.WNDENUMPROC getWndenumproc() {
        return wndenumproc;
    }

    public ProgressInfo() {
        wndenumproc = new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hwnd, Pointer pointer) {

                char[] textBuffer = new char[512];
                char[] textBuffer2 = new char[512];
                User32.INSTANCE.GetClassName(hwnd, textBuffer, 512);
                User32.INSTANCE.GetWindowText(hwnd, textBuffer2, 512);

                String className = Native.toString(textBuffer);
                String title = Native.toString(textBuffer2);

                if (className.matches("TProgressBar")) {
       //             System.out.println("bar couter"+barCounter);
                    barCounter++;
                    if(barCounter==1){
                        WinDef.LRESULT lresult = User32.INSTANCE.SendMessage(hwnd, WinUser.WM_USER + 8, null, null);
         //               System.out.println("atUpdate bar 1 " + lresult.intValue());
                        int lprogress = lresult.intValue();
                    }
                    else {
                        WinDef.LRESULT lresult = User32.INSTANCE.SendMessage(hwnd, WinUser.WM_USER + 8, null, null);
           //             System.out.println("atUpdate bar 2 " + lresult.intValue());
                        int lprogress = lresult.intValue();
                        barCounter=0;
                    }
                    if(state){progress=0;
                        barCounter=0;
                    }
                    // WinUser.MSG msg = new WinUser.MSG("");
                    // User32.INSTANCE.GetMessage(msg,hwnd,0,0);
                    //   System.out.println("ok founded progress bar");
                    if(barCounter==1) {
                        WinDef.LRESULT lresult = User32.INSTANCE.SendMessage(hwnd, WinUser.WM_USER + 8, null, null);
             //           System.out.println("atUpdate" + lresult.intValue());
                        int lprogress = lresult.intValue();

                        progress = lprogress;
                    }

                    //в программе 2 прогресс бара, но поскольку я всегда перезаписываю значения  с первого вторым , то и результат всегда верный
                    //System.out.println("progress: "+progress);
                }

                return true;
            }
        };
    }

//                        return false;
    }

