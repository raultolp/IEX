### TODO LIST
-----------
#### 1. Priority
-----------

-----------
#### 2. Priority
-----------
- serveris kõikide logimist vajavate tegevuste logimine ühte faili
- menüü süsteem lugeda failist (JSON vms)
- aktsiate nimekiri lugeda failist

-----------
#### 3. Priority
-----------
- Menüü valik: Stock technical analysis
- Top võrdlus üldise turu indeksiga või SPY-ga

--------------------------
#### Lisaideed edasiarneduseks:
--------------------------

1. Kliendi UI:
    - 0. variant - Retro tekst versioon .. vähemalt toimib :)
    - 1. variant - Java FX - katsetatud, keerukas, ilus, seni parim lahendus, TabelView testitud
    - 2. variant - Swing - katsetatud, lihtsam tabeleid teha, kuid vana ja kole ning ei soovitata kasutada, Java FX-ga sidumine keerukas - jääb ära
    - 3. variant - sügisel aine veebirakenduste loomine, saaks teha veebibrauseri põhise lahenduse, nõuab HTML, XML, CSS, JavaScript, PHP jne tundmist
2. Aktsiatega kauplemine:
    - Päevakauplemine Margin = 2x vaba raha + intress margini kasutamise eest.
    - Short selling + päeva intress selle eest
    - Aktsiate nimekirja pikendada, tuua sisse ka peamised ETF-d (SPY jne) ja mitmekordse võimendusega ETF-d, ETN-d
    - ostu-müügitehingute põhjalikum analüüs, kust raha tuli/kadus ja kui suured on tehingutasud, intressid
    - portfelli võrdlus keskmise aktsiaturu ja erinevate sektorite indeksite võrdluses
    - LMT BUY - osta aktsia hind mingi kindla hinnaga, mis on madalam hetkehinnast, ostuorder peaks kehtima N (kasutaja poolt määratud arvu) päevi ja süsteem peaks ise ostma kui hind on sobilik
    - LMT SELL - samas asi kas portfellis oleva aktsia müümiseks või aktsia shortimiseks
    - STOP SELL - kui aktsia hind langeb alla mingit hinda ja närvid täitsa läbi, siis müü automaatselt
    - STOP BUY - sama, mis SELL, kuid toimib aktsia Shortimisel
    - TECHNICAL ANALYSIS: aktsia kohta tehniliste näitajate küsimine. Näit moving average, MACD, RSI, Stoh jne. Google "stock technical analysis" aitab täpseid valemeid leida. Võib programmi nende arvutamise lisada või küsida IEX-st kui saab.
    - TECHNICAL ANALYSIS 2: kui mingi näitaja väärtus jõuab sellisele tasemele, siis osta mulle aktsiaid / saada mulle email vms

-----------
#### Code Review
-----------
1. versiooni code review: https://docs.google.com/document/d/1V0i4ERb-X_gyJ2nOwEq_U6HcQoKT2cRasGIIky2rlv4/edit

--------
#### TESTIDA: TODO
--------
- Kasutajanimeks sobib tühik
- Kui käsuks panna numbri asemel tühik, jookseb client kokku.
- View Company name data, kui panna sinna vale nimi, siis viskab automaatselt algusesse, aga võiks olla proovi uuesti (lisaks “kas te mõtlesite…”).
- Peale kasutaja kustutamist, ei kustu kasutaja ära.
- Väike asi, mis häirib: kui ma vajutasin, et tahan näiteks aktsiat osta, aga mõtlen ümber, siis mingi käsuga võiks saada tagasi minna.
- Hetkel on võimalik luua mitu sama nimega kasutajat jooksutades korraga mitut klienti enne mängu salvestamist, kusjuures mängu uuesti laadimisel on jäetud sama nimega kasutajatest alles vaid see, kellel on kõige vähem raha (võis olla juhuste kokkusattumus, et alati see mul alles jäi :( )
  Mingi meetod juba on olemas selleks, aga kasutamata, nii et tundub, et sellele probleemile on ka juba peaaegu lahendus olemas. Serverist mängides on see meetod kasutusel ka juba

