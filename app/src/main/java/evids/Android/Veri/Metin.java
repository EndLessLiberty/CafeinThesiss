package evids.Android.Veri;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Kişisel on 01.10.2017.
 */

public class Metin {
    public static String birlestir(String... kMetinler){
        String mSonucMetni="";
        for (String mMetin: kMetinler)
            mSonucMetni+=mMetin;
        return mSonucMetni;
    }
    public static String birlestir(Object... kNesneler){
        String mSonucMetni="";
        for (Object nNesne: kNesneler)
            mSonucMetni+=String.valueOf(nNesne);
        return mSonucMetni;
    }
    public static String paraMetniUret(float oMiktar){
        return paraMetniUret((double) oMiktar);
    }
    public static String paraMetniUret(long tMiktar){
        return paraMetniUret((double) tMiktar);
    }
    public static String paraMetniUret(int tMiktar){
        return paraMetniUret((double) tMiktar);
    }
    public static String paraMetniUret(BigDecimal oMiktar){
        return paraMetniUret(oMiktar.doubleValue());
    }
    public static String paraMetniUret(double oMiktar){
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("tr","TR"));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        return "₺"+nf.format(oMiktar).replaceAll("\\s","");
    }
}
