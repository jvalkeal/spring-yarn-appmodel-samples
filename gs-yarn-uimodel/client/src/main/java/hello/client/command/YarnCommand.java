package hello.client.command;

import java.util.List;

public interface YarnCommand {

	String execute(List<String> appArgs);

	boolean isHelp();

}
