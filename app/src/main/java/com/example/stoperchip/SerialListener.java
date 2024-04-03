package com.example.stoperchip;

import java.util.ArrayDeque;
interface SerialListener {
    void onSerialConnect      ();
    void onSerialConnectError (Exception e);
    void onSerialIoError      (Exception e);
}