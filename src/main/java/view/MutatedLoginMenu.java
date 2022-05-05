package view;

import com.beust.jcommander.JCommander;

public class MutatedLoginMenu extends MutatedMenu{

    protected JCommander jCommander() {
        JCommander jCommander = new JCommander();
        jCommander.setAllowAbbreviatedOptions(true);
        jCommander.addCommand("city", new MutatedGameMenu.cityCommands());
        jCommander.addCommand("cheat", new MutatedGameMenu.cheatCommands());
        jCommander.addCommand("info", new MutatedGameMenu.infoCommands());
        jCommander.addCommand("map", new MutatedGameMenu.mapCommands());
        jCommander.addCommand("menu", new MutatedGameMenu.menuCommands());
        jCommander.addCommand("unit", new MutatedGameMenu.unitState());
        jCommander.addCommand("increase", new MutatedGameMenu.increase());
        jCommander.addCommand("select", new MutatedGameMenu.tileXAndYFlagSelectUnit());
        jCommander.addCommand("next-turn", new MutatedGameMenu.FreeFlagCommands());
        jCommander.addCommand("capture_city", new MutatedGameMenu.FreeFlagCommands());
        return jCommander;
    }
}
