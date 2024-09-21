package lying.fengfeng.foodrecords.utils

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object EffectUtil {

    fun playNotification(context: Context) {
        val notificationUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, notificationUri).play()
    }
    fun playSoundEffect(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
    }

    fun playVibrationEffect(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        } else {
            VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
        }
        vibrator.vibrate(vibrationEffect)
    }
}