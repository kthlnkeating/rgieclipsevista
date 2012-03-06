package com.raygroupintl.eclipse.vista.popup.actions;

import java.io.InputStream;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.raygroupintl.vista.mtoken.Routine;

public class ExpandKeywords implements IObjectActionDelegate {

	private Shell shell;
	private IFile lastSelected;
	
	/**
	 * Constructor for BeautifyAction.
	 */
	public ExpandKeywords() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (this.lastSelected != null) {
			try {
				ITextFileBufferManager mgr = FileBuffers.getTextFileBufferManager();
				if (mgr != null) {
					IPath path = this.lastSelected.getFullPath();
					if (path != null) {
						ITextFileBuffer buffer = mgr.getTextFileBuffer(path, LocationKind.IFILE);
						if (buffer != null) {							
							InputStream is = this.lastSelected.getContents();
							Routine r = Routine.getInstance(is);
							is.close();
							r.beautify();
							String v = r.getStringValue();
							buffer.getDocument().set(v);							
						}												
					}					
				}
				/*
				InputStream is = this.lastSelected.getContents();
				Routine r = Routine.getInstance(is);
				is.close();
				r.beautify();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				r.write(out);
				ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
				this.lastSelected.setContents(in, false, true, null);				
				*/
			} catch (Exception t) {
				String msg = this.lastSelected == null ? "Unexpected error: " : "Unexpected error beautifying " + this.lastSelected.getName() + " :";
				MessageDialog.openInformation(this.shell, "Vista", msg);
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.lastSelected = null;
		if (selection instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) selection;
			TreePath[] paths = ts.getPaths();
			TreePath path = paths[paths.length-1];
			Object lastSegment = path.getLastSegment();
			if (lastSegment instanceof IFile) {
				this.lastSelected = (IFile) lastSegment;
			}
		}
	}

}
