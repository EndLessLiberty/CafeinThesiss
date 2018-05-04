package evids.Android.Veri;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
public class $ {

    //private static ArrayList<Demet<Integer,View>> birlesenler=new ArrayList<Demet<Integer,View>>();

    public static $ kur(@IdRes int tBirlesenKimligi, @NonNull Activity nProgramBolumu){
        return new $(tBirlesenKimligi,nProgramBolumu);
    }
    public static $ kur(View nBirlesen){
        return new $(nBirlesen);
    }

    /*private static View birlesenlerdeBul(int tBirlesenKimligi){
        for(int tI=0, tIU=birlesenler.size(); tI<tIU; tI++){
            Demet<Integer, View> nDemet=birlesenler.get(tI);
            if(nDemet.x==tBirlesenKimligi){
                return nDemet.y;
            }
        }
        return null;
    }*/


    public View birlesen=null;
    private int birlesenKimligi=-1;
    public int birlesenKimliginiAl(){
       return birlesenKimligi;
    }
    public $(@NonNull View nBirlesen) {
        birlesen=nBirlesen;
    }

    public $(@IdRes int tBirlesenKimligi, Activity nProgramBolumu){
        //birlesen=birlesenlerdeBul(tBirlesenKimligi);
        //if(birlesen==null) {
            birlesen=nProgramBolumu.findViewById(tBirlesenKimligi);
            birlesenKimligi=tBirlesenKimligi;
            //birlesenler.add(new Demet<Integer, View>(tBirlesenKimligi,birlesen));
        //}
    }

    public $ goster(){
        birlesen.setVisibility(View.VISIBLE);
        return this;
    }
    public $ gizle(){
        birlesen.setVisibility(View.INVISIBLE);
        return this;
    }

    public void sil(){
        ViewParent nUstBirlesen=birlesen.getParent();
        if(nUstBirlesen!=null && nUstBirlesen instanceof ViewGroup)
            ((ViewGroup)nUstBirlesen).removeView(birlesen);
    }

    public $ gecisleriTemizle(){
        birlesen.clearAnimation();
        return this;
    }

    public $ odaktanCikar(){
        birlesen.clearFocus();
        return this;
    }

    public $ odakla(){
        birlesen.requestFocus();
        return this;
    }

    public boolean odakta(){
        return birlesen.hasFocus();
    }

    public $ etkinlikAta(@NonNull Object nEtkinlik){

        if(nEtkinlik instanceof View.OnClickListener)
            EtkinlikMerkezi.bagla(birlesen, (View.OnClickListener) nEtkinlik);
        else if(nEtkinlik instanceof View.OnTouchListener)
            EtkinlikMerkezi.bagla(birlesen, (View.OnTouchListener) nEtkinlik);
        else if(nEtkinlik instanceof View.OnFocusChangeListener)
            EtkinlikMerkezi.bagla(birlesen,(View.OnFocusChangeListener) nEtkinlik);

        return this;
    }
    public $ etkinlikAta(@NonNull String mEtkinlikAdi){
        EtkinlikMerkezi.bagla(birlesen, mEtkinlikAdi);
        return this;
    }
    public $ etkinlikKaldir(@NonNull Object nEtkinlik){

        EtkinlikMerkezi.ayir(birlesen,nEtkinlik);

        return this;
    }
    public $ yazi(String mYeniYazi){
        if(birlesen instanceof EditText)
            ((EditText)birlesen).setText(mYeniYazi);
        else if(birlesen instanceof TextView)
            ((TextView)birlesen).setText(mYeniYazi);
        return this;
    }

    @IdRes
    public int kimlik(){
        return birlesen.getId();
    }

    public void kimlik(int tYeniKimlik){
        birlesen.setId(tYeniKimlik);
    }

    public String yazi(){
        if(birlesen instanceof EditText)
            return ((EditText)birlesen).getText().toString();
        else if(birlesen instanceof TextView)
            return ((TextView)birlesen).getText().toString();
        return null;
    }

    public $ matlas(int tSure, @NonNull Runnable lSonucIslerligi, boolean iGecisKuyrugunuTemizle) {
        tSure=Math.max(66, tSure);
        if(iGecisKuyrugunuTemizle) {
            birlesen.clearAnimation();
        }
        birlesen.animate().alpha(1.0f).withEndAction(lSonucIslerligi);
        return this;
    }
    public $ matlas(int tSure, boolean iGecisKuyrugunuTemizle) {
        tSure=Math.max(66, tSure);
        if(iGecisKuyrugunuTemizle) {
            birlesen.clearAnimation();
        }
        birlesen.animate().alpha(1.0f);
        return this;
    }
    public $ matlas(int tSure) {
        tSure=Math.max(66, tSure);
        birlesen.animate().alpha(1.0f);
        return this;
    }
    public $ matlas(int tSure,  @NonNull Runnable lSonucIslerligi) {
        tSure=Math.max(66, tSure);
        birlesen.animate().alpha(1.0f).withEndAction(lSonucIslerligi);
        return this;
    }

    public $ saydamlas(int tSure, @NonNull Runnable lSonucIslerligi, boolean iGecisKuyrugunuTemizle) {
        tSure=Math.max(66, tSure);
        if(iGecisKuyrugunuTemizle) {
            birlesen.clearAnimation();
        }
        birlesen.animate().alpha(0.0f).withEndAction(lSonucIslerligi);
        return this;
    }
    public $ saydamlas(int tSure, boolean iGecisKuyrugunuTemizle) {
        tSure=Math.max(66, tSure);
        if(iGecisKuyrugunuTemizle) {
            birlesen.clearAnimation();
        }
        birlesen.animate().alpha(0.0f);
        return this;
    }
    public $ saydamlas(int tSure) {
        tSure=Math.max(66, tSure);
        birlesen.animate().alpha(0.0f);
        return this;
    }
    public $ saydamlas(int tSure,  @NonNull Runnable lSonucIslerligi) {
        tSure=Math.max(66, tSure);
        birlesen.animate().alpha(0.0f).withEndAction(lSonucIslerligi);
        return this;
    }

    public ImageView resimBirleseni() {
        return (ImageView) birlesen;
    }
    public Spinner acilirListe() {
        return (Spinner) birlesen;
    }

    public ConstraintLayout kisitAlani() {
        return (ConstraintLayout) birlesen;
    }
}