package com.sdu.runningsdu.Map;


import android.content.ContextWrapper;

import com.unity3d.player.UnityPlayer;

public class MyUnityPlayer extends UnityPlayer {
    public MyUnityPlayer(ContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected void kill() {}
}
