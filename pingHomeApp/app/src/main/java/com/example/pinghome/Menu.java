package com.example.pinghome;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public String title;
    public String subtitle;

    public Menu(String title, String subtitle){
        this.title = title;
        this.subtitle = subtitle;
    }

    public static ArrayList<Menu> getMenus(){
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(new Menu("Settings", "The settings page"));
        return menus;
    }
}
