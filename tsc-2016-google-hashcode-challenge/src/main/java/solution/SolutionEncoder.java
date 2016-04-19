package solution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import commands.Command;

public class SolutionEncoder {

    private final BufferedWriter writer;
    private boolean consoleOutput = true;
    
    public SolutionEncoder(String file) throws IOException{
        writer = new BufferedWriter(new FileWriter(file));
    }
    
    public void setConsoleOutput(boolean console){
        consoleOutput = console;
    }
    
    private void writeCommand(Command command) throws IOException{
        writer.write(command.encode()+"\n");
    }
    
    public void writeCommands(List<Command> commands) throws IOException{
        writer.write(commands.size()+"\n");
        
        for(Command command: commands){
            writeCommand(command);
        }
    }
    
    public void close() throws IOException{
        writer.close();
    }
    
}
