package com.ygs;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//dummy container
class Pos{
    public int x=0,y=0;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
public class WINAPIController {
    static private WinDef.HWND hwnd;

    private  String windowName;
    private int width,height;
    private final int MIN_BTN_GEOMETRY_W = 73;
    private final int MIN_BTN_VIEW_W=47;
    private final int MIN_BTN_CALC_W = 56;
    private final int BTN_START_W = 18;
    private final int LEFT_CLICK = InputEvent.getMaskForButton(1);
    private final Pos FILE_BTN_POS = new Pos(18,40);
    private final Pos REOPEN_BTN_POS = new Pos(118,100);
    private final Pos LAST_FILE_POS = new Pos(400,100);
    private Pos resFieldPos;
    private Pos posStart,posEnd;
    private Pos geometryBtnPos,calcBtnPos,viewBtnPos;
    private Pos startBtnPos;

    Robot robot ;
    Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();

    public WINAPIController(String windowName, WinDef.HWND hwnd){
        this.windowName= windowName;

        if(hwnd==null) {
            hwnd = User32.INSTANCE.FindWindow(null, "MMANA-GAL basic");
            this.hwnd= hwnd;
        }

        char []buf=new char[96];

        WinUser.WINDOWPLACEMENT windowplacement = new WinUser.WINDOWPLACEMENT();
        User32.INSTANCE.GetWindowPlacement(hwnd,windowplacement);
        int length = User32.INSTANCE.GetWindowTextLength(hwnd);
        User32.INSTANCE.GetWindowText(hwnd,buf,length+1);
        //windowName = buf.toString();
      //  System.out.println(windowName);

        System.out.println("x1:"+windowplacement.rcNormalPosition.left+" y1:"+ windowplacement.rcNormalPosition.top);
        System.out.println("x2:"+windowplacement.rcNormalPosition.right+" y2:"+ windowplacement.rcNormalPosition.bottom);
        User32.INSTANCE.SetForegroundWindow(hwnd);
        User32.INSTANCE.ShowWindow(hwnd,9);
        int x = windowplacement.rcNormalPosition.left;
        int y = windowplacement.rcNormalPosition.top;
        posStart = new Pos(x,y);

        x = windowplacement.rcNormalPosition.right;
        y = windowplacement.rcNormalPosition.bottom;
        posEnd = new Pos(x,y);
        width = posEnd.x - posStart.x;
        height= posEnd.y - posStart.y;

        geometryBtnPos= new Pos(0,0);
        viewBtnPos = new Pos(0,0);
        calcBtnPos=new Pos(0,0);
        startBtnPos=new Pos(90,0);
        resFieldPos= new Pos(width-40,height*2/3);

        //int width = windowplacement.rcNormalPosition.right-windowplacement.rcNormalPosition.left;
        //int height=windowplacement.rcNormalPosition.bottom - windowplacement.rcNormalPosition.top;



    }
    public void makeResearch(){

      //  User32.INSTANCE.SetForegroundWindow(hwnd);
       // User32.INSTANCE.ShowWindow(hwnd,9);
        float cof=1f;
        if(isFullScreen(hwnd)){
            cof=1.2f;
        }

        geometryBtnPos.x=Math.round(2+MIN_BTN_GEOMETRY_W*cof/2.f);
        viewBtnPos.x= Math.round((geometryBtnPos.x*1.5f)+MIN_BTN_VIEW_W*cof/2.f);
        calcBtnPos.x= Math.round(viewBtnPos.x*1.5f+(MIN_BTN_CALC_W*cof/2.f));
        startBtnPos.x =90+ BTN_START_W/2;
        geometryBtnPos.y=80+(24-8);
        calcBtnPos.y = viewBtnPos.y=geometryBtnPos.y;
        startBtnPos.y= height -18;
        User32.INSTANCE.SetCursorPos(posStart.x+calcBtnPos.x,posStart.y+calcBtnPos.y);
        click(FILE_BTN_POS);
        click(REOPEN_BTN_POS);
        click(LAST_FILE_POS);
        click(calcBtnPos);
        click(startBtnPos);
        String res = getResults();
        System.out.println(res);
        Pattern pattern =  Pattern.compile("\\d.+\\D ");
        //[^ |c|j|\+|\-]
        Matcher matcher = pattern.matcher(res);

        // check all occurance
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end() + " ");
            res = matcher.group();

            System.out.println("group: "+matcher.group());
        }
        System.out.println("rsult"+res);
        res = res.replace("j","    \t ");
        res = res.replace("1c","");
        res = res.replace("+","");
        res = res.replace("-    \t ","    \t -");
        System.out.println(res);
        String[]resArr = res.split("    \t ");
        for (String str:
            resArr ) {
            System.out.println(str);
        }
        float r = Float.parseFloat(resArr[5]);
        float j = Float.parseFloat(resArr[6]);
        System.out.printf("Resistance:%f\tj:%f",r,j);
        startAgain();
    }
    private void startAgain(){
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_N);

    }
    private String getResults(){
        String res ="";
        StringSelection stringSelection = new StringSelection("");
        cp.setContents(stringSelection,null);

        try {


            if(robot==null){
                robot=new Robot();
            }

            Thread.sleep(9000);
            while (res==""||res==null){

                resFieldPos.y=height/2;

                robot.mouseMove(posStart.x+resFieldPos.x,posStart.y+resFieldPos.y);
                Thread.sleep(5000);
                robot.mousePress(LEFT_CLICK);
                resFieldPos.y = 0;
                robot.mouseMove(posStart.x+resFieldPos.x,posStart.y+resFieldPos.y);
                robot.mouseRelease(LEFT_CLICK);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_C);
                Thread.sleep(1000);
                Transferable contents = cp.getContents(null);

                boolean isTextInside = contents!=null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                if(isTextInside){
                    try {
                        res = (String)contents.getTransferData(DataFlavor.stringFlavor);
                        System.out.println(res);

                        cp.setContents(stringSelection,null);
                    }
                    catch (UnsupportedFlavorException e){e.printStackTrace();}
                    catch (IOException e){
                    e.printStackTrace();
                    }
                }
            }
        }
        catch (AWTException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return res;
    }
    private void click(Pos position){
        try {


        if(robot==null){
            robot=new Robot();
        }

        robot.mouseMove(posStart.x+position.x,posStart.y+position.y);
        robot.mousePress(LEFT_CLICK);
        robot.mouseRelease(LEFT_CLICK);
        }
        catch (AWTException e){}
    }
    private   boolean isFullScreen(  WinDef.HWND foregroundWindow )
    {
        WinDef.RECT foregroundRectangle = new WinDef.RECT();
        WinDef.RECT desktopWindowRectangle = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(foregroundWindow, foregroundRectangle);
        WinDef.HWND desktopWindow = User32.INSTANCE.GetDesktopWindow();
        User32.INSTANCE.GetWindowRect(desktopWindow, desktopWindowRectangle);
        return foregroundRectangle.toString().equals(desktopWindowRectangle.toString());
    }
}
