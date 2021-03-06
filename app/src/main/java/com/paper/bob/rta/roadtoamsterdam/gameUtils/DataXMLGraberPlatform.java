/*
 * Copyright (c)
 * Road To Amsterdam, RTA
 * Andrei Cristian Bobirica - Matteo Pedron
 * Classe 5IA 2019
 */

package com.paper.bob.rta.roadtoamsterdam.gameUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.paper.bob.rta.roadtoamsterdam.R;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.EngineGame;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.Objects.Background;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.Objects.Base;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.Objects.Ostacolo;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.Objects.Person.Personaggio;
import com.paper.bob.rta.roadtoamsterdam.game.enginePlatform.Objects.Person.Player;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Objects;

public class DataXMLGraberPlatform extends DataXMLGraber {

    private BitmapFactory.Options options;
    public DataXMLGraberPlatform(String file,Context c)
    {
        super(file,c);
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;
    }

    private Node getLevel(String lvName)
    {
        NodeList livelli = radice.getChildNodes();
        for (int i = 0; i < livelli.getLength(); i++)
        {
            Node lv = livelli.item(i);
            if(lv.getAttributes().getNamedItem("name").getNodeValue().equals(lvName)) {
                return lv;
            }
        }
        return livelli.item(0);
    }

    private int[] getGrandezza(String tipo)
    {
        int width,height;
        switch (tipo) {
            case "grande":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_grande_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_grande_height);
                break;
            case "piattaforma_normale":
                width = context.getResources().getDimensionPixelSize(R.dimen.piattaforma_normale_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.piattaforma_normale_height);
                break;
            case "muro":
                width = context.getResources().getDimensionPixelSize(R.dimen.muro_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.muro_height);
                break;
            case "grande_oriz":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_grande_oriz_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_grande_oriz_height);
                break;
            case "normale_oriz":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_oriz_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_oriz_height);
                break;
            case "piccolo_quad":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_quad_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_quad_height);
                break;
            case "normale_quad":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_quad_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_quad_height);
                break;
            case "normale":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_height);
                break;
            case "piccolo":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_height);
                break;
            case "piattaforma_linea":
                width = context.getResources().getDimensionPixelSize(R.dimen.piattaforma_linea_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.piattaforma_linea_height);
                break;
            case "piccolo_vert":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_vert_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_vert_height);
                break;
            case "picss_vert":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_picss_vert_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_picss_vert_height);
                break;
            case "piccolo_oriz":
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_oriz_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_piccolo_oriz_height);
                break;
            case "person":
                width = context.getResources().getDimensionPixelSize(R.dimen.person_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.person_height);
                break;

            default:
                width = context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_width);
                height= context.getResources().getDimensionPixelSize(R.dimen.ostacolo_normale_height);
                break;
        }
        return new int[]{width,height};
    }

    private int[] positionAdapter(int x, int y)
    {
        int[] ret= new int[2];
        int screenWidth = EngineGame.WIDTH;
        int screenHeight = EngineGame.HEIGHT;
        //Position x and y stay for Coordinates in 1280 and 720 pxs
        if(x==0)
        {ret[0]=0;}
        else
        {ret[0]=(x*screenWidth)/1280;}

        if(y==0)
        {ret[1]=0;}
        else
        {ret[1]=(y*screenHeight)/720;}
        return  ret;
    }

    public Background getBackground(String lvName)
    {
        int resId = context.getResources().getIdentifier(getLevel(lvName).getFirstChild().getNextSibling().getFirstChild().getTextContent(), "drawable", context.getPackageName());
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId,options);
        Background bg = new Background(img,0,0);
        Log.i("RTA", "- Background");
        return bg;
    }

    public ArrayList<Ostacolo> getOstacoli(String lvName)
    {
        ArrayList<Ostacolo> ostacoli = new ArrayList<Ostacolo>();
        NodeList ost = getLevel(lvName).getFirstChild().getChildNodes();//Ostacoli Lista
        for (int e = 0; e < ost.getLength(); e++) {
            //IMAGE
            String imgName = ost.item(e).getFirstChild().getTextContent();
            int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId,options);
            //Posizione
            int x = Integer.parseInt(ost.item(e).getAttributes().getNamedItem("x").getNodeValue());
            int y = Integer.parseInt(ost.item(e).getAttributes().getNamedItem("y").getNodeValue());
            //Grandezza Relativa
            String tipo = ost.item(e).getAttributes().getNamedItem("tipo").getNodeValue();
            int width = getGrandezza(tipo)[0];
            int height = getGrandezza(tipo)[1];
            //Individuazione n. frame
            int nFrame = Integer.parseInt(ost.item(e).getAttributes().getNamedItem("frame").getNodeValue());
            //Individuazione se fisico
            boolean fisico = Boolean.parseBoolean(ost.item(e).getAttributes().getNamedItem("fisico").getNodeValue());
            //Adattamento della risoluzione relativa
            x = positionAdapter(x,y)[0];
            y = positionAdapter(x,y)[1];
            //Creazione Ostacolo
            Ostacolo o = new Ostacolo(img, x, y, height, width, nFrame);
            if(Objects.equals(tipo, "endlevel"))
            {
                o.setTipo("endlevel");
                o.setWidth(getGrandezza("normale_oriz")[0]);
                o.setHeight(getGrandezza("normale_oriz")[1]);
            }
            o.setFisico(fisico);
            ostacoli.add(o);
        }

        Log.i("RTA", "- Ostacoli");
        return ostacoli;
    }

    public Bitmap getNotifyImage()
    {
        int resId = context.getResources().getIdentifier("notify", "drawable", context.getPackageName());
        Bitmap imgNot = BitmapFactory.decodeResource(context.getResources(), resId,options);
        return imgNot;
    }

    public ArrayList<Personaggio> getPersonaggi(String lvName)
    {
        ArrayList<Personaggio> personaggi = new ArrayList<>();
        NodeList prs = getLevel(lvName).getFirstChild().getNextSibling().getNextSibling().getChildNodes();
        for (int e = 0; e < prs.getLength(); e++) {
            //Image
            int resId = context.getResources().getIdentifier(prs.item(e).getFirstChild().getTextContent(), "drawable", context.getPackageName());
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId,options);
            //Posizione
            int x = Integer.parseInt(prs.item(e).getAttributes().getNamedItem("x").getNodeValue());
            int y = Integer.parseInt(prs.item(e).getAttributes().getNamedItem("y").getNodeValue());
            //Grandezza Relativa
            int width,height;
            width = context.getResources().getDimensionPixelSize(R.dimen.person_width);
            height= context.getResources().getDimensionPixelSize(R.dimen.person_height);
            //Individuazione n. frame
            int nFrame = Integer.parseInt(prs.item(e).getAttributes().getNamedItem("frame").getNodeValue());
            //Adattamento della risoluzione relativa
            x = positionAdapter(x,y)[0];
            y = positionAdapter(x,y)[1];
            //Individuazione del dialogo
            String dialogo = prs.item(e).getAttributes().getNamedItem("dialogo").getNodeValue();
            //Individuazione notify
            boolean notify = Boolean.parseBoolean(prs.item(e).getAttributes().getNamedItem("notify").getNodeValue());
            //Creazione Personaggio
            Personaggio p = new Personaggio(img, x, y, height, width, nFrame,dialogo,notify);
            personaggi.add(p);
        }
        Log.i("RTA", "- Personaggi");
        return personaggi;
    }

    public Player getPlayer(String lvName)
    {
        Node pl = getLevel(lvName).getFirstChild().getNextSibling().getNextSibling().getNextSibling();
        //Estrapolazione immagini
        //Immagine di base
        String imgName = pl.getFirstChild().getTextContent();
        int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId,options);
        //IMMAGINI LEFT E RIGHT
        int resIdL = context.getResources().getIdentifier(imgName+"left", "drawable", context.getPackageName());
        Bitmap imgL = BitmapFactory.decodeResource(context.getResources(), resIdL,options);
        int resIdR = context.getResources().getIdentifier(imgName+"right", "drawable", context.getPackageName());
        Bitmap imgR = BitmapFactory.decodeResource(context.getResources(), resIdR,options);
        //Immagine Up
        int resIdUp = context.getResources().getIdentifier(imgName+"up", "drawable", context.getPackageName());
        Bitmap imgUp = BitmapFactory.decodeResource(context.getResources(), resIdUp,options);
        //Image UpRight Up left
        int resIdLUp = context.getResources().getIdentifier(imgName+"leftup", "drawable", context.getPackageName());
        Bitmap imgLUp = BitmapFactory.decodeResource(context.getResources(), resIdLUp,options);
        int resIdRUp = context.getResources().getIdentifier(imgName+"rightup", "drawable", context.getPackageName());
        Bitmap imgRUp = BitmapFactory.decodeResource(context.getResources(), resIdRUp,options);

        //Posizione
        int x = Integer.parseInt(pl.getAttributes().getNamedItem("x").getNodeValue());
        int y = Integer.parseInt(pl.getAttributes().getNamedItem("y").getNodeValue());
        //Grandezza Relativa
        int width,height;
        width = context.getResources().getDimensionPixelSize(R.dimen.person_width);
        height= context.getResources().getDimensionPixelSize(R.dimen.person_height);
        //Individuazione n. frame
        int nFrame = Integer.parseInt(pl.getAttributes().getNamedItem("frame").getNodeValue());
        //Adattamento della risoluzione relativa
        x = positionAdapter(x,y)[0];
        y = positionAdapter(x,y)[1];
        //Creazione Player
        Player play = new Player(img,x,y,height,width,nFrame);
        play.setJumpLAnim(imgLUp);
        play.setJumpRAnim(imgRUp);
        play.setLeftAnim(imgL);
        play.setRightAnim(imgR);
        play.setUpAnima(imgUp);
        Log.i("RTA", "- Player");
        return play;
    }

    public Base getBase(String lvName)
    {
        Node pl = getLevel(lvName).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
        int resId = context.getResources().getIdentifier(pl.getFirstChild().getTextContent(), "drawable", context.getPackageName());
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId,options);
        Base base = new Base(img);
        Log.i("RTA", "- Base");
        return base;
    }

    public ArrayList<Sound> getSounds(String lvName)
    {
        ArrayList<Sound> sounds = new ArrayList<>();
        NodeList Nodesounds = getLevel(lvName).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getChildNodes();
        for (int i = 0; i< Nodesounds.getLength();i++)
        {
            String tipoSound =  Nodesounds.item(i).getAttributes().getNamedItem("tipo").getNodeValue();
            boolean loop = Boolean.parseBoolean(Nodesounds.item(i).getAttributes().getNamedItem("loop").getNodeValue());
            String nomeSound = Nodesounds.item(i).getFirstChild().getTextContent();
            Sound s = new Sound(nomeSound,loop,tipoSound);
            s.setSoundPlayer(context);
            sounds.add(s);
        }
        Log.i("RTA", "- Sound");
        return sounds;
    }

    public SoundBG getSoundBG(String lvName)
    {
        NodeList Nodesounds = getLevel(lvName).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getChildNodes();
        for (int i = 0; i< Nodesounds.getLength();i++)
        {
            String tipoSound =  Nodesounds.item(i).getAttributes().getNamedItem("tipo").getNodeValue();
            if(tipoSound.equals("background"))
            {
                String nomeBgSound = Nodesounds.item(i).getFirstChild().getTextContent();
                int resId =context.getResources().getIdentifier(nomeBgSound, "raw", context.getPackageName());
                SoundBG sbg = new SoundBG(resId,context);
                return sbg;
            }
        }
        Log.i("RTA", "- SoundBG");
        return null;
    }
}


