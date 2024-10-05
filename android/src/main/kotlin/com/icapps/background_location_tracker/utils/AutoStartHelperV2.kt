package com.icapps.background_location_tracker.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings


inline fun <reified T : Enum<T>> String.toEnum(value: (T) -> String, def: T): T {
    return enumValues<T>().firstOrNull { value(it) == this } ?: def
}

object AutoStartHelperV2 {

    /**
     * Request AutoStart permission based on [Brand] type.
     * Note-> No permission required for [Brand.OTHER].
     */
    fun requestAutoStartPermission(context: Context): Boolean {
        when (Build.BRAND.uppercase().toEnum(Brand::name, Brand.OTHER)) {
            Brand.XIAOMI, Brand.REDMI -> xiaomiAutoStart(context)
            Brand.NOKIA -> nokiaAutoStart(context)
            Brand.LETV -> letvAutoStart(context)
            Brand.ASUS -> asusAutoStart(context)
            Brand.HONOR -> honorAutoStart(context)
            Brand.OPPO -> oppoAutoStart(context)
            Brand.VIVO -> vivoAutoStart(context)
            Brand.OTHER -> {
                return false
            }
        }
        return true
    }

    /**
     * Request AutoStart permission for [Brand.XIAOMI] and [Brand.REDMI].
     */
    private fun xiaomiAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.XIAOMI_MAIN)) {
            try {
                startAutoStartActivity(
                    context,
                    BrandPackage.XIAOMI_MAIN,
                    BrandPackage.XIAOMI_COMPONENT
                )

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())
            }
        } else {
            Logger.error("AutoStartHelperV2", "Xiaomi package not found")
        }
    }

    @Throws(Exception::class)
    private fun startAutoStartActivity(
        context: Context,
        packageName: String,
        componentName: String
    ) {
        val intentAutoStartPage = Intent().apply {
            component = ComponentName(packageName, componentName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intentAutoStartPage)
        } catch (e: Exception) {
            throw Exception("Activity not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.NOKIA].
     */
    private fun nokiaAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.NOKIA_MAIN)) {
            try {
                startAutoStartActivity(
                    context,
                    BrandPackage.NOKIA_MAIN,
                    BrandPackage.NOKIA_COMPONENT
                )

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())
            }
        } else {
            Logger.error("AutoStartHelperV2", "Nokia package not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.LETV].
     */
    private fun letvAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.LETV_MAIN)) {
            try {
                startAutoStartActivity(context, BrandPackage.LETV_MAIN, BrandPackage.LETV_COMPONENT)

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())
            }
        } else {
            Logger.error("AutoStartHelperV2", "Letv package not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.ASUS].
     */
    private fun asusAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.ASUS_MAIN)) {
            try {
                startAutoStartActivity(context, BrandPackage.ASUS_MAIN, BrandPackage.ASUS_COMPONENT)

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())
            }
        } else {
            Logger.error("AutoStartHelperV2", "Asus package not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.HONOR].
     */
    private fun honorAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.HONOR_MAIN)) {
            try {
                startAutoStartActivity(
                    context,
                    BrandPackage.HONOR_MAIN,
                    BrandPackage.HONOR_COMPONENT
                )

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())
            }
        } else {
            Logger.error("AutoStartHelperV2", "Honor package not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.OPPO].
     */
    private fun oppoAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.OPPO_MAIN) || isPackageExists(
                context,
                BrandPackage.OPPO_FALLBACK
            )
        ) {
            try {
                startAutoStartActivity(context, BrandPackage.OPPO_MAIN, BrandPackage.OPPO_COMPONENT)

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())

                try {
                    startAutoStartActivity(
                        context,
                        BrandPackage.OPPO_FALLBACK,
                        BrandPackage.OPPO_COMPONENT_FALLBACK
                    )

                } catch (ex: Exception) {

                    Logger.error("AutoStartHelperV2", ex.message.toString())

                    try {
                        startAutoStartActivity(
                            context,
                            BrandPackage.OPPO_MAIN,
                            BrandPackage.OPPO_COMPONENT_FALLBACK_A
                        )

                    } catch (exx: Exception) {
                        Logger.error("AutoStartHelperV2", exx.message.toString())
                    }
                }
            }
        } else {
            Logger.error("AutoStartHelperV2", "Oppo package not found")
        }
    }

    /**
     * Request AutoStart permission for [Brand.VIVO].
     */
    private fun vivoAutoStart(context: Context) {
        if (isPackageExists(context, BrandPackage.VIVO_MAIN) ||
            isPackageExists(context, BrandPackage.VIVO_FALLBACK) ||
            isPackageExists(context, BrandPackage.VIVO_MAIN1)
        ) {
            try {
                startAutoStartActivity(context, BrandPackage.VIVO_MAIN, BrandPackage.VIVO_COMPONENT)

            } catch (e: Exception) {
                Logger.error("AutoStartHelperV2", e.message.toString())

                try {
                    startAutoStartActivity(
                        context,
                        BrandPackage.VIVO_FALLBACK,
                        BrandPackage.VIVO_COMPONENT_FALLBACK
                    )

                } catch (ex: Exception) {
                    Logger.error("AutoStartHelperV2", ex.message.toString())

                    try {
                        startAutoStartActivity(
                            context,
                            BrandPackage.VIVO_MAIN,
                            BrandPackage.VIVO_COMPONENT_FALLBACK_A
                        )

                    } catch (exx: Exception) {
                        Logger.error("AutoStartHelperV2", exx.message.toString())

                        autoStartGeneric(context)
                    }
                }
            }
        } else {
            Logger.error("AutoStartHelperV2", "Vivo package not found")
        }
    }

    /**
     * Return true if requested package exist false otherwise.
     */
    private fun isPackageExists(context: Context, targetPackage: String): Boolean {
        val packages = context.packageManager.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName.equals(targetPackage)) return true
        }

        return false
    }
    private fun autoStartGeneric(context: Context) {
        try {
            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.setData(Uri.parse("package:" + context.packageName))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            Logger.error("AutoStartHelperV2",e.message.toString())
        }
    }

    /**
     * Type of brands for which we can request AutoStart permission.
     * Note-> No permission required for [Brand.OTHER].
     */
    private enum class Brand {
        REDMI,
        XIAOMI,
        NOKIA,
        LETV,
        ASUS,
        HONOR,
        OPPO,
        VIVO,
        OTHER
    }

    /**
     * All [Brand] packages using this we can request AutoStart permission.
     */
    private object BrandPackage {

        // Xiaomi
        val XIAOMI_MAIN = "com.miui.securitycenter"
        val XIAOMI_COMPONENT = "com.miui.permcenter.autostart.AutoStartManagementActivity"

        // Nokia
        val NOKIA_MAIN = "com.evenwell.powersaving.g3"
        val NOKIA_COMPONENT =
            "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"

        // Letv
        val LETV_MAIN = "com.letv.android.letvsafe"
        val LETV_COMPONENT = "com.letv.android.letvsafe.AutobootManageActivity"

        // ASUS ROG
        val ASUS_MAIN = "com.asus.mobilemanager"
        val ASUS_COMPONENT = "com.asus.mobilemanager.powersaver.PowerSaverSettings"

        // Honor
        val HONOR_MAIN = "com.huawei.systemmanager"
        val HONOR_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity"

        // Oppo
        val OPPO_MAIN = "com.coloros.safecenter"
        val OPPO_FALLBACK = "com.oppo.safe"
        val OPPO_COMPONENT =
            "com.coloros.safecenter.permission.startup.StartupAppListActivity"
        val OPPO_COMPONENT_FALLBACK =
            "com.oppo.safe.permission.startup.StartupAppListActivity"
        val OPPO_COMPONENT_FALLBACK_A =
            "com.coloros.safecenter.startupapp.StartupAppListActivity"

        // Vivo
        val VIVO_MAIN1 = "com.vivo.abe"
        val VIVO_MAIN = "com.iqoo.secure"
        val VIVO_FALLBACK = "com.vivo.permissionmanager"
        val VIVO_COMPONENT = "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
        val VIVO_COMPONENT_FALLBACK =
            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
        val VIVO_COMPONENT_FALLBACK_A = "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
    }
}