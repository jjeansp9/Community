package com.jspstudio.community.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityOptionsCompat
import java.io.Serializable
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Util {

    fun getFormattedValue(doubleValue: Double, pattern: String): String { return DecimalFormat(pattern).format(doubleValue) } // 숫자 형식
    fun rateOfReturn(beforeValue: Double, afterValue: Double): Double { return ((afterValue - beforeValue) / beforeValue) * 100 } // 수익률 구하기
    fun calculateFinalPrice(initialAmount: Double, returnRate: Double): Double { return initialAmount * (1 + returnRate / 100) } // 평가금액 구하기

    fun formattedDecimal(value : Double, decimalNum: String) : String { return decimalNum.format(value) }

    /**
     * dp값 or px값 가져오기
     */
    fun fromPxToDp(px: Float): Float { return (px / Resources.getSystem().displayMetrics.density) }
    fun fromDpToPx(dp: Float): Float { return (dp * Resources.getSystem().displayMetrics.density) }
    fun fromDpToPx(dp: Int): Float { return (dp * Resources.getSystem().displayMetrics.density) }

    fun replaceStr(str: String) : String {
        return str.replace(",", "").replace("%", "").replace("∞", "")
    }

    fun showKeyboard(mContext: Context, view: View) {
        view.requestFocus()
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 파라미터로 받은 editText의 개수만큼 focus 얻어오고 키보드를 내리는 메소드
     * */
    fun hideKeyboard(mContext: Context, focusList: Array<View?>) {
        val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
        for (view in focusList) {
            view?.clearFocus()
            view?.let {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    fun hideKeyboard(mContext: Context, focus: View?) {
        focus?.clearFocus()
        val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
        focus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    /**
     * bundle serializable extra
     */
    fun <T : Serializable?> getSerializableExtra(
        intent: Intent,
        key: String?,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            clazz.cast(intent.getSerializableExtra(key, clazz))
        } else {
            val temp = intent.getSerializableExtra(key)
            if (clazz.isInstance(temp)) {
                clazz.cast(temp)
            } else null
        }
    }

    /**
     * intent parcelableExtra extra
     */
    fun <T : Parcelable?> getParcelableExtra(intent: Intent, key: String?, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            clazz.cast(intent.getParcelableExtra(key, clazz))
        } else {
            val temp = intent.getParcelableExtra<Parcelable>(key)
            if (clazz.isInstance(temp)) {
                clazz.cast(temp)
            } else null
        }
    }

    fun launcher(activity: Activity, intent: Intent, enter: Int, exit: Int, resultLauncher: ActivityResultLauncher<Intent>) : Unit {
        val animationBundle = ActivityOptionsCompat.makeCustomAnimation(
            activity,
            enter,  // Enter animation
            exit  // Exit animation
        )
        return resultLauncher.launch(intent, animationBundle)
    }

    fun setFinishActivityAnim(activity: Activity, enter: Int, exit: Int) {
        activity.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            val animationBundle = ActivityOptionsCompat.makeCustomAnimation(
//                activity,
//                enter,  // Enter animation
//                exit  // Exit animation
//            ).toBundle()
            activity?.overridePendingTransition(enter, exit)
        } else {

            activity?.overridePendingTransition(enter, exit)
        }
    }

    /**
     * 날짜 포맷
     */
    fun formatDate(inputDate: String?, inputFormatStr: String?, outputFormatStr: String?): String? {
        val inputDateFormat = SimpleDateFormat(inputFormatStr, Locale.KOREA)
        val outputFormat = SimpleDateFormat(outputFormatStr, Locale.KOREA)
        return try {
            val date = if (inputDate != null) inputDateFormat.parse(inputDate)
            else inputDateFormat.parse("")

            var formattedDate: String? = ""
            if (date != null) formattedDate = outputFormat.format(date)
            formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 실행중인 앱의 가로길이가 600이상이면 true
     * */
    fun isScreenWidthGreaterThan600dp(context: Context): Boolean {
        val configuration = context.resources.configuration
        val screenWidthDp = configuration.screenWidthDp
        return screenWidthDp >= 600
    }

    /**
     * 숫자 외 모든 문자열 제거
     * */
    fun extractNumbers(input: String): String {
        val regex = Regex("[0-9]+") // 숫자 패턴을 정규식으로 정의
        val matches = regex.findAll(input) // 정규식과 매치되는 숫자를 찾음
        val result = StringBuilder() // 결과를 저장할 StringBuilder 생성

        for (match in matches) result.append(match.value) // 매치된 숫자를 결과에 추가

        return result.toString()
    }

    fun getStr(s : String) : String {
        if (s == null) return ""
        return s.ifEmpty { "" }
    }
}