package com.chainreaction.mercadopagoclient.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.math.BigInteger
import java.security.SecureRandom
import java.util.*
import java.util.regex.Pattern

class Fingerprint(@field:Transient private val mContext: Context) {

    @Transient
    private val mLocationManager: LocationManager

    @Transient
    private val mLocationListener: LocationListener
    private var vendorIds: ArrayList<VendorId>
    var model: String
    var os: String
    var systemVersion: String
    var resolution: String
    var ram: Long?
    var diskSpace: Long
    var freeDiskSpace: Long
    var vendorSpecificAttributes: VendorSpecificAttributes
    var location: Location?

    fun getVendorIdsInternal(): ArrayList<VendorId> {
        val vendorIds = ArrayList<VendorId>()

        // android_id
        val androidId = Settings.Secure.getString(
            mContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        vendorIds.add(VendorId("android_id", androidId))

        // serial
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            if (!TextUtils.isEmpty(Build.SERIAL) && "unknown" != Build.SERIAL) {
                vendorIds.add(VendorId("serial", Build.SERIAL))
            }
        }

        // SecureRandom
        val randomId = SecureRandomId.getValue(mContext)
        if (!TextUtils.isEmpty(randomId)) {
            vendorIds.add(VendorId("fsuuid", randomId))
        }
        return vendorIds
    }

    private object SecureRandomId {
        private const val FILENAME_FSUUID = "fsuuid"
        private var mFSUUID: String? = null

        @Throws(IOException::class)
        private fun readFile(file: File): String {
            val f = RandomAccessFile(file, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            f.close()
            return String(bytes)
        }

        @Throws(IOException::class)
        private fun writeFile(file: File) {
            val out = FileOutputStream(file)
            val random = SecureRandom()
            val id = BigInteger(64, random).toString(16)
            out.write(id.toByteArray())
            out.close()
        }

        @Synchronized
        fun getValue(context: Context): String? {
            if (mFSUUID == null) {
                val path =
                    Environment.getExternalStorageDirectory().absolutePath
                val file = File(
                    path + "/" + context.packageName,
                    FILENAME_FSUUID
                )
                try {
                    if (!file.exists()) {
                        val dirs = file.parentFile.mkdirs()
                        if (dirs) {
                            writeFile(file)
                        }
                    }
                    mFSUUID = readFile(file)
                } catch (ignored: Exception) {
                }
            }
            return mFSUUID
        }
    }

    fun getSystemVersionInternal(): String {
        return Build.VERSION.RELEASE
    }

    private fun getResolutionInternal(): String {
        val manager =
            mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        return display.width.toString() + "x" + display.height
    }

    private fun getRamInternal(): Long? {
        var ram: Long? = null
        try {
            val reader = RandomAccessFile("/proc/meminfo", "r")
            val load = reader.readLine()
            val pattern = Pattern.compile("(\\d+)")
            val matcher = pattern.matcher(load)
            if (matcher.find()) {
                ram = java.lang.Long.valueOf(matcher.group(0))
            }
        } catch (ex: Exception) {
        }
        return ram
    }

    fun getDiskSpaceInternal(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.blockSize.toLong() * statFs.blockCount.toLong() / 1048576
    }

    fun getFreeDiskSpaceInternal(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.blockSize.toLong() * statFs.availableBlocks / 1048576
    }

    fun getVendorSpecificAttributesInternal(): VendorSpecificAttributes {
        return VendorSpecificAttributes()
    }

    fun getLocationInternal(): Location? {
        var location = Location()
        try {
            if (mLocationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_COARSE
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED

                ) else return null

                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0, 0f, mLocationListener, Looper.getMainLooper()
                )
            }

            // we've a few options here to get the location while we pull a new one.
            // 1. get it from the local storage.
            // 2. get it from provider's cache on the case we don't have any stored.
            val cached =
                mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location.hasLocation()) {
                return location
            } else if (cached != null) {
                location = Location(
                    cached.latitude,
                    cached.longitude
                )
                setLocationInternal(location)
                return location
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun setLocationInternal(location: Location) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val editor = prefs.edit()
        editor.putString(
            SHARED_PREFS_FINGERPRINT_LOCATION,
            location.toString()
        )
        editor.commit()
    }

    inner class VendorId(val name: String, val value: String?)

    inner class VendorSpecificAttributes {
        var featureCamera: Boolean
        var featureFlash: Boolean
        var featureFrontCamera: Boolean
        var product: String
        var device: String
        var platform: String?
        var brand: String
        var featureAccelerometer: Boolean
        var featureBluetooth: Boolean
        var featureCompass: Boolean
        var featureGps: Boolean
        var featureGyroscope: Boolean
        var featureMicrophone: Boolean
        var featureNfc: Boolean
        var featureTelephony: Boolean
        var featureTouchScreen: Boolean
        var manufacturer: String
        var screenDensity: Float

        init {
            featureCamera = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
            featureFlash = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            featureFrontCamera = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
            product = Build.PRODUCT
            device = Build.DEVICE
            platform = getSystemProperty("ro.product.cpu.abi")
            brand = Build.BRAND
            featureAccelerometer = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
            featureBluetooth = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
            featureCompass = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
            featureGps = mContext.packageManager
                .hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
            featureGyroscope = mContext.packageManager
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)
            featureMicrophone = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
            featureNfc = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
            featureTelephony = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
            featureTouchScreen = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)
            manufacturer = Build.MANUFACTURER
            screenDensity = mContext.resources.displayMetrics.density
        }
    }

    inner class Location {
        private var location: JSONObject? = null

        constructor() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
            try {
                location = JSONObject(
                    prefs.getString(
                        SHARED_PREFS_FINGERPRINT_LOCATION,
                        "{}"
                    )
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        constructor(latitude: Double, longitude: Double) {
            location = JSONObject()
            try {
                location!!.put(
                    LOCATION_TIMESTAMP,
                    System.currentTimeMillis() / 1000L
                )
                location!!.put(
                    LOCATION_LATITUDE,
                    latitude
                )
                location!!.put(
                    LOCATION_LONGITUDE,
                    longitude
                )
            } catch (ignored: JSONException) {
            }
        }

        override fun toString(): String {
            val gson = Gson()
            try {
                return gson.toJson(this)
            } catch (ignored: Exception) {
            }
            return ""
        }

        fun hasLocation(): Boolean {
            return latitude != 0.0 && latitude != 0.0
        }

        val timestamp: Long
            get() {
                if (location != null) {
                    try {
                        return location!!.getLong(LOCATION_TIMESTAMP)
                    } catch (ignored: JSONException) {
                    }
                }
                return System.currentTimeMillis() / 1000L
            }

        val latitude: Double
            get() {
                if (location != null) {
                    try {
                        return location!!.getDouble(LOCATION_LATITUDE)
                    } catch (ignored: JSONException) {
                    }
                }
                return 0.0
            }

        val longitude: Double
            get() {
                if (location != null) {
                    try {
                        return location!!.getDouble(LOCATION_LONGITUDE)
                    } catch (e: JSONException) {
                    }
                }
                return 0.0
            }

            private val LOCATION_TIMESTAMP = "timestamp"
            private val LOCATION_LATITUDE = "latitude"
            private val LOCATION_LONGITUDE = "longitude"
    }

    private inner class FingerprintLocationListener : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            setLocationInternal(
                Location(
                    location.latitude,
                    location.longitude
                )
            )
            mLocationManager.removeUpdates(mLocationListener)
        }

        override fun onStatusChanged(
            s: String,
            i: Int,
            bundle: Bundle
        ) {
        }

        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    companion object {
        private const val TAG = "Fingerprint"
        private const val SHARED_PREFS_FINGERPRINT_LOCATION = "FINGERPRINT_LOCATION"
        private fun getSystemProperty(propName: String): String? {
            val line: String
            var input: BufferedReader? = null
            try {
                val p =
                    Runtime.getRuntime().exec("getprop $propName")
                input = BufferedReader(InputStreamReader(p.inputStream), 1024)
                line = input.readLine()
                input.close()
            } catch (ex: IOException) {
                Log.e(
                    TAG,
                    "Unable to read sysprop $propName",
                    ex
                )
                return null
            } finally {
                if (input != null) {
                    try {
                        input.close()
                    } catch (e: IOException) {
                        Log.e(
                            TAG,
                            "Exception while closing InputStream",
                            e
                        )
                    }
                }
            }
            return line
        }
    }

    init {
        mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = FingerprintLocationListener()
        vendorIds = getVendorIdsInternal()
        model = Build.MODEL
        os = "android"
        systemVersion = Build.VERSION.RELEASE
        resolution = getResolutionInternal()
        ram = getRamInternal()
        diskSpace = getDiskSpaceInternal()
        freeDiskSpace = getFreeDiskSpaceInternal()
        vendorSpecificAttributes = getVendorSpecificAttributesInternal()
        location = getLocationInternal()
    }
}