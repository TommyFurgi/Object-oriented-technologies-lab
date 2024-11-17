package command;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private final ObservableList<Command> commandStack = FXCollections
			.observableArrayList();

	private final ObservableList<Command> canceledCommandStack = FXCollections
			.observableArrayList();

	public void executeCommand(Command command) {
		command.execute();
		commandStack.add(command);
		canceledCommandStack.clear();
	}

	public void redo() {
		if (!canceledCommandStack.isEmpty()) {
			Command command =  canceledCommandStack.remove(canceledCommandStack.size() - 1);
			command.redo();
			commandStack.add(command);
		}
	}

	public void undo() {
		if (!commandStack.isEmpty()) {
			Command command =  commandStack.remove(commandStack.size() - 1);
			command.undo();
			canceledCommandStack.add(command);
		}
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}
}
