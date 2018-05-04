package evids.Android.Veri;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
public class EtkinlikMerkezi {

    private static ArrayList<View> birlesenler=new ArrayList<View>();
    private static ArrayList<Demet<View, ArrayList<Object>>> etkinlikBirlesenDemetleri=new ArrayList<Demet<View, ArrayList<Object>>>();
    private static ArrayList<Demet<String, Object>> kaydedilmisEtkinlikler=new ArrayList<Demet<String, Object>>();

    private static Demet<View, ArrayList<Object>> dizideDemetiBulYoksaOlustur(View nBirlesen){
        for(Demet<View, ArrayList<Object>> nDemet:etkinlikBirlesenDemetleri){
            if(nDemet.x.equals(nBirlesen))
                return nDemet;
        }
        etkinlikBirlesenDemetleri.add(new Demet<View, ArrayList<Object>>(nBirlesen,new ArrayList<Object>()));
        return etkinlikBirlesenDemetleri.get(etkinlikBirlesenDemetleri.size()-1);
    }
    private static Demet<String, Object> kaydedilmisEtkinliklerdeBul(String mAd){
        for(Demet<String, Object> nDemet:kaydedilmisEtkinlikler) {
            if(nDemet.x.equals(mAd))
                return nDemet;
        }
        return null;
    }


    private static void etkinlikAta(View nBirlesen, Object nEtkinlik){
        Demet<View, ArrayList<Object>> nDemet=dizideDemetiBulYoksaOlustur(nBirlesen);
        if(nDemet.y.indexOf(nEtkinlik)<0)
            nDemet.y.add(nEtkinlik);
    }
    public static void bagla(@NonNull View nBirlesen, @NonNull String mAd){
        Demet<String, Object> nEtkinlik=kaydedilmisEtkinliklerdeBul(mAd);
        if(nEtkinlik!=null) {
            if(nEtkinlik.y instanceof View.OnTouchListener){
                nBirlesen.setOnTouchListener(temelDokunmaOlayi);
            }else if(nEtkinlik.y instanceof View.OnClickListener){
                nBirlesen.setOnClickListener(temelTiklanmaOlayi);
            }else if(nEtkinlik.y instanceof View.OnFocusChangeListener){
                nBirlesen.setOnFocusChangeListener(temelOdaktaliginDegismesiOlayi);
            }else{
                return;
            }
            etkinlikAta(nBirlesen, nEtkinlik.y);
        }
    }
    public static void ayir(@NonNull View nBirlesen, @NonNull Object nEtkinlik){
        Demet<View, ArrayList<Object>> nDemet=dizideDemetiBulYoksaOlustur(nBirlesen);
        if(nDemet.y.indexOf(nEtkinlik)>-1)
            nDemet.y.remove(nEtkinlik);
    }

    public static void kaydet(@NonNull String mAd, @NonNull Object nEtkinlik) {

        if(kaydedilmisEtkinliklerdeBul(mAd)==null)
            kaydedilmisEtkinlikler.add(new Demet<String, Object>(mAd, nEtkinlik));
    }
    public static void kaldir(@NonNull String mAd){
        Demet<String, Object> nDemet=kaydedilmisEtkinliklerdeBul(mAd);
        if(nDemet!=null)
            kaydedilmisEtkinlikler.remove(nDemet);
    }
    public static Object bul(@NonNull String mAd) {
        Demet nDemet=kaydedilmisEtkinliklerdeBul(mAd);
        return (nDemet==null)?null:nDemet.y;
    }


    // region Tıklanma Etkinliği
    private static View.OnClickListener temelTiklanmaOlayi=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EtkinlikMerkezi.tiklandi(v);
        }
    };

    public static void bagla(View nBirlesen, View.OnClickListener nEtkinlik){
        if(nEtkinlik!=null && nBirlesen!=null) {
            nBirlesen.setOnClickListener(temelTiklanmaOlayi);
            etkinlikAta(nBirlesen, (Object) nEtkinlik);
        }
    }
    public static void tiklandi(@NonNull View nBirlesen){
        if( nBirlesen==null) return;
        Demet<View, ArrayList<Object>> nDemet=dizideDemetiBulYoksaOlustur(nBirlesen);
        if(nDemet==null) return;
        for(Object nEtkinlik:nDemet.y) {
            if(nEtkinlik instanceof View.OnClickListener)
                ((View.OnClickListener)nEtkinlik).onClick(nBirlesen);
        }

    }
    // endregion

    // region Dokunma Etkinliği
    private static View.OnTouchListener temelDokunmaOlayi=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            EtkinlikMerkezi.dokunuldu(view,motionEvent);
            return true;
        }
    };

    public static void bagla(@NonNull View nBirlesen, @NonNull View.OnTouchListener nEtkinlik){
        nBirlesen.setOnTouchListener(temelDokunmaOlayi);
        etkinlikAta(nBirlesen, (Object) nEtkinlik);

    }
    public static void dokunuldu(@NonNull View nBirlesen, MotionEvent motionEvent){

        Demet<View, ArrayList<Object>> nDemet=dizideDemetiBulYoksaOlustur(nBirlesen);
        if(nDemet==null) return;
        for(Object nEtkinlik:nDemet.y) {
            if(nEtkinlik instanceof View.OnTouchListener)
                ((View.OnTouchListener)nEtkinlik).onTouch(nBirlesen, motionEvent);
        }

    }
    // endregion

    // region Odaktalığın Değişmesi Etkinliği
    private static View.OnFocusChangeListener temelOdaktaliginDegismesiOlayi=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            EtkinlikMerkezi.odaktaligiDegisti(view, b);
        }
    };

    public static void bagla(@NonNull View nBirlesen, @NonNull View.OnFocusChangeListener nEtkinlik){
        nBirlesen.setOnFocusChangeListener(temelOdaktaliginDegismesiOlayi);
        etkinlikAta(nBirlesen, nEtkinlik);
    }
    public static void odaktaligiDegisti(@NonNull View nBirlesen, boolean iOdakta){
        Demet<View, ArrayList<Object>> nDemet=dizideDemetiBulYoksaOlustur(nBirlesen);
        if(nDemet==null) return;
        for(Object nEtkinlik:nDemet.y) {
            if(nEtkinlik instanceof View.OnFocusChangeListener)
                ((View.OnFocusChangeListener)nEtkinlik).onFocusChange(nBirlesen, iOdakta);
        }
    }
    // endregion
}
