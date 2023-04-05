package com.shatyuka.killergram;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    public static final List<String> hookPackages = List.of(
            "org.telegram.messenger",
            "org.telegram.messenger.web",
            "org.telegram.messenger.beta",
            "nekox.messenger",
            "com.cool2645.nekolite",
            "org.telegram.plus",
            "com.iMe.android",
            "org.telegram.BifToGram",
            "ua.itaysonlab.messenger",
            "org.forkclient.messenger",
            "org.forkclient.messenger.beta",
            "org.aka.messenger",
            "ellipi.messenger",
            "org.nift4.catox",
            "it.owlgram.android",
            "tw.nekomimi.nekogram");

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (hookPackages.contains(lpparam.packageName)) {
            try {
                Class<?> messagesControllerClass = XposedHelpers.findClassIfExists("org.telegram.messenger.MessagesController", lpparam.classLoader);
                if (messagesControllerClass != null) {
                    XposedBridge.hookAllMethods(messagesControllerClass, "getSponsoredMessages", XC_MethodReplacement.returnConstant(null));
                    XposedBridge.hookAllMethods(messagesControllerClass, "isChatNoForwards", XC_MethodReplacement.returnConstant(false));
                    XposedBridge.log("Hooked all methods from " + messagesControllerClass.getName());
                }
                Class<?> chatUIActivityClass = XposedHelpers.findClassIfExists("org.telegram.ui.ChatActivity", lpparam.classLoader);
                if (chatUIActivityClass != null) {
                    XposedBridge.hookAllMethods(chatUIActivityClass, "addSponsoredMessages", XC_MethodReplacement.returnConstant(null));
                    XposedBridge.log("Hooked all methods from " + chatUIActivityClass.getName());
                }
                Class<?> sharedConfigClass = XposedHelpers.findClassIfExists("org.telegram.messenger.SharedConfig", lpparam.classLoader);
                if (sharedConfigClass != null) {
                    XposedBridge.hookAllMethods(sharedConfigClass, "getDevicePerformanceClass", XC_MethodReplacement.returnConstant(2));
                }
                Class<?> userConfigClass = XposedHelpers.findClassIfExists("org.telegram.messenger.UserConfig", lpparam.classLoader);
                if (userConfigClass != null) {
                    XposedBridge.hookAllMethods(userConfigClass, "getMaxAccountCount", XC_MethodReplacement.returnConstant(999));
                    XposedBridge.hookAllMethods(userConfigClass, "hasPremiumOnAccounts", XC_MethodReplacement.returnConstant(true));
                }
            } catch (Exception ex) {
                XposedBridge.log("Unexpected error" + ex.getMessage());
            }
        }
    }
}
