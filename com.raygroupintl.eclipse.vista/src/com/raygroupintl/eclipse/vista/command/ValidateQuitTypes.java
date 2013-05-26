package com.raygroupintl.eclipse.vista.command;

import java.util.List;

import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.quittype.QuitType;
import com.raygroupintl.m.tool.entry.quittype.QuitTypeTool;

public class ValidateQuitTypes extends MToolsCommand {
	@Override
	protected MEntryToolResult<QuitType> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		CommonToolParams params = new CommonToolParams(pts);
		params.getRecursionSpecification().setDepth(RecursionDepth.ALL);
		QuitTypeTool tool = new QuitTypeTool(params);
		MEntryToolResult<QuitType> result = tool.getResultForRoutines(selectedFileNames);
		return result;
	}
}
