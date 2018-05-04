package evids.Android.Veri;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

public class $$ {
    private ArrayList<$> ogeler;
    public static $$ kur(){
        return new $$();
    }
    public $$(){
        ogeler=new ArrayList<$>();
    }
    private int ogelerdeBul(View nBirlesen){int tI=0;
        for($ nOge:ogeler) {
            if(nOge.birlesen.equals(nBirlesen))
                return tI;
            tI++;
        }
        return -1;
    }
    private int ogelerdeBul(@IdRes int tBirlesenKimligi){
        int tI=0;
        for($ nOge:ogeler) {
            if(nOge.birlesenKimliginiAl()==tBirlesenKimligi)
                return tI;
            tI++;
        }
        return -1;
    }

    public $$ ekle(@NonNull View nBirlesen){
        if(ogelerdeBul(nBirlesen)<0)
            ogeler.add($.kur(nBirlesen));
        return this;
    }
    public $$ ekle(@IdRes int tBirlesenKimligi, @NonNull Activity nProgramBolumu){
        if(ogelerdeBul(tBirlesenKimligi)<0)
            ogeler.add($.kur(tBirlesenKimligi,nProgramBolumu));
        return this;
    }
    public $$ ekle(ArrayList<Integer> kKimlikler, @NonNull Activity nProgramBolumu){
        for(int tKimlik:kKimlikler){
            if(nProgramBolumu.findViewById(tKimlik)!=null)
                ekle(tKimlik, nProgramBolumu);
        }
        return this;
    }
    public $$ ekle(int[] kKimlikler, @NonNull Activity nProgramBolumu){
        for(int tKimlik:kKimlikler){
            if(nProgramBolumu.findViewById(tKimlik)!=null)
                ekle(tKimlik, nProgramBolumu);
        }
        return this;
    }
    public $$ ekle(View[] kBirlesenler){
        for(View nBirlesen:kBirlesenler){
            if(nBirlesen!=null)
                ekle(nBirlesen);
        }
        return this;
    }
    public $$ ekle(ArrayList<View> kBirlesenler){
        for(View nBirlesen:kBirlesenler){
            if(nBirlesen!=null)
                ekle(nBirlesen);
        }
        return this;
    }

    public $$ kaldir(@NonNull View nBirlesen){
        int tKonum=ogelerdeBul(nBirlesen);
        if(tKonum>-1)
            ogeler.remove(tKonum);
        return this;
    }
    public $$ kaldir(int tBirlesenKimligi){
        int tKonum=ogelerdeBul(tBirlesenKimligi);
        if(tKonum>-1)
            ogeler.remove(tKonum);
        return this;
    }
    public $$ kaldir(int[] kKimlikler){
        for(int tKimlik:kKimlikler)
            kaldir(tKimlik);
        return this;
    }
    public $$ kaldir(View[] kBirlesenler){
        for(View nBirlesen:kBirlesenler){
            if(nBirlesen!=null)
                kaldir(nBirlesen);
        }
        return this;
    }

    public $$ etkinlikAta(@NonNull String mAd) {
        for($ nOge:ogeler)
            nOge.etkinlikAta(mAd);
        return this;
    }
    public $$ etkinlikAta(@NonNull Object nEtkinlik) {
        for($ nOge:ogeler)
            nOge.etkinlikAta(nEtkinlik);
        return this;
    }
    public $$ etkinlikKaldir(@NonNull Object nEtkinlik) {
        for($ nOge:ogeler)
            nOge.etkinlikKaldir(nEtkinlik);
        return this;
    }

    public $$ yazi(String mYeniYazi) {
        for($ nOge:ogeler)
            nOge.yazi(mYeniYazi);
        return this;
    }

    public String yazi() {
        if(ogeler.size()>0) {
            return ogeler.get(0).yazi();
        }
        return null;
    }

    public String[] yazilar() {
        String[] kYazilar=new String[ogeler.size()];
        int tI=0;
        for($ nOge:ogeler)
            kYazilar[tI++]=nOge.yazi();
        return kYazilar;
    }

    public $$ gizle() {
        for($ nOge:ogeler)
            nOge.gizle();
        return this;
    }
    public $$ goster() {
        for($ nOge:ogeler)
            nOge.goster();
        return this;
    }
}
