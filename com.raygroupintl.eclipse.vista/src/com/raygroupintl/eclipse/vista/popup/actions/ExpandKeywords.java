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

import com.raygroupintl.m.struct.MRefactorSettings;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;

public class ExpandKeywords extends BaseAction {
	
	/**
	 * Constructor for BeautifyAction.
	 */
	public ExpandKeywords() {
		super();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
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
							MTFSupply m = MTFSupply.getInstance(MVersion.CACHE);
							TFRoutine tf = new TFRoutine(m);
							String lastSegment = path.lastSegment();
							String routineName = lastSegment.split("\\.m")[0];
							MRoutineContent content = MRoutineContent.getInstance(routineName, is);
							MRoutine r = tf.tokenize(content);
							is.close();
							MRefactorSettings settings = new MRefactorSettings();
							r.refactor(settings);
							String v = r.toValue().toString();
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



}
