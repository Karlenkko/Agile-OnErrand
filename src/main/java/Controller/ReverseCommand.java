package Controller;

public class ReverseCommand implements Command {
    private Command cmd;

    /**
     * Create the command reverse to cmd (so that cmd.doCommand corresponds to this.undoCommand, and vice-versa)
     * @param cmd the command to reverse
     */
    public ReverseCommand(Command cmd){
        this.cmd = cmd;
    }

    @Override
    public void doCommand() {
        cmd.undoCommand();
    }

    @Override
    public void undoCommand() {
        cmd.doCommand();
    }
}
