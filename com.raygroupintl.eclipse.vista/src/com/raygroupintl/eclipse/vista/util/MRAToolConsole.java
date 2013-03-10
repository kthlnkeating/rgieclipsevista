package com.raygroupintl.eclipse.vista.util;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;

public class MRAToolConsole {
	
	private static MessageConsole messageConsole;

	public static MessageConsole getMessageConsole() {
		if (messageConsole == null) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IConsoleView consoleView = null;
			try {
				consoleView = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			messageConsole = new MessageConsole("MRA Tool Results", null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { messageConsole });
			consoleView.display(messageConsole);
		}
		
		return messageConsole;
	}
}
