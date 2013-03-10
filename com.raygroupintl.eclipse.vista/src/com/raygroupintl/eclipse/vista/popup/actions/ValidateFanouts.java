package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;

import com.raygroupintl.eclipse.vista.util.MRAToolConsole;
import com.raygroupintl.eclipse.vista.views.MRAToolConsoleView;
import com.raygroupintl.output.OutputTerm;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.tools.ReturnTypeTool;

public class ValidateFanouts extends BaseAction {

	@Override
	public void run(IAction arg0) {
		if (lastSelected == null)
			return;
		
		try {
			IPath path = this.lastSelected.getFullPath();
			if (path != null) {
				
				//clear originalConsole
				//MRAToolConsoleView.clearText();
				MRAToolConsole.getMessageConsole().clearConsole();
				
				String lastSegment = path.lastSegment();
				String routineName = lastSegment.split("\\.m")[0];
				List<String> routineNames = new ArrayList<String>(1);
				routineNames.add(routineName);
				
				
				//Terminal outputTerm = new OutputTerm(MRAToolConsoleView.out);
				Terminal outputTerm = new OutputTerm(MRAToolConsole.getMessageConsole().newOutputStream());
				
//				MessageConsoleStream stream = myConsole.newMessageStream();
//				Terminal outputTerm = new OutputTerm(MRAToolConsoleView.out);
//				stream.println("Hello World");
				
				ReturnTypeTool rtt = new ReturnTypeTool(outputTerm, routineNames);
				rtt.run();
			}
		} catch (Exception t) {
			t.printStackTrace();
			String msg = this.lastSelected == null ? "Unexpected error: " : "Unexpected error validating fanouts in routine: " + this.lastSelected.getName() + " :";
			MessageDialog.openInformation(this.shell, "Vista", msg);
		}
		
	}

}
