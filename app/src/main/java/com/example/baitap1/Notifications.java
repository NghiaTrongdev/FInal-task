package com.example.baitap1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class Notifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction()))
        {
            showAlertDialog(context);
        }
    }
    private void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Battery Low")
                .setMessage("Your battery is running low.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng AlertDialog khi người dùng nhấn OK
                        dialog.dismiss();
                    }
                })
                .setCancelable(false); // Đảm bảo dialog không thể bị huỷ bằng cách nhấn ra ngoài

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
