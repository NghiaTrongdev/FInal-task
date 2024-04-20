package com.example.baitap1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            // Hiển thị AlertDialog thông báo mất kết nối Internet
            showNetworkLostDialog(context);
        }
    }

    private void showNetworkLostDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Network Lost")
                .setMessage("You have lost internet connection.")
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
