package com.example.hunter.alliancepicker;

import java.util.ArrayList;

/**
 * Created by Hunter on 9/1/2015.
 */
public class ColorScheme {
    public static ArrayList<ColorScheme> allColorSchemes = new ArrayList();

    static ColorScheme light = new ColorScheme("Light_Monotone", " The color scheme that doesn't \n take sides!", "#99CCCCCC","#AAFFFFFF", "#99EEEEEE", "#ff000000"); //R.color.light_gray, R.color.dark_gray
    static ColorScheme sith = new ColorScheme("Sith", "Join the dark side with this \n color scheme!", "#ff000000", "#99404040" ,"#99353535", "#ffd71a1a");
    static ColorScheme yoda = new ColorScheme("Yoda", "The best color scheme it is!", "#ff1f5c1c","#99e7d8a2","#99c9bc8d","#ff000000");
    static ColorScheme obiwan = new ColorScheme("Obi-Wan", "Follow in the footsteps of \n Obi-Wan Kenobi!","#ffbcc4c9","#991c79ae","#996cb0d7","#ff000000");

    public static ColorScheme selectedColor = light;

    String name;
    String desc;
    String EdgeBgColor;
    String BgColor;
    String BgColor2;
    String TxtColor;

    public ColorScheme(String iName, String iDescription, String ParentBgColor, String BackgroundColor, String BackgroundColor2, String TextColor){
        name = iName;
        desc = iDescription;
        EdgeBgColor = ParentBgColor;
        BgColor = BackgroundColor;
        BgColor2 = BackgroundColor2;
        TxtColor = TextColor;
        allColorSchemes.add(this);
    }

    public static ColorScheme selectColorScheme(String iName){
        for(int cntr = 0; cntr < allColorSchemes.toArray().length; cntr++){
            if(iName.equalsIgnoreCase(allColorSchemes.get(cntr).name)){
                selectedColor = allColorSchemes.get(cntr);
            }
        }
        return selectedColor;
    }
}
