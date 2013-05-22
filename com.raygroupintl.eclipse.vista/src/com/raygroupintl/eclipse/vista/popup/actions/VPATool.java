package com.raygroupintl.eclipse.vista.popup.actions;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.raygroupintl.eclipse.vista.toolconsole.MToolsConsoleHandler;
import com.raygroupintl.eclipse.vista.toolconsole.MToolsPatternMatchListener;
import com.raygroupintl.eclipse.vista.util.MRAParamSupply;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SourceCodeFiles;
import com.raygroupintl.m.tool.SourceCodeToParseTreeAdapter;
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
	
	private void writeResult(ToolResult result, SourceCodeFiles scf) throws IOException {
		OutputFlags flags = this.getOutputFlags();
		MessageConsole console = MToolsConsoleHandler.getMessageConsole();
		IPatternMatchListener listener = new MToolsPatternMatchListener(this.project, this.window, scf);
		console.addPatternMatchListener(listener);
		console.clearConsole();
		MessageConsoleStream os = console.newMessageStream();
		Terminal t = new OSTerminal(os);  //MToolsConsoleOSTerminal(this.project, this.window, os, scf);
		this.updateFormat(t.getTerminalFormatter());
		result.write(t, flags);	
		MToolsConsoleHandler.displayMToolsConsole();
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
			SourceCodeFiles scf = MRAParamSupply.getSourceCodeFiles(project);
			SourceCodeToParseTreeAdapter pts = new SourceCodeToParseTreeAdapter(scf);
			
			ToolResult result = this.getResult(pts, selectedFileNames);
			this.writeResult(result, scf);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Unexpected error.";
			MessageDialog.openInformation(this.shell, "M Tools", msg);
		}		
	}
}
