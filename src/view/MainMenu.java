package view;

public class MainMenu extends Menu{

    @Override
    protected boolean commands(String command)
    {
        //if logout return true
        return false;
    }
    private boolean startGame(String command)
    {
        return true;
    }
}
