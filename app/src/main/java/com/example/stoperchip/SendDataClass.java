package com.example.stoperchip;

import java.io.Serializable;

/*
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

    public SendDataClass() {
        address = 0;
        data1 = 0;
        data2 = 0;
        checksum = 0;
    }

    private void calculate_checksum(){
        checksum = address + data1/10 + data1%10 + data2/10 + data2%10;
    }

    public String create_sending_object(){
        String s_data1;
        String s_data2;
        if(address == 1){
            s_data1 = String.valueOf(data1);
            s_data2 = String.valueOf(data2);
            if(data1 < 10)
                s_data1 = "0" + data1;
            if(data2 < 10)
                s_data2 = "0" + data2;
            calculate_checksum();
            if(checksum < 10)
                return address + s_data1 + s_data2 + "0" + checksum;
            else
                return address + s_data1 + s_data2 + checksum;
        }
        calculate_checksum();
        s_data1 = String.valueOf(data1);
        s_data2 = String.valueOf(data2);
        if(checksum < 10)
            return address + s_data1 + s_data2 + "0" + checksum;
        else
            return address + s_data1 + s_data2 + checksum;
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


}
