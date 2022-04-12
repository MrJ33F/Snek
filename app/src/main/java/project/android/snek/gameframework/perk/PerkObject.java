package project.android.snek.gameframework.perk;

import android.content.Context;

import project.android.snek.gameframework.GameObject;
import project.android.snek.utils.Vector;

public abstract class PerkObject extends GameObject implements Perk {

    public PerkObject(Context context, Vector initPosition) {
        super(context, initPosition);
    }

}