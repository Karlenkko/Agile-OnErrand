package Controller;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> list;
    private int currentIndex;

    public ListOfCommands(){
        currentIndex = -1;
        list = new LinkedList<>();
    }

    /**
     * Add command c to this
     * @param c the command to add
     */
    public void add(Command c){
        int i = currentIndex+1;
        while(i<list.size()){
            list.remove(i);
        }
        currentIndex++;
        list.add(currentIndex, c);
        c.doCommand();
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo)
     */
    public void undo(){
        if (currentIndex >= 0){
            Command cde = list.get(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Permanently remove the last added command (this command cannot be reinserted again with redo)
     */
    public void cancel(){
        if (currentIndex >= 0){
            Command cde = list.get(currentIndex);
            list.remove(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Reinsert the last command removed by undo
     */
    public void redo(){
        if (currentIndex < list.size()-1){
            currentIndex++;
            Command cde = list.get(currentIndex);
            cde.doCommand();
        }
    }

    /**
     * Permanently remove all commands from the list
     */
    public void reset(){
        currentIndex = -1;
        list.clear();
    }
}
