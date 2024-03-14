package com.example.doodhbhandar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] smsobj = (Object[]) bundle.get("pdus");
        for (Object obj: smsobj){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);

            String mobNo = message.getDisplayOriginatingAddress();
            String msg = message.getDisplayMessageBody();
            Log.d("MsgDetails","MobNo:"+ mobNo +",Msg: "+msg);
        }

    }
}
