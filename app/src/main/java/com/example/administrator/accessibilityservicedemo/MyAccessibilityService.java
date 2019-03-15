package com.example.administrator.accessibilityservicedemo;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.administrator.accessibilityservicedemo.Other.SetOtherHB;
import com.example.administrator.accessibilityservicedemo.tool.UI;
import com.example.administrator.accessibilityservicedemo.tool.UIConstants;

import java.util.List;


/**
 * Created by Administrator on 2018/2/13 0013.
 */

public class MyAccessibilityService extends AccessibilityService {


    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

            if (SetOtherHB.zongkg != true) return;//总开关
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    List<CharSequence> texts = event.getText();
                    if (!texts.isEmpty()) {
                        for (CharSequence text : texts) {
                            String content = text.toString();
                            if (content.contains("[微信红包]")) {
//                                if (SetOtherHB.hulueci && SetOtherHB.huluecitext != "") {
//                                    if (content.contains("[微信红包]" + SetOtherHB.huluecitext)) return;
//                                }
                                SetOtherHB.isAutoQHB = true;
                                wakeUpAndUnlock(this);
                                //模拟打开通知栏消息
                                openNotify(event);

                            }
                        }
                    }
                    break;

                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    openHongBao(event);

                    break;

                //表示用户开始触摸屏幕的事件。
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                    if ("android.widget.ListView".equals(event.getClassName())) {
                        openPacket();
                    }
                    break;


            }
        }



    @Override
    public void onInterrupt() {

    }

    /**
     * 测试用的Log
     */
    private void printEventLog(AccessibilityEvent event) {
        Log.i(TAG, "-------------------------------------------------------------");
        int eventType = event.getEventType(); //事件类型
        Log.i(TAG, "PackageName:" + event.getPackageName() + ""); // 响应事件的包名
        Log.i(TAG, "Source Class:" + event.getClassName() + ""); // 事件源的类名
        Log.i(TAG, "Description:" + event.getContentDescription() + ""); // 事件源描述
        Log.i(TAG, "Event Type(int):" + eventType + "");

        switch (eventType) {

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                Log.i(TAG, "event type:TYPE_NOTIFICATION_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://窗体状态改变
                Log.i(TAG, "event type:TYPE_WINDOW_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED://View获取到焦点
                Log.i(TAG, "event type:TYPE_VIEW_ACCESSIBILITY_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                Log.i(TAG, "event type:TYPE_VIEW_ACCESSIBILITY_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                Log.i(TAG, "event type:TYPE_GESTURE_DETECTION_END");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.i(TAG, "event type:TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "event type:TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.i(TAG, "event type:TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.i(TAG, "event type:TYPE_VIEW_SCROLLED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                Log.i(TAG, "event type:TYPE_VIEW_TEXT_SELECTION_CHANGED");
                break;
            default:
                Log.i(TAG, "no listen event");
        }

        for (CharSequence txt : event.getText()) {
            Log.i(TAG, "text:" + txt);
        }

        Log.i(TAG, "-------------------------------------------------------------");
    }

    /**
     * 打开通知栏消息
     */
    private void openNotify(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        // 将微信的通知栏消息打开
        // 获取Notification对象
        Notification notification = (Notification) event.getParcelableData();
        // 调用其中的PendingIntent，打开微信界面
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开微信后，判断是什么界面，并做相应的动作
     */
    private void openHongBao(AccessibilityEvent event) {
        Log.d("sjl", "opemHongBao----" + event.getClassName());
        if (UI.LUCKY_MONEY_RECEIVE_UI.equals(event.getClassName())) {
//             拆红包界面
//            Log.d("sjl", "拆红包界面");
            try {
                Thread.sleep(Long.parseLong(SetOtherHB.delayTime) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getPacket();
        } else if (UI.LUCKY_MONEY_DETAIL_UI.equals(event.getClassName())) {
//             拆完红包后，看红包金额的界面
//            Log.d("sjl", "红包金额界面");
//            if (SetOtherHB.isAutoQHB) {
                try {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    if (SetOtherHB.hufu && SetOtherHB.hufutext!=null) {
                        inputHFC(SetOtherHB.hufutext);//输入回复词
                        send();//发送
                    }
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Thread.sleep(200);
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
//                    SetOtherHB.isAutoQHB = false;
//                    wakeDownAndUnlock(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
//             聊天界面
//            Log.d("sjl", "聊天界面");
            openPacket();
        }
    }

    /**
     * 在聊天界面中点红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        // 找到领取红包的点击事件
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        // 最新的红包领起
        for (int i = list.size() - 1; i >= 0; i--) {
            // 通过调试可知 [领取红包] 是 text，本身不可被点击，用 getParent()获取可被点击的对象
            AccessibilityNodeInfo parent = list.get(i).getParent();
            // 谷歌重写了 toString() 方法，不能用它获取 ClassName@hashCode 串
            if (parent != null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break; // 只领最新的一个红包
            }
        }
    }

    /**
     * 拆红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        nodeInfo = findNodeInfoByClassName(nodeInfo, UI.BUTTON);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }


    /**
     * 点亮屏幕并解锁（不能有解锁密码）
     */
    public void wakeUpAndUnlock(Context context) {
        /**
         * 唤醒手机屏幕并解锁
         */

            // 获取电源管理器对象
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean screenOn = pm.isScreenOn();
            if (!screenOn) {
                // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
                PowerManager.WakeLock wl = pm.newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire(10000); // 点亮屏幕
                wl.release(); // 释放
            }
            // 屏幕解锁
            KeyguardManager keyguardManager = (KeyguardManager) context
                    .getSystemService(KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
            // 屏幕锁定
            keyguardLock.reenableKeyguard();
            keyguardLock.disableKeyguard(); // 解锁

    }


    /**
     * 通过 组件名称查找
     */
    public static AccessibilityNodeInfo findNodeInfoByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (null == nodeInfo || TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);

            if (null != node && className.equals(node.getClassName())) {
                return node;
            }
        }
        return findNodeInfoByClassNameRecycle(nodeInfo, className);
    }

    public static AccessibilityNodeInfo findNodeInfoByContentDescribeAndClassName(AccessibilityNodeInfo nodeInfo, String contentDsc,
                                                                                  String className) {
        if (null == nodeInfo || TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if(null != node ){
            }
            if (null != node && className.equals(node.getClassName()) && contentDsc.equals(node.getContentDescription())) {
                return node;
            }
        }
        return findNodeInfoByClassNameRecycleAndContentDescription(nodeInfo, contentDsc, className);
    }
    private static AccessibilityNodeInfo findNodeInfoByClassNameRecycleAndContentDescription(AccessibilityNodeInfo parentNodeInfo,
                                                                                             String contentDsc, String className) {
        if (null == parentNodeInfo || TextUtils.isEmpty(className)) {
            return null;
        }
        if (className.equals(parentNodeInfo.getClassName()) && contentDsc.equals(parentNodeInfo.getContentDescription())) {
            return parentNodeInfo;
        }

        for (int i = 0; i < parentNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfoResult =
                    findNodeInfoByClassNameRecycleAndContentDescription(parentNodeInfo.getChild(i), contentDsc, className);
            if (null != nodeInfoResult) {
                return nodeInfoResult;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeInfoByClassName1(AccessibilityNodeInfo nodeInfo, String className) {

        if (null == nodeInfo || TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);

            if (null != node && className.equals(node.getClassName())) {
                return node;
            }
        }
        return findNodeInfoByClassNameRecycle(nodeInfo, className);
    }

    /**
     * 通过 组件名称查找---递归， 遍历树中的第一项
     */
    private static AccessibilityNodeInfo findNodeInfoByClassNameRecycle(AccessibilityNodeInfo parentNodeInfo, String className) {
        String mContentDescription="表情";
        if (null == parentNodeInfo || TextUtils.isEmpty(className)) {
            return null;
        }
        if (className.equals(parentNodeInfo.getClassName())) {
            return parentNodeInfo;
        }

        for (int i = 0; i < parentNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfoResult =
                    findNodeInfoByClassNameRecycle(parentNodeInfo.getChild(i), className);
            if (null != nodeInfoResult) {
                return nodeInfoResult;
            }
        }
        return null;
    }

    /**
     * 自动点击发送
     */
    public void send() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("发送");
        for (int i = list.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo parent = list.get(i);
            if (parent != null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
    }

    //自动回复内容
    private void inputHFC(String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo backImageView =findNodeInfoByContentDescribeAndClassName(nodeInfo, "表情", "android.widget.ImageButton");
        //找到当前获取焦点的view
        AccessibilityNodeInfo target = backImageView.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            Log.d(TAG, "inputText: null");
            return;
        }
//mContentDescription="聊天信息"
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }
}


