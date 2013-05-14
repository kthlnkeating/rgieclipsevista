package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.raygroupintl.eclipse.vista.util.MRAParamSupply;
import com.raygroupintl.eclipse.vista.util.MRAToolConsole;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.quittype.QuitType;
import com.raygroupintl.m.tool.entry.quittype.QuitTypeTool;
import com.raygroupintl.output.OSTerminal;
import com.raygroupintl.output.Terminal;

public class ValidateFanouts extends BaseAction {
	@Override
	public void run(IAction arg0) {
		try {
			List<String> selectedFileNames = this.getFileNames();
			if (selectedFileNames == null) {
				String msg = "No file is selected.";
				MessageDialog.openInformation(this.shell, "M Tools", msg);
				return;			
			}
			IProject project = this.project;
			ParseTreeSupply pts = MRAParamSupply.getParseTreeSupply(project);
			CommonToolParams params = new CommonToolParams(pts);
			QuitTypeTool tool = new QuitTypeTool(params);
			MEntryToolResult<QuitType> result = tool.getResultForRoutines(selectedFileNames);
			OutputFlags flags = new OutputFlags();
			IOConsoleOutputStream os = MRAToolConsole.getMessageConsole().newOutputStream();
			Terminal t = new OSTerminal(os);
			t.getTerminalFormatter().setTitleWidth(12);
			result.write(t, flags);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = this.lastSelected == null ? "Unexpected error: " : "Unexpected error validating fanouts in routine: " + this.lastSelected.getName() + " :";
			MessageDialog.openInformation(this.shell, "Vista", msg);
		}		
	}
}
