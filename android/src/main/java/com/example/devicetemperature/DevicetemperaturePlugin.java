package com.example.devicetemperature;

import androidx.annotation.NonNull;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** DeviceTemperaturePlugin */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class DevicetemperaturePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Activity activity;
  private double batteryTemperature = 0;
  int batterLevel;
  boolean check = false;
  private BroadcastReceiver broadcastreceiver;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "devicetemperature");
    channel.setMethodCallHandler(this);
  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    if(call.method.equals("getDeviceTemperature")) {
     // deviceSense = senseManage.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
      broadcastreceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, Intent intent) {
          batteryTemperature = (double)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0))/10;
        }
      };
      result.success(batteryTemperature);

      IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      activity.registerReceiver(broadcastreceiver, intentFilter);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    activity = activityPluginBinding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
