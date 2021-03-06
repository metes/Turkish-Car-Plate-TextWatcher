package com.platetextwatch

import android.widget.EditText



class PlateTextWatcher : TextViewListener() {

    private var wasEdited = false
    private var editText: EditText? = null
    private var maxCityCount: Int = 81

    private val enteredText get() = editText?.text.toString()

    private val isLengthLimitReached: Boolean get() {
        val lettersLength = getLetters()?.length?: 0
        val lastDigitsLength = getLastDigits()?.length?: 0

        return when (lettersLength) {
            1 -> lastDigitsLength >= 4
            2 -> lastDigitsLength >= 4
            3 -> lastDigitsLength == 2
            else -> false
        }
    }

    fun adTextWatcher(editText: EditText?) {
        this.editText = editText
        this.editText?.addTextChangedListener(this)
    }

    /**
     *  Future-proof :)
     */
    fun updateMaxCityCount(count: Int) {
        this.maxCityCount = count
    }

    /*
        İstanbul Emniyet Müdürlüğü Trafik Tescil Şube Müdürlüğü’nden alınan bilgiye göre,
        ilde halen toplam 5 milyon kapasiteye sahip çift harf ve 3 rakam, tek harf ve 4 rakam,
        3 harf ve 2 rakam, çift harf ve 4 rakam ile özel isimlerden oluşan plakalar kullanılıyor.
     */
    fun isPlateValid(): Boolean {
        val letters = getLetters()
        val lastDigitsLength = getLastDigits()?.length?: 0

        return when (letters?.length) {
            1 -> lastDigitsLength == 4
            2 -> lastDigitsLength == 3 || lastDigitsLength == 4
            3 -> lastDigitsLength == 2
            else -> false
        }
    }

    private fun getLetters(): String? {
        val isContains2Spaces = enteredText.filter { it == ' ' }.length == 2
        if (!isContains2Spaces) {
            return null
        }
        val letters = enteredText.substring(3)
        return letters.substring(0, letters.indexOfFirst { it == ' ' })
    }

    private fun getLastDigits(): String? {
        var letters: String? = null
        getLetters()?.let { it ->
            letters = enteredText.substring(3 + enteredText.indexOf(it))
        }
        return letters
    }

    private fun convertText(plateText: String, isDelete: Boolean): CharSequence? {
        return try {
            var newText = plateText
            // ilk iki karakteri (il kodu) sayi olmasi icin zorla
            if (newText.length < 3) {
                if (!isFullNumber(newText)) {
                    return removeLast(newText)
                }
                if (!isCityCodeValid(newText)) {
                    return removeLast(newText)
                }
                // ilkodu sayi ise 3. karaktere bosluk ekle
                if (newText.length == 2) {
                    newText = "$newText "
                }
            }
            if (newText.length == 3 && !newText.contains(" ") && !isDelete) {
                return newText.substring(0, 2) + " " +
                        if (!isFullNumber(newText[2].toString())) newText[2].toString() else ""
            }
            if (newText.length == 3 && isDelete) {
                return newText.substring(0, 2)
            } else if (newText.length == 4) {
                // 4. karakteri harf olmasi icin zorla
                if (isFullNumber(newText[3].toString())) {
                    return removeLast(newText)
                }
            } else if (newText.length > 4) {
                // Orta (harflerin yer aldigi) bolumde mi?
                if (!haveTwoSpace(newText)) {
                    // Orta bolumde 3'den fazla harf olamaz
                    val letterSequence = newText.substring(newText.indexOf(" "))
                    if (letterSequence.length > 4) {
                        if (isDelete || !isFullNumber(getLastChar(newText))) {
                            newText = removeLast(newText)
                        }
                    }
                    // 2. bolumde rakam girildi ise 3. bolumu olusturmak icin rakamin onune bosluk ekle
                    val lastCharIndex = getFirstCharIndexOfLastPart(newText)
                    if (lastCharIndex > 0) {
                        return newText.substring(0, newText.length - 1) + " " + getLastChar(newText)
                    }
                } else {
                    // ikinci boslugu siliyor ise
                    val isDeletingSecondSpace = getLastChar(newText) == " " && isDelete
                    // 2 bosluk varsa(3. bolumde ise)
                    if (!isFullNumber(newText.substring(newText.lastIndexOf(' ')).replace(" ", ""))
                        || isDeletingSecondSpace
                    ) {
                        return removeLast(newText)
                    }
                }
            }
            if (isLengthLimitReached) {
                return removeLast(newText)
            }
            newText
        } catch (e: Exception) {
            e.printStackTrace()
            plateText
        }
    }

    private fun isCityCodeValid(newText: String): Boolean {
        val cityCode = newText.toInt()
        return cityCode in 1..maxCityCount
    }

    private fun getLastChar(newText: String): String {
        return newText.substring(newText.length - 1)
    }

    private fun getFirstCharIndexOfLastPart(text: String): Int {
        var fistCharIndexOfLastPart = 0
        text.substring(2).forEachIndexed { index, c ->
            if (isFullNumber(c.toString())) {
                fistCharIndexOfLastPart = index
                return@forEachIndexed
            }
        }
        return fistCharIndexOfLastPart
    }

    private fun haveTwoSpace(text: String): Boolean {
        var count = 0
        text.forEach { if (it == ' ') count++ }
        return count > 1
    }

    private fun removeLast(text: String): String {
        return text.substring(0, text.length - 1)
    }

    override fun onTextChanged(before: String, old: String, aNew: String, after: String) {
        if (wasEdited) {
            wasEdited = false
            return
        }

        val newValue = convertText(enteredText, old.isNotEmpty() && aNew.isEmpty())
        // don't get trap into infinite loop
        wasEdited = true
        // just replace entered value with whatever you want
        editText?.setText(newValue)
        editText?.setSelection(newValue?.length ?: 0)
    }

    private fun isFullNumber(text: String): Boolean {
        text.toCharArray().forEach {
            if (!Character.isDigit(it)) {
                return false
            }
        }
        return true
    }

}

