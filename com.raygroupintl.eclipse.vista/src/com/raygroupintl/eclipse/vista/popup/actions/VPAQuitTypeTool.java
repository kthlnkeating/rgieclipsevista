package com.raygroupintl.eclipse.vista.popup.actions;

import java.util.List;

import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.quittype.QuitType;
import com.raygroupintl.m.tool.entry.quittype.QuitTypeTool;

public class VPAQuitTypeTool extends VPAEntryTool<QuitType> {
	@Override
	protected MEntryToolResult<QuitType> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		CommonToolParams params = new CommonToolParams(pts);
		QuitTypeTool tool = new QuitTypeTool(params);
		MEntryToolResult<QuitType> result = tool.getResultForRoutines(selectedFileNames);
		return result;
	}
}
