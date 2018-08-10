package br.com.oi.reactnative.module.datausage.helper;

import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

@TargetApi(Build.VERSION_CODES.M)
public class NetworkStatsHelper {

    NetworkStatsManager networkStatsManager;
    int packageUid;

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager, int packageUid) {
        this.networkStatsManager = networkStatsManager;
        this.packageUid = packageUid;
    }

    public Map<String, Long> getMobileSummaryForDevice(Context context, Date startDate, Date endDate) {
        NetworkStats.Bucket bucket;
        Map<String, Long> result = new HashMap();
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis());
        } catch (RemoteException e) {
            return result;
        }

        result.put("rx", bucket.getRxBytes());
        result.put("tx", bucket.getTxBytes());
        result.put("start", bucket.getStartTimeStamp());
        result.put("end", bucket.getEndTimeStamp());

        return result;
    }

    public long getAllRxBytesWifi() {
        return getAllRxBytesWifi(null, null);
    }

    public long getAllRxBytesWifi(Date startDate, Date endDate) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "",
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi() {
        return getAllTxBytesWifi(null, null);
    }

    public long getAllTxBytesWifi(Date startDate, Date endDate) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "",
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getPackageRxBytesMobile(Context context) {
        return getPackageRxBytesMobile(context, null, null);
    }

    public long getPackageRxBytesMobile(Context context, Date startDate, Date endDate) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis(), packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        networkStats.getNextBucket(bucket);
        long rx = bucket.getRxBytes();
        networkStats.close();
        return rx;
    }

    public long getPackageTxBytesMobile(Context context) {
        return getPackageTxBytesMobile(context, null, null);
    }

    public long getPackageTxBytesMobile(Context context, Date startDate, Date endDate) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis(), packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        long tx = bucket.getTxBytes();
        networkStats.close();
        return tx;
    }

    public long getPackageRxBytesWifi() {
        return getPackageRxBytesWifi(null, null);
    }

    public long getPackageRxBytesWifi(Date startDate, Date endDate) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "",
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis(), packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        long rx = bucket.getRxBytes();
        networkStats.close();
        return rx;
    }

    public long getPackageTxBytesWifi() {
        return getPackageTxBytesWifi(null, null);
    }

    public long getPackageTxBytesWifi(Date startDate, Date endDate) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "",
                    startDate != null ? startDate.getTime() : 0,
                    endDate != null ? endDate.getTime() : System.currentTimeMillis(), packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        long tx = bucket.getTxBytes();
        networkStats.close();
        return tx;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }

        return "";
    }
}
