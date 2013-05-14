package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.raygroupintl.eclipse.vista.util.MRAParamSupply;
import com.raygroupintl.eclipse.vista.util.MRAToolConsole;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.occurance.Occurance;
import com.raygroupintl.m.tool.routine.occurance.OccuranceTool;
import com.raygroupintl.m.tool.routine.occurance.OccuranceToolParams;
import com.raygroupintl.output.OSTerminal;
import com.raygroupintl.output.Terminal;

public class VPAOccurancesTool extends BaseAction {
	protected ResultsByRoutine<Occurance, List<Occurance>> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		OccuranceToolParams p = new OccuranceToolParams(pts);	
		OccuranceTool tool = new OccuranceTool(p);
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutines(selectedFileNames);
		ResultsByRoutine<Occurance, List<Occurance>> result = tool.getResult(input);
		return result;
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
			
			ResultsByRoutine<Occurance, List<Occurance>> result = this.getResult(pts, selectedFileNames);
			
			OutputFlags flags = new OutputFlags();
			IOConsoleOutputStream os = MRAToolConsole.getMessageConsole().newOutputStream();
			Terminal t = new OSTerminal(os);
			t.getTerminalFormatter().setTitleWidth(12);
			result.write(t, flags);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Unexpected error.";
			MessageDialog.openInformation(this.shell, "M Tools", msg);
		}		
	}
}
