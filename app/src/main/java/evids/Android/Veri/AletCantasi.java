package evids.Android.Veri;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class AletCantasi {

    public Resources Kaynaklar;
    private Activity Sayfa;
    private Window SayfaPenceresi;
    private View SayfaDecorView;

    public AletCantasi(Activity nSayfa) {
        Sayfa=nSayfa;
        Kaynaklar=nSayfa.getResources();
        SayfaPenceresi=nSayfa.getWindow();
        SayfaDecorView=SayfaPenceresi.getDecorView();
    }

    public void SayfayiDegistir(Activity nSayfa) {
        Sayfa=nSayfa;
        Kaynaklar=nSayfa.getResources();
        SayfaPenceresi=nSayfa.getWindow();
        SayfaDecorView=SayfaPenceresi.getDecorView();
    }

    public void UygulamadanCik(){
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void Yenile() {
        Kaynaklar=Sayfa.getResources();
        SayfaPenceresi=Sayfa.getWindow();
        SayfaDecorView=SayfaPenceresi.getDecorView();
    }

    public void YinelenenIslemBaslat(final TimerTask nIslem, int tGecikmeSuresi, int tArasure){
        new Timer().scheduleAtFixedRate(nIslem, (long)tGecikmeSuresi, (long)tArasure);
    }
    public Timer YinelenenIslem(final TimerTask nIslem, int tGecikmeSuresi, int tArasure){
        Timer nZamanlayici=new Timer();
        nZamanlayici.scheduleAtFixedRate(nIslem, (long)tGecikmeSuresi, (long)tArasure);
        return nZamanlayici;
    }
    public void GecikmeliIslemBaslat(final TimerTask nIslem, int tGecikmeSuresi){
        new Timer().schedule(nIslem, (long)tGecikmeSuresi);
    }
    public Timer GecikmeliIslem(final TimerTask nIslem, int tGecikmeSuresi){
        Timer nZamanlayici=new Timer();
        nZamanlayici.schedule(nIslem, (long)tGecikmeSuresi);
        return nZamanlayici;
    }
    public void YinelenenArayuzIslemiBaslat(final Runnable nIslem, int tGecikmeSuresi, int tArasure){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sayfa.runOnUiThread(nIslem);
            }
        }, (long)tGecikmeSuresi, (long)tArasure);
    }
    public Timer YinelenenArayuzIslemi(final Runnable nIslem, int tGecikmeSuresi, int tArasure){
        Timer nZamanlayici=new Timer();
        nZamanlayici.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sayfa.runOnUiThread(nIslem);
            }
        }, (long)tGecikmeSuresi, (long)tArasure);
        return nZamanlayici;
    }
    public void GecikmeliArayuzIslemiBaslat(final Runnable nIslem, int tGecikmeSuresi){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Sayfa.runOnUiThread(nIslem);
            }
        }, (long)tGecikmeSuresi);
    }
    public Timer GecikmeliArayuzIslemi(final Runnable nIslem, int tGecikmeSuresi){
        Timer nZamanlayici=new Timer();
        nZamanlayici.schedule(new TimerTask() {
            @Override
            public void run() {
                Sayfa.runOnUiThread(nIslem);
            }
        }, (long)tGecikmeSuresi);
        return nZamanlayici;
    }

    public void DugmeveBildirimCubuklariniKaldir(){
        int UI_OPTIONS =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        SayfaDecorView.setSystemUiVisibility(UI_OPTIONS);
    }

    public void SayfayiTamEkranYap() {
        SayfaPenceresi.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int UI_OPTIONS =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
        SayfaDecorView.setSystemUiVisibility(UI_OPTIONS);
    }

    public void GoruntulenisiDegistir(int tGoruntulenis){

        Sayfa.setRequestedOrientation(tGoruntulenis);

    }
    public void GoruntulenisiDegistir(Goruntulenis uGoruntulenis){
        int tGoruntulenis=0;
        switch(uGoruntulenis) {
            case Portre:
                tGoruntulenis=ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
                break;
            case Manzara:
                tGoruntulenis=ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
                break;
            default:
                return;
        }
        Sayfa.setRequestedOrientation(tGoruntulenis);
    }

    public enum Goruntulenis {
        Manzara,
        Portre
    }

    public float PikseleDonustur(int tMiktar) {
        return PikseleDonustur((float)tMiktar);
    }
    public float PikseleDonustur(double oMiktar) {
        return PikseleDonustur((float)oMiktar);
    }

    public float PikseleDonustur(float oMiktar){

        DisplayMetrics nOlculer=Kaynaklar.getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, oMiktar, nOlculer);
    }

}
