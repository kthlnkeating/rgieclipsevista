package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.List;

import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;

public class VPAAssumedVariablesTool extends VPATool {
	@Override
	protected OutputFlags getOutputFlags() {
		OutputFlags fs = new OutputFlags();
		fs.setSkipEmpty(true);
		fs.setShowDetail(true);
		return fs;
	}
	
	@Override
	protected MEntryToolResult<AssumedVariables> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		AssumedVariablesToolParams params = new AssumedVariablesToolParams(pts);
		params.addExpected("U");
		params.addExpected("DT");
		params.addExpected("DUZ");
		params.getRecursionSpecification().setDepth(RecursionDepth.LABEL);
		AssumedVariablesTool tool = new AssumedVariablesTool(params);
		MEntryToolResult<AssumedVariables> result = tool.getResultForRoutines(selectedFileNames);
		return result;
	}
}
