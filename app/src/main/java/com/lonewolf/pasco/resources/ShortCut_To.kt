package com.lonewolf.pasco.resources

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.lonewolf.pasco.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ShortCut_To {
    const val DATEWITHTIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATEFORMATDDMMYYYY = "dd/MM/yyyy"
    const val DATEFORMATDDMMYYYY2 = "dd-MM-yyyy"
    const val DATEFORMATYYYYMMDD = "yyyy-MM-dd"
    const val TIME = "hh:mm a"
    const val DATEWITHTIMEDDMMYYY = "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"
    val currentDatewithTime: String
        get() {
            val dateFormat = SimpleDateFormat(DATEWITHTIMEDDMMYYY, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.d("No keyboard", "No keyboard to drop")
        }
    }

    val currentDates: String
        get() {
            val dateFormat = SimpleDateFormat(DATEFORMATYYYYMMDD, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    val currentDateFormat2: String
        get() {
            val dateFormat = SimpleDateFormat(DATEFORMATDDMMYYYY, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    var getServices = arrayOf(
        "Select Service",
        "Android App",
        "Web App",
        "Mobile App",
        "Website Development",
        "Web App",
        "Technical Support"
    )

    fun decodeBase64(input: String?): Bitmap? {
        try {
            val decodedByte = Base64.decode(input, 0)
            return BitmapFactory.decodeByteArray(
                decodedByte, 0,
                decodedByte.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getTimeFromDate(str: String?): String {
        return if (str != null && !str.equals(
                "null",
                ignoreCase = true
            ) && str.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEWITHTIME, Locale.US)
            val sdf2 = SimpleDateFormat(TIME, Locale.US)
            try {
                val date = sdf1.parse(str)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        } else {
            " "
        }
    }

    val currentDay: String
        get() {
            val daysArray = arrayOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_WEEK]
            val mt = calendar[Calendar.MONTH]
            val yr = calendar[Calendar.YEAR]
            return yr.toString() + daysArray[day - 1] + mt
        }
    val currentMonthYear: String
        get() {
            val c = Calendar.getInstance()
            val currMonth = c[Calendar.MONTH] + 1
            val currYear = c[Calendar.YEAR]
            val curDay = c[Calendar.DAY_OF_MONTH]
            return "$currMonth/$currYear"
        }
    val currentDayMonthYear: String
        get() {
            val c = Calendar.getInstance()
            val currMonth = c[Calendar.MONTH]
            val currYear = c[Calendar.YEAR]
            val curDay = c[Calendar.DAY_OF_MONTH]
            return if (currMonth == 0) {
                "January $curDay, $currYear"
            } else if (currMonth == 1) {
                "February $curDay, $currYear"
            } else if (currMonth == 2) {
                "March $curDay, $currYear"
            } else if (currMonth == 3) {
                "April $curDay, $currYear"
            } else if (currMonth == 4) {
                "May $curDay, $currYear"
            } else if (currMonth == 5) {
                "June $curDay, $currYear"
            } else if (currMonth == 6) {
                "July $curDay, $currYear"
            } else if (currMonth == 7) {
                "August $curDay, $currYear"
            } else if (currMonth == 8) {
                "September $curDay, $currYear"
            } else if (currMonth == 9) {
                "October $curDay, $currYear"
            } else if (currMonth == 10) {
                "November $curDay, $currYear"
            } else if (currMonth == 11) {
                "December $curDay, $currYear"
            } else {
                ""
            }
        }

    fun getDateTimeForAPI(dateFormatted: String?): String {
        val apiDate = Calendar.getInstance()
        try {
            val dateFormat = SimpleDateFormat(DATEFORMATDDMMYYYY)
            apiDate.time = dateFormat.parse(dateFormatted)
            val corrTime = Calendar.getInstance()
            apiDate[Calendar.HOUR_OF_DAY] = corrTime[Calendar.HOUR_OF_DAY]
            apiDate[Calendar.MINUTE] = corrTime[Calendar.MINUTE]
            apiDate[Calendar.SECOND] = corrTime[Calendar.SECOND]
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //2014-03-15T21:04:43.162Z
        val dateFormat = SimpleDateFormat(DATEWITHTIME)
        return dateFormat.format(apiDate.time)
    }

    fun getDateForAPP(strDate: String?): String? {
        return if (strDate != null && !strDate.equals(
                "null",
                ignoreCase = true
            ) && strDate.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEWITHTIME)
            val sdf2 = SimpleDateFormat(DATEFORMATDDMMYYYY2)
            try {
                val date = sdf1.parse(strDate)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }

    fun getFormatDateAPI(str: String?): String? {
        return if (str != null && !str.equals(
                "null",
                ignoreCase = true
            ) && str.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEFORMATDDMMYYYY)
            val sdf2 = SimpleDateFormat(DATEFORMATYYYYMMDD)
            try {
                val date = sdf1.parse(str)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }

    fun sortData(list: ArrayList<HashMap<String, String>>, field: String?) {
        Collections.sort(list) { lhs: HashMap<String, String>, rhs: HashMap<String, String> ->
            lhs[field]!!
                .compareTo(rhs[field]!!)
        }
    }

    fun sortDataInvert(list: ArrayList<HashMap<String, String>>?, field: String?) {
        Collections.sort(list, { lhs, rhs ->
            rhs[field]!!.compareTo(
                lhs[field]!!
            )
        })
    }

    fun convertDate(date: String): String {
        val nDate = date.split("T".toRegex()).toTypedArray()
        val mDate = nDate[0]
        val oDate = mDate.split("-".toRegex()).toTypedArray()
        return oDate[2] + "/" + oDate[1] + "/" + oDate[0]
    }

    fun addDaysToDate(date: String, numDays: Int): String {
        val arrDate = date.split("/".toRegex()).toTypedArray()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = arrDate[0].toInt()
        cal[Calendar.MONTH] = arrDate[1].toInt() - 1
        cal[Calendar.YEAR] = arrDate[2].toInt()
        cal.add(Calendar.DAY_OF_MONTH, numDays)
        return sdf.format(cal.time)
    }

    fun getMonthYear(date: String): String {
        val c = Calendar.getInstance()
        val currMonth = date.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        val currYear = date.split("/".toRegex()).toTypedArray()[2].toInt()
        val curDay = date.split("/".toRegex()).toTypedArray()[0].toInt()
        return if (currMonth == 0) {
            "January, $currYear"
        } else if (currMonth == 1) {
            "February, $currYear"
        } else if (currMonth == 2) {
            "March, $currYear"
        } else if (currMonth == 3) {
            "April, $currYear"
        } else if (currMonth == 4) {
            "May, $currYear"
        } else if (currMonth == 5) {
            "June, $currYear"
        } else if (currMonth == 6) {
            "July, $currYear"
        } else if (currMonth == 7) {
            "August, $currYear"
        } else if (currMonth == 8) {
            "September, $currYear"
        } else if (currMonth == 9) {
            "October, $currYear"
        } else if (currMonth == 10) {
            "November, $currYear"
        } else if (currMonth == 11) {
            "December, $currYear"
        } else {
            ""
        }
    }

    fun getMonthYearShort(date: String): String {
        val c = Calendar.getInstance()
        val currMonth = date.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        val currYear = date.split("/".toRegex()).toTypedArray()[2].toInt()
        val curDay = date.split("/".toRegex()).toTypedArray()[0].toInt()
        return if (currMonth == 0) {
            "Jan. $currYear"
        } else if (currMonth == 1) {
            "February, $currYear"
        } else if (currMonth == 2) {
            "Mar. $currYear"
        } else if (currMonth == 3) {
            "Apr. $currYear"
        } else if (currMonth == 4) {
            "May. $currYear"
        } else if (currMonth == 5) {
            "Jun. $currYear"
        } else if (currMonth == 6) {
            "Jul. $currYear"
        } else if (currMonth == 7) {
            "Aug. $currYear"
        } else if (currMonth == 8) {
            "Sep. $currYear"
        } else if (currMonth == 9) {
            "Oct. $currYear"
        } else if (currMonth == 10) {
            "Nov. $currYear"
        } else if (currMonth == 11) {
            "Dec. $currYear"
        } else {
            ""
        }
    }

    fun getDayOFWeek(dDate: String): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = dDate.split("/".toRegex()).toTypedArray()[0].toInt()
        calendar[Calendar.MONTH] = dDate.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        calendar[Calendar.YEAR] = dDate.split("/".toRegex()).toTypedArray()[2].toInt()
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).uppercase(
            Locale.getDefault()
        )
    }

    fun getCompany(company: String?, limit: String): String {
        var newCompany = "Alpha"
        if (limit == "Charlie") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Alpha"
            }
        } else if (limit == "Delta") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Alpha"
            }
        } else if (limit == "Echo") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Alpha"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Alpha"
            }
        } else if (limit == "Foxtrot") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Foxtrot"
                "Foxtrot" -> newCompany = "Alpha"
            }
        } else if (limit == "Gulf") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Foxtrot"
                "Foxtrot" -> newCompany = "Gulf"
                "Gulf" -> newCompany = "Alpha"
            }
        }
        return newCompany
    }

    fun getAgeAlt(dob: String): Int {
        val arrYear = dob.split("/".toRegex()).toTypedArray()
        val currYear = currentDateFormat2.split("/".toRegex()).toTypedArray()
        var newYear = currYear[2].toInt() - arrYear[2].toInt()
        if (arrYear[1].toInt() > currYear[1].toInt()) {
            newYear = newYear - 1
        } else {
            if (arrYear[1].toInt() == currYear[1].toInt()) {
                if (arrYear[0].toInt() > currYear[0].toInt()) {
                    newYear = newYear - 1
                }
            }
        }
        return newYear
    }

    fun getAge(dobString: String?): Int {
        var date: Date? = null
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        try {
            date = sdf.parse(dobString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date == null) return 0
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.time = date
        val year = dob[Calendar.YEAR]
        val month = dob[Calendar.MONTH]
        val day = dob[Calendar.DAY_OF_MONTH]
        dob[year, month + 1] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age + 1
    }

    fun getDateAfterNumberOfDays(strDate: String?, num: Int): String {
        if (strDate == null || strDate.equals(
                "null",
                ignoreCase = true
            ) || strDate.trim { it <= ' ' }.length == 0
        ) {
            return ""
        }
        try {
            val sdf = SimpleDateFormat(DATEFORMATYYYYMMDD)
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(strDate)
            calendar.add(Calendar.DATE, -num)
            val resultdate = Date(calendar.timeInMillis)
            return sdf.format(resultdate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun addMonth(date: Date?, i: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, i)
        return cal.time
    }

    fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
        val dateFormat = SimpleDateFormat("dd/mm/yyyy")
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val start: Calendar = GregorianCalendar()
        start.time = createdConvertedDate
        val end: Calendar = GregorianCalendar()
        end.time = expireCovertedDate
        val diff = end.timeInMillis - start.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return dayCount.toInt()
    }

    fun getCountOfDay(createdDateString: String?, expireDateString: String?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }


        /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return "" + dayCount.toInt()
    }

    fun showAlert(activity: Activity?, title: String?, message: String?) {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setCancelable(false)
        alert.show()
    }

    val regions: List<String>
        get() {
            val listRegions: MutableList<String> = ArrayList()
            listRegions.add("Select your Current region")
            listRegions.add("Ahafo")
            listRegions.add("Ashanti")
            listRegions.add("Bono")
            listRegions.add("Bono East")
            listRegions.add("Central")
            listRegions.add("Eastern")
            listRegions.add("Greater Accra")
            listRegions.add("North East")
            listRegions.add("Northern")
            listRegions.add("Oti")
            listRegions.add("Savannah")
            listRegions.add("Upper East")
            listRegions.add("Upper West")
            listRegions.add("Volta")
            listRegions.add("Western")
            listRegions.add("Western North")
            return listRegions
        }
    val regionsHome: List<String>
        get() {
            val listRegions: MutableList<String> = ArrayList()
            listRegions.add("Select your Home region")
            listRegions.add("Ahafo")
            listRegions.add("Ashanti")
            listRegions.add("Bono")
            listRegions.add("Bono East")
            listRegions.add("Central")
            listRegions.add("Eastern")
            listRegions.add("Greater Accra")
            listRegions.add("North East")
            listRegions.add("Northern")
            listRegions.add("Oti")
            listRegions.add("Savannah")
            listRegions.add("Upper East")
            listRegions.add("Upper West")
            listRegions.add("Volta")
            listRegions.add("Western")
            listRegions.add("Western North")
            return listRegions
        }
    val gender: List<String>
        get() {
            val list: MutableList<String> = ArrayList()
            list.add("Select Gender")
            list.add("Female")
            list.add("Male")
            return list
        }

    val grade: List<String>
        get() {
            val list: MutableList<String> = ArrayList()
            list.add("Select Grade")
            list.add("SHS 1")
            list.add("SHS 2")
            list.add("SHS 3")
            list.add("Completed")
            list.add("Teacher")

            return list
        }
    val countryies: Array<String>
        get() = arrayOf(
            "Select Country",
            "Ghana",
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antarctica",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burma",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cape Verde",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo, Democratic Republic",
            "Congo, Republic of the",
            "Costa Rica",
            "Cote d'Ivoire",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Greece",
            "Greenland",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea, North",
            "Korea, South",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macedonia",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Mongolia",
            "Morocco",
            "Monaco",
            "Mozambique",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Norway",
            "Oman",
            "Pakistan",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Samoa",
            "San Marino",
            " Sao Tome",
            "Saudi Arabia",
            "Senegal",
            "Serbia and Montenegro",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe"
        )
    val netWorks: Array<String>
        get() = arrayOf("Select Network", "MTN", "Vodafone", "AirtelTigo")
    val netWorksVal: Array<String>
        get() = arrayOf("", "mtn", "vod", "tgo")

    val getObjTopics:MutableList<String>
        get() = mutableListOf("Historiography and Historical Skills", "Trans – Saharan Trade",
    "Islam in West Africa", "European Contact with West Africa", "Trans – Atlantic slave trade",
    "Christians Missionary Activities in West Africa",
    "The Scramble for the Partition of West Africa",
    "Colonial Rule in west Africa",
    "Problems of Independent West Africa States",
    "West Africa and International Organizations")

    val getSectBTopics:MutableList<String>
        get() = mutableListOf("Historiography and Historical Skills",
                "Pre-History of Africa",
                "The Civilization of Ancient Egypt",
                "The Civilization of North Africa Berbers",
                "The Civilization of Axum Ancient Ethiopia",
                "The Civilization of The Bantu",
                "The Civilization of East Africa Swahili",
            "Trans – Saharan Trade", "The Civilization of West African Sudanese States",
    "History of Ghana", "Pre-History of Ghana",
    "Peopling of Ghana: Origin and Rise of States and Kingdoms",
    "Social and Political systems of Pre-Colonial Ghana",
    "History of Medicine", "History of Arts and Technology Visual Art",
    "History of the Economy of Ghana up to 1900",
    "European Contact with West Africa",
    "The Trans – Atlantic slave trade",
    "Christian Missionary Activities in West Africa Ghana",
    "The Scramble for the Partition of West Africa",
    "Colonial Rule and Nationalism in west Africa",
    "Problems of Independent and After Nkrumah's Era",
    "Ghana Under Nkrumah's Regime",
    "Ghana in the Community of Nations")

    fun getTopics(type : String) : MutableList<String>{
        if(type.equals("Objectives")){
            return mutableListOf("Historiography and Historical Skills", "Trans – Saharan Trade",
                "Islam in West Africa", "European Contact with West Africa", "Trans – Atlantic slave trade",
                "Christians Missionary Activities in West Africa",
                "The Scramble for the Partition of West Africa",
                "Colonial Rule in west Africa",
                "Problems of Independent West Africa States",
                "West Africa and International Organizations")
        }else if(type.equals("Section B")){
            return mutableListOf("Historiography and Historical Skills", "Pre-History of Africa",
                "The Civilization of Ancient Egypt",
                "The Civilization of North Africa (Berbers)",
                "The Civilization of Axum (Ancient Ethiopia)",
                "The Origin and Spread of Bantu Civilization",
                "Swahili Civilization of East African Coast",
                "Trans – Saharan Trade",
                "Islam in West Africa",
                "The Civilization of West African Sudanese States",
                "The Civilization of West African Coastal States and Forest States",
                "Introduction to History of Ghana",
                "Pre-History of Ghana",
                "Peopling of Ghana: Origin and Rise of States and Kingdoms",
                "Social and Political systems of Pre-Colonial Ghana",
                "History of Medicine",
                "History of Arts and Technology (Visual Art)",
                "History of the Economy of Ghana up to 1900",
                "European Contact with West Africa",
                "The Trans – Atlantic Slave Trade",
                "Christian Missionary Activities in West Africa (Ghana)",
                "The Scramble for And Partition of West Africa (Ghana)",
                "Colonial Rule in West Africa",
                "Nationalism in West Africa (Ghana)",
                "Independence and After :  Nkrumah's Era",
                "Ghana After Nkrumah's Regime",
                "Problems Of Independent West Africa",
                "Ghana in the Community of Nations")
        }else if(type.equals("WASSCE Textbook")) {
            return mutableListOf("Historiography and Historical Skills",
                    "Trans – Saharan Trade",
                    "Islam in West Africa",
                    "European Contact with West Africa",
                    "Trans – Atlantic slave trade",
                    "Christian Missionary Activities in West Africa",
                    "The Scramble for the Partition of West Africa",
                "Colonial Rule in West Africa",
                "Problems of Independent West Africa States",
                "West Africa and International Organizations")
        }else if(type.equals("Quiz")){
            return mutableListOf("Objective Quiz", "Word Scramble", "Missing Letters", "Define Quiz", "Horde")
        }else
        {
            return mutableListOf()

        }
    }

    fun getGrade(mark : Int) : String{
        if(mark>=80){
            return "A"
        }else if(mark>=70){
            return "B2"
        }else if(mark>=65){
            return "B3"
        }else if(mark>=60){
            return "C4"
        }else if(mark>=55){
            return "C5"
        }else if(mark>=50){
            return "C6"
        }else if(mark>=45){
            return "D7"
        }else if(mark>=40){
            return "E8"
        }else {
            return "F9"
        }
    }

    fun getBackground(mark : Int):Int{
        if(mark>=80){
            return R.drawable.high
        }else if(mark>=55){
            return R.drawable.medium
        }else{
            return R.drawable.small
        }
    }

    fun getCongratMess(mark : Int):String{
        if(mark>=80){
            return "Keep it up. Keep reading your history Notes"
        }else if(mark>=55){
            return "Read more on this topic in your history notes and" +
                    "bet at your best"
        }else{
            return "Read more on this topic in your history notes. " +
                    R.string.readMore
        }
    }

    fun getColorGrade(mark: Int):Int{
        if(mark>=80){
            return R.color.green
        }else if(mark>=55){
            return R.color.yellow
        }else{
            return R.color.red
        }
    }

    fun boldText(inputString: String, textView: TextView){
        // Create a SpannableString from the complete string
        val regex = "\\*(.*?)\\*".toRegex()
        val matches = regex.findAll(inputString)

        var num = 0
        val spannableBuilder = SpannableStringBuilder(inputString)
        for (match in matches) {
            val boldStyle = StyleSpan(Typeface.BOLD)
            val start = match.range.start + 1-num // Skip the initial asterisk
            val end = match.range.endInclusive-num // Include the closing asterisk
            spannableBuilder.setSpan(boldStyle, start, end, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE)


            spannableBuilder.replace(start- 1, start, "")
            spannableBuilder.replace(end - 1, end, "")
            num +=2
        }

// Set the modified SpannableStringBuilder to the TextView
        textView.text = spannableBuilder
    }

    fun getInstructions(num : Int) : String{
        val list =  mutableListOf("All Questions are compulsory to attempt. ", 
                "", 
                "For each correct answer, you get 3 points.", 
                "", 
                "You have 3 life lines to help you answer the questions correctly.", 
                "", 
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - 50/50 - Two wrong answers will be removed leaving the correct answer and a wrong answer \n"+
                "\n"+
                " - Popular Choice - The ratio of selected answers from previous tries will be shown in a graph.\n"+
                "\n"+
                "You start all over again once you miss a question.", 
                "", 
                "Good Luck!",
        "All Questions are compulsory to attempt.", 
                "\n"+
                "For each correct answer, you get 3 points.", 
                "", 
                "You have 2 life lines to help you answer the questions correctly.", 
               "", 
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - Hint - You are given the definition of the word\n\n"+
               "You start all over again once you miss a question.+\n", 
                "Good Luck!",
        "All Questions are compulsory to attempt. \n", 
                "Enter the term who best describes the definition \n\n"+
               "For each correct answer, you get 3 points.\n", 
                "You have 2 life lines to help you answer the questions correctly.\n", 
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - Hint - You are given the word with some letters missing\n\n"+
                "You start all over again once you miss a question.\n", 
                "Good Luck!",
        "All Questions are compulsory to attempt. \n", 
                "This is a combination of Objective Quiz, "+
                "For each correct answer, you get 3 points.\n", 
                "You have 3 life lines to help you answer the questions correctly.\n", 
                "You start all over again once you miss a question.\n", 
                "Good Luck!",
        "")

        return list[num]
    }

    fun getSchools():Array<String>{
        return arrayOf("ABAKRAMPA SENIOR HIGH/TECH SCHOOL", 
                "ABEADZE STATE COLLEGE", 
                "ABEASEMAN COMM. DAY SENIOR HIGH SCHOOL", 
                "ABESIM SENIOR HIGH", 
                "ABETIFI PRESBY SENIOR HIGH SCHOOL", 
                "ABETIFI TECH. INST.", 
                "ABOR SENIOR HIGH SCHOOL", 
                "ABRAFI SENIOR HIGH SCHOOL", 
                "ABUADI/TSREFE SENIOR HIGH SCHOOL", 
                "ABUAKWA STATE COLLEGE", 
                "ABUBAKAR SIDIQ SENIOR HIGH SCHOOL", 
                "ABURA ASEBU KWAMANKESE ISL. SEN. HIGH", 
                "ABURAMAN SENIOR HIGH SCHOOL", 
                "ABURI GIRLS SENIOR HIGH SCHOOL", 
                "ABUTIA SENIOR HIGH/TCHNICAL SCHOOL ", 
                "ACADEMY OF CHRIST THE KING, CAPE COAST", 
                "ACCRA ACADEMY", 
                "ACCRA GIRLS SENIOR HIGH SCHOOL ", 
                "ACCRA SENIOR HIGH SCHOOL", 
                "ACCRA TECH. TRG. CENTRE ", 
                "ACCRA WESLEY GIRLS SENIOR HIGH SCHOOL", 
                "ACHERENSUA SENIOR HIGH SCHOOL", 
                "ACHIASE SENIOR HIGH SCHOOL", 
                "ACHIMOTA BUSINESS COLL.", 
                "ACHIMOTA SENIOR HIGH SCHOOL", 
                "ACHINAKROM SENIOR HIGH SCHOOL", 
                "ACTION NABINCHA SENIOR HIGH", 
                "ACTION SEC/TECH SCHOOL", 
                "ACTION SENIOR HIGH/TECH", 
                "ADA SENIOR HIGH SCHOOL ", 
                "ADA SENIOR HIGH/TECH SCHOOL ", 
                "ADABIE COMM. INST.", 
                "ADAKLU SENIOR HIGH SCHOOL", 
                "ADANKWAMAN SENIOR HIGH SCHOOL", 
                "ADANWOMASE SENIOR HIGH SCHOOL", 
                "ADEHYEMAN SENIOR HIGH", 
                "ADEISO PRESBY SENIOR HIGH SCHOOL", 
                "ADIDOME SENIOR HIGH SCHOOL", 
                "ADIEMBRA SENIOR HIGH SCHOOL", 
                "ADISADEL COLLEGE", 
                "ADJEN KOTOKU SENIOR HIGH SCHOOL", 
                "ADJENA SENIOR HIGH/TECH SCHOOL", 
                "ADJOAFUA COMM. SENIOR HIGH SCHOOL", 
                "ADOBEWORA COMM. SENIOR HIGH SCHOOL", 
                "ADONTEN SENIOR HIGH SCHOOL", 
                "ADOTE OTINTOR COLLEGE", 
                "ADU GYAMFI SENIOR HIGH SCHOOL", 
                "ADUGYAMA COMM. SENIOR HIGH SCHOOL", 
                "ADUMAN SENIOR HIGH SCHOOL", 
                "ADVENTIST GIRLS SENIOR HIGH SCHOOL, NTONSO", 
                "ADVENTIST SENIOR HIGH", 
                "ADVENTIST SENIOR HIGH SCHOOL, KUMASI", 
                "AFADJATO SENIOR HIGH/TECH SCHOOL", 
                "AFIFE SENIOR HIGH TECH SCHOOL", 
                "AFIGYAMAN SENIOR HIGH SCHOOL", 
                "AFRICAN ADV. COL. OF COM/TECH SCH", 
                "AFRICANA SENIOR HIGH SCHOOL", 
                "AFUA KOBI AMPEM GIRLS' SENIOR HIGH SCHOOL", 
                "AGATE COMM. SENIOR HIGH SCHOOL", 
                "AGGREY MEM. A.M.E.ZION SENIOR HIGH SCHOOL", 
                "AGOGO STATE COLLEGE ", 
                "AGONA FANKOBAA SENIOR HIGH SCHOOL", 
                "AGONA NAMONWORA COMM.SENIOR HIGH SCHOOL", 
                "AGONA SENIOR HIGH/TECH SCHOOL", 
                "AGOTIME SENIOR HIGH SCHOOL", 
                "AGRIC NZEMA SENIOR HIGH SCHOOL, KUMASI", 
                "AHAFOMAN SENIOR HIGH/TECH SCHOOL", 
                "AHAMANSU ISLAMIC SENIOR HIGH SCHOOL", 
                "AHANTAMAN GIRLS' SENIOR HIGH SCHOOL", 
                "AI-BASAR SENIOR HIGH SCHOOL", 
                "AKATSI SENIOR HIGH/TECH SCHOOL", 
                "AKIM ASAFO SENIOR HIGH SCHOOL", 
                "AKIM STATE COLLEGE", 
                "AKIM SWEDRU SENIOR HIGH SCHOOL", 
                "AKOKOASO SENIOR HIGH/TECH SCHOOL", 
                "AKOME SENIOR HIGH/TECH SCHOOL", 
                "AKONTOMBRA SENIOR HIGH SCHOOL", 
                "AKOSOMBO INTERNATIONAL SENIOR HIGH", 
                "AKPAFU SENIOR HIGH/TECH SCHOOL", 
                "AKRO SENIOR HIGH/TECH SCHOOL", 
                "AKROFUOM SENIOR HIGH/TECH SCHOOL", 
                "AKUMADAN SENIOR HIGH SCHOOL", 
                "AKUSE METHODIST SENIOR HIGH/TECH SCHOOL", 
                "AKWAMUMAN SENIOR HIGH SCHOOL", 
                "AKWATIA TECH. INST.", 
                "AKWESI AWOBAA SENIOR HIGH SCHOOL", 
                "AKYIN SENIOR HIGH SCHOOL", 
                "AL AKROSO SENIOR HIGH/TECH SCHOOL", 
                "AL ATWEAMAN SENIOR HIGH SCHOOL", 
                "ALAVANYO SENIOR HIGH/TECH SCHOOL", 
                "AL-AZARIYA ISLAMIC SENIOR HIGH SCHOOL, KUMASI", 
                "AL-BAYAN ISLAMIC SENIOR HIGH", 
                "ALL FOR CHRIST SENIOR HIGH TECHNICAL SCHOOL", 
                "ALL-STANDARD SENIOR HIGH", 
                "ALMAKTOUN ISLAMIC SNR. HIGH", 
                "ALSALAMEXCELLENT ACADEMY", 
                "AMANIAMPONG SENIOR HIGH SCHOOL ", 
                "AMANKWAKROM FISHERIES AGRIC. TECH. INST.", 
                "AMANTEN SENIOR HIGH SCHOOL", 
                "AMASAMAN SENIOR HIGH/TECH SCHOOL", 
                "AMAZING LOVE SENIOR HIGH SCHOOL", 
                "AME ZION GIRLS SEC SCHOOL", 
                "AMEDZOFE TECHNICAL INSTITUTE", 
                "AMENFIMAN SENIOR HIGH SCHOOL", 
                "AMEYAW AKUMFI SENIOR HIGH/TECH SCHOOL", 
                "AMOANA PRASO SENIOR HIGH SCHOOL", 
                "AMUANA PRASO SENIOR HIGH SCHOOL", 
                "ANBARIYA SENIOR HIGH SCHOOL", 
                "ANDAM SENIOR HIGH/TECH SCH", 
                "ANFOEGA SENIOR HIGH SCHOOL", 
                "ANGEL EDUC. COMPLEX SENIOR HIGH", 
                "ANGLICAN SENIOR HIGH SCHOOL", 
                "ANGLICAN SENIOR HIGH SCHOOL, KUMASI", 
                "ANISA SENIOR HIGH", 
                "ANLO AFIADENYIGBA SENIOR HIGH SCHOOL", 
                "ANLO AWOMEFIA SENIOR HIGH SCHOOL", 
                "ANLO SENIOR HIGH SCHOOL", 
                "ANLO TECH. INST.", 
                "ANMCHARA SENIOR HIGH SCHOOL", 
                "ANNOR ADJAYE SENIOR HIGH SCHOOL", 
                "ANSON SENIOR HIGH SCH", 
                "ANTOA SENIOR HIGH SCHOOL", 
                "ANUM APAPAM COMM. DAY SENIOR HIGH SCHOOL", 
                "ANUM PRESBY SENIOR HIGH SCHOOL", 
                "APAM SENIOR HIGH SCHOOL", 
                "APEDWA PRESBY SENIOR HIGH", 
                "APEDWA PRESBY SENIOR HIGH SCHOOL", 
                "APEGUSO SENIOR HIGH SCHOOL", 
                "APERADE SENIOR HIGH/TECH SCHOOL", 
                "APEX COLLEGE", 
                "APOSTLE SAFO SCH OF ARTS AND SCIENCE", 
                "APOSTOLIC SENIOR HIGH", 
                "ARCHBISHOP PORTER GIRLS SENIOR HIGH SCHOOL", 
                "ARCHIMEDES COLLEGE OF SCIENCE AND TECHNOLOGY", 
                "ARMED FORCES SENIOR HIGH/TECH SCHOOL, KUMASI", 
                "ARTIC SENIOR HIGH", 
                "ASAMANKESE SENIOR HIGH SCHOOL ", 
                "ASANKRANGWA SENIOR HIGH SCHOOL", 
                "ASANKRANGWA SENIOR HIGH/TECH SCHOOL", 
                "ASANTE AKYEM TECHNOLOGY INSTITUTE", 
                "ASANTEMAN SENIOR HIGH SCHOOL", 
                "ASARE BEDIAKO SENIOR HIGH SCHOOL", 
                "ASAWINSO SENIOR HIGH SCHOOL", 
                "ASCENSION SENIOR HIGH", 
                "ASESEWA SENIOR HIGH SCHOOL", 
                "ASHIAMAN SENIOR HIGH SCHOOL ", 
                "ASHIAMAN TECH/VOC. INST. ", 
                "ASHLEY SEC/BUSINESS COL.", 
                "ASSEMBLIES OF GOD", 
                "ASSIN MANSO SENIOR HIGH SCHOOL", 
                "ASSIN NORTH SENIOR HIGH/TECH SCHOOL", 
                "ASSIN NSUTA SENIOR HIGH SCHOOL", 
                "ASSIN STATE COLLEGE", 
                "ASSURANCE SENIOR HIGH TECHNICAL SCHOOL", 
                "ASUANSI TECH. INST.", 
                "ASUKAWKAW SENIOR HIGH SCHOOL", 
                "ASUOM SENIOR HIGH SCHOOL", 
                "ASUOSO COMM. SENIOR HIGH SCHOOL", 
                "ASWAJ SENIOR HIGH", 
                "ATEBUBU SENIOR HIGH SCHOOL", 
                "ATECO HIGH SCHOOL COMPLEX", 
                "ATIAVI SENIOR HIGH/TECH SCHOOL", 
                "ATTAFUAH SENIOR HIGH/TECH SCHOOL", 
                "ATWIMA KWANWOMA SENIOR HIGH/TECH SCHOOL", 
                "AVATIME SENIOR HIGH SCHOOL", 
                "AVE SENIOR HIGH SCHOOL", 
                "AVENOR SENIOR HIGH SCHOOL", 
                "AVEYIME BATTOR SENIOR HIGH/TECH SCHOOL", 
                "AWASUMAN SENIOR HIGH", 
                "AWE SENIOR HIGH/TECH SCHOOL", 
                "AWUDOME SENIOR HIGH SCHOOL", 
                "AWUTU BAWJIASE COMM SENIOR HIGH SCHOOL", 
                "AWUTU WINTON SENIOR HIGH SCHOOL", 
                "AXIM GIRLS SENIOR HIGH SCHOOL", 
                "AYANFURI COMM. DAY SENIOR HIGH", 
                "AYANFURI SENIOR HIGH SCHOOL", 
                "AYIREBI SENIOR HIGH SCHOOL", 
                "AZEEM-NAMOA SENIOR HIGH/TECH SCHOOL", 
                "AZOKA MEM. ACADEMY", 
                "BADU SENIOR HIGH/TECH SCHOOL", 
                "BAGLO RIDGE SENIOR HIGH/TECH SCHOOL", 
                "BAIDOO BONSOE SENIOR HIGH/TECH SCHOOL", 
                "BAMBOI COMM. SENIOR HIGH SCHOOL", 
                "BANDAMAN SENIOR HIGH SCHOOL", 
                "BANKA COMM. SENIOR HIGH SCHOOL", 
                "BANKOMAN SENIOR HIGH SCHOOL", 
                "BAPTIST SENIOR HIGH", 
                "BAREKESE SENIOR HIGH SCHOOL", 
                "BASSA COMMUNITY SENIOR HIGH SCHOOL", 
                "BATTOR SENIOR HIGH SCHOOL", 
                "BAWKU SENIOR HIGH SCHOOL", 
                "BAWKU SENIOR HIGH/TECH SCHOOL", 
                "BAWKU TECH. INST.", 
                "BECHEM PRESBY SENIOR HIGH SCHOOL", 
                "BENKUM SENIOR HIGH SCHOOL", 
                "BENSO SENIOR HIGH/TECH SCHOOL", 
                "BEO SENIOR HIGH/TECH", 
                "BEPONG SENIOR HIGH SCHOOL", 
                "BEPOSO SENIOR HIGH SCHOOL", 
                "BEREKUM PRESBY SENIOR HIGH SCHOOL", 
                "BEREKUM SENIOR HIGH SCHOOL", 
                "BEREKUM STAR SENIOR HIGH", 
                "BIA SENIOR HIGH/TECH SCHOOL", 
                "BIAKOYE COMM. DAY SCHOOL", 
                "BIBIANI SENIOR HIGH/TECH SCHOOL ", 
                "BIMBILLA SENIOR HIGH SCHOOL", 
                "BINDURI COMM. SENIOR HIGH SCHOOL", 
                "BIRIFOH SENIOR HIGH SCHOOL", 
                "BISEASE SENIOR HIGH/COMM. SCHOOL", 
                "BISHOP HERMAN COLLEGE", 
                "BOA-AMPONSEM SENIOR HIGH SCHOOL", 
                "BOAKYE TROMO SENIOR HIGH/TECH SCHOOL", 
                "BODADA PRESBY SENIOR HIGH", 
                "BODI SENIOR HIGH SCHOOL", 
                "BODOMASE SENIOR HIGH/TECH SCHOOL", 
                "BODWESANGO SENIOR HIGH SCHOOL", 
                "BOLE SENIOR HIGH SCHOOL", 
                "BOLGA GIRLS SENIOR HIGH SCHOOL", 
                "BOLGA SHERIGU COMM. SENIOR HIGH SCHOOL", 
                "BOLGA TECH. INST.", 
                "BOLGATANGA SENIOR HIGH SCHOOL", 
                "BOMAA COMM. SENIOR HIGH SCHOOL", 
                "BOMPATA PRESBY SENIOR HIGH SCHOOL", 
                "BOMPEH SENIOR HIGH./TECH SCHOOL", 
                "BONGO SENIOR HIGH SCHOOL", 
                "BONOMAN SENIOR HIGH", 
                "BONTRASE SENIOR HIGH TECH. SCHOOL", 
                "BONWIRE SENIOR HIGH/TECH SCHOOL", 
                "BONZO-KAKU SENIOR HIGH SCHOOL", 
                "BOSO SENIOR HIGH TECHNICAL SCHOOL", 
                "BOSOME SENIOR HIGH/TECH. SCHOOL", 
                "BOSOMTWE OYOKO COMM. SENIOR HIGH SCHOOL", 
                "BOWIRI COMM. DAY SCHOOL", 
                "BRAKWA SENIOR HIGH/TECH SCHOOL", 
                "BREMAN ASIKUMA SENIOR HIGH SCHOOL", 
                "BRIDGE SENIOR HIGH SCHOOL", 
                "BRIGHT SENIOR HIGH", 
                "BUEMAN SENIOR HIGH SCHOOL", 
                "BUIPE SENIOR HIGH SCHOOL", 
                "BUIPE TECH/VOC INST.", 
                "BUNKPURUGU SENIOR HIGH/TECH SCHOOL", 
                "BUSINESS SENIOR HIGH SCHOOL, TAMALE", 
                "BUSUNYA SENIOR HIGH SCHOOL", 
                "C.P.F SCHOOL OF SCIENCE", 
                "C.Y.O.VOC. TECH. INST.", 
                "CAMBRIDGE SENIOR HIGH.", 
                "CAPE COAST INTERNATIONAL SENIOR HIGH", 
                "CAPE COAST TECH. INST.", 
                "CENTRAL SENIOR HIGH", 
                "CENTRAL SENIOR HIGH SCHOOL", 
                "CENTRE COLLEGE SENIOR HIGH", 
                "CENTRE OF EXCELLENT BUSINESS SEN. HIGH", 
                "CHARITY COMM SCH SCHOOL", 
                "CHARITY SENIOR HIGH", 
                "CHEMU SENIOR HIGH/TECH SCHOOL", 
                "CHEREPONI SENIOR HIGH/TECH SCHOOL", 
                "CHIANA SENIOR HIGH SCHOOL", 
                "CHIRAA SENIOR HIGH SCHOOL", 
                "CHIRANO COMM. DAY SENIOR HIGH SCHOOL", 
                "CHRIST APOSTOLIC SENIOR HIGH", 
                "CHRIST SENIOR HIGH SCHOOL", 
                "CHRIST THE KING CATH., OBUASI", 
                "CHRISTIAN METHODIST SENIOR HIGH SCHOOL", 
                "CHURCH OF CHRIST SENIOR HIGH", 
                "CHURCH OF CHRIST SENIOR HIGH", 
                "CHURCH OF CHRIST SENIOR HIGH SCHOOL", 
                "CITADEL SENIOR HIGH SCHOOL", 
                "CITY SENIOR HIGH", 
                "CITY SENIOR HIGH/BUSINESS COLL", 
                "COLLINS SENIOR HIGH/COMMERCIAL SCHOOL, AGOGO", 
                "COMBONI TECH/VOC INST.", 
                "COMMONWEALTH COLLEGE", 
                "CONQUERORS ACADEMY OF BUSINESS STUDIES", 
                "CORPUS CHRISTI SEC SCHOOL", 
                "COSMOS SENIOR HIGH", 
                "D. D. D. SENIOR HIGH TECH SCH", 
                "DABALA SENIOR HIGH/TECH.", 
                "DABOASE SENIOR HIGH/TECH SCHOOL", 
                "DABOKPA VOC/TECH. INST.", 
                "DABOYA COMM. DAY SCHOOL", 
                "DADEASE AGRIC SENIOR HIGH SCHOOL", 
                "DADIESO SENIOR HIGH SCHOOL", 
                "DAFFIAMAH SENIOR HIGH SCHOOL", 
                "DAGBON STATE SENIOR HIGH/TECH SCHOOL", 
                "DAMBAI SENIOR HIGH SCHOOL", 
                "DAMONGO SENIOR HIGH SCHOOL", 
                "DANAKS SENIOR HIGH", 
                "DANNAKS SENIOR HIGH", 
                "DANQUAH INTERNATIONAL SCHOOL", 
                "DANSIS SENIOR HIGH SCH", 
                "DANSOMAN SEC. SCH.", 
                "DARD SENIOR HIGH/TECHNICAL INSTITUTE", 
                "DARIUS SENIOR HIGH/TECH.", 
                "DASEIN PRACTICAL SCHOOL", 
                "DATUS SEC. BUSINESS COLL.", 
                "DEKS SENIOR HIGH", 
                "DELCAM SENIOR HIGH SCHOOL", 
                "DENYASEMAN CATH.SENIOR HIGH SCHOOL", 
                "DERMA COMM. DAY SENIOR HIGH SCHOOL", 
                "DE-YONGSTERS SENIOR HIGH", 
                "DIABENE SENIOR HIGH/TECH SCHOOL", 
                "DIAMONO SENIOR HIGH SCHOOL", 
                "DIASO SENIOR HIGH SCHOOL", 
                "DIASPORA GIRLS' SENIOR HIGH SCHOOL", 
                "DODI-PAPASE SENIOR HIGH/TECH SCHOOL", 
                "DOFOR SENIOR HIGH SCHOOL", 
                "DOMPOASE SENIOR HIGH SCHOOL", 
                "DON BOSCO VOC./TECH. INST. ", 
                "DONKORKROM AGRIC SENIOR HIGH SCHOOL", 
                "DONKRO-NKWANTA SENIOR HIGH", 
                "DORA MEM. SENIOR HIGH", 
                "DORMAA SENIOR HIGH SCHOOL", 
                "DR. HILA LIMAN SENIOR HIGH SCHOOL", 
                "DROBO SENIOR HIGH SCHOOL", 
                "DUADASO NO. 1 SENIOR HIGH/TECH SCHOOL", 
                "DUNCAN SENIOR HIGH/TECH SCHOOL", 
                "DUNKWA SENIOR HIGH/TECH SCHOOL", 
                "DWAMENA AKENTEN SENIOR HIGH SCHOOL", 
                "DWENTI COLLEGE", 
                "DZODZE PENYI SENIOR HIGH SCHOOL", 
                "DZOKSON BUSINESS COLLEGE", 
                "DZOLO SENIOR HIGH SCHOOL", 
                "E. P. AGRIC SENIOR HIGH/TECH SCHOOL", 
                "E. P. SENIOR HIGH SCHOOL", 
                "E. P. TECHNICAL VOCATIONAL INSTITUTE, ALAVANYO", 
                "E.P.C. MAWUKO GIRLS SENIOR HIGH SCHOOL", 
                "EAGLECREST SENIOR HIGH", 
                "EASTBANK SENIOR HIGH SCHOOL", 
                "EBENEZER SENIOR HIGH SCHOOL", 
                "EDGE HILL SENIOR HIGH", 
                "EDINAMAN SENIOR HIGH SCHOOL", 
                "EFFIDUASE SENIOR HIGH/COM SCHOOL", 
                "EFFIDUASE SENIOR HIGH/TECH SCHOOL", 
                "EFFUTU SENIOR HIGH/TECH SCHOOL", 
                "EGUAFO-ABREM SENIOR HIGH SCHOOL", 
                "EJISU SENIOR HIGH/TECH SCHOOL", 
                "EJISUMAN SENIOR HIGH SCHOOL", 
                "EJURA ISLAMIC SENIOR HIGH SCHOOL", 
                "EJURAMAN ANGLICAN SENIOR HIGH SCHOOL", 
                "EKUMFI T. I. AHMADIIYYA SENIOR HIGH SCHOOL", 
                "ELIM SENIOR SENIOR HIGH", 
                "ELITE COLLEGE", 
                "ELOHIM SNR. HIGH SCHOOL", 
                "EMMANUEL FOUNDATION SHS", 
                "ENYAN DENKYIRA SENIOR HIGH SCHOOL", 
                "ENYAN MAIM COMM. DAY SENIOR HIGH SCHOOL", 
                "ENYAN-ABAASA TECHNICAL INSTITUTE", 
                "EPINAL SENIOR HIGH SCHOOL", 
                "EREMON SENIOR HIGH/TECH SCHOOL", 
                "ESAASE BONTEFUFUO SNR. HIGH/TECH. SCHOOL", 
                "ESIAMA SENIOR HIGH/TECH SCHOOL", 
                "EVANGELICAL BUSINESS HIGH", 
                "EXACAM SENIOR HIGH", 
                "EXCELLENCE COLLEGE INT.", 
                "FAITH COMM. BAPTIST SCH", 
                "FAITH HIGH SCHOOL", 
                "FD'S INTERNATIONAL SENIOR HIGH TECH.", 
                "FETTEHMAN SENIOR HIGH SCHOOL COLLEGE OF MUSIC SENIOR HIGH SCHOOL, MOZANO", 
                "FIASEMAN SENIOR HIGH SCHOOL", 
                "FIJAI SENIOR HIGH SCHOOL", 
                "FIRM FOUNDATION SENIOR HIGH", 
                "FODOA COMM. SENIOR HIGH SCHOOL", 
                "FOMENA T.I. AHMAD SENIOR HIGH SCHOOL", 
                "FORCES SENIOR HIGH/TECH SCHOOL, BURMA CAMP ", 
                "FOREVER YOUNG INT. SENIOR HIGH", 
                "FR. AUGUSTINE MURPHY HIGH SCH", 
                "FR. DOGLI MEMORIAL VOC.TECH. INST.", 
                "FRAFRAHA COMM. SENIOR HIGH SCHOOL ", 
                "FRUITFUL LIFE SENIOR HIGH", 
                "FUMBISI SENIOR HIGH SCHOOL", 
                "FUNSI SENIOR HIGH SCHOOL", 
                "GALAXY INT. SCHOOL", 
                "GAMBAGA GIRLS SENIOR HIGH SCHOOL", 
                "GAMBIGO COMM. DAY SENIOR HIGH SCHOOL", 
                "GARDEN CITY COMM. COLLEGE", 
                "GARU COMM. DAY SENIOR HIGH SCHOOL", 
                "GATEWAY SENIOR HIGH", 
                "GHANA CHRISTIAN INT.HIGH SCH.", 
                "GHANA COLLEGE SENIOR HIGH", 
                "GHANA INTERNATIONAL CHRISTIAN ACADEMY", 
                "GHANA MUSLIM MISSION SENIOR HIGH SCHOOL", 
                "GHANA NATIONAL ACADEMY", 
                "GHANA NATIONAL COLLEGE", 
                "GHANA SENIOR HIGH SCHOOL, KOFORIDUA", 
                "GHANA SENIOR HIGH SCHOOL, TAMALE", 
                "GHANA SENIOR HIGH/TECH SCHOOL", 
                "GHANAIAN GERMAN SENIOR HIGH SCHOOL", 
                "GHANA-LEBANON ISLAMIC SEC.", 
                "GHANATA SENIOR HIGH SCHOOL", 
                "GIFAM TRAINING CENTRE", 
                "GLOBAL SENIOR HIGH", 
                "GOKA SENIOR HIGH/TECH SCHOOL", 
                "GOLDEN GATE SENIOR HIGH", 
                "GOLDEN STEP SENIOR HIGH SCHOOL", 
                "GOMOA GYAMAN SENIOR HIGH SCHOOL", 
                "GOMOA SENIOR HIGH/TECH SCHOOL", 
                "GONNO ADA TECH. INST. ", 
                "GOOD SHEPHERD SENIOR HIGH", 
                "GOWRIE SENIOR HIGH/TECH SCHOOL", 
                "GRACE ACADEMY SENIOR HIGH SCHOOL", 
                "GREAT DAFCO SENIOR HIGH", 
                "GREAT FAITH ROCKERY SENIOR HIGH", 
                "GREAT GHADECO SENIOR HIGH", 
                "GREATER MANCHESTER EDUCATIONAL INSTITUTE", 
                "GREENFIELD SENIOR HIGH SCHOOL", 
                "GUAKRO EFFAH SENIOR HIGH SCHOOL", 
                "GUSHEGU SENIOR HIGH SCHOOL", 
                "GWIRAMAN COMM.SENIOR HIGH SCHOOL", 
                "GYAAMA PENSAN SENIOR HIGH/TECH SCHOOL", 
                "GYAASE COMMUNITY SENIOR HIGH SCHOOL", 
                "GYAMFI KUMANINI SENIOR HIGH/TECH SCHOOL", 
                "GYARKO COMM. DAY SENIOR HIGH SCHOOL", 
                "HALF ASSINI SENIOR HIGH SCHOOL", 
                "HAN SENIOR HIGH", 
                "HAN SENIOR HIGH SCHOOL", 
                "HARVARD SENIOR HIGH", 
                "HAVE TECH. INST.", 
                "HAZELWAY INTERNATIONAL SCHOOL", 
                "HERITAGE ACADEMY SENIOR HIGH", 
                "HILL VIEW SENIOR HIGH", 
                "HIS MAJESTY ACADEMY", 
                "H'MOUNT SINAI SENIOR HIGH SCHOOL", 
                "HOLY CHILD SCHOOL, CAPE COAST", 
                "HOLY CITY SENIOR HIGH/TECH.", 
                "HOLY FAMILY SENIOR HIGH SCHOOL", 
                "HOLY SPIRIT SENIOR HIGH", 
                "HOLY TRINITY SENIOR HIGH SCHOOL", 
                "HOPE AND FAITH SENIOR HIGH SCHOOL", 
                "HOPE COLLEGE", 
                "HUMAN FACTOR LEADERSHIP SENIOR HIGH", 
                "HUNI VALLEY SENIOR HIGH SCHOOL", 
                "HWIDIEM SENIOR HIGH SCHOOL", 
                "IBANDUR RAHMAN ACADEMY SENIOR HIGH", 
                "IBN ABBASS SENIOR HIGH SCHOOL", 
                "ICODEHS SENIOR HIGH", 
                "IDEAL COLLEGE", 
                "IDEAL COLLEGE", 
                "IDEAL COLLEGE", 
                "INFANT JESUS CATHOLIC SENIOR HIGH", 
                "INSAANIYYA SENIOR HIGH", 
                "INTELLECTUALS SENIOR HIGH, CHINDERI", 
                " ISLAMIC GIRLS SENIOR HIGH SCHOOL,SUHUM ", 
                "ISLAMIC SCIENCE SENIOR HIGH SCHOOL, TAMALE", 
                "ISLAMIC SENIOR HIGH SCHOOL, AMPABAME", 
                "ISLAMIC SENIOR HIGH SCHOOL, WA", 
                "ISTIQUAAMA SENIOR HIGH SCHOOL", 
                "ITAWHEED SENIOR HIGH SCHOOL", 
                "J.E.A. MILLS SENIOR HIGH SCHOOL", 
                "J.G. KNOL VOC. TECH. INST.", 
                "JABEZ COLLEGE", 
                "JABEZ DOMINIC EDUCATIONAL INSTITUTE", 
                "JACHIE PRAMSO SENIOR HIGH SCHOOL", 
                "JACOBU SENIOR HIGH/TECH. SCHOOL", 
                "JAMIAT AL-HIDAYA ISLAMIC GIRLS SENIOR HIGH SCHOOL", 
                "JANGA SENIOR HIGH/TECH SCHOOL", 
                "JEMA SENIOR HIGH SCHOOL", 
                "JEMIMA ELIZABETH TAYLOR GIRLS SENIOR HIGH", 
                "JIM BOURTON MEM AGRIC. SENIOR HIGH SCHOOL", 
                "JINIJINI SENIOR HIGH SCHOOL", 
                "JIRAPA SENIOR HIGH SCHOOL", 
                "JITA SENIOR HIGH", 
                "JOY PROFESSIONAL ACADEMY", 
                "JOY STANDARD COLLEGE", 
                "JUABEN SENIOR HIGH SCHOOL", 
                "JUABOSO SENIOR HIGH SCHOOL", 
                "JUASO SENIOR HIGH/TECH SCHOOL", 
                "JUBILEE SENIOR HIGH", 
                "JUBILEE SENIOR HIGH SCHOOL", 
                "JUKWA SENIOR HIGH SCHOOL", 
                "JUST LOVE SENIOR HIGH SCHOOL", 
                "KADE SENIOR HIGH/TECH SCHOOL", 
                "KADJEBI-ASATO SENIOR HIGH SCHOOL", 
                "KAJAJI SENIOR HIGH SCHOOL", 
                "KALEO SENIOR HIGH/TECH SCHOOL", 
                "KALPOHIN SENIOR HIGH SCHOOL", 
                "KANJAGA COMM. SENIOR HIGH SCHOOL", 
                "KANTON SENIOR HIGH SCHOOL", 
                "KARAGA SENIOR HIGH SCHOOL", 
                "KASULIYILI SENIOR HIGH SCHOOL", 
                "KESSE BASAHYIA SENIOR HIGH SCHOOL", 
                "KETA BUSINESS SENIOR HIGH SCHOOL", 
                "KETA SENIOR HIGH/TECH SCHOOL", 
                "KETE KRACHI SENIOR HIGH/TECH SCHOOL", 
                "KIBI SENIOR HIGH/TECH SCHOOL", 
                "KIKAM TECH. INST.", 
                "KINBU SENIOR HIGH/TECH SCHOOL", 
                "KING DAVID COMM COLLEGE", 
                "KINGS COLLEGE", 
                "KINGS SENIOR HIGH SCHOOL", 
                "KINGSBY METHODIST GIRLS SENIOR HIGH", 
                "KINTAMPO SENIOR HIGH SCHOOL", 
                "KLIKOR SENIOR HIGH/TECH SCHOOL", 
                "KLO-AGOGO SENIOR HIGH SCHOOL KANESHIE SENIOR HIGH/TECH SCHOOL", 
                "KNUST SENIOR HIGH SCHOOL", 
                "KO SENIOR HIGH SCHOOL", 
                "KOASE SENIOR HIGH/TECH SCHOOL", 
                "KOFI ADJEI SENIOR HIGH/TECH SCHOOL", 
                "KOFIASE ADVENTIST SENIOR HIGH/TECH. SCHOOL ", 
                "KOFORIDUA SENIOR HIGH/TECH SCHOOL", 
                "KOFORIDUA TECH. INST.", 
                "KOLEGE HIGH SCHOOL", 
                "KOMENDA SENIOR HIGH/TECH SCHOOL", 
                "KONADU YIADOM CATHOLIC SENIOR HIGH SCHOOL", 
                "KONGO SENIOR HIGH SCHOOL", 
                "KONONGO ODUMASE SENIOR HIGH SCHOOL", 
                "KPANDAI SENIOR HIGH SCHOOL", 
                "KPANDO SENIOR HIGH SCHOOL", 
                "KPANDO TECH. INST.", 
                "KPASSA SENIOR HIGH/TECH SCHOOL", 
                "KPEDZE SENIOR HIGH SCHOOL", 
                "KPEVE SENIOR HIGH SCHOOL", 
                "KPONE COMM. SENIOR HIGH SCHOOL", 
                "KRABOA-COALTAR PRESBY SENIOR HIGH SCHOOL HIGH/TECH.", 
                "KRACHI SENIOR HIGH SCHOOL", 
                "KROBEA ASANTE TECH/VOC SCHOOL", 
                "KROBO COMM.SENIOR HIGH SCHOOL", 
                "KROBO GIRLS SENIOR HIGH SCHOOL", 
                "KUKUOM AGRIC SENIOR HIGH SCHOOL", 
                "KUMASI ACADEMY", 
                "KUMASI GIRLS SENIOR HIGH SCHOOL", 
                "KUMASI SENIOR HIGH SCHOOL", 
                "KUMASI SENIOR HIGH/TECH SCHOOL", 
                "KUMASI SPORTS ACADEMY SENIOR HIGH", 
                "KUMASI TECH. INST.", 
                "KUMASI WESLEY GIRLS HIGH SCHOOL", 
                "KUMBUNGU SENIOR HIGH SCHOOL", 
                "KUROFA METHODIST SENIOR HIGH", 
                "KUROFA METHODIST SENIOR HIGH SCHOOL", 
                "KUSANABA SENIOR HIGH SCHOOL", 
                "KUSAUG STATE SENIOR HIGH/TECH", 
                "KWABENG ANGLICAN SENIOR HIGH/TECH SCHOOL", 
                "KWABENYA COMM. SENIOR HIGH SCHOOL", 
                "KWABRE SENIOR HIGH SCHOOL", 
                "KWAHU RIDGE SENIOR HIGH SCHOOL", 
                "KWAHU TAFO SENIOR HIGH SCHOOL", 
                "KWAME DANSO SENIOR HIGH/TECH SCHOOL", 
                "KWANWOMA SENIOR HIGH SCHOOL", 
                "KWANYAKO SENIOR HIGH SCHOOL", 
                "KWAOBAAH NYANOA COMM. SENIOR HIGH SCHOOL ", 
                "KWARTENG ANKOMAH SENIOR HIGH SCHOOL", 
                "KWEGYIR AGGREY SENIOR HIGH SCHOOL", 
                "KYABOBO GIRLS SENIOR HIGH SCHOOL", 
                "LA PRESBY SENIOR HIGH SCHOOL", 
                "LABONE SENIOR HIGH SCHOOL", 
                "LADY FATIMAH GIRLS SENIOR HIGH SCHOOL", 
                "LAMBUSSIE COMM SENIOR HIGH SCHOOL", 
                "LASHIBI COMMUNITY SENIOR HIGH SCHOOL", 
                "LASSIE-TUOLU SENIOR HIGH SCHOOL", 
                "LAWRA SENIOR HIGH SCHOOL", 
                "LAWRENCE SENIOR HIGH", 
                "LEARNING FIELD SENIOR HIGH SCHOOL", 
                "LEGACY GIRLS COLLEGE", 
                "LEKLEBI SENIOR HIGH SCHOOL", 
                "LIBERTY SENIOR HIGH", 
                "LIKPE SENIOR HIGH SCHOOL", 
                "LOGGU COMM. DAY SCHOOL", 
                "MAABANG SENIOR HIGH/TECH SCHOOL", 
                "MAAME KROBO COMM. SENIOR HIGH SCHOOL", 
                "MAFI-KUMASI SENIOR HIGH/TECH SCHOOL", 
                "MAMPONG/AKW SENIOR HIGH/TECH SCHOOL FOR THE DEAF", 
                "MANDO SENIOR HIGH/TECH SCHOOL", 
                "MANGOASE SENIOR HIGH SCHOOL", 
                "MANKESSIM SENIOR HIGH/TECH SCHOOL", 
                "MANKRANSO SENIOR HIGH SCHOOL", 
                "MANSEN SENIOR HIGH SCHOOL", 
                "MANSO-ADUBIA SENIOR HIGH SCHOOL", 
                "MANSO-AMENFI COMM. DAY SENIOR HIGH SCHOOL", 
                "MANSOMAN SENIOR HIGH SCHOOL", 
                "MANYA KROBO SENIOR HIGH SCHOOL", 
                "MARAKAZ ISLAMIC SENIOR HIGH SCHOOL", 
                "MARANATHA BUSINESS SENIOR HIGH", 
                "MARS BUSINESS SENIOR HIGH", 
                "MAWULI SCHOOL, HO", 
                "MEDINA SNR. SEC.", 
                "MEM-CHEMFRE COMM. SENIOR HIGH SCHOOL", 
                "MENJI SENIOR HIGH SCHOOL", 
                "MEPE ST. KIZITO SENIOR HIGH/TECH SCHOOL", 
                "MERCY ISLAMIC SCHOOL", 
                "METHODIST GIRLS SENIOR HIGH SCHOOL, MAMFE", 
                "METHODIST HIGH SCHOOL,SALTPOND", 
                "METHODIST SENIOR HIGH SCHOOL, SEKONDI", 
                "METHODIST SENIOR HIGH/TECH", 
                "METHODIST SENIOR HIGH/TECH SCHOOL, BIADAN", 
                "METHODIST TECHNINCAL INSTITUTE", 
                "MFANTSIMAN GIRLS SENIOR HIGH SCHOOL", 
                "MFANTSIPIM SCHOOL", 
                "MIGHTY ROYAL SENIOR HIGH", 
                "MIM SENIOR HIGH SCHOOL", 
                "MINNESOTA CHRISTIAN SENIOR HIGH SCHOOL", 
                "MIRACLE SENIOR HIGH", 
                "MIRIGU COMMUNITY DAY SENIOR HIGH SCHOOL", 
                "MIST SENIOR HIGH", 
                "MIST SENIOR HIGH, DAMBAI", 
                "MODERN BUSINESS COLLEGE", 
                "MODERN SENIOR HIGH", 
                "MOKWAA SENIOR HIGH SCHOOL", 
                "MOREE COMM. SENIOR HIGH SCHOOL", 
                "MORNING STAR INT. HIGH SCHOOL", 
                "MOUNT MORIAH SENIOR HIGH", 
                "MOUNTAIN SENIOR HIGH", 
                "MOUNTAIN SENIOR HIGH, KONA", 
                "MOZANO SENIOR HIGH SCHOOL", 
                "MPAHA COMMUNITY DAY SENIOR HIGH SCHOOL", 
                "MPASATIA SENIOR HIGH/TECH SCHOOL", 
                "MPOHOR SENIOR HIGH SCHOOL", 
                "MPRAESO SENIOR HIGH SCHOOL", 
                "MPUASUMAN SENIOR HIGH", 
                "NABANGO SENIOR HIGH SCHOOL", 
                "NAFANA SENIOR HIGH SCHOOL", 
                "NAKPANDURI SENIOR HIGH SCHOOL", 
                "NALERIGU SENIOR HIGH SCHOOL", 
                "NAMONG SENIOR HIGH/TECH SCHOOL", 
                "NANA BRENTU SENIOR HIGH/TECH SCHOOL", 
                "NANANOM SENIOR HIGH", 
                "NANDOM SENIOR HIGH SCHOOL", 
                "NATIONAL SENIOR HIGH SCHOOL", 
                "NAVRONGO SENIOR HIGH SCHOOL", 
                "NAVS SENIOR HIGH/TECHNICAL", 
                "NCHUMURUMAN COMM. DAY SENIOR HIGH SCHOOL", 
                "NCHUMURUMAN PENTECOST SEC", 
                "NDEWURA JAKPA SENIOR HIGH/TECH SCHOOL", 
                "NEW ABIREM/AFOSU SENIOR HIGH SCHOOL", 
                "NEW EDUBIASE SENIOR HIGH SCHOOL", 
                "NEW JUABEN COLLEGE OF COMMERCE", 
                "NEW JUABEN SENIOR HIGH/COMM SCHOOL", 
                "NEW KROKOMPE COMM. SENIOR HIGH SCHOOL", 
                "NEW LONGORO COMM SENIOR HIGH SCHOOL (DEGA)", 
                "NEW NSUTAM SENIOR HIGH/TECH SCHOOL", 
                "NEW STAR EDUCATIONAL INSTITUTE", 
                "NGLESHIE AMANFRO SENIOR HIGH SCHOOL", 
                "NIFA SENIOR HIGH SCHOOL", 
                "NIGRITIAN COLLEGE", 
                "NINGO SENIOR HIGH SCHOOL", 
                "NKAWIE SENIOR HIGH/TECH SCHOOL", 
                "NKAWKAW SENIOR HIGH SCHOOL", 
                "NKENKANSU COMMUNITY SENIOR HIGH SCHOOL", 
                "NKONYA SENIOR HIGH SCHOOL", 
                "NKORANMAN SENIOR HIGH SCHOOL", 
                "NKORANZA SENIOR HIGH/TECH SCHOOL\"NKORANZA TECH INST.", 
                "NKRANKWANTA COMM SENIOR HIGH SCHOOL", 
                "NKROFUL AGRIC. SENIOR HIGH SCHOOL", 
                "NKWANTA COMM SENIOR HIGH SCHOOL", 
                "NKWANTA SENIOR HIGH SCHOOL", 
                "NKWATIA PRESBY SENIOR HIGH/COMM SCHOOL", 
                "NKWATIA ST. PAUL'S SENIOR HIGH SCHOOL, ASAKRAKA KWAHU", 
                "NKYERAA SENIOR HIGH SCHOOL", 
                "NOAH'S COLLEGE", 
                "NOBLE PRINCE SENIOR HIGH SCHOOL", 
                "NORO-AMEEN ISLAMIC SENIOR HIGH SCHOOL, ASEWASE", 
                "NORTH AMERICAN EDUCATIONAL CENTRE", 
                "NORTHERN SCHOOL OF BUSINESS", 
                "NORTHERN STAR SENIOR HIGH SCHOOL ", 
                "NOTRE DAME GIRLS SENIOR HIGH SCHOOL, SUNYANI", 
                "NOTRE DAME SEM/SENIOR HIGH SCHOOL, NAVRONGO", 
                "NSABA PRESBY SENIOR HIGH SCHOOL", 
                "NSAWAM SENIOR HIGH SCHOOL", 
                "NSAWKAW STATE SENIOR HIGH SCHOOL", 
                "NSAWORA EDUMAFA COMM. SENIOR HIGH SCHOOL", 
                "NSEIN SENIOR HIGH SCHOOL", 
                "NSUTAMAN CATH. SENIOR HIGH SCHOOL", 
                "NTRUBOMAN SENIOR HIGH SCHOOL", 
                "NUNGUA SENIOR HIGH SCHOOL", 
                "NYAKROM SENIOR HIGH TECH SCHOOL", 
                "NYANKUMASE AHENKRO SENIOR HIGH SCHOOL", 
                "NYINAHIN CATH. SENIOR HIGH SCHOOL", 
                "O.L.L. GIRLS SENIOR HIGH SCHOOL", 
                "OBAMA COLLEGE", 
                "OBIRI YEBOAH SENIOR HIGH/TECHNICAL SCHOOL", 
                "OBRACHIRE SENIOR HIGH/TECH SCHOOL", 
                "OBUASI SENIOR HIGH/TECH SCHOOL", 
                "ODA SENIOR HIGH SCHOOL ST. FRANCIS SENIOR HIGH/TECH SCHOOL, AKIM ODA", 
                "ODOBEN SENIOR HIGH SCHOOL", 
                "ODOMASEMAN SENIOR HIGH SCHOOL", 
                "ODORGONNO SENIOR HIGH SCHOOL ", 
                "ODUKO BOATEMAA SEN. HIGH/VOC.", 
                "ODUPONG COMM. DAY SCHOOL", 
                "OFOASE KOKOBEN SENIOR HIGH SCHOOL", 
                "OFOASE SENIOR HIGH/TECH SCHOOL", 
                "OFORI PANIN SENIOR HIGH SCHOOL", 
                "OGUAA SENIOR HIGH/TECH SCHOOL", 
                "OGYEEDOM COMM SENIOR HIGH/TECH SCHOOL", 
                "OGYEEDON SENIOR HIGH/TECHNICAL", 
                "OKADJAKROM SENIOR HIGH/TECH SCHOOL", 
                "OKOMFO ANOKYE SENIOR HIGH", 
                "OKOMFO ANOKYE SENIOR HIGH SCHOOL", 
                "OKUAPEMAN SENIOR HIGH SCHOOL", 
                "OLA GIRLS SENIOR HIGH SCHOOL, HO", 
                "OLA GIRLS SENIOR HIGH SCHOOL, KENYASI", 
                "OLISTAR SECONDARY TECHNICAL", 
                "ONWE SENIOR HIGH SCHOOL", 
                "OPOKU AGYEMAN SENIOR HIGH/TECH SCHOOL ", 
                "OPOKU WARE SENIOR HIGH SCHOOL", 
                "OPPONG MEM. SENIOR HIGH SCHOOL", 
                "O'REILLY SENIOR HIGH SCHOOL", 
                "OSEI ADUTWUM SENIOR HIGH SCHOOL", 
                "OSEI BONSU SENIOR HIGH SCHOOL", 
                "OSEI KYERETWIE SENIOR HIGH SCHOOL ", 
                "OSEI TUTU II SENIOR HIGH/TECHNICAL", 
                "OSEI TUTU SENIOR HIGH SCHOOL, AKROPONG", 
                "OSINO PRESBY SENIOR HIGH/TECH SCHOOL", 
                "OSUDOKU SENIOR HIGH/TECH SCHOOL", 
                "OTI BOATENG SENIOR HIGH SCHOOL", 
                "OTI SENIOR HIGH/TECH SCHOOL", 
                "OTUMFUO OSEI TUTU II COLLEGE", 
                "OUR LADY OF GRACE SENIOR HIGH", 
                "OUR LADY OF MERCY SENIOR HIGH SCHOOL", 
                "OUR LADY OF MOUNT CARMEL GIRLS SENIOR HIGH SCHOOL, TECHIMAN", 
                "OUR LADY OF PROVIDENCE SENIOR HIGH SCHOOL", 
                "OWERRIMAN SENIOR HIGH SCHOOL", 
                "OXFORD SENIOR HIGH", 
                "OXFORD SENIOR HIGH SCHOOL", 
                "OYOKO METHODIST SENIOR HIGH SCHOOL", 
                "PACE SENIOR HIGH", 
                "PAGA SENIOR HIGH SCHOOL", 
                "PAL LANGBINSI SENIOR HIGH TECHNICAL SCHOOL", 
                "PANK SEC/BUSINESS COLLEGE", 
                "PANK SEC/BUSINESS COLLEGE**", 
                "PARKOSO COMM. SENIOR HIGH SCHOOL", 
                "PASS INTERNATIONAL COLLEGE", 
                "PASSION SENIOR HIGH", 
                "PEACE HILL SENIOR HIGH SCHOOL", 
                "PEKI SENIOR HIGH SCHOOL", 
                "PEKI SENIOR HIGH/TECHNICAL SCHOOL", 
                "PENTECOST SENIOR HIGH SCHOOL, KOFORIDUA", 
                "PENTECOST SENIOR HIGH SCHOOL, KUMASI", 
                "PETER B. A. HOLDBROOK-SMITH ACADEMIC COMPLEX", 
                "PHILIPS COM. COLLEGE", 
                "PIINA SENIOR HIGH SCHOOL", 
                "PLACID SENIOR HIGH SCH", 
                "PONG-TAMALE SENIOR HIGH SCHOOL", 
                "POPE JOHN SENIOR HIGH & MIN. SEM. SCHOOL, KOFORIDUA", 
                "POTSIN T.I. AHM. SENIOR HIGH SCHOOL", 
                "PRAMPRAM SENIOR HIGH SCHOOL", 
                "PRANG SENIOR HIGH", 
                "PRECIOUS JEWEL SENIOR HIGH SCHOOL", 
                "PREMPEH COLLEGE", 
                "PRESBY BOYS SENIOR HIGH SCHOOL, LEGON", 
                "PRESBY SEC. SCHOOL", 
                "PRESBY SEC/COMM. SCH., NUNGUA", 
                "PRESBY SENIOR HIGH SCHOOL, BEGORO", 
                "PRESBY SENIOR HIGH SCHOOL, MAMPONG AKWAPIM", 
                "PRESBY SENIOR HIGH SCHOOL, OSU", 
                "PRESBY SENIOR HIGH SCHOOL, SUHUM", 
                "PRESBY SENIOR HIGH SCHOOL, TAMALE", 
                "PRESBY SENIOR HIGH SCHOOL, TEMA", 
                "PRESBY SENIOR HIGH SCHOOL, TESHIE", 
                "PRESBY SENIOR HIGH/COMMERCIAL SCHOOL", 
                "PRESBY SENIOR HIGH/TECH SCHOOL, ABURI", 
                "PRESBY SENIOR HIGH/TECH SCHOOL, ADUKROM", 
                "PRESBY SENIOR HIGH/TECH SCHOOL, KWAMANG", 
                "PRESBY SENIOR HIGH/TECH SCHOOL, LARTEH", 
                "PRESBY-DUTCH SENIOR HIGH", 
                "PRESBYTERIAN SENIOR HIGH/TECH", 
                "PRESDEL COLLEGE", 
                "PRESET PACESETTERS INSTITUTE", 
                "PRESTEA SENIOR HIGH/TECH SCHOOL", 
                "PRINCE BOATENG MEM, SNR. HIGH", 
                "PRINCE OF PEACE GIRLS", 
                "PURPOSE GIRLS' CHRISTIAN INT.HIGH SCH.", 
                "PUSIGA SENIOR HIGH", 
                "QUEEN OF PEACE SENIOR HIGH SCHOOL, NADOWLI ", 
                "QUEENS GIRLS' SENIOR HIGH SCHOOL, SEFWI AWHIASO", 
                "REV. JOHN TEYE MEM.INST.", 
                "ROCK FOUNDATION SENIOR HIGH SCHOOL", 
                "ROYAL MAJESTY COLLEGE", 
                "S.D.A SENIOR HIGH SCHOOL, KOFORIDUA", 
                "S.D.A SENIOR HIGH SCHOOL, SUNYANI", 
                "S.D.A. SENIOR HIGH SCHOOL, AGONA", 
                "S.D.A. SENIOR HIGH SCHOOL, BEKWAI", 
                "S.D.A. SENIOR HIGHSCHOOL, AKIM SEKYERE", 
                "S.M.A SENIOR HIGH SCHOOL", 
                "SABOBA E.P. SENIOR HIGH SCHOOL", 
                "SABRONUM METHODIST SENIOR HIGH/TECH SCHOOL", 
                "SACRED HEART SENIOR HIGH SCHOOL, NSOATRE", 
                "SACRED HEART TECH. INST.", 
                "SAINT FRANCIS XAVIER SENIOR HIGH", 
                "SAKAFIA ISLAMIC SENIOR HIGH SCHOOL", 
                "SAKOGU SENIOR HIGH/TECH SCHOOL", 
                "SALAGA SENIOR HIGH SCHOOL", 
                "SALAGA T.I. AHMAD SENIOR HIGH SCHOOL", 
                "SALEM SENIOR HIGH", 
                "SALVATION ARMY SENIOR HIGH SCHOOL, ABOABO DORMAA", 
                "SALVATION ARMY SENIOR HIGH SCHOOL, AKIM WENCHI", 
                "SAMMO SEC/COMM/TECH SCH", 
                "SAMTET OXFORD SENIOR HIGH", 
                "SAMUEL OTU PRESBY SENIOR HIGH SCHOOL", 
                "SANDEMA SENIOR HIGH SCHOOL", 
                "SANDEMA SENIOR HIGH/TECH SCHOOL", 
                "SANG COMM. DAY SCHOOL", 
                "SANITY SENIOR HIGH", 
                "SANKOR COMM. DAY SENIOR HIGH SCHOOL", 
                "SANKORE SENIOR HIGH SCHOOL YAMFO", 
                "SANTA MARIA SENIOR HIGH", 
                "SAVELUGU SENIOR HIGH SCHOOL", 
                "SAVIOUR SENIOR HIGH SCHOOL, OSIEM", 
                "SAWLA SENIOR HIGH SCHOOL", 
                "SDA SENIOR HIGH", 
                "SDA SENIOR HIGH", 
                "SEA SENIOR HIGH SCHOOL", 
                "SEFAH BONSU ROYAL SENIOR", 
                "SEFWI BEKWAI SENIOR HIGH SCHOOL", 
                "SEFWI-WIAWSO SENIOR HIGH SCHOOL", 
                "SEFWI-WIAWSO SENIOR HIGH/TECH SCHOOL", 
                "SEIBEL AND BAKER COLLEGE", 
                "SEKONDI COLLEGE", 
                "SEKYEDUMASE SENIOR HIGH/TECH SCHOOL", 
                "SENYA SENIOR HIGH SCHOOL", 
                "SERWAA KESSE GIRLS SENIOR HIGH SCHOOL", 
                "SERWAAH NYARKO GIRLS' SENIOR HIGH SCHOOL", 
                "SEVEN GREAT PRINCESS ACADEMY", 
                "SHAMA SENIOR HIGH SCHOOL", 
                "SHIA SENIOR HIGHTECHNICAL SCHOOL", 
                "SIDDIQ SENIOR HIGH SCHOOL", 
                "SIMMS SENIOR HIGH/COM. SCHOOL ", 
                "SIRIGU INTEGRATED SENIOR HIGH SCHOOL", 
                "SNAPPS COLL OF ACCT SEC", 
                "SOCIAL ADVANCED INSTITUTE", 
                "SOE SENIOR HIGH", 
                "SOGAKOPE SENIOR HIGH SCHOOL", 
                "SOKODE SENIOR HIGH/TECH SCHOOL", 
                "SOMANYA SEC/TECH SCH.", 
                "SOMBO SENIOR HIGH", 
                "SOMBO SENIOR HIGH SCHOOL", 
                "SOME SENIOR HIGH SCHOOL", 
                "SPIRITAN SENIOR HIGH SCHOOL ", 
                "SPRINGBOARD INTERNATIONAL COLLEGE", 
                "ST. AGATHA SENIOR HIGH/ADVANCE COLLEGE", 
                "ST. ANDREW'S SENIOR HIGH", 
                "ST. ANDREW'S SENIOR HIGH", 
                "ST. ANDREW'S SENIOR HIGH", 
                "ST. ANDREW'S SENIOR HIGH", 
                "ST. ANGLICAN SEC.", 
                "ST. ANNE'S GIRL'S SENIOR HIGH, DAMONGO", 
                "ST. ANN'S GIRLS SENIOR HIGH SCHOOL, SAMPA", 
                "ST. ANTHONY OF PADUA SENIOR HIGH/TECH SCHOOL", 
                "ST. ANTHONY SENIOR HIGH SCHOOL", 
                "ST. AUGUSTINE SENIOR HIGH SCHOOL, NSAPOR- BEREKUM", 
                "ST. AUGUSTINE SENIOR HIGH/TECH SCHOOL, SAAN CHARIKPONG", 
                "ST. AUGUSTINE'S COLLEGE, CAPE COAST", 
                "ST. AUGUSTINE'S SENIOR HIGH SCHOOL, BOGOSO", 
                "ST. BASILIDES VOC./TECH. INST.", 
                "ST. BENEDICT SENIOR HIGH", 
                "ST. BERNADETTES TECH/VOC. INST.", 
                "ST. BROTHER ANDRE SENIOR HIGH", 
                "ST. CATHERINE GIRLS SENIOR HIGH SCHOOL", 
                "ST. CHARLES SENIOR HIGH SCHOOL, TAMALE", 
                "ST. CYPRIAN MINOR SEMINARY SENIOR HIGH SCHOOL", 
                "ST. DANIEL COMBONI TECH/VOC INST.", 
                "ST. DOMINIC'S SENIOR HIGH/TECH SCHOOL, PEPEASE", 
                "ST. FIDELIS SENIOR HIGH/TECH SCHOOL", 
                "ST. FRANCIS GIRLS SENIOR HIGH SCHOOL, JIRAPA", 
                "ST. FRANCIS SEMINARY/SENIOR HIGH SCHOOL, BUOYEM", 
                "ST. GEORGE'S SENIOR HIGH TECH SCHOOL", 
                "ST. GREGORY CATHOLIC SENIOR HIGH SCHOOL", 
                "ST. GREGORY CATHOLIC SENIOR HIGH SCHOOL", 
                "ST. HUBERT SEM/SENIOR HIGH SCHOOL, KUMASI", 
                "ST. JAMES BUSINESS COLLEGE", 
                "ST. JAMES SEM & SENIOR HIGH SCHOOL, ABESIM", 
                "ST. JEROME SENIOR HIGH SCHOOL, ABOFOUR", 
                "ST. JOHN'S GRAMMAR SENIOR HIGH SCHOOL ", 
                "ST. JOHN'S INTEGRATED SENIOR HIGH/TECH SCHOOL", 
                "ST. JOHN'S SENIOR HIGH SCHOOL, SEKONDI", 
                "ST. JOSEPH SEM/SENIOR HIGH SCHOOL, MAMPONG", 
                "ST. JOSEPH SENIOR HIGH SCHOOL, SEFWI WIAWSO", 
                "ST. JOSEPH SENIOR HIGH/TECH SCHOOL, AHWIREN", 
                "ST. JOSEPH TECH. COM. SCH.", 
                "ST. JOSEPH'S TECH. INST.", 
                "ST. JOSEPH'S TECH. INST.", 
                "ST. LOUIS SENIOR HIGH SCHOOL, KUMASI", 
                "ST. MARGARET MARY SENIOR HIGH/TECH SCHOOL", 
                "ST. MARTIN'S SENIOR HIGH SCHOOL, NSAWAM", 
                "ST. MARY'S BOYS' SENIOR HIGH SCHOOL, APOWA", 
                "ST. MARY'S GIRL'S SENIOR HIGH, KONONGO", 
                "ST. MARY'S SEM.& SENIOR HIGH SCHOOL, LOLOBI", 
                "ST. MARY'S SENIOR HIGH SCHOOL, KORLE", 
                "ST. MARY'S VOC./TECH. INST.", 
                "ST. MICHAEL TECH/VOC INST", 
                "ST. MICHAEL'S SENIOR HIGH SCHOOL, AHENKRO", 
                "ST. MICHAEL'S SENIOR HIGH SCHOOL, AKOASE (NKAWKAW)", 
                "ST. MONICA'S SENIOR HIGH SCHOOL, MAMPONG ", 
                "ST. PAUL CATHOLIC SNR. HIGH SCHOOL", 
                "ST. PAUL'S SENIOR HIGH SCHOOL, DENU", 
                "ST. PAUL'S TECH. SCHOOL", 
                "ST. PETER'S MISSION SCHOOLS", 
                "ST. PETER'S SENIOR HIGH SCHOOL,", 
                "ST. PROSPER COLLEGE", 
                "ST. ROSE'S SENIOR HIGH SCHOOL, AKWATIA", 
                "ST. SEBASTIAN CATH. SENIOR HIGH SCHOOL", 
                "ST. STEPHEN'S PRESBY SENIOR HIGH/TECH SCHOOL, ASIAKWA", 
                "ST. THOMAS AQUINAS SENIOR HIGH SCHOOL, CANTOMENTS", 
                "ST. THOMAS SENIOR HIGH/TECH SCHOOL ", 
                "ST.JOHN'S VOC. TECH. INSTITUTE", 
                "STANDARD SENIOR HIGH", 
                "STARTRITE CHRISTIAN COLLEGE", 
                "SUCCESS CITY SENIOR HIGH", 
                "SUCCESS SENIOR HIGH", 
                "SUHUM SENIOR HIGH/TECH SCHOOL", 
                "SUMAMAN SENIOR HIGH SCHOOL", 
                "SUNYANI BUSINESS SEC", 
                "SUNYANI METHODIST TECHNICAL INST.", 
                "SUNYANI SENIOR HIGH SCHOOL", 
                "SUPREME COLLEGE", 
                "SWEDRU SCH. OF BUSINESS", 
                "SWEDRU SENIOR HIGH SCHOOL", 
                "T. I. AHMADIYYA GIRL'S SENIOR HIGH SCHOOL, ASOKORE", 
                "T. I. AHMADIYYA SENIOR HIGH SCHOOL, KUMASI", 
                "T. I. AHMADIYYA SENIOR HIGH SCHOOL, WA", 
                "TAKORADI SENIOR HIGH SCHOOL", 
                "TAKORADI TECH. INST.", 
                "TAKPO SENIOR HIGH SCHOOL", 
                "TALENTS RESTORATION SENIOR HIGH", 
                "TAMALE GIRLS SENIOR HIGH SCHOOL", 
                "TAMALE INTERNATIONAL SENIOR HIGH SCHOOL", 
                "TAMALE SENIOR HIGH SCHOOL", 
                "TAMALE TECHNICAL INSTITUTE", 
                "TANYIGBE SENIOR HIGH SCHOOL", 
                "TAPAMAN SENIOR HIGH/TECH SCHOOL", 
                "TARKROSI COMM. SENIOR HIGH SCHOOL", 
                "TARKWA SENIOR HIGH SCHOOL", 
                "TAVIEFE COMM. SENIOR HIGH SCHOOL", 
                "TEACHERS' SENIOR HIGH SCH", 
                "TECHIMAN SENIOR HIGH SCHOOL", 
                "TEMA HIGH SCHOOL", 
                "TEMA MANHEAN SENIOR HIGH/TECH SCHOOL", 
                "TEMA METH. DAY SENIOR HIGH SCHOOL", 
                "TEMA SENIOR HIGH SCHOOL", 
                "TEMA TECH. INST.", 
                "TEMPANE SENIOR HIGH SCHOOL", 
                "TEOGO SENIOR HIGH", 
                "TEPA SENIOR HIGH SCHOOL", 
                "TERCHIRE SENIOR HIGH SCHOOL", 
                "TESHIE ST. JOHN SENIOR HIGH SCH.", 
                "TESHIE TECH. INSTITTUTE", 
                "THE GOLDEN SUNBEAM COL. OF SC. & TECH.", 
                "THE LINCOLN SCHOOL", 
                "THE MASTERS SENIOR HIGH SCHOOL", 
                "THE ROYALS' SENIOR HIGH SCHOOL", 
                "THEOCRACY SENIOR HIGH", 
                "THREE TOWN SENIOR HIGH SCHOOL", 
                "TIJJANIYA SENIOR HIGH SCHOOL", 
                "TOASE SENIOR HIGH SCHOOL", 
                "TOLON SENIOR HIGH SCHOOL", 
                "TONGO SENIOR HIGH/TECH SCHOOL", 
                "TONGOR SENIOR HIGH TECH SCHOOL", 
                "TOP ACCOUNTANCY & SECRETARYSHIP", 
                "TSIAME SENIOR HIGH SCHOOL", 
                "TSITO SENIOR HIGH/TECH SCHOOL", 
                "TUMU SENIOR HIGH/TECH SCHOOL", 
                "TUNA SENIOR HIGH/TECH SCHOOL", 
                "TUOBODOM SENIOR HIGH/TECH SCHOOL", 
                "TUPASO SENIOR HIGH", 
                "TWEAPEASE SENIOR HIGH SCHOOL", 
                "TWENE AMANFO SENIOR HIGH/TECH SCHOOL", 
                "TWENEBOA KODUA SENIOR HIGH SCHOOL", 
                "TWIFO HEMANG SENIOR HIGH/TECH SCHOOL", 
                "TWIFO PRASO SENIOR HIGH SCHOOL", 
                "TWUMASI BOATENG SENIOR HIGH/TECHNICAL", 
                "ULLO SENIOR HIGH SCHOOL", 
                "UNCLE RICH SENIOR HIGH", 
                "UNIVERSAL GIRLS SENIOR HIGH", 
                "UNIVERSITY PRACTICE SENIOR HIGH SCHOOL", 
                "UNIVERSITY TUTORIAL SEC.SCHOOL", 
                "UTHMAN BIN AFAM SENIOR HIGH SCHOOL", 
                "UTHMANIYA SENIOR HIGH SCHOOL, TAFO", 
                "VAKPO SENIOR HIGH SCHOOL", 
                "VAKPO SENIOR HIGH/TECH SCHOOL", 
                "VE COMM. SENIOR HIGH SCHOOL", 
                "VICAR TRUST SENIOR HIGH SCHOOL", 
                "VICTORIA COLLEGE", 
                "VICTORY HIGH SCHOOL", 
                "VILAC INTERNATIONAL SCHOOL", 
                "VISION OBAMA SENIOR HIGH", 
                "VITTORIA BUSINESS COLLEGE, YEJI", 
                "VOLO COMM. SENIOR HIGH SCHOOL", 
                "VOLTA SENIOR HIGH SCHOOL", 
                "VOLTA TECH INST", 
                "W.B.M. ZION SENIOR HIGH SCHOOL, OLD TAFO", 
                "WA SENIOR HIGH SCHOOL", 
                "WA SENIOR HIGH/TECH SCHOOL", 
                "WA TECH. INST.", 
                "WALEWALE SENIOR HIGH SCHOOL", 
                "WALEWALETECH/ VOC INST.", 
                "WAMANAFO SENIOR HIGH/TECH SCHOOL", 
                "WAPULI COMM. SENIOR HIGH SCHOOL", 
                "WENCHI METH. SENIOR HIGH SCHOOL", 
                "WESLEY GIRLS SENIOR HIGH SCHOOL, CAPE COAST", 
                "WESLEY GRAMMAR SENIOR HIGH SCHOOL", 
                "WESLEY HIGH SCHOOL, BEKWAI", 
                "WESLEY SENIOR HIGH SCHOOL, KONONGO", 
                "WEST AFRICA SENIOR HIGH SCHOOL", 
                "WESTERN SENIOR HIGH/TECH", 
                "WESTPHALIAN SENIOR HIGH", 
                "WETA SENIOR HIGH/TECH SCHOOL", 
                "WIAFE AKENTEN PRESBY SENIOR HIGH SCHOOL", 
                "WIAGA COMM. SENIOR HIGH SCHOOL", 
                "WINNEBA SCH OF BUSINESS", 
                "WINNEBA SENIOR HIGH SCHOOL", 
                "WINNERS SENIOR HIGH", 
                "WIREDU BREMPONG SENIOR HIGH/TECH", 
                "WITSAND SENIOR HIGH", 
                "WORAWORA SENIOR HIGH SCHOOL", 
                "WOVENU SENIOR HIGH TECHNICAL SCHOOL", 
                "WULENSI SENIOR HIGH SCHOOL", 
                "WULUGU SENIOR HIGH SCHOOL", 
                "YAA ASANTEWAA GIRLS SENIOR HIGH SCHOOL", 
                "YABRAM COMM. DAY SCHOOL", 
                "YAGABA SENIOR HIGH SCHOOL", 
                "YEBOAH ASUAMAH SENIOR HIGH SCHOOL", 
                "YEJI SENIOR HIGH/TECH SCHOOL", 
                "YENDI SENIOR HIGH SCHOOL", 
                "YILO KROBO SENIOR HIGH/COMM SCHOOL", 
                "ZABZUGU SENIOR HIGH SCHOOL", 
                "ZAMSE SENIOR HIGH/TECH SCHOOL", 
                "ZEBILLA SENIOR HIGH/TECH SCHOOL", 
                "ZIAVI COMMUNITY SENIOR HIGH", 
                "ZION SENIOR HIGH SCHOOL", 
                "ZION SENIOR HIGH SCHOOL, TAMALE", 
                "ZIOPE SENIOR HIGH SCHOOL", 
                "ZORKOR SENIOR HIGH SCHOOL", 
                "ZUARUNGU SENIOR HIGH SCHOOL")
    }

}