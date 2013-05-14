package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.List;

import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;

public class VPAAssumedVariablesTool extends VPAEntryTool<AssumedVariables> {
	@Override
	protected MEntryToolResult<AssumedVariables> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		AssumedVariablesToolParams params = new AssumedVariablesToolParams(pts);
		params.addExpected("U");
		params.addExpected("DT");
		params.addExpected("DUZ");
		AssumedVariablesTool tool = new AssumedVariablesTool(params);
		MEntryToolResult<AssumedVariables> result = tool.getResultForRoutines(selectedFileNames);
		return result;
	}
}
