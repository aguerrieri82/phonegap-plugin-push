package com.ictmakers.eventmarker;

import java.util.Date;

/**
 * Created by Andrea on 26/04/2017.
 */

public class SetPosizioneUtente implements Command {

    @Override
    public String getTypeName() {
        return "EventMarker.Entities.SetPosizioneUtente";
    }

    public double Longitudine;

    public double Latitudine;

    public double Precisione;

    public Date Data;
}
