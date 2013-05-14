package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class BaseAction implements IObjectActionDelegate {
	protected IFile lastSelected;
	protected TreePath[] selections;
	protected Shell shell;
	protected IProject project;

	protected List<String> getFileNames() {
		if (this.selections == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (TreePath path : this.selections) {
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
		
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {		
		this.lastSelected = null;
		this.selections = null;
		this.project = null;
		if (selection instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) selection;
			TreePath[] paths = ts.getPaths();
			this.selections = paths;
			TreePath path = paths[paths.length-1];
			Object lastSegment = path.getLastSegment();
			if (lastSegment instanceof IFile) {
				this.lastSelected = (IFile) lastSegment;
			}
			if (lastSegment instanceof IResource) {
				this.project = ((IResource) lastSegment).getProject();
			}
		}
	}
}
