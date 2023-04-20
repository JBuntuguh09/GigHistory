package com.lonewolf.pasco.resources

import com.lonewolf.pasco.resources.ShortCut_To
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.lonewolf.pasco.R
import java.lang.Exception
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
    val categoryName: List<String>
        get() {
            val list: MutableList<String> = ArrayList()
            list.add("Harmonies")
            list.add("Beats")
            list.add("Instrumentals")
            list.add("Vocals")
            list.add("Educational")
            list.add("New Release")
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
            return mutableListOf("Historiography and Historical Skills",
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

    fun getInstructions(num : Int) : String{
        val list =  mutableListOf("All Questions are compulsory to attempt. \n" +
                "\n" +
                "For each correct answer, you get 3 points.\n" +
                "\n" +
                "You have 3 life lines to help you answer the questions correctly.\n" +
                "\n" +
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - 50/50 - Two wrong answers will be removed leaving the correct answer and a wrong answer \n"+
                "\n"+
                " - Popular Choice - The ratio of selected answers from previous tries will be shown in a graph.\n"+
                "\n"+
                "You start all over again once you miss a question.\n" +
                "\n" +
                "Good Luck!",
        "All Questions are compulsory to attempt.\n" +
                "\n"+
                "For each correct answer, you get 3 points.\n" +
                "\n" +
                "You have 2 life lines to help you answer the questions correctly.\n" +
               "\n" +
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - Hint - You are given the definition of the word\n\n"+
               "You start all over again once you miss a question.+\n\n" +
                "Good Luck!",
        "All Questions are compulsory to attempt. \n\n" +
                "Enter the term who best describes the definition \n\n"+
               "For each correct answer, you get 3 points.\n\n" +
                "You have 2 life lines to help you answer the questions correctly.\n\n" +
                " - WhatsApp friend - Send the question to a friend on Whatsapp. Timer will be paused for 1 minute.\n"+
                "\n"+
                " - Hint - You are given the word with some letters missing\n\n"+
                "You start all over again once you miss a question.\n\n" +
                "Good Luck!",
        "All Questions are compulsory to attempt. \n\n" +
                "This is a combination of Objective Quiz, "+
                "For each correct answer, you get 3 points.\n\n" +
                "You have 3 life lines to help you answer the questions correctly.\n\n" +
                "You start all over again once you miss a question.\n\n" +
                "Good Luck!",
        "")

        return list[num]
    }

}