package Controller;

public interface Command {
    /**
     * Execute the command
     */
    void doCommand();

    /**
     * Execute the reverse command
     */
    void undoCommand();
}
