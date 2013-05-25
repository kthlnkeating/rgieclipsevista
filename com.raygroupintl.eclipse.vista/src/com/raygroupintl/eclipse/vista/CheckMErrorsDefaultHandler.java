package com.raygroupintl.eclipse.vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import com.raygroupintl.eclipse.vista.toolconsole.MToolsConsoleHandler;
import com.raygroupintl.eclipse.vista.toolconsole.MToolsPatternMatchListener;
import com.raygroupintl.eclipse.vista.util.MRAParamSupply;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SourceCodeFiles;
import com.raygroupintl.m.tool.SourceCodeToParseTreeAdapter;
import com.raygroupintl.m.tool.ToolResult;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.m.tool.routine.error.ErrorTool;
import com.raygroupintl.output.OSTerminal;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

public class CheckMErrorsDefaultHandler extends AbstractHandler {
	protected List<String> getFileNames(TreePath[] selections) {
		if (selections == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (TreePath path : selections) {
			Object lastSegment = path.getLastSegment();
			if (lastSegment instanceof IFile) {
				IFile selected = (IFile) lastSegment;
				String name = selected.getName();
				if (name.endsWith(".m")) {
					name = name.substring(0, name.length()-2);
					result.add(name);
				}
			}
		}
		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}

	protected ToolResult getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		RoutineToolParams p = new RoutineToolParams(pts);
		ErrorTool tool = new ErrorTool(p);
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutines(selectedFileNames);
		ToolResult result = tool.getResult(input);
		return result;
	}

	protected OutputFlags getOutputFlags() {
		OutputFlags fs = new OutputFlags();
		fs.setSkipEmpty(true);
		return fs;
	}
	
	protected void updateFormat(TerminalFormatter formatter) {
		formatter.setTitleWidth(12);
	}
	
	private void writeResult(IProject project, IWorkbenchWindow window, ToolResult result, SourceCodeFiles scf) throws IOException {
		OutputFlags flags = this.getOutputFlags();
		MessageConsole console = MToolsConsoleHandler.getMessageConsole();
		IPatternMatchListener listener = new MToolsPatternMatchListener(project, window, scf);
		console.addPatternMatchListener(listener);
		console.clearConsole();
		MessageConsoleStream os = console.newMessageStream();
		Terminal t = new OSTerminal(os);  //MToolsConsoleOSTerminal(this.project, this.window, os, scf);
		this.updateFormat(t.getTerminalFormatter());
		result.write(t, flags);	
		MToolsConsoleHandler.displayMToolsConsole();
	}

	public void run(IWorkbenchWindow window, Shell shell, IProject project, TreePath[] selections) {
		try {
			List<String> selectedFileNames = this.getFileNames(selections);
			if (selectedFileNames == null) {
				String msg = "No file is selected.";
				MessageDialog.openInformation(shell, "M Tools", msg);
				return;			
			}			
			SourceCodeFiles scf = MRAParamSupply.getSourceCodeFiles(project);
			SourceCodeToParseTreeAdapter pts = new SourceCodeToParseTreeAdapter(scf);
			
			ToolResult result = this.getResult(pts, selectedFileNames);
			this.writeResult(project, window, result, scf);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Unexpected error.";
			MessageDialog.openInformation(shell, "M Tools", msg);
		}
	}

	public void run(IWorkbenchWindow window, Shell shell, IProject project, IFile file) {
		try {
			SourceCodeFiles scf = MRAParamSupply.getSourceCodeFiles(project);
			SourceCodeToParseTreeAdapter pts = new SourceCodeToParseTreeAdapter(scf);
			List<String> fileNames = new ArrayList<String>();
			String name = file.getName();
			if (name.endsWith(".m")) {
				name = name.substring(0, name.length()-2);
				fileNames.add(name);
			} else {
				return;
			}
			ToolResult result = this.getResult(pts, fileNames);
			this.writeResult(project, window, result, scf);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Unexpected error.";
			MessageDialog.openInformation(shell, "M Tools", msg);
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) selection;
			TreePath[] paths = ts.getPaths();
			TreePath[] selections = paths;
			TreePath path = paths[paths.length-1];
			Object lastSegment = path.getLastSegment();
			if (lastSegment instanceof IResource) {
				IProject project = ((IResource) lastSegment).getProject();
				IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
				Shell shell = HandlerUtil.getActiveShell(event);
				this.run(window, shell, project, selections);
			} else if (lastSegment instanceof IAdaptable){
				IAdaptable adaptable = (IAdaptable) lastSegment;
				IFile file = (IFile) adaptable.getAdapter(IFile.class);
				IProject project = file.getProject();
				IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
				Shell shell = HandlerUtil.getActiveShell(event);
				this.run(window, shell, project, file);				
			}
		}
		return null;
	}
}
