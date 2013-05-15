package com.raygroupintl.eclipse.vista.popup.actions;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.raygroupintl.eclipse.vista.util.MRAParamSupply;
import com.raygroupintl.eclipse.vista.util.MRAToolConsole;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ToolResult;
import com.raygroupintl.output.OSTerminal;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

public abstract class VPATool extends BaseAction {
	protected abstract ToolResult getResult(ParseTreeSupply pts, List<String> selectedFileNames);
	
	protected OutputFlags getOutputFlags() {
		OutputFlags fs = new OutputFlags();
		fs.setSkipEmpty(true);
		return fs;
	}
	
	protected void updateFormat(TerminalFormatter formatter) {
		formatter.setTitleWidth(12);
	}
	
	private void writeResult(ToolResult result) throws IOException {
		OutputFlags flags = this.getOutputFlags();
		IOConsoleOutputStream os = MRAToolConsole.getMessageConsole().newOutputStream();
		Terminal t = new OSTerminal(os);
		this.updateFormat(t.getTerminalFormatter());
		result.write(t, flags);		
	}
	
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
			
			ToolResult result = this.getResult(pts, selectedFileNames);
			this.writeResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Unexpected error.";
			MessageDialog.openInformation(this.shell, "M Tools", msg);
		}		
	}
}
