package com.qinsong.qspay;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by song
 * Contact github.com/tohodog
 * Date 2019/4/24
 * 权限工具
 */
public class QSPermissionUtil {

    public final static String[] SDCARD = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final static String[] CAMERA_AUDIO = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    public final static String[] CAMERA = new String[]{Manifest.permission.CAMERA};

    public final static String[] PHONE = new String[]{Manifest.permission.READ_PHONE_STATE};

    public final static String[] LOCATION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public final static String[] BLE = LOCATION;

    //安卓10beta没有SDCARD权限了,需要申请对应, 后面版本又改回原权限了,不过只能访问对应文件夹
//    public final static String[] SDCARD_Q = new String[]{
//            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.READ_MEDIA_AUDIO};

    /**
     * 动态申请权限
     */
    public static void requestPermission(Activity activity, PermissionListener permissionListener, String... permissions) {
        requestPermission(activity, 775, permissionListener, permissions);
    }

    public static void requestPermission(Activity activity, int requestCode, PermissionListener permissionListener, String... permissions) {
        if (hasPermission(activity, permissions)) {
            if (permissionListener != null)
                permissionListener.onPermissionSucceed(requestCode, Arrays.asList(permissions));
        } else {
            try {
                //利用Fragment申请权限,不用开发者处理onRequestPermissionsResult了
                PermissionFragment permissionFragment = new PermissionFragment();
                permissionFragment.setPermission(permissionListener, requestCode, permissions);
                activity.getFragmentManager().beginTransaction().add(Window.ID_ANDROID_CONTENT, permissionFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (permissionListener != null)
                    permissionListener.onPermissionFailed(requestCode, Arrays.asList(permissions));
            }
        }
    }

    /**
     * 动态申请权限,被拒一次后申请前增加提示框
     */
    public static void requestRationalePermission(Activity activity, PermissionListener permissionListener, String... permissions) {
        requestRationalePermission(activity, 775, permissionListener, permissions);
    }

    public static void requestRationalePermission(final Activity activity, final int requestCode, final PermissionListener permissionListener, final String... permissions) {
        if (hasPermission(activity, permissions)) {
            if (permissionListener != null)
                permissionListener.onPermissionSucceed(requestCode, Arrays.asList(permissions));
        } else {
            if (isRationalePermission(activity, permissions)) {
                new AlertDialog.Builder(activity)
                        .setTitle("权限已被拒绝")
                        .setMessage("您已经拒绝过我们申请授权，请您同意授权，否则功能无法正常使用！")
                        .setCancelable(false)
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission(activity, requestCode, permissionListener, permissions);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (permissionListener != null)
                                    permissionListener.onPermissionFailed(requestCode, Arrays.asList(permissions));
                            }
                        })
                        .show();
            } else {
                requestPermission(activity, requestCode, permissionListener, permissions);
            }
        }
    }

    /**
     * 显示手动设置权限对话框
     */
    public static void showSettingDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("权限申请失败")
                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), (String) null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, 755);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 是否有权限
     */
    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            for (String permission : permissions) {
                boolean hasPermission = context.checkPermission
                        (permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
                if (!hasPermission) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 是否被拒绝了一次了
     */
    public static boolean isRationalePermission(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        } else {
            for (String permission : permissions) {
                //有一个被拒了一次返回
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 是否永远拒绝了
     * 需要在被拒绝后调用
     */
    public static boolean isAlwaysDeniedPermission(Activity activity, String... permissions) {
        return isAlwaysDeniedPermission(activity, Arrays.asList(permissions));
    }

    /**
     * 是否永远拒绝了
     * 需要在被拒绝后调用
     */
    public static boolean isAlwaysDeniedPermission(Activity activity, List<String> permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        } else {
            for (String permission : permissions) {
                //有一个被永远拒了返回
                //shouldShowRequestPermissionRationale只有拒绝了之后返回true
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    return true;
                }
            }
            return false;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static class PermissionFragment extends Fragment {

        private PermissionListener permissionListener;
        private int requestCode;
        private String[] permissions;


        public void setPermission(PermissionListener permissionListener, int requestCode, String... permissions) {
            this.permissionListener = permissionListener;
            this.requestCode = requestCode;
            this.permissions = permissions;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return new View(container.getContext());
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            try {
                requestPermissions(permissions, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //申请权限回调
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (permissionListener != null) {

                List<String> grantedList = new ArrayList<>();
                List<String> deniedList = new ArrayList<>();

                for (int i = 0; i < permissions.length; ++i) {
                    if (grantResults[i] == 0) {
                        grantedList.add(permissions[i]);
                    } else {
                        deniedList.add(permissions[i]);
                    }
                }

                if (deniedList.isEmpty()) {
                    permissionListener.onPermissionSucceed(requestCode, grantedList);
                } else {
                    permissionListener.onPermissionFailed(requestCode, deniedList);
                }
            }
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

    }

    public interface PermissionListener {
        void onPermissionSucceed(int requestCode, List<String> grantedList);

        void onPermissionFailed(int requestCode, List<String> deniedList);
    }
}
