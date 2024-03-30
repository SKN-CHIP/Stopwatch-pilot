package com.example.stoperchip;

import java.io.Serializable;

/**
 * addresses:
 * 1 - timer
 * 2 - diodes
 * 3 - flags
 * else - error
 *
 *
 * case timer:
 * data 1:
 * 2 digits, minutes value, 0-99
 *
 * data 2:
 * 2 digits, seconds value, 0-59
 *
 *
 * case diodes:
 * data 1:
 * 1 digit, diode address, 1-8
 *
 * data 2:
 * 1 digit, diode color, 0 - 6
 * 0 - off; 1 - white; 2 - red; 3 - orange; 4 - green; 5 - blue; 6 - purple
 *
 *
 * case flags:
 * data 1:
 * 1 digit, flag type, 1 - auto led mode
 *
 * data 2:
 * 1 digit; 0 reset, 1 set
 *
 *
 */

public class SendDataClass implements Serializable {
    private int address, data1, data2, checksum;
    private String send_data;

    public SendDataClass() {
        address = 0;
        data1 = 0;
        data2 = 0;
        checksum = 0;
        send_data = null;
    }

    private void calculate_checksum(){
        checksum = address + data1 + data2;
    }

    private void create_sending_object(){
        send_data = "" + address + data1 + data2 + checksum;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public int getData1() {
        return data1;
    }

    public void setDiodeAddress(int diode_address){
        address = 2;
        data1 = diode_address;
    }

    public void resetData(){
        address = 0;
        data1 = 0;
        data2 = 0;
    }

}
