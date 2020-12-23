# Turkish-Car-Plate-TextWatcher

This library add an TextWatcher to EditText for Turkish car plates.

The rules are written according to this text:

İstanbul Emniyet Müdürlüğü Trafik Tescil Şube Müdürlüğü’nden alınan bilgiye göre;
ilde halen toplam 5 milyon kapasiteye sahip çift harf ve 3 rakam, tek harf ve 4 rakam, 3 harf ve 2 rakam, çift harf ve 4 rakam ile özel isimlerden oluşan plakalar kullanılıyor.


### Sample usage:

        val editText = findViewById<EditText>(R.id.myEditText)
        val button = findViewById<Button>(R.id.myButton)

        val carPlateWatcher = PlateTextWatcher(editText)

        button.setOnClickListener {
            val validationResult = if (carPlateWatcher.isPlateValid) {
                "It is valid"
            } else {
                "It is not valid"
            }
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show()
        }

You can update max city count for future:
   
      carPlateWatcher.updateMaxCityCount(82)
  
  
Also you can add forbidden word list and catch function for taking action;

     carPlateWatcher.forbiddenWordList = arrayListOf("AAA", "BBB", "CCC")
     carPlateWatcher.onForbiddenWordEnteredFunction = {
         // Take some actions
     }
  
