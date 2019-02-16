package com.paper.bob.rta.roadtoamsterdam.enginePlatform;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import com.paper.bob.rta.roadtoamsterdam.activity.PlatformMainActivity;
import com.paper.bob.rta.roadtoamsterdam.enginePlatform.Objects.Base;
import com.paper.bob.rta.roadtoamsterdam.enginePlatform.Objects.GameObject;
import com.paper.bob.rta.roadtoamsterdam.enginePlatform.Objects.Ostacolo;
import com.paper.bob.rta.roadtoamsterdam.enginePlatform.Objects.Person.Personaggio;
import com.paper.bob.rta.roadtoamsterdam.enginePlatform.Objects.Person.Player;
import com.paper.bob.rta.roadtoamsterdam.gameUtils.Sound;

import java.util.ArrayList;

public class Controller{

    //Variabili con cui verificare le collisioni
    private Player play;
    private ArrayList<GameObject> objColl;
    private Base base;
    private PlatformMainActivity plActivity;
    private ArrayList<Sound> sounds;

    //Vettori sensore accelerometro
    private float sensorX = 0;
    private float sensorY = 0;
    private float sensorZ = 0;


    //VETTORI DI MOVIMENTO Player
    private final int dx = 18;
    private final int dy = 22;
    private final int dDown = 20;

    private boolean mRight=false,mLeft=false,mUp=false,mDown=true;
    /**Variabile uping che indica se si sta ancora effetuando l'azione di salto oppure no
     * inolte la variabile dTiime indica il dELAY TIME con cui il salto deve essere interroto*
     * La variabile numSalti indica il numero massimo di salti che il player può fare
     * La variabile alreadyUp serve per capire se il numero di salti sono stati completati*/
    private boolean uping=false;
    private final int dTime = 550;
    private final int numSalti = 2;
    private int alreadyUp = numSalti;

    private boolean debugMode = false;

    /**
     * Costruttore di Default
     */
    public Controller(){
        play =null;objColl=null;
        base =null;plActivity=null;}
    /**
     * Metodo che ritorna il valore di dX
     * Il valore di dX è il vettore di movimento verso  destra e sinistra, cioè di movimento
     * @return vettore di movimento
     */
    public int getDX(){return dx;}
    /**
     * Metodo che ritorna il valore di dY
     * Il valore di dY è il vettore di movimento verso l'alto, cioè di salto
     * @return vettore di movimento
     */
    public int getDY(){return dy;}
    /**
     * Metodo che ritorna il valore di dDown
     * Il valore di dDown è il vettore di movimento verso il Basso, cioè di caduta
     * @return vettore di movimento
     */
    public int getDDown(){return dDown;}
    /**
     * Metodo che serve per settare la possibilità di movimento del Player a destra
     * @param m boolean che rappresenta la possibilità di movimento
     */
    public void setMRight(boolean m) {mRight = m;}
    /**
     * Metodo che serve per settare la possibilità di movimento del Player a sinistra
     * @param m boolean che rappresenta la possibilità di movimento
     */
    public void setMLeft(boolean m) {mLeft = m;}
    /**
     * Metodo che serve per settare la possibilità di movimento del Player in basso
     * @param m boolean che rappresenta la possibilità di movimento
     */
    public void setMDown(boolean m) {mDown = m;}
    /**
     * Metodo che serve per settare la possibilità di movimento del Player in alto
     * A differenza di tutti gli altri metodo set Movimento, questo ha un algoritmo che interferisce con la
     * variabile mUp solo per un certo periodo di tempo, il che provoca il fatto che il salto è solo una azione spontanea, durevole, e inspammabile.
     * Cioè che mentre si decide di saltare, e si preme il pulsante per saltare, una volta iniziata l'azione di salto si deve aspettare che finisca, per
     * Ripoter saltare, per che l'azione finisca devono passare il delayMillis impostato nel Handler.
     * Per capire se è passato il delay o no si fa appoggio sulla variabile uping, che indica se si sta ancora nel bel mezzo della azione salto
     * Oppure si ha finito l'azione salto.
     * @param m boolean che rappresenta la possibilità di movimento
     */
    public void setMUp(boolean m)
    {
        if(!uping  &&  alreadyUp<numSalti) {
            if(!debugMode)
                alreadyUp++;
            mUp = m;
            mDown=false;
            uping = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mUp = false;
                    mDown=true;
                    uping= false;
                }
            }, dTime);
        }
    }
    /**
     * Metodo che ritorna se il Player può muoversi in basso
     * @return boolean che rappresenta la possibilità di movimento
     */
    public boolean getMDown()
    {
        sounds.get(0).play();
        boolean col = verCol(0,dDown);
        if(col){alreadyUp=numSalti-1;}
        return (!col && mDown);
    }
    /**
     * Metodo che ritorna se il Player può muoversi in basso IN MANIERA perfetta, senza lasciare pixel tra esso e il ostacolo
     * @return int vettore con cui il Player si deve spostare sull'asse Y per ridurre al minimo la distanza tra esso e l'ostacolo
     */
    public int getMDownPerfect()
    {
        int ret= 0;
        if(!uping) {
            ret = dDown;
            while (verCol(0, ret)) {
                ret--;
            }
        }
        if(ret>0)return ret;
        return 0;
    }
    /**
     * Metodo che ritorna se il Player può muoversi a destra
     * @return boolean che rappresenta la possibilità di movimento
     */
    public boolean getMRight() {return (!verCol(dx,0)&& mRight);}
    /**
     * Metodo che ritorna se il Player può muoversi a sinistra
     * @return boolean che rappresenta la possibilità di movimento
     */
    public boolean getMLeft() {return (!verCol(-dx,0)&& mLeft);}
    /**
     * Metodo che ritorna se il Player può muoversi in alto
     * @return boolean che rappresenta la possibilità di movimento
     */
    public boolean getMUp() {return (!verCol(0,-dy)&& mUp);}
    /**
     * Metodo setPlayer che serve a settare il Player, Successivamente si uttilizzerà il player per verificare la sua collisione con altri oggetti o con la Base
     * Il Rapporto tra Controller e Player è 1 a 1, esiste una istanza di Player nel Controller e d eesiste una istanza di Controller nel Player.
     * Questo perchè il controller ha bisogno delle coordinate di Player per verificare la sua collisione, e decidere i suoi possibili movimenti.
     * Questo perchè il Player ha bisogno delle decisioni del Controller, per effetuare i movimenti.
     * @param pl Player di gioco.
     */
    public void setPlayer(Player pl) {this.play = pl;}
    /**
     * Metodo che setta gli objCol, questo metodo serve a settare tutti gli oggetti con cui il Player si potrà collidere.
     * Una volta memorizzati nel Controller gli Oggetti con cui si potrà collidere , successivi controlli verificheranno le collisioni con essi.
     * @param o ArrayList di GameObject, Lista di Oggetti con cui si potrà collidere il Player
     */
    public void setObjColl(ArrayList<GameObject> o) {this.objColl = o;}
    /**
     * Metodo che confronta due Oggetti Rect e verifica se è avvenuta una collisione fra i due.
     * Il metodo ritorna un valore booleano che specifica se è avvenuta una collisione tra i due.
     * Non specifica su che lato o da che parte, solo che le aree dei due rettangoli si sono sovrapposte.
     * @param a GameObject a da confrontare con base
     * @param b GameObject base da confrontare con a
     * @return valore booleano, se true significa che è avvenuta una collisione, se false non è avvenuta una collisione
     */
    private boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }
    /**
     * Metodo che confronta tutti gli GameObject Fisici con il Player, e ritorna se in un ipotetico movimento collidono oppure no.
     * Il metodo contronta tutti gli GameObject objCol, oggetti preventivamente creati che rappresentato tutti gli GameOnject Fisici e che possono collidere.
     * Il ipotetico movimento viene ipotizzato tramite i due vettori dx e dy, che sono parametri aggiuntivi alla effettiva posizione del Player.
     * Se il Player con i parametri aggiuntivi effetua una collisione, non gli viene permesso di muoversi, in pratica viene ritornato True, cioè collisione effetuata.
     * Se return true, effetua una collisione, se false no.
     * Sarà poi il Controller che guardando il risultato permetterà al Player di muoversi oppure no.
     * @param dx Vettore di movimento sull'asse X, viene aggiunto in caso di ipotetico movimento a sinistra o a destra
     * @param dy Vettore di movimento sull'asse Y, viene aggiunto in caso di ipotetico movimento in giù o su
     * @return valore booleano che indica se nel ipotetico movimento avviene una collisione o meno.
     */
    private boolean verCol(int dx, int dy)
    {
        boolean ret = false;
        for(GameObject g : objColl) {
            if(g.getWidth()>-50 && g.getX()<EngineGame.WIDTH && g.getHeight()>-50 && g.getY()<EngineGame.HEIGHT+50) {
                ret = (collision(new Ostacolo(null, play.getX() + dx, play.getY() + dy, play.getHeight(), play.getWidth(), 0), g));
                if (ret){
                    if(g.getTipo().equals("Personaggio"))
                    {
                        Personaggio p = (Personaggio) g;
                        Log.i("RTA","Not: "+p.getNotify());
                        this.avviaDialogo(p.getDialogo());
                        p.setFisico(false);
                        p.setNotify(false);
                        objColl.remove(g);
                    }
                    break;
                }
            }
        }
        return ret;
    }
    /**
     * Metodo che Setta l'activity principale dei livelli platform
     * L'activity si chiama PlatformMainActivity, la si viene settata dentro questa classe perchè si potrebbe aver bisogno
     * di richiamare alcuni metodo della activity, legati al cambio delle activity, e lo spostamento su un'altra activity.
     * @param pl PlayerMainACtivity Activity sa settare sulla classe Controller
     */
    public void setPlActivity(PlatformMainActivity pl)
    {plActivity = pl;}
    /**
     * Metodo Avvia dialogo, richiama la medesima funzione di PlayerMainActivity, e richiamandola gli passa anche il parametro String d
     * Che serve per individuare quale dialogo avviare.
     * PlayerMainAcitivty successivamente avvia un'altra Activity con il dialogo impostato.
     * @param d Nome del dialogo
     */
    private void avviaDialogo(String d)
    {
        plActivity.avviaDialogo(d);
    }


    public void setSensorX(float sensorX) {
        this.sensorX = sensorX;
    }

    public void setSensorY(float sensorY) {
        this.sensorY = sensorY;
    }

    public void setSensorZ(float sensorZ) {
        this.sensorZ = sensorZ;
    }

    public float getSensorZ() {
        return sensorZ;
    }

    public float getSensorY() {
        return sensorY;
    }

    public float getSensorX() {
        return sensorX;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }
}